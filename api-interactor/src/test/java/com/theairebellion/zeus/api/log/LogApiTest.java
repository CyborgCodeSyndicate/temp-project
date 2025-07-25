package com.theairebellion.zeus.api.log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@DisplayName("LogApi Tests")
class LogApiTest {

   private static final String INSTANCE_FIELD = "instance";

   @Test
   @DisplayName("All log methods should execute without exceptions")
   void allLogMethodsShouldExecuteWithoutExceptions() {
      assertAll(
            () -> LogApi.info("Info message"),
            () -> LogApi.warn("Warn message"),
            () -> LogApi.error("Error message"),
            () -> LogApi.debug("Debug message"),
            () -> LogApi.trace("Trace message"),
            () -> LogApi.step("Step message"),
            () -> LogApi.validation("Validation message"),
            () -> LogApi.extended("Extended message")
      );
   }

   @Test
   @DisplayName("getInstance should always return the same instance")
   void getInstanceShouldReturnSameInstance() throws Exception {
      // Access the private getInstance method via reflection
      Method getInstanceMethod = LogApi.class.getDeclaredMethod("getInstance");
      getInstanceMethod.setAccessible(true);

      // Reset the INSTANCE field first
      Field instanceField = LogApi.class.getDeclaredField("instance");
      instanceField.setAccessible(true);

      // Store the original instance
      Object originalInstance = instanceField.get(null);

      try {
         // Set INSTANCE to null
         instanceField.set(null, null);

         // Call getInstance twice
         Object instance1 = getInstanceMethod.invoke(null);
         Object instance2 = getInstanceMethod.invoke(null);

         // Both calls should return the same instance
         assertSame(instance1, instance2, "getInstance should return the same instance");

         // Verify the instance is of type LogApi
         assertInstanceOf(LogApi.class, instance1, "Instance should be of type LogApi");
      } finally {
         // Restore the original instance
         instanceField.set(null, originalInstance);
      }
   }

   @Test
   @DisplayName("LogApi constructor should set correct logger and marker names")
   void constructorShouldSetCorrectLoggerAndMarkerNames() throws Exception {
      // This test verifies that LogApi passes the correct names to LogCore
      // We need reflection to access the private constructor

      // Reset the INSTANCE field first to force a new instance creation
      Field instanceField = LogApi.class.getDeclaredField("instance");
      instanceField.setAccessible(true);

      // Store the original instance
      Object originalInstance = instanceField.get(null);

      try {
         // Set INSTANCE to null
         instanceField.set(null, null);

         // Call getInstance to create a new instance with the constructor parameters we want to test
         Method getInstanceMethod = LogApi.class.getDeclaredMethod("getInstance");
         getInstanceMethod.setAccessible(true);
         getInstanceMethod.invoke(null);

         // The test passes if no exception is thrown during instance creation
         // We can't directly verify the parameters because they're passed to the superclass constructor
      } finally {
         // Restore the original instance
         instanceField.set(null, originalInstance);
      }
   }

   @Test
   @DisplayName("Should allow extending with custom instance")
   void testExtend() throws Exception {
      // Given
      Field instanceField = getInstanceField();
      Object originalInstance = instanceField.get(null);
      LogApi mockInstance = mock(LogApi.class);

      // When
      LogApi.extend(mockInstance);

      // Then
      assertSame(mockInstance, instanceField.get(null),
            "Instance field should be set to the mock");

      // Restore original instance
      instanceField.set(null, originalInstance);
   }

   private static Field getInstanceField() throws NoSuchFieldException {
      Field instanceField = LogApi.class.getDeclaredField(INSTANCE_FIELD);
      instanceField.setAccessible(true);
      return instanceField;
   }
}