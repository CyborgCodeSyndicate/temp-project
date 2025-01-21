package com.theairebellion.zeus.db.log;

import com.theairebellion.zeus.logging.LogCore;

public final class LogDB extends LogCore {

    private static LogDB INSTANCE = new LogDB();


    private LogDB() {
        super("Zeus.DB", "DB");
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


    public static <T extends LogCore> void extend(final T INSTANCE) {
        LogDB.INSTANCE = (LogDB) INSTANCE;
    }

}
