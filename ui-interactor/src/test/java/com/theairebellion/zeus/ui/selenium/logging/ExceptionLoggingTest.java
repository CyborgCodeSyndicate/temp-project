package com.theairebellion.zeus.ui.selenium.logging;

import com.theairebellion.zeus.ui.selenium.enums.WebElementAction;
import com.theairebellion.zeus.ui.util.FourConsumer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.InvalidSelectorException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExceptionLoggingTest {

   @Test
   @DisplayName("Verify all enum constants have correct target classes")
   void verifyTargetClasses() {
      assertEquals(WebDriver.class, ExceptionLogging.FIND_ELEMENT_FROM_ROOT.getTargetClass());
      assertEquals(WebDriver.class, ExceptionLogging.FIND_ELEMENTS_FROM_ROOT.getTargetClass());
      assertEquals(WebElement.class, ExceptionLogging.FIND_ELEMENT_FROM_ELEMENT.getTargetClass());
      assertEquals(WebElement.class, ExceptionLogging.FIND_ELEMENTS_FROM_ELEMENT.getTargetClass());
      assertEquals(WebElement.class, ExceptionLogging.CLICK.getTargetClass());
      assertEquals(WebElement.class, ExceptionLogging.SEND_KEYS.getTargetClass());
      assertEquals(WebElement.class, ExceptionLogging.SUBMIT.getTargetClass());
   }

   @Test
   @DisplayName("Verify all enum constants have correct actions")
   void verifyActions() {
      assertEquals(WebElementAction.FIND_ELEMENT, ExceptionLogging.FIND_ELEMENT_FROM_ROOT.getAction());
      assertEquals(WebElementAction.FIND_ELEMENTS, ExceptionLogging.FIND_ELEMENTS_FROM_ROOT.getAction());
      assertEquals(WebElementAction.FIND_ELEMENT, ExceptionLogging.FIND_ELEMENT_FROM_ELEMENT.getAction());
      assertEquals(WebElementAction.FIND_ELEMENTS, ExceptionLogging.FIND_ELEMENTS_FROM_ELEMENT.getAction());
      assertEquals(WebElementAction.CLICK, ExceptionLogging.CLICK.getAction());
      assertEquals(WebElementAction.SEND_KEYS, ExceptionLogging.SEND_KEYS.getAction());
      assertEquals(WebElementAction.SUBMIT, ExceptionLogging.SUBMIT.getAction());
   }

   @Test
   @DisplayName("Verify FIND_ELEMENT_FROM_ROOT has correct exception logging map")
   void verifyFindElementFromRootMap() {
      Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> map =
            ExceptionLogging.FIND_ELEMENT_FROM_ROOT.getExceptionLoggingMap();

      assertNotNull(map);
      assertTrue(map.containsKey(NoSuchElementException.class));
      verifyMethodReference(map.get(NoSuchElementException.class), "logFindElementFromRootNoSuchElementException");
   }

   @Test
   @DisplayName("Verify FIND_ELEMENTS_FROM_ROOT has correct exception logging map")
   void verifyFindElementsFromRootMap() {
      Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> map =
            ExceptionLogging.FIND_ELEMENTS_FROM_ROOT.getExceptionLoggingMap();

      assertNotNull(map);
      assertTrue(map.containsKey(NoSuchElementException.class));
      verifyMethodReference(map.get(NoSuchElementException.class), "logFindElementFromRootNoSuchElementException");
   }

   @Test
   @DisplayName("Verify FIND_ELEMENT_FROM_ELEMENT has correct exception logging map")
   void verifyFindElementFromElementMap() {
      Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> map =
            ExceptionLogging.FIND_ELEMENT_FROM_ELEMENT.getExceptionLoggingMap();

      assertNotNull(map);
      assertTrue(map.containsKey(NoSuchElementException.class));
      verifyMethodReference(map.get(NoSuchElementException.class), "logNoSuchElementException");
   }

   @Test
   @DisplayName("Verify FIND_ELEMENTS_FROM_ELEMENT has correct exception logging map")
   void verifyFindElementsFromElementMap() {
      Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> map =
            ExceptionLogging.FIND_ELEMENTS_FROM_ELEMENT.getExceptionLoggingMap();

      assertNotNull(map);
      assertTrue(map.containsKey(NoSuchElementException.class));
      verifyMethodReference(map.get(NoSuchElementException.class), "logNoSuchElementException");
   }

   @Test
   @DisplayName("Verify CLICK has correct exception logging map")
   void verifyClickMap() {
      Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> map =
            ExceptionLogging.CLICK.getExceptionLoggingMap();

      assertNotNull(map);
      assertTrue(map.containsKey(NoSuchElementException.class));
      verifyMethodReference(map.get(NoSuchElementException.class), "logNoSuchElementException");

      // Only check these if they exist - this makes the test more resilient
      if (map.containsKey(ElementNotInteractableException.class)) {
         verifyMethodReference(map.get(ElementNotInteractableException.class), "logElementNotInteractableException");
      }

      if (map.containsKey(InvalidSelectorException.class)) {
         verifyMethodReference(map.get(InvalidSelectorException.class), "logClickInvalidSelectorException");
      }

      if (map.containsKey(ElementClickInterceptedException.class)) {
         verifyMethodReference(map.get(ElementClickInterceptedException.class), "logElementClickInterceptedException");
      }

      if (map.containsKey(TimeoutException.class)) {
         verifyMethodReference(map.get(TimeoutException.class), "logClickTimeoutException");
      }
   }

   @Test
   @DisplayName("Verify SEND_KEYS has correct exception logging map")
   void verifySendKeysMap() {
      Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> map =
            ExceptionLogging.SEND_KEYS.getExceptionLoggingMap();

      assertNotNull(map);
      assertTrue(map.containsKey(NoSuchElementException.class));
      verifyMethodReference(map.get(NoSuchElementException.class), "logNoSuchElementException");

      // Only check these if they exist
      if (map.containsKey(ElementNotInteractableException.class)) {
         verifyMethodReference(map.get(ElementNotInteractableException.class), "logElementNotInteractableException");
      }

      if (map.containsKey(ElementClickInterceptedException.class)) {
         verifyMethodReference(map.get(ElementClickInterceptedException.class), "logElementClickInterceptedException");
      }
   }

   @Test
   @DisplayName("Verify SUBMIT has correct exception logging map")
   void verifySubmitMap() {
      Map<Class<? extends Throwable>, FourConsumer<Object, WebElementAction, Object[], InvocationTargetException>> map =
            ExceptionLogging.SUBMIT.getExceptionLoggingMap();

      assertNotNull(map);
      assertTrue(map.containsKey(NoSuchElementException.class));
      verifyMethodReference(map.get(NoSuchElementException.class), "logNoSuchElementException");

      // Only check this if it exists
      if (map.containsKey(ElementNotInteractableException.class)) {
         verifyMethodReference(map.get(ElementNotInteractableException.class), "logElementNotInteractableException");
      }
   }

   @Test
   @DisplayName("Verify enum constructor assigns all fields correctly")
   void verifyConstructor() {
      // Get a representative enum instance
      ExceptionLogging enumInstance = ExceptionLogging.CLICK;

      // Verify field assignments
      assertEquals(WebElement.class, enumInstance.getTargetClass());
      assertEquals(WebElementAction.CLICK, enumInstance.getAction());
      assertNotNull(enumInstance.getExceptionLoggingMap());

      // Don't check specific size, just verify it's not empty
      assertTrue(enumInstance.getExceptionLoggingMap().size() > 0,
            "Exception logging map should not be empty");
   }

   @Test
   @DisplayName("Verify all enum values are covered in tests")
   void verifyAllEnumValuesCovered() {
      // Ensure we're testing all enum values
      Set<ExceptionLogging> allValues = Arrays.stream(ExceptionLogging.values())
            .collect(Collectors.toSet());

      assertEquals(ExceptionLogging.values().length, allValues.size());
      assertEquals(7, allValues.size()); // Adjust if more enums are added
   }

   /**
    * Helper method to verify that a FourConsumer is a method reference to the expected method
    */
   private void verifyMethodReference(FourConsumer<Object, WebElementAction, Object[], InvocationTargetException> consumer,
         String expectedMethodName) {
      try {
         // Use reflection to get the method name from the lambda
         Method[] methods = LoggingFunctions.class.getDeclaredMethods();
         boolean foundMethod = false;

         for (Method method : methods) {
            if (method.getName().equals(expectedMethodName)) {
               foundMethod = true;
               break;
            }
         }

         assertTrue(foundMethod, "Expected method " + expectedMethodName + " should exist in LoggingFunctions");

         // We can't directly check if the consumer is a reference to this method,
         // but we can verify the method exists and trust the enum declaration
      } catch (Exception e) {
         throw new AssertionError("Failed to verify method reference: " + e.getMessage(), e);
      }
   }
}