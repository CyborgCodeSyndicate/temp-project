package com.theairebellion.zeus.api.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestAssertionTargetTest {

    @Test
    void testEnumValues() {
        var values = RestAssertionTarget.values();
        assertAll(
                () -> assertEquals(3, values.length),
                () -> assertNotNull(RestAssertionTarget.valueOf("STATUS")),
                () -> assertNotNull(RestAssertionTarget.valueOf("BODY")),
                () -> assertNotNull(RestAssertionTarget.valueOf("HEADER"))
        );
    }

    @Test
    void testTargetMethod() {
        assertAll(
                () -> assertEquals(RestAssertionTarget.STATUS, RestAssertionTarget.STATUS.target()),
                () -> assertEquals(RestAssertionTarget.BODY, RestAssertionTarget.BODY.target()),
                () -> assertEquals(RestAssertionTarget.HEADER, RestAssertionTarget.HEADER.target())
        );
    }
}