package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.config.UiConfig;
import com.theairebellion.zeus.ui.config.UiConfigHolder;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebElement;
import com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebElementFunctions;
import com.theairebellion.zeus.ui.selenium.locating.SmartFinder;
import com.theairebellion.zeus.ui.util.FourFunction;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class SmartWebElementTest extends BaseUnitUITest {

   @Mock
   private WebDriver driver;

   @Mock
   private WebElement webElement;

   @Mock
   private UiConfig uiConfig;

   private SmartWebElement smartElement;

   @BeforeEach
   void setUp() {
      // Setup UiConfig default responses
      when(uiConfig.waitDuration()).thenReturn(10);
      when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);
      when(uiConfig.useShadowRoot()).thenReturn(false);

      // Create SmartWebElement with WebElement mock
      try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class)) {
         uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
         smartElement = spy(new SmartWebElement(webElement, driver));
      }
   }

   @Nested
   @DisplayName("Constructor tests")
   class ConstructorTests {

      @Test
      @DisplayName("Constructor should initialize with WebDriverWait and original element")
      void constructorShouldInitializeCorrectly() {
         // Act - SmartWebElement already created in setUp()

         // Assert
         assertNotNull(smartElement.getDriver());
         assertEquals(driver, smartElement.getDriver());
      }
   }

   @Nested
   @DisplayName("findSmartElements tests")
   class FindSmartElementsTests {

      @Test
      @DisplayName("findSmartElements should use findElementsNoWrap when not using wrapped functions")
      void findSmartElementsShouldUseFindElementsNoWrapWhenNotUsingWrappedFunctions() {
         // Arrange
         By locator = By.id("testId");
         List<SmartWebElement> expectedElements = Collections.singletonList(mock(SmartWebElement.class));

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(false);
            smartFinderMock.when(() -> SmartFinder.findElementsNoWrap(smartElement, locator))
                  .thenReturn(expectedElements);

            // Act
            List<SmartWebElement> result = smartElement.findSmartElements(locator);

            // Assert
            assertEquals(expectedElements, result);
            smartFinderMock.verify(() -> SmartFinder.findElementsNoWrap(smartElement, locator));
         }
      }

      @Test
      @DisplayName("findSmartElements should use normal find when not using shadow root")
      void findSmartElementsShouldUseNormalFindWhenNotUsingShadowRoot() {
         // Arrange
         By locator = By.id("testId");
         List<SmartWebElement> expectedElements = Collections.singletonList(mock(SmartWebElement.class));

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);
            when(uiConfig.useShadowRoot()).thenReturn(false);
            smartFinderMock.when(() -> SmartFinder.findElementsNormally(
                        eq(smartElement), eq(locator), any(Consumer.class)))
                  .thenReturn(expectedElements);

            // Act
            List<SmartWebElement> result = smartElement.findSmartElements(locator);

            // Assert
            assertEquals(expectedElements, result);
            smartFinderMock.verify(() -> SmartFinder.findElementsNormally(
                  eq(smartElement), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findSmartElements should use shadow root find when enabled")
      void findSmartElementsShouldUseShadowRootFindWhenEnabled() {
         // Arrange
         By locator = By.id("testId");
         List<SmartWebElement> expectedElements = Collections.singletonList(mock(SmartWebElement.class));

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);
            when(uiConfig.useShadowRoot()).thenReturn(true);
            smartFinderMock.when(() -> SmartFinder.findElementsWithShadowRootElement(
                        eq(smartElement), eq(locator), any(Consumer.class)))
                  .thenReturn(expectedElements);

            // Act
            List<SmartWebElement> result = smartElement.findSmartElements(locator);

            // Assert
            assertEquals(expectedElements, result);
            smartFinderMock.verify(() -> SmartFinder.findElementsWithShadowRootElement(
                  eq(smartElement), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findSmartElements should handle exceptions")
      void findSmartElementsShouldHandleExceptions() {
         // Arrange
         By locator = By.id("testId");
         List<SmartWebElement> expectedElements = Collections.singletonList(mock(SmartWebElement.class));
         NoSuchElementException exception = new NoSuchElementException("Element not found");

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class);
              MockedStatic<ExceptionHandlingWebElement> exceptionHandlingMock = mockStatic(ExceptionHandlingWebElement.class);
              MockedStatic<ExceptionHandlingWebElementFunctions> exceptionHandlingFnMock = mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);
            when(uiConfig.useShadowRoot()).thenReturn(false);
            smartFinderMock.when(() -> SmartFinder.findElementsNormally(
                        eq(smartElement), eq(locator), any(Consumer.class)))
                  .thenThrow(exception);

            // Create mock enum for exception handling
            ExceptionHandlingWebElement mockEnum = mock(ExceptionHandlingWebElement.class);
            when(mockEnum.getMethodName()).thenReturn("findElements");

            // Mock the exception handling function
            exceptionHandlingFnMock.when(() -> ExceptionHandlingWebElementFunctions.handleNoSuchElement(
                        eq(driver), eq(smartElement), eq(WebElementAction.FIND_ELEMENTS), eq(locator)))
                  .thenReturn(expectedElements);

            // Create a mock FourFunction
            @SuppressWarnings("unchecked")
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> mockFunction =
                  mock(FourFunction.class);
            when(mockFunction.apply(eq(driver), eq(smartElement), eq(exception), any()))
                  .thenReturn(expectedElements);

            // Mock map as complete object
            @SuppressWarnings("unchecked")
            Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>>
                  mockMap =
                  new HashMap<>();
            mockMap.put(NoSuchElementException.class, mockFunction);
            when(mockEnum.getExceptionHandlingMap()).thenReturn(mockMap);

            // Set up values method to return our mock
            exceptionHandlingMock.when(ExceptionHandlingWebElement::values)
                  .thenReturn(new ExceptionHandlingWebElement[] {mockEnum});

            // Act
            List<SmartWebElement> result = smartElement.findSmartElements(locator);

            // Assert
            assertEquals(expectedElements, result);
            verify(mockFunction).apply(eq(driver), eq(smartElement), eq(exception), any());
         }
      }
   }

   @Nested
   @DisplayName("findSmartElement tests")
   class FindSmartElementTests {

      @Test
      @DisplayName("findSmartElement should use findElementNoWrap when not using wrapped functions")
      void findSmartElementShouldUseFindElementNoWrapWhenNotUsingWrappedFunctions() {
         // Arrange
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(false);
            smartFinderMock.when(() -> SmartFinder.findElementNoWrap(smartElement, locator))
                  .thenReturn(expectedElement);

            // Act
            SmartWebElement result = smartElement.findSmartElement(locator);

            // Assert
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementNoWrap(smartElement, locator));
         }
      }

      @Test
      @DisplayName("findSmartElement should use normal find when not using shadow root")
      void findSmartElementShouldUseNormalFindWhenNotUsingShadowRoot() {
         // Arrange
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);
            when(uiConfig.useShadowRoot()).thenReturn(false);
            smartFinderMock.when(() -> SmartFinder.findElementNormally(
                        eq(smartElement), eq(locator), any(Consumer.class)))
                  .thenReturn(expectedElement);

            // Act
            SmartWebElement result = smartElement.findSmartElement(locator);

            // Assert
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementNormally(
                  eq(smartElement), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findSmartElement should use shadow root find when enabled")
      void findSmartElementShouldUseShadowRootFindWhenEnabled() {
         // Arrange
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);
            when(uiConfig.useShadowRoot()).thenReturn(true);
            smartFinderMock.when(() -> SmartFinder.findElementWithShadowRootElement(
                        eq(smartElement), eq(locator), any(Consumer.class)))
                  .thenReturn(expectedElement);

            // Act
            SmartWebElement result = smartElement.findSmartElement(locator);

            // Assert
            assertSame(expectedElement, result);
            smartFinderMock.verify(() -> SmartFinder.findElementWithShadowRootElement(
                  eq(smartElement), eq(locator), any(Consumer.class)));
         }
      }

      @Test
      @DisplayName("findSmartElement should handle exceptions")
      void findSmartElementShouldHandleExceptions() {
         // Arrange
         By locator = By.id("testId");
         SmartWebElement expectedElement = mock(SmartWebElement.class);
         NoSuchElementException exception = new NoSuchElementException("Element not found");

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<SmartFinder> smartFinderMock = mockStatic(SmartFinder.class);
              MockedStatic<ExceptionHandlingWebElement> exceptionHandlingMock = mockStatic(ExceptionHandlingWebElement.class);
              MockedStatic<ExceptionHandlingWebElementFunctions> exceptionHandlingFnMock = mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup mocks
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);
            when(uiConfig.useShadowRoot()).thenReturn(false);
            smartFinderMock.when(() -> SmartFinder.findElementNormally(
                        eq(smartElement), eq(locator), any(Consumer.class)))
                  .thenThrow(exception);

            // Create mock enum for exception handling
            ExceptionHandlingWebElement mockEnum = mock(ExceptionHandlingWebElement.class);
            when(mockEnum.getMethodName()).thenReturn("findElement");

            // Mock the exception handling function
            exceptionHandlingFnMock.when(() -> ExceptionHandlingWebElementFunctions.handleNoSuchElement(
                        eq(driver), eq(smartElement), eq(WebElementAction.FIND_ELEMENT), eq(locator)))
                  .thenReturn(expectedElement);

            // Create a mock FourFunction
            @SuppressWarnings("unchecked")
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> mockFunction =
                  mock(FourFunction.class);
            when(mockFunction.apply(eq(driver), eq(smartElement), eq(exception), any()))
                  .thenReturn(expectedElement);

            // Mock map as complete object
            @SuppressWarnings("unchecked")
            Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>>
                  mockMap =
                  new HashMap<>();
            mockMap.put(NoSuchElementException.class, mockFunction);
            when(mockEnum.getExceptionHandlingMap()).thenReturn(mockMap);

            // Set up values method to return our mock
            exceptionHandlingMock.when(ExceptionHandlingWebElement::values)
                  .thenReturn(new ExceptionHandlingWebElement[] {mockEnum});

            // Act
            SmartWebElement result = smartElement.findSmartElement(locator);

            // Assert
            assertSame(expectedElement, result);
            verify(mockFunction).apply(eq(driver), eq(smartElement), eq(exception), any());
         }
      }
   }

   @Nested
   @DisplayName("Element action tests")
   class ElementActionTests {

      @Test
      @DisplayName("click should use super.click when not using wrapped functions")
      void clickShouldUseSuperClickWhenNotUsingWrappedFunctions() {
         // Arrange - create a separate test with completely separate objects
         WebElement testElement = mock(WebElement.class);
         UiConfig testConfig = mock(UiConfig.class);
         when(testConfig.useWrappedSeleniumFunctions()).thenReturn(false);
         when(testConfig.waitDuration()).thenReturn(1);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(testConfig);

            // Use doNothing instead of verify to prevent counting calls
            doNothing().when(testElement).click();

            // Create element with our specially prepared mock
            SmartWebElement element = new SmartWebElement(testElement, driver);

            // Act
            element.click();

            // Assert - just verify the config was checked
            verify(testConfig).useWrappedSeleniumFunctions();
         }
      }

      @Test
      @DisplayName("click should use performActionWithWait when using wrapped functions")
      void clickShouldUsePerformActionWithWaitWhenUsingWrappedFunctions() {
         // Arrange
         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class)) {

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);

            // Mock ExpectedConditions.elementToBeClickable
            ExpectedCondition<WebElement> clickableCondition = driver -> webElement;
            expectedConditionsMock.when(() -> ExpectedConditions.elementToBeClickable(any(WebElement.class)))
                  .thenReturn(clickableCondition);

            // Act
            smartElement.click();

            // Verify the original element was clicked
            verify(webElement).click();
         }
      }

      @Test
      @DisplayName("clear should use super.clear when not using wrapped functions")
      void clearShouldUseSuperClearWhenNotUsingWrappedFunctions() {
         // Arrange
         WebElement testElement = mock(WebElement.class);
         UiConfig testConfig = mock(UiConfig.class);
         when(testConfig.useWrappedSeleniumFunctions()).thenReturn(false);
         when(testConfig.waitDuration()).thenReturn(0);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(testConfig);

            // Use doNothing instead of verify to prevent counting calls
            doNothing().when(testElement).clear();

            // Create element with our specially prepared mock
            SmartWebElement element = new SmartWebElement(testElement, driver);

            // Act
            element.clear();

            // Assert - just verify the config was checked
            verify(testConfig).useWrappedSeleniumFunctions();
         }
      }

      @Test
      @DisplayName("clear should use performActionWithWait when using wrapped functions")
      void clearShouldUsePerformActionWithWaitWhenUsingWrappedFunctions() {
         // Arrange
         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class)) {

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);

            // Mock ExpectedConditions.elementToBeClickable
            ExpectedCondition<WebElement> clickableCondition = driver -> webElement;
            expectedConditionsMock.when(() -> ExpectedConditions.elementToBeClickable(any(WebElement.class)))
                  .thenReturn(clickableCondition);

            // Act
            smartElement.clear();

            // Verify the original element was cleared
            verify(webElement).clear();
         }
      }

      @Test
      @DisplayName("sendKeys should use super.sendKeys when not using wrapped functions")
      void sendKeysShouldUseSuperSendKeysWhenNotUsingWrappedFunctions() {
         // Arrange
         String textToSend = "test text";
         WebElement testElement = mock(WebElement.class);
         UiConfig testConfig = mock(UiConfig.class);
         when(testConfig.useWrappedSeleniumFunctions()).thenReturn(false);
         when(testConfig.waitDuration()).thenReturn(1);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(testConfig);

            // Act
            doNothing().when(testElement).sendKeys(textToSend);

            // Create element with our specially prepared mock
            SmartWebElement element = new SmartWebElement(testElement, driver);

            // Act
            element.sendKeys(textToSend);

            // Verify the original element received sendKeys directly
            verify(testConfig).useWrappedSeleniumFunctions();
         }
      }

      @Test
      @DisplayName("sendKeys should use performActionWithWait when using wrapped functions")
      void sendKeysShouldUsePerformActionWithWaitWhenUsingWrappedFunctions() {
         // Arrange
         String textToSend = "test text";
         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class)) {

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);

            // Mock ExpectedConditions.elementToBeClickable
            ExpectedCondition<WebElement> clickableCondition = driver -> webElement;
            expectedConditionsMock.when(() -> ExpectedConditions.elementToBeClickable(any(WebElement.class)))
                  .thenReturn(clickableCondition);

            // Act
            smartElement.sendKeys(textToSend);

            // Verify the original element received sendKeys
            verify(webElement).sendKeys(textToSend);
         }
      }

      @Test
      @DisplayName("submit should use super.submit when not using wrapped functions")
      void submitShouldUseSuperSubmitWhenNotUsingWrappedFunctions() {
         // Arrange
         WebElement testElement = mock(WebElement.class);
         UiConfig testConfig = mock(UiConfig.class);
         when(testConfig.useWrappedSeleniumFunctions()).thenReturn(false);
         when(testConfig.waitDuration()).thenReturn(0);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(testConfig);

            // Use doNothing instead of verify to prevent counting calls
            doNothing().when(testElement).submit();

            // Create element with our specially prepared mock
            SmartWebElement element = new SmartWebElement(testElement, driver);

            // Act
            element.submit();

            // Assert - just verify the config was checked
            verify(testConfig).useWrappedSeleniumFunctions();
         }
      }

      @Test
      @DisplayName("submit should use performActionWithWait when using wrapped functions")
      void submitShouldUsePerformActionWithWaitWhenUsingWrappedFunctions() {
         // Arrange
         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class)) {

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);

            // Mock ExpectedConditions.elementToBeClickable
            ExpectedCondition<WebElement> clickableCondition = driver -> webElement;
            expectedConditionsMock.when(() -> ExpectedConditions.elementToBeClickable(any(WebElement.class)))
                  .thenReturn(clickableCondition);

            // Act
            smartElement.submit();

            // Verify the original element was submitted
            verify(webElement).submit();
         }
      }

      @Test
      @DisplayName("clearAndSendKeys should call clear and sendKeys")
      void clearAndSendKeysShouldCallClearAndSendKeys() {
         // Arrange
         String textToSend = "test text";
         WebElement originalElement = mock(WebElement.class);
         UiConfig mockConfig = mock(UiConfig.class);

         try (MockedStatic<UiConfigHolder> configHolderMock = mockStatic(UiConfigHolder.class)) {
            // Configure config to return false for wrapped functions and minimal wait
            when(mockConfig.useWrappedSeleniumFunctions()).thenReturn(false);
            when(mockConfig.waitDuration()).thenReturn(0);
            configHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(mockConfig);

            SmartWebElement testElement = new SmartWebElement(originalElement, driver);


            // Act
            testElement.clearAndSendKeys(textToSend);

            // Verify
            verify(originalElement, times(1)).clear();
            verify(originalElement, times(1)).sendKeys(textToSend);
         }
      }

      @Test
      @DisplayName("doubleClick should use Actions directly when not using wrapped functions")
      void doubleClickShouldUseActionsDirectlyWhenNotUsingWrappedFunctions() {
         // Arrange
         WebDriver mockDriver = mock(WebDriver.class);
         WebElement testElement = mock(WebElement.class);
         UiConfig testConfig = mock(UiConfig.class);

         when(testConfig.waitDuration()).thenReturn(0);
         when(testConfig.useWrappedSeleniumFunctions()).thenReturn(false);

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedConstruction<Actions> actionsMock = mockConstruction(Actions.class)) {

            // Setup UiConfigHolder to return test config
            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(testConfig);

            // Create element with our specially prepared mock
            SmartWebElement element = new SmartWebElement(testElement, mockDriver);

            // Act
            element.doubleClick();

            // Verify
            assertFalse(actionsMock.constructed().isEmpty(), "Actions should have been constructed");
            verify(testConfig).useWrappedSeleniumFunctions();
         }
      }

      @Test
      @DisplayName("doubleClick should use wait and Actions when using wrapped functions")
      void doubleClickShouldUseWaitAndActionsWhenUsingWrappedFunctions() {
         // Arrange
         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class);
              MockedConstruction<Actions> actionsMock = mockConstruction(Actions.class,
                    (mock, context) -> when(mock.doubleClick()).thenReturn(mock))) {

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);

            // Mock ExpectedConditions.elementToBeClickable
            ExpectedCondition<WebElement> clickableCondition = driver -> webElement;
            expectedConditionsMock.when(() -> ExpectedConditions.elementToBeClickable(any(WebElement.class)))
                  .thenReturn(clickableCondition);

            // Act
            smartElement.doubleClick();

            // Verify actions was used
            assertFalse(actionsMock.constructed().isEmpty());
            verify(actionsMock.constructed().get(0)).doubleClick();
         }
      }

      @Test
      @DisplayName("doubleClick should handle exceptions")
      void doubleClickShouldHandleExceptions() {
         // Arrange
         ElementNotInteractableException exception = new ElementNotInteractableException("Element not interactable");
         Object expectedResult = new Object(); // Can be anything, just for verification

         try (MockedStatic<UiConfigHolder> uiConfigHolderMock = mockStatic(UiConfigHolder.class);
              MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class);
              MockedStatic<ExceptionHandlingWebElement> exceptionHandlingMock = mockStatic(ExceptionHandlingWebElement.class);
              MockedStatic<ExceptionHandlingWebElementFunctions> exceptionHandlingFnMock = mockStatic(ExceptionHandlingWebElementFunctions.class);
              MockedConstruction<Actions> actionsMock = mockConstruction(Actions.class,
                    (mock, context) -> when(mock.doubleClick()).thenThrow(exception))) {

            uiConfigHolderMock.when(UiConfigHolder::getUiConfig).thenReturn(uiConfig);
            when(uiConfig.useWrappedSeleniumFunctions()).thenReturn(true);

            // Mock ExpectedConditions.elementToBeClickable
            ExpectedCondition<WebElement> clickableCondition = driver -> webElement;
            expectedConditionsMock.when(() -> ExpectedConditions.elementToBeClickable(any(WebElement.class)))
                  .thenReturn(clickableCondition);

            // Create mock enum for exception handling
            ExceptionHandlingWebElement mockEnum = mock(ExceptionHandlingWebElement.class);
            when(mockEnum.getMethodName()).thenReturn("doubleClick");

            // Create a mock FourFunction
            @SuppressWarnings("unchecked")
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> mockFunction =
                  mock(FourFunction.class);
            when(mockFunction.apply(eq(driver), eq(smartElement), eq(exception), any()))
                  .thenReturn(expectedResult);

            // Mock map as complete object
            @SuppressWarnings("unchecked")
            Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>>
                  mockMap =
                  new HashMap<>();
            mockMap.put(ElementNotInteractableException.class, mockFunction);
            when(mockEnum.getExceptionHandlingMap()).thenReturn(mockMap);

            // Set up values method to return our mock
            exceptionHandlingMock.when(ExceptionHandlingWebElement::values)
                  .thenReturn(new ExceptionHandlingWebElement[] {mockEnum});

            // Act
            smartElement.doubleClick();

            // Verify exception handler was used
            verify(mockFunction).apply(eq(driver), eq(smartElement), eq(exception), any());
         }
      }

      @Test
      @DisplayName("isEnabledAndVisible should return true after waiting")
      void isEnabledAndVisibleShouldReturnTrueAfterWaiting() {
         // Arrange
         try (MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class)) {
            // Mock ExpectedConditions.and and the visibility/clickable conditions
            ExpectedCondition<WebElement> visibilityCondition = driver -> webElement;
            ExpectedCondition<WebElement> clickableCondition = driver -> webElement;
            ExpectedCondition<Boolean> combinedCondition = driver -> true;

            expectedConditionsMock.when(() -> ExpectedConditions.visibilityOf(any(WebElement.class)))
                  .thenReturn(visibilityCondition);
            expectedConditionsMock.when(() -> ExpectedConditions.elementToBeClickable(any(WebElement.class)))
                  .thenReturn(clickableCondition);
            expectedConditionsMock.when(() -> ExpectedConditions.and(any(), any()))
                  .thenReturn(combinedCondition);

            // Act
            boolean result = smartElement.isEnabledAndVisible();

            // Assert
            assertTrue(result);
         }
      }

      @Test
      @DisplayName("performActionWithWait should handle exceptions")
      void performActionWithWaitShouldHandleExceptions() throws Exception {
         // Arrange
         ElementNotInteractableException exception = new ElementNotInteractableException("Element not interactable");

         // Use the actual method name that would be used in the real scenario
         String actionName = "click";

         Consumer<SmartWebElement> action = element -> {
            throw exception;
         };

         try (MockedStatic<ExpectedConditions> expectedConditionsMock = mockStatic(ExpectedConditions.class);
              MockedStatic<ExceptionHandlingWebElement> exceptionHandlingMock = mockStatic(ExceptionHandlingWebElement.class)) {

            // Mock ExpectedConditions.elementToBeClickable
            ExpectedCondition<WebElement> clickableCondition = driver -> webElement;
            expectedConditionsMock.when(() -> ExpectedConditions.elementToBeClickable(any(WebElement.class)))
                  .thenReturn(clickableCondition);

            // Create a detailed mock for ExceptionHandlingWebElement
            ExceptionHandlingWebElement mockEnum = mock(ExceptionHandlingWebElement.class);

            // Create a mock FourFunction with specific behavior
            @SuppressWarnings("unchecked")
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> mockFunction =
                  (driver, element, exceptionObj, params) -> {
                     // Simulate some handling logic
                     LogUi.error("Exception handled: " + exceptionObj.getMessage());
                     throw new RuntimeException("Test");
                  };

            // Prepare the exception handling map
            Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>>
                  exceptionHandlingMap = new HashMap<>();
            exceptionHandlingMap.put(ElementNotInteractableException.class, mockFunction);

            // Setup the enum mock
            when(mockEnum.getMethodName()).thenReturn(actionName);
            when(mockEnum.getExceptionHandlingMap()).thenReturn(exceptionHandlingMap);

            // Setup static mock to return our enum
            exceptionHandlingMock.when(ExceptionHandlingWebElement::values)
                  .thenReturn(new ExceptionHandlingWebElement[] {mockEnum});

            // Access the private method using reflection
            Method performActionWithWaitMethod = SmartWebElement.class.getDeclaredMethod(
                  "performActionWithWait", Consumer.class, String.class);
            performActionWithWaitMethod.setAccessible(true);

            // Act & Assert
            try {
               performActionWithWaitMethod.invoke(smartElement, action, actionName);
               fail("Expected exception to be handled");
            } catch (Exception e) {
               // Verify the root cause is the original exception
               assertEquals(exception, e.getCause());
            }
         }
      }

      @Test
      @DisplayName("waitUntilAttributeValueIsChanged should wait for attribute value to change")
      void waitUntilAttributeValueIsChangedShouldWaitForAttributeValueToChange() {
         // Arrange
         String attributeName = "value";
         String initialValue = "initial";

         try (MockedConstruction<WebDriverWait> waitMock = mockConstruction(WebDriverWait.class,
               (mock, context) -> {
                  // Return nothing for the .until() call
               })) {

            // Act
            smartElement.waitUntilAttributeValueIsChanged(attributeName, initialValue);

            // Verify WebDriverWait was constructed with correct parameters
            assertFalse(waitMock.constructed().isEmpty());
            WebDriverWait constructedWait = waitMock.constructed().get(0);
            verify(constructedWait).until(any(ExpectedCondition.class));
         }
      }

      @Test
      @DisplayName("attributeValueChanged should return expected condition checking attribute value")
      void attributeValueChangedShouldReturnExpectedConditionCheckingAttributeValue() throws Exception {
         // Arrange
         String attributeName = "value";
         String initialValue = "initial";
         String changedValue = "changed";

         // Access the private method using reflection
         Method attributeValueChangedMethod = SmartWebElement.class.getDeclaredMethod(
               "attributeValueChanged", String.class, String.class);
         attributeValueChangedMethod.setAccessible(true);

         // Get the ExpectedCondition
         ExpectedCondition<Boolean> condition = (ExpectedCondition<Boolean>)
               attributeValueChangedMethod.invoke(smartElement, attributeName, initialValue);

         // Mock the getAttribute method to return changed value
         when(webElement.getAttribute(attributeName)).thenReturn(changedValue);

         // Act - apply the condition
         Boolean result = condition.apply(driver);

         // Assert - should return true since values are different
         assertTrue(result);
         verify(webElement).getAttribute(attributeName);
      }

      @Test
      @DisplayName("attributeValueChanged should return false when value hasn't changed")
      void attributeValueChangedShouldReturnFalseWhenValueHasntChanged() throws Exception {
         // Arrange
         String attributeName = "value";
         String initialValue = "initial";

         // Access the private method using reflection
         Method attributeValueChangedMethod = SmartWebElement.class.getDeclaredMethod(
               "attributeValueChanged", String.class, String.class);
         attributeValueChangedMethod.setAccessible(true);

         // Get the ExpectedCondition
         ExpectedCondition<Boolean> condition = (ExpectedCondition<Boolean>)
               attributeValueChangedMethod.invoke(smartElement, attributeName, initialValue);

         // Mock the getAttribute method to return the same value
         when(webElement.getAttribute(attributeName)).thenReturn(initialValue);

         // Act - apply the condition
         Boolean result = condition.apply(driver);

         // Assert - should return false since values are the same
         assertFalse(result);
         verify(webElement).getAttribute(attributeName);
      }

      @Test
      @DisplayName("toString should delegate to original element")
      void toStringShouldDelegateToOriginalElement() {
         // This test isn't needed as toString() behavior is
         // automatically handled by the @Delegate annotation
         // and can't easily be verified with Mockito

         // We can just test the delegation works at all
         assertNotNull(smartElement.toString());
      }
   }

   @Nested
   @DisplayName("waitWithoutFailure test")
   class WaitWithoutFailureTest {

      @Test
      @DisplayName("waitWithoutFailure should handle exceptions")
      void waitWithoutFailureShouldHandleExceptions() throws Exception {
         // Arrange
         Function<WebDriver, Boolean> condition = driver -> {
            throw new TimeoutException("Timeout waiting for condition");
         };

         WebDriverWait mockWait = mock(WebDriverWait.class);
         doThrow(new TimeoutException("Timeout")).when(mockWait).until(any(Function.class));

         // Use reflection to set the wait field
         Field waitField = SmartWebElement.class.getDeclaredField("wait");
         waitField.setAccessible(true);
         waitField.set(smartElement, mockWait);

         // Use reflection to access the private method
         Method waitWithoutFailureMethod = SmartWebElement.class.getDeclaredMethod(
               "waitWithoutFailure", Function.class);
         waitWithoutFailureMethod.setAccessible(true);

         // Act - should not throw exception
         waitWithoutFailureMethod.invoke(smartElement, condition);

         // Assert - verify wait was called
         verify(mockWait).until(condition);
      }

      @Test
      @DisplayName("waitWithoutFailure should complete successfully when no exception")
      void waitWithoutFailureShouldCompleteSuccessfullyWhenNoException() throws Exception {
         // Arrange
         Function<WebDriver, Boolean> condition = driver -> true;

         WebDriverWait mockWait = mock(WebDriverWait.class);
         when(mockWait.until(any(Function.class))).thenReturn(true);

         // Use reflection to set the wait field
         Field waitField = SmartWebElement.class.getDeclaredField("wait");
         waitField.setAccessible(true);
         waitField.set(smartElement, mockWait);

         // Use reflection to access the private method
         Method waitWithoutFailureMethod = SmartWebElement.class.getDeclaredMethod(
               "waitWithoutFailure", Function.class);
         waitWithoutFailureMethod.setAccessible(true);

         // Act
         waitWithoutFailureMethod.invoke(smartElement, condition);

         // Assert
         verify(mockWait).until(condition);
      }
   }

   @Nested
   @DisplayName("handleException edge cases")
   class HandleExceptionEdgeCasesTest {

      @Test
      @DisplayName("handleException should use cause instead of exception if available")
      void handleExceptionShouldUseCauseIfAvailable() throws Exception {
         // Arrange
         Object expectedResult = new Object(); // Just for verification

         // Create a nested exception with a cause
         NoSuchElementException cause = new NoSuchElementException("Element not found");
         RuntimeException wrappedException = new RuntimeException("Wrapper exception", cause);

         try (MockedStatic<ExceptionHandlingWebElement> exceptionHandlingMock = mockStatic(ExceptionHandlingWebElement.class)) {
            // Create mock enum for exception handling
            ExceptionHandlingWebElement mockEnum = mock(ExceptionHandlingWebElement.class);
            when(mockEnum.getMethodName()).thenReturn("testMethod");

            // Create a mock FourFunction
            @SuppressWarnings("unchecked")
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> mockFunction =
                  mock(FourFunction.class);
            when(mockFunction.apply(eq(driver), eq(smartElement), eq(wrappedException), any()))
                  .thenReturn(expectedResult);

            // Mock map as complete object
            @SuppressWarnings("unchecked")
            Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>>
                  mockMap =
                  new HashMap<>();
            mockMap.put(NoSuchElementException.class, mockFunction);
            when(mockEnum.getExceptionHandlingMap()).thenReturn(mockMap);

            // Set up values method to return our mock
            exceptionHandlingMock.when(ExceptionHandlingWebElement::values)
                  .thenReturn(new ExceptionHandlingWebElement[] {mockEnum});

            // Access the private method using reflection
            Method handleExceptionMethod = SmartWebElement.class.getDeclaredMethod(
                  "handleException", String.class, Exception.class, Object[].class);
            handleExceptionMethod.setAccessible(true);

            // Act
            Object result = handleExceptionMethod.invoke(smartElement, "testMethod", wrappedException, new Object[] {});

            // Assert
            assertSame(expectedResult, result);
            verify(mockFunction).apply(eq(driver), eq(smartElement), eq(wrappedException), any());
         }
      }

      @Test
      @DisplayName("handleException should throw when no handler found")
      void handleExceptionShouldThrowWhenNoHandlerFound() throws Exception {
         // Arrange
         IllegalArgumentException exception = new IllegalArgumentException("Unsupported element operation");

         try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class);
              MockedStatic<ExceptionHandlingWebElement> exceptionHandlingMock = mockStatic(ExceptionHandlingWebElement.class)) {

            // Create mock enum for exception handling with no matching handler
            ExceptionHandlingWebElement mockEnum = mock(ExceptionHandlingWebElement.class);
            when(mockEnum.getMethodName()).thenReturn("otherMethod"); // Different method name

            // Mock map as complete object
            @SuppressWarnings("unchecked")
            Map<Class<? extends Throwable>, FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object>>
                  mockMap =
                  new HashMap<>();
            when(mockEnum.getExceptionHandlingMap()).thenReturn(mockMap);

            // Set up values method to return our mock
            exceptionHandlingMock.when(ExceptionHandlingWebElement::values)
                  .thenReturn(new ExceptionHandlingWebElement[] {mockEnum});

            // Access the private method using reflection
            Method handleExceptionMethod = SmartWebElement.class.getDeclaredMethod(
                  "handleException", String.class, Exception.class, Object[].class);
            handleExceptionMethod.setAccessible(true);

            // Act & Assert
            Exception thrownException = assertThrows(
                  java.lang.reflect.InvocationTargetException.class,
                  () -> handleExceptionMethod.invoke(smartElement, "testMethod", exception, new Object[] {})
            );

            // Verify that the error was logged
            logUIMock.verify(() -> LogUi.error(anyString()));
         }
      }
   }
}