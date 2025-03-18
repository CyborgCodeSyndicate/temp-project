package com.theairebellion.zeus.db.log;

import com.theairebellion.zeus.logging.LogCore;

/**
 * Provides logging utilities for database-related operations.
 * <p>
 * This class serves as the centralized logger for database interactions, offering
 * multiple log levels such as informational, warnings, debugging, validation, and step-based logging.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public final class LogDb extends LogCore {

    private static LogDb INSTANCE;

    private LogDb() {
        super("Zeus.DB", "DB");
    }

    /**
     * Logs an informational message.
     *
     * @param message The message to log.
     * @param args    Optional arguments to format the message.
     */
    public static void info(String message, Object... args) {
        getInstance().infoLog(message, args);
    }

    /**
     * Logs a warning message.
     *
     * @param message The warning message.
     * @param args    Optional arguments to format the message.
     */
    public static void warn(String message, Object... args) {
        getInstance().warnLog(message, args);
    }

    /**
     * Logs an error message.
     *
     * @param message The error message.
     * @param args    Optional arguments to format the message.
     */
    public static void error(String message, Object... args) {
        getInstance().errorLog(message, args);
    }

    /**
     * Logs a debug message.
     *
     * @param message The debug message.
     * @param args    Optional arguments to format the message.
     */
    public static void debug(String message, Object... args) {
        getInstance().debugLog(message, args);
    }

    /**
     * Logs a trace-level message.
     *
     * @param message The trace message.
     * @param args    Optional arguments to format the message.
     */
    public static void trace(String message, Object... args) {
        getInstance().traceLog(message, args);
    }

    /**
     * Logs a step message to track database operations.
     *
     * @param message The step description.
     * @param args    Optional arguments to format the message.
     */
    public static void step(String message, Object... args) {
        getInstance().stepLog(message, args);
    }

    /**
     * Logs a validation message for database assertions.
     *
     * @param message The validation message.
     * @param args    Optional arguments to format the message.
     */
    public static void validation(String message, Object... args) {
        getInstance().validationLog(message, args);
    }

    /**
     * Logs extended details when debugging is enabled.
     *
     * @param message The extended log message.
     * @param args    Optional arguments to format the message.
     */
    public static void extended(String message, Object... args) {
        getInstance().extendedLog(message, args);
    }

    /**
     * Extends the logging functionality using a custom log core instance.
     *
     * @param INSTANCE The custom log instance to extend.
     * @param <T>      A subclass of {@link LogCore}.
     */
    public static <T extends LogCore> void extend(final T INSTANCE) {
        LogDb.INSTANCE = (LogDb) INSTANCE;
    }

    /**
     * Retrieves the singleton instance of {@code LogDb}.
     *
     * @return The singleton instance of {@code LogDb}.
     */
    private static LogDb getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LogDb();
        }
        return INSTANCE;
    }

}
