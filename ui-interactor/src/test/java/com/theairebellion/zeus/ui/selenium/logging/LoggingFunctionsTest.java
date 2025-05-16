package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.log.LogUI;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.LocatorParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class LoggingFunctionsTest {

    @Mock
    private WebDriver driver;

    @Mock
    private WebElement element;

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
    }

    @Nested
    @DisplayName("logFindElementFromRootNoSuchElementException tests")
    class LogFindElementFromRootNoSuchElementExceptionTests {

        @Test
        @DisplayName("Should log driver details with extracted locator")
        void shouldLogDriverDetailsWithExtractedLocator() {
            // Given
            By by = By.id("testId");
            Object[] args = new Object[]{by};

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logFindElementFromRootNoSuchElementException(driver, WebElementAction.FIND_ELEMENT, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(
                        contains("Exception: "),
                        eq("NoSuchElementException"),
                        contains("WebDriver"),
                        any(),
                        eq("findElement"),
                        anyString()));

                logUIMock.verify(() -> LogUI.extended(contains("Additional Info "),
                        contains("Failed to locate element using locator")));
            }
        }

        @Test
        @DisplayName("Should log driver details with null locator")
        void shouldLogDriverDetailsWithNullLocator() {
            // Given
            Object[] args = new Object[]{null};

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logFindElementFromRootNoSuchElementException(driver, WebElementAction.FIND_ELEMENT, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(
                        contains("Exception: "),
                        eq("NoSuchElementException"),
                        contains("WebDriver"),
                        any(),
                        eq("findElement"),
                        eq("null")));

                logUIMock.verify(() -> LogUI.extended(contains("Additional Info "),
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
            // Given
            By by = By.id("testId");
            Object[] args = new Object[]{by};
            Object nonElementTarget = "Not an element";

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logNoSuchElementException(nonElementTarget, WebElementAction.FIND_ELEMENT, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(
                        contains("Exception:"),
                        eq("NoSuchElementException"),
                        eq("String"),
                        eq("Not an element"),
                        eq("findElement"),
                        anyString()));

                logUIMock.verify(() -> LogUI.extended(contains("Additional Info "),
                        contains("Exception [NoSuchElementException]:")));
            }
        }

        @Test
        @DisplayName("Should log for FIND_ELEMENT action with locator")
        void shouldLogForFindElementActionWithLocator() {//
            // Given
            By by = By.id("testId");
            Object[] args = new Object[]{by};

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // Set up mocks
                locatorParserMock.when(() -> LocatorParser.extractLocator(args)).thenReturn(by);
                locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                        .thenReturn("Element Details");

                // When
                LoggingFunctions.logNoSuchElementException(element, WebElementAction.FIND_ELEMENT, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info "),
                        contains("Unable to locate element")));
            }
        }

        @Test
        @DisplayName("Should log for FIND_ELEMENT action without locator")
        void shouldLogForFindElementActionWithoutLocator() {//
            // Given
            Object[] args = new Object[]{};

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // Set up mocks
                locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                        .thenReturn("Element Details");

                // When
                LoggingFunctions.logNoSuchElementException(element, WebElementAction.FIND_ELEMENT, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info "),
                        contains("Unable to locate element")));
            }
        }

        @Test
        @DisplayName("Should log for CLICK action")
        void shouldLogForClickAction() {
            // Given
            Object[] args = new Object[]{};

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // Set up mocks
                locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                        .thenReturn("Element Details");

                // When
                LoggingFunctions.logNoSuchElementException(element, WebElementAction.CLICK, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Click action failed")));
            }
        }

        @Test
        @DisplayName("Should log for SEND_KEYS action")
        void shouldLogForSendKeysAction() {
            // Given
            Object[] args = new Object[]{"test input"};

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // Set up mocks
                locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                        .thenReturn("Element Details");

                // When
                LoggingFunctions.logNoSuchElementException(element, WebElementAction.SEND_KEYS, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Unable to send keys to element")));
            }
        }

        @Test
        @DisplayName("Should log for SUBMIT action")
        void shouldLogForSubmitAction() {
            // Given
            Object[] args = new Object[]{};

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // Set up mocks
                locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                        .thenReturn("Element Details");

                // When
                LoggingFunctions.logNoSuchElementException(element, WebElementAction.SUBMIT, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Unable to submit form")));
            }
        }

        @Test
        @DisplayName("Should log for other actions")
        void shouldLogForOtherActions() {
            // Given
            Object[] args = new Object[]{};

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // Set up mocks
                locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                        .thenReturn("Element Details");

                // When
                LoggingFunctions.logNoSuchElementException(element, WebElementAction.CLEAR, args, exception);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info "),
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
            // Given
            Object[] args = new Object[]{};
            Object nonElementTarget = "Not an element";
            InvocationTargetException e = new InvocationTargetException(elementNotInteractableException);

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {
                // When
                LoggingFunctions.logElementNotInteractableException(nonElementTarget, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(
                        contains("Exception:"),
                        eq("ElementNotInteractableException"),
                        eq("String"),
                        eq("Not an element"),
                        eq("click"),
                        eq("")));

                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Element is null")));
            }
        }

        @Test
        @DisplayName("Should log for WebElement target")
        void shouldLogForWebElementTarget() {
            // Given
            Object[] args = new Object[]{};
            InvocationTargetException e = new InvocationTargetException(elementNotInteractableException);

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // Set up mocks
                locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                        .thenReturn("Element Details");

                // When
                LoggingFunctions.logElementNotInteractableException(element, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("is not interactable")));
            }
        }
    }

    @Nested
    @DisplayName("logClickInvalidSelectorException tests")
    class LogClickInvalidSelectorExceptionTests {

        @Test
        @DisplayName("Should log with extracted locator")
        void shouldLogWithExtractedLocator() {
            // Given
            By by = By.id("testId");
            Object[] args = new Object[]{by};
            InvocationTargetException e = new InvocationTargetException(invalidSelectorException);

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logClickInvalidSelectorException(element, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Invalid selector while clicking element")));
            }
        }

        @Test
        @DisplayName("Should log with null locator")
        void shouldLogWithNullLocator() {
            // Given
            Object[] args = new Object[]{};
            InvocationTargetException e = new InvocationTargetException(invalidSelectorException);

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logClickInvalidSelectorException(element, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Invalid selector")));
            }
        }
    }

    @Nested
    @DisplayName("logElementClickInterceptedException tests")
    class LogElementClickInterceptedExceptionTests {

        @Test
        @DisplayName("Should log when target is not a WebElement")
        void shouldLogWhenTargetIsNotWebElement() {
            // Given
            Object[] args = new Object[]{};
            Object nonElementTarget = "Not an element";
            InvocationTargetException e = new InvocationTargetException(elementClickInterceptedException);

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {
                // When
                LoggingFunctions.logElementClickInterceptedException(nonElementTarget, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Element is null")));
            }
        }

        @Test
        @DisplayName("Should log for WebElement target with blocking locator")
        void shouldLogForWebElementTarget() {
            // Given
            Object[] args = new Object[]{};
            InvocationTargetException e = new InvocationTargetException(elementClickInterceptedException);

            try (MockedStatic<LocatorParser> locatorParserMock = mockStatic(LocatorParser.class);
                 MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // Set up mocks
                locatorParserMock.when(() -> LocatorParser.getElementDetails(eq(element), anyString(), anyString()))
                        .thenReturn("Element Details");
                locatorParserMock.when(() -> LocatorParser.extractLocatorFromMessage(anyString()))
                        .thenReturn("By.xpath: //div[@id='overlay']");

                // When
                LoggingFunctions.logElementClickInterceptedException(element, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Failed to click")));
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
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
            // Given
            By by = By.id("testId");
            Object[] args = new Object[]{by};
            InvocationTargetException e = new InvocationTargetException(timeoutException);

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logClickTimeoutException(element, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Timeout while waiting for element to meet condition")));
            }
        }

        @Test
        @DisplayName("Should log with extracted locator and specified timeout")
        void shouldLogWithExtractedLocatorAndSpecifiedTimeout() {
            // Given
            Long timeout = 5000L;
            By by = By.id("testId");
            Object[] args = new Object[]{by, timeout};
            InvocationTargetException e = new InvocationTargetException(timeoutException);

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logClickTimeoutException(element, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Waited for 5000 milliseconds.")));
            }
        }

        @Test
        @DisplayName("Should log with null locator")
        void shouldLogWithNullLocator() {
            // Given
            Object[] args = new Object[]{};
            InvocationTargetException e = new InvocationTargetException(timeoutException);

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logClickTimeoutException(element, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(contains("Additional Info"),
                        contains("Unknown locator")));
            }
        }

        @Test
        @DisplayName("Should handle non-Long second argument")
        void shouldHandleNonLongSecondArgument() {
            // Given
            By by = By.id("testId");
            Object[] args = new Object[]{by, "not a long"};
            InvocationTargetException e = new InvocationTargetException(timeoutException);

            try (MockedStatic<LogUI> logUIMock = mockStatic(LogUI.class)) {

                // When
                LoggingFunctions.logClickTimeoutException(element, WebElementAction.CLICK, args, e);

                // Then
                logUIMock.verify(() -> LogUI.extended(eq("Additional Info for the problem: {}"),
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
            // Given
            Method printMethodArgsClassesMethod = LoggingFunctions.class.getDeclaredMethod(
                    "printMethodArgsClasses", Object[].class);
            printMethodArgsClassesMethod.setAccessible(true);

            // When & Then
            assertEquals("", printMethodArgsClassesMethod.invoke(null, new Object[]{null}),
                    "Null args should return empty string");
        }

        @Test
        @DisplayName("truncateString should handle null input")
        void truncateStringShouldHandleNullInput() throws Exception {
            // Given
            Method truncateStringMethod = LoggingFunctions.class.getDeclaredMethod(
                    "truncateString", String.class, int.class);
            truncateStringMethod.setAccessible(true);

            // When
            String result = (String) truncateStringMethod.invoke(null, null, 10);

            // Then
            assertEquals("Page source unavailable.", result,
                    "Null input should return 'Page source unavailable.'");
        }

        @Test
        @DisplayName("truncateString should truncate long strings")
        void truncateStringShouldTruncateLongStrings() throws Exception {
            // Given
            Method truncateStringMethod = LoggingFunctions.class.getDeclaredMethod(
                    "truncateString", String.class, int.class);
            truncateStringMethod.setAccessible(true);

            String longString = "This is a very long string that should be truncated";

            // When
            String result = (String) truncateStringMethod.invoke(null, longString, 10);

            // Then
            assertEquals("This is a ...", result,
                    "Long string should be truncated to specified length with ellipsis");
        }
    }
}