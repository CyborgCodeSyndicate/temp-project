package com.theairebellion.zeus.framework.log;

import com.theairebellion.zeus.logging.LogCore;

/**
 * Provides structured logging utilities for test execution.
 * <p>
 * This class extends {@code LogCore} and offers various logging levels tailored
 * for test execution, including information, warnings, errors, debugging, tracing,
 * and validation-specific logs.
 * </p>
 *
 * <p>
 * The logging is categorized under the "Zeus.TEST" namespace, ensuring test-related logs
 * are distinguishable from other logs in the system.
 * </p>
 *
 * <p>
 * This class follows the singleton pattern, ensuring that only one instance of
 * {@code LogTest} exists.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public final class LogTest extends LogCore {

    private static LogTest INSTANCE;

    /**
     * Private constructor to enforce singleton pattern.
     * Initializes the logger with a predefined namespace.
     */
    private LogTest() {
        super("Zeus.TEST", "TEST");
    }

    /**
     * Logs an informational message.
     *
     * @param message The log message template.
     * @param args    The arguments to be formatted into the message.
     */
    public static void info(String message, Object... args) {
        getInstance().infoLog(message, args);
    }

    /**
     * Logs a warning message.
     *
     * @param message The log message template.
     * @param args    The arguments to be formatted into the message.
     */
    public static void warn(String message, Object... args) {
        getInstance().warnLog(message, args);
    }

    /**
     * Logs an error message.
     *
     * @param message The log message template.
     * @param args    The arguments to be formatted into the message.
     */
    public static void error(String message, Object... args) {
        getInstance().errorLog(message, args);
    }

    /**
     * Logs a debug message.
     *
     * @param message The log message template.
     * @param args    The arguments to be formatted into the message.
     */
    public static void debug(String message, Object... args) {
        getInstance().debugLog(message, args);
    }

    /**
     * Logs a trace-level message.
     *
     * @param message The log message template.
     * @param args    The arguments to be formatted into the message.
     */
    public static void trace(String message, Object... args) {
        getInstance().traceLog(message, args);
    }

    /**
     * Logs a step in the test execution process.
     *
     * @param message The log message template.
     * @param args    The arguments to be formatted into the message.
     */
    public static void step(String message, Object... args) {
        getInstance().stepLog(message, args);
    }

    /**
     * Logs a validation-specific message.
     *
     * @param message The log message template.
     * @param args    The arguments to be formatted into the message.
     */
    public static void validation(String message, Object... args) {
        getInstance().validationLog(message, args);
    }

    /**
     * Logs an extended test execution message.
     *
     * @param message The log message template.
     * @param args    The arguments to be formatted into the message.
     */
    public static void extended(String message, Object... args) {
        getInstance().extendedLog(message, args);
    }

    /**
     * Retrieves the singleton instance of {@code LogTest}.
     *
     * @return The singleton instance of {@code LogTest}.
     */
    private static LogTest getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogTest();
        }
        return INSTANCE;
    }

}
