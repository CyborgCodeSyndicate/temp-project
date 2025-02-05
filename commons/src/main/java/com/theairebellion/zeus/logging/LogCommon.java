package com.theairebellion.zeus.logging;

public final class LogCommon extends LogCore {

    private static LogCommon INSTANCE;


    private LogCommon() {
        super("Zeus.COMMON", "COMMON");
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


    public static void extended(String message, Object... args) {
        getInstance().extendedLog(message, args);
    }


    public static <T extends LogCore> void extend(final T INSTANCE) {
        LogCommon.INSTANCE = (LogCommon) INSTANCE;
    }


    private static LogCommon getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogCommon();
        }
        return INSTANCE;
    }

}
