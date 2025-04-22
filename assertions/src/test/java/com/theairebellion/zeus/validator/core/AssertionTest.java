package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
                            .target(TestTarget.FIELD_ONE)
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
                            .target(TestTarget.FIELD_ONE)
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
                            .target(TestTarget.FIELD_ONE)
                            .build();

        // Then
        assertNull(assertion.getKey(), "Key should be null");
    }


    @Test
    @DisplayName("Builder should not accept null for target")
    void testBuilderThrowsExceptionWhenNullValueForTarget() {
        // Then
        assertThrows(NullPointerException.class, () -> {
            // When
            Assertion.<String>builder()
                .key(MY_KEY)
                .type(AssertionTypes.IS)
                .expected(MY_VAL)
                .soft(true)
                .build();
            ;
        }, "Builder should throw NullPointerException when target is not added");
    }


    @DisplayName("Builder should not accept null for type")
    void testBuilderThrowsExceptionWhenNullValueForType() {
        // Then
        assertThrows(NullPointerException.class, () -> {
            // When
            Assertion.<String>builder()
                .key(MY_KEY)
                .expected(MY_VAL)
                .target(TestTarget.FIELD_ONE)
                .soft(true)
                .build();
            ;
        }, "Builder should throw NullPointerException when type is not added");
    }


    @DisplayName("Builder should not accept null for expected")
    void testBuilderThrowsExceptionWhenNullValueForExpected() {
        // Then
        assertThrows(NullPointerException.class, () -> {
            // When
            Assertion.<String>builder()
                .key(MY_KEY)
                .type(AssertionTypes.IS)
                .target(TestTarget.FIELD_ONE)
                .soft(true)
                .build();
            ;
        }, "Builder should throw NullPointerException when expected is not added");
    }


    @Test
    @DisplayName("Builder should accept null type value for key")
    void testBuilderWithNullType() {
        // When
        var assertion = Assertion.<String>builder()
                            .type(AssertionTypes.IS)
                            .target(TestTarget.FIELD_ONE)
                            .soft(true)
                            .expected(MY_VAL)
                            .build();

        // Then
        assertNull(assertion.getKey(), "Key should be null");
    }


    @Test
    @DisplayName("Should allow changing the key after construction")
    void testKeySetter() {
        // Given
        var assertion = Assertion.<String>builder()
                            .key(MY_KEY)
                            .type(AssertionTypes.CONTAINS)
                            .expected(MY_VAL)
                            .target(TestTarget.FIELD_TWO)
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
                            .target(TestTarget.FIELD_TWO)
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
                            .target(TestTarget.FIELD_ONE)
                            .soft(false)
                            .build();

        // Then
        assertEquals(type, assertion.getType(), "Type should match");
    }





    // Test enum for AssertionTarget implementation
    private enum TestTarget implements AssertionTarget<TestTarget> {
        FIELD_ONE,
        FIELD_TWO;


        @Override
        public TestTarget target() {
            return this;
        }
    }

}