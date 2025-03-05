package com.theairebellion.zeus.db.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseOperationExceptionTest {

    private static final String TEST_MESSAGE = "Test exception";
    private static final String ROOT_CAUSE_MESSAGE = "Root cause";

    @Test
    @DisplayName("Should set message and cause when both are provided")
    void testDatabaseOperationException_WithMessageAndCause() {
        // Given
        Throwable rootCause = new Throwable(ROOT_CAUSE_MESSAGE);

        // When
        DatabaseOperationException exception = new DatabaseOperationException(TEST_MESSAGE, rootCause);

        // Then
        assertEquals(TEST_MESSAGE, exception.getMessage(), "Message should match");
        assertEquals(rootCause, exception.getCause(), "Cause should match");
        assertEquals(ROOT_CAUSE_MESSAGE, exception.getCause().getMessage(), "Root cause message should match");
    }

    @Test
    @DisplayName("Should set only message when cause is not provided")
    void testDatabaseOperationException_WithMessageOnly() {
        // When
        DatabaseOperationException exception = new DatabaseOperationException(TEST_MESSAGE);

        // Then
        assertEquals(TEST_MESSAGE, exception.getMessage(), "Message should match");
        assertNull(exception.getCause(), "Cause should be null");
    }

    @Test
    @DisplayName("Should properly inherit from RuntimeException")
    void testDatabaseOperationException_InheritsFromRuntimeException() {
        // When
        DatabaseOperationException exception = new DatabaseOperationException(TEST_MESSAGE);

        // Then
        assertInstanceOf(RuntimeException.class, exception, "Should be a RuntimeException");
    }

    @Test
    @DisplayName("Should properly propagate when thrown")
    void testDatabaseOperationException_CanBeThrown() {
        // Given
        Throwable rootCause = new Throwable(ROOT_CAUSE_MESSAGE);

        // When/Then
        Exception caughtException = assertThrows(
                DatabaseOperationException.class,
                () -> {
                    throw new DatabaseOperationException(TEST_MESSAGE, rootCause);
                },
                "Should be able to throw and catch the exception"
        );

        // Verify it's the right exception with correct properties
        assertInstanceOf(DatabaseOperationException.class, caughtException);
        assertEquals(TEST_MESSAGE, caughtException.getMessage());
        assertEquals(rootCause, caughtException.getCause());
    }
}