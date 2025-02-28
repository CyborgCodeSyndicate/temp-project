package com.theairebellion.zeus.api.exceptions;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("RestServiceException Tests")
class RestServiceExceptionTest {

    @Test
    @DisplayName("Constructor should set message and cause correctly")
    void constructorShouldSetMessageAndCauseCorrectly() {
        // Arrange
        String expectedMessage = "Service failed";
        Throwable expectedCause = new Exception("Root cause");

        // Act
        RestServiceException exception = new RestServiceException(expectedMessage, expectedCause);

        // Assert
        assertAll(
                () -> assertEquals(expectedMessage, exception.getMessage(),
                        "Exception message should match the provided message"),
                () -> assertEquals(expectedCause, exception.getCause(),
                        "Exception cause should match the provided cause")
        );
    }
}