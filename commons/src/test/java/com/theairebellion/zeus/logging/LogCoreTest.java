package com.theairebellion.zeus.logging;

import com.theairebellion.zeus.logging.mock.DummyLogCore;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.MockedStatic;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class LogCoreTest {

    private static final String INFO = "info";
    private static final String WARN = "warn";
    private static final String ERROR = "error";
    private static final String DEBUG = "debug";
    private static final String TRACE = "trace";
    private static final String STEP = "step";
    private static final String VALIDATION = "validation";
    private static final String EXTENDED = "extended";
    private static final String ARG_1 = "arg1";
    private static final String ARG_2 = "arg2";
    private static final String TEST_LOGGER = "TestLogger";
    private static final String TEST_MARKER = "TestMarker";
    private static final String EXTENDED_MESSAGE = "Extended message";
    private static final String EXTENDED_LOGGING_FIELD = "EXTENDED_LOGGING";

    private Logger mockLogger;
    private Marker mockMarker;
    private DummyLogCore dummyLogCore;
    private MockedStatic<LogZeus> logZeusMockedStatic;

    @BeforeEach
    void setup() {
        mockLogger = mock(Logger.class);
        mockMarker = mock(Marker.class);

        logZeusMockedStatic = mockStatic(LogZeus.class);
        logZeusMockedStatic.when(() -> LogZeus.getLogger(TEST_LOGGER)).thenReturn(mockLogger);
        logZeusMockedStatic.when(() -> LogZeus.registerMarker(TEST_MARKER)).thenReturn(mockMarker);

        dummyLogCore = new DummyLogCore(TEST_LOGGER, TEST_MARKER);
    }

    @AfterEach
    void tearDown() {
        logZeusMockedStatic.close();
    }

    @ParameterizedTest(name = "verify {1} logging")
    @MethodSource("loggingMethods")
    void testLoggingMethods(BiConsumer<DummyLogCore, String[]> method, String methodName) {
        String message = "Test %s message".formatted(methodName);

        method.accept(dummyLogCore, new String[]{message, ARG_1, ARG_2});

        switch (methodName) {
            case INFO -> verify(mockLogger).info(eq(mockMarker), eq(message), aryEq(new Object[]{ARG_1, ARG_2}));
            case WARN -> verify(mockLogger).warn(eq(mockMarker), eq(message), aryEq(new Object[]{ARG_1, ARG_2}));
            case ERROR -> verify(mockLogger).error(eq(mockMarker), eq(message), aryEq(new Object[]{ARG_1, ARG_2}));
            case DEBUG -> verify(mockLogger).debug(eq(mockMarker), eq(message), aryEq(new Object[]{ARG_1, ARG_2}));
            case TRACE -> verify(mockLogger).trace(eq(mockMarker), eq(message), aryEq(new Object[]{ARG_1, ARG_2}));
            case STEP -> verify(mockLogger).log(eq(Level.forName("STEP", 350)), eq(mockMarker), eq(message),
                    aryEq(new Object[]{ARG_1, ARG_2}));
            case VALIDATION -> verify(mockLogger).log(eq(Level.forName("VALIDATION", 350)), eq(mockMarker), eq(message),
                    aryEq(new Object[]{ARG_1, ARG_2}));
        }
    }

    private static Stream<Arguments> loggingMethods() {
        return Stream.of(
                Arguments.of((BiConsumer<DummyLogCore, String[]>) (core, a) -> core.infoLog(a[0], a[1], a[2]), INFO),
                Arguments.of((BiConsumer<DummyLogCore, String[]>) (core, a) -> core.warnLog(a[0], a[1], a[2]), WARN),
                Arguments.of((BiConsumer<DummyLogCore, String[]>) (core, a) -> core.errorLog(a[0], a[1], a[2]), ERROR),
                Arguments.of((BiConsumer<DummyLogCore, String[]>) (core, a) -> core.debugLog(a[0], a[1], a[2]), DEBUG),
                Arguments.of((BiConsumer<DummyLogCore, String[]>) (core, a) -> core.traceLog(a[0], a[1], a[2]), TRACE),
                Arguments.of((BiConsumer<DummyLogCore, String[]>) (core, a) -> core.stepLog(a[0], a[1], a[2]), STEP),
                Arguments.of((BiConsumer<DummyLogCore, String[]>) (core, a) -> core.validationLog(a[0], a[1], a[2]),
                        VALIDATION)
        );
    }

    @Test
    void testExtendedLogWhenEnabled() throws Exception {
        setExtendedLogging(true);
        dummyLogCore.extendedLog(EXTENDED_MESSAGE, ARG_1, ARG_2);
        verify(mockLogger).log(
                eq(Level.forName(EXTENDED.toUpperCase(), 450)),
                eq(mockMarker),
                eq(EXTENDED_MESSAGE),
                aryEq(new Object[]{ARG_1, ARG_2})
        );
    }

    @Test
    void testExtendedLogWhenDisabled() throws Exception {
        setExtendedLogging(false);
        dummyLogCore.extendedLog(EXTENDED_MESSAGE, ARG_1, ARG_2);
        verify(mockLogger, never()).log(any(Level.class), eq(mockMarker), anyString(), any(Object[].class));
    }

    private void setExtendedLogging(boolean value) throws Exception {
        Field field = LogCore.class.getDeclaredField(EXTENDED_LOGGING_FIELD);
        field.setAccessible(true);
        field.set(null, value);
    }
}