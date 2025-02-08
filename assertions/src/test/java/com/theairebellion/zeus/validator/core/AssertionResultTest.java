package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssertionResultTest {

    private static final String IS_DESCRIPTION = "IS";
    private static final String HELLO = "Hello";
    private static final String WORLD = "World";

    @Test
    void testConstructorAndGetters() {
        var result = new AssertionResult<>(
                true,
                IS_DESCRIPTION,
                HELLO,
                HELLO,
                false
        );

        assertAll(
                () -> assertTrue(result.isPassed()),
                () -> assertEquals(IS_DESCRIPTION, result.getDescription()),
                () -> assertEquals(HELLO, result.getExpectedValue()),
                () -> assertEquals(HELLO, result.getActualValue()),
                () -> assertFalse(result.isSoft())
        );
    }

    @Test
    void testToString_Passed() {
        var result = new AssertionResult<>(
                true,
                IS_DESCRIPTION,
                HELLO,
                HELLO,
                false
        );

        var msg = result.toString();
        assertAll(
                () -> assertTrue(msg.contains("✔ Validation passed: " + IS_DESCRIPTION)),
                () -> assertTrue(msg.contains("Expected: Hello, Actual: Hello"))
        );
    }

    @Test
    void testToString_Failed() {
        var result = new AssertionResult<>(
                false,
                IS_DESCRIPTION,
                HELLO,
                WORLD,
                false
        );

        var msg = result.toString();
        assertAll(
                () -> assertTrue(msg.contains("✘ Validation failed: " + IS_DESCRIPTION)),
                () -> assertTrue(msg.contains("Expected: Hello, Actual: World"))
        );
    }

    @Test
    void testToString_Soft() {
        var result = new AssertionResult<>(
                false,
                IS_DESCRIPTION,
                HELLO,
                WORLD,
                true
        );

        var msg = result.toString();
        assertTrue(msg.contains("✘ Validation failed: " + IS_DESCRIPTION));
    }
}