package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.BaseUnitUITest;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.FrameHelper;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.theairebellion.zeus.ui.selenium.handling.ExceptionHandlingWebElementFunctions.handleNoSuchElement;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ExceptionHandlingWebElementFunctionsTest extends BaseUnitUITest {

   @Mock
   private WebDriver mockDriver;

   @Mock
   private WebElement mockWebElement;


   @Test
   @DisplayName("handleStaleElement - Should successfully update and handle element with arguments")
   void testHandleStaleElementWithArgs() {
      try (MockedStatic<LocatorParser> mockedParser = mockStatic(LocatorParser.class)) {
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);
         String arg = "test-arg";

         // Setup mocks
         mockedParser.when(() -> LocatorParser.updateWebElement(any(WebDriver.class), any(SmartWebElement.class)))
               .thenReturn(element);

         // Call the method
         ExceptionHandlingWebElementFunctions.handleStaleElement(mockDriver, element, WebElementAction.SEND_KEYS, arg);

         // Verify the static method was called
         mockedParser.verify(() -> LocatorParser.updateWebElement(eq(mockDriver), eq(element)));
      }
   }

   @Test
   @DisplayName("handleStaleElement - Should throw exception and log error when element update fails")
   void testHandleStaleElement_ExceptionPath() {
      try (MockedStatic<LocatorParser> mockedParser = mockStatic(LocatorParser.class);
           MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)) {

         // Setup test elements
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);

         // Force exception from LocatorParser
         RuntimeException testException = new RuntimeException("Test exception");
         mockedParser.when(() -> LocatorParser.updateWebElement(any(), any()))
               .thenThrow(testException);

         // Execute and verify exception
         assertThrows(RuntimeException.class, () ->
               ExceptionHandlingWebElementFunctions.handleStaleElement(
                     mockDriver, element, WebElementAction.CLICK));

         // Verify error was logged
         mockedLogUi.verify(() -> LogUi.error(any()));
      }
   }


   @Test
   @DisplayName("handleNoSuchElement - Should throw IllegalArgumentException for invalid arguments")
   void testHandleNoSuchElementWithInvalidArgs() {
      try (MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)) {
         // Create test elements
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);

         // Test with various invalid args
         // Empty args
         Object[] emptyArgs = new Object[0];
         IllegalArgumentException emptyException = assertThrows(IllegalArgumentException.class, () -> {
            handleNoSuchElement(mockDriver, element,
                  WebElementAction.FIND_ELEMENT, emptyArgs);
         });
         assertNotNull(emptyException.getMessage());
         assertFalse(emptyException.getMessage().isEmpty());

         // Null args
         Object[] nullArgs = new Object[] {null};
         IllegalArgumentException nullException = assertThrows(IllegalArgumentException.class, () -> {
            handleNoSuchElement(mockDriver, element,
                  WebElementAction.FIND_ELEMENT, nullArgs);
         });
         assertNotNull(nullException.getMessage());
         assertFalse(nullException.getMessage().isEmpty());

         // Non-By args
         Object[] invalidArgs = new Object[] {"not a By object"};
         IllegalArgumentException invalidException = assertThrows(IllegalArgumentException.class, () -> {
            handleNoSuchElement(mockDriver, element,
                  WebElementAction.FIND_ELEMENT, invalidArgs);
         });
         assertNotNull(invalidException.getMessage());
         assertFalse(invalidException.getMessage().isEmpty());

         // Verify error was logged at least once
         mockedLogUi.verify(() -> LogUi.error(anyString()), atLeastOnce());
      }
   }


   @Test
   @DisplayName("handleNoSuchElement - Should successfully find element in iframes when element exists")
   void testHandleNoSuchElementWhenElementFound() {
      try (MockedStatic<FrameHelper> mockedHelper = mockStatic(FrameHelper.class)) {
         // Create test elements
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);

         By byLocator = By.id("testId");
         Object[] args = new Object[] {byLocator};

         // Setup mocks
         mockedHelper.when(() -> FrameHelper.findElementInIFrames(any(WebDriver.class), any(WebElement.class)))
               .thenReturn(element);
         WebElementAction action = WebElementAction.FIND_ELEMENT;
         ExceptionHandlingWebElementFunctions.handleNoSuchElement(mockDriver, element, action, args);

         // Verify method was called
         mockedHelper.verify(() -> FrameHelper.findElementInIFrames(eq(mockDriver), eq(mockWebElement)));
      }
   }


   @Test
   @DisplayName("handleNoSuchElement - Should throw NoSuchElementException when element not found in iframes")
   void testHandleNoSuchElementWhenElementNotFound() {
      try (MockedStatic<FrameHelper> mockedHelper = mockStatic(FrameHelper.class);
           MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)
      ) {

         // Create test elements
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);

         By byLocator = By.id("testId");
         Object[] args = new Object[] {byLocator};

         // Setup mocks
         mockedHelper.when(() -> FrameHelper.findElementInIFrames(any(WebDriver.class), any(WebElement.class)))
               .thenReturn(null);

         // Execute method and expect exception
         assertThrows(NoSuchElementException.class, () -> {
            handleNoSuchElement(mockDriver, element,
                  WebElementAction.FIND_ELEMENT, args);
         });

         // Verify methods were called
         mockedHelper.verify(() -> FrameHelper.findElementInIFrames(eq(mockDriver), eq(mockWebElement)));
         mockedLogUi.verify(() -> LogUi.error(any()));
      }
   }

   @Test
   @DisplayName("Should handle FIND_ELEMENTS action by locating container iframe first")
   void testHandleNoSuchElementWithFindContainerReturnsElement() {
      try (MockedStatic<FrameHelper> mockedHelper = mockStatic(FrameHelper.class)) {
         // Given
         By locator = By.id("testId");
         Object[] args = new Object[] {locator};
         SmartWebElement element = mock(SmartWebElement.class);
         mockedHelper.when(() -> FrameHelper.findContainerIFrame(any(), any()))
               .thenReturn(mock(WebElement.class));

         // When
         Object result = ExceptionHandlingWebElementFunctions.handleNoSuchElement(
               mockDriver, element, WebElementAction.FIND_ELEMENTS, args);

         // Then
         mockedHelper.verify(() -> FrameHelper.findContainerIFrame(eq(mockDriver), eq(locator)));
         assertNotNull(result);
         assertTrue(result instanceof List);
      }
   }

   @Test
   @DisplayName("Should throw exception when container is null")
   void testHandleNoSuchElementWithFindContainerNull() {
      try (MockedStatic<FrameHelper> mockedHelper = mockStatic(FrameHelper.class);
           MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)) {
         // Given
         By locator = By.id("testId");
         Object[] args = new Object[] {locator};
         SmartWebElement element = mock(SmartWebElement.class);

         // When
         mockedHelper.when(() -> FrameHelper.findContainerIFrame(any(), any()))
               .thenReturn(null);

         // Then
         assertThrows(NoSuchElementException.class, () -> ExceptionHandlingWebElementFunctions.handleNoSuchElement(mockDriver, element, WebElementAction.FIND_ELEMENTS, args));
         mockedHelper.verify(() -> FrameHelper.findContainerIFrame(any(WebDriver.class), any(By.class)));
         mockedLogUi.verify(() -> LogUi.error(anyString()));
      }
   }


   @Test
   @DisplayName("handleElementClickIntercepted - Should handle null exception message and log error")
   void testHandleElementClickIntercepted_NullExceptionMessage() {
      try (MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)) {

         // Create test elements
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);

         // Create exception with null message
         Exception exception = mock(Exception.class);
         when(exception.getMessage()).thenReturn(null);


         assertThrows(Exception.class,
               () -> ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(
                     mockDriver, element, WebElementAction.CLICK, exception));

         mockedLogUi.verify(() -> LogUi.error(any()));
      }
   }


   @Test
   @DisplayName("handleElementClickIntercepted - Should handle locator parsing")
   void testHandleElementClickIntercepted_ExceptionWithLocator() {
      try (MockedStatic<LocatorParser> mockedParser = mockStatic(LocatorParser.class);
           MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class);
           MockedStatic<By> mockedBy = mockStatic(By.class)) {

         // Create test elements
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);

         String testLocator = "//div[@id='blocker']";
         mockedParser.when(() -> LocatorParser.extractBlockingElementLocator(any())).thenReturn(testLocator);

         By mockBlocker = mock(By.class);
         mockedBy.when(() -> By.xpath(testLocator)).thenReturn(mockBlocker);

         // Create exception with null message
         Exception exception = mock(Exception.class);
         when(exception.getMessage()).thenReturn(null);


         assertThrows(Exception.class,
               () -> ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(
                     mockDriver, element, WebElementAction.CLICK, exception));

         mockedLogUi.verify(() -> LogUi.error(contains(testLocator)));
      }
   }

   @Test
   @DisplayName("handleElementClickIntercepted - Should handle timeout exception and retry click")
   void testHandleElementClickIntercepted_TimeoutExceptionBranch() {
      try (MockedStatic<LocatorParser> mockedParser = mockStatic(LocatorParser.class);
           MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class);
           MockedStatic<By> mockedBy = mockStatic(By.class);
           MockedStatic<WebElementAction> mockedWebElementAction = mockStatic(WebElementAction.class);
           MockedConstruction<WebDriverWait> mocked = mockConstruction(
                 WebDriverWait.class,
                 (mockWait, context) -> configureMockWait(mockWait, true, false)
           )) {

         // Create test elements
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);

         // Setup exception with message
         ElementClickInterceptedException clickException = mock(ElementClickInterceptedException.class);
         when(clickException.getMessage()).thenReturn("Element is blocked");

         // Setup mocks for locator extraction
         String testLocator = "//div[@id='blocker']";
         mockedParser.when(() -> LocatorParser.extractBlockingElementLocator(any())).thenReturn(testLocator);

         // Mock By.xpath
         By mockBlocker = mock(By.class);
         mockedBy.when(() -> By.xpath(testLocator)).thenReturn(mockBlocker);

         // Execute the method
         ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(
               mockDriver, element, WebElementAction.CLICK, clickException);

         // Verify the correct warning was logged
         mockedLogUi.verify(() -> LogUi.warn(any()));
         mockedWebElementAction.verify(() -> WebElementAction.performAction(mockDriver, mockWebElement, WebElementAction.CLICK));
      }
   }

   @Test
   @DisplayName("handleElementClickIntercepted - Should continue execution after timeout when element is blocked")
   void testHandleElementClickIntercepted_TimeoutHandling() {
      try (MockedStatic<LocatorParser> mockedParser = mockStatic(LocatorParser.class);
           MockedStatic<By> mockedBy = mockStatic(By.class);
           MockedConstruction<WebDriverWait> mocked = mockConstruction(
                 WebDriverWait.class,
                 (mockWait, context) -> configureMockWait(mockWait, false, false)
           )) {

         // Setup test elements
         SmartWebElement element = mock(SmartWebElement.class);
         when(element.getOriginal()).thenReturn(mockWebElement);

         // Setup exception with message
         ElementClickInterceptedException clickException =
               new ElementClickInterceptedException("Element blocked by: //div[@id='overlay']");

         // Setup parser to return locator
         String testLocator = "//div[@id='overlay']";
         mockedParser.when(() -> LocatorParser.extractBlockingElementLocator(anyString()))
               .thenReturn(testLocator);

         // Mock By.xpath
         By mockBlocker = mock(By.class);
         mockedBy.when(() -> By.xpath(testLocator)).thenReturn(mockBlocker);

         // Execute - should not throw since we continue after timeout
         assertDoesNotThrow(() ->
               ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(
                     mockDriver, element, WebElementAction.CLICK, clickException));
      }
   }


   @Test
   @DisplayName("handleElementNotInteractable - Should successfully update and interact with element")
   void testHandleElementNotInteractable_CompleteFlow() {
      try (MockedStatic<LocatorParser> mockedParser = mockStatic(LocatorParser.class);
           MockedConstruction<Actions> mockedActions = mockConstruction(Actions.class,
                 (mock, context) -> {
                    when(mock.moveToElement(any())).thenReturn(mock);
                    doNothing().when(mock).perform();
                 });
           MockedConstruction<WebDriverWait> mocked = mockConstruction(
                 WebDriverWait.class,
                 (mockWait, context) -> configureMockWait(mockWait, false, false)
           )) {
         // Setup mocks
         SmartWebElement element = mock(SmartWebElement.class);
         SmartWebElement updatedElement = mock(SmartWebElement.class);

         when(element.getOriginal()).thenReturn(mockWebElement);
         when(updatedElement.getOriginal()).thenReturn(mockWebElement);

         mockedParser.when(() -> LocatorParser.updateWebElement(any(), any()))
               .thenReturn(updatedElement);
         Object result = ExceptionHandlingWebElementFunctions.handleElementNotInteractable(
               mockDriver, element, WebElementAction.CLICK
         );

         // Verify that LocatorParser.updateWebElement was called
         mockedParser.verify(() -> LocatorParser.updateWebElement(eq(mockDriver), eq(element)));
         assertNull(result);
      }
   }


   @Test
   @DisplayName("handleElementNotInteractable - Should throw and log exception when interaction fails")
   void testHandleElementNotInteractable_GeneralException() {
      try (MockedStatic<LocatorParser> mockedParser = mockStatic(LocatorParser.class);
           MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class);
           MockedConstruction<Actions> mockedActions = mockConstruction(Actions.class,
                 (mock, context) -> {
                    RuntimeException generalException = new RuntimeException("Test exception");
                    doThrow(generalException).when(mock).moveToElement(any());
                 })) {

         // Setup element mocks
         SmartWebElement element = mock(SmartWebElement.class);
         SmartWebElement updatedElement = mock(SmartWebElement.class);

         when(element.getOriginal()).thenReturn(mockWebElement);
         when(updatedElement.getOriginal()).thenReturn(mockWebElement);

         mockedParser.when(() -> LocatorParser.updateWebElement(any(), any()))
               .thenReturn(updatedElement);


         // Run the test to hit the general exception path
         assertThrows(RuntimeException.class,
               () -> ExceptionHandlingWebElementFunctions.handleElementNotInteractable(
                     mockDriver, element, WebElementAction.CLICK));

         // Verify the warning was logged with the message
         mockedLogUi.verify(() -> LogUi.error(any()));
      }
   }

   private void configureMockWait(
         WebDriverWait mockWait,
         boolean shouldThrowTimeout,
         boolean shouldThrowException
   ) {
      if (shouldThrowTimeout) {
         when(mockWait.until(any()))
               .thenThrow(new TimeoutException("Test timeout"));
      } else if (shouldThrowException) {
         when(mockWait.until(any()))
               .thenThrow(new RuntimeException("Test exception"));
      } else {
         when(mockWait.until(any()))
               .thenReturn(mock(WebElement.class));
      }
   }

}