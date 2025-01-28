package com.theairebellion.zeus.api.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestServiceExceptionTest {

    @Test
    void testConstructorAndMessage() {
        Exception cause = new Exception("Root cause");
        RestServiceException ex = new RestServiceException("Service failed", cause);

        assertEquals("Service failed", ex.getMessage());
        assertEquals(cause, ex.getCause());
    }
}