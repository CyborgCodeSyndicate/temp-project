package com.theairebellion.zeus.framework.assertion;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import static org.junit.jupiter.api.Assertions.*;

public class CustomSoftAssertionTest {

    @Test
    void testRegisterAndGetObject() {
        CustomSoftAssertion customAssertion = new CustomSoftAssertion();
        customAssertion.registerObjectForPostErrorHandling(String.class, "Hello");
        Optional<String> result = customAssertion.getObjectFromType(String.class);
        assertTrue(result.isPresent(), "Object should be registered and retrievable");
        assertEquals("Hello", result.get());

        // For a non-registered type.
        Optional<Integer> intResult = customAssertion.getObjectFromType(Integer.class);
        assertFalse(intResult.isPresent(), "Non-registered type should return empty Optional");
    }

    @Test
    void testCollectAssertionErrorHandlerCalled() {
        CustomSoftAssertion customAssertion = new CustomSoftAssertion();
        customAssertion.registerObjectForPostErrorHandling(String.class, "Hello");
        AtomicBoolean handlerCalled = new AtomicBoolean(false);
        CustomSoftAssertion.registerCustomAssertion(String.class,
                (error, obj) -> {
                    if ("Hello".equals(obj)) {
                        handlerCalled.set(true);
                    }
                },
                stackTrace -> true);  // Always returns true
        AssertionError error = new AssertionError("Test error");
        customAssertion.collectAssertionError(error);
        assertTrue(handlerCalled.get(), "Handler should have been called");
    }

    @Test
    void testCollectAssertionErrorHandlerNotCalledDueToErrorChecker() {
        CustomSoftAssertion customAssertion = new CustomSoftAssertion();
        customAssertion.registerObjectForPostErrorHandling(String.class, "Hello");
        AtomicBoolean handlerCalled = new AtomicBoolean(false);
        CustomSoftAssertion.registerCustomAssertion(String.class,
                (error, obj) -> handlerCalled.set(true),
                stackTrace -> false);  // Always returns false
        AssertionError error = new AssertionError("Test error");
        customAssertion.collectAssertionError(error);
        assertFalse(handlerCalled.get(), "Handler should not be called because errorChecker returned false");
    }

    @Test
    void shouldStoreAndRetrieveRegisteredObjects() {
        CustomSoftAssertion softAssertion = new CustomSoftAssertion();
        String testObject = "Test String";

        softAssertion.registerObjectForPostErrorHandling(String.class, testObject);

        Optional<String> retrieved = softAssertion.getObjectFromType(String.class);
        assertTrue(retrieved.isPresent());
        assertEquals(testObject, retrieved.get());
    }

    @Test
    void shouldInvokePostErrorHandler() {
        AtomicBoolean handlerCalled = new AtomicBoolean(false);
        CustomSoftAssertion.registerCustomAssertion(
                String.class,
                (error, str) -> handlerCalled.set(true),
                stackTrace -> true
        );

        CustomSoftAssertion softAssertion = new CustomSoftAssertion();
        softAssertion.registerObjectForPostErrorHandling(String.class, "test");

        softAssertion.assertThat(true).isFalse(); // Trigger error
        softAssertion.assertAll();

        assertTrue(handlerCalled.get());
    }

    @Test
    void shouldRespectErrorCheckerPredicate() {
        AtomicBoolean handlerCalled = new AtomicBoolean(false);
        CustomSoftAssertion.registerCustomAssertion(
                String.class,
                (error, str) -> handlerCalled.set(true),
                stackTrace -> false // Never trigger handler
        );

        CustomSoftAssertion softAssertion = new CustomSoftAssertion();
        softAssertion.registerObjectForPostErrorHandling(String.class, "test");

        softAssertion.assertThat(true).isFalse();
        softAssertion.assertAll();

        assertFalse(handlerCalled.get());
    }

    @Test
    void shouldHandleMultipleErrorHandlers() {
        AtomicBoolean stringHandlerCalled = new AtomicBoolean(false);
        AtomicBoolean integerHandlerCalled = new AtomicBoolean(false);

        CustomSoftAssertion.registerCustomAssertion(
                String.class,
                (error, str) -> stringHandlerCalled.set(true),
                stackTrace -> true
        );

        CustomSoftAssertion.registerCustomAssertion(
                Integer.class,
                (error, num) -> integerHandlerCalled.set(true),
                stackTrace -> true
        );

        CustomSoftAssertion softAssertion = new CustomSoftAssertion();
        softAssertion.registerObjectForPostErrorHandling(String.class, "test");
        softAssertion.registerObjectForPostErrorHandling(Integer.class, 42);

        softAssertion.assertThat(true).isFalse();
        softAssertion.assertAll();

        assertTrue(stringHandlerCalled.get());
        assertTrue(integerHandlerCalled.get());
    }
}