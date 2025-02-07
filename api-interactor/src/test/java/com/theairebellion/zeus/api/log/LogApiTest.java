package com.theairebellion.zeus.api.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogApiTest {

    @Test
    void testAllLogMethods() {
        assertAll(
                () -> LogApi.info("Info message"),
                () -> LogApi.warn("Warn message"),
                () -> LogApi.error("Error message"),
                () -> LogApi.debug("Debug message"),
                () -> LogApi.trace("Trace message"),
                () -> LogApi.step("Step message"),
                () -> LogApi.validation("Validation message"),
                () -> LogApi.extended("Extended message")
        );
    }
}