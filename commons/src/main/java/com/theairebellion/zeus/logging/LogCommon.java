package com.theairebellion.zeus.logging;

public final class LogCommon extends LogCore {

    private static LogCommon INSTANCE = new LogCommon();


    private LogCommon() {
        super("Zeus.COMMON", "COMMON");
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


    public static void extended(String message, Object... args) {
        INSTANCE.extendedLog(message, args);
    }


    public static <T extends LogCore> void extend(final T INSTANCE) {
        LogCommon.INSTANCE = (LogCommon) INSTANCE;
    }

}
