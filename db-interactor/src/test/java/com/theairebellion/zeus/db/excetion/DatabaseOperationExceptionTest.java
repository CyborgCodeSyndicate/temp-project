package com.theairebellion.zeus.db.excetion;

import com.theairebellion.zeus.db.exceptions.DatabaseOperationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DatabaseOperationExceptionTest {

    @Test
    void testDatabaseOperationException_ShouldSetMessageAndCause() {
        Throwable cause = new Throwable("Root cause");
        DatabaseOperationException exception = new DatabaseOperationException("Test exception", cause);

        assertEquals("Test exception", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}