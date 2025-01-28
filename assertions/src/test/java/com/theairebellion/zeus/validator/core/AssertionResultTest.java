package com.theairebellion.zeus.validator.core;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionResultTest {

    @Test
    void testConstructorAndGetters() {
        AssertionResult<String> r = new AssertionResult<>(
                true,
                "IS",
                "Hello",
                "Hello",
                false
        );
        assertTrue(r.isPassed());
        assertEquals("IS", r.getDescription());
        assertEquals("Hello", r.getExpectedValue());
        assertEquals("Hello", r.getActualValue());
        assertFalse(r.isSoft());
    }

    @Test
    void testToString_Passed() {
        AssertionResult<String> r = new AssertionResult<>(
                true,
                "IS",
                "Hello",
                "Hello",
                false
        );
        String msg = r.toString();
        assertTrue(msg.contains("✔ Validation passed: IS"));
        assertTrue(msg.contains("Expected: Hello, Actual: Hello"));
    }

    @Test
    void testToString_Failed() {
        AssertionResult<String> r = new AssertionResult<>(
                false,
                "IS",
                "Hello",
                "World",
                false
        );
        String msg = r.toString();
        assertTrue(msg.contains("✘ Validation failed: IS"));
        assertTrue(msg.contains("Expected: Hello, Actual: World"));
    }

    @Test
    void testToString_Soft() {
        AssertionResult<String> r = new AssertionResult<>(
                false,
                "IS",
                "Hello",
                "World",
                true
        );
        String msg = r.toString();
        assertTrue(msg.contains("✘ Validation failed: IS"));
    }
}
