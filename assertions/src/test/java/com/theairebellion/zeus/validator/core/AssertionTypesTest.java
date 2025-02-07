package com.theairebellion.zeus.validator.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AssertionTypesTest {

    @Test
    void testEachEnum() {
        for (var type : AssertionTypes.values()) {
            assertAll(
                    () -> assertSame(type, type.type()),
                    () -> assertNotNull(type.getSupportedType())
            );
        }
    }

    @Test
    void testSample() {
        assertAll(
                () -> assertEquals(Number.class, AssertionTypes.GREATER_THAN.getSupportedType()),
                () -> assertEquals(String.class, AssertionTypes.CONTAINS.getSupportedType())
        );
    }
}