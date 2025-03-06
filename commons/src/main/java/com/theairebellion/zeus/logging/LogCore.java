package com.theairebellion.zeus.logging;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;

public abstract class LogCore {

    private final Logger logger;
    private final Marker marker;

    private static final Level STEP_LEVEL = Level.forName("STEP", 350);
    private static final Level VALIDATION_LEVEL = Level.forName("VALIDATION", 350);
    private static final Level EXTENDED_LEVEL = Level.forName("EXTENDED", 450);
    private static final Boolean QUICK_EXECUTION = Boolean.parseBoolean(System.getProperty("quick.execution", "false"));
    private static Boolean EXTENDED_LOGGING;


    protected LogCore(String loggerName, String markerName) {
        this.logger = LogZeus.getLogger(loggerName);
        this.marker = LogZeus.registerMarker(markerName);
    }


    protected void infoLog(String message, Object... args) {
        logger.info(marker, message, args);
    }


    protected void warnLog(String message, Object... args) {
        if (!QUICK_EXECUTION) {
            logger.warn(marker, message, args);
        }
    }

    protected void errorLog(String message, Object... args) {
        if (!QUICK_EXECUTION) {
            logger.error(marker, message, args);
        }
    }

    protected void debugLog(String message, Object... args) {
        if (!QUICK_EXECUTION) {
            logger.debug(marker, message, args);
        }
    }

    protected void traceLog(String message, Object... args) {
        if (!QUICK_EXECUTION) {
            logger.trace(marker, message, args);
        }
    }

    protected void stepLog(String message, Object... args) {
        if (!QUICK_EXECUTION) {
            logger.log(STEP_LEVEL, marker, message, args);
        }
    }

    protected void validationLog(String message, Object... args) {
        if (!QUICK_EXECUTION) {
            logger.log(VALIDATION_LEVEL, marker, message, args);
        }
    }

    protected void extendedLog(String message, Object... args) {
        if (!QUICK_EXECUTION && extendedLoggingEnabled()) {
            logger.log(EXTENDED_LEVEL, marker, message, args);
        }
    }


    private boolean extendedLoggingEnabled() {
        if (EXTENDED_LOGGING == null) {
            EXTENDED_LOGGING = Boolean.parseBoolean(System.getProperty("extended.logging", "false"));
        }
        return EXTENDED_LOGGING;
    }


}