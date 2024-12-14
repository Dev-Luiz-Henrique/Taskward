package com.ilp506.taskward.exceptions.handlers;

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.ilp506.taskward.exceptions.codes.DatabaseErrorCode;
import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;

/**
 * Handles SQLite exceptions on Android, providing detailed error codes and logging.
 */
public class DatabaseErrorHandler {

    private static final String TAG = DatabaseErrorHandler.class.getSimpleName();

    /**
     * Maps SQLite exceptions to DatabaseOperationException with appropriate error codes.
     *
     * @param e The SQLite exception to handle.
     * @param operationDetails Additional context or details about the failed operation.
     * @return A DatabaseOperationException with a relevant error code and message.
     */
    @NonNull
    public static DatabaseOperationException handleSQLiteException(@NonNull SQLiteException e,
                                                                   @NonNull String operationDetails) {
        Log.e(TAG, "SQLiteException occurred during operation: " + operationDetails, e);

        if (e instanceof SQLiteConstraintException)
            return handleConstraintException((SQLiteConstraintException) e, operationDetails);
        else if (e instanceof SQLiteDatabaseLockedException) {
            return createDatabaseOperationException(
                    DatabaseErrorCode.CONNECTION_FAILURE,
                    "Database is locked. Retry or check concurrent operations. " + operationDetails,
                    e
            );
        }

        String errorMessage = e.getMessage();
        if (errorMessage != null) {
            if (errorMessage.contains("syntax error")) {
                return createDatabaseOperationException(
                        DatabaseErrorCode.INVALID_QUERY_SYNTAX,
                        "Syntax error in SQL query. " + operationDetails,
                        e
                );
            } else if (errorMessage.contains("no such column")) {
                return createDatabaseOperationException(
                        DatabaseErrorCode.SCHEMA_MISMATCH,
                        "No such column in schema. " + operationDetails,
                        e
                );
            } else if (errorMessage.contains("file is encrypted")) {
                return createDatabaseOperationException(
                        DatabaseErrorCode.ENCRYPTION_ERROR,
                        "Database encryption error. Verify encryption configuration. " + operationDetails,
                        e
                );
            }
        }

        return createDatabaseOperationException(
                DatabaseErrorCode.UNEXPECTED_ERROR,
                "An unexpected database error occurred. " + operationDetails,
                e
        );
    }

    /**
     * Handles SQLiteConstraintException specifically.
     *
     * @param e The exception instance.
     * @param operationDetails Details about the operation.
     * @return A DatabaseOperationException.
     */
    @NonNull
    private static DatabaseOperationException handleConstraintException(
            @NonNull SQLiteConstraintException e,
            @NonNull String operationDetails
    ) {
        String errorMessage = e.getMessage();
        if (errorMessage != null && errorMessage.contains("UNIQUE")) {
            return createDatabaseOperationException(
                    DatabaseErrorCode.DUPLICATE_RESOURCE,
                    "Unique constraint violated. " + operationDetails,
                    e
            );
        }
        return createDatabaseOperationException(
                DatabaseErrorCode.DATA_INTEGRITY_VIOLATION,
                "Constraint violation detected. " + operationDetails,
                e
        );
    }

    /**
     * Utility method to create a DatabaseOperationException.
     *
     * @param errorCode The database error code.
     * @param message The error message.
     * @param cause The root exception.
     * @return A DatabaseOperationException instance.
     */
    @NonNull
    private static DatabaseOperationException createDatabaseOperationException(@NonNull DatabaseErrorCode errorCode,
                                                                               @NonNull String message,
                                                                               @NonNull Throwable cause) {
        return DatabaseOperationException.fromError(errorCode, message, cause);
    }
}
