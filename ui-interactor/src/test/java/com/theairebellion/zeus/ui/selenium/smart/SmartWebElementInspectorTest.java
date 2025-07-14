package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUiException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.support.ui.FluentWait;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SmartWebElementInspectorTest {

   @Test
   @DisplayName("inspectStackTrace should return false values for null throwable")
   void inspectStackTraceShouldReturnFalseValuesForNullThrowable() {
      // Given // When
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(null);

      // Then
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
      assertFalse(result.comingFromWait());
   }

   @Test
   @DisplayName("inspectStackTrace should detect FluentWait in stack trace")
   void inspectStackTraceShouldDetectFluentWaitInStackTrace() {
      // Given
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  FluentWait.class.getName(),
                  "until",
                  "FluentWait.java",
                  150
            )
      );

      // When
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Then
      assertTrue(result.comingFromWait());
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
   }

   @Test
   @DisplayName("inspectStackTrace should detect annotated method in SmartWebElement")
   void inspectStackTraceShouldDetectAnnotatedMethodInSmartWebElement() {
      // Given
      Method[] methods = SmartWebElement.class.getDeclaredMethods();
      String annotatedMethodName = "";

      for (Method method : methods) {
         if (method.isAnnotationPresent(HandleUiException.class)) {
            annotatedMethodName = method.getName();
            break;
         }
      }

      // Skip test if no annotated method found
      assumeTrue(!annotatedMethodName.isEmpty(), "No @HandleUiException annotated method found in SmartWebElement");

      // When
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  SmartWebElement.class.getName(),
                  annotatedMethodName,
                  "SmartWebElement.java",
                  100
            )
      );

      // Then
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
      assertTrue(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
      assertFalse(result.comingFromWait());
   }

   @Test
   @DisplayName("inspectStackTrace should detect handleException method in SmartWebElement")
   void inspectStackTraceShouldDetectHandleExceptionMethodInSmartWebElement() {
      // Given
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  SmartWebElement.class.getName(),
                  "handleException",
                  "SmartWebElement.java",
                  200
            )
      );

      // When
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Then
      assertTrue(result.foundHandleException());
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.comingFromWait());
   }

   @Test
   @DisplayName("inspectStackTrace should detect multiple conditions simultaneously")
   void inspectStackTraceShouldDetectMultipleConditionsSimultaneously() throws Exception {
      // Find an actual annotated method from SmartWebElement
      Field annotatedMethodsField = SmartWebElementInspector.class.getDeclaredField("ANNOTATED_METHODS");
      annotatedMethodsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      Set<String> annotatedMethods = (Set<String>) annotatedMethodsField.get(null);
      String annotatedMethodName = annotatedMethods.iterator().next();

      // Given
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  FluentWait.class.getName(),
                  "until",
                  "FluentWait.java",
                  150
            ),
            new StackTraceElement(
                  SmartWebElement.class.getName(),
                  annotatedMethodName,
                  "SmartWebElement.java",
                  100
            ),
            new StackTraceElement(
                  SmartWebDriver.class.getName(),
                  "handleException",
                  "SmartWebDriver.java",
                  250
            )
      );

      // When
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Then
      assertTrue(result.comingFromWait());
      assertTrue(result.foundAnnotatedMethod());
      assertTrue(result.foundHandleException());
   }

   @Test
   @DisplayName("inspectStackTrace should handle empty stack trace")
   void inspectStackTraceShouldHandleEmptyStackTrace() {
      // Given
      RuntimeException exception = new RuntimeException("Test exception");
      exception.setStackTrace(new StackTraceElement[0]);

      // When
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Then
      assertFalse(result.comingFromWait());
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
   }

   @Test
   @DisplayName("inspectStackTrace should ignore irrelevant stack trace elements")
   void inspectStackTraceShouldIgnoreIrrelevantStackTraceElements() {
      // Given
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  "java.lang.Thread",
                  "run",
                  "Thread.java",
                  833
            ),
            new StackTraceElement(
                  "com.example.OtherClass",
                  "someMethod",
                  "OtherClass.java",
                  42
            )
      );

      // When
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Then
      assertFalse(result.comingFromWait());
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
   }

   // Helper method to create exceptions with custom stack traces
   private RuntimeException createExceptionWithStackTrace(StackTraceElement... elements) {
      RuntimeException exception = new RuntimeException("Test exception");
      exception.setStackTrace(elements);
      return exception;
   }

   // Helper method for conditional tests
   private void assumeTrue(boolean condition, String message) {
      if (!condition) {
         throw new org.opentest4j.TestAbortedException(message);
      }
   }
}