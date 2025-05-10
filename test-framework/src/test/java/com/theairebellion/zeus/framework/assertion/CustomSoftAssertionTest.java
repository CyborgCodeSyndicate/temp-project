package com.theairebellion.zeus.framework.assertion;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
@DisplayName("CustomSoftAssertion Tests")
class CustomSoftAssertionTest {

   private CustomSoftAssertion softAssertions;

   @Mock
   private BiConsumer<AssertionError, String> stringHandler;

   @Mock
   private BiConsumer<AssertionError, Integer> integerHandler;

   @BeforeEach
   void setUp() {
      softAssertions = new CustomSoftAssertion();
      // Clear static handlers from previous tests
      clearStaticHandlers();
   }

   /**
    * Clear static handlers between tests to ensure test isolation
    */
   private void clearStaticHandlers() {
      // Use reflection to access and clear the private static maps
      try {
         var handlersField = CustomSoftAssertion.class.getDeclaredField("postErrorHandlers");
         handlersField.setAccessible(true);
         ((java.util.Map<?, ?>) handlersField.get(null)).clear();

         var checkersField = CustomSoftAssertion.class.getDeclaredField("errorCheckers");
         checkersField.setAccessible(true);
         ((java.util.Map<?, ?>) checkersField.get(null)).clear();
      } catch (Exception e) {
         fail("Failed to clear static handlers: " + e.getMessage());
      }
   }

   @Test
   @DisplayName("Should register and retrieve object by type")
   void testRegisterAndRetrieveObject() {
      // Given
      String testObject = "TestObject";

      // When
      softAssertions.registerObjectForPostErrorHandling(String.class, testObject);
      Optional<String> retrieved = softAssertions.getObjectFromType(String.class);

      // Then
      assertTrue(retrieved.isPresent(), "Object should be present after registration");
      assertEquals(testObject, retrieved.get(), "Retrieved object should match registered object");
   }

   @Test
   @DisplayName("Should return empty Optional when retrieving unregistered type")
   void testGetObjectFromType_WithUnregisteredType() {
      // When
      Optional<String> retrieved = softAssertions.getObjectFromType(String.class);

      // Then
      assertTrue(retrieved.isEmpty(), "Should return empty Optional for unregistered type");
   }

   @Test
   @DisplayName("Should execute handler when assertion error is collected")
   void testCollectAssertionError_ExecutesHandler() {
      // Given
      AssertionError testError = new AssertionError("Test error");
      String testObject = "Test";

      // Register handler that always processes errors
      CustomSoftAssertion.registerCustomAssertion(
            String.class,
            stringHandler,
            stackTrace -> true
      );
      softAssertions.registerObjectForPostErrorHandling(String.class, testObject);

      // When
      softAssertions.collectAssertionError(testError);

      // Then
      verify(stringHandler).accept(testError, testObject);
   }

   @Test
   @DisplayName("Should not execute handler when no matching object is registered")
   void testCollectAssertionError_DoesNotExecuteHandlerIfNoMatchingObject() {
      // Given
      AssertionError testError = new AssertionError("Test error");

      // Register handler but don't register an object
      CustomSoftAssertion.registerCustomAssertion(
            Integer.class,
            integerHandler,
            stackTrace -> true
      );

      // When
      softAssertions.collectAssertionError(testError);

      // Then
      verifyNoInteractions(integerHandler);
   }

   @Test
   @DisplayName("Should not execute handler when error checker returns false")
   void testCollectAssertionError_DoesNotExecuteHandlerIfCheckerFails() {
      // Given
      AssertionError testError = new AssertionError("Test error");
      String testObject = "Test";

      // Register handler with checker that always fails
      CustomSoftAssertion.registerCustomAssertion(
            String.class,
            stringHandler,
            stackTrace -> false
      );
      softAssertions.registerObjectForPostErrorHandling(String.class, testObject);

      // When
      softAssertions.collectAssertionError(testError);

      // Then
      verifyNoInteractions(stringHandler);
   }

   @Test
   @DisplayName("Should handle multiple registered types")
   void testMultipleRegisteredTypes() {
      // Given
      AssertionError testError = new AssertionError("Test error");
      String stringObject = "Test";
      Integer integerObject = 42;

      // Register both handlers
      CustomSoftAssertion.registerCustomAssertion(
            String.class,
            stringHandler,
            stackTrace -> true
      );
      CustomSoftAssertion.registerCustomAssertion(
            Integer.class,
            integerHandler,
            stackTrace -> true
      );

      // Register both objects
      softAssertions.registerObjectForPostErrorHandling(String.class, stringObject);
      softAssertions.registerObjectForPostErrorHandling(Integer.class, integerObject);

      // When
      softAssertions.collectAssertionError(testError);

      // Then
      verify(stringHandler).accept(testError, stringObject);
      verify(integerHandler).accept(testError, integerObject);
   }

   @Test
   @DisplayName("Should override previously registered object of same type")
   void testOverrideRegisteredObject() {
      // Given
      String firstObject = "First";
      String secondObject = "Second";

      // When
      softAssertions.registerObjectForPostErrorHandling(String.class, firstObject);
      softAssertions.registerObjectForPostErrorHandling(String.class, secondObject);
      Optional<String> retrieved = softAssertions.getObjectFromType(String.class);

      // Then
      assertTrue(retrieved.isPresent(), "Object should be present");
      assertEquals(secondObject, retrieved.get(), "Should return the most recently registered object");
   }

   @Test
   @DisplayName("Should correctly evaluate custom error checker")
   void testCustomErrorChecker() {
      // Given
      AssertionError testError = new AssertionError("Test error");
      AtomicInteger handlerCallCount = new AtomicInteger(0);

      BiConsumer<AssertionError, String> countingHandler =
            (error, obj) -> handlerCallCount.incrementAndGet();

      // Create a checker that only accepts errors with specific class name in stack trace
      Predicate<StackTraceElement[]> specificClassChecker = stackTrace -> {
         for (StackTraceElement element : stackTrace) {
            if (element.getClassName().contains("CustomSoftAssertionTest")) {
               return true;
            }
         }
         return false;
      };

      CustomSoftAssertion.registerCustomAssertion(
            String.class,
            countingHandler,
            specificClassChecker
      );
      softAssertions.registerObjectForPostErrorHandling(String.class, "Test");

      // When
      softAssertions.collectAssertionError(testError);

      // Then
      assertEquals(1, handlerCallCount.get(),
            "Handler should be called once for error with matching stack trace");
   }

   @Test
   @DisplayName("Should call super.collectAssertionError to accumulate assertions")
   void testSuperCollectAssertionErrorCalled() {
      // Given
      CustomSoftAssertion spyAssertions = spy(softAssertions);
      AssertionError testError = new AssertionError("Test error");

      // When
      spyAssertions.collectAssertionError(testError);

      // Then - Verify super.collectAssertionError was called
      // This requires verifying that errors field is updated
      assertFalse(spyAssertions.errorsCollected().isEmpty(),
            "Error should be collected by parent class");
   }
}