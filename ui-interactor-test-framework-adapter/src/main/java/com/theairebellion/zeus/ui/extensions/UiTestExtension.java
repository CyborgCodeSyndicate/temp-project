package com.theairebellion.zeus.ui.extensions;

import com.theairebellion.zeus.framework.assertion.CustomSoftAssertion;
import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.ui.annotations.AuthenticateViaUiAs;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.authentication.BaseLoginClient;
import com.theairebellion.zeus.ui.authentication.LoginCredentials;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import io.qameta.allure.Allure;
import manifold.ext.rt.api.Jailbreak;
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
import org.springframework.core.ParameterizedTypeReference;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.PASSWORD;
import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.RESPONSES;
import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;
import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.USERNAME;

public class UiTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback,
                                            TestExecutionExceptionHandler {


    private static final String SELENIUM_PACKAGE = "org.openqa.selenium";
    private static final String UI_MODULE_PACKAGE = "theairebellion.zeus.ui";


    @Override
    public void beforeTestExecution(final ExtensionContext context) {
        context.getTestMethod().ifPresent(method -> {
            processInterceptRequestsAnnotation(context, method);
            registerAssertionConsumer(context);
            processAuthenticateViaUiAsAnnotation(context, method);
        });
    }


    private void processInterceptRequestsAnnotation(ExtensionContext context, Method method) {
        Optional.ofNullable(method.getAnnotation(InterceptRequests.class))
            .ifPresent(intercept -> {
                String[] urlsForIntercepting = intercept.requestUrlSubStrings();
                Consumer<Quest> questConsumer = quest -> postQuestCreationIntercept(quest, urlsForIntercepting);
                addQuestConsumer(context, questConsumer);
            });
    }


    private void processAuthenticateViaUiAsAnnotation(ExtensionContext context, Method method) {
        Optional.ofNullable(method.getAnnotation(AuthenticateViaUiAs.class))
            .ifPresent(login -> {
                try {
                    LoginCredentials credentials = login.credentials().getDeclaredConstructor().newInstance();
                    Consumer<Quest> questConsumer = quest -> postQuestCreationLogin(quest, credentials.username(),
                        credentials.password(), login.type(), login.cacheCredentials());
                    addQuestConsumer(context, questConsumer);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException("Failed to instantiate login credentials", e);
                }
            });
    }


    private void registerAssertionConsumer(ExtensionContext context) {
        Consumer<Quest> questConsumer = quest -> postQuestCreationAssertion(quest, context.getDisplayName());
        addQuestConsumer(context, questConsumer);
    }


    private void addQuestConsumer(ExtensionContext context, Consumer<Quest> questConsumer) {
        var consumers = getOrCreateQuestConsumers(context);
        consumers.add(questConsumer);
    }


    @SuppressWarnings("unchecked")
    private List<Consumer<Quest>> getOrCreateQuestConsumers(ExtensionContext context) {
        return (List<Consumer<Quest>>) context.getStore(ExtensionContext.Namespace.GLOBAL)
                                           .getOrComputeIfAbsent(StoreKeys.QUEST_CONSUMERS, key -> new ArrayList<>());
    }


    private static void addResponseInStorage(Storage storage, ApiResponse apiResponse) {

        List<ApiResponse> responses = storage.sub(UI).get(RESPONSES, new ParameterizedTypeReference<>() {
        });
        if (responses == null) {
            responses = new ArrayList<>();
        }
        responses.add(apiResponse);
        storage.sub(UI).put(RESPONSES, responses);
    }


    private static boolean checkUrl(String[] urlsForIntercepting, String url) {
        return Arrays.stream(urlsForIntercepting).anyMatch(url::contains);
    }


    @Override
    public void afterTestExecution(ExtensionContext context) {
        WebDriver driver = getWebDriver(context);
        if (context.getExecutionException().isPresent()) {
            takeScreenshot(driver, context.getDisplayName());
        }
        driver.close();
        driver.quit();
    }


    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        WebDriver driver = getWebDriver(context);
        takeScreenshot(driver, context.getDisplayName());
        System.err.println("Exception during UI test: " + throwable.getMessage());
        throw throwable;
    }


    private static void postQuestCreationIntercept(final @Jailbreak Quest quest, final String[] urlsForIntercepting) {
        SmartWebDriver artifact = quest.artifact(UIServiceFluent.class, SmartWebDriver.class);
        WebDriver driver = unwrapDriver(artifact.getOriginal());
        if (driver instanceof ChromeDriver) {
            DevTools chromeDevTools = ((ChromeDriver) driver).getDevTools();
            chromeDevTools.createSession();
            chromeDevTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            chromeDevTools.addListener(Network.responseReceived(), entry -> {
                ApiResponse response = new ApiResponse(entry.getResponse().getUrl(), entry.getResponse().getStatus());
                if (checkUrl(urlsForIntercepting, entry.getResponse().getUrl())) {
                    String body = chromeDevTools.send(Network.getResponseBody(entry.getRequestId())).getBody();
                    response.setBody(body);
                }
                addResponseInStorage(quest.getStorage(), response);
            });
        } else {
            throw new IllegalArgumentException("Intercepting Backend Requests is only acceptable with Chrome browser");
        }
    }


    private static void postQuestCreationLogin(@Jailbreak Quest quest, final String username, final String password,
                                               final Class<? extends BaseLoginClient> type, boolean cache) {
        quest.getStorage().sub(UI).put(USERNAME, username);
        quest.getStorage().sub(UI).put(PASSWORD, password);
        UIServiceFluent uiServiceFluent = quest.enters(UIServiceFluent.class);

        try {
            BaseLoginClient baseLoginClient = type.getDeclaredConstructor().newInstance();
            baseLoginClient.login(uiServiceFluent, username, password, cache);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Failed to instantiate or execute login client", e);
        }
    }


    private static void postQuestCreationAssertion(@Jailbreak Quest quest, String testName) {
        SmartWebDriver smartWebDriver = quest.artifact(UIServiceFluent.class, SmartWebDriver.class);
        quest.getSoftAssertions().registerObjectForPostErrorHandling(SmartWebDriver.class, smartWebDriver);

        CustomSoftAssertion.registerCustomAssertion(
            SmartWebDriver.class,
            (assertionError, driver) -> takeScreenshot(unwrapDriver(driver.getOriginal()),
                "soft_assert_failure_" + testName),
            stackTrace -> Arrays.stream(stackTrace)
                              .anyMatch(element -> element.getClassName().contains("org.openqa.selenium"))
        );
    }


    private static void takeScreenshot(WebDriver driver, String testName) {
        try {
            TakesScreenshot screenshot = (TakesScreenshot) driver;
            byte[] screenshotBytes = screenshot.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(testName, new ByteArrayInputStream(screenshotBytes));
            System.out.println("Screenshot taken for: " + testName);
        } catch (Exception e) {
            System.err.println("Failed to take screenshot: " + e.getMessage());
        }
    }


    private WebDriver getWebDriver(ExtensionContext context) {
        @Jailbreak Quest quest = (Quest) context.getStore(ExtensionContext.Namespace.GLOBAL).get(StoreKeys.QUEST);
        SmartWebDriver artifact = quest.artifact(UIServiceFluent.class, SmartWebDriver.class);
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


    private static boolean checkForUiError(StackTraceElement[] stackTraceElements) {
        return Arrays.stream(stackTraceElements)
                   .anyMatch(stackTraceElement -> {
                       String packageName = stackTraceElement.getClassName();
                       return packageName.contains(SELENIUM_PACKAGE) || packageName.contains(
                           UI_MODULE_PACKAGE);
                   });
    }

}