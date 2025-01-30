package com.theairebellion.zeus.ui.log;

import com.theairebellion.zeus.logging.LogCommon;
import com.theairebellion.zeus.logging.LogCore;

public final class LogUI extends LogCore {

    private static LogUI INSTANCE;


    private LogUI() {
        super("Zeus.UI", "UI");
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

    private static LogUI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogUI();
        }
        return INSTANCE;
    }


}

