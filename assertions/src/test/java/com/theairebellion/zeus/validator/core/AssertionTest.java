package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssertionTest {

    public static final String MY_KEY = "myKey";
    public static final String MY_VAL = "myVal";
    public static final String NEW_KEY = "newKey";

    @Test
    void testBuilderAllFields() {
        var assertion = Assertion.builder()
                .key(MY_KEY)
                .type(AssertionTypes.IS)
                .expected(MY_VAL)
                .soft(true)
                .build();

        assertAll(
                () -> assertEquals(MY_KEY, assertion.getKey()),
                () -> assertEquals(AssertionTypes.IS, assertion.getType()),
                () -> assertEquals(MY_VAL, assertion.getExpected()),
                () -> assertTrue(assertion.isSoft())
        );
    }

    @Test
    void testBuilderSoftSetter() {
        var assertion = Assertion.builder()
                .key("something")
                .type(AssertionTypes.CONTAINS)
                .expected("val")
                .soft(false)
                .build();

        assertion.setKey(NEW_KEY);
        assertEquals(NEW_KEY, assertion.getKey());
    }
}