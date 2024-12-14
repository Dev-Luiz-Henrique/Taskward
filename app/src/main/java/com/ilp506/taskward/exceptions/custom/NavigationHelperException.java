package com.ilp506.taskward.exceptions.custom;

import androidx.annotation.NonNull;

import com.ilp506.taskward.exceptions.codes.NavigationErrorCode;

/**
 * Custom exception to represent errors related to navigation operations.
 * This exception is used to signal issues that occur during navigation between
 * screens or fragments, such as invalid destinations, missing arguments, or
 * failures in navigation logic.
 */
public class NavigationHelperException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;
    private final String details;

    /**
     * Constructs a new NavigationHelperException with an error code and message.
     *
     * @param errorCode A unique identifier for the error type.
     * @param message   A description of the error that occurred.
     */
    public NavigationHelperException(@NonNull String errorCode, @NonNull String message) {
        super(message);
        this.errorCode = errorCode;
        this.details = null;
    }

    /**
     * Constructs a new NavigationHelperException with an error code, message, and details.
     *
     * @param errorCode A unique identifier for the error type.
     * @param message   A description of the error that occurred.
     * @param details   Additional context or information about the error.
     */
    public NavigationHelperException(@NonNull String errorCode, @NonNull String message, String details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * Constructs a new NavigationHelperException with an error code, message, details, and cause.
     *
     * @param errorCode A unique identifier for the error type.
     * @param message   A description of the error that occurred.
     * @param details   Additional context or information about the error.
     * @param cause     The underlying cause of the error (can be another exception).
     */
    public NavigationHelperException(@NonNull String errorCode, @NonNull String message, String details, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details;
    }

    /**
     * Factory method to create a NavigationHelperException from a NavigationErrorCode and details.
     *
     * @param errorCode The error code associated with the navigation error.
     * @param details   Additional context or information about the error.
     * @return A new instance of NavigationHelperException.
     */
    @NonNull
    public static NavigationHelperException fromError(@NonNull NavigationErrorCode errorCode, String details) {
        return new NavigationHelperException(
                errorCode.getCode(),
                errorCode.getMessage(),
                details
        );
    }

    /**
     * Factory method to create a NavigationHelperException from a NavigationErrorCode, details, and a cause.
     *
     * @param errorCode The error code associated with the navigation error.
     * @param details   Additional context or information about the error.
     * @param cause     The underlying cause of the error (can be another exception).
     * @return A new instance of NavigationHelperException.
     */
    @NonNull
    public static NavigationHelperException fromError(@NonNull NavigationErrorCode errorCode, String details, Throwable cause) {
        return new NavigationHelperException(
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
     * Returns a string representation of the NavigationHelperException.
     *
     * @return A string containing the error code, message, and details.
     */
    @NonNull
    @Override
    public String toString() {
        return String.format(
                "NavigationHelperException{errorCode='%s', message='%s', details='%s'}",
                errorCode, getMessage(), details
        );
    }
}
