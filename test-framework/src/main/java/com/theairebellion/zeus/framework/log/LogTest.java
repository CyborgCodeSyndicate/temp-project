package com.theairebellion.zeus.framework.log;

import com.theairebellion.zeus.logging.LogCore;

public final class LogTest extends LogCore {

    private static final LogTest INSTANCE = new LogTest();


    private LogTest() {
        super("Zeus.TEST", "TEST");
    }


    public static void info(String message, Object... args) {
        INSTANCE.infoLog(message, args);
    }


    public static void warn(String message, Object... args) {
        INSTANCE.warnLog(message, args);
    }


    public static void error(String message, Object... args) {
        INSTANCE.errorLog(message, args);
    }


    public static void debug(String message, Object... args) {
        INSTANCE.debugLog(message, args);
    }


    public static void trace(String message, Object... args) {
        INSTANCE.traceLog(message, args);
    }


    public static void step(String message, Object... args) {
        INSTANCE.stepLog(message, args);
    }


    public static void validation(String message, Object... args) {
        INSTANCE.validationLog(message, args);
    }

    public static void extended(String message, Object... args) {
        INSTANCE.extendedLog(message, args);
    }

}
