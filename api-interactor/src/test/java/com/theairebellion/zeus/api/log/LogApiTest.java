package com.theairebellion.zeus.api.log;

import org.junit.jupiter.api.Test;

class LogApiTest {

    @Test
    void testAllLogMethods() {
        LogApi.info("Info message");
        LogApi.warn("Warn message");
        LogApi.error("Error message");
        LogApi.debug("Debug message");
        LogApi.trace("Trace message");
        LogApi.step("Step message");
        LogApi.validation("Validation message");
        LogApi.extended("Extended message");
        // No assertion needed for coverage; we just ensure no exceptions are thrown
    }
}