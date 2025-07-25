package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebDriverFunctions;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.contains;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.startsWith;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SmartWebDriverTest extends BaseUnitUITest {

   @Mock
   private SmartWebElement smartElement;

   @Mock
   private UiConfig uiConfig;

   private SmartWebDriver smartDriver;

   private WebDriver jsDriver;

   @BeforeEach
   void setUp() {
      // Configure WebDriver for correct casting
      jsDriver = mock(WebDriver.class, withSettings().extraInterfaces(JavascriptExecutor.class));

      // Setup UiConfig default responses
      lenient().when(uiConfig.waitDuration()).thenReturn(10);
      lenient().when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);
      lenient().when(uiConfig.useShadowRoot()).thenReturn(false);

      smartDriver = spy(new SmartWebDriver(jsDriver));
   }

   @Nested
   @DisplayName("Constructor tests")
   class ConstructorTests {

      @Test
      @DisplayName("Constructor should initialize with WebDriverWait")
      void constructorShouldInitializeWithWebDriverWait() {
         // Assert
         assertNotNull(smartDriver.getWait());
         assertNotNull(smartDriver.getOriginal());
      }
   }

   @Nested
   @DisplayName("findSmartElement tests")
   class FindSmartElementTests {

      @Test
      @DisplayName("findSmartElement should use findElementNoWrap when not using wrapped functions")
      void findSmartElementShouldUseFindElementNoWrapWhenNotUsingWrappedFunctions() {
         // Given
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(false);
            smartFinderMock.when(() -> SmartFinder.findElementNoWrap(smartDriver.getOriginal(), locator))
                  .thenReturn(expectedElement);

            // When
            SmartWebElement result = smartDriver.findSmartElement(locator);

            // Then
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementNoWrap(smartDriver.getOriginal(), locator));
         }
      }

      @Test
      @DisplayName("findSmartElement should use normal find when not using shadow root")
      void findSmartElementShouldUseNormalFindWhenNotUsingShadowRoot() {
         // Given
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            smartFinderMock.when(() -> SmartFinder.findElementNormally(
                        eq(smartDriver.getOriginal()), eq(locator), any(Consumer.class)))
                  .thenReturn(expectedElement);

            // When
            SmartWebElement result = smartDriver.findSmartElement(locator);

            // Then
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementNormally(
                  eq(smartDriver.getOriginal()), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findSmartElement should use shadow root find when enabled")
      void findSmartElementShouldUseShadowRootFindWhenEnabled() {
         // Given
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useShadowRoot()).thenReturn(true);
            smartFinderMock.when(() -> SmartFinder.findElementWithShadowRootDriver(
                        eq(smartDriver), eq(locator), any(Consumer.class), eq(null)))
                  .thenReturn(expectedElement);

            // When
            SmartWebElement result = smartDriver.findSmartElement(locator);

            // Then
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementWithShadowRootDriver(
                  eq(smartDriver), eq(locator), any(Consumer.class), eq(null)));
         }
      }

      @Test
      @DisplayName("findSmartElement should handle NoSuchElementException")
      void findSmartElementShouldHandleExceptions() {
         // Given
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);
         NoSuchElementException exception = new NoSuchElementException("Element not found");

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class);
              MockedStatic<ExceptionHandlingWebDriverFunctions> exceptionHandlingFunctionsMock = mockStatic(ExceptionHandlingWebDriverFunctions.class)) {

            // Setup config mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Configure the SmartFinder to throw our exception
            smartFinderMock.when(() -> SmartFinder.findElementNormally(
                        any(WebDriver.class), eq(locator), any(Consumer.class)))
                  .thenThrow(exception);

            // Mock the handler method to return our expected element
            exceptionHandlingFunctionsMock.when(() -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                        any(WebDriver.class), eq(WebElementAction.FIND_ELEMENT), any(Object[].class)))
                  .thenReturn(expectedElement);

            // When
            SmartWebElement result = smartDriver.findSmartElement(locator);

            // Then
            assertSame(expectedElement, result);

            // Verify the exception handler was called with the right parameters
            exceptionHandlingFunctionsMock.verify(() ->
                        ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                              any(WebDriver.class), eq(WebElementAction.FIND_ELEMENT), any(Object[].class)),
                  times(1));
         }
      }

      @Test
      @DisplayName("findSmartElement with wait time should use custom wait")
      void findSmartElementWithWaitTimeShouldUseCustomWait() {
         // Given
         By locator = By.id("testId");
         long waitTime = 5000L;
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useShadowRoot()).thenReturn(true);
            smartFinderMock.when(() -> SmartFinder.findElementWithShadowRootDriver(
                        eq(smartDriver), eq(locator), any(Consumer.class), eq(waitTime)))
                  .thenReturn(expectedElement);

            // When
            SmartWebElement result = smartDriver.findSmartElement(locator, waitTime);

            // Then
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementWithShadowRootDriver(
                  eq(smartDriver), eq(locator), any(Consumer.class), eq(waitTime)));
         }
      }
   }

   @Nested
   @DisplayName("findSmartElements tests")
   class FindSmartElementsTests {

      @Test
      @DisplayName("findSmartElements should use findElementsNoWrap when not using wrapped functions")
      void findSmartElementsShouldUseFindElementsNoWrapWhenNotUsingWrappedFunctions() {
         // Given
         By locator = By.id("testId");
         List<SmartWebElement> expectedElements = Collections.singletonList(mock(SmartWebElement.class));

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(false);
            smartFinderMock.when(() -> SmartFinder.findElementsNoWrap(smartDriver.getOriginal(), locator))
                  .thenReturn(expectedElements);

            // When
            List<SmartWebElement> result = smartDriver.findSmartElements(locator);

            // Then
            assertEquals(expectedElements, result);
            smartFinderMock.verify(() -> SmartFinder.findElementsNoWrap(smartDriver.getOriginal(), locator));
         }
      }

      @Test
      @DisplayName("findSmartElements should use normal find when not using shadow root")
      void findSmartElementsShouldUseNormalFindWhenNotUsingShadowRoot() {
         // Given
         By locator = By.id("testId");
         List<SmartWebElement> expectedElements = Collections.singletonList(mock(SmartWebElement.class));

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            smartFinderMock.when(() -> SmartFinder.findElementsNormally(
                        eq(smartDriver.getOriginal()), eq(locator), any(Consumer.class)))
                  .thenReturn(expectedElements);

            // When
            List<SmartWebElement> result = smartDriver.findSmartElements(locator);

            // Then
            assertEquals(expectedElements, result);
            smartFinderMock.verify(() -> SmartFinder.findElementsNormally(
                  eq(smartDriver.getOriginal()), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findSmartElements should use shadow root find when enabled")
      void findSmartElementsShouldUseShadowRootFindWhenEnabled() {
         // Given
         By locator = By.id("testId");
         List<SmartWebElement> expectedElements = Collections.singletonList(mock(SmartWebElement.class));

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useShadowRoot()).thenReturn(true);
            smartFinderMock.when(() -> SmartFinder.findElementsWithShadowRootDriver(
                        eq(smartDriver), eq(locator), any(Consumer.class)))
                  .thenReturn(expectedElements);

            // When
            List<SmartWebElement> result = smartDriver.findSmartElements(locator);

            // Then
            assertEquals(expectedElements, result);
            smartFinderMock.verify(() -> SmartFinder.findElementsWithShadowRootDriver(
                  eq(smartDriver), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findSmartElements should handle NoSuchElementException")
      void findSmartElementsShouldHandleExceptions() {
         // Given
         By locator = By.id("testId");
         List<SmartWebElement> expectedElements = Collections.singletonList(mock(SmartWebElement.class));
         NoSuchElementException exception = new NoSuchElementException("Elements not found");

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class);
              MockedStatic<ExceptionHandlingWebDriverFunctions> exceptionHandlingFunctionsMock = mockStatic(ExceptionHandlingWebDriverFunctions.class)) {

            // Setup config mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Configure the SmartFinder to throw our exception
            smartFinderMock.when(() -> SmartFinder.findElementsNormally(
                        any(WebDriver.class), eq(locator), any(Consumer.class)))
                  .thenThrow(exception);

            // Mock the handler method to return our expected elements
            exceptionHandlingFunctionsMock.when(() -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                        any(WebDriver.class), eq(WebElementAction.FIND_ELEMENTS), any(Object[].class)))
                  .thenReturn(expectedElements);

            // When
            List<SmartWebElement> results = smartDriver.findSmartElements(locator);

            // Then
            assertSame(expectedElements, results);

            // Verify the exception handler was called with the right parameters
            exceptionHandlingFunctionsMock.verify(() ->
                        ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                              any(WebDriver.class), eq(WebElementAction.FIND_ELEMENTS), any(Object[].class)),
                  times(1));
         }
      }
   }

   @Nested
   @DisplayName("findElement and findElements override tests")
   class FindElementOverrideTests {

      @Test
      @DisplayName("findElement should use normal finder when shadow root is disabled")
      void findElementShouldUseNormalFinderWhenShadowRootDisabled() {
         // Given
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            smartFinderMock.when(() -> SmartFinder.findElementNormally(
                        eq(smartDriver.getOriginal()), eq(locator), any(Consumer.class)))
                  .thenReturn(expectedElement);

            // When
            WebElement result = smartDriver.findElement(locator);

            // Then
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementNormally(
                  eq(smartDriver.getOriginal()), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findElement should use shadow root finder when enabled")
      void findElementShouldUseShadowRootFinderWhenEnabled() {
         // Given
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useShadowRoot()).thenReturn(true);
            smartFinderMock.when(() -> SmartFinder.findElementWithShadowRootDriver(
                        eq(smartDriver), eq(locator), any(Consumer.class), eq(10L)))
                  .thenReturn(expectedElement);

            // When
            WebElement result = smartDriver.findElement(locator);

            // Then
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementWithShadowRootDriver(
                  eq(smartDriver), eq(locator), any(Consumer.class), eq(10L)));
         }
      }

      @Test
      @DisplayName("findElements should use normal finder when shadow root is disabled")
      void findElementsShouldUseNormalFinderWhenShadowRootDisabled() {
         // Given
         By locator = By.id("testId");
         SmartWebElement smartElement1 = mock(SmartWebElement.class);
         List<SmartWebElement> smartElements = Collections.singletonList(smartElement1);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            smartFinderMock.when(() -> SmartFinder.findElementsNormally(
                        eq(smartDriver.getOriginal()), eq(locator), any(Consumer.class)))
                  .thenReturn(smartElements);

            // When
            List<WebElement> results = smartDriver.findElements(locator);

            // Then
            assertEquals(1, results.size());
            assertTrue(results.contains(smartElement1));
            smartFinderMock.verify(() -> SmartFinder.findElementsNormally(
                  eq(smartDriver.getOriginal()), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findElements should use shadow root finder when enabled")
      void findElementsShouldUseShadowRootFinderWhenEnabled() {
         // Given
         By locator = By.id("testId");
         SmartWebElement smartElement1 = mock(SmartWebElement.class);
         List<SmartWebElement> smartElements = Collections.singletonList(smartElement1);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useShadowRoot()).thenReturn(true);
            smartFinderMock.when(() -> SmartFinder.findElementsWithShadowRootDriver(
                        eq(smartDriver), eq(locator), any(Consumer.class)))
                  .thenReturn(smartElements);

            // When
            List<WebElement> results = smartDriver.findElements(locator);

            // Then
            assertEquals(1, results.size());
            assertTrue(results.contains(smartElement1));
            smartFinderMock.verify(() -> SmartFinder.findElementsWithShadowRootDriver(
                  eq(smartDriver), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findElement should execute customWait.until in lambda")
      void findElementShouldExecuteCustomWaitUntilInLambda() {
         // Given
         By locator = By.id("testId");
         Function<WebDriver, Boolean> testCondition = driver -> true;

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class)) {

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Mock SmartFinder to capture the lambda
            try (MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {
               smartFinderMock.when(() -> SmartFinder.findElementNormally(
                           any(WebDriver.class), any(By.class), any(Consumer.class)))
                     .thenAnswer(invocation -> {
                        // Get the waitFn consumer
                        Consumer<Function<WebDriver, ?>> waitFn = invocation.getArgument(2);
                        // Execute it with our test condition
                        waitFn.accept(testCondition);
                        return mock(SmartWebElement.class);
                     });

               // When
               smartDriver.findElement(locator);

               // Then - verify WebDriverWait was constructed and until() was called
               assertEquals(1, waitMock.constructed().size());
               WebDriverWait customWait = waitMock.constructed().get(0);
               verify(customWait).until(any(Function.class));
            }
         }
      }


      @Test
      @DisplayName("findElements should execute customWait.until in lambda")
      void findElementsShouldExecuteCustomWaitUntilInLambda() {
         // Given
         By locator = By.id("testId");
         Function<WebDriver, Boolean> testCondition = driver -> true;

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class)) {

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Mock SmartFinder to capture the lambda
            try (MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {
               smartFinderMock.when(() -> SmartFinder.findElementsNormally(
                           any(WebDriver.class), any(By.class), any(Consumer.class)))
                     .thenAnswer(invocation -> {
                        // Get the waitFn consumer
                        Consumer<Function<WebDriver, ?>> waitFn = invocation.getArgument(2);
                        // Execute it with our test condition
                        waitFn.accept(testCondition);
                        return Collections.singletonList(mock(SmartWebElement.class));
                     });

               // When
               List<WebElement> results = smartDriver.findElements(locator);

               // Then - verify WebDriverWait was constructed and until() was called
               assertEquals(1, waitMock.constructed().size());
               WebDriverWait customWait = waitMock.constructed().get(0);
               verify(customWait).until(any(Function.class));
               assertFalse(results.isEmpty());
            }
         }
      }
   }

   @Nested
   @DisplayName("Wait and utility method tests")
   class WaitAndUtilityTests {

      @Test
      @DisplayName("checkNoException should return true when no exception occurs")
      void checkNoExceptionShouldReturnTrueWhenNoExceptionOccurs() {
         // Given
         Runnable successRunnable = () -> { /* No exception */ };

         // When
         boolean result = smartDriver.checkNoException(successRunnable);

         // Then
         assertTrue(result);
      }

      @Test
      @DisplayName("checkNoException should return false when exception occurs")
      void checkNoExceptionShouldReturnFalseWhenExceptionOccurs() {
         // Given
         Runnable failureRunnable = () -> {
            throw new RuntimeException("Test exception");
         };

         // When
         boolean result = smartDriver.checkNoException(failureRunnable);

         // Then
         assertFalse(result);
      }

      @Test
      @DisplayName("waitUntilElementIsShown with element should handle successful wait")
      void waitUntilElementIsShownWithElementShouldHandleSuccessfulWait() {
         // Given
         int waitSeconds = 5;

         // Create a proper ExpectedCondition implementation
         ExpectedCondition<Boolean> visibilityCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
               return true;
            }

            @Override
            public String toString() {
               return "visibility of element";
            }
         };

         try (MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class);
              MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class,
                    (mock, context) -> {
                       when(mock.until(any(ExpectedCondition.class))).thenReturn(true);
                    })) {

            // Set up ExpectedConditions mock with proper return type
            expectedConditionsMock.when(() -> ExpectedConditions.visibilityOf(any(WebElement.class)))
                  .thenReturn(visibilityCondition);

            // When
            smartDriver.waitUntilElementIsShown(smartElement, waitSeconds);

            // Then
            WebDriverWait constructedWait = waitMock.constructed().get(0);
            verify(constructedWait).until(any(ExpectedCondition.class));
         }
      }

      @Test
      @DisplayName("waitUntilElementIsShown with element should handle exceptions")
      void waitUntilElementIsShownWithElementShouldHandleExceptions() {
         // Given
         int waitSeconds = 5;

         // Create a proper ExpectedCondition implementation
         ExpectedCondition<Boolean> visibilityCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
               return true;
            }

            @Override
            public String toString() {
               return "visibility of element";
            }
         };

         try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
              MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class);
              MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class,
                    (mock, context) -> {
                       when(mock.until(any(ExpectedCondition.class)))
                             .thenThrow(new RuntimeException("Test exception"));
                    })) {

            // Set up ExpectedConditions mock with proper return type
            expectedConditionsMock.when(() -> ExpectedConditions.visibilityOf(any(WebElement.class)))
                  .thenReturn(visibilityCondition);

            // When
            smartDriver.waitUntilElementIsShown(smartElement, waitSeconds);

            // Then
            logUIMock.verify(() -> LogUi.error("Element wasn't displayed after: " + waitSeconds + " seconds"));
         }
      }

      @Test
      @DisplayName("waitUntilElementIsShown with locator should handle successful wait")
      void waitUntilElementIsShownWithLocatorShouldHandleSuccessfulWait() {
         // Given
         By locator = By.id("testId");
         int waitSeconds = 5;

         // Create a proper ExpectedCondition implementation
         ExpectedCondition<Boolean> presenceCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
               return true;
            }

            @Override
            public String toString() {
               return "presence of element located by " + locator;
            }
         };

         try (MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class);
              MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class,
                    (mock, context) -> {
                       when(mock.until(any(ExpectedCondition.class))).thenReturn(true);
                    })) {

            // Set up ExpectedConditions mock with proper return type
            expectedConditionsMock.when(() -> ExpectedConditions.presenceOfElementLocated(any(By.class)))
                  .thenReturn(presenceCondition);

            // When
            smartDriver.waitUntilElementIsShown(locator, waitSeconds);

            // Then
            WebDriverWait constructedWait = waitMock.constructed().get(0);
            verify(constructedWait).until(any(ExpectedCondition.class));
         }
      }

      @Test
      @DisplayName("waitUntilElementIsShown with locator should handle exceptions")
      void waitUntilElementIsShownWithLocatorShouldHandleExceptions() {
         // Given
         By locator = By.id("testId");
         int waitSeconds = 5;

         // Create a proper ExpectedCondition implementation
         ExpectedCondition<Boolean> presenceCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
               return true;
            }

            @Override
            public String toString() {
               return "presence of element located by " + locator;
            }
         };

         try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
              MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class);
              MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class,
                    (mock, context) -> {
                       when(mock.until(any(ExpectedCondition.class)))
                             .thenThrow(new RuntimeException("Test exception"));
                    })) {

            // Set up ExpectedConditions mock with proper return type
            expectedConditionsMock.when(() -> ExpectedConditions.presenceOfElementLocated(any(By.class)))
                  .thenReturn(presenceCondition);

            // When
            smartDriver.waitUntilElementIsShown(locator, waitSeconds);

            // Then
            logUIMock.verify(() -> LogUi.error("Element wasn't displayed after: " + waitSeconds + " seconds"));
         }
      }

      @Test
      @DisplayName("waitUntilElementIsRemoved with element should handle successful wait")
      void waitUntilElementIsRemovedWithElementShouldHandleSuccessfulWait() {
         // Given
         int waitSeconds = 5;

         // Create a proper ExpectedCondition implementation
         ExpectedCondition<Boolean> invisibilityCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
               return true;
            }

            @Override
            public String toString() {
               return "invisibility of element";
            }
         };

         try (MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class);
              MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class,
                    (mock, context) -> {
                       when(mock.until(any(ExpectedCondition.class))).thenReturn(true);
                    })) {

            // Set up ExpectedConditions mock with proper return type
            expectedConditionsMock.when(() -> ExpectedConditions.invisibilityOf(any(WebElement.class)))
                  .thenReturn(invisibilityCondition);

            // When
            smartDriver.waitUntilElementIsRemoved(smartElement, waitSeconds);

            // Then
            WebDriverWait constructedWait = waitMock.constructed().get(0);
            verify(constructedWait).until(any(ExpectedCondition.class));
         }
      }

      @Test
      @DisplayName("waitUntilElementIsRemoved with element should handle exceptions")
      void waitUntilElementIsRemovedWithElementShouldHandleExceptions() {
         // Given
         int waitSeconds = 5;

         // Create a proper ExpectedCondition implementation
         ExpectedCondition<Boolean> invisibilityCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
               return true;
            }

            @Override
            public String toString() {
               return "invisibility of element";
            }
         };

         try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
              MockedStatic<ExpectedConditions> expectedConditions = mockStatic(ExpectedConditions.class);
              MockedConstruction<WebDriverWait> mockConstructed = mockConstruction(WebDriverWait.class,
                    (mock, context) -> {
                       when(mock.until(any(ExpectedCondition.class)))
                             .thenThrow(new RuntimeException("Test exception"));
                    })) {

            // Set up expected conditions with proper return type
            expectedConditions.when(() -> ExpectedConditions.invisibilityOf(any(WebElement.class)))
                  .thenReturn(invisibilityCondition);

            // When
            smartDriver.waitUntilElementIsRemoved(smartElement, waitSeconds);

            // Then
            logUIMock.verify(() -> LogUi.error("Element wasn't removed after: " + waitSeconds + " seconds"));
         }
      }

      @Test
      @DisplayName("waitUntilElementIsRemoved with locator should handle successful wait")
      void waitUntilElementIsRemovedWithLocatorShouldHandleSuccessfulWait() {
         // Given
         By locator = By.id("testId");
         int waitSeconds = 5;

         // Create a proper ExpectedCondition implementation
         ExpectedCondition<Boolean> invisibilityCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
               return true;
            }

            @Override
            public String toString() {
               return "invisibility of element located by " + locator;
            }
         };

         try (MockedStatic<ExpectedConditions> expectedConditions = mockStatic(ExpectedConditions.class);
              MockedConstruction<WebDriverWait> mockConstructed = mockConstruction(WebDriverWait.class,
                    (mock, context) -> {
                       when(mock.until(any(ExpectedCondition.class))).thenReturn(true);
                    })) {

            // Set up expected conditions with proper return type
            expectedConditions.when(() -> ExpectedConditions.invisibilityOfElementLocated(any(By.class)))
                  .thenReturn(invisibilityCondition);

            // When
            smartDriver.waitUntilElementIsRemoved(locator, waitSeconds);

            // Then
            assertEquals(1, mockConstructed.constructed().size());
            WebDriverWait constructedWait = mockConstructed.constructed().get(0);
            verify(constructedWait).until(any(ExpectedCondition.class));
         }
      }

      @Test
      @DisplayName("waitUntilElementIsRemoved with locator should handle exceptions")
      void waitUntilElementIsRemovedWithLocatorShouldHandleExceptions() {
         // Given
         By locator = By.id("testId");
         int waitSeconds = 5;

         // Create a proper ExpectedCondition implementation
         ExpectedCondition<Boolean> invisibilityCondition = new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
               return true;
            }

            @Override
            public String toString() {
               return "invisibility of element located by " + locator;
            }
         };

         try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
              MockedStatic<ExpectedConditions> expectedConditions = mockStatic(ExpectedConditions.class);
              MockedConstruction<WebDriverWait> mockConstructed = mockConstruction(WebDriverWait.class,
                    (mock, context) -> {
                       when(mock.until(any(ExpectedCondition.class)))
                             .thenThrow(new RuntimeException("Test exception"));
                    })) {

            // Set up expected conditions with proper return type
            expectedConditions.when(() -> ExpectedConditions.invisibilityOfElementLocated(any(By.class)))
                  .thenReturn(invisibilityCondition);

            // When
            smartDriver.waitUntilElementIsRemoved(locator, waitSeconds);

            // Then
            logUIMock.verify(() -> LogUi.error("Element wasn't removed after: " + waitSeconds + " seconds"));
         }
      }

      @Test
      @DisplayName("waitWithoutFailure should suppress exceptions")
      void waitWithoutFailureShouldSuppressExceptions() throws Exception {
         // Given
         Function<WebDriver, Boolean> condition = driver -> {
            throw new TimeoutException("Timeout waiting for condition");
         };

         WebDriverWait mockWait = mock(WebDriverWait.class);
         doThrow(new TimeoutException("Timeout")).when(mockWait).until(any(Function.class));

         // Use reflection to set the wait field
         Field waitField = SmartWebDriver.class.getDeclaredField("wait");
         waitField.setAccessible(true);
         waitField.set(smartDriver, mockWait);

         // Use reflection to access the private method
         Method waitWithoutFailureMethod = SmartWebDriver.class.getDeclaredMethod(
               "waitWithoutFailure", Function.class);
         waitWithoutFailureMethod.setAccessible(true);

         // When
         waitWithoutFailureMethod.invoke(smartDriver, condition);

         // Then
         verify(mockWait).until(condition);
      }

      @Test
      @DisplayName("waitWithoutFailure should complete successfully when no exception")
      void waitWithoutFailureShouldCompleteSuccessfullyWhenNoException() throws Exception {
         // Given
         Function<WebDriver, Boolean> condition = driver -> true;

         WebDriverWait mockWait = mock(WebDriverWait.class);
         when(mockWait.until(any(Function.class))).thenReturn(true);

         // Use reflection to set the wait field
         Field waitField = SmartWebDriver.class.getDeclaredField("wait");
         waitField.setAccessible(true);
         waitField.set(smartDriver, mockWait);

         // Use reflection to access the private method
         Method waitWithoutFailureMethod = SmartWebDriver.class.getDeclaredMethod(
               "waitWithoutFailure", Function.class);
         waitWithoutFailureMethod.setAccessible(true);

         // When
         waitWithoutFailureMethod.invoke(smartDriver, condition);

         // Then
         verify(mockWait).until(condition);
      }
   }

   @Nested
   @DisplayName("Lambda functions in findElement and findElements")
   class LambdaFunctionTests {

      @Test
      @DisplayName("Lambda functions in findElement method should handle exception")
      void lambdaFunctionInFindElementShouldHandleException() {
         // Given
         By locator = By.id("testId");
         Function<WebDriver, Boolean> condition = driver -> {
            throw new TimeoutException("Timeout waiting for condition");
         };

         // Create a spy to verify the lambda is called
         SmartWebDriver realDriver = new SmartWebDriver(jsDriver);
         SmartWebDriver spyDriver = spy(realDriver);
         when(spyDriver.getOriginal()).thenReturn(jsDriver);

         // Make sure shadow root is disabled
         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Mock SmartFinder to capture and execute the lambda
            try (MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {
               // Create a mock that will capture the waitFn consumer
               smartFinderMock.when(() -> SmartFinder.findElementNormally(
                           any(WebDriver.class), any(By.class), any(Consumer.class)))
                     .thenAnswer(invocation -> {
                        // Extract the consumer (waitFn lambda) from invocation
                        Consumer<Function<WebDriver, ?>> waitFn = invocation.getArgument(2);

                        // Execute the lambda with our condition that will throw
                        waitFn.accept(condition);

                        // Return a mock element
                        return mock(SmartWebElement.class);
                     });

               // When
               spyDriver.findElement(locator);

               // Then
               smartFinderMock.verify(() -> SmartFinder.findElementNormally(
                     any(WebDriver.class), any(By.class), any(Consumer.class)), times(1));
            }
         }
      }

      @Test
      @DisplayName("Lambda functions in findElements method should handle exception")
      void lambdaFunctionInFindElementsShouldHandleException() {
         // Given
         By locator = By.id("testId");
         Function<WebDriver, Boolean> condition = driver -> {
            throw new TimeoutException("Timeout waiting for condition");
         };

         // Create a spy to verify the lambda is called
         SmartWebDriver realDriver = new SmartWebDriver(jsDriver);
         SmartWebDriver spyDriver = spy(realDriver);
         when(spyDriver.getOriginal()).thenReturn(jsDriver);

         // Make sure shadow root is disabled
         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Mock SmartFinder to capture and execute the lambda
            try (MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {
               // Create a mock that will capture the waitFn consumer
               smartFinderMock.when(() -> SmartFinder.findElementsNormally(
                           any(WebDriver.class), any(By.class), any(Consumer.class)))
                     .thenAnswer(invocation -> {
                        // Extract the consumer (waitFn lambda) from invocation
                        Consumer<Function<WebDriver, ?>> waitFn = invocation.getArgument(2);

                        // Execute the lambda with our condition that will throw
                        waitFn.accept(condition);

                        // Return a mock list
                        return Collections.singletonList(mock(SmartWebElement.class));
                     });

               // When
               spyDriver.findElements(locator);

               // Then
               smartFinderMock.verify(() -> SmartFinder.findElementsNormally(
                     any(WebDriver.class), any(By.class), any(Consumer.class)), times(1));
            }
         }
      }

      @Test
      @DisplayName("Lambda function in findSmartElementInternal with custom wait should handle exception")
      void lambdaFunctionInFindSmartElementInternalShouldHandleException() {
         // Given
         By locator = By.id("testId");
         Long waitTime = 5000L;  // Use custom wait time to trigger the lambda
         Function<WebDriver, Boolean> condition = driver -> {
            throw new TimeoutException("Timeout waiting for condition");
         };

         // Create a spy to verify the lambda is called
         SmartWebDriver realDriver = new SmartWebDriver(jsDriver);
         SmartWebDriver spyDriver = spy(realDriver);
         when(spyDriver.getOriginal()).thenReturn(jsDriver);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Mock SmartFinder to capture and execute the lambda
            try (MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {
               // Create a mock that will capture the waitFn consumer
               smartFinderMock.when(() -> SmartFinder.findElementNormally(
                           any(WebDriver.class), any(By.class), any(Consumer.class)))
                     .thenAnswer(invocation -> {
                        // Extract the consumer (waitFn lambda) from invocation
                        Consumer<Function<WebDriver, ?>> waitFn = invocation.getArgument(2);

                        // Execute the lambda with our condition that will throw
                        waitFn.accept(condition);

                        // Return a mock element
                        return mock(SmartWebElement.class);
                     });

               // When
               spyDriver.findSmartElement(locator, waitTime);

               // Then
               smartFinderMock.verify(() -> SmartFinder.findElementNormally(
                     any(WebDriver.class), any(By.class), any(Consumer.class)), times(1));
            }
         }
      }

      @Test
      @DisplayName("Lambda function in findSmartElementInternal with custom wait should handle exception")
      void lambdaFunctionInFindSmartElementInternalShouldCallUntilFunction() {
         // Given
         By locator = By.id("testId");
         Long waitTime = 5000L;  // Use custom wait time to trigger the lambda
         Function<WebDriver, Boolean> condition = driver -> true;

         // Create a spy to verify the lambda is called
         SmartWebDriver realDriver = new SmartWebDriver(jsDriver);
         SmartWebDriver spyDriver = spy(realDriver);
         when(spyDriver.getOriginal()).thenReturn(jsDriver);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Mock SmartFinder to capture and execute the lambda
            try (MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {
               // Create a mock that will capture the waitFn consumer
               smartFinderMock.when(() -> SmartFinder.findElementNormally(
                           any(WebDriver.class), any(By.class), any(Consumer.class)))
                     .thenAnswer(invocation -> {
                        // Extract the consumer (waitFn lambda) from invocation
                        Consumer<Function<WebDriver, ?>> waitFn = invocation.getArgument(2);

                        // Execute the lambda with our condition that will throw
                        waitFn.accept(condition);

                        // Return a mock element
                        return mock(SmartWebElement.class);
                     });

               // When
               spyDriver.findSmartElement(locator, waitTime);

               // Then
               smartFinderMock.verify(() -> SmartFinder.findElementNormally(
                     any(WebDriver.class), any(By.class), any(Consumer.class)), times(1));
            }
         }
      }
   }

   @Nested
   @DisplayName("handleException edge cases")
   class HandleExceptionEdgeCasesTest {

      @Test
      @DisplayName("handleException should use cause instead of exception if available")
      void handleExceptionShouldUseCauseIfAvailable() throws Exception {
         // Given
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         // Create a nested exception with a cause
         NoSuchElementException cause = new NoSuchElementException("Element not found");
         RuntimeException wrappedException = new RuntimeException("Wrapper exception", cause);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class);
              MockedStatic<ExceptionHandlingWebDriverFunctions> exceptionHandlingFunctionsMock = mockStatic(ExceptionHandlingWebDriverFunctions.class)) {

            // Setup config mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Configure the SmartFinder to throw our wrapper exception
            smartFinderMock.when(() -> SmartFinder.findElementNormally(
                        any(WebDriver.class), eq(locator), any(Consumer.class)))
                  .thenThrow(wrappedException);

            // Mock the handler method to return our expected element
            exceptionHandlingFunctionsMock.when(() -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                        any(WebDriver.class), eq(WebElementAction.FIND_ELEMENT), any(Object[].class)))
                  .thenReturn(expectedElement);

            // When
            SmartWebElement result = smartDriver.findSmartElement(locator);

            // Then
            assertSame(expectedElement, result);

            // Verify the exception handler was called with the right parameters
            exceptionHandlingFunctionsMock.verify(() ->
                        ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                              any(WebDriver.class), eq(WebElementAction.FIND_ELEMENT), any(Object[].class)),
                  times(1));
         }
      }

      @Test
      @DisplayName("handleException should throw when no handler found")
      void handleExceptionShouldThrowWhenNoHandlerFound() throws Exception {
         // Given
         By locator = By.id("testId");
         IllegalArgumentException exception = new IllegalArgumentException("Unsupported locator");

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Setup config mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Configure the SmartFinder to throw our unsupported exception
            smartFinderMock.when(() -> SmartFinder.findElementNormally(
                        any(WebDriver.class), eq(locator), any(Consumer.class)))
                  .thenThrow(exception);

            // When & Then
            assertThrows(IllegalArgumentException.class, () -> smartDriver.findSmartElement(locator));

            // Verify that the error was logged
            logUIMock.verify(() -> LogUi.error(contains("Exception handling failed for method")));
         }
      }

      @Test
      @DisplayName("handleException should log and propagate when handler fails")
      void handleExceptionShouldLogAndPropagateWhenHandlerFails() {
         // Given
         By locator = By.id("testId");
         NoSuchElementException originalException = new NoSuchElementException("Element not found");
         RuntimeException handlerException = new RuntimeException("Handler failed");

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
              MockedStatic<ExceptionHandlingWebDriverFunctions> exceptionHandlingMock = mockStatic(ExceptionHandlingWebDriverFunctions.class)) {

            // Setup config
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);

            // Make SmartFinder throw our exception
            smartFinderMock.when(() -> SmartFinder.findElementNormally(
                        any(WebDriver.class), eq(locator), any(Consumer.class)))
                  .thenThrow(originalException);

            // Make handler throw an exception
            exceptionHandlingMock.when(() -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                        any(WebDriver.class), eq(WebElementAction.FIND_ELEMENT), any(Object[].class)))
                  .thenThrow(handlerException);

            // When/Then - should throw original exception
            NoSuchElementException thrown = assertThrows(NoSuchElementException.class,
                  () -> smartDriver.findSmartElement(locator));
            assertSame(originalException, thrown);

            // Verify logging
            logUIMock.verify(() -> LogUi.error(
                  startsWith("Framework attempted to handle an exception in method"),
                  eq(handlerException)));
            logUIMock.verify(() -> LogUi.error(
                  startsWith("Propagating original exception"),
                  eq(originalException)));
         }
      }
   }
}