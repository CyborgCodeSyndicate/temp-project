package com.theairebellion.zeus.framework.assertion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.*;

class CustomSoftAssertionTest {

    private CustomSoftAssertion softAssertions;

    @BeforeEach
    void setUp() {
        softAssertions = new CustomSoftAssertion();
    }

    @Test
    void testRegisterAndRetrieveObject() {
        String testObject = "TestObject";
        softAssertions.registerObjectForPostErrorHandling(String.class, testObject);

        Optional<String> retrieved = softAssertions.getObjectFromType(String.class);
        assertTrue(retrieved.isPresent());
        assertEquals(testObject, retrieved.get());
    }

    @Test
    void testCollectAssertionError_ExecutesHandler() {
        AtomicBoolean handlerExecuted = new AtomicBoolean(false);

        BiConsumer<AssertionError, String> handler = (error, obj) -> handlerExecuted.set(true);
        Predicate<StackTraceElement[]> errorChecker = stackTrace -> true;

        CustomSoftAssertion.registerCustomAssertion(String.class, handler, errorChecker);
        softAssertions.registerObjectForPostErrorHandling(String.class, "Test");

        softAssertions.collectAssertionError(new AssertionError("Test error"));
        assertTrue(handlerExecuted.get());
    }

    @Test
    void testCollectAssertionError_DoesNotExecuteHandlerIfNoMatchingObject() {
        AtomicBoolean handlerExecuted = new AtomicBoolean(false);

        BiConsumer<AssertionError, Integer> handler = (error, obj) -> handlerExecuted.set(true);
        Predicate<StackTraceElement[]> errorChecker = stackTrace -> true;

        CustomSoftAssertion.registerCustomAssertion(Integer.class, handler, errorChecker);
        softAssertions.collectAssertionError(new AssertionError("Test error"));

        assertFalse(handlerExecuted.get());
    }

    @Test
    void testCollectAssertionError_DoesNotExecuteHandlerIfCheckerFails() {
        AtomicBoolean handlerExecuted = new AtomicBoolean(false);

        BiConsumer<AssertionError, String> handler = (error, obj) -> handlerExecuted.set(true);
        Predicate<StackTraceElement[]> errorChecker = stackTrace -> false;

        CustomSoftAssertion.registerCustomAssertion(String.class, handler, errorChecker);
        softAssertions.registerObjectForPostErrorHandling(String.class, "Test");

        softAssertions.collectAssertionError(new AssertionError("Test error"));
        assertFalse(handlerExecuted.get());
    }
}