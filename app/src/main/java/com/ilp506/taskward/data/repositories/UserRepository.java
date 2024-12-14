package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;

import com.ilp506.taskward.data.DatabaseContract.UserTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.exceptions.codes.DatabaseErrorCode;
import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;
import com.ilp506.taskward.exceptions.handlers.DatabaseErrorHandler;
import com.ilp506.taskward.utils.DateUtils;
import com.ilp506.taskward.utils.Logger;

/**
 * Repository class responsible for managing database operations related to the User model.
 * This class performs CRUD operations for the User model in the local SQLite database.
 */
public class UserRepository {
    private static final String TAG = UserRepository.class.getSimpleName();

    private final DatabaseHelper dbHelper;

    /**
     * Constructs a UserRepository with a database helper instance.
     *
     * @param context The application context used to initialize the database helper.
     */
    public UserRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Maps the data from a Cursor object to a User instance.
     *
     * @param cursor The cursor containing the queried data.
     * @return A User instance populated with the cursor's data.
     * @throws RuntimeException If an error occurs during cursor mapping, such as missing or incorrect data format.
     */
    protected User mapCursorToUser(Cursor cursor) {
        User user = new User();
        try {
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_NAME)));
            user.setEmail(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_EMAIL)));
            user.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_PHOTO)));
            user.setPoints(cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_POINTS)));
            user.setCreatedAt(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_CREATED_AT))
            ));
        } catch (Exception e) {
            Logger.e(TAG, "Error mapping cursor to User: " + e.getMessage(), e);
            throw new RuntimeException("Error mapping cursor to User: " + e.getMessage(), e);
        }
        return user;
    }

    /**
     * Inserts a new user into the database.
     *
     * @param user The User instance to be created.
     * @return The created User instance.
     * @throws DatabaseOperationException If an error occurs during the database operation, such as an insertion failure.
     */
    public User createUser(@NonNull User user) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_NAME, user.getName());
            values.put(UserTable.COLUMN_EMAIL, user.getEmail());
            values.put(UserTable.COLUMN_PHOTO, user.getPhoto());

            long newId = db.insertOrThrow(UserTable.TABLE_NAME, null, values);
            if (newId == -1) {
                Logger.e(TAG, "Failed to insert new user");
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.QUERY_FAILURE,
                        "Failed to insert user into the database."
                );
            }
            return getUserById((int) newId);
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e, "Error during user creation.");
        }
    }

    /**
     * Retrieves a user by their ID from the database.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User instance if found.
     * @throws DatabaseOperationException If an error occurs during the database operation or if the user is not found.
     */
    public User getUserById(int userId) {
        final String[] columns = UserTable.ALL_COLUMNS;
        final String selection = UserTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(userId)};

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     UserTable.TABLE_NAME,
                     columns,
                     selection,
                     selectionArgs,
                     null,
                     null,
                     null
             )) {

            if (cursor.moveToFirst())
                return mapCursorToUser(cursor);
            else {
                Logger.w(TAG, "User not found with ID: " + userId);
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.QUERY_FAILURE,
                        String.format("User with ID %d not found.", userId)
                );
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error occurred while retrieving user with ID %d.", userId)
            );
        }
    }

    /**
     * Updates an existing user's details in the database.
     *
     * @param user The User instance containing updated data.
     * @return The updated User instance.
     * @throws DatabaseOperationException If an error occurs during the database operation or if the user is not found.
     */
    public User updateUser(@NonNull User user) {
        final String selection = UserTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(user.getId())};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_NAME, user.getName());
            values.put(UserTable.COLUMN_EMAIL, user.getEmail());
            values.put(UserTable.COLUMN_PHOTO, user.getPhoto());
            values.put(UserTable.COLUMN_POINTS, user.getPoints());

            int rowsUpdated = db.update(UserTable.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.DATA_INTEGRITY_VIOLATION,
                        "No rows updated. User not found."
                );
            }
            return getUserById(user.getId());
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error during user update for ID %d.", user.getId())
            );
        }
    }

    /**
     * Deletes a user by their ID from the database.
     *
     * @param userId The ID of the user to delete.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public void deleteUser(int userId) {
        final String selection = UserTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(userId)};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            int rowsDeleted = db.delete(UserTable.TABLE_NAME, selection, selectionArgs);
            if (rowsDeleted == 0) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.QUERY_FAILURE,
                        String.format("Failed to delete user with ID %d. User not found.", userId)
                );
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error during user deletion for ID %d.", userId)
            );
        }
    }
}
