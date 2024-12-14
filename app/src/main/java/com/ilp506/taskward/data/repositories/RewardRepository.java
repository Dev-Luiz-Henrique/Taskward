package com.ilp506.taskward.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import androidx.annotation.NonNull;

import com.ilp506.taskward.data.DatabaseContract.RewardTable;
import com.ilp506.taskward.data.DatabaseHelper;
import com.ilp506.taskward.data.models.Reward;
import com.ilp506.taskward.exceptions.codes.DatabaseErrorCode;
import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;
import com.ilp506.taskward.exceptions.handlers.DatabaseErrorHandler;
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
     *
     * @param context The application context used to initialize the database helper.
     */
    public RewardRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);
    }

    /**
     * Maps the data from a Cursor object to a Reward instance.
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
     *
     * @param reward The Reward instance to be created.
     * @return The created Reward instance.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public Reward createReward(@NonNull Reward reward) {
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
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.QUERY_FAILURE,
                        "Failed to insert reward into the database."
                );
            }
            return getRewardById((int) newId);
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e, "Error during reward creation.");
        }
    }

    /**
     * Retrieves all rewards from the database.
     *
     * @return A list of Reward instances.
     * @throws DatabaseOperationException If an error occurs during the database operation.
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
            throw DatabaseErrorHandler.handleSQLiteException(e, "Error during retrieval of all rewards.");
        }
        return rewards;
    }

    /**
     * Retrieves a reward from the database by its ID.
     *
     * @param rewardId The ID of the reward to retrieve.
     * @return The Reward instance if found.
     * @throws DatabaseOperationException If an error occurs during the database operation or the reward is not found.
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
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.QUERY_FAILURE,
                        String.format("Reward with ID %d not found.", rewardId)
                );
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error during retrieval of reward with ID %d.", rewardId)
            );
        }
    }

    /**
     * Updates an existing reward in the database.
     *
     * @param reward The Reward instance containing updated data.
     * @return The updated Reward instance.
     * @throws DatabaseOperationException If an error occurs during the database operation or the reward is not found.
     */
    public Reward updateReward(@NonNull Reward reward) {
        final String selection = RewardTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(reward.getId())};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            ContentValues values = new ContentValues();
            values.put(RewardTable.COLUMN_USER_ID, reward.getUserId());
            values.put(RewardTable.COLUMN_ICON, reward.getIcon());
            values.put(RewardTable.COLUMN_TITLE, reward.getTitle());
            values.put(RewardTable.COLUMN_DESCRIPTION, reward.getDescription());
            values.put(RewardTable.COLUMN_POINTS_REQUIRED, reward.getPointsRequired());
            values.put(RewardTable.COLUMN_DATE_REDEEMED, DateUtils.formatLocalDateTime(reward.getDateRedeemed()));

            int rowsUpdated = db.update(RewardTable.TABLE_NAME, values, selection, selectionArgs);
            if (rowsUpdated == 0) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.DATA_INTEGRITY_VIOLATION,
                        "No rows updated. Reward not found."
                );
            }
            return getRewardById(reward.getId());
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error during reward update for ID %d.", reward.getId())
            );
        }
    }

    /**
     * Deletes a reward from the database by its ID.
     *
     * @param rewardId The ID of the reward to delete.
     * @throws DatabaseOperationException If an error occurs during the database operation.
     */
    public void deleteReward(int rewardId) {
        final String selection = RewardTable.COLUMN_ID + " = ?";
        final String[] selectionArgs = {String.valueOf(rewardId)};

        try (SQLiteDatabase db = dbHelper.getWritableDatabase()) {
            int rowsDeleted = db.delete(RewardTable.TABLE_NAME, selection, selectionArgs);
            if (rowsDeleted == 0) {
                throw DatabaseOperationException.fromError(
                        DatabaseErrorCode.QUERY_FAILURE,
                        String.format("Failed to delete reward with ID %d. Reward not found.", rewardId)
                );
            }
        } catch (SQLiteException e) {
            throw DatabaseErrorHandler.handleSQLiteException(e,
                    String.format("Error during reward deletion for ID %d.", rewardId)
            );
        }
    }
}
