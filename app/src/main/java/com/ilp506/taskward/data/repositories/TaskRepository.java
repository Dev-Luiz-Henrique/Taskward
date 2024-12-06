package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ilp506.taskward.data.DatabaseContract.TaskTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.enums.TaskFrequencyEnum;
import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.exceptions.DatabaseOperationException;
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
     * The database helper is responsible for managing database connections and operations.
     *
     * @param context The application context used to initialize the database helper.
     */
    public TaskRepository(Context context) {
        dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Maps the data from a Cursor object to a Task instance.
     * This method retrieves data from the Cursor and converts it into a Task object.
     * It extracts the task details based on the column indices and handles potential exceptions.
     *
     * @param cursor The cursor containing the queried data.
     * @return A Task instance populated with the cursor's data.
     * @throws RuntimeException If an error occurs during cursor mapping, such as missing or incorrect data format.
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
            throw new RuntimeException("Error mapping cursor to Task: " + e.getMessage(), e);
        }
        return task;
    }

    /**
     * Creates a new task in the database.
     * This method inserts a new task record into the database and retrieves the created Task object.
     * It throws an exception if the insert operation fails.
     *
     * @param task The Task instance to be created.
     * @return The created Task instance.
     * @throws DatabaseOperationException If an error occurs during the database operation, such as an insertion failure.
     * @throws RuntimeException If an error occurs during cursor mapping when retrieving the newly created task.
     */
    public Task createTask(Task task) {
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
                Logger.e(TAG, "Failed to create task");
                throw new DatabaseOperationException("Failed to create task");
            }
            return getTaskById((int) newId);
        }
        catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during task creation: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all tasks from the database.
     * This method queries the database for all task records and returns them as a list of Task objects.
     * If the database query fails, an exception is thrown.
     *
     * @return A list of Task instances.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     * @throws RuntimeException If an error occurs while mapping the cursor data to Task objects.
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
                Task task = mapCursorToTask(cursor);
                tasks.add(task);
            }
        } catch (SQLiteException e) {
            Logger.e(TAG, "Error retrieving tasks: " + e.getMessage(), e);
            throw new DatabaseOperationException("Error retrieving tasks: " + e.getMessage(), e);
        }
        return tasks;
    }

    /**
     * Retrieves a task from the database by its ID.
     * This method queries the database for a single task based on its ID. If the task is found,
     * it returns the corresponding Task object. Otherwise, it logs a warning and returns null.
     *
     * @param taskId The ID of the task to retrieve.
     * @return The Task instance if found, or null if not found.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     * @throws RuntimeException If an error occurs during cursor mapping when retrieving the task.
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
                Logger.w(TAG, "Task not found with ID: " + taskId);
                return null;
            }
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during getTaskById: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing task in the database.
     * This method modifies an existing task record in the database using the provided Task instance.
     * It throws an exception if no rows were updated, meaning the task with the given ID was not found.
     *
     * @param task The Task instance containing updated data.
     * @return The updated Task instance.
     * @throws DatabaseOperationException If no rows are updated, meaning the task was not found
     * or there was an error during the update.
     * @throws RuntimeException If an error occurs during the database operation.
     */
    public Task updateTask(Task task) {
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
            values.put(TaskTable.COLUMN_CREATED_AT, DateUtils.formatLocalDateTime(task.getCreatedAt()));

            int rowsUpdated = db.update(TaskTable.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0)
                throw new DatabaseOperationException("No rows updated. Task not found.");

            return getTaskById(task.getId());
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during task update: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a task from the database by its ID.
     * This method performs a deletion operation based on the provided task ID.
     * If the task does not exist or the deletion fails, an exception is thrown.
     *
     * @param taskId The ID of the task to delete.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public void deleteTask(int taskId) {
        final String selection = TaskTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskId)};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete(TaskTable.TABLE_NAME, selection, selectionArgs);

        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during task deletion: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }
}
