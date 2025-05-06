package com.theairebellion.zeus.ui.exceptions;

public class AuthenticationUiException extends RuntimeException {

   public AuthenticationUiException(String message, Throwable cause) {
      super(message, cause);
   }

   public AuthenticationUiException(Throwable cause) {
      super("Failed to authenticate via UI", cause);
   }
}