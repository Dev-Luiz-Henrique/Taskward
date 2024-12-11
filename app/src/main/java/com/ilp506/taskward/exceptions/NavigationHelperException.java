package com.ilp506.taskward.exceptions;

/**
 * Custom exception to represent errors related to navigation operations.
 *
 * This exception is used to signal issues that occur during navigation between
 * screens or fragments, such as invalid destinations, missing arguments, or
 * failures in navigation logic.
 */
public class NavigationHelperException extends RuntimeException {

    /**
     * Constructs a new NavigationHelperException with the specified detail message.
     *
     * @param message A description of the error that occurred.
     */
    public NavigationHelperException(String message) {
        super(message);
    }

    /**
     * Constructs a new NavigationHelperException with the specified detail message and cause.
     *
     * @param message A description of the error that occurred.
     * @param cause The underlying cause of the error (can be another exception).
     */
    public NavigationHelperException(String message, Throwable cause) {
        super(message, cause);
    }
}
