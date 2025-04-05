package com.theairebellion.zeus.db.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonPathExtractionExceptionTest {

    private static final String TEST_MESSAGE = "Test exception";
    private static final String ROOT_CAUSE_MESSAGE = "Root cause";
    private static final String JSON_PATH = "$.some.path";

    @Test
    @DisplayName("Should set message and cause when both are provided")
    void testJsonPathExtractionException_WithMessageAndCause() {
        // Given
        Throwable rootCause = new Throwable(ROOT_CAUSE_MESSAGE);

        // When
        JsonPathExtractionException exception = new JsonPathExtractionException(TEST_MESSAGE, rootCause);

        // Then
        assertEquals(TEST_MESSAGE, exception.getMessage(), "Message should match");
        assertEquals(rootCause, exception.getCause(), "Cause should match");
        assertEquals(ROOT_CAUSE_MESSAGE, exception.getCause().getMessage(), "Root cause message should match");
    }

    @Test
    @DisplayName("Should set only message when cause is not provided")
    void testJsonPathExtractionException_WithMessageOnly() {
        // When
        JsonPathExtractionException exception = new JsonPathExtractionException(TEST_MESSAGE);

        // Then
        assertEquals(TEST_MESSAGE, exception.getMessage(), "Message should match");
        assertNull(exception.getCause(), "Cause should be null");
    }

    @Test
    @DisplayName("Should properly inherit from RuntimeException")
    void testJsonPathExtractionException_InheritsFromRuntimeException() {
        // When
        JsonPathExtractionException exception = new JsonPathExtractionException(TEST_MESSAGE);

        // Then
        assertInstanceOf(RuntimeException.class, exception, "Should be a RuntimeException");
    }

    @Test
    @DisplayName("Should create exception with informative message for path")
    void testJsonPathExtractionException_WithJsonPath() {
        // When
        String message = "Failed to extract path: " + JSON_PATH;
        JsonPathExtractionException exception = new JsonPathExtractionException(message);

        // Then
        assertTrue(exception.getMessage().contains(JSON_PATH),
                "Exception message should contain the JSON path");
    }

    @Test
    @DisplayName("Should properly propagate when thrown")
    void testJsonPathExtractionException_CanBeThrown() {
        // Given
        Throwable rootCause = new Throwable(ROOT_CAUSE_MESSAGE);

        // When/Then
        Exception caughtException = assertThrows(
                JsonPathExtractionException.class,
                () -> {
                    throw new JsonPathExtractionException(TEST_MESSAGE, rootCause);
                },
                "Should be able to throw and catch the exception"
        );

        // Verify it's the right exception with correct properties
        assertInstanceOf(JsonPathExtractionException.class, caughtException);
        assertEquals(TEST_MESSAGE, caughtException.getMessage());
        assertEquals(rootCause, caughtException.getCause());
    }

    @Test
    @DisplayName("Should work with try-catch blocks")
    void testJsonPathExtractionException_WorksWithTryCatch() {
        // Given
        String exceptionMessage = "Error extracting path: " + JSON_PATH;

        // When
        String actualMessage = null;
        try {
            throw new JsonPathExtractionException(exceptionMessage);
        } catch (JsonPathExtractionException e) {
            actualMessage = e.getMessage();
        }

        // Then
        assertEquals(exceptionMessage, actualMessage, "Exception message should be preserved");
    }
}