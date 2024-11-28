package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ilp506.taskward.data.DatabaseContract.UserTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.exceptions.DatabaseOperationException;
import com.ilp506.taskward.utils.DateUtils;
import com.ilp506.taskward.utils.Logger;

/**
 * Repository class responsible for managing database operations related to the User model.
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
     */
    protected User mapCursorToUser(Cursor cursor) {
        User user = new User();
        try {
            user.setId(cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_ID)));
            user.setName(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_NAME)));
            user.setPhoto(cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_PHOTO)));
            user.setPoints(cursor.getInt(cursor.getColumnIndexOrThrow(UserTable.COLUMN_POINTS)));

            String createdAtString = cursor.getString(cursor.getColumnIndexOrThrow(UserTable.COLUMN_CREATED_AT));
            user.setCreatedAt(DateUtils.parseTimestamp(createdAtString));
        } catch (Exception e) {
            Logger.e(TAG, "Error mapping cursor to User: " + e.getMessage(), e);
            throw new RuntimeException("Error mapping cursor to User: " + e.getMessage(), e);
        }
        return user;
    }

    /**
     * Creates a new user in the database.
     *
     * @param user The User instance to be created.
     * @return The created User instance.
     */
    public User createUser(User user) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {

            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_NAME, user.getName());
            values.put(UserTable.COLUMN_PHOTO, user.getPhoto());

            long newId = db.insertOrThrow(UserTable.TABLE_NAME, null, values);
            if (newId == -1) {
                Logger.e(TAG, "Failed to insert new user");
                throw new DatabaseOperationException("Failed to insert new user");
            }

            return getUserById((int) newId);
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during user creation: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a user from the database by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The User instance if found, or null if not found.
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
                return null;
            }
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during getUserById: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }


    /**
     * Updates an existing user's details in the database.
     *
     * @param user The User instance containing updated data.
     * @return The updated User instance.
     */
    public User updateUser(User user) {
        final String selection = UserTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(user.getId())};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_NAME, user.getName());
            values.put(UserTable.COLUMN_PHOTO, user.getPhoto());
            values.put(UserTable.COLUMN_POINTS, user.getPoints());

            int rowsUpdated = db.update(UserTable.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0)
                throw new DatabaseOperationException("No rows updated. User not found.");

            return getUserById(user.getId());
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during user update: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a user from the database by their ID.
     *
     * @param userId The ID of the user to delete.
     */
    public void deleteUser(int userId) {
        final String selection = UserTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(userId)};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete(UserTable.TABLE_NAME, selection, selectionArgs);

        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during user deletion: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Updates the points of a user in the database.
     *
     * @param userId The ID of the user to update.
     * @param points The new points value to set.
     * @return The updated User instance.
     */
    public User updateUserPoints(int userId, int points) {
        final String selection = UserTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(userId)};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_POINTS, points);

            int rowsUpdated = db.update(UserTable.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0)
                throw new DatabaseOperationException("No rows updated. User not found.");

            return getUserById(userId);
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during updateUserPoints: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }
}
