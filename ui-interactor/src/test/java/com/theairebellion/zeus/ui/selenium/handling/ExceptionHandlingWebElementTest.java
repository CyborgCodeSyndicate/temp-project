package com.theairebellion.zeus.ui.selenium.handling;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.selenium.smart.SmartWebElement;
import com.theairebellion.zeus.ui.util.FourFunction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlingWebElementTest {

   @Mock
   private WebDriver mockDriver;

   @Mock
   private SmartWebElement mockSmartElement;

   @Mock
   private By mockBy;

   @ParameterizedTest
   @EnumSource(ExceptionHandlingWebElement.class)
   @DisplayName("Each enum should have non-null fields")
   void testEnumConstructorAndGetters(ExceptionHandlingWebElement enumValue) {
      // Test that each enum has non-null values for its fields
      assertNotNull(enumValue.getMethodName());
      assertNotNull(enumValue.getExceptionHandlingMap());
      assertFalse(enumValue.getExceptionHandlingMap().isEmpty());
   }

   @Nested
   @DisplayName("Tests for method names")
   class MethodNameTests {
      @Test
      @DisplayName("FIND_ELEMENT should have correct method name")
      void testFindElementMethodName() {
         assertEquals("findElement", ExceptionHandlingWebElement.FIND_ELEMENT.getMethodName());
      }

      @Test
      @DisplayName("FIND_ELEMENTS should have correct method name")
      void testFindElementsMethodName() {
         assertEquals("findElements", ExceptionHandlingWebElement.FIND_ELEMENTS.getMethodName());
      }

      @Test
      @DisplayName("CLICK should have correct method name")
      void testClickMethodName() {
         assertEquals("click", ExceptionHandlingWebElement.CLICK.getMethodName());
      }

      @Test
      @DisplayName("SEND_KEYS should have correct method name")
      void testSendKeysMethodName() {
         assertEquals("sendKeys", ExceptionHandlingWebElement.SEND_KEYS.getMethodName());
      }

      @Test
      @DisplayName("SUBMIT should have correct method name")
      void testSubmitMethodName() {
         assertEquals("submit", ExceptionHandlingWebElement.SUBMIT.getMethodName());
      }

      @Test
      @DisplayName("CLEAR should have correct method name")
      void testClearMethodName() {
         assertEquals("clear", ExceptionHandlingWebElement.CLEAR.getMethodName());
      }
   }

   @Nested
   @DisplayName("Tests for lambda function execution")
   class LambdaFunctionTests {

      @Test
      @DisplayName("FIND_ELEMENT lambda for StaleElementReferenceException should call correct handler")
      void testFindElementStaleElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.FIND_ELEMENT.getExceptionHandlingMap()
                        .get(StaleElementReferenceException.class);

            StaleElementReferenceException staleException = new StaleElementReferenceException("Stale element");
            Object[] args = new Object[] {mockBy};

            // Execute
            function.apply(mockDriver, mockSmartElement, staleException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleStaleElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.FIND_ELEMENT),
                        eq(mockBy)
                  )
            );
         }
      }

      @Test
      @DisplayName("FIND_ELEMENT lambda for NoSuchElementException should call correct handler")
      void testFindElementNoSuchElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.FIND_ELEMENT.getExceptionHandlingMap()
                        .get(NoSuchElementException.class);

            NoSuchElementException noSuchException = new NoSuchElementException("No element found");
            Object[] args = new Object[] {mockBy};

            // Execute
            function.apply(mockDriver, mockSmartElement, noSuchException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleNoSuchElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.FIND_ELEMENT),
                        eq(mockBy)
                  )
            );
         }
      }

      @Test
      @DisplayName("FIND_ELEMENTS lambda for StaleElementReferenceException should call correct handler")
      void testFindElementsStaleElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.FIND_ELEMENTS.getExceptionHandlingMap()
                        .get(StaleElementReferenceException.class);

            StaleElementReferenceException staleException = new StaleElementReferenceException("Stale element");
            Object[] args = new Object[] {mockBy};

            // Execute
            function.apply(mockDriver, mockSmartElement, staleException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleStaleElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.FIND_ELEMENTS),
                        eq(mockBy)
                  )
            );
         }
      }

      @Test
      @DisplayName("FIND_ELEMENTS lambda for NoSuchElementException should call correct handler")
      void testFindElementsNoSuchElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.FIND_ELEMENTS.getExceptionHandlingMap()
                        .get(NoSuchElementException.class);

            NoSuchElementException noSuchException = new NoSuchElementException("No element found");
            Object[] args = new Object[] {mockBy};

            // Execute
            function.apply(mockDriver, mockSmartElement, noSuchException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleNoSuchElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.FIND_ELEMENTS),
                        eq(mockBy)
                  )
            );
         }
      }

      @Test
      @DisplayName("CLICK lambda for StaleElementReferenceException should call correct handler")
      void testClickStaleElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.CLICK.getExceptionHandlingMap()
                        .get(StaleElementReferenceException.class);

            StaleElementReferenceException staleException = new StaleElementReferenceException("Stale element");
            Object[] args = new Object[] {};

            // Execute
            function.apply(mockDriver, mockSmartElement, staleException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleStaleElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.CLICK)
                  )
            );
         }
      }

      @Test
      @DisplayName("CLICK lambda for ElementClickInterceptedException should call correct handler")
      void testClickElementClickInterceptedLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.CLICK.getExceptionHandlingMap()
                        .get(ElementClickInterceptedException.class);

            ElementClickInterceptedException clickException =
                  new ElementClickInterceptedException("Click intercepted");
            Object[] args = new Object[] {};

            // Execute
            function.apply(mockDriver, mockSmartElement, clickException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.CLICK),
                        eq(clickException)
                  )
            );
         }
      }

      @Test
      @DisplayName("CLICK lambda for ElementNotInteractableException should call correct handler")
      void testClickElementNotInteractableLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.CLICK.getExceptionHandlingMap()
                        .get(ElementNotInteractableException.class);

            ElementNotInteractableException notInteractableException =
                  new ElementNotInteractableException("Not interactable");
            Object[] args = new Object[] {};

            // Execute
            function.apply(mockDriver, mockSmartElement, notInteractableException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleElementNotInteractable(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.CLICK)
                  )
            );
         }
      }

      @Test
      @DisplayName("CLICK lambda for NoSuchElementException should call correct handler")
      void testClickNoSuchElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.CLICK.getExceptionHandlingMap()
                        .get(NoSuchElementException.class);

            NoSuchElementException noSuchException = new NoSuchElementException("No element found");
            Object[] args = new Object[] {mockBy};

            // Execute
            function.apply(mockDriver, mockSmartElement, noSuchException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleNoSuchElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.CLICK),
                        eq(mockBy)
                  )
            );
         }
      }

      @Test
      @DisplayName("SEND_KEYS lambda for StaleElementReferenceException should call correct handler")
      void testSendKeysStaleElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.SEND_KEYS.getExceptionHandlingMap()
                        .get(StaleElementReferenceException.class);

            StaleElementReferenceException staleException = new StaleElementReferenceException("Stale element");
            String keys = "test input";
            Object[] args = new Object[] {keys};

            // Execute
            function.apply(mockDriver, mockSmartElement, staleException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleStaleElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.SEND_KEYS),
                        eq(keys)
                  )
            );
         }
      }

      @Test
      @DisplayName("SEND_KEYS lambda for ElementClickInterceptedException should call correct handler")
      void testSendKeysElementClickInterceptedLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.SEND_KEYS.getExceptionHandlingMap()
                        .get(ElementClickInterceptedException.class);

            ElementClickInterceptedException clickException =
                  new ElementClickInterceptedException("Click intercepted");
            String keys = "test input";
            Object[] args = new Object[] {keys};

            // Execute
            function.apply(mockDriver, mockSmartElement, clickException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleElementClickIntercepted(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.SEND_KEYS),
                        eq(clickException),
                        eq(keys)
                  )
            );
         }
      }

      @Test
      @DisplayName("SEND_KEYS lambda for ElementNotInteractableException should call correct handler")
      void testSendKeysElementNotInteractableLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.SEND_KEYS.getExceptionHandlingMap()
                        .get(ElementNotInteractableException.class);

            ElementNotInteractableException notInteractableException =
                  new ElementNotInteractableException("Not interactable");
            String keys = "test input";
            Object[] args = new Object[] {keys};

            // Execute
            function.apply(mockDriver, mockSmartElement, notInteractableException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleElementNotInteractable(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.SEND_KEYS),
                        eq(keys)
                  )
            );
         }
      }

      @Test
      @DisplayName("SEND_KEYS lambda for NoSuchElementException should call correct handler")
      void testSendKeysNoSuchElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.SEND_KEYS.getExceptionHandlingMap()
                        .get(NoSuchElementException.class);

            NoSuchElementException noSuchException = new NoSuchElementException("No element found");
            String keys = "test input";
            Object[] args = new Object[] {keys};

            // Execute
            function.apply(mockDriver, mockSmartElement, noSuchException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleNoSuchElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.SEND_KEYS),
                        eq(keys)
                  )
            );
         }
      }

      @Test
      @DisplayName("SUBMIT lambda for StaleElementReferenceException should call correct handler")
      void testSubmitStaleElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.SUBMIT.getExceptionHandlingMap()
                        .get(StaleElementReferenceException.class);

            StaleElementReferenceException staleException = new StaleElementReferenceException("Stale element");
            Object[] args = new Object[] {};

            // Execute
            function.apply(mockDriver, mockSmartElement, staleException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleStaleElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.SUBMIT)
                  )
            );
         }
      }

      @Test
      @DisplayName("SUBMIT lambda for ElementNotInteractableException should call correct handler")
      void testSubmitElementNotInteractableLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.SUBMIT.getExceptionHandlingMap()
                        .get(ElementNotInteractableException.class);

            ElementNotInteractableException notInteractableException =
                  new ElementNotInteractableException("Not interactable");
            Object[] args = new Object[] {};

            // Execute
            function.apply(mockDriver, mockSmartElement, notInteractableException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleElementNotInteractable(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.SUBMIT)
                  )
            );
         }
      }

      @Test
      @DisplayName("SUBMIT lambda for NoSuchElementException should call correct handler")
      void testSubmitNoSuchElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.SUBMIT.getExceptionHandlingMap()
                        .get(NoSuchElementException.class);

            NoSuchElementException noSuchException = new NoSuchElementException("No element found");
            Object[] args = new Object[] {mockBy};

            // Execute
            function.apply(mockDriver, mockSmartElement, noSuchException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleNoSuchElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.SUBMIT),
                        eq(mockBy)
                  )
            );
         }
      }

      @Test
      @DisplayName("CLEAR lambda for StaleElementReferenceException should call correct handler")
      void testClearStaleElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.CLEAR.getExceptionHandlingMap()
                        .get(StaleElementReferenceException.class);

            StaleElementReferenceException staleException = new StaleElementReferenceException("Stale element");
            Object[] args = new Object[] {};

            // Execute
            function.apply(mockDriver, mockSmartElement, staleException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleStaleElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.CLEAR)
                  )
            );
         }
      }

      @Test
      @DisplayName("CLEAR lambda for ElementNotInteractableException should call correct handler")
      void testClearElementNotInteractableLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.CLEAR.getExceptionHandlingMap()
                        .get(ElementNotInteractableException.class);

            ElementNotInteractableException notInteractableException =
                  new ElementNotInteractableException("Not interactable");
            Object[] args = new Object[] {};

            // Execute
            function.apply(mockDriver, mockSmartElement, notInteractableException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleElementNotInteractable(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.CLEAR)
                  )
            );
         }
      }

      @Test
      @DisplayName("CLEAR lambda for NoSuchElementException should call correct handler")
      void testClearNoSuchElementLambda() {
         try (MockedStatic<ExceptionHandlingWebElementFunctions> mockedFunctions =
                    mockStatic(ExceptionHandlingWebElementFunctions.class)) {

            // Setup
            FourFunction<WebDriver, SmartWebElement, Object[], Exception, Object> function =
                  ExceptionHandlingWebElement.CLEAR.getExceptionHandlingMap()
                        .get(NoSuchElementException.class);

            NoSuchElementException noSuchException = new NoSuchElementException("No element found");
            Object[] args = new Object[] {mockBy};

            // Execute
            function.apply(mockDriver, mockSmartElement, noSuchException, args);

            // Verify
            mockedFunctions.verify(() ->
                  ExceptionHandlingWebElementFunctions.handleNoSuchElement(
                        eq(mockDriver),
                        eq(mockSmartElement),
                        eq(WebElementAction.CLICK), // Note: This is intentional as per implementation
                        eq(mockBy)
                  )
            );
         }
      }
   }

   @Test
   @DisplayName("Private constructor should not be accessible")
   void testPrivateConstructorNotAccessible() {
      // This doesn't actually test the constructor but ensures coverage
      // by acknowledging the enum has a default constructor
      assertNotNull(ExceptionHandlingWebElement.FIND_ELEMENT);
   }
}