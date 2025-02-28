package com.theairebellion.zeus.ui.authentication;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUIServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public abstract class BaseLoginClient implements LoginClient {

    private static final Map<LoginKey, SessionInfo> userLoginMap = new ConcurrentHashMap<>();
    public static final List<SmartWebDriver> driverToKeep = new ArrayList<>();
    private static final String GET_LOCAL_STORAGE = "return JSON.stringify(window.localStorage);";
    private static final String UPDATE_LOCAL_STORAGE = "let data = %s; for (let key in data) { window.localStorage.setItem(key, data[key]); }";
    private static String urlAfterLogging;


    @Override
    public void login(final SuperUIServiceFluent<?> uiService, final String username, final String password,
                      final boolean cache) {
        LoginKey loginKey = new LoginKey(username, password, this.getClass());

        if (!cache) {
            performLoginAndCache(uiService, loginKey, username, password);
        } else {
            synchronized (userLoginMap) {
                if (userLoginMap.get(loginKey) == null) {
                    performLoginAndCache(uiService, loginKey, username, password);
                } else {
                    restoreSession(uiService, userLoginMap.get(loginKey));
                }
            }
        }
    }


    public Optional<SessionInfo> getAuthentication(final LoginKey loginKey) {
        return Optional.ofNullable(userLoginMap.get(loginKey));
    }


    private void performLoginAndCache(SuperUIServiceFluent<?> uiService, LoginKey loginKey, String username,
                                      String password) {
        loginImpl(uiService, username, password);
        SmartWebDriver smartWebDriver = uiService.getDriver();
        smartWebDriver.setKeepDriverForSession(true);
        driverToKeep.add(smartWebDriver);

        try {
            smartWebDriver.getWait()
                .until(ExpectedConditions.presenceOfElementLocated(successfulLoginElementLocator()));
        } catch (Exception e) {
            //todo create custom exception
            throw new RuntimeException("Logging was not successful");
        }

        urlAfterLogging = smartWebDriver.getCurrentUrl();

        WebDriver driver = smartWebDriver.getOriginal();

        Set<Cookie> cookies = driver.manage().getCookies();
        String localStorage = executeJavaScript(driver, GET_LOCAL_STORAGE);

        userLoginMap.put(loginKey, new SessionInfo(cookies, localStorage));
    }


    private void restoreSession(SuperUIServiceFluent<?> uiService, SessionInfo sessionInfo) {
        SmartWebDriver smartWebDriver = uiService.getDriver();
        WebDriver driver = smartWebDriver.getOriginal();

        smartWebDriver.get(urlAfterLogging);
        smartWebDriver.manage().window().maximize();
        smartWebDriver.manage().deleteAllCookies();

        sessionInfo.getCookies().forEach(cookie -> driver.manage().addCookie(cookie));

        executeJavaScript(driver, String.format(UPDATE_LOCAL_STORAGE, sessionInfo.getLocalStorage()));

        smartWebDriver.get(urlAfterLogging);

        try {
            smartWebDriver.getWait()
                .until(ExpectedConditions.presenceOfElementLocated(successfulLoginElementLocator()));
        } catch (Exception e) {
            //todo create custom exception
            throw new RuntimeException("Logging was not successful");
        }

    }


    private String executeJavaScript(WebDriver driver, String script) {
        if (driver instanceof JavascriptExecutor jsExecutor) {
            return (String) jsExecutor.executeScript(script);
        } else {
            throw new IllegalStateException("Driver does not support JavaScript execution");
        }
    }


    protected abstract <T extends UIServiceFluent<?>> void loginImpl(T uiService, String username, String password);


    protected abstract By successfulLoginElementLocator();

}
