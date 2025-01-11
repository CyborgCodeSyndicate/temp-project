package com.theairebellion.zeus.api.exceptions;

public class RestServiceException extends RuntimeException {

    public RestServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
