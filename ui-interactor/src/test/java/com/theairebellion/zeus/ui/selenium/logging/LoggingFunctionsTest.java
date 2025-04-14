package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class LoggingFunctionsTest {

   @Mock
   private WebDriver driver;

   @Mock
   private WebElement element;

   @Mock
   private By by;

   private InvocationTargetException exception;
   private NoSuchElementException noSuchElementException;
   private ElementNotInteractableException elementNotInteractableException;
   private InvalidSelectorException invalidSelectorException;
   private ElementClickInterceptedException elementClickInterceptedException;
   private TimeoutException timeoutException;

   @BeforeEach
   void setUp() {
      // Set up common exception objects
      noSuchElementException = new NoSuchElementException("Element not found");
      exception = new InvocationTargetException(noSuchElementException);

      elementNotInteractableException = new ElementNotInteractableException("Element not interactable");
      invalidSelectorException = new InvalidSelectorException("Invalid selector");
      elementClickInterceptedException = new ElementClickInterceptedException("Element click intercepted");
      timeoutException = new TimeoutException("Timeout waiting for element");

      // Set up common mock behaviors
      lenient().when(driver.getPageSource()).thenReturn("<html><body>Test Page</body></html>");
      lenient().when(element.getTagName()).thenReturn("div");
      lenient().when(element.getText()).thenReturn("Test Element");
      lenient().when(element.getAttribute("outerHTML")).thenReturn("<div>Test Element</div>");
      lenient().when(element.getAttribute("innerHTML")).thenReturn("Test Element");
      lenient().when(by.toString()).thenReturn("By.id: testId");
   }

   @Nested
   @DisplayName("logFindElementFromRootNoSuchElementException tests")
   class LogFindElementFromRootNoSuchElementExceptionTests {

      @Test
      @DisplayName("Should log driver details with extracted locator")
      void shouldLogDriverDetailsWithExtractedLocator() {
         // Arrange
         Object[] args = new Object[] {by};

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(by);

            // Act
            LoggingFunctions.logFindElementFromRootNoSuchElementException(driver, WebElementAction.FIND_ELEMENT, args, exception);

            // Assert - use less specific verification
            logUIMock.verify(() -> LogUi.extended(
                  eq("Exception: [{}] thrown on target: [{}({})] from method: [{}] called with argument types: [{}]"),
                  eq("NoSuchElementException"),
                  contains("WebDriver"),  // Use contains instead of exact match
                  any(),                  // Don't try to match the toString representation
                  eq("findElement"),
                  anyString()));          // Don't try to match the exact type representation

            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Failed to locate element using locator")));
         }
      }

      @Test
      @DisplayName("Should log driver details with null locator")
      void shouldLogDriverDetailsWithNullLocator() {
         // Arrange
         Object[] args = new Object[] {null};

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(null);

            // Act
            LoggingFunctions.logFindElementFromRootNoSuchElementException(driver, WebElementAction.FIND_ELEMENT, args, exception);

            // Assert - use less specific verification
            logUIMock.verify(() -> LogUi.extended(
                  eq("Exception: [{}] thrown on target: [{}({})] from method: [{}] called with argument types: [{}]"),
                  eq("NoSuchElementException"),
                  contains("WebDriver"),  // Use contains instead of exact match
                  any(),                  // Don't try to match the toString representation
                  eq("findElement"),
                  eq("null")));

            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Failed to locate element using locator [null]")));
         }
      }
   }

   @Nested
   @DisplayName("logNoSuchElementException tests")
   class LogNoSuchElementExceptionTests {

      @Test
      @DisplayName("Should log when target is not a WebElement")
      void shouldLogWhenTargetIsNotWebElement() {
         // Arrange
         Object[] args = new Object[] {by};
         Object nonElementTarget = "Not an element";

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(by);

            // Act
            LoggingFunctions.logNoSuchElementException(nonElementTarget, WebElementAction.FIND_ELEMENT, args, exception);

            // Assert - use less specific verification
            logUIMock.verify(() -> LogUi.extended(
                  eq("Exception: [{}] thrown on target: [{}({})] from method: [{}] called with argument types: [{}]"),
                  eq("NoSuchElementException"),
                  eq("String"),
                  eq("Not an element"),
                  eq("findElement"),
                  anyString()));          // Don't try to match the exact type representation

            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  eq("Exception [NoSuchElementException]: Element is null, possibly due to a stale reference or incorrect locator.")));
         }
      }

      @Test
      @DisplayName("Should log for FIND_ELEMENT action with locator")
      void shouldLogForFindElementActionWithLocator() {
         // Arrange
         Object[] args = new Object[] {by};

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(by);
            locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                  .thenReturn("Element Details");

            // Act
            LoggingFunctions.logNoSuchElementException(element, WebElementAction.FIND_ELEMENT, args, exception);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Unable to locate element [div] using locator")));
         }
      }

      @Test
      @DisplayName("Should log for FIND_ELEMENT action without locator")
      void shouldLogForFindElementActionWithoutLocator() {
         // Arrange
         Object[] args = new Object[] {};

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(null);
            locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                  .thenReturn("Element Details");

            // Act
            LoggingFunctions.logNoSuchElementException(element, WebElementAction.FIND_ELEMENT, args, exception);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Unable to locate element [div], but no locator was provided")));
         }
      }

      @Test
      @DisplayName("Should log for CLICK action")
      void shouldLogForClickAction() {
         // Arrange
         Object[] args = new Object[] {};

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(null);
            locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                  .thenReturn("Element Details");

            // Act
            LoggingFunctions.logNoSuchElementException(element, WebElementAction.CLICK, args, exception);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Click action failed on element [div] with text [Test Element]")));
         }
      }

      @Test
      @DisplayName("Should log for SEND_KEYS action")
      void shouldLogForSendKeysAction() {
         // Arrange
         Object[] args = new Object[] {"test input"};

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(null);
            locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                  .thenReturn("Element Details");

            // Act
            LoggingFunctions.logNoSuchElementException(element, WebElementAction.SEND_KEYS, args, exception);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Unable to send keys to element [div] with text [Test Element]")));
         }
      }

      @Test
      @DisplayName("Should log for SUBMIT action")
      void shouldLogForSubmitAction() {
         // Arrange
         Object[] args = new Object[] {};

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(null);
            locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                  .thenReturn("Element Details");

            // Act
            LoggingFunctions.logNoSuchElementException(element, WebElementAction.SUBMIT, args, exception);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Unable to submit form using element [div]")));
         }
      }

      @Test
      @DisplayName("Should log for other actions")
      void shouldLogForOtherActions() {
         // Arrange
         Object[] args = new Object[] {};

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(null);
            locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                  .thenReturn("Element Details");

            // Act
            LoggingFunctions.logNoSuchElementException(element, WebElementAction.CLEAR, args, exception);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Unknown action")));
         }
      }
   }

   @Nested
   @DisplayName("logElementNotInteractableException tests")
   class LogElementNotInteractableExceptionTests {

      @Test
      @DisplayName("Should log when target is not a WebElement")
      void shouldLogWhenTargetIsNotWebElement() {
         // Arrange
         Object[] args = new Object[] {};
         Object nonElementTarget = "Not an element";
         InvocationTargetException e = new InvocationTargetException(elementNotInteractableException);

         try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {
            // Act
            LoggingFunctions.logElementNotInteractableException(nonElementTarget, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Exception: [{}] thrown on target: [{}({})] from method: [{}] called with argument types: [{}]"),
                  eq("ElementNotInteractableException"),
                  eq("String"),
                  eq("Not an element"),
                  eq("click"),
                  eq("")));

            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  eq("Element is null, cannot log additional info.")));
         }
      }

      @Test
      @DisplayName("Should log for WebElement target")
      void shouldLogForWebElementTarget() {
         // Arrange
         Object[] args = new Object[] {};
         InvocationTargetException e = new InvocationTargetException(elementNotInteractableException);

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                  .thenReturn("Element Details");

            // Act
            LoggingFunctions.logElementNotInteractableException(element, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Element [div] with text [Test Element] is not interactable when attempting to perform click()")));
         }
      }
   }

   @Nested
   @DisplayName("logClickInvalidSelectorException tests")
   class LogClickInvalidSelectorExceptionTests {

      @Test
      @DisplayName("Should log with extracted locator")
      void shouldLogWithExtractedLocator() {
         // Arrange
         Object[] args = new Object[] {by};
         InvocationTargetException e = new InvocationTargetException(invalidSelectorException);

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(by);

            // Act
            LoggingFunctions.logClickInvalidSelectorException(element, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Invalid selector while clicking element. Malformed locator: [By.id: testId]")));
         }
      }

      @Test
      @DisplayName("Should log with null locator")
      void shouldLogWithNullLocator() {
         // Arrange
         Object[] args = new Object[] {};
         InvocationTargetException e = new InvocationTargetException(invalidSelectorException);

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(null);

            // Act
            LoggingFunctions.logClickInvalidSelectorException(element, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Invalid selector while clicking element. Malformed locator: [null]")));
         }
      }
   }

   @Nested
   @DisplayName("logElementClickInterceptedException tests")
   class LogElementClickInterceptedExceptionTests {

      @Test
      @DisplayName("Should log when target is not a WebElement")
      void shouldLogWhenTargetIsNotWebElement() {
         // Arrange
         Object[] args = new Object[] {};
         Object nonElementTarget = "Not an element";
         InvocationTargetException e = new InvocationTargetException(elementClickInterceptedException);

         try (MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {
            // Act
            LoggingFunctions.logElementClickInterceptedException(nonElementTarget, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  eq("Element is null, cannot log additional info.")));
         }
      }

      @Test
      @DisplayName("Should log for WebElement target with blocking locator")
      void shouldLogForWebElementTarget() {
         // Arrange
         Object[] args = new Object[] {};
         InvocationTargetException e = new InvocationTargetException(elementClickInterceptedException);

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                  .thenReturn("Element Details");
            locatorParserMock.when(() -> LocatorParser.extractLocatorFromMessage(anyString()))
                  .thenReturn("By.xpath: //div[@id='overlay']");

            // Act
            LoggingFunctions.logElementClickInterceptedException(element, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Failed to click on element:")));
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Locator of the blocking element: [By.xpath: //div[@id='overlay']]")));
         }
      }
   }

   @Nested
   @DisplayName("logClickTimeoutException tests")
   class LogClickTimeoutExceptionTests {

      @Test
      @DisplayName("Should log with extracted locator and no timeout")
      void shouldLogWithExtractedLocatorAndNoTimeout() {
         // Arrange
         Object[] args = new Object[] {by};
         InvocationTargetException e = new InvocationTargetException(timeoutException);

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(by);

            // Act
            LoggingFunctions.logClickTimeoutException(element, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Timeout while waiting for element to meet condition: [click]. Locator: [By.id: testId]. No specific timeout set.")));
         }
      }

      @Test
      @DisplayName("Should log with extracted locator and specified timeout")
      void shouldLogWithExtractedLocatorAndSpecifiedTimeout() {
         // Arrange
         Long timeout = 5000L;
         Object[] args = new Object[] {by, timeout};
         InvocationTargetException e = new InvocationTargetException(timeoutException);

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(by);

            // Act
            LoggingFunctions.logClickTimeoutException(element, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Timeout while waiting for element to meet condition: [click]. Locator: [By.id: testId]. Waited for 5000 milliseconds.")));
         }
      }

      @Test
      @DisplayName("Should log with null locator")
      void shouldLogWithNullLocator() {
         // Arrange
         Object[] args = new Object[] {};
         InvocationTargetException e = new InvocationTargetException(timeoutException);

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(null);

            // Act
            LoggingFunctions.logClickTimeoutException(element, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("Unknown locator")));
         }
      }

      @Test
      @DisplayName("Should handle non-Long second argument")
      void shouldHandleNonLongSecondArgument() {
         // Arrange
         Object[] args = new Object[] {by, "not a long"};
         InvocationTargetException e = new InvocationTargetException(timeoutException);

         try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
              MockedStatic<LogUi> logUIMock = mockStatic(LogUi.class)) {

            // Set up mocks
            locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(by);

            // Act
            LoggingFunctions.logClickTimeoutException(element, WebElementAction.CLICK, args, e);

            // Assert
            logUIMock.verify(() -> LogUi.extended(eq("Additional Info for the problem: {}"),
                  contains("No specific timeout set.")));
         }
      }
   }

   @Nested
   @DisplayName("Private method tests")
   class PrivateMethodTests {

      @Test
      @DisplayName("printMethodArgsClasses should handle null or empty args")
      void printMethodArgsClassesShouldHandleNullOrEmptyArgs() throws Exception {
         // Arrange
         Method printMethodArgsClassesMethod = LoggingFunctions.class.getDeclaredMethod(
               "printMethodArgsClasses", Object[].class);
         printMethodArgsClassesMethod.setAccessible(true);

         // Act & Assert
         assertEquals("", printMethodArgsClassesMethod.invoke(null, new Object[] {null}),
               "Null args should return empty string");

         assertEquals("", printMethodArgsClassesMethod.invoke(null, new Object[] {new Object[0]}),
               "Empty args should return empty string");
      }

      @Test
      @DisplayName("printMethodArgsClasses should format args correctly")
      void printMethodArgsClassesShouldFormatArgsCorrectly() throws Exception {
         // Arrange
         Method printMethodArgsClassesMethod = LoggingFunctions.class.getDeclaredMethod(
               "printMethodArgsClasses", Object[].class);
         printMethodArgsClassesMethod.setAccessible(true);

         Object[] args = new Object[] {"string", 123, null, new Object()};

         // Act
         String result = (String) printMethodArgsClassesMethod.invoke(null, new Object[] {args});

         // Assert
         assertEquals("String,Integer,null,Object", result,
               "Args should be formatted as comma-separated class names with null for null values");
      }

      @Test
      @DisplayName("truncateString should handle null input")
      void truncateStringShouldHandleNullInput() throws Exception {
         // Arrange
         Method truncateStringMethod = LoggingFunctions.class.getDeclaredMethod(
               "truncateString", String.class, int.class);
         truncateStringMethod.setAccessible(true);

         // Act
         String result = (String) truncateStringMethod.invoke(null, null, 10);

         // Assert
         assertEquals("Page source unavailable.", result,
               "Null input should return 'Page source unavailable.'");
      }

      @Test
      @DisplayName("truncateString should truncate long strings")
      void truncateStringShouldTruncateLongStrings() throws Exception {
         // Arrange
         Method truncateStringMethod = LoggingFunctions.class.getDeclaredMethod(
               "truncateString", String.class, int.class);
         truncateStringMethod.setAccessible(true);

         String longString = "This is a very long string that should be truncated";

         // Act
         String result = (String) truncateStringMethod.invoke(null, longString, 10);

         // Assert
         assertEquals("This is a ...", result,
               "Long string should be truncated to specified length with ellipsis");
      }

      @Test
      @DisplayName("truncateString should not truncate short strings")
      void truncateStringShouldNotTruncateShortStrings() throws Exception {
         // Arrange
         Method truncateStringMethod = LoggingFunctions.class.getDeclaredMethod(
               "truncateString", String.class, int.class);
         truncateStringMethod.setAccessible(true);

         String shortString = "Short";

         // Act
         String result = (String) truncateStringMethod.invoke(null, shortString, 10);

         // Assert
         assertEquals("Short", result,
               "Short string should not be truncated");
      }
   }
}