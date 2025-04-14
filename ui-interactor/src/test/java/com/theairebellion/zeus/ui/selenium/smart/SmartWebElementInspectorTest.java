package com.theairebellion.zeus.ui.selenium.smart;

import com.theairebellion.zeus.ui.annotations.HandleUiException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openqa.selenium.support.ui.FluentWait;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class SmartWebElementInspectorTest {

   @Mock
   private SmartWebElement mockSmartWebElement;

   @Mock
   private SmartWebDriver mockSmartWebDriver;

   @Test
   @DisplayName("inspectStackTrace should return false values for null throwable")
   void inspectStackTraceShouldReturnFalseValuesForNullThrowable() {
      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(null);

      // Assert
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
      assertFalse(result.comingFromWait());
   }

   @Test
   @DisplayName("inspectStackTrace should detect FluentWait in stack trace")
   void inspectStackTraceShouldDetectFluentWaitInStackTrace() {
      // Arrange
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  FluentWait.class.getName(),
                  "until",
                  "FluentWait.java",
                  150
            )
      );

      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
      assertTrue(result.comingFromWait());
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
   }

   @Test
   @DisplayName("inspectStackTrace should detect annotated method in SmartWebElement")
   void inspectStackTraceShouldDetectAnnotatedMethodInSmartWebElement() throws Exception {
      // Find an actual annotated method from SmartWebElement
      Method[] methods = SmartWebElement.class.getDeclaredMethods();
      String annotatedMethodName = "";

      for (Method method : methods) {
         if (method.isAnnotationPresent(HandleUiException.class)) {
            annotatedMethodName = method.getName();
            break;
         }
      }

      // Skip test if no annotated method found
      assumeTrue(!annotatedMethodName.isEmpty(), "No @HandleUIException annotated method found in SmartWebElement");

      // Arrange
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  SmartWebElement.class.getName(),
                  annotatedMethodName,
                  "SmartWebElement.java",
                  100
            )
      );

      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
      assertTrue(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
      assertFalse(result.comingFromWait());
   }

   @Test
   @DisplayName("inspectStackTrace should detect handleException method in SmartWebElement")
   void inspectStackTraceShouldDetectHandleExceptionMethodInSmartWebElement() {
      // Arrange
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  SmartWebElement.class.getName(),
                  "handleException",
                  "SmartWebElement.java",
                  200
            )
      );

      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
      assertTrue(result.foundHandleException());
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.comingFromWait());
   }

   @Test
   @DisplayName("inspectStackTrace should detect annotated method in SmartWebDriver")
   void inspectStackTraceShouldDetectAnnotatedMethodInSmartWebDriver() throws Exception {
      // We need to find a method that's in the ANNOTATED_METHODS set
      // For testing, use reflection to access the private static field
      Field annotatedMethodsField = SmartWebElementInspector.class.getDeclaredField("ANNOTATED_METHODS");
      annotatedMethodsField.setAccessible(true);
      @SuppressWarnings("unchecked")
      Set<String> annotatedMethods = (Set<String>) annotatedMethodsField.get(null);

      // Get a method from the set
      String annotatedMethodName = annotatedMethods.iterator().next();

      // Arrange
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  SmartWebDriver.class.getName(),
                  annotatedMethodName,
                  "SmartWebDriver.java",
                  150
            )
      );

      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
      assertTrue(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
      assertFalse(result.comingFromWait());
   }

   @Test
   @DisplayName("inspectStackTrace should detect handleException method in SmartWebDriver")
   void inspectStackTraceShouldDetectHandleExceptionMethodInSmartWebDriver() {
      // Arrange
      RuntimeException exception = createExceptionWithStackTrace(
            new StackTraceElement(
                  SmartWebDriver.class.getName(),
                  "handleException",
                  "SmartWebDriver.java",
                  250
            )
      );

      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
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

      // Arrange - create a stack trace with multiple matching elements
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

      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
      assertTrue(result.comingFromWait());
      assertTrue(result.foundAnnotatedMethod());
      assertTrue(result.foundHandleException());
   }

   @Test
   @DisplayName("inspectStackTrace should handle empty stack trace")
   void inspectStackTraceShouldHandleEmptyStackTrace() {
      // Arrange
      RuntimeException exception = new RuntimeException("Test exception");
      exception.setStackTrace(new StackTraceElement[0]);

      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
      assertFalse(result.comingFromWait());
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
   }

   @Test
   @DisplayName("inspectStackTrace should ignore irrelevant stack trace elements")
   void inspectStackTraceShouldIgnoreIrrelevantStackTraceElements() {
      // Arrange
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

      // Act
      SmartWebElementInspector.Result result = SmartWebElementInspector.inspectStackTrace(exception);

      // Assert
      assertFalse(result.comingFromWait());
      assertFalse(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
   }

   @Test
   @DisplayName("Result class should have proper getters")
   void resultClassShouldHaveProperGetters() {
      // Arrange
      SmartWebElementInspector.Result result = new SmartWebElementInspector.Result(true, false, true);

      // Assert
      assertTrue(result.foundAnnotatedMethod());
      assertFalse(result.foundHandleException());
      assertTrue(result.comingFromWait());
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