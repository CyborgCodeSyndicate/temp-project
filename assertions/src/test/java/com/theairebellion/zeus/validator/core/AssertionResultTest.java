package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class AssertionResultTest {

    private static final String IS_DESCRIPTION = "IS";
    private static final String HELLO = "Hello";
    private static final String WORLD = "World";

    @Test
    @DisplayName("Should correctly initialize and provide access to all properties")
    void testConstructorAndGetters() {
        // Given
        boolean passed = true;
        boolean soft = false;

        // When
        var result = new AssertionResult<>(passed, IS_DESCRIPTION, HELLO, HELLO, soft);

        // Then
        assertAll(
                () -> assertTrue(result.isPassed(), "isPassed should be true"),
                () -> assertEquals(IS_DESCRIPTION, result.getDescription(), "Description should match"),
                () -> assertEquals(HELLO, result.getExpectedValue(), "Expected value should match"),
                () -> assertEquals(HELLO, result.getActualValue(), "Actual value should match"),
                () -> assertFalse(result.isSoft(), "isSoft should be false")
        );
    }

    @Test
    @DisplayName("Should correctly handle null values in constructor")
    void testConstructorWithNullValues() {
        // When
        var result = new AssertionResult<>(false, null, null, null, true);

        // Then
        assertAll(
                () -> assertFalse(result.isPassed(), "isPassed should be false"),
                () -> assertNull(result.getDescription(), "Description should be null"),
                () -> assertNull(result.getExpectedValue(), "Expected value should be null"),
                () -> assertNull(result.getActualValue(), "Actual value should be null"),
                () -> assertTrue(result.isSoft(), "isSoft should be true")
        );
    }

    @Test
    @DisplayName("Should correctly handle different value types")
    void testConstructorWithDifferentTypes() {
        // Given
        Integer expected = 42;
        Double actual = 42.0;

        // When
        var result = new AssertionResult<>(false, IS_DESCRIPTION, expected, actual, false);

        // Then
        assertAll(
                () -> assertEquals(expected, result.getExpectedValue()),
                () -> assertEquals(actual, result.getActualValue())
        );
    }

    @Nested
    @DisplayName("toString() method tests")
    class ToStringTests {

        @Test
        @DisplayName("Should format success message correctly when validation passes")
        void testToString_Passed() {
            // Given
            var result = new AssertionResult<>(true, IS_DESCRIPTION, HELLO, HELLO, false);

            // When
            var msg = result.toString();

            // Then
            assertAll(
                    () -> assertTrue(msg.contains("✔ Validation passed: " + IS_DESCRIPTION),
                            "Should contain success prefix and description"),
                    () -> assertTrue(msg.contains("Expected: Hello, Actual: Hello"),
                            "Should contain expected and actual values")
            );
        }

        @Test
        @DisplayName("Should format failure message correctly when validation fails")
        void testToString_Failed() {
            // Given
            var result = new AssertionResult<>(false, IS_DESCRIPTION, HELLO, WORLD, false);

            // When
            var msg = result.toString();

            // Then
            assertAll(
                    () -> assertTrue(msg.contains("✘ Validation failed: " + IS_DESCRIPTION),
                            "Should contain failure prefix and description"),
                    () -> assertTrue(msg.contains("Expected: Hello, Actual: World"),
                            "Should contain expected and actual values")
            );
        }

        @ParameterizedTest
        @CsvSource({
                "true, ✔ Validation passed",
                "false, ✘ Validation failed"
        })
        @DisplayName("Should format message according to passed status")
        void testToString_Format(boolean passed, String expectedPrefix) {
            // Given
            var result = new AssertionResult<>(passed, IS_DESCRIPTION, HELLO,
                    passed ? HELLO : WORLD, true);

            // When
            var msg = result.toString();

            // Then
            assertTrue(msg.contains(expectedPrefix),
                    "Should contain correct prefix based on passed status");
        }

        @Test
        @DisplayName("Should handle null values in toString gracefully")
        void testToString_NullValues() {
            // Given
            var result = new AssertionResult<>(true, null, null, null, false);

            // When
            var msg = result.toString();

            // Then
            assertAll(
                    () -> assertTrue(msg.contains("✔ Validation passed: null"),
                            "Should handle null description"),
                    () -> assertTrue(msg.contains("Expected: null, Actual: null"),
                            "Should handle null expected and actual values")
            );
        }

        @Test
        @DisplayName("Should handle complex object values in toString")
        void testToString_ComplexObjects() {
            // Given
            var expected = new TestClass("expected");
            var actual = new TestClass("actual");
            var result = new AssertionResult<>(false, IS_DESCRIPTION, expected, actual, false);

            // When
            var msg = result.toString();

            // Then
            assertAll(
                    () -> assertTrue(msg.contains(expected.toString()), "Should include expected object's toString"),
                    () -> assertTrue(msg.contains(actual.toString()), "Should include actual object's toString")
            );
        }
    }

    @Test
    @DisplayName("Should handle boolean soft property")
    void testSoftProperty() {
        // Given
        var result = new AssertionResult<>(true, IS_DESCRIPTION, HELLO, HELLO, true);

        // Then
        assertTrue(result.isSoft(), "isSoft should be true");
    }

    // Helper class for testing complex objects
    private static class TestClass {
        private final String value;

        TestClass(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "TestClass[" + value + "]";
        }
    }
}