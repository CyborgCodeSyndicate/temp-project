package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssertionResultTest {

    @Test
    void testConstructorAndGetters() {
        var result = new AssertionResult<>(
                true,
                "IS",
                "Hello",
                "Hello",
                false
        );

        assertAll(
                () -> assertTrue(result.isPassed()),
                () -> assertEquals("IS", result.getDescription()),
                () -> assertEquals("Hello", result.getExpectedValue()),
                () -> assertEquals("Hello", result.getActualValue()),
                () -> assertFalse(result.isSoft())
        );
    }

    @Test
    void testToString_Passed() {
        var result = new AssertionResult<>(
                true,
                "IS",
                "Hello",
                "Hello",
                false
        );

        var msg = result.toString();
        assertAll(
                () -> assertTrue(msg.contains("✔ Validation passed: IS")),
                () -> assertTrue(msg.contains("Expected: Hello, Actual: Hello"))
        );
    }

    @Test
    void testToString_Failed() {
        var result = new AssertionResult<>(
                false,
                "IS",
                "Hello",
                "World",
                false
        );

        var msg = result.toString();
        assertAll(
                () -> assertTrue(msg.contains("✘ Validation failed: IS")),
                () -> assertTrue(msg.contains("Expected: Hello, Actual: World"))
        );
    }

    @Test
    void testToString_Soft() {
        var result = new AssertionResult<>(
                false,
                "IS",
                "Hello",
                "World",
                true
        );

        var msg = result.toString();
        assertTrue(msg.contains("✘ Validation failed: IS"));
    }
}