package com.theairebellion.zeus.framework.log;

import com.theairebellion.zeus.logging.LogCore;
import com.theairebellion.zeus.logging.LogZeus;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LogTestTest {
    // Constants for test data
    static final String INFO_MESSAGE = "info message";
    static final String WARN_MESSAGE = "warn message";
    static final String ERROR_MESSAGE = "error message";
    static final String DEBUG_MESSAGE = "debug message";
    static final String TRACE_MESSAGE = "trace message";
    static final String STEP_MESSAGE = "step message";
    static final String VALIDATION_MESSAGE = "validation message";
    static final String EXTENDED_MESSAGE = "extended message";

    static final String EXTENDED_LOGGING = "extended.logging";
    static final String TRUE = "true";

    static final String ARG_1 = "arg1";
    static final String ARG_2 = "arg2";
    static final String ARG_3 = "arg3";
    static final String ARG_4 = "arg4";
    static final String ARG_5 = "arg5";
    static final String ARG_6 = "arg6";
    static final String ARG_7 = "arg7";
    static final String ARG_8 = "arg8";

    @BeforeEach
    void setUp() throws Exception {
        clearLogTestInstance();
        clearExtendedLogging();
    }

    @AfterEach
    void tearDown() throws Exception {
        clearLogTestInstance();
        clearExtendedLogging();
        System.clearProperty(EXTENDED_LOGGING);
    }

    @Test
    @DisplayName("Test info log method")
    void testInfoLog() {
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);

            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_1, 100};
            LogTest.info(INFO_MESSAGE, args);

            verify(mockLogger, times(1)).info(mockMarker, INFO_MESSAGE, args);
        }
    }

    @Test
    @DisplayName("Test warn log method")
    void testWarnLog() {
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);

            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_2, 200};
            LogTest.warn(WARN_MESSAGE, args);

            verify(mockLogger, times(1)).warn(mockMarker, WARN_MESSAGE, args);
        }
    }

    @Test
    @DisplayName("Test error log method")
    void testErrorLog() {
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);

            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_3, 300};
            LogTest.error(ERROR_MESSAGE, args);

            verify(mockLogger, times(1)).error(mockMarker, ERROR_MESSAGE, args);
        }
    }

    @Test
    @DisplayName("Test debug log method")
    void testDebugLog() {
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);

            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_4, 400};
            LogTest.debug(DEBUG_MESSAGE, args);

            verify(mockLogger, times(1)).debug(mockMarker, DEBUG_MESSAGE, args);
        }
    }

    @Test
    @DisplayName("Test trace log method")
    void testTraceLog() {
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);

            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_5, 500};
            LogTest.trace(TRACE_MESSAGE, args);

            verify(mockLogger, times(1)).trace(mockMarker, TRACE_MESSAGE, args);
        }
    }

    @Test
    @DisplayName("Test step log method")
    void testStepLog() {
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);
            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_6, 600};
            Level stepLevel = Level.forName("STEP", 350);

            LogTest.step(STEP_MESSAGE, args);

            verify(mockLogger, times(1)).log(stepLevel, mockMarker, STEP_MESSAGE, args);
        }
    }

    @Test
    @DisplayName("Test validation log method")
    void testValidationLog() {
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);
            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_7, 700};
            Level validationLevel = Level.forName("VALIDATION", 350);

            LogTest.validation(VALIDATION_MESSAGE, args);

            verify(mockLogger, times(1)).log(validationLevel, mockMarker, VALIDATION_MESSAGE, args);
        }
    }

    @Test
    @DisplayName("Test extended log method when disabled")
    void testExtendedLogDisabled() {
        System.clearProperty(EXTENDED_LOGGING);
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);
            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_8, 800};

            LogTest.extended(EXTENDED_MESSAGE, args);

            verify(mockLogger, times(0)).log(
                    any(Level.class),
                    eq(mockMarker),
                    any(String.class),
                    any(Object[].class)
            );
        }
    }

    @Test
    @DisplayName("Test extended log method when enabled")
    void testExtendedLogEnabled() {
        System.setProperty(EXTENDED_LOGGING, TRUE);
        try (MockedStatic<LogZeus> mockedZeus = Mockito.mockStatic(LogZeus.class)) {
            Logger mockLogger = mock(Logger.class);
            Marker mockMarker = mock(Marker.class);
            mockedZeus.when(() -> LogZeus.getLogger("Zeus.TEST")).thenReturn(mockLogger);
            mockedZeus.when(() -> LogZeus.registerMarker("TEST")).thenReturn(mockMarker);

            Object[] args = {ARG_8, 800};
            Level extendedLevel = Level.forName("EXTENDED", 450);

            LogTest.extended(EXTENDED_MESSAGE, args);

            verify(mockLogger, times(1)).log(extendedLevel, mockMarker, EXTENDED_MESSAGE, args);
        }
    }

    // Reflection-based methods to reset static state
    private void clearLogTestInstance() throws Exception {
        Field instanceField = LogTest.class.getDeclaredField("INSTANCE");
        instanceField.setAccessible(true);
        instanceField.set(null, null);
    }

    private void clearExtendedLogging() throws Exception {
        Field extendedField = LogCore.class.getDeclaredField("EXTENDED_LOGGING");
        extendedField.setAccessible(true);
        extendedField.set(null, null);
    }
}