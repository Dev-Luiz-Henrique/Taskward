package com.ilp506.taskward.exceptions.codes;

import com.ilp506.taskward.exceptions.custom.NavigationHelperException;

/**
 * Enum to centralize error codes and descriptions for {@link NavigationHelperException}.
 * This enum helps in organizing and managing the error codes and their corresponding
 * messages related to navigation operations.
 */
public enum NavigationErrorCode {

    INVALID_DESTINATION("NAV001", "The specified navigation destination is invalid."),

    MISSING_ARGUMENTS("NAV002", "Required arguments for the navigation are missing."),

    NAVIGATION_FAILURE("NAV003", "General navigation failure."),

    UNEXPECTED_ERROR("NAV999", "Unexpected navigation error.");

    /**
     * The unique error code associated with the specific navigation error.
     */
    private final String code;

    /**
     * The error message that describes the specific navigation error.
     */
    private final String message;

    /**
     * Constructor to initialize the enum constants with their respective code and message.
     *
     * @param code The unique error code associated with the navigation error.
     * @param message The descriptive message for the error.
     */
    NavigationErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets the error code.
     *
     * @return The error code associated with the navigation error.
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the error message.
     *
     * @return The error message associated with the navigation error.
     */
    public String getMessage() {
        return message;
    }
}
