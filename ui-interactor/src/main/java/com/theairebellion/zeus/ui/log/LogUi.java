package com.theairebellion.zeus.ui.log;

import com.theairebellion.zeus.logging.LogCore;

/**
 * Provides logging utilities for UI-related operations.
 *
 * <p>This class serves as the centralized logger for UI interactions, offering
 * multiple log levels such as informational, warnings, debugging, validation, and step-based logging.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public final class LogUi extends LogCore {

   private static LogUi INSTANCE;

   private LogUi() {
      super("Zeus.UI", "UI");
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
    * Logs a step message to track UI flow.
    *
    * @param message The step description.
    * @param args    Optional arguments to format the message.
    */
   public static void step(String message, Object... args) {
      getInstance().stepLog(message, args);
   }

   /**
    * Logs a validation message for UI assertions.
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
    * Retrieves the singleton instance of {@code LogUI}.
    *
    * @return The singleton instance of {@code LogUI}.
    */
   private static LogUi getInstance() {
      if (INSTANCE == null) {
         INSTANCE = new LogUi();
      }
      return INSTANCE;
   }

}
