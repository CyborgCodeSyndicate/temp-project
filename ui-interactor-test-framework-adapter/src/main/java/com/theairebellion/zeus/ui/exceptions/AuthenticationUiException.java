package com.theairebellion.zeus.ui.exceptions;

/**
 * Exception thrown when UI authentication fails.
 *
 * <p>This exception indicates that an error occurred while attempting to authenticate
 * via the UI, such as missing credentials or an unexpected login failure.
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class AuthenticationUiException extends RuntimeException {

   /**
    * Constructs a new {@code AuthenticationUiException} with the specified detail message and cause.
    *
    * <p>The message provides details about the authentication failure, and the cause allows
    * inspection of the underlying exception that triggered this error.
    *
    * @param message the detail message
    * @param cause   the root cause of the authentication failure
    */
   public AuthenticationUiException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructs a new {@code AuthenticationUiException} with a default message and the specified cause.
    *
    * <p>Use this constructor when no custom message is needed; a standard message will be used instead.
    *
    * @param cause the root cause of the authentication failure
    */
   public AuthenticationUiException(Throwable cause) {
      super("Failed to authenticate via UI", cause);
   }
}