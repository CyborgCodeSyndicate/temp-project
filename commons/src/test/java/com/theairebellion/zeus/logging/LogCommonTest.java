package com.theairebellion.zeus.logging;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

class LogCommonTest {

    private LogCommon logCommon;

    @BeforeEach
    void setup() {
        logCommon = mock(LogCommon.class);
    }

    @Test
    void testInfo() {
        try (MockedStatic<LogCommon> mockedStatic = Mockito.mockStatic(LogCommon.class)) {
            mockedStatic.when(() -> LogCommon.info("Test info message", "arg1", "arg2"))
                    .thenCallRealMethod();

            LogCommon.info("Test info message", "arg1", "arg2");

            mockedStatic.verify(() -> LogCommon.info("Test info message", "arg1", "arg2"));
        }
    }

    @Test
    void testWarn() {
        try (MockedStatic<LogCommon> mockedStatic = Mockito.mockStatic(LogCommon.class)) {
            mockedStatic.when(() -> LogCommon.warn("Test warn message", "arg1", "arg2"))
                    .thenCallRealMethod();

            LogCommon.warn("Test warn message", "arg1", "arg2");

            mockedStatic.verify(() -> LogCommon.warn("Test warn message", "arg1", "arg2"));
        }
    }

    @Test
    void testError() {
        try (MockedStatic<LogCommon> mockedStatic = Mockito.mockStatic(LogCommon.class)) {
            mockedStatic.when(() -> LogCommon.error("Test error message", "arg1", "arg2"))
                    .thenCallRealMethod();

            LogCommon.error("Test error message", "arg1", "arg2");

            mockedStatic.verify(() -> LogCommon.error("Test error message", "arg1", "arg2"));
        }
    }

    @Test
    void testDebug() {
        try (MockedStatic<LogCommon> mockedStatic = Mockito.mockStatic(LogCommon.class)) {
            mockedStatic.when(() -> LogCommon.debug("Test debug message", "arg1", "arg2"))
                    .thenCallRealMethod();

            LogCommon.debug("Test debug message", "arg1", "arg2");

            mockedStatic.verify(() -> LogCommon.debug("Test debug message", "arg1", "arg2"));
        }
    }

    @Test
    void testTrace() {
        try (MockedStatic<LogCommon> mockedStatic = Mockito.mockStatic(LogCommon.class)) {
            mockedStatic.when(() -> LogCommon.trace("Test trace message", "arg1", "arg2"))
                    .thenCallRealMethod();

            LogCommon.trace("Test trace message", "arg1", "arg2");

            mockedStatic.verify(() -> LogCommon.trace("Test trace message", "arg1", "arg2"));
        }
    }

    @Test
    void testStep() {
        try (MockedStatic<LogCommon> mockedStatic = Mockito.mockStatic(LogCommon.class)) {
            mockedStatic.when(() -> LogCommon.step("Test step message", "arg1", "arg2"))
                    .thenCallRealMethod();

            LogCommon.step("Test step message", "arg1", "arg2");

            mockedStatic.verify(() -> LogCommon.step("Test step message", "arg1", "arg2"));
        }
    }

    @Test
    void testExtended() {
        try (MockedStatic<LogCommon> mockedStatic = Mockito.mockStatic(LogCommon.class)) {
            mockedStatic.when(() -> LogCommon.extended("Test extended message", "arg1", "arg2"))
                    .thenCallRealMethod();

            LogCommon.extended("Test extended message", "arg1", "arg2");

            mockedStatic.verify(() -> LogCommon.extended("Test extended message", "arg1", "arg2"));
        }
    }

    @Test
    void testExtend() {
        try (MockedStatic<LogCommon> mockedStatic = Mockito.mockStatic(LogCommon.class)) {
            LogCommon mockInstance = mock(LogCommon.class);

            mockedStatic.when(() -> LogCommon.extend(mockInstance))
                    .thenCallRealMethod();

            LogCommon.extend(mockInstance);

            mockedStatic.verify(() -> LogCommon.extend(mockInstance));
        }
    }
}