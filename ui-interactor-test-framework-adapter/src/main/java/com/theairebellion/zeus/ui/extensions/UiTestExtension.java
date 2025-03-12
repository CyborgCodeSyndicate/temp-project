package com.theairebellion.zeus.ui.extensions;

import com.theairebellion.zeus.framework.allure.CustomAllureListener;
import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.decorators.DecoratorsFactory;
import com.theairebellion.zeus.framework.log.LogTest;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.quest.SuperQuest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.framework.util.ObjectFormatter;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUIServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import com.theairebellion.zeus.ui.validator.TableAssertionFunctions;
import com.theairebellion.zeus.ui.validator.TableAssertionTypes;
import com.theairebellion.zeus.util.reflections.ReflectionUtil;
import com.theairebellion.zeus.validator.registry.AssertionRegistry;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.v85.network.Network;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.theairebellion.zeus.framework.allure.StepType.TEAR_DOWN;
import static com.theairebellion.zeus.framework.allure.StepType.TEST_EXECUTION;
import static com.theairebellion.zeus.framework.util.TestContextManager.getSuperQuest;
import static com.theairebellion.zeus.ui.config.UiConfigHolder.getUiConfig;
import static com.theairebellion.zeus.ui.config.UiFrameworkConfigHolder.getUiFrameworkConfig;
import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.*;

public class UiTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback,
        TestExecutionExceptionHandler {

    private static final String SELENIUM_PACKAGE = "org.openqa.selenium";
    private static final String UI_MODULE_PACKAGE = "theairebellion.zeus.ui";

    static {
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.TABLE_NOT_EMPTY, TableAssertionFunctions::validateTableNotEmpty);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.TABLE_ROW_COUNT, TableAssertionFunctions::validateTableRowCount);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.EVERY_ROW_CONTAINS_VALUES, TableAssertionFunctions::validateEveryRowContainsValues);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.TABLE_DOES_NOT_CONTAIN_ROW, TableAssertionFunctions::validateTableDoesNotContainRow);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ALL_ROWS_ARE_UNIQUE, TableAssertionFunctions::validateAllRowsAreUnique);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.NO_EMPTY_CELLS, TableAssertionFunctions::validateNoEmptyCells);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.COLUMN_VALUES_ARE_UNIQUE, TableAssertionFunctions::validateColumnValuesAreUnique);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.TABLE_DATA_MATCHES_EXPECTED, TableAssertionFunctions::validateTableDataMatchesExpected);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ROW_NOT_EMPTY, TableAssertionFunctions::validateRowNotEmpty);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ROW_CONTAINS_VALUES, TableAssertionFunctions::validateRowContainsValues);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ALL_CELLS_ENABLED, TableAssertionFunctions::validateAllCellsEnabled);
        AssertionRegistry.registerCustomAssertion(TableAssertionTypes.ALL_CELLS_CLICKABLE, TableAssertionFunctions::validateAllCellsClickable);
    }


    @Override
    public void beforeTestExecution(final ExtensionContext context) {
        context.getTestMethod().ifPresent(method -> {
            processInterceptRequestsAnnotation(context, method);
            registerAssertionConsumer(context);
            processAuthenticateViaUiAsAnnotation(context, method);
            registerCustomServices(context);
        });
    }

    private void registerCustomServices(ExtensionContext context) {

        Consumer<SuperQuest> questConsumer = UiTestExtension::postQuestCreationRegisterCustomServices;
        addQuestConsumer(context, questConsumer);
    }


    private void processInterceptRequestsAnnotation(ExtensionContext context, Method method) {
        Optional.ofNullable(method.getAnnotation(InterceptRequests.class))
                .ifPresent(intercept -> {
                    String[] urlsForIntercepting = intercept.requestUrlSubStrings();
                    Consumer<SuperQuest> questConsumer =
                            quest -> postQuestCreationIntercept(quest, urlsForIntercepting);
                    addQuestConsumer(context, questConsumer);
                });
    }

    private static void postQuestCreationIntercept(final SuperQuest quest, final String[] urlsForIntercepting) {
        SmartWebDriver artifact = quest.artifact(UIServiceFluent.class, SmartWebDriver.class);
        WebDriver driver = unwrapDriver(artifact.getOriginal());
        if (driver instanceof ChromeDriver) {
            DevTools chromeDevTools = ((ChromeDriver) driver).getDevTools();
            chromeDevTools.createSession();
            chromeDevTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            chromeDevTools.addListener(Network.responseReceived(), entry -> {
                int statusCode = entry.getResponse().getStatus();
                String url = entry.getResponse().getUrl();
                ApiResponse response = new ApiResponse(url, statusCode);

                if (checkUrl(urlsForIntercepting, url)) {
                    try {
                        String body = chromeDevTools.send(Network.getResponseBody(entry.getRequestId())).getBody();

                        if (body != null && body.length() > 10000) {
                            response.setBody(String.format(
                                    "Response body truncated. Original length: %d characters. " +
                                            "First 100 characters: %s",
                                    body.length(),
                                    body.substring(0, 100)
                            ));
                        } else {
                            response.setBody(body);
                        }
                    } catch (Exception e) {
                        response.setBody("Error retrieving response body: " + e.getMessage());
                    }
                }
                addResponseInStorage(quest.getStorage(), response);
            });
        } else {
            throw new IllegalArgumentException("Intercepting Backend Requests is only acceptable with Chrome browser");
        }
    }



    private void processAuthenticateViaUiAsAnnotation(ExtensionContext context, Method method) {
        Optional.ofNullable(method.getAnnotation(AuthenticateViaUiAs.class))
                .ifPresent(login -> {
                    try {
                        ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
                        DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);
                        LoginCredentials credentials = login.credentials().getDeclaredConstructor().newInstance();
                        Consumer<SuperQuest> questConsumer =
                                quest -> postQuestCreationLogin(quest, decoratorsFactory, credentials.username(),
                                        credentials.password(), login.type(), login.cacheCredentials());
                        addQuestConsumer(context, questConsumer);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                             NoSuchMethodException e) {
                        throw new RuntimeException("Failed to instantiate login credentials", e);
                    }
                });
    }


    private void registerAssertionConsumer(ExtensionContext context) {
        Consumer<SuperQuest> questConsumer = quest -> postQuestCreationAssertion(quest, context.getDisplayName());
        addQuestConsumer(context, questConsumer);
    }


    private void addQuestConsumer(ExtensionContext context, Consumer<SuperQuest> questConsumer) {
        var consumers = getOrCreateQuestConsumers(context);
        consumers.add(questConsumer);
    }


    @SuppressWarnings("unchecked")
    private List<Consumer<SuperQuest>> getOrCreateQuestConsumers(ExtensionContext context) {
        return (List<Consumer<SuperQuest>>) context.getStore(ExtensionContext.Namespace.GLOBAL)
                .getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, key -> new ArrayList<>());
    }


    private static void addResponseInStorage(Storage storage, ApiResponse apiResponse) {
        List<ApiResponse> responses = storage.sub(UI).get(RESPONSES, new ParameterizedTypeReference<>() {
        });
        if (responses == null) {
            responses = new ArrayList<>();
        }
        responses.add(apiResponse);
        storage.sub(UI).put(RESPONSES, apiResponse);

        LogUI.extended("Response added to storage: URL={}, Status={}", apiResponse.getUrl(), apiResponse.getStatus());
    }


    private static boolean checkUrl(String[] urlsForIntercepting, String url) {
        return Arrays.stream(urlsForIntercepting).anyMatch(url::contains);
    }


    @Override
    public void afterTestExecution(ExtensionContext context) {
        ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
        DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);
        WebDriver driver = getWebDriver(decoratorsFactory, context);
        if (context.getExecutionException().isEmpty() && getUiFrameworkConfig().makeScreenshotOnPassedTest()) {
            LogUI.warn("Test failed. Taking screenshot for: {}", context.getDisplayName());
            takeScreenshot(driver, context.getDisplayName(), getSuperQuest(context));
        }
        List<Object> responses = getSuperQuest(context).getStorage().sub(UI).getAllByClass(RESPONSES, Object.class);
        if (!responses.isEmpty()) {
            String formattedResponses = new ObjectFormatter().formatResponses(Collections.singletonList(responses));
            Allure.addAttachment("Intercepted Requests", "text/html",
                    new ByteArrayInputStream(formattedResponses.getBytes(StandardCharsets.UTF_8)), ".html");
        }
        driver.close();
        driver.quit();
        LogUI.info("WebDriver closed successfully.");
    }


    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        ApplicationContext appCtx = SpringExtension.getApplicationContext(context);
        DecoratorsFactory decoratorsFactory = appCtx.getBean(DecoratorsFactory.class);

        WebDriver driver = getWebDriver(decoratorsFactory, context);
        takeScreenshot(driver, context.getDisplayName(), getSuperQuest(context));
        throw throwable;
    }

    private static void postQuestCreationRegisterCustomServices(SuperQuest quest) {
        Class<? extends UIServiceFluent> customUIServiceFluentClass =
                ReflectionUtil.findClassThatExtendsClass(UIServiceFluent.class, getUiConfig().projectPackage());
        if (customUIServiceFluentClass != null) {
            try {
                SmartWebDriver driver = quest.artifact(UIServiceFluent.class, SmartWebDriver.class);
                quest.registerWorld(customUIServiceFluentClass, customUIServiceFluentClass.getDeclaredConstructor(
                        SmartWebDriver.class, SuperQuest.class).newInstance(driver, quest));
                quest.removeWorld(UIServiceFluent.class);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

    }


    private static void postQuestCreationLogin(SuperQuest quest, DecoratorsFactory decoratorsFactory,
                                               final String username, final String password,
                                               final Class<? extends BaseLoginClient> type, boolean cache) {
        quest.getStorage().sub(UI).put(USERNAME, username);
        quest.getStorage().sub(UI).put(PASSWORD, password);
        UIServiceFluent uiServiceFluent = quest.enters(UIServiceFluent.class);

        try {
            BaseLoginClient baseLoginClient = type.getDeclaredConstructor().newInstance();
            baseLoginClient.login(decoratorsFactory.decorate(uiServiceFluent, SuperUIServiceFluent.class), username,
                    password, cache);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate or execute login client", e);
        }
    }


    private static void postQuestCreationAssertion(SuperQuest quest, String testName) {
        SmartWebDriver smartWebDriver = quest.artifact(UIServiceFluent.class, SmartWebDriver.class);
        quest.getSoftAssertions().registerObjectForPostErrorHandling(SmartWebDriver.class, smartWebDriver);

        CustomSoftAssertion.registerCustomAssertion(
                SmartWebDriver.class,
                (assertionError, driver) -> takeScreenshot(unwrapDriver(driver.getOriginal()),
                        "soft_assert_failure_" + testName, quest),
                stackTrace -> Arrays.stream(stackTrace)
                        .anyMatch(element -> element.getClassName().contains(SELENIUM_PACKAGE) ||
                                element.getClassName().contains(UI_MODULE_PACKAGE))
        );
    }


    private static void takeScreenshot(WebDriver driver, String testName, SuperQuest superQuest) {
        if(CustomAllureListener.isParentStepActive(TEST_EXECUTION)) {
            CustomAllureListener.stopParentStep();
            CustomAllureListener.startParentStep(TEAR_DOWN);
        }
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(testName, new ByteArrayInputStream(screenshotBytes));
            LogTest.info("Screenshot taken and stored for: " + testName);
        } catch (Exception e) {
            LogUI.error("Failed to take screenshot for test '{}': {}", testName, e.getMessage());
        }
    }


    private WebDriver getWebDriver(DecoratorsFactory decoratorsFactory, ExtensionContext context) {
        Quest quest = (Quest) context.getStore(ExtensionContext.Namespace.GLOBAL).get(StoreKeys.QUEST);
        SuperQuest superQuest = decoratorsFactory.decorate(quest, SuperQuest.class);
        SmartWebDriver artifact = superQuest.artifact(UIServiceFluent.class, SmartWebDriver.class);
        return unwrapDriver(artifact.getOriginal());
    }


    private static WebDriver unwrapDriver(WebDriver maybeProxy) {
        try {
            Method getOriginalMethod = maybeProxy.getClass().getMethod("getOriginal");
            return (WebDriver) getOriginalMethod.invoke(maybeProxy);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Failed to unwrap WebDriver", e);
        }
    }


}