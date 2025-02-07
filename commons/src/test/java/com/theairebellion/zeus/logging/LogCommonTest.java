package com.theairebellion.zeus.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class LogCommonTest {

    private static final String ARG_1 = "arg1";
    private static final String ARG_2 = "arg2";

    private static final String INFO = "info";
    private static final String WARN = "warn";
    private static final String ERROR = "error";
    private static final String DEBUG = "debug";
    private static final String TRACE = "trace";
    private static final String STEP = "step";
    private static final String EXTENDED = "extended";

    @ParameterizedTest(name = "verify {1} logging")
    @MethodSource("loggingMethods")
    void testLoggingMethods(BiConsumer<String, Object[]> method, String methodName) {
        String message = "Test %s message".formatted(methodName);
        Object[] args = {ARG_1, ARG_2};

        LogCommon mockInstance = mock(LogCommon.class);
        LogCommon.extend(mockInstance);

        method.accept(message, args);

        switch (methodName) {
            case INFO -> verify(mockInstance).infoLog(message, args);
            case WARN -> verify(mockInstance).warnLog(message, args);
            case ERROR -> verify(mockInstance).errorLog(message, args);
            case DEBUG -> verify(mockInstance).debugLog(message, args);
            case TRACE -> verify(mockInstance).traceLog(message, args);
            case STEP -> verify(mockInstance).stepLog(message, args);
            case EXTENDED -> verify(mockInstance).extendedLog(message, args);
            default -> throw new IllegalStateException("Unexpected method: " + methodName);
        }
    }

    private static Stream<Arguments> loggingMethods() {
        return Stream.of(
                Arguments.of((BiConsumer<String, Object[]>) LogCommon::info, INFO),
                Arguments.of((BiConsumer<String, Object[]>) LogCommon::warn, WARN),
                Arguments.of((BiConsumer<String, Object[]>) LogCommon::error, ERROR),
                Arguments.of((BiConsumer<String, Object[]>) LogCommon::debug, DEBUG),
                Arguments.of((BiConsumer<String, Object[]>) LogCommon::trace, TRACE),
                Arguments.of((BiConsumer<String, Object[]>) LogCommon::step, STEP),
                Arguments.of((BiConsumer<String, Object[]>) LogCommon::extended, EXTENDED)
        );
    }

    @Test
    void testExtend() throws Exception {
        Field instanceField = LogCommon.class.getDeclaredField("INSTANCE");
        instanceField.setAccessible(true);
        LogCommon original = (LogCommon) instanceField.get(null);

        LogCommon mockInstance = mock(LogCommon.class);
        LogCommon.extend(mockInstance);
        assertSame(mockInstance, instanceField.get(null));

        instanceField.set(null, original); // Cleanup
    }
}