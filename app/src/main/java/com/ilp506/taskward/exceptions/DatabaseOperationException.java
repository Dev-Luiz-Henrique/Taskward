package com.ilp506.taskward.exceptions;

/**
 * Custom exception to represent database-related errors.
 * This exception is used to signal issues that occur during database
 * operations, such as query failures, connection issues, or any other
 * unexpected behavior involving the database.
 */
public class DatabaseOperationException extends RuntimeException {

    /**
     * Constructs a new DatabaseOperationException with the specified detail message.
     *
     * @param message A description of the error that occurred.
     */
    public DatabaseOperationException(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseOperationException with the specified detail message and cause.
     *
     * @param message A description of the error that occurred.
     * @param cause The underlying cause of the error (can be another exception).
     */
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
