package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.ilp506.taskward.data.DatabaseContract.RewardTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.models.Reward;
import com.ilp506.taskward.exceptions.DatabaseOperationException;
import com.ilp506.taskward.utils.DateUtils;
import com.ilp506.taskward.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Repository class responsible for managing database operations related to the Reward model.
 * This class performs CRUD operations for the Reward model in the local SQLite database.
 */
public class RewardRepository {
    private static final String TAG = RewardRepository.class.getSimpleName();

    private final DatabaseHelper dbHelper;

    /**
     * Constructs a RewardRepository with a database helper instance.
     * The database helper is responsible for managing database connections and operations.
     *
     * @param context The application context used to initialize the database helper.
     */
    public RewardRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Maps the data from a Cursor object to a Reward instance.
     * This method retrieves data from the Cursor and converts it into a Reward object.
     * It extracts the reward details based on the column indices and handles potential exceptions.
     *
     * @param cursor The cursor containing the queried data.
     * @return A Reward instance populated with the cursor's data.
     * @throws RuntimeException If an error occurs during cursor mapping, such as missing or incorrect data format.
     */
    protected Reward mapCursorToReward(Cursor cursor) {
        Reward reward = new Reward();
        try {
            reward.setId(cursor.getInt(cursor.getColumnIndexOrThrow(RewardTable.COLUMN_ID)));
            reward.setUserId(cursor.getInt(cursor.getColumnIndexOrThrow(RewardTable.COLUMN_USER_ID)));
            reward.setIcon(cursor.getString(cursor.getColumnIndexOrThrow(RewardTable.COLUMN_ICON)));
            reward.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(RewardTable.COLUMN_TITLE)));
            reward.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(RewardTable.COLUMN_DESCRIPTION)));
            reward.setPointsRequired(cursor.getInt(cursor.getColumnIndexOrThrow(RewardTable.COLUMN_POINTS_REQUIRED)));
            reward.setDateRedeemed(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(RewardTable.COLUMN_DATE_REDEEMED))
            ));
            reward.setCreatedAt(DateUtils.parseLocalDateTime(
                    cursor.getString(cursor.getColumnIndexOrThrow(RewardTable.COLUMN_CREATED_AT))
            ));

        } catch (Exception e) {
            Logger.e(TAG, "Error mapping cursor to Reward: " + e.getMessage(), e);
            throw new RuntimeException("Error mapping cursor to Reward: " + e.getMessage(), e);
        }
        return reward;
    }

    /**
     * Creates a new reward in the database.
     * This method inserts a new reward record into the database and retrieves the created Reward object.
     * It throws an exception if the insert operation fails.
     *
     * @param reward The Reward instance to be created.
     * @return The created Reward instance.
     * @throws DatabaseOperationException If an error occurs during the database operation, such as an insertion failure.
     * @throws RuntimeException If an error occurs during cursor mapping when retrieving the newly created reward.
     */
    public Reward createReward(Reward reward) {
        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {

            ContentValues values = new ContentValues();
            values.put(RewardTable.COLUMN_USER_ID, reward.getUserId());
            values.put(RewardTable.COLUMN_ICON, reward.getIcon());
            values.put(RewardTable.COLUMN_TITLE, reward.getTitle());
            values.put(RewardTable.COLUMN_DESCRIPTION, reward.getDescription());
            values.put(RewardTable.COLUMN_POINTS_REQUIRED, reward.getPointsRequired());

            long newId = db.insertOrThrow(RewardTable.TABLE_NAME, null, values);
            if (newId == -1) {
                Logger.e(TAG, "Failed to insert new reward");
                throw new DatabaseOperationException("Failed to insert new reward");
            }

            return getRewardById((int) newId);
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during reward creation: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Retrieves all rewards from the database.
     * This method queries the database for all reward records and returns them as a list of Reward objects.
     * If the database query fails, an exception is thrown.
     *
     * @return A list of Reward instances.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     * @throws RuntimeException If an error occurs while mapping the cursor data to Reward objects.
     */
    public List<Reward> getAllRewards() {
        List<Reward> rewards = new ArrayList<>();
        final String[] columns = RewardTable.ALL_COLUMNS;

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     RewardTable.TABLE_NAME,
                     columns,
                     null,
                     null,
                     null,
                     null,
                     null
             )) {

            while (cursor.moveToNext()) {
                Reward reward = mapCursorToReward(cursor);
                rewards.add(reward);
            }
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during getAllRewards: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
        return rewards;
    }

    /**
     * Retrieves a reward from the database by its ID.
     * This method queries the database for a single reward based on its ID. If the reward is found,
     * it returns the corresponding Reward object. Otherwise, it logs a warning and returns null.
     *
     * @param rewardId The ID of the reward to retrieve.
     * @return The Reward instance if found, or null if not found.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     * @throws RuntimeException If an error occurs while mapping the cursor data to a Reward object.
     */
    public Reward getRewardById(int rewardId) {
        final String[] columns = RewardTable.ALL_COLUMNS;
        final String selection = RewardTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(rewardId)};

        try (SQLiteDatabase db = dbHelper.getReadableDatabase();
             Cursor cursor = db.query(
                     RewardTable.TABLE_NAME,
                     columns,
                     selection,
                     selectionArgs,
                     null,
                     null,
                     null
             )) {

            if (cursor.moveToFirst())
                return mapCursorToReward(cursor);
            else {
                Logger.w(TAG, "Reward not found with ID: " + rewardId);
                return null;
            }
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during getRewardById: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Updates an existing reward in the database.
     * This method modifies an existing reward record in the database using the provided Reward instance.
     * It throws an exception if no rows were updated, meaning the reward with the given ID was not found.
     *
     * @param reward The Reward instance containing updated data.
     * @return The updated Reward instance.
     * @throws DatabaseOperationException If no rows are updated, meaning the reward was not found
     * or there was an error during the update.
     * @throws RuntimeException If an error occurs while mapping the cursor data to the updated Reward object.
     */
    public Reward updateReward(Reward reward) {
        final String selection = RewardTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(reward.getId())};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(RewardTable.COLUMN_USER_ID, reward.getUserId());
            values.put(RewardTable.COLUMN_ICON, reward.getIcon());
            values.put(RewardTable.COLUMN_TITLE, reward.getTitle());
            values.put(RewardTable.COLUMN_DESCRIPTION, reward.getDescription());
            values.put(RewardTable.COLUMN_POINTS_REQUIRED, reward.getPointsRequired());

            int rowsUpdated = db.update(RewardTable.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0)
                throw new DatabaseOperationException("No rows updated. Reward not found.");

            return getRewardById(reward.getId());
        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during reward update: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }

    /**
     * Deletes a reward from the database by its ID.
     * This method performs a deletion operation based on the provided reward ID.
     * If the reward does not exist or the deletion fails, an exception is thrown.
     *
     * @param rewardId The ID of the reward to delete.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public void deleteReward(int rewardId) {
        final String selection = RewardTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(rewardId)};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            db.delete(RewardTable.TABLE_NAME, selection, selectionArgs);

        } catch (SQLiteException e) {
            Logger.e(TAG, "SQLite error during reward deletion: " + e.getMessage(), e);
            throw new DatabaseOperationException("Database error: " + e.getMessage(), e);
        }
    }
}
