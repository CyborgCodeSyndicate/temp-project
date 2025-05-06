package com.theairebellion.zeus.framework.exceptions;

public class StaticTestDataInitializationException extends RuntimeException {

   public StaticTestDataInitializationException(String message, Throwable cause) {
      super(message, cause);
   }

   public StaticTestDataInitializationException(Throwable cause) {
      super("Failed to initialize static test data", cause);
   }
}