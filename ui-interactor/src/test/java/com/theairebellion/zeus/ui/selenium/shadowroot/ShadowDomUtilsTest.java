package com.theairebellion.zeus.ui.selenium.shadowroot;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.selenium.exceptions.UiInteractionException;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebDriver;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.theairebellion.zeus.ui.selenium.shadowroot.ShadowDomUtils.findElementInShadowRoots;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
            // Given
            java.lang.reflect.Method parseByMethod = ShadowDomUtils.class.getDeclaredMethod("parseBy", By.class);
            parseByMethod.setAccessible(true);

            // When
            Map<String, Object> result = (Map<String, Object>) parseByMethod.invoke(null, locator);

            // Then
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
            // Given
            java.lang.reflect.Method parseByMethod = ShadowDomUtils.class.getDeclaredMethod("parseBy", By.class);
            parseByMethod.setAccessible(true);

            // Create xpath locator
            By xpathLocator = By.xpath("//div[@id='test']");

            // When
            Exception exception = assertThrows(
                    java.lang.reflect.InvocationTargetException.class,
                    () -> parseByMethod.invoke(null, xpathLocator)
            );

            // Then
            assertInstanceOf(IllegalArgumentException.class, exception.getCause());
            assertTrue(exception.getCause().getMessage().contains("Xpath selectors are not supported inside shadow roots"));
        }

        @Test
        @DisplayName("parseBy should throw exception for unsupported types")
        void parseByShouldThrowExceptionForUnsupportedTypes() throws Exception {
            // Given
            java.lang.reflect.Method parseByMethod = ShadowDomUtils.class.getDeclaredMethod("parseBy", By.class);
            parseByMethod.setAccessible(true);

            // Create a custom By that won't match any of the standard patterns
            By customBy = mock(By.class);
            when(customBy.toString()).thenReturn("By.custom: value");

            // When
            Exception exception = assertThrows(
                    java.lang.reflect.InvocationTargetException.class,
                    () -> parseByMethod.invoke(null, customBy)
            );

            // Then
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
            // Given
            WebElement foundElement = mock(WebElement.class);
            By locator = By.id("testId");

            // Configure driver as a JavascriptExecutor for this test only
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartDriver.getOriginal()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn(foundElement);

            try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
                // Set up mocks
                configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

                // When
                SmartWebElement result = findElementInShadowRoots(smartDriver, locator);

                // Then
                assertNotNull(result);
                assertEquals(foundElement, result.getOriginal());
                assertEquals(jsDriver, result.getDriver());

                // Verify the script was executed with the right parameters
                verify((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class));
            }
        }

        @Test
        @DisplayName("findElementInShadowRoots should throw exception when element is not found")
        void findElementInShadowRootsWithWebDriverShouldThrowExceptionWhenElementIsNotFound() {
            // Given
            WebElement foundElement = mock(WebElement.class);
            By locator = By.id("testId");

            // When
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartDriver.getOriginal()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn(null);

            try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
                // Set up mocks
                configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

                // Then
                assertThrows(UiInteractionException.class,
                        () -> findElementInShadowRoots(smartDriver, locator)
                );
            }
        }
    }

    @Nested
    @DisplayName("findElementInShadowRoots with SmartWebElement tests")
    class FindElementInShadowRootsWithElementTests {

        @Test
        @DisplayName("findElementInShadowRoots with element should return element if found")
        void findElementInShadowRootsWithElementShouldReturnElementIfFound() {
            // Given
            WebElement foundElement = mock(WebElement.class);
            By locator = By.id("testId");

            // Configure driver as a JavascriptExecutor for this test only
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartElement.getDriver()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class))).thenReturn(foundElement);

            try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
                // Set up mocks
                configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

                // When
                SmartWebElement result = findElementInShadowRoots(smartElement, locator);

                // Then
                assertNotNull(result);
                assertEquals(foundElement, result.getOriginal());
                assertEquals(jsDriver, result.getDriver());

                // Verify the script was executed
                verify((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class));
            }
        }

        @Test
        @DisplayName("findElementInShadowRoots should throw exception when element is not found")
        void findElementInShadowRootsWithElementShouldThrowExceptionWhenElementIsNotFound() {
            // Given
            WebElement foundElement = mock(WebElement.class);
            By locator = By.id("testId");

            // Configure driver as a JavascriptExecutor for this test only
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartElement.getDriver()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class))).thenReturn(null);

            try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
                // Set up mocks
                configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

                // When
                // Then
                assertThrows(UiInteractionException.class,
                        () -> findElementInShadowRoots(smartElement, locator)
                );
            }
        }

        @Test
        @DisplayName("findElementInShadowRoots with element should return null if root is null")
        void findElementInShadowRootsWithElementShouldReturnNullIfRootIsNull() {
            // Given
            By locator = By.id("testId");

            // When
            SmartWebElement result = findElementInShadowRoots((SmartWebElement) null, locator);

            // Then
            assertEquals(null, result);
        }
    }

    @Nested
    @DisplayName("findElementsInShadowRoots with SmartWebDriver tests")
    class FindElementsInShadowRootsWithDriverTests {

        @Test
        @DisplayName("findElementsInShadowRoots should return list of elements")
        void findElementsInShadowRootsShouldReturnListOfElements() {
            // Given
            List<WebElement> foundElements = new ArrayList<>();
            foundElements.add(mock(WebElement.class));
            foundElements.add(mock(WebElement.class));
            By locator = By.id("testId");

            // Configure driver as a JavascriptExecutor for this test only
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartDriver.getOriginal()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn(foundElements);

            try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
                // Set up mocks
                configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

                // When
                List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartDriver, locator);

                // Then
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
            // Given
            By locator = By.id("testId");

            // Configure driver as a JavascriptExecutor for this test only
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartDriver.getOriginal()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), any(Map.class))).thenReturn(null);

            try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
                // Set up mocks
                configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

                // When
                List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartDriver, locator);

                // Then
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
            // Given
            List<WebElement> foundElements = new ArrayList<>();
            foundElements.add(mock(WebElement.class));
            foundElements.add(mock(WebElement.class));
            By locator = By.id("testId");

            // Configure driver as a JavascriptExecutor for this test only
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartElement.getDriver()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class))).thenReturn(foundElements);

            try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
                // Set up mocks
                configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

                // When
                List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartElement, locator);

                // Then
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
            // Given
            By locator = By.id("testId");

            // When
            List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots((SmartWebElement) null, locator);

            // Then
            assertNotNull(results);
            assertTrue(results.isEmpty());
        }

        @Test
        @DisplayName("findElementsInShadowRoots with element should return empty list if script returns non-list")
        void findElementsInShadowRootsWithElementShouldReturnEmptyListIfScriptReturnsNonList() {
            // Given
            By locator = By.id("testId");

            // Configure driver as a JavascriptExecutor for this test only
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartElement.getDriver()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement), any(Map.class))).thenReturn("not a list");

            try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
                // Set up mocks
                configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

                // When
                List<SmartWebElement> results = ShadowDomUtils.findElementsInShadowRoots(smartElement, locator);

                // Then
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
            // Given
            // Configure driver as a JavascriptExecutor for this test
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartDriver.getOriginal()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString())).thenReturn(Boolean.TRUE);

            // When
            boolean result = ShadowDomUtils.shadowRootElementsPresent(smartDriver);

            // Then
            assertTrue(result);

            // Verify the script was executed
            verify((JavascriptExecutor) jsDriver).executeScript(anyString());
        }

        @Test
        @DisplayName("shadowRootElementsPresent with driver should return false when script returns null")
        void shadowRootElementsPresentWithDriverShouldReturnFalseWhenScriptReturnsNull() {
            // Given
            // Configure driver as a JavascriptExecutor for this test
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartDriver.getOriginal()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString())).thenReturn(null);

            // When
            boolean result = ShadowDomUtils.shadowRootElementsPresent(smartDriver);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("shadowRootElementsPresent with element should return true when shadow roots exist")
        void shadowRootElementsPresentWithElementShouldReturnTrueWhenShadowRootsExist() {
            // Given
            // Configure driver as a JavascriptExecutor for this test
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartElement.getDriver()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement))).thenReturn(Boolean.TRUE);

            // When
            boolean result = ShadowDomUtils.shadowRootElementsPresent(smartElement);

            // Then
            assertTrue(result);

            // Verify the script was executed
            verify((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement));
        }

        @Test
        @DisplayName("shadowRootElementsPresent with element should return false when script returns null")
        void shadowRootElementsPresentWithElementShouldReturnFalseWhenScriptReturnsNull() {
            // Given
            // Configure driver as a JavascriptExecutor for this test
            WebDriver jsDriver = mock(WebDriver.class, org.mockito.Mockito.withSettings().extraInterfaces(JavascriptExecutor.class));
            when(smartElement.getDriver()).thenReturn(jsDriver);
            when(((JavascriptExecutor) jsDriver).executeScript(anyString(), eq(webElement))).thenReturn(null);

            // When
            boolean result = ShadowDomUtils.shadowRootElementsPresent(smartElement);

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("shadowRootElementsPresent with element should return false when element is null")
        void shadowRootElementsPresentWithElementShouldReturnFalseWhenElementIsNull() {
            // When
            boolean result = ShadowDomUtils.shadowRootElementsPresent((SmartWebElement) null);

            // Then
            assertFalse(result);
        }
    }
}