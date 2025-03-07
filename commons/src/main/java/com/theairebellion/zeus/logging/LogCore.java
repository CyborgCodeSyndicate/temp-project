package com.theairebellion.zeus.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

/**
 * Provides a core logging mechanism for structured and categorized logging.
 * <p>
 * This abstract class serves as a base for logging operations, utilizing Log4j2
 * for different logging levels, including standard logs (info, warn, error, debug, trace)
 * and custom levels such as {@code STEP}, {@code VALIDATION}, and {@code EXTENDED}.
 * It integrates with {@link LogZeus} to manage logger instances and markers.
 * </p>
 *
 * <p>
 * Extended logging can be controlled via the system property {@code extended.logging}.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public abstract class LogCore {

    /**
     * The Log4j2 logger instance used for logging messages.
     */
    private final Logger logger;

    /**
     * The marker associated with log messages for categorization.
     */
    private final Marker marker;

    /**
     * Custom log level for step-based logs.
     */
    private static final Level STEP_LEVEL = Level.forName("STEP", 350);

    /**
     * Custom log level for validation-related logs.
     */
    private static final Level VALIDATION_LEVEL = Level.forName("VALIDATION", 350);

    /**
     * Custom log level for extended logs.
     */
    private static final Level EXTENDED_LEVEL = Level.forName("EXTENDED", 450);

    /**
     * Flag indicating whether extended logging is enabled, controlled via system properties.
     */
    private static Boolean EXTENDED_LOGGING;

    /**
     * Initializes the logger and marker for a specific logging category.
     *
     * @param loggerName The name of the logger.
     * @param markerName The name of the marker associated with this logger.
     */
    protected LogCore(String loggerName, String markerName) {
        this.logger = LogZeus.getLogger(loggerName);
        this.marker = LogZeus.registerMarker(markerName);
    }

    /**
     * Logs an informational message.
     *
     * @param message The log message.
     * @param args    Arguments to be formatted within the message.
     */
    protected void infoLog(String message, Object... args) {
        logger.info(marker, message, args);
    }

    /**
     * Logs a warning message.
     *
     * @param message The log message.
     * @param args    Arguments to be formatted within the message.
     */
    protected void warnLog(String message, Object... args) {
        logger.warn(marker, message, args);
    }

    /**
     * Logs an error message.
     *
     * @param message The log message.
     * @param args    Arguments to be formatted within the message.
     */
    protected void errorLog(String message, Object... args) {
        logger.error(marker, message, args);
    }

    /**
     * Logs a debug message.
     *
     * @param message The log message.
     * @param args    Arguments to be formatted within the message.
     */
    protected void debugLog(String message, Object... args) {
        logger.debug(marker, message, args);
    }

    /**
     * Logs a trace message, providing fine-grained debugging details.
     *
     * @param message The log message.
     * @param args    Arguments to be formatted within the message.
     */
    protected void traceLog(String message, Object... args) {
        logger.trace(marker, message, args);
    }

    /**
     * Logs a step-based message, commonly used for tracking test steps.
     *
     * @param message The log message.
     * @param args    Arguments to be formatted within the message.
     */
    protected void stepLog(String message, Object... args) {
        logger.log(STEP_LEVEL, marker, message, args);
    }

    /**
     * Logs a validation-related message.
     *
     * @param message The log message.
     * @param args    Arguments to be formatted within the message.
     */
    protected void validationLog(String message, Object... args) {
        logger.log(VALIDATION_LEVEL, marker, message, args);
    }

    /**
     * Logs an extended message if extended logging is enabled.
     * <p>
     * Extended logging is controlled via the system property {@code extended.logging}.
     * </p>
     *
     * @param message The log message.
     * @param args    Arguments to be formatted within the message.
     */
    protected void extendedLog(String message, Object... args) {
        if (extendedLoggingEnabled()) {
            logger.log(EXTENDED_LEVEL, marker, message, args);
        }
    }

    /**
     * Determines whether extended logging is enabled based on the system property {@code extended.logging}.
     *
     * @return {@code true} if extended logging is enabled, otherwise {@code false}.
     */
    private boolean extendedLoggingEnabled() {
        if (EXTENDED_LOGGING == null) {
            EXTENDED_LOGGING = Boolean.parseBoolean(System.getProperty("extended.logging", "false"));
        }
        return EXTENDED_LOGGING;
    }

}