package com.theairebellion.zeus.ui.selenium.shadowroot;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ShadowDomUtilsTest extends BaseUnitUITest {

   @Mock
   private WebDriver driver;

   @Mock
   private SmartWebDriver smartDriver;

   @Mock
   private WebElement webElement;

   @Mock
   private SmartWebElement smartElement;

   @Mock
   private UiConfig uiConfig;

   @BeforeEach
   void setUp() {
      // Set up common mocks
      when(smartDriver.getOriginal()).thenReturn(driver);

      // We'll use a separate JavascriptExecutor mock
      when(smartElement.getOriginal()).thenReturn(webElement);
      when(smartElement.getDriver()).thenReturn(driver);

      // Mock UiConfig to return a standard wait duration
      when(uiConfig.waitDuration()).thenReturn(10); // 10 seconds
   }

   @Nested
   @DisplayName("parseBy tests")
   class ParseByTests {

      @ParameterizedTest
      @MethodSource("supportedByTypes")
      @DisplayName("parseBy should handle all supported By types correctly")
      void parseByShouldHandleSupportedByTypes(By locator, String expectedType, String expectedValue) throws Exception {
         // Use reflection to access private method
         java.lang.reflect.Method parseByMethod = ShadowDomUtils.class.getDeclaredMethod("parseBy", By.class);
         parseByMethod.setAccessible(true);

         // Execute the method
         Map<String, Object> result = (Map<String, Object>) parseByMethod.invoke(null, locator);

         // Verify results
         assertEquals(expectedType, result.get("type"));
         assertEquals(expectedValue, result.get("value"));
      }

      static Stream<Arguments> supportedByTypes() {
         return Stream.of(
               Arguments.of(By.id("testId"), "id", "testId"),
               Arguments.of(By.name("testName"), "name", "testName"),
               Arguments.of(By.className("testClass"), "className", "testClass"),
               Arguments.of(By.cssSelector("div.test"), "css", "div.test"),
               Arguments.of(By.tagName("div"), "tagName", "div"),
               Arguments.of(By.linkText("Click me"), "linkText", "Click me"),
               Arguments.of(By.partialLinkText("Click"), "partialLinkText", "Click")
         );
      }

      @Test
      @DisplayName("parseBy should throw exception for xpath")
      void parseByShouldThrowExceptionForXpath() throws Exception {
         // Use reflection to access private method
         java.lang.reflect.Method parseByMethod = ShadowDomUtils.class.getDeclaredMethod("parseBy", By.class);
         parseByMethod.setAccessible(true);

         // Create xpath locator
         By xpathLocator = By.xpath("//div[@id='test']");

         // Execute and verify exception
         Exception exception = assertThrows(
               java.lang.reflect.InvocationTargetException.class,
               () -> parseByMethod.invoke(null, xpathLocator)
         );

         assertInstanceOf(IllegalArgumentException.class, exception.getCause());
         assertTrue(
               exception.getCause().getMessage().contains("Xpath selectors are not supported inside shadow roots"));
      }

      @Test
      @DisplayName("parseBy should throw exception for unsupported types")
      void parseByShouldThrowExceptionForUnsupportedTypes() throws Exception {
         // Use reflection to access private method
         java.lang.reflect.Method parseByMethod = ShadowDomUtils.class.getDeclaredMethod("parseBy", By.class);
         parseByMethod.setAccessible(true);

         // Create a custom By that won't match any of the standard patterns
         By customBy = mock(By.class);
         when(customBy.toString()).thenReturn("By.custom: value");

         // Execute and verify exception
         Exception exception = assertThrows(
               java.lang.reflect.InvocationTargetException.class,
               () -> parseByMethod.invoke(null, customBy)
         );

         assertInstanceOf(IllegalArgumentException.class, exception.getCause());
         assertTrue(exception.getCause().getMessage().contains("Unsupported By type"));
      }
   }

   @Nested
   @DisplayName("findElementInShadowRoots with SmartWebDriver tests")
   class FindElementInShadowRootsWithDriverTests {

      @Test
      @DisplayName("findElementInShadowRoots should use default wait from config")
      void findElementInShadowRootsShouldUseDefaultWait() {
         // Arrange
         WebElement foundElement = mock(WebElement.class);
         By locator = By.id("testId");

         // Configure driver as a JavascriptExecutor for this test only
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartDriver.getOriginal()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn(foundElement);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Set up mocks
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Act
            SmartWebElement result = ShadowDomUtils.findElementInShadowRoots(smartDriver, locator);

            // Assert
            assertNotNull(result);
            assertEquals(foundElement, result.getOriginal());
            assertEquals(jsDriver, result.getDriver());

            // Verify the script was executed with the right parameters
            verify((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class));
         }
      }

      @Test
      @DisplayName("findElementInShadowRoots should use specified wait")
      void findElementInShadowRootsShouldUseSpecifiedWait() {
         // Arrange
         WebElement foundElement = mock(WebElement.class);
         By locator = By.id("testId");
         long waitTime = 5000L;

         // Configure driver as a JavascriptExecutor for this test only
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartDriver.getOriginal()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn(foundElement);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Set up mocks
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Act
            SmartWebElement result = ShadowDomUtils.findElementInShadowRoots(smartDriver, locator, waitTime);

            // Assert
            assertNotNull(result);
            assertEquals(foundElement, result.getOriginal());
            assertEquals(jsDriver, result.getDriver());

            // Verify the script was executed with correct parameters
            verify((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class));
         }
      }
   }

   @Nested
   @DisplayName("findElementInShadowRoots with SmartWebElement tests")
   class FindElementInShadowRootsWithElementTests {

      @Test
      @DisplayName("findElementInShadowRoots with element should return element if found")
      void findElementInShadowRootsWithElementShouldReturnElementIfFound() {
         // Arrange
         WebElement foundElement = mock(WebElement.class);
         By locator = By.id("testId");

         // Configure driver as a JavascriptExecutor for this test only
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartElement.getDriver()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class))).thenReturn(
               foundElement);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Set up mocks
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Act
            SmartWebElement result = ShadowDomUtils.findElementInShadowRoots(smartElement, locator);

            // Assert
            assertNotNull(result);
            assertEquals(foundElement, result.getOriginal());
            assertEquals(jsDriver, result.getDriver());

            // Verify the script was executed
            verify((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class));
         }
      }

      @Test
      @DisplayName("findElementInShadowRoots with element should return null if root is null")
      void findElementInShadowRootsWithElementShouldReturnNullIfRootIsNull() {
         // Arrange
         By locator = By.id("testId");

         // Act
         SmartWebElement result = ShadowDomUtils.findElementInShadowRoots((SmartWebElement) null, locator);

         // Assert
         assertEquals(null, result);
      }
   }

   @Nested
   @DisplayName("findElementsInShadowRoots with SmartWebDriver tests")
   class FindElementsInShadowRootsWithDriverTests {

      @Test
      @DisplayName("findElementsInShadowRoots should return list of elements")
      void findElementsInShadowRootsShouldReturnListOfElements() {
         // Arrange
         List<WebElement> foundElements = new ArrayList<>();
         foundElements.add(mock(WebElement.class));
         foundElements.add(mock(WebElement.class));
         By locator = By.id("testId");

         // Configure driver as a JavascriptExecutor for this test only
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartDriver.getOriginal()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn(foundElements);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Set up mocks
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Act
            List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartDriver, locator);

            // Assert
            assertNotNull(results);
            assertEquals(2, results.size());

            // Verify all elements were wrapped correctly
            for (int i = 0; i < results.size(); i++) {
               assertEquals(foundElements.get(i), results.get(i).getOriginal());
               assertEquals(jsDriver, results.get(i).getDriver());
            }

            // Verify the script was executed
            verify((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class));
         }
      }

      @Test
      @DisplayName("findElementsInShadowRoots should return empty list if no elements found")
      void findElementsInShadowRootsShouldReturnEmptyListIfNoElementsFound() {
         // Arrange
         By locator = By.id("testId");

         // Configure driver as a JavascriptExecutor for this test only
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartDriver.getOriginal()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn(null);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Set up mocks
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Act
            List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartDriver, locator);

            // Assert
            assertNotNull(results);
            assertTrue(results.isEmpty());
         }
      }

      @Test
      @DisplayName("findElementsInShadowRoots should return empty list if script returns non-list")
      void findElementsInShadowRootsShouldReturnEmptyListIfScriptReturnsNonList() {
         // Arrange
         By locator = By.id("testId");

         // Configure driver as a JavascriptExecutor for this test only
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartDriver.getOriginal()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn("not a list");

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Set up mocks
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Act
            List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartDriver, locator);

            // Assert
            assertNotNull(results);
            assertTrue(results.isEmpty());
         }
      }
   }

   @Nested
   @DisplayName("findElementsInShadowRoots with SmartWebElement tests")
   class FindElementsInShadowRootsWithElementTests {

      @Test
      @DisplayName("findElementsInShadowRoots with element should return list of elements")
      void findElementsInShadowRootsWithElementShouldReturnListOfElements() {
         // Arrange
         List<WebElement> foundElements = new ArrayList<>();
         foundElements.add(mock(WebElement.class));
         foundElements.add(mock(WebElement.class));
         By locator = By.id("testId");

         // Configure driver as a JavascriptExecutor for this test only
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartElement.getDriver()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class))).thenReturn(
               foundElements);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Set up mocks
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Act
            List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartElement, locator);

            // Assert
            assertNotNull(results);
            assertEquals(2, results.size());

            // Verify elements were wrapped correctly
            for (int i = 0; i < results.size(); i++) {
               assertEquals(foundElements.get(i), results.get(i).getOriginal());
               assertEquals(jsDriver, results.get(i).getDriver());
            }

            // Verify the script was executed
            verify((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class));
         }
      }

      @Test
      @DisplayName("findElementsInShadowRoots with element should return empty list if root is null")
      void findElementsInShadowRootsWithElementShouldReturnEmptyListIfRootIsNull() {
         // Arrange
         By locator = By.id("testId");

         // Act
         List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots((SmartWebElement) null, locator);

         // Assert
         assertNotNull(results);
         assertTrue(results.isEmpty());
      }

      @Test
      @DisplayName("findElementsInShadowRoots with element should return empty list if script returns non-list")
      void findElementsInShadowRootsWithElementShouldReturnEmptyListIfScriptReturnsNonList() {
         // Arrange
         By locator = By.id("testId");

         // Configure driver as a JavascriptExecutor for this test only
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartElement.getDriver()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class))).thenReturn(
               "not a list");

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Set up mocks
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Act
            List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartElement, locator);

            // Assert
            assertNotNull(results);
            assertTrue(results.isEmpty());
         }
      }
   }

   @Nested
   @DisplayName("shadowRootElementsPresent tests")
   class ShadowRootElementsPresentTests {

      @Test
      @DisplayName("shadowRootElementsPresent with driver should return true when shadow roots exist")
      void shadowRootElementsPresentWithDriverShouldReturnTrueWhenShadowRootsExist() {
         // Arrange
         // Configure driver as a JavascriptExecutor for this test
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartDriver.getOriginal()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString())).thenReturn(Boolean.TRUE);

         // Act
         boolean result = ShadowDomUtils.shadowRootElementsPresent(smartDriver);

         // Assert
         assertTrue(result);

         // Verify the script was executed
         verify((JavascriptExecutor) jsDriver).executeScript(anyString());
      }

      @Test
      @DisplayName("shadowRootElementsPresent with driver should return false when no shadow roots exist")
      void shadowRootElementsPresentWithDriverShouldReturnFalseWhenNoShadowRootsExist() {
         // Arrange
         // Configure driver as a JavascriptExecutor for this test
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartDriver.getOriginal()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString())).thenReturn(Boolean.FALSE);

         // Act
         boolean result = ShadowDomUtils.shadowRootElementsPresent(smartDriver);

         // Assert
         assertFalse(result);
      }

      @Test
      @DisplayName("shadowRootElementsPresent with driver should return false when script returns null")
      void shadowRootElementsPresentWithDriverShouldReturnFalseWhenScriptReturnsNull() {
         // Arrange
         // Configure driver as a JavascriptExecutor for this test
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartDriver.getOriginal()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString())).thenReturn(null);

         // Act
         boolean result = ShadowDomUtils.shadowRootElementsPresent(smartDriver);

         // Assert
         assertFalse(result);
      }

      @Test
      @DisplayName("shadowRootElementsPresent with element should return true when shadow roots exist")
      void shadowRootElementsPresentWithElementShouldReturnTrueWhenShadowRootsExist() {
         // Arrange
         // Configure driver as a JavascriptExecutor for this test
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartElement.getDriver()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement))).thenReturn(Boolean.TRUE);

         // Act
         boolean result = ShadowDomUtils.shadowRootElementsPresent(smartElement);

         // Assert
         assertTrue(result);

         // Verify the script was executed
         verify((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement));
      }

      @Test
      @DisplayName("shadowRootElementsPresent with element should return false when no shadow roots exist")
      void shadowRootElementsPresentWithElementShouldReturnFalseWhenNoShadowRootsExist() {
         // Arrange
         // Configure driver as a JavascriptExecutor for this test
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartElement.getDriver()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement))).thenReturn(Boolean.FALSE);

         // Act
         boolean result = ShadowDomUtils.shadowRootElementsPresent(smartElement);

         // Assert
         assertFalse(result);
      }

      @Test
      @DisplayName("shadowRootElementsPresent with element should return false when script returns null")
      void shadowRootElementsPresentWithElementShouldReturnFalseWhenScriptReturnsNull() {
         // Arrange
         // Configure driver as a JavascriptExecutor for this test
         WebDriver jsDriver =
               mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
         when(smartElement.getDriver()).thenReturn(jsDriver);
         when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement))).thenReturn(null);

         // Act
         boolean result = ShadowDomUtils.shadowRootElementsPresent(smartElement);

         // Assert
         assertFalse(result);
      }

      @Test
      @DisplayName("shadowRootElementsPresent with element should return false when element is null")
      void shadowRootElementsPresentWithElementShouldReturnFalseWhenElementIsNull() {
         // Act
         boolean result = ShadowDomUtils.shadowRootElementsPresent((SmartWebElement) null);

         // Assert
         assertFalse(result);
      }
   }

   @Nested
   @DisplayName("JavaScript constants tests")
   class JavaScriptConstantsTests {

      @Test
      @DisplayName("JavaScript constants should be accessible via reflection")
      void javaScriptConstantsShouldBeAccessible() throws Exception {
         // Use reflection to access private static final fields
         java.lang.reflect.Field findShadowElementJs = ShadowDomUtils.class.getDeclaredField("FIND_SHADOW_ELEMENT_JS");
         findShadowElementJs.setAccessible(true);

         java.lang.reflect.Field findShadowElementFromElementJs =
               ShadowDomUtils.class.getDeclaredField("FIND_SHADOW_ELEMENT_FROM_ELEMENT_JS");
         findShadowElementFromElementJs.setAccessible(true);

         java.lang.reflect.Field findShadowElementsJs =
               ShadowDomUtils.class.getDeclaredField("FIND_SHADOW_ELEMENTS_JS");
         findShadowElementsJs.setAccessible(true);

         java.lang.reflect.Field findShadowElementsFromElementJs =
               ShadowDomUtils.class.getDeclaredField("FIND_SHADOW_ELEMENTS_FROM_ELEMENT_JS");
         findShadowElementsFromElementJs.setAccessible(true);

         // Assert all fields are non-null and contain JavaScript code
         assertNotNull(findShadowElementJs.get(null));
         assertInstanceOf(String.class, findShadowElementJs.get(null));
         assertTrue(((String) findShadowElementJs.get(null)).contains("function findShadowElement"));

         assertNotNull(findShadowElementFromElementJs.get(null));
         assertInstanceOf(String.class, findShadowElementFromElementJs.get(null));
         assertTrue(((String) findShadowElementFromElementJs.get(null)).contains("function findShadowElementFromRoot"));

         assertNotNull(findShadowElementsJs.get(null));
         assertInstanceOf(String.class, findShadowElementsJs.get(null));
         assertTrue(((String) findShadowElementsJs.get(null)).contains("function findShadowElements"));

         assertNotNull(findShadowElementsFromElementJs.get(null));
         assertInstanceOf(String.class, findShadowElementsFromElementJs.get(null));
         assertTrue(
               ((String) findShadowElementsFromElementJs.get(null)).contains("function findShadowElementsFromRoot"));
      }
   }
}