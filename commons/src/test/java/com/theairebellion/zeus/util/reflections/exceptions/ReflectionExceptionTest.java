package com.theairebellion.zeus.util.reflections.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionExceptionTest {

    @Test
    void testConstructorWithMessage() {
        ReflectionException exception = new ReflectionException("Test message");
        assertEquals("Test message", exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException("Cause");
        ReflectionException exception = new ReflectionException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}