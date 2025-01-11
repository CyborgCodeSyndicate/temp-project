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
    private static final boolean EXTENDED_LOGGING =
        Boolean.parseBoolean(System.getProperty("zeus.extendedLogging", "false"));


    protected LogCore(String loggerName, String markerName) {
        this.logger = LogZeus.getLogger(loggerName);
        this.marker = LogZeus.registerMarker(markerName);
    }


    protected void infoLog(String message, Object... args) {
        logger.info(marker, message, args);
    }


    protected void warnLog(String message, Object... args) {
        logger.warn(marker, message, args);
    }


    protected void errorLog(String message, Object... args) {
        logger.error(marker, message, args);
    }


    protected void debugLog(String message, Object... args) {
        logger.debug(marker, message, args);
    }


    protected void traceLog(String message, Object... args) {
        logger.trace(marker, message, args);
    }


    protected void stepLog(String message, Object... args) {
        logger.log(STEP_LEVEL, marker, message, args);
    }


    protected void validationLog(String message, Object... args) {
        logger.log(VALIDATION_LEVEL, marker, message, args);
    }


    protected void extendedLog(String message, Object... args) {
        if (EXTENDED_LOGGING) {
            logger.log(EXTENDED_LEVEL, marker, message, args);
        }
    }

}