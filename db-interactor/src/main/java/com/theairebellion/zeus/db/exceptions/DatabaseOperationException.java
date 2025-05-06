package com.theairebellion.zeus.db.exceptions;

/**
 * Exception representing database operation failures.
 *
 * <p>This exception is thrown when a database query or connection operation
 * encounters an error, such as an invalid query, connection failure, or
 * unexpected database response.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class DatabaseOperationException extends RuntimeException {

   /**
    * Constructs a new {@code DatabaseOperationException} with the specified message and cause.
    *
    * @param message The detail message explaining the reason for the exception.
    * @param cause   The underlying cause of the exception.
    */
   public DatabaseOperationException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructs a new {@code DatabaseOperationException} with the specified message.
    *
    * @param message The detail message explaining the reason for the exception.
    */
   public DatabaseOperationException(String message) {
      super(message);
   }

}
