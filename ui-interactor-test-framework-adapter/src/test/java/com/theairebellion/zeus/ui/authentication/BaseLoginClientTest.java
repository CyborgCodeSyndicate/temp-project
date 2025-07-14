package com.theairebellion.zeus.ui.authentication;

import com.theairebellion.zeus.ui.exceptions.AuthenticationUiException;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUiServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@SuppressWarnings("all")
@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class BaseLoginClientTest {

   private static final String USERNAME = "testUser";
   private static final String PASSWORD = "testPassword";
   private static final String CURRENT_URL = "https://example.com/dashboard";
   private static final String LOCAL_STORAGE_VALUE = "{\"token\":\"abc123\"}";
   private static final By SUCCESS_LOCATOR = By.id("successElement");

   @Mock
   private SuperUiServiceFluent<?> mockUiService;
   @Mock
   private SmartWebDriver mockSmartWebDriver;
   @Mock
   private WebDriver.Options mockOptions;
   @Mock
   private WebDriverWait mockWait;

   private TestLoginClient loginClient;
   private Set<Cookie> cookies;

   @BeforeEach
   void setUp() throws Exception {
      // clear userLoginMap
      Field userMap = BaseLoginClient.class.getDeclaredField("userLoginMap");
      userMap.setAccessible(true);
      ((ConcurrentHashMap<?, ?>) userMap.get(null)).clear();

      // clear urlAfterLoggingMap
      Field urlMap = BaseLoginClient.class.getDeclaredField("urlAfterLoggingMap");
      urlMap.setAccessible(true);
      ((ConcurrentHashMap<?, ?>) urlMap.get(null)).clear();

      // clear driverToKeep *as a List*, not a Set*
      Field drivers = BaseLoginClient.class.getDeclaredField("driverToKeep");
      drivers.setAccessible(true);
      @SuppressWarnings("unchecked")
      List<SmartWebDriver> list = (List<SmartWebDriver>) drivers.get(null);
      list.clear();

      // your original test setup…
      cookies = new HashSet<>();
      cookies.add(new Cookie("sessionId", "123456"));

      loginClient = spy(new TestLoginClient());
      // (you already stub successfulLoginElementLocator in the TestLoginClient impl)

      when(mockUiService.getDriver()).thenReturn(mockSmartWebDriver);
      when(mockSmartWebDriver.getWait()).thenReturn(mockWait);
      when(mockSmartWebDriver.getCurrentUrl()).thenReturn(CURRENT_URL);
   }

   @Test
   void loginWithoutCache_ShouldPerformLogin() {
      WebDriver mockWebDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) mockWebDriver;

      when(mockSmartWebDriver.getOriginal()).thenReturn(mockWebDriver);
      when(mockWebDriver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(mockCondition);

         when(mockWait.until(any(ExpectedCondition.class))).thenReturn(true);
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         loginClient.login(mockUiService, USERNAME, PASSWORD, false);

         verify(loginClient).loginImpl(eq(mockUiService), eq(USERNAME), eq(PASSWORD));
         verify(mockOptions).getCookies();
      }
   }

   @Test
   void loginWithCache_FirstTime_ShouldPerformAndCacheLogin() {
      WebDriver mockWebDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) mockWebDriver;

      when(mockSmartWebDriver.getOriginal()).thenReturn(mockWebDriver);
      when(mockWebDriver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(mockCondition);

         when(mockWait.until(any(ExpectedCondition.class))).thenReturn(true);
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         loginClient.login(mockUiService, USERNAME, PASSWORD, true);

         verify(loginClient).loginImpl(eq(mockUiService), eq(USERNAME), eq(PASSWORD));
         verify(mockOptions).getCookies();

         LoginKey key = new LoginKey(USERNAME, PASSWORD, TestLoginClient.class);
         Optional<SessionInfo> sessionInfo = loginClient.getAuthentication(key);
         assertTrue(sessionInfo.isPresent());
         assertEquals(cookies, sessionInfo.get().getCookies());
         assertEquals(LOCAL_STORAGE_VALUE, sessionInfo.get().getLocalStorage());
      }
   }

   @Test
   void loginWithCache_SubsequentLogin_ShouldRestoreSession() {
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

      when(mockSmartWebDriver.getOriginal()).thenReturn(driver);
      when(mockSmartWebDriver.manage()).thenReturn(mockOptions);
      when(driver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(mockCondition);

         when(mockWait.until(any(ExpectedCondition.class))).thenReturn(true);
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         // first login
         loginClient.login(mockUiService, USERNAME, PASSWORD, true);

         reset(loginClient);
         doCallRealMethod().when(loginClient).login(any(), anyString(), anyString(), anyBoolean());
         doCallRealMethod().when(loginClient).getAuthentication(any());

         // second login should *not* call loginImpl but should re-inject cookies
         loginClient.login(mockUiService, USERNAME, PASSWORD, true);

         verify(loginClient, never()).loginImpl(any(), anyString(), anyString());
         verify(mockOptions, atLeastOnce()).addCookie(any(Cookie.class));
      }
   }

   @Test
   void loginWithCache_SessionRestoreFailed_ShouldThrowException() {
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

      when(mockSmartWebDriver.getOriginal()).thenReturn(driver);
      when(driver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      ExpectedCondition<Object> successCond = mock(ExpectedCondition.class);
      ExpectedCondition<Object> failCond = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         // first login success
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(successCond);
         when(mockWait.until(eq(successCond))).thenReturn(true);
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());
         loginClient.login(mockUiService, USERNAME, PASSWORD, true);

         // second login: restore fails on wait
         reset(mockWait);
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(failCond);
         doThrow(new RuntimeException("Element not found")).when(mockWait).until(eq(failCond));

         RuntimeException ex = assertThrows(RuntimeException.class, () ->
               loginClient.login(mockUiService, USERNAME, PASSWORD, true)
         );
         assertEquals("Restoring session was not successful", ex.getMessage());
      }
   }

   @Test
   void login_InitialLoginFailed_ShouldThrowException() {
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

      when(mockSmartWebDriver.getOriginal()).thenReturn(driver);
      when(driver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      ExpectedCondition<Object> failCond = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(failCond);

         doThrow(new RuntimeException("Element not found"))
               .when(mockWait).until(eq(failCond));
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         RuntimeException ex = assertThrows(RuntimeException.class, () ->
               loginClient.login(mockUiService, USERNAME, PASSWORD, false)
         );
         assertEquals("Logging in was not successful", ex.getMessage());
      }
   }

   @Test
   void executeJavaScript_DriverNotJsExecutor_ShouldThrowException() {
      WebDriver nonJsDriver = mock(WebDriver.class);

      ExpectedCondition<Object> cond = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         when(mockSmartWebDriver.getOriginal()).thenReturn(nonJsDriver);
         when(nonJsDriver.manage()).thenReturn(mockOptions);
         when(mockOptions.getCookies()).thenReturn(cookies);

         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(any()))
               .thenReturn(cond);

         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
               loginClient.login(mockUiService, USERNAME, PASSWORD, false)
         );
         assertEquals("Driver does not support JavaScript execution", ex.getMessage());
      }
   }

   @Test
   void getAuthentication_NoStoredSession_ShouldReturnEmpty() {
      LoginKey nonExistent = new LoginKey("no", "pw", TestLoginClient.class);
      Optional<SessionInfo> result = loginClient.getAuthentication(nonExistent);
      assertFalse(result.isPresent());
   }

   @Test
   void loginConcurrently_ShouldHandleRaceConditions() throws InterruptedException {
      WebDriver driver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

      when(mockSmartWebDriver.getOriginal()).thenReturn(driver);
      when(driver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      ExpectedCondition<Object> cond = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(cond);
         when(mockWait.until(any(ExpectedCondition.class))).thenReturn(true);
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         Thread t1 = new Thread(() -> loginClient.login(mockUiService, USERNAME, PASSWORD, true));
         Thread t2 = new Thread(() -> loginClient.login(mockUiService, USERNAME, PASSWORD, true));
         t1.start();
         t2.start();
         t1.join();
         t2.join();

         LoginKey key = new LoginKey(USERNAME, PASSWORD, TestLoginClient.class);
         assertTrue(loginClient.getAuthentication(key).isPresent());
      }
   }

   @Test
   void restoreSession_secondTryWaitFails_shouldThrowAuthenticationUiException() throws Exception {
      // 1) clear & seed the two static maps so we take the "already cached → restoreSession" branch
      Field userMapFld = BaseLoginClient.class.getDeclaredField("userLoginMap");
      userMapFld.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<LoginKey,SessionInfo> userMap = (Map<LoginKey,SessionInfo>)userMapFld.get(null);
      userMap.clear();
      Field urlMapFld = BaseLoginClient.class.getDeclaredField("urlAfterLoggingMap");
      urlMapFld.setAccessible(true);
      @SuppressWarnings("unchecked")
      Map<LoginKey,String> urlMap = (Map<LoginKey,String>)urlMapFld.get(null);
      urlMap.clear();

      LoginKey key = new LoginKey(USERNAME, PASSWORD, TestLoginClient.class);
      SessionInfo session = new SessionInfo(cookies, LOCAL_STORAGE_VALUE);
      userMap.put(key, session);
      urlMap.put(key, CURRENT_URL);

      // 2) stub getDriver() to return our SmartWebDriver
      when(mockUiService.getDriver()).thenReturn(mockSmartWebDriver);

      // 3) stub both smartWebDriver.manage() and the underlying WebDriver.manage()
      when(mockSmartWebDriver.manage()).thenReturn(mockOptions);
      WebDriver raw = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      when(mockSmartWebDriver.getOriginal()).thenReturn(raw);
      when(raw.manage()).thenReturn(mockOptions);

      // 4) make sure deleteAllCookies() and addCookie(...) are no-ops
      doNothing().when(mockOptions).deleteAllCookies();
      doNothing().when(mockOptions).addCookie(any(Cookie.class));

      // 5) stub executeJavaScript so UPDATE_LOCAL_STORAGE never blows up
      JavascriptExecutor js = (JavascriptExecutor)raw;
      doReturn("ignored").when(js).executeScript(anyString());

      // 6) static-mock ExpectedConditions so wait.until(…) will use our fake
      ExpectedCondition<?> cond = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> ecs = mockStatic(ExpectedConditions.class)) {
         ecs.when(()  -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(cond);

         // 7) force the *second* wait.until(...) to throw
         when(mockSmartWebDriver.getWait()).thenReturn(mockWait);
         when(mockWait.until(cond)).thenThrow(new RuntimeException("uh-oh"));

         // 8) now call login(cache=true) → calls restoreSession → second try → exception
         AuthenticationUiException ex = assertThrows(
               AuthenticationUiException.class,
               () -> loginClient.login(mockUiService, USERNAME, PASSWORD, true),
               "should wrap into 'Logging in was not successful'"
         );
         assertEquals("Logging in was not successful", ex.getMessage());
      }
   }

   // ----------------------------------------------------------------
   // helper subclass
   private static class TestLoginClient extends BaseLoginClient {
      @Override
      protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
         // no-op
      }

      @Override
      protected By successfulLoginElementLocator() {
         return SUCCESS_LOCATOR;
      }
   }
}
