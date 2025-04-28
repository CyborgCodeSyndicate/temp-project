package com.theairebellion.zeus.ui.components.table.exceptions;

public class TableException extends RuntimeException {

    /**
     * Constructs a new TableDefinitionException with the specified detail message.
     *
     * @param message the detail message.
     */
    public TableException(String message) {
        super(message);
    }

    /**
     * Constructs a new TableDefinitionException with the specified detail message and cause.
     *
     * @param message the detail message.
     * @param cause   the cause (can be retrieved later by the {@link Throwable#getCause()} method).
     */
    public TableException(String message, Throwable cause) {
        super(message, cause);
    }
}
