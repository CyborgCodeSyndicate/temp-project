package com.theairebellion.zeus.ui.authentication;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUIServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UIServiceFluent;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Abstract base class for implementing UI-based authentication clients.
 * <p>
 * This class provides a mechanism for logging in through a UI service, caching session data,
 * and restoring sessions using stored cookies and local storage data. Subclasses must implement
 * the {@link #loginImpl(UIServiceFluent, String, String)} method to define specific login logic.
 * </p>
 *
 * <p>The authentication flow consists of:</p>
 * <ul>
 *     <li>Performing login through UI interaction.</li>
 *     <li>Waiting for a successful login indication.</li>
 *     <li>Caching session cookies and local storage data for reuse.</li>
 *     <li>Restoring an existing session if caching is enabled.</li>
 * </ul>
 *
 * @author Cyborg Code Syndicate
 */
public abstract class BaseLoginClient implements LoginClient {

    /**
     * A thread-safe map to store user sessions based on login credentials.
     */
    private static final Map<LoginKey, SessionInfo> userLoginMap = new ConcurrentHashMap<>();

    //todo: javaDocs
    public static final List<SmartWebDriver> driverToKeep = new ArrayList<>();

    /**
     * JavaScript command to retrieve local storage data.
     */
    private static final String GET_LOCAL_STORAGE = "return JSON.stringify(window.localStorage);";

    /**
     * JavaScript command to restore local storage data.
     */
    private static final String UPDATE_LOCAL_STORAGE = "let data = %s; for (let key in data) { window.localStorage.setItem(key, data[key]); }";

    /**
     * URL of the page after a successful login.
     */
    private static String urlAfterLogging;

    /**
     * Logs in the user and optionally caches session data for reuse.
     *
     * @param uiService The UI service used for login interactions.
     * @param username  The username of the user.
     * @param password  The password of the user.
     * @param cache     Whether to cache the session for future reuse.
     */
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

    /**
     * Retrieves cached authentication session info if available.
     *
     * @param loginKey The key representing the login credentials.
     * @return An {@link Optional} containing the session information if found.
     */
    public Optional<SessionInfo> getAuthentication(final LoginKey loginKey) {
        return Optional.ofNullable(userLoginMap.get(loginKey));
    }

    /**
     * Performs login and caches session cookies and local storage data.
     *
     * @param uiService The UI service used for login interactions.
     * @param loginKey  The key representing the login credentials.
     * @param username  The username of the user.
     * @param password  The password of the user.
     */
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

    /**
     * Restores a previously cached session by injecting cookies and local storage data.
     *
     * @param uiService   The UI service used for browser interactions.
     * @param sessionInfo The stored session information.
     */
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

    /**
     * Executes JavaScript in the browser.
     *
     * @param driver The WebDriver instance.
     * @param script The JavaScript command to execute.
     * @return The result of the JavaScript execution.
     */
    private String executeJavaScript(WebDriver driver, String script) {
        if (driver instanceof JavascriptExecutor jsExecutor) {
            return (String) jsExecutor.executeScript(script);
        } else {
            throw new IllegalStateException("Driver does not support JavaScript execution");
        }
    }

    /**
     * Performs the actual login operation.
     * <p>
     * This method should be implemented by subclasses to define the specific UI interactions
     * required to log in.
     * </p>
     *
     * @param uiService The UI service for interacting with the login page.
     * @param username  The username for authentication.
     * @param password  The password for authentication.
     * @param <T>       The type of UI service being used.
     */
    protected abstract <T extends UIServiceFluent<?>> void loginImpl(T uiService, String username, String password);

    /**
     * Provides the locator for an element that indicates a successful login.
     * <p>
     * This method should return a {@link By} locator that identifies an element that only
     * appears after a successful login, such as a dashboard or a user profile menu.
     * </p>
     *
     * @return A Selenium {@link By} locator for the post-login verification element.
     */
    protected abstract By successfulLoginElementLocator();

}
