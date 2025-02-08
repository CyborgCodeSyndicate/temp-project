package com.theairebellion.zeus.framework.log;

import com.theairebellion.zeus.logging.LogCore;

public final class LogTest extends LogCore {

    private static LogTest INSTANCE;


    private LogTest() {
        super("Zeus.TEST", "TEST");
    }


    public static void info(String message, Object... args) {
        getInstance().infoLog(message, args);
    }


    public static void warn(String message, Object... args) {
        getInstance().warnLog(message, args);
    }


    public static void error(String message, Object... args) {
        getInstance().errorLog(message, args);
    }


    public static void debug(String message, Object... args) {
        getInstance().debugLog(message, args);
    }


    public static void trace(String message, Object... args) {
        getInstance().traceLog(message, args);
    }


    public static void step(String message, Object... args) {
        getInstance().stepLog(message, args);
    }


    public static void validation(String message, Object... args) {
        getInstance().validationLog(message, args);
    }


    public static void extended(String message, Object... args) {
        getInstance().extendedLog(message, args);
    }


    private static LogTest getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogTest();
        }
        return INSTANCE;
    }


}
