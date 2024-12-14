package com.ilp506.taskward.exceptions.custom;

import androidx.annotation.NonNull;

import com.ilp506.taskward.exceptions.codes.DatabaseErrorCode;

/**
 * Custom exception to represent database-related errors.
 * This exception is used to signal issues during database
 * operations, such as query failures, connection issues, or
 * unexpected behavior involving the database.
 */
public class DatabaseOperationException extends RuntimeException {

    private final String errorCode;
    private final String details;

    /**
     * Constructs a new DatabaseOperationException with an error code and message.
     *
     * @param errorCode A unique identifier for the error type.
     * @param message   A description of the error that occurred.
     */
    public DatabaseOperationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * Constructs a new DatabaseOperationException with an error code, message, and cause.
     *
     * @param errorCode A unique identifier for the error type.
     * @param message   A description of the error that occurred.
     * @param cause     The underlying cause of the error (can be another exception).
     */
    public DatabaseOperationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * Constructs a new DatabaseOperationException with an error code, message, details, and cause.
     *
     * @param errorCode A unique identifier for the error type.
     * @param message   A description of the error that occurred.
     * @param details   Additional context or information about the error.
     * @param cause     The underlying cause of the error (can be another exception).
     */
    public DatabaseOperationException(String errorCode, String message, String details, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * Factory method to create a DatabaseOperationException from a DatabaseErrorCode and details.
     *
     * @param errorCode The error code associated with the database error.
     * @param details   Additional context or information about the error.
     * @return A new instance of DatabaseOperationException.
     */
    public static DatabaseOperationException fromError(@NonNull DatabaseErrorCode errorCode, String details) {
        return new DatabaseOperationException(
                errorCode.getCode(),
                errorCode.getMessage(),
                details,
                null
        );
    }

    /**
     * Factory method to create a DatabaseOperationException from a DatabaseErrorCode, details, and a cause.
     *
     * @param errorCode The error code associated with the database error.
     * @param details   Additional context or information about the error.
     * @param cause     The underlying cause of the error (can be another exception).
     * @return A new instance of DatabaseOperationException.
     */
    @NonNull
    public static DatabaseOperationException fromError(@NonNull DatabaseErrorCode errorCode, String details, Throwable cause) {
        return new DatabaseOperationException(
                errorCode.getCode(),
                errorCode.getMessage(),
                details,
                cause
        );
    }

    /**
     * Gets the error code associated with this exception.
     *
     * @return The error code.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Gets the additional details associated with this exception.
     *
     * @return Additional details about the error.
     */
    public String getDetails() {
        return details;
    }

    /**
     * Returns a string representation of the DatabaseOperationException.
     *
     * @return A string containing the error code, message, and details.
     */
    @NonNull
    @Override
    public String toString() {
        return String.format(
                "DatabaseOperationException{errorCode='%s', message='%s', details='%s'}",
                errorCode, getMessage(), details
        );
    }
}
