package com.theairebellion.zeus.util.reflections.exceptions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReflectionExceptionTest {

    private static final String TEST_MESSAGE = "Test message";
    private static final String CAUSE_MESSAGE = "Cause";

    @Test
    void testConstructorWithMessage() {
        ReflectionException exception = new ReflectionException(TEST_MESSAGE);
        assertEquals(TEST_MESSAGE, exception.getMessage());
    }

    @Test
    void testConstructorWithMessageAndCause() {
        Throwable cause = new RuntimeException(CAUSE_MESSAGE);
        ReflectionException exception = new ReflectionException(TEST_MESSAGE, cause);
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}
