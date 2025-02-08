package com.theairebellion.zeus.db.excetion;

import com.theairebellion.zeus.db.exceptions.JsonPathExtractionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonPathExtractionExceptionTest {

    private static final String TEST_MESSAGE = "Test exception";
    private static final String ROOT_CAUSE_MESSAGE = "Root cause";

    @Test
    void testJsonPathExtractionException_ShouldSetMessageAndCause() {
        Throwable rootCause = new Throwable(ROOT_CAUSE_MESSAGE);
        JsonPathExtractionException exception = new JsonPathExtractionException(TEST_MESSAGE, rootCause);

        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertEquals(rootCause, exception.getCause());
    }
}
