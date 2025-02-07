package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssertionTest {

    @Test
    void testBuilderAllFields() {
        var assertion = Assertion.builder(String.class)
                .key("myKey")
                .type(AssertionTypes.IS)
                .expected("myVal")
                .soft(true)
                .build();

        assertAll(
                () -> assertEquals("myKey", assertion.getKey()),
                () -> assertEquals(AssertionTypes.IS, assertion.getType()),
                () -> assertEquals("myVal", assertion.getExpected()),
                () -> assertTrue(assertion.isSoft())
        );
    }

    @Test
    void testBuilderSoftSetter() {
        var assertion = Assertion.builder(String.class)
                .key("something")
                .type(AssertionTypes.CONTAINS)
                .expected("val")
                .soft(false)
                .build();

        assertion.setKey("newKey");
        assertEquals("newKey", assertion.getKey());
    }
}