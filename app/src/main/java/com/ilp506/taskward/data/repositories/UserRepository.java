package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ilp506.taskward.data.DatabaseContract.UserTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.utils.DateUtils;
import com.ilp506.taskward.utils.Logger;

public class UserRepository {

    private static final String TAG = UserRepository.class.getSimpleName();
    private final DatabaseHelper dbHelper;

    public UserRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    private User mapCursorToUser(Cursor cursor) {
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
        }
        return user;
    }

    public void createUser(User user) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {

            ContentValues values = new ContentValues();
            values.put(UserTable.COLUMN_NAME, user.getName());
            values.put(UserTable.COLUMN_PHOTO, user.getPhoto());
            db.insertOrThrow(UserTable.TABLE_NAME, null, values);

        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during user creation: " + e.getMessage(), e);
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }

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
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }


    public User updateUser(User user) {
        // TODO: Implementar a logica de atualizacao do usuario
        return null;
    }

    public void deleteUser(int userId) {
        // TODO: Implementar a logica de remocao do usuario
    }

    public User updateUserPoints(int userId, int points) {
        // TODO: Implementar a logica de atualizacao de pontos do usuario
        return null;
    }
}
