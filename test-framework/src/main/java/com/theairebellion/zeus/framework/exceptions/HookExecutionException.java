package com.theairebellion.zeus.framework.exceptions;

public class HookExecutionException extends RuntimeException {

   public HookExecutionException(String message, Throwable cause) {
      super(message, cause);
   }

   public HookExecutionException(Throwable cause) {
      super("Failed to execute hook", cause);
   }
}