package com.ilp506.taskward.exceptions.handlers;

import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;

import com.ilp506.taskward.exceptions.codes.DatabaseErrorCode;
import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;

/**
 * This class handles database-related exceptions and maps them to appropriate DatabaseOperationException instances.
 */
public class DatabaseErrorHandler {

    /**
     * Maps SQLite exceptions to DatabaseOperationException with appropriate error codes.
     *
     * @param e The SQLite exception to handle.
     * @param operationDetails Additional context or details about the failed operation.
     * @return A DatabaseOperationException with a relevant error code and message.
     */
    @NonNull
    public static DatabaseOperationException handleSQLiteException(SQLiteException e, String operationDetails) {
        if (e instanceof SQLiteConstraintException) {
            return DatabaseOperationException.fromError(
                    DatabaseErrorCode.DATA_INTEGRITY_VIOLATION,
                    operationDetails,
                    e
            );
        } else if (e instanceof SQLiteDatabaseLockedException) {
            return DatabaseOperationException.fromError(
                    DatabaseErrorCode.CONNECTION_FAILURE,
                    "Database is locked. " + operationDetails,
                    e
            );
        } else {
            return DatabaseOperationException.fromError(
                    DatabaseErrorCode.UNEXPECTED_ERROR,
                    operationDetails,
                    e
            );
        }
    }
}
