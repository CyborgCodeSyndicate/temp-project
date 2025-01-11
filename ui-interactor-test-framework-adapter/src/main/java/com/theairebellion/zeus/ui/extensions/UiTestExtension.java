package com.theairebellion.zeus.ui.extensions;

import com.theairebellion.zeus.framework.quest.Quest;
import com.theairebellion.zeus.framework.storage.Storage;
import com.theairebellion.zeus.framework.storage.StoreKeys;
import com.theairebellion.zeus.ui.annotations.InterceptRequests;
import com.theairebellion.zeus.ui.components.interceptor.ApiResponse;
import com.theairebellion.zeus.ui.selenium.UIDriver;
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
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.RESPONSES;
import static com.theairebellion.zeus.ui.extensions.StorageKeysUi.UI;

public class UiTestExtension implements BeforeTestExecutionCallback, AfterTestExecutionCallback,
                                            TestExecutionExceptionHandler {

    @Override
    public void beforeTestExecution(final ExtensionContext context) throws Exception {
        Optional<Method> testMethod = context.getTestMethod();
        if (testMethod.isPresent()) {
            InterceptRequests annotation = testMethod.get().getAnnotation(InterceptRequests.class);
            if (annotation != null) {
                String[] urlsForIntercepting = annotation.requestUrlSubStrings();
                Consumer<Quest> questConsumer = (@Jailbreak Quest quest) -> postQuestCreation(quest,
                    urlsForIntercepting);

                @SuppressWarnings("unchecked")
                var consumers = (java.util.List<Consumer<Quest>>) context.getStore(ExtensionContext.Namespace.GLOBAL)
                                                                      .getOrComputeIfAbsent(
                                                                          StoreKeys.QUEST_CONSUMERS,
                                                                          key -> new java.util.ArrayList<Consumer<Quest>>()
                                                                      );
                consumers.add(questConsumer);
            }
        }
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
        return urlsForIntercepting.stream()
                   .anyMatch(url::contains);
    }


    @Override
    public void afterTestExecution(ExtensionContext context) {
        WebDriver driver = getWebDriver(context);
        if (context.getExecutionException().isPresent()) {
            takeScreenshot(driver, context.getDisplayName());
            // System.out.println("URL at failure: " + driver.getCurrentUrl());
        }
        driver.close();
        driver.quit();
        // System.out.println("UI Test ended: " + context.getDisplayName());
    }


    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        WebDriver driver = getWebDriver(context);
        takeScreenshot(driver, context.getDisplayName());
        System.err.println("Exception during UI test: " + throwable.getMessage());
        throw throwable;
    }


    private static void postQuestCreation(final @Jailbreak Quest quest, final String[] urlsForIntercepting) {
        UIDriver artifact = quest.artifact(UIServiceFluent.class, UIDriver.class);
        WebDriver driver = unwrapDriver(artifact.getDriver());
        if (driver instanceof ChromeDriver) {
            DevTools chromeDevTools = ((ChromeDriver) driver).getDevTools();
            chromeDevTools.createSession();

            chromeDevTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty()));
            chromeDevTools.addListener(
                Network.responseReceived(),
                entry -> {
                    ApiResponse response = new ApiResponse(entry.getResponse().getUrl(),
                        entry.getResponse().getStatus());
                    if (checkUrl(urlsForIntercepting, entry.getResponse().getUrl())) {
                        String body = chromeDevTools.send(Network.getResponseBody(entry.getRequestId()))
                                          .getBody();
                        response.setBody(body);
                    }
                    addResponseInStorage(quest.getStorage(), response);
                });
        } else {
            throw new IllegalArgumentException(
                "Intercepting Backend Requests is only acceptable with Chrome browser ");
        }
    }


    private void takeScreenshot(WebDriver driver, String testName) {
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
        @Jailbreak Quest quest = (Quest) context.getStore(ExtensionContext.Namespace.GLOBAL).get(
            StoreKeys.QUEST.getKey());
        UIDriver artifact = quest.artifact(UIServiceFluent.class, UIDriver.class);
        WebDriver driver = artifact.getDriver();
        return unwrapDriver(driver);
    }


    private static WebDriver unwrapDriver(WebDriver maybeProxy) {
        Method getOriginalMethod;
        try {
            getOriginalMethod = maybeProxy.getClass().getMethod("getOriginal");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        try {
            return (WebDriver) getOriginalMethod.invoke(maybeProxy);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

}
