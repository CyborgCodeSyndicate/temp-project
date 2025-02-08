package com.theairebellion.zeus.db.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogDbTest {

    private static final String INFO_LOG = "Test info log";
    private static final String WARN_LOG = "Test warn log";
    private static final String ERROR_LOG = "Test error log";
    private static final String DEBUG_LOG = "Test debug log";
    private static final String STEP_LOG = "Test step log";

    @Test
    void testInfo_ShouldLogInfoMessage() {
        assertDoesNotThrow(() -> LogDb.info(INFO_LOG));
    }

    @Test
    void testWarn_ShouldLogWarnMessage() {
        assertDoesNotThrow(() -> LogDb.warn(WARN_LOG));
    }

    @Test
    void testError_ShouldLogErrorMessage() {
        assertDoesNotThrow(() -> LogDb.error(ERROR_LOG));
    }

    @Test
    void testDebug_ShouldLogDebugMessage() {
        assertDoesNotThrow(() -> LogDb.debug(DEBUG_LOG));
    }

    @Test
    void testStep_ShouldLogStepMessage() {
        assertDoesNotThrow(() -> LogDb.step(STEP_LOG));
    }
}