package com.theairebellion.zeus.framework.exceptions;

/**
 * Exception thrown when a hook (e.g., {@code DbHook}, {@code ApiHook}) fails during execution.
 *
 * <p>This exception wraps any underlying errors encountered while looking up or invoking
 * hook flows, providing a consistent runtime exception type for hook failures.</p>
 *
 * @author Cyborg Code Syndicate 💍👨💻
 */
public class HookExecutionException extends RuntimeException {

   /**
    * Constructs a new {@code HookExecutionException} with the specified detail message
    * and cause.
    *
    * @param message the detail message explaining the context of the failure
    * @param cause   the underlying exception that caused the hook execution to fail
    */
   public HookExecutionException(String message, Throwable cause) {
      super(message, cause);
   }

   /**
    * Constructs a new {@code HookExecutionException} with a default message and the specified cause.
    *
    * @param cause the underlying exception that caused the hook execution to fail
    */
   public HookExecutionException(Throwable cause) {
      super("Failed to execute hook", cause);
   }
}