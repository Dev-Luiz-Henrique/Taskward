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
    RESOURCE_NOT_FOUND("DB004", "Requested resource not found in the database."),
    DUPLICATE_RESOURCE("DB005", "Duplicate resource error."),
    INVALID_QUERY_SYNTAX("DB006", "Invalid query syntax."),
    PERMISSION_DENIED("DB007", "Permission denied for database operation."),
    TRANSACTION_FAILURE("DB008", "Transaction failure."),
    CONNECTION_TIMEOUT("DB009", "Database connection timeout."),
    SCHEMA_MISMATCH("DB010", "Database schema mismatch."),
    OPERATION_NOT_SUPPORTED("DB011", "Operation not supported by the database."),
    ENCRYPTION_ERROR("DB012", "Database encryption error."),
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
