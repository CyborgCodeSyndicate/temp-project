package com.theairebellion.zeus.db.exceptions;

/**
 * Exception representing failures during JSONPath extraction.
 * <p>
 * This exception is thrown when an error occurs while extracting data
 * using JSONPath expressions from a database query response.
 * </p>
 *
 * @author Cyborg Code Syndicate
 */
public class JsonPathExtractionException extends RuntimeException {

    /**
     * Constructs a new {@code JsonPathExtractionException} with the specified message and cause.
     *
     * @param message The detail message explaining the reason for the exception.
     * @param cause   The underlying cause of the exception.
     */
    public JsonPathExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs a new {@code JsonPathExtractionException} with the specified message.
     *
     * @param message The detail message explaining the reason for the exception.
     */
    public JsonPathExtractionException(String message) {
        super(message);
    }
}
