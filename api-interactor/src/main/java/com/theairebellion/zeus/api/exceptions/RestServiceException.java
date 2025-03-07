package com.theairebellion.zeus.api.exceptions;

/**
 * Exception thrown when an error occurs during the execution of a REST service request.
 * <p>
 * This exception is used to wrap underlying errors encountered while making API calls,
 * providing additional context about the failure.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class RestServiceException extends RuntimeException {

    /**
     * Constructs a new {@code RestServiceException} with the specified message and cause.
     *
     * @param message   The detailed error message.
     * @param throwable The root cause of the exception.
     */
    public RestServiceException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
