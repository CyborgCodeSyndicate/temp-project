package com.theairebellion.zeus.framework.exceptions;

/**
 * Exception thrown when a service fails to initialize.
 *
 * <p>This runtime exception indicates that a required service component could not be
 * created or configured correctly, preventing the application or test framework from
 * proceeding.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class ServiceInitializationException extends RuntimeException {

   /**
    * Constructs a new {@code ServiceInitializationException} with the specified detail message
    * and cause.
    *
    * @param message the detail message explaining why the service failed to initialize
    * @param cause   the underlying exception that triggered the initialization failure
    */
   public ServiceInitializationException(String message, Throwable cause) {
      super(message, cause);
   }
}