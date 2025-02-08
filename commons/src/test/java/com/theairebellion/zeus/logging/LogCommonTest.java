package com.theairebellion.zeus.logging;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

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
    private static final String INSTANCE_FIELD = "INSTANCE";

    @ParameterizedTest
    @MethodSource("loggingMethods")
    void testLoggingMethods(BiConsumer<String, Object[]> logMethod, String methodName) {
        var args = new Object[]{ARG_1, ARG_2};
        var message = formatMessage(methodName);

        var mockInstance = mock(LogCommon.class);
        LogCommon.extend(mockInstance);

        logMethod.accept(message, args);

        verifyLogMethod(mockInstance, methodName, message, args);
    }

    @Test
    void testExtend() throws Exception {
        var instanceField = getInstanceField();
        var originalInstance = instanceField.get(null);

        var mockInstance = mock(LogCommon.class);
        LogCommon.extend(mockInstance);
        assertSame(mockInstance, instanceField.get(null));

        instanceField.set(null, originalInstance);
    }

    @Test
    void testSingletonInitializationUsingReflection() throws Exception {
        var instanceField = getInstanceField();
        instanceField.set(null, null);

        var getInstanceMethod = LogCommon.class.getDeclaredMethod("getInstance");
        getInstanceMethod.setAccessible(true);

        var firstInstance = getInstanceMethod.invoke(null);
        assertNotNull(firstInstance);

        var secondInstance = getInstanceMethod.invoke(null);
        assertSame(firstInstance, secondInstance);
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

    private static String formatMessage(String methodName) {
        return "Test " + methodName + " message";
    }

    private static void verifyLogMethod(LogCommon mockInstance, String methodName, String message, Object[] args) {
        switch (methodName) {
            case INFO -> verify(mockInstance).infoLog(message, args);
            case WARN -> verify(mockInstance).warnLog(message, args);
            case ERROR -> verify(mockInstance).errorLog(message, args);
            case DEBUG -> verify(mockInstance).debugLog(message, args);
            case TRACE -> verify(mockInstance).traceLog(message, args);
            case STEP -> verify(mockInstance).stepLog(message, args);
            case EXTENDED -> verify(mockInstance).extendedLog(message, args);
            default -> throw new IllegalArgumentException("Unexpected method: " + methodName);
        }
    }

    private static Field getInstanceField() throws NoSuchFieldException {
        var instanceField = LogCommon.class.getDeclaredField(INSTANCE_FIELD);
        instanceField.setAccessible(true);
        return instanceField;
    }
}
