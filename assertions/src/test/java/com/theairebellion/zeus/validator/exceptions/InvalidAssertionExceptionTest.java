package com.theairebellion.zeus.validator.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InvalidAssertionExceptionTest {

    private static final String ERROR_MESSAGE = "Invalid assertion";

    @Test
    @DisplayName("Exception should be created with the provided message")
    void testConstructorWithMessage() {
        // When
        var exception = new InvalidAssertionException(ERROR_MESSAGE);

        // Then
        assertEquals(ERROR_MESSAGE, exception.getMessage(), "Exception message should match the provided message");
    }

    @Test
    @DisplayName("Exception should inherit from IllegalArgumentException")
    void testInheritance() {
        // When
        var exception = new InvalidAssertionException(ERROR_MESSAGE);

        // Then
        assertTrue(exception instanceof IllegalArgumentException,
                "InvalidAssertionException should be a subclass of IllegalArgumentException");
    }

    @Test
    @DisplayName("Exception should be throwable and catchable")
    void testExceptionCanBeThrown() {
        // Then
        assertThrows(InvalidAssertionException.class, () -> {
            throw new InvalidAssertionException(ERROR_MESSAGE);
        }, "Should be able to throw and catch InvalidAssertionException");
    }
}