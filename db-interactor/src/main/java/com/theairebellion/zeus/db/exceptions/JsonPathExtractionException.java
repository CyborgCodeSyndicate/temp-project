package com.theairebellion.zeus.db.exceptions;

public class JsonPathExtractionException extends RuntimeException {
    public JsonPathExtractionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonPathExtractionException(String message) {
        super(message);
    }
}
