package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;

import com.ilp506.taskward.data.DatabaseContract.TaskEventTable;
import com.ilp506.taskward.data.DatabaseContract.TaskTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.exceptions.codes.DatabaseErrorCode;
import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;
import com.ilp506.taskward.exceptions.handlers.DatabaseErrorHandler;
import com.ilp506.taskward.utils.DateUtils;
import com.ilp506.taskward.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing database operations related to the TaskEvent model.
 * This class performs CRUD operations for the TaskEvent model in the local SQLite database.
 */
public class TaskEventRepository {
    private static final String TAG = TaskEventRepository.class.getSimpleName();

    private final DatabaseHelper dbHelper;

    /**
     * Constructs a TaskEventRepository with a database helper instance.
     *
     * @param context The application context used to initialize the DatabaseHelper.
     */
    public TaskEventRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Maps the data from a Cursor object to a TaskEvent instance.
     *
     * @param cursor The cursor containing the queried data.
     * @return A TaskEvent instance populated with the cursor's data.
     * @throws DatabaseOperationException If an error occurs during the mapping process.
     */
    protected TaskEvent mapCursorToTaskEvent(Cursor cursor) {
        TaskEvent taskEvent = new TaskEvent();
        try {
            taskEvent.setId(cursor.getInt(cursor.getColumnIndexOrThrow(TaskEventTable.COLUMN_ID)));
            taskEvent.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(TaskEventTable.COLUMN_USER_ID)));
            taskEvent.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow(TaskEventTable.COLUMN_TASK_ID)));
            taskEvent.setScheduledDate(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskEventTable.COLUMN_SCHEDULED_DATE)))
            );
            taskEvent.setCompletedDate(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskEventTable.COLUMN_COMPLETED_DATE)))
            );
            taskEvent.setPointsEarned(cursor.getInt(cursor.getColumnIndexOrThrow(TaskEventTable.COLUMN_POINTS_EARNED)));
            taskEvent.setStatus(TaskEventStatusEnum.fromString(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskEventTable.COLUMN_STATUS)))
            );
            taskEvent.setCreatedAt(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(TaskEventTable.COLUMN_CREATED_AT)))
            );

            int titleIndex = cursor.getColumnIndex(TaskTable.COLUMN_TITLE);
            if (titleIndex != -1) taskEvent.setTitle(cursor.getString(titleIndex));

        } catch (Exception e) {
            Logger.e(TAG, "Error mapping cursor to TaskEvent: " + e.getMessage(), e);
            throw DatabaseOperationException.fromError(
                    DatabaseErrorCode.UNEXPECTED_ERROR,
                    "Error mapping cursor to TaskEvent.",
                    e
            );
        }
        return taskEvent;
    }

    /**
     * Creates a new TaskEvent in the database.
     *
     * @param taskEvent The TaskEvent instance to be created.
     * @return The created TaskEvent instance.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public TaskEvent createTaskEvent(@NonNull TaskEvent taskEvent) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(TaskEventTable.COLUMN_USER_ID, taskEvent.getUserId());
            values.put(TaskEventTable.COLUMN_TASK_ID, taskEvent.getTaskId());
            values.put(TaskEventTable.COLUMN_SCHEDULED_DATE, DateUtils.formatLocalDateTime(taskEvent.getScheduledDate()));
            values.put(TaskEventTable.COLUMN_COMPLETED_DATE, DateUtils.formatLocalDateTime(taskEvent.getCompletedDate()));
            values.put(TaskEventTable.COLUMN_POINTS_EARNED, taskEvent.getPointsEarned());
            values.put(TaskEventTable.COLUMN_STATUS, taskEvent.getStatus().getValue());
            values.put(TaskEventTable.COLUMN_CREATED_AT, DateUtils.formatLocalDateTime(taskEvent.getCreatedAt()));

            long newId = db.insertOrThrow(TaskEventTable.TABLE_NAME, null, values);
            if (newId == -1) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.QUERY_FAILURE,
                        "Failed to insert new TaskEvent."
                );
            }
            return getTaskEventById((int) newId);
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e, "Error during TaskEvent creation.");
        }
    }

    /**
     * Retrieves all TaskEvents from the database.
     *
     * @return A list of TaskEvent instances.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public List<TaskEvent> getAllTaskEvents() {
        List<TaskEvent> taskEvents = new ArrayList<>();
        final String table = String.format(
                "%s LEFT JOIN %s ON %s.%s = %s.%s",
                TaskEventTable.TABLE_NAME,
                TaskTable.TABLE_NAME,
                TaskEventTable.TABLE_NAME,
                TaskEventTable.COLUMN_TASK_ID,
                TaskTable.TABLE_NAME,
                TaskTable.COLUMN_ID
        );

        final String[] columns = {
                TaskEventTable.TABLE_NAME + ".*",
                TaskTable.TABLE_NAME + "." + TaskTable.COLUMN_TITLE
        };

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     table,
                     columns,
                     null,
                     null,
                     null,
                     null,
                     null
             )) {
            while (cursor.moveToNext()) {
                taskEvents.add(mapCursorToTaskEvent(cursor));
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e, "Error retrieving all TaskEvents.");
        }
        return taskEvents;
    }

    /**
     * Retrieves a TaskEvent by its ID.
     *
     * @param taskEventId The ID of the TaskEvent to retrieve.
     * @return The TaskEvent instance if found.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public TaskEvent getTaskEventById(int taskEventId) {
        final String[] columns = TaskEventTable.ALL_COLUMNS;
        final String selection = TaskEventTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskEventId)};

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     TaskEventTable.TABLE_NAME,
                     columns,
                     selection,
                     selectionArgs,
                     null,
                     null,
                     null
             )) {

            if (cursor.moveToFirst())
                return mapCursorToTaskEvent(cursor);
            else {
                Logger.w(TAG, "TaskEvent not found with ID: " + taskEventId);
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.RESOURCE_NOT_FOUND,
                        String.format("TaskEvent not found with ID %d.", taskEventId)
                );
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error retrieving TaskEvent with ID %d.", taskEventId)
            );
        }
    }

    /**
     * Updates an existing TaskEvent in the database.
     *
     * @param taskEvent The TaskEvent instance containing updated data.
     * @return The updated TaskEvent instance.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public TaskEvent updateTaskEvent(@NonNull TaskEvent taskEvent) {
        final String selection = TaskEventTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskEvent.getId())};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(TaskEventTable.COLUMN_USER_ID, taskEvent.getUserId());
            values.put(TaskEventTable.COLUMN_TASK_ID, taskEvent.getTaskId());
            values.put(TaskEventTable.COLUMN_SCHEDULED_DATE, DateUtils.formatLocalDateTime(taskEvent.getScheduledDate()));
            values.put(TaskEventTable.COLUMN_COMPLETED_DATE, DateUtils.formatLocalDateTime(taskEvent.getCompletedDate()));
            values.put(TaskEventTable.COLUMN_POINTS_EARNED, taskEvent.getPointsEarned());
            values.put(TaskEventTable.COLUMN_STATUS, taskEvent.getStatus().getValue());
            values.put(TaskEventTable.COLUMN_CREATED_AT, DateUtils.formatLocalDateTime(taskEvent.getCreatedAt()));

            int rowsUpdated = db.update(TaskEventTable.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.DATA_INTEGRITY_VIOLATION,
                        String.format("Failed to update TaskEvent with ID %d. TaskEvent not found.", taskEvent.getId())
                );
            }
            return getTaskEventById(taskEvent.getId());
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error updating TaskEvent with ID %d.", taskEvent.getId())
            );
        }
    }

    /**
     * Deletes a TaskEvent by its ID.
     *
     * @param taskEventId The ID of the TaskEvent to delete.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public void deleteTaskEvent(int taskEventId) {
        final String selection = TaskEventTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskEventId)};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            int rowsDeleted = db.delete(TaskEventTable.TABLE_NAME, selection, selectionArgs);
            if (rowsDeleted == 0) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.RESOURCE_NOT_FOUND,
                        String.format("Failed to delete TaskEvent with ID %d. TaskEvent not found.", taskEventId)
                );
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error deleting TaskEvent with ID %d.", taskEventId)
            );
        }
    }

    /**
     * Retrieves the next TaskEvent with the same Task ID and a scheduled date after the current TaskEvent.
     *
     * @param currentTaskEvent The current TaskEvent to base the search on.
     * @return The next TaskEvent, or null if no next event exists.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public TaskEvent getNextTaskEvent(@NonNull TaskEvent currentTaskEvent) {
        final String query = "SELECT * FROM " + TaskEventTable.TABLE_NAME +
                " WHERE " + TaskEventTable.COLUMN_TASK_ID + " = ? AND " +
                TaskEventTable.COLUMN_SCHEDULED_DATE + " > ?" +
                " ORDER BY " + TaskEventTable.COLUMN_SCHEDULED_DATE + " ASC LIMIT 1";

        String taskId = String.valueOf(currentTaskEvent.getTaskId());
        String formattedDate = DateUtils.formatLocalDateTime(currentTaskEvent.getScheduledDate());

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[]{taskId, formattedDate})) {

            if (cursor.moveToFirst())
                return mapCursorToTaskEvent(cursor);
            else {
                Logger.w(TAG, String.format("No next TaskEvent found for Task ID %s after %s.", taskId, formattedDate));
                return null;
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error retrieving next TaskEvent for Task ID %s after %s.", taskId, formattedDate)
            );
        }
    }

    /**
     * Retrieves all TaskEvents associated with a specific Task ID.
     *
     * @param taskId The Task ID to retrieve TaskEvents for.
     * @return A list of TaskEvent instances.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public List<TaskEvent> getAllTaskEventsByTaskId(int taskId) {
        final String[] columns = TaskEventTable.ALL_COLUMNS;
        final String selection = TaskEventTable.COLUMN_TASK_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskId)};

        List<TaskEvent> taskEvents = new ArrayList<>();
        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     TaskEventTable.TABLE_NAME,
                     columns,
                     selection,
                     selectionArgs,
                     null,
                     null,
                     null
             )) {
            while (cursor.moveToNext()) {
                taskEvents.add(mapCursorToTaskEvent(cursor));
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error retrieving TaskEvents for Task ID %d.", taskId)
            );
        }
        return taskEvents;
    }
}
