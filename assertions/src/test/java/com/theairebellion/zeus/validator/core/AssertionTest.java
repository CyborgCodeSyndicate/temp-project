package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class AssertionTest {

    private static final String MY_KEY = "myKey";
    private static final String MY_VAL = "myVal";
    private static final String NEW_KEY = "newKey";

    @Test
    @DisplayName("Builder should correctly set all fields")
    void testBuilderAllFields() {
        // When
        var assertion = Assertion.<String>builder()
                .key(MY_KEY)
                .type(AssertionTypes.IS)
                .expected(MY_VAL)
                .target(() -> TestTarget.FIELD_ONE)
                .soft(true)
                .build();

        // Then
        assertAll(
                () -> assertEquals(MY_KEY, assertion.getKey(), "Key should match"),
                () -> assertEquals(AssertionTypes.IS, assertion.getType(), "Type should match"),
                () -> assertEquals(MY_VAL, assertion.getExpected(), "Expected value should match"),
                () -> assertEquals(TestTarget.FIELD_ONE, assertion.getTarget().target(), "Target should match"),
                () -> assertTrue(assertion.isSoft(), "Soft flag should be true")
        );
    }

    @Test
    @DisplayName("Builder should work with default soft value (false)")
    void testBuilderWithDefaultSoft() {
        // When
        var assertion = Assertion.<String>builder()
                .key(MY_KEY)
                .type(AssertionTypes.IS)
                .expected(MY_VAL)
                .target(() -> TestTarget.FIELD_ONE)
                .build();

        // Then
        assertFalse(assertion.isSoft(), "Soft should default to false");
    }

    @Test
    @DisplayName("Builder should set null key value")
    void testBuilderWithNullKey() {
        // When
        var assertion = Assertion.<String>builder()
                .key(null)
                .type(AssertionTypes.IS)
                .expected(MY_VAL)
                .target(() -> TestTarget.FIELD_ONE)
                .build();

        // Then
        assertNull(assertion.getKey(), "Key should be null");
    }

    @Test
    @DisplayName("Builder should accept null expected value")
    void testBuilderWithNullExpected() {
        // When
        var assertion = Assertion.<String>builder()
                .key(MY_KEY)
                .type(AssertionTypes.IS_NULL)
                .expected(null)
                .target(() -> TestTarget.FIELD_ONE)
                .build();

        // Then
        assertNull(assertion.getExpected(), "Expected should be null");
    }

    @Test
    @DisplayName("Builder should accept null type value")
    void testBuilderWithNullType() {
        // When
        var assertion = Assertion.<String>builder()
                .key(MY_KEY)
                .type(null)
                .expected(MY_VAL)
                .target(() -> TestTarget.FIELD_ONE)
                .build();

        // Then
        assertNull(assertion.getType(), "Type should be null");
    }

    @Test
    @DisplayName("Builder should accept null target value")
    void testBuilderWithNullTarget() {
        // When
        var assertion = Assertion.<String>builder()
                .key(MY_KEY)
                .type(AssertionTypes.IS)
                .expected(MY_VAL)
                .target(null)
                .build();

        // Then
        assertNull(assertion.getTarget(), "Target should be null");
    }

    @Test
    @DisplayName("Should allow changing the key after construction")
    void testKeySetter() {
        // Given
        var assertion = Assertion.<String>builder()
                .key(MY_KEY)
                .type(AssertionTypes.CONTAINS)
                .expected(MY_VAL)
                .target(() -> TestTarget.FIELD_TWO)
                .soft(false)
                .build();

        // When
        assertion.setKey(NEW_KEY);

        // Then
        assertEquals(NEW_KEY, assertion.getKey(), "Key should be updated");
    }

    @Test
    @DisplayName("Should allow setting key to null after construction")
    void testKeySetterWithNull() {
        // Given
        var assertion = Assertion.<String>builder()
                .key(MY_KEY)
                .type(AssertionTypes.CONTAINS)
                .expected(MY_VAL)
                .target(() -> TestTarget.FIELD_TWO)
                .build();

        // When
        assertion.setKey(null);

        // Then
        assertNull(assertion.getKey(), "Key should be null after setting to null");
    }

    @ParameterizedTest
    @EnumSource(AssertionTypes.class)
    @DisplayName("Should work with any assertion type")
    void testWithDifferentTypes(AssertionTypes type) {
        // When
        var assertion = Assertion.builder()
                .key(MY_KEY)
                .type(type)
                .expected("value")
                .target(() -> TestTarget.FIELD_ONE)
                .soft(false)
                .build();

        // Then
        assertEquals(type, assertion.getType(), "Type should match");
    }

    @Test
    @DisplayName("Should work with different generic types")
    void testWithDifferentGenericTypes() {
        // Test with Integer
        var intAssertion = Assertion.<Integer>builder()
                .key(MY_KEY)
                .type(AssertionTypes.GREATER_THAN)
                .expected(42)
                .target(() -> TestTarget.FIELD_ONE)
                .build();

        assertEquals(Integer.valueOf(42), intAssertion.getExpected(), "Expected value should be Integer 42");

        // Test with Boolean
        var boolAssertion = Assertion.<Boolean>builder()
                .key(MY_KEY)
                .type(AssertionTypes.IS)
                .expected(true)
                .target(() -> TestTarget.FIELD_ONE)
                .build();

        assertEquals(Boolean.TRUE, boolAssertion.getExpected(), "Expected value should be Boolean TRUE");
    }

    // Test enum for AssertionTarget implementation
    private enum TestTarget implements AssertionTarget {
        FIELD_ONE,
        FIELD_TWO;

        @Override
        public Enum<?> target() {
            return this;
        }
    }
}