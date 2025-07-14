package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.testutil.BaseUnitUITest;
import com.theairebellion.zeus.ui.log.LogUi;
import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.helper.FrameHelper;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ExceptionHandlingWebDriverFunctionsTest extends BaseUnitUITest {

   @Mock
   private WebDriver mockDriver;


   @Nested
   @DisplayName("Tests for handleNoSuchElement with invalid arguments")
   class InvalidArgumentTests {
      @Test
      @DisplayName("Should throw IllegalArgumentException when args are empty")
      void testHandleNoSuchElementWithEmptyArgs() {
         try (MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)) {
            // Given
            Object[] emptyArgs = new Object[0];

            // When
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                  () -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(mockDriver, WebElementAction.FIND_ELEMENT, emptyArgs));

            // Then
            mockedLogUi.verify(() -> LogUi.error(anyString()));
            assertNotNull(exception.getMessage());
            assertFalse(exception.getMessage().isEmpty());
         }
      }

      @Test
      @DisplayName("Should throw IllegalArgumentException when first arg is null")
      void testHandleNoSuchElementWithNullArgs() {
         try (MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)) {
            // Given
            Object[] nullArgs = new Object[] {null};

            //When
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                  () -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(mockDriver, WebElementAction.FIND_ELEMENT, nullArgs));

            // Then
            mockedLogUi.verify(() -> LogUi.error(anyString()));
            assertNotNull(exception.getMessage());
            assertFalse(exception.getMessage().isEmpty());
         }
      }

      @Test
      @DisplayName("Should throw IllegalArgumentException when first arg is not a By")
      void testHandleNoSuchElementWithNonByArg() {
         try (MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)) {
            // Given
            Object[] invalidArgs = new Object[] {"not a By object"};

            //When
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                  () -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(mockDriver, WebElementAction.FIND_ELEMENT, invalidArgs));

            // Then
            mockedLogUi.verify(() -> LogUi.error(anyString()));
            assertNotNull(exception.getMessage());
            assertFalse(exception.getMessage().isEmpty());
         }
      }
   }

   @Nested
   @DisplayName("Tests for handleNoSuchElement with valid arguments")
   class ValidArgumentTests {
      @Test
      @DisplayName("Should return element when found in iframes")
      void testHandleNoSuchElementWhenElementFound() {
         try (MockedStatic<FrameHelper> mockedHelper = mockStatic(FrameHelper.class)) {
            // Given
            By byLocator = By.id("testId");
            Object[] args = new Object[] {byLocator};
            SmartWebElement smartElement = mock(SmartWebElement.class);

            // When
            mockedHelper.when(() -> FrameHelper.findElementInIframes(any(WebDriver.class), any(By.class)))
                  .thenReturn(smartElement);
            Object result =
                  ExceptionHandlingWebDriverFunctions.handleNoSuchElement(mockDriver, WebElementAction.FIND_ELEMENT, args);

            // Then
            mockedHelper.verify(() -> FrameHelper.findElementInIframes(any(WebDriver.class), any(By.class)));
            assertNotNull(result);
         }
      }

      @Test
      @DisplayName("Should throw NoSuchElementException when element not found")
      void testHandleNoSuchElementWhenElementNotFound() {
         try (MockedStatic<FrameHelper> mockedHelper = mockStatic(FrameHelper.class);
              MockedStatic<LogUi> mockedLogUi = mockStatic(LogUi.class)) {
            // Given
            By byLocator = By.id("testId");
            Object[] args = new Object[] {byLocator};

            // When
            mockedHelper.when(() -> FrameHelper.findElementInIframes(any(WebDriver.class), any(By.class)))
                  .thenReturn(null);
            assertThrows(NoSuchElementException.class, () -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(mockDriver, WebElementAction.FIND_ELEMENT, args));

            // Then
            mockedHelper.verify(() -> FrameHelper.findElementInIframes(any(WebDriver.class), any(By.class)));
            mockedLogUi.verify(() -> LogUi.error(anyString()));
         }
      }

      @Test
      @DisplayName("Should handle FIND_ELEMENTS action by locating container iframe first")
      void testHandleNoSuchElementWithFindContainerReturnsElement() {
         try (MockedStatic<FrameHelper> mockedHelper = mockStatic(FrameHelper.class)) {
            // Given
            By locator = By.id("testId");
            Object[] args = new Object[] {locator};
            mockedHelper.when(() -> FrameHelper.findContainerIframe(any(), any()))
                  .thenReturn(mock(WebElement.class));
            when(mockDriver.findElements(locator)).thenReturn(List.of(mock(WebElement.class)));

            // When
            Object result = ExceptionHandlingWebDriverFunctions.handleNoSuchElement(
                  mockDriver, WebElementAction.FIND_ELEMENTS, args);

            // Then
            mockedHelper.verify(() -> FrameHelper.findContainerIframe(eq(mockDriver), eq(locator)));
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

            // When
            mockedHelper.when(() -> FrameHelper.findContainerIframe(any(), any()))
                  .thenReturn(null); // Just return a mock, no need for real frame

            // Then
            assertThrows(NoSuchElementException.class, () -> ExceptionHandlingWebDriverFunctions.handleNoSuchElement(mockDriver, WebElementAction.FIND_ELEMENTS, args));
            mockedHelper.verify(() -> FrameHelper.findContainerIframe(any(WebDriver.class), any(By.class)));
            mockedLogUi.verify(() -> LogUi.error(anyString()));
         }
      }
   }
}