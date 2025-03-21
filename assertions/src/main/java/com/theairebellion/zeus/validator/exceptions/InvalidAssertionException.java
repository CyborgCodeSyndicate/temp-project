package com.theairebellion.zeus.validator.exceptions;

/**
 * Exception thrown when an invalid assertion is encountered.
 * <p>
 * This exception is used to indicate that an assertion has been improperly
 * configured, such as having a missing key, target, type, or expected value.
 * It extends {@link IllegalArgumentException} to provide a clear error message
 * about the issue.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class InvalidAssertionException extends IllegalArgumentException {

    /**
     * Constructs a new {@code InvalidAssertionException} with the specified error message.
     *
     * @param message The detail message describing the assertion issue.
     */
    public InvalidAssertionException(String message) {
        super(message);
    }

}
