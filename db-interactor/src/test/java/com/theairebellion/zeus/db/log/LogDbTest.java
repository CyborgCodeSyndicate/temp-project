package com.theairebellion.zeus.db.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LogDbTest {

    @Test
    void testInfo_ShouldLogInfoMessage() {
        assertDoesNotThrow(() -> LogDb.info("Test info log"));
    }

    @Test
    void testWarn_ShouldLogWarnMessage() {
        assertDoesNotThrow(() -> LogDb.warn("Test warn log"));
    }

    @Test
    void testError_ShouldLogErrorMessage() {
        assertDoesNotThrow(() -> LogDb.error("Test error log"));
    }

    @Test
    void testDebug_ShouldLogDebugMessage() {
        assertDoesNotThrow(() -> LogDb.debug("Test debug log"));
    }

    @Test
    void testStep_ShouldLogStepMessage() {
        assertDoesNotThrow(() -> LogDb.step("Test step log"));
    }
}