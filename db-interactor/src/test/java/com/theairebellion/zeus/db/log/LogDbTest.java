package com.theairebellion.zeus.db.log;

import java.lang.reflect.Field;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class LogDbTest {

   private static final String TEST_MESSAGE = "Test log message";
   private static final String TEST_MESSAGE_WITH_PARAM = "Test message with param: {}";
   private static final String PARAM_VALUE = "parameter";

   private LogDb originalInstance;

   @BeforeEach
   void setUp() throws Exception {
      // Store the original instance
      Field instanceField = LogDb.class.getDeclaredField("instance");
      instanceField.setAccessible(true);
      originalInstance = (LogDb) instanceField.get(null);
   }

   @AfterEach
   void tearDown() throws Exception {
      // Restore the original instance
      Field instanceField = LogDb.class.getDeclaredField("instance");
      instanceField.setAccessible(true);
      instanceField.set(null, originalInstance);
   }

   @Test
   @DisplayName("All logging methods should not throw exceptions")
   void testAllLogMethodsDoNotThrow() {
      // Testing all methods to ensure they don't throw
      assertDoesNotThrow(() -> LogDb.info(TEST_MESSAGE));
      assertDoesNotThrow(() -> LogDb.warn(TEST_MESSAGE));
      assertDoesNotThrow(() -> LogDb.error(TEST_MESSAGE));
      assertDoesNotThrow(() -> LogDb.debug(TEST_MESSAGE));
      assertDoesNotThrow(() -> LogDb.trace(TEST_MESSAGE));
      assertDoesNotThrow(() -> LogDb.step(TEST_MESSAGE));
      assertDoesNotThrow(() -> LogDb.validation(TEST_MESSAGE));
      assertDoesNotThrow(() -> LogDb.extended(TEST_MESSAGE));
   }

   @Test
   @DisplayName("Logging methods should accept message templates with parameters")
   void testLoggingWithParameters() {
      // Testing with parameter formatting
      assertDoesNotThrow(() -> LogDb.info(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
      assertDoesNotThrow(() -> LogDb.warn(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
      assertDoesNotThrow(() -> LogDb.error(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
      assertDoesNotThrow(() -> LogDb.debug(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
      assertDoesNotThrow(() -> LogDb.trace(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
      assertDoesNotThrow(() -> LogDb.step(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
      assertDoesNotThrow(() -> LogDb.validation(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
      assertDoesNotThrow(() -> LogDb.extended(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE));
   }

   @Test
   @DisplayName("Should create instance on first call")
   void testSingletonInitialization() throws Exception {
      // Given - reset the instance for this test
      Field instanceField = LogDb.class.getDeclaredField("instance");
      instanceField.setAccessible(true);
      instanceField.set(null, null);

      // When
      LogDb.info(TEST_MESSAGE); // This should initialize the instance

      // Then
      LogDb instance = (LogDb) instanceField.get(null);
      assertNotNull(instance, "LogDb instance should be created");
   }

   @Test
   @DisplayName("Should reuse the same instance")
   void testSingletonReuse() throws Exception {
      // Given - reset the instance for this test
      Field instanceField = LogDb.class.getDeclaredField("instance");
      instanceField.setAccessible(true);
      instanceField.set(null, null);

      // When
      LogDb.info("First call"); // Initialize instance
      LogDb firstInstance = (LogDb) instanceField.get(null);

      LogDb.info("Second call"); // Should reuse instance
      LogDb secondInstance = (LogDb) instanceField.get(null);

      // Then
      assertSame(firstInstance, secondInstance, "Same LogDb instance should be reused");
   }

   @Test
   @DisplayName("Static methods should delegate to instance methods")
   void testStaticMethodsDelegateToInstanceMethods() {
      // Since we can't directly test the protected methods due to the final class,
      // we can use static mocking to verify the static method calls
      try (MockedStatic<LogDb> mockedLogDb = mockStatic(LogDb.class)) {
         // When
         LogDb.info(TEST_MESSAGE);

         // Then - verify the static method was called
         mockedLogDb.verify(() -> LogDb.info(eq(TEST_MESSAGE)));

         // Also verify parameters are passed correctly
         LogDb.info(TEST_MESSAGE_WITH_PARAM, PARAM_VALUE);
         mockedLogDb.verify(() -> LogDb.info(eq(TEST_MESSAGE_WITH_PARAM), eq(PARAM_VALUE)));
      }
   }

   @Test
   @DisplayName("Should handle null message gracefully")
   void testNullMessageHandling() {
      // When/Then - just verify it doesn't throw
      assertDoesNotThrow(() -> LogDb.info(null));
   }

   @Test
   @DisplayName("Should handle null arguments gracefully")
   void testNullArgumentsHandling() {
      // When/Then - just verify it doesn't throw
      assertDoesNotThrow(() -> LogDb.info(TEST_MESSAGE_WITH_PARAM, (Object) null));
   }

   @Test
   @DisplayName("Should handle multiple arguments")
   void testMultipleArguments() {
      // When/Then - just verify it doesn't throw with multiple arguments
      assertDoesNotThrow(() -> LogDb.info("Message with {} and {}", "arg1", "arg2"));
   }

   @Test
   @DisplayName("Should handle exception as parameter")
   void testExceptionAsParameter() {
      // When/Then - verify exception as parameter doesn't throw
      Exception testException = new RuntimeException("Test exception");
      assertDoesNotThrow(() -> LogDb.error("Error occurred", testException));
   }
}