package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;

import com.ilp506.taskward.data.DatabaseContract.TaskTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.enums.TaskFrequencyEnum;
import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.exceptions.codes.DatabaseErrorCode;
import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;
import com.ilp506.taskward.exceptions.handlers.DatabaseErrorHandler;
import com.ilp506.taskward.utils.DateUtils;
import com.ilp506.taskward.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing database operations related to the Task model.
 * This class performs CRUD operations for the Task model in the local SQLite database.
 */
public class TaskRepository {
    private static final String TAG = TaskRepository.class.getSimpleName();

    private final DatabaseHelper dbHelper;

    /**
     * Constructs a TaskRepository with a database helper instance.
     *
     * @param context The application context used to initialize the database helper.
     */
    public TaskRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Maps the data from a Cursor object to a Task instance.
     *
     * @param cursor The cursor containing the queried data.
     * @return A Task instance populated with the cursor's data.
     * @throws DatabaseOperationException If an error occurs during the mapping process.
     */
    protected Task mapCursorToTask(Cursor cursor) {
        Task task = new Task();
        try {
            task.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_ID)));
            task.setIcon(cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_ICON)));
            task.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_TITLE)));
            task.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_DESCRIPTION)));
            task.setFrequency(TaskFrequencyEnum.fromString(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_FREQUENCY)))
            );
            task.setFrequencyInterval(cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_FREQUENCY_INTERVAL)));
            task.setStartDate(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_START_DATE)))
            );
            task.setEndDate(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_END_DATE)))
            );
            task.setPointsReward(cursor.getInt(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_POINTS_REWARD)));
            task.setCreatedAt(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskTable.COLUMN_CREATED_AT)))
            );
        } catch (Exception e) {
            Logger.e(TAG, "Error mapping cursor to Task: " + e.getMessage(), e);
            throw DatabaseOperationException.fromError(
                    DatabaseErrorCode.UNEXPECTED_ERROR,
                    "Error mapping cursor to Task.",
                    e
            );
        }
        return task;
    }

    /**
     * Creates a new task in the database.
     *
     * @param task The Task instance to be created.
     * @return The created Task instance.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public Task createTask(@NonNull Task task) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(TaskTable.COLUMN_ICON, task.getIcon());
            values.put(TaskTable.COLUMN_TITLE, task.getTitle());
            values.put(TaskTable.COLUMN_DESCRIPTION, task.getDescription());
            values.put(TaskTable.COLUMN_FREQUENCY, task.getFrequency().getValue());
            values.put(TaskTable.COLUMN_FREQUENCY_INTERVAL, task.getFrequencyInterval());
            values.put(TaskTable.COLUMN_START_DATE, DateUtils.formatLocalDateTime(task.getStartDate()));
            values.put(TaskTable.COLUMN_END_DATE, DateUtils.formatLocalDateTime(task.getEndDate()));
            values.put(TaskTable.COLUMN_POINTS_REWARD, task.getPointsReward());

            long newId = db.insertOrThrow(TaskTable.TABLE_NAME, null, values);
            if (newId == -1) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.QUERY_FAILURE,
                        "Failed to insert task into the database."
                );
            }
            return getTaskById((int) newId);
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e, "Error during task creation.");
        }
    }

    /**
     * Retrieves all tasks from the database.
     *
     * @return A list of Task instances.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        final String[] columns = TaskTable.ALL_COLUMNS;

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     TaskTable.TABLE_NAME,
                     columns,
                     null,
                     null,
                     null,
                     null,
                     null
             )) {
            while (cursor.moveToNext()) {
                tasks.add(mapCursorToTask(cursor));
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e, "Error during retrieval of all tasks.");
        }
        return tasks;
    }

    /**
     * Retrieves a task from the database by its ID.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The Task instance if found.
     * @throws DatabaseOperationException If an error occurs during the database operation or if the task is not found.
     */
    public Task getTaskById(int taskId) {
        final String[] columns = TaskTable.ALL_COLUMNS;
        final String selection = TaskTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskId)};

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     TaskTable.TABLE_NAME,
                     columns,
                     selection,
                     selectionArgs,
                     null,
                     null,
                     null
             )) {

            if (cursor.moveToFirst())
                return mapCursorToTask(cursor);
            else {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.RESOURCE_NOT_FOUND,
                        String.format("Task not found with ID %d.", taskId)
                );
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error during retrieval of task with ID %d.", taskId)
            );
        }
    }

    /**
     * Updates an existing task in the database.
     *
     * @param task The Task instance containing updated data.
     * @return The updated Task instance.
     * @throws DatabaseOperationException If an error occurs during the database operation or if the task is not found.
     */
    public Task updateTask(@NonNull Task task) {
        final String selection = TaskTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(task.getId())};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(TaskTable.COLUMN_ICON, task.getIcon());
            values.put(TaskTable.COLUMN_TITLE, task.getTitle());
            values.put(TaskTable.COLUMN_DESCRIPTION, task.getDescription());
            values.put(TaskTable.COLUMN_FREQUENCY, task.getFrequency().getValue());
            values.put(TaskTable.COLUMN_FREQUENCY_INTERVAL, task.getFrequencyInterval());
            values.put(TaskTable.COLUMN_START_DATE, DateUtils.formatLocalDateTime(task.getStartDate()));
            values.put(TaskTable.COLUMN_END_DATE, DateUtils.formatLocalDateTime(task.getEndDate()));
            values.put(TaskTable.COLUMN_POINTS_REWARD, task.getPointsReward());

            int rowsUpdated = db.update(TaskTable.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.DATA_INTEGRITY_VIOLATION,
                        String.format("Failed to update task with ID %d. Task not found.", task.getId())
                );
            }
            return getTaskById(task.getId());
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error during task update for ID %d.", task.getId())
            );
        }
    }

    /**
     * Deletes a task from the database by its ID.
     *
     * @param taskId The ID of the task to delete.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public void deleteTask(int taskId) {
        final String selection = TaskTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskId)};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            int rowsDeleted = db.delete(TaskTable.TABLE_NAME, selection, selectionArgs);
            if (rowsDeleted == 0) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.RESOURCE_NOT_FOUND,
                        String.format("Failed to delete task with ID %d. Task not found.", taskId)
                );
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error during task deletion for ID %d.", taskId)
            );
        }
    }
}
