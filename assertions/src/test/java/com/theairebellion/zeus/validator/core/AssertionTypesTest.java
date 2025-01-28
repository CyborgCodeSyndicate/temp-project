package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssertionTypesTest {

    @Test
    void testEachEnum() {
        for (AssertionTypes t : AssertionTypes.values()) {
            assertSame(t, t.type());
            assertNotNull(t.getSupportedType());
        }
    }

    @Test
    void testSample() {
        assertEquals(Number.class, AssertionTypes.GREATER_THAN.getSupportedType());
        assertEquals(String.class, AssertionTypes.CONTAINS.getSupportedType());
    }
}
