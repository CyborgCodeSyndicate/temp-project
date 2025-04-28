package com.theairebellion.zeus.framework.exceptions;

public class ServiceInitializationException extends RuntimeException {
    public ServiceInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}