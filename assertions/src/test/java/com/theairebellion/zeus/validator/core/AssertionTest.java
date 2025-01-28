package com.theairebellion.zeus.validator.core;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AssertionTest {

    @Test
    void testBuilderAllFields() {
        Assertion<String> a = Assertion.builder(String.class)
                .key("myKey")
                .type(AssertionTypes.IS)
                .expected("myVal")
                .soft(true)
                .build();

        assertEquals("myKey", a.getKey());
        assertEquals(AssertionTypes.IS, a.getType());
        assertEquals("myVal", a.getExpected());
        assertTrue(a.isSoft());
    }

    @Test
    void testBuilderSoftSetter() {
        Assertion<String> a = Assertion.builder(String.class)
                .key("something")
                .type(AssertionTypes.CONTAINS)
                .expected("val")
                .soft(false)
                .build();

        a.setKey("newKey");
        assertEquals("newKey", a.getKey());
    }
}
