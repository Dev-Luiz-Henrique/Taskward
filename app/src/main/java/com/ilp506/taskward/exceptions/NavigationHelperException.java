package com.ilp506.taskward.exceptions;

public class NavigationHelperException extends RuntimeException {
    public NavigationHelperException(String message) {
        super(message);
    }

    public NavigationHelperException(String message, Throwable cause) {
        super(message, cause);
    }
}
