package com.theairebellion.zeus.framework.exceptions;

/**
 * Exception thrown when static test data cannot be initialized.
 *
 * <p>This runtime exception indicates a failure during the setup of static data
 * required for tests, such as loading fixtures or pre-defined datasets.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class StaticTestDataInitializationException extends RuntimeException {

   /**
    * Constructs a new {@code StaticTestDataInitializationException} with the specified detail message
    * and cause.
    *
    * @param message the detail message explaining the context of the initialization failure
    * @param cause   the underlying exception that caused the failure
    */
   public StaticTestDataInitializationException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructs a new {@code StaticTestDataInitializationException} with a default message
    * and the specified cause.
    *
    * @param cause the underlying exception that caused the failure
    */
   public StaticTestDataInitializationException(Throwable cause) {
      super("Failed to initialize static test data", cause);
   }
}