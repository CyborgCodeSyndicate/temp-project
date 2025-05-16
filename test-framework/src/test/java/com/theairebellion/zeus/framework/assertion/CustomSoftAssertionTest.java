package com.theairebellion.zeus.framework.assertion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        clearStaticHandlers();
    }

    /**
     * Clear static handlers between tests to ensure test isolation
     */
    private void clearStaticHandlers() {
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
    void testRegisterAndRetrieveObject_ShouldReturnCorrectlyRegisteredInstance() {
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

        // When
        CustomSoftAssertion.registerCustomAssertion(
                String.class,
                stringHandler,
                stackTrace -> true
        );
        softAssertions.registerObjectForPostErrorHandling(String.class, testObject);
        softAssertions.collectAssertionError(testError);

        // Then
        verify(stringHandler).accept(testError, testObject);
    }

    @Test
    @DisplayName("Should not execute handler when no matching object is registered")
    void testCollectAssertionError_DoesNotExecuteHandlerIfNoMatchingObject() {
        // Given
        AssertionError testError = new AssertionError("Test error");

        // When
        CustomSoftAssertion.registerCustomAssertion(
                Integer.class,
                integerHandler,
                stackTrace -> true
        );
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

        // When
        CustomSoftAssertion.registerCustomAssertion(
                String.class,
                stringHandler,
                stackTrace -> false
        );
        softAssertions.registerObjectForPostErrorHandling(String.class, testObject);
        softAssertions.collectAssertionError(testError);

        // Then
        verifyNoInteractions(stringHandler);
    }

    @Test
    @DisplayName("Should handle multiple registered types")
    void testCollectAssertionError_WithMultipleRegisteredTypes() {
        // Given
        AssertionError testError = new AssertionError("Test error");
        String stringObject = "Test";
        Integer integerObject = 42;

        // When
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
        softAssertions.registerObjectForPostErrorHandling(String.class, stringObject);
        softAssertions.registerObjectForPostErrorHandling(Integer.class, integerObject);
        softAssertions.collectAssertionError(testError);

        // Then
        verify(stringHandler).accept(testError, stringObject);
        verify(integerHandler).accept(testError, integerObject);
    }

    @Test
    @DisplayName("Should override previously registered object of same type")
    void testRegisterObject_ShouldOverridePreviousRegistrationForSameType() {
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
    void testCustomErrorChecker_WithMatchingStackTrace() {
        // Given
        AssertionError testError = new AssertionError("Test error");
        AtomicInteger handlerCallCount = new AtomicInteger(0);
        BiConsumer<AssertionError, String> countingHandler =
                (error, obj) -> handlerCallCount.incrementAndGet();

        // When
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
        softAssertions.collectAssertionError(testError);

        // Then
        assertEquals(1, handlerCallCount.get(),
                "Handler should be called once for error with matching stack trace");
    }

    @Test
    @DisplayName("Should call super.collectAssertionError to accumulate assertions")
    void testCollectAssertionError_shouldStoreErrorInParentCollector() {
        // Given
        AssertionError testError = new AssertionError("Test error");

        // When
        softAssertions.collectAssertionError(testError);

        // Then
        assertFalse(softAssertions.errorsCollected().isEmpty(),
                "Error should be collected by parent class");
    }

    @Test
    @DisplayName("Should throw exception when null object is registered")
    void testRegisterObject_withNullObject() {
        // When
        // Then
        assertThrows(NullPointerException.class, () -> softAssertions.registerObjectForPostErrorHandling(String.class, null));
    }

    @Test
    @DisplayName("Should call different handlers based on stack trace conditions")
    void testMultipleErrorHandlersWithDifferentConditions() {
        // Given
        AssertionError testError = new AssertionError("Test error");
        AtomicInteger stringHandlerCallCount = new AtomicInteger(0);
        AtomicInteger integerHandlerCallCount = new AtomicInteger(0);

        Predicate<StackTraceElement[]> stringChecker = stackTrace -> {
            System.out.println("Checking stack trace for String handler...");
            for (StackTraceElement element : stackTrace) {
                System.out.println("Element: " + element.getClassName());
                if (element.getClassName().contains("CustomSoftAssertionTest")) {
                    return true;
                }
            }
            return false;
        };

        Predicate<StackTraceElement[]> integerChecker = stackTrace -> {
            System.out.println("Checking stack trace for Integer handler...");
            for (StackTraceElement element : stackTrace) {
                System.out.println("Element: " + element.getClassName());
                if (element.getClassName().contains("CustomSoftAssertion") && !element.getClassName().contains("CustomSoftAssertionTest")) {
                    return true;
                }
            }
            return false;
        };

        BiConsumer<AssertionError, String> stringHandler = (error, obj) -> stringHandlerCallCount.incrementAndGet();
        BiConsumer<AssertionError, Integer> integerHandler = (error, obj) -> integerHandlerCallCount.incrementAndGet();

        CustomSoftAssertion.registerCustomAssertion(
                String.class,
                stringHandler,
                stringChecker
        );
        CustomSoftAssertion.registerCustomAssertion(
                Integer.class,
                integerHandler,
                integerChecker
        );

        softAssertions.registerObjectForPostErrorHandling(String.class, "Test String");
        softAssertions.registerObjectForPostErrorHandling(Integer.class, 42);

        // When
        softAssertions.collectAssertionError(testError);

        // Then
        assertEquals(1, stringHandlerCallCount.get(), "String handler should be called for matching stack trace condition");
        assertEquals(0, integerHandlerCallCount.get(), "Integer handler should not be called for non-matching stack trace condition");
    }


}