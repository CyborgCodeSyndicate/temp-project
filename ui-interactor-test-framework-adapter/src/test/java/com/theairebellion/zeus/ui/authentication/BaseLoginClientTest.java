package com.theairebellion.zeus.ui.authentication;

import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.service.fluent.SuperUiServiceFluent;
import com.theairebellion.zeus.ui.service.fluent.UiServiceFluent;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyBoolean;
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
   private WebDriverWait mockWait;

   @Mock
   private WebDriver.Options mockOptions;

   private TestLoginClient loginClient;
   private Set<Cookie> cookies;

   @BeforeEach
   void setUp() {
      // Create test data
      cookies = new HashSet<>();
      cookies.add(new Cookie("sessionId", "123456"));

      // Configure login client
      loginClient = spy(new TestLoginClient());
      doReturn(SUCCESS_LOCATOR).when(loginClient).successfulLoginElementLocator();

      // Configure minimal mocks
      when(mockUiService.getDriver()).thenReturn(mockSmartWebDriver);
      when(mockSmartWebDriver.getWait()).thenReturn(mockWait);
      when(mockSmartWebDriver.getCurrentUrl()).thenReturn(CURRENT_URL);
   }

   @Test
   void loginWithoutCache_ShouldPerformLogin() {
      // Given
      WebDriver mockWebDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) mockWebDriver;

      // Setup mocks
      when(mockSmartWebDriver.getOriginal()).thenReturn(mockWebDriver);
      when(mockWebDriver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      // This is the key part: mock the ExpectedConditions.presenceOfElementLocated
      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(mockCondition);

         doReturn(true).when(mockWait).until(any(ExpectedCondition.class));
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         // When
         loginClient.login(mockUiService, USERNAME, PASSWORD, false);

         // Then
         verify(loginClient).loginImpl(eq(mockUiService), eq(USERNAME), eq(PASSWORD));
         verify(mockOptions).getCookies();
      }
   }

   @Test
   void loginWithCache_FirstTime_ShouldPerformAndCacheLogin() {
      // Given
      WebDriver mockWebDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) mockWebDriver;

      // Setup mocks
      when(mockSmartWebDriver.getOriginal()).thenReturn(mockWebDriver);
      when(mockWebDriver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      // Mock the ExpectedConditions
      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(mockCondition);

         doReturn(true).when(mockWait).until(any(ExpectedCondition.class));
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         // When
         loginClient.login(mockUiService, USERNAME, PASSWORD, true);

         // Then
         verify(loginClient).loginImpl(eq(mockUiService), eq(USERNAME), eq(PASSWORD));
         verify(mockOptions).getCookies();

         // Verify session is cached
         LoginKey key = new LoginKey(USERNAME, PASSWORD, TestLoginClient.class);
         Optional<SessionInfo> sessionInfo = loginClient.getAuthentication(key);
         assertTrue(sessionInfo.isPresent());
         assertEquals(cookies, sessionInfo.get().getCookies());
         assertEquals(LOCAL_STORAGE_VALUE, sessionInfo.get().getLocalStorage());
      }
   }

   @Test
   @Disabled
   void loginWithCache_SubsequentLogin_ShouldRestoreSession() {
      // Given
      WebDriver mockWebDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) mockWebDriver;

      // Setup mocks
      when(mockSmartWebDriver.getOriginal()).thenReturn(mockWebDriver);
      when(mockWebDriver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      // Mock the ExpectedConditions for both logins
      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(mockCondition);

         doReturn(true).when(mockWait).until(any(ExpectedCondition.class));
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         // First login to cache the session
         loginClient.login(mockUiService, USERNAME, PASSWORD, true);

         // Reset the loginClient spy but keep cookies
         reset(loginClient);
         doReturn(SUCCESS_LOCATOR).when(loginClient).successfulLoginElementLocator();
         doCallRealMethod().when(loginClient).login(any(), anyString(), anyString(), anyBoolean());
         doCallRealMethod().when(loginClient).getAuthentication(any());

         // When - second login should use cached session
         loginClient.login(mockUiService, USERNAME, PASSWORD, true);

         // Then
         verify(loginClient, never()).loginImpl(any(), anyString(), anyString());
         verify(mockOptions, atLeastOnce()).addCookie(any(Cookie.class));
      }
   }

   @Test
   @Disabled
   void loginWithCache_SessionRestoreFailed_ShouldThrowException() {
      // Given
      WebDriver mockWebDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) mockWebDriver;

      // Setup mocks
      when(mockSmartWebDriver.getOriginal()).thenReturn(mockWebDriver);
      when(mockWebDriver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      // Mock the ExpectedConditions for both logins
      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      ExpectedCondition<Object> failingCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         // First setup successful condition
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(mockCondition);

         doReturn(true).when(mockWait).until(eq(mockCondition));
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         // First login to cache the session
         loginClient.login(mockUiService, USERNAME, PASSWORD, true);

         // Now setup failing condition for the second login
         reset(mockWait);
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(failingCondition);

         doThrow(new RuntimeException("Element not found")).when(mockWait).until(eq(failingCondition));

         // When/Then
         RuntimeException exception = assertThrows(RuntimeException.class, () ->
               loginClient.login(mockUiService, USERNAME, PASSWORD, true)
         );

         assertEquals("Restoring session was not successful", exception.getMessage());
      }
   }

   @Test
   void login_InitialLoginFailed_ShouldThrowException() {
      // Given
      WebDriver mockWebDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) mockWebDriver;

      // Setup mocks
      when(mockSmartWebDriver.getOriginal()).thenReturn(mockWebDriver);
      when(mockWebDriver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      // Mock the failing ExpectedConditions
      ExpectedCondition<Object> failingCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(failingCondition);

         doThrow(new RuntimeException("Element not found")).when(mockWait).until(eq(failingCondition));
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         // When/Then
         RuntimeException exception = assertThrows(RuntimeException.class, () ->
               loginClient.login(mockUiService, USERNAME, PASSWORD, false)
         );

         assertEquals("Logging in was not successful", exception.getMessage());
      }
   }

   @Test
   void executeJavaScript_DriverNotJsExecutor_ShouldThrowException() {
      // Given - setup a WebDriver that is NOT a JavascriptExecutor
      WebDriver nonJsDriver = mock(WebDriver.class);

      // Need to make sure we're mocking the ExpectedConditions
      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         // Configure mocks
         when(mockSmartWebDriver.getOriginal()).thenReturn(nonJsDriver);
         when(nonJsDriver.manage()).thenReturn(mockOptions);
         when(mockOptions.getCookies()).thenReturn(cookies);

         // We'll get to the JS executor check before the ExpectedConditions call
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(any()))
               .thenReturn(mockCondition);

         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         // When/Then
         IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
               loginClient.login(mockUiService, USERNAME, PASSWORD, false)
         );

         assertEquals("Driver does not support JavaScript execution", exception.getMessage());
      }
   }

   @Test
   void getAuthentication_NoStoredSession_ShouldReturnEmpty() {
      // Given - a key that doesn't exist in the login map
      LoginKey nonExistentKey = new LoginKey("nonExistent", "password", TestLoginClient.class);

      // When
      Optional<SessionInfo> result = loginClient.getAuthentication(nonExistentKey);

      // Then
      assertFalse(result.isPresent());
   }

   @Test
   void loginConcurrently_ShouldHandleRaceConditions() throws InterruptedException {
      // This test simulates concurrent login attempts with the same credentials

      // Given
      WebDriver mockWebDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));
      JavascriptExecutor jsExecutor = (JavascriptExecutor) mockWebDriver;

      // Setup mocks
      when(mockSmartWebDriver.getOriginal()).thenReturn(mockWebDriver);
      when(mockWebDriver.manage()).thenReturn(mockOptions);
      when(mockOptions.getCookies()).thenReturn(cookies);
      doReturn(LOCAL_STORAGE_VALUE).when(jsExecutor).executeScript(anyString());

      // Mock the ExpectedConditions
      ExpectedCondition<Object> mockCondition = mock(ExpectedCondition.class);
      try (MockedStatic<ExpectedConditions> mockedStatic = mockStatic(ExpectedConditions.class)) {
         mockedStatic.when(() -> ExpectedConditions.presenceOfElementLocated(eq(SUCCESS_LOCATOR)))
               .thenReturn(mockCondition);

         doReturn(true).when(mockWait).until(any(ExpectedCondition.class));
         doNothing().when(loginClient).loginImpl(any(), anyString(), anyString());

         // When - simulate concurrent login calls
         Thread thread1 = new Thread(() -> loginClient.login(mockUiService, USERNAME, PASSWORD, true));
         Thread thread2 = new Thread(() -> loginClient.login(mockUiService, USERNAME, PASSWORD, true));

         thread1.start();
         thread2.start();

         thread1.join();
         thread2.join();

         // Then - verify session is cached
         LoginKey key = new LoginKey(USERNAME, PASSWORD, TestLoginClient.class);
         Optional<SessionInfo> sessionInfo = loginClient.getAuthentication(key);
         assertTrue(sessionInfo.isPresent());
      }
   }

   // Test class implementation
   private static class TestLoginClient extends BaseLoginClient {
      @Override
      protected <T extends UiServiceFluent<?>> void loginImpl(T uiService, String username, String password) {
         // Test implementation
      }

      @Override
      protected By successfulLoginElementLocator() {
         return SUCCESS_LOCATOR;
      }
   }
}