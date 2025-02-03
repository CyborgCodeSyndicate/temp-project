package com.theairebellion.zeus.db.excetion;

import com.theairebellion.zeus.db.exceptions.JsonPathExtractionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonPathExtractionExceptionTest {

    @Test
    void testJsonPathExtractionException_ShouldSetMessageAndCause() {
        Throwable cause = new Throwable("Root cause");
        JsonPathExtractionException exception = new JsonPathExtractionException("Test exception", cause);

        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
