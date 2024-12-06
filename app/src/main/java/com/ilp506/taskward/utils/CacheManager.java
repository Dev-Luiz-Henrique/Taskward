package com.ilp506.taskward.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

/**
 * A utility class for managing cached data using SharedPreferences.
 * This class is designed to store and retrieve small amounts of data
 * persistently across app sessions.
 */
public class CacheManager {

    private static final String PREFS_NAME = "user_cache";

    private static final String KEY_USER_ID = "user_id";

    private static final int DEFAULT_USER_ID = 1;


    private final SharedPreferences sharedPreferences;

    /**
     * Constructs a new CacheManager instance.
     *
     * @param context The context used to access SharedPreferences.
     */
    public CacheManager(@NonNull Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Saves the user ID to the cache.
     *
     * @param userId The user ID to save.
     */
    public void saveUserId(int userId) {
        sharedPreferences.edit().putInt(KEY_USER_ID, userId).apply();
    }

    /**
     * Retrieves the user ID from the cache.
     *
     * @return The stored user ID, or the default value if no ID is stored.
     */
    public int getUserId() {
        return sharedPreferences.getInt(KEY_USER_ID, DEFAULT_USER_ID);
    }

    /**
     * Clears all cached data.
     * This will remove all entries stored in the SharedPreferences file.
     */
    public void clearCache() {
        sharedPreferences.edit().clear().apply();
    }
}
