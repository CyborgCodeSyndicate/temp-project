package com.theairebellion.zeus.api.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestServiceExceptionTest {

    @Test
    void testConstructorAndMessage() {
        var cause = new Exception("Root cause");
        var ex = new RestServiceException("Service failed", cause);

        assertAll(
                () -> assertEquals("Service failed", ex.getMessage()),
                () -> assertEquals(cause, ex.getCause())
        );
    }
}