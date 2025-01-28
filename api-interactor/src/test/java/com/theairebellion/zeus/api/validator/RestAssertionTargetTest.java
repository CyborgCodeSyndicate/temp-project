package com.theairebellion.zeus.api.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestAssertionTargetTest {

    @Test
    void testEnumValues() {
        assertEquals(3, RestAssertionTarget.values().length);

        // Ensure each is accessible
        assertNotNull(RestAssertionTarget.valueOf("STATUS"));
        assertNotNull(RestAssertionTarget.valueOf("BODY"));
        assertNotNull(RestAssertionTarget.valueOf("HEADER"));
    }

    @Test
    void testTargetMethod() {
        assertEquals(RestAssertionTarget.STATUS, RestAssertionTarget.STATUS.target());
        assertEquals(RestAssertionTarget.BODY, RestAssertionTarget.BODY.target());
        assertEquals(RestAssertionTarget.HEADER, RestAssertionTarget.HEADER.target());
    }
}