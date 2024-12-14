package com.ilp506.taskward.exceptions.codes;

import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;

/**
 * Enum to centralize error codes and descriptions for {@link DatabaseOperationException}.
 * This enum helps in organizing and managing the error codes and their corresponding
 * messages related to database operations.
 */
public enum DatabaseErrorCode {

    QUERY_FAILURE("DB001", "Failed to execute the database query."),

    CONNECTION_FAILURE("DB002", "Database connection failure."),

    DATA_INTEGRITY_VIOLATION("DB003", "Data integrity violation."),

    UNEXPECTED_ERROR("DB999", "Unexpected database error.");

    /**
     * The unique error code associated with the specific database error.
     */
    private final String code;

    /**
     * The error message that describes the specific database error.
     */
    private final String message;

    /**
     * Constructor to initialize the enum constants with their respective code and message.
     *
     * @param code The unique error code associated with the database error.
     * @param message The descriptive message for the error.
     */
    DatabaseErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * Gets the error code.
     *
     * @return The error code associated with the database error.
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets the error message.
     *
     * @return The error message associated with the database error.
     */
    public String getMessage() {
        return message;
    }
}
