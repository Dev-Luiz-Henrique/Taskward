package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ilp506.taskward.data.DatabaseContract.TaskEventTable;
import com.ilp506.taskward.data.DatabaseContract.TaskTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.exceptions.DatabaseOperationException;
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
     * Constructor for TaskEventRepository.
     * Initializes the DatabaseHelper instance using the provided context.
     *
     * @param context The application context used to initialize the DatabaseHelper.
     */
    public TaskEventRepository(Context context){
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Maps the data from a Cursor object to a TaskEvent instance.
     * This method retrieves data from the Cursor and converts it into a TaskEvent object.
     * It extracts the task event details based on the column indices and handles potential exceptions.
     *
     * @param cursor The cursor containing the queried data.
     * @return A TaskEvent instance populated with the cursor's data.
     * @throws RuntimeException If an error occurs during cursor mapping, such as missing or incorrect data format.
     */
    protected TaskEvent mapCursorToTaskEvent(Cursor cursor){
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

        } catch (Exception e){
            Logger.e(TAG, "Error mapping cursor to TaskEvent: " + e.getMessage(), e);
            throw new RuntimeException("Error mapping cursor to TaskEvent: " + e.getMessage(), e);
        }
        return taskEvent;
    }

    /**
     * Creates a new task event in the database.
     * This method inserts a new task event record into the database and retrieves the created TaskEvent object.
     * It throws an exception if the insert operation fails.
     *
     * @param taskEvent The TaskEvent instance to be created.
     * @return The created TaskEvent instance.
     * @throws DatabaseOperationException If an error occurs during the database operation, such as an insertion failure.
     * @throws RuntimeException If an error occurs during cursor mapping when retrieving the newly created task event.
     */
    public TaskEvent createTaskEvent(TaskEvent taskEvent){
        try(SQLiteDatabase db = dbHelper.getWritableDatabase()){

            ContentValues values = new ContentValues();
            values.put(TaskEventTable.COLUMN_USER_ID, taskEvent.getUserId());
            values.put(TaskEventTable.COLUMN_TASK_ID, taskEvent.getTaskId());
            values.put(TaskEventTable.COLUMN_SCHEDULED_DATE, DateUtils.formatLocalDateTime(taskEvent.getScheduledDate()));
            values.put(TaskEventTable.COLUMN_COMPLETED_DATE, DateUtils.formatLocalDateTime(taskEvent.getCompletedDate()));
            values.put(TaskEventTable.COLUMN_POINTS_EARNED, taskEvent.getPointsEarned());
            values.put(TaskEventTable.COLUMN_STATUS, taskEvent.getStatus().getValue());
            values.put(TaskEventTable.COLUMN_CREATED_AT, DateUtils.formatLocalDateTime(taskEvent.getCreatedAt()));

            long newId = db.insertOrThrow(TaskEventTable.TABLE_NAME, null, values);
            if(newId == -1){
                Logger.e(TAG, "Failed to insert new task event");
                throw new DatabaseOperationException("Failed to insert new task event");
            }
            return getTaskEventById((int) newId);
        } catch (SQLiteException e){
            Logger.e(TAG, "SQLite error during task event creation: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all task events from the database.
     * This method queries the database for all task event records and returns them as a list of TaskEvent objects.
     * If the database query fails, an exception is thrown.
     *
     * @return A list of TaskEvent instances.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     * @throws RuntimeException If an error occurs while mapping the cursor data to TaskEvent objects.
     */
    public List<TaskEvent> getAllTaskEvents(){
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

        try(SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    table,
                    columns,
                    null,
                    null,
                    null,
                    null,
                    null
            )){
            while(cursor.moveToNext()){
                TaskEvent taskEvent = mapCursorToTaskEvent(cursor);
                taskEvents.add(taskEvent);
            }
            return taskEvents;
        } catch (SQLiteException e){
            Logger.e(TAG, "SQLite error during getAllTaskEvents: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves a task event from the database by its ID.
     * This method queries the database for a specific task event record based on the provided taskEventId.
     * It returns the corresponding TaskEvent object. Otherwise, it logs a warning and returns null.
     *
     * @param taskEventId The ID of the task event to retrieve.
     * @return The TaskEvent instance if found, or null if not found.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     * @throws RuntimeException If an error occurs while mapping the cursor data to a TaskEvent object.
     */
    public TaskEvent getTaskEventById(int taskEventId){
        final String[] columns = TaskEventTable.ALL_COLUMNS;
        final String selection = TaskEventTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskEventId)};

        try(SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(
                    TaskEventTable.TABLE_NAME,
                    columns,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            )){
            if(cursor.moveToFirst())
                return mapCursorToTaskEvent(cursor);
            else {
                Logger.w(TAG, "Task event not found with ID: " + taskEventId);
                return null;
            }
        } catch (SQLiteException e){
            Logger.e(TAG, "SQLite error during getTaskEventById: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing task event in the database.
     * This method modifies an existing task event record in the database using the provided TaskEvent instance.
     * It throws an exception if no rows were updated, meaning the task event with the given ID was not found.
     *
     * @param taskEvent The TaskEvent instance containing updated data.
     * @return The updated TaskEvent instance.
     * @throws DatabaseOperationException If no rows are updated, meaning the task event was not found
     * or there was an error during the update.
     * @throws RuntimeException If an error occurs during the database operation.
     */
    public TaskEvent updateTaskEvent(TaskEvent taskEvent){
        final String selection = TaskEventTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskEvent.getId())};

        try(SQLiteDatabase db = dbHelper.getWritableDatabase()){

            ContentValues values = new ContentValues();
            values.put(TaskEventTable.COLUMN_USER_ID, taskEvent.getUserId());
            values.put(TaskEventTable.COLUMN_TASK_ID, taskEvent.getTaskId());
            values.put(TaskEventTable.COLUMN_SCHEDULED_DATE, DateUtils.formatLocalDateTime(taskEvent.getScheduledDate()));
            values.put(TaskEventTable.COLUMN_COMPLETED_DATE, DateUtils.formatLocalDateTime(taskEvent.getCompletedDate()));
            values.put(TaskEventTable.COLUMN_POINTS_EARNED, taskEvent.getPointsEarned());
            values.put(TaskEventTable.COLUMN_STATUS, taskEvent.getStatus().getValue());
            values.put(TaskEventTable.COLUMN_CREATED_AT, DateUtils.formatLocalDateTime(taskEvent.getCreatedAt()));

            int rowsUpdated = db.update(TaskEventTable.TABLE_NAME, values, selection, selectionArgs);
            if(rowsUpdated == 0)
                throw new DatabaseOperationException("No rows updated. Task event not found.");

            return getTaskEventById(taskEvent.getId());
        } catch (SQLiteException e){
            Logger.e(TAG, "SQLite error during task event update: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a task event from the database by its ID.
     * This method performs a deletion operation based on the provided taskEventId.
     * If the task event does not exist or the deletion fails, an exception is thrown.
     *
     * @param taskEventId The ID of the task event to delete.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public void deleteTaskEvent(int taskEventId){
        final String selection = TaskEventTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(taskEventId)};

        try(SQLiteDatabase db = dbHelper.getWritableDatabase()){
            db.delete(TaskEventTable.TABLE_NAME, selection, selectionArgs);

        } catch (SQLiteException e){
            Logger.e(TAG, "SQLite error during task event deletion: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves the next TaskEvent with the same Task ID and a scheduled date after the current TaskEvent.
     *
     * @param currentTaskEvent The current TaskEvent to base the search on.
     * @return The next TaskEvent, or null if no next event exists.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public TaskEvent getNextTaskEvent(TaskEvent currentTaskEvent) {
        final String query = "SELECT * FROM " + TaskEventTable.TABLE_NAME +
                " WHERE " + TaskEventTable.COLUMN_TASK_ID + " = ? AND " +
                TaskEventTable.COLUMN_SCHEDULED_DATE + " > ?" +
                " ORDER BY " + TaskEventTable.COLUMN_SCHEDULED_DATE + " ASC LIMIT 1";

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.rawQuery(query, new String[] {
                     String.valueOf(currentTaskEvent.getTaskId()),
                     DateUtils.formatLocalDateTime(currentTaskEvent.getScheduledDate())
             })) {

            if (cursor.moveToFirst())
                return mapCursorToTaskEvent(cursor);
            else {
                Logger.w(TAG, "No next task event found for Task ID: " + currentTaskEvent.getTaskId());
                return null;
            }
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during getNextTaskEvent: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

}
