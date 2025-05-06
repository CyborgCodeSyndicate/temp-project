package com.theairebellion.zeus.ui.selenium.exceptions;

/**
 * A generic unchecked exception for Selenium-based UI interaction errors.
 * Use this to wrap any unexpected behavior during UI automation or component actions.
 *
 * <p>Example use cases:
 * - Element not found or not interactable
 * - Unexpected page state
 * - Component logic failure
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class UiInteractionException extends RuntimeException {

   /**
    * Constructs a new UiInteractionException with the specified detail message.
    *
    * @param message the detail message.
    */
   public UiInteractionException(String message) {
      super(message);
   }

   /**
    * Constructs a new UiInteractionException with the specified detail message and cause.
    *
    * @param message the detail message.
    * @param cause   the cause of the exception.
    */
   public UiInteractionException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructs a new UiInteractionException with the specified cause.
    *
    * @param cause the cause of the exception.
    */
   public UiInteractionException(Throwable cause) {
      super(cause);
   }
}