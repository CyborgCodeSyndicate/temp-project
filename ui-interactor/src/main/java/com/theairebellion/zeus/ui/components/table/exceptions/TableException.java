package com.theairebellion.zeus.ui.components.table.exceptions;

/**
 * Exception thrown when a table component encounters an error while defining or rendering.
 *
 * <p>This exception is the base for all errors related to table configuration,
 * data binding, or UI rendering issues within the table component.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class TableException extends RuntimeException {

   /**
    * Constructs a new {@code TableException} with the specified detail message.
    *
    * @param message the detail message explaining the cause of the exception
    */
   public TableException(String message) {
      super(message);
   }

   /**
    * Constructs a new {@code TableException} with the specified detail message and cause.
    *
    * @param message the detail message explaining the cause of the exception
    * @param cause   the underlying cause (retrievable via {@link Throwable#getCause()})
    */
   public TableException(String message, Throwable cause) {
      super(message, cause);
   }
}
