package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ilp506.taskward.data.DatabaseContract.UserTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class UserRepositoryTest {

    private UserRepository userRepository;
    private SQLiteDatabase mockDatabase;
    private Cursor mockCursor;

    @Before
    public void setUp() {
        Context mockContext = mock(Context.class);
        mockDatabase = mock(SQLiteDatabase.class);
        mockCursor = mock(Cursor.class);
        try (DatabaseHelper mockDatabaseHelper = mock(DatabaseHelper.class)) {

            when(mockDatabaseHelper.getReadableDatabase()).thenReturn(mockDatabase);
            when(mockDatabaseHelper.getWritableDatabase()).thenReturn(mockDatabase);
        }
        when(mockContext.getApplicationContext()).thenReturn(mockContext);

        userRepository = new UserRepository(mockContext);
    }


    @Test
    public void mapCursorToUser_whenCursorHasValidData_shouldReturnUserObject() {
        when(mockCursor.getInt(mockCursor.getColumnIndexOrThrow(UserTable.COLUMN_ID))).thenReturn(1);
        when(mockCursor.getString(mockCursor.getColumnIndexOrThrow(UserTable.COLUMN_NAME))).thenReturn("John Doe");
        when(mockCursor.getString(mockCursor.getColumnIndexOrThrow(UserTable.COLUMN_PHOTO))).thenReturn("photo_url");
        when(mockCursor.getInt(mockCursor.getColumnIndexOrThrow(UserTable.COLUMN_POINTS))).thenReturn(100);
        when(mockCursor.getString(mockCursor.getColumnIndexOrThrow(UserTable.COLUMN_CREATED_AT))).thenReturn("2024-11-27 12:00:00");

        User user = userRepository.mapCursorToUser(mockCursor);

        assertEquals(1, user.getId());
        assertEquals("John Doe", user.getName());
        assertEquals("photo_url", user.getPhoto());
        assertEquals(100, user.getPoints());
    }

    @Test
    public void mapCursorToUser_whenCursorHasNullValues_shouldReturnUserWithNullFields() {
        when(mockCursor.getInt(mockCursor.getColumnIndexOrThrow(UserTable.COLUMN_ID))).thenReturn(1);
        when(mockCursor.getString(mockCursor.getColumnIndexOrThrow(UserTable.COLUMN_NAME))).thenReturn(null);
        when(mockCursor.getString(mockCursor.getColumnIndexOrThrow(UserTable.COLUMN_PHOTO))).thenReturn(null);

        User user = userRepository.mapCursorToUser(mockCursor);

        assertEquals(1, user.getId());
        assertNull(user.getName());
        assertNull(user.getPhoto());
    }

    @Test
    public void createUser_whenValidUser_shouldInsertIntoDatabase() {
        User user = new User();
        user.setName("John Doe");
        user.setPhoto("/path/to/valid/photo");

        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_NAME, user.getName());
        values.put(UserTable.COLUMN_PHOTO, user.getPhoto());

        userRepository.createUser(user);

        verify(mockDatabase).insertOrThrow(UserTable.TABLE_NAME, null, values);
    }

    @Test
    public void updateUser_whenUserIsValid_shouldUpdateInDatabase() {
        User user = new User();
        user.setId(1);
        user.setName("John Doe Updated");
        user.setPhoto("/updated/path/to/valid/photo");
        user.setPoints(200);

        ContentValues values = new ContentValues();
        values.put(UserTable.COLUMN_NAME, user.getName());
        values.put(UserTable.COLUMN_PHOTO, user.getPhoto());
        values.put(UserTable.COLUMN_POINTS, user.getPoints());

        userRepository.updateUser(user);

        verify(mockDatabase).update(
                eq(UserTable.TABLE_NAME),
                eq(values),
                eq(UserTable.COLUMN_ID + " = ?"),
                eq(new String[]{"1"})
        );
    }

    @Test
    public void getUserById_whenUserDoesNotExist_shouldReturnNull() {
        when(mockDatabase.query(anyString(), any(), any(), any(), any(), any(), any())).thenReturn(mockCursor);
        when(mockCursor.moveToFirst()).thenReturn(false);

        User user = userRepository.getUserById(1);
        assertNull(user);
    }

    @Test(expected = DatabaseOperationException.class)
    public void getUserById_whenDatabaseErrorOccurs_shouldThrowDatabaseOperationException() {
        when(mockDatabase.query(anyString(), any(), any(), any(), any(), any(), any())).thenThrow(new SQLiteException());

        userRepository.getUserById(1);
    }

    @Test(expected = DatabaseOperationException.class)
    public void createUser_whenDatabaseErrorOccurs_shouldThrowDatabaseOperationException() {
        User user = new User();
        user.setName("John Doe");
        user.setPhoto("/path/to/valid/photo");

        doThrow(new SQLiteException()).when(mockDatabase).insertOrThrow(anyString(), any(), any());

        userRepository.createUser(user);
    }
}
