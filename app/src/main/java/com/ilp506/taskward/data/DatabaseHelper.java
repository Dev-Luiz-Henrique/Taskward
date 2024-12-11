package com.ilp506.taskward.data;

import static com.ilp506.taskward.utils.SQLScriptUtils.executeSQLFromResource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ilp506.taskward.R;
import com.ilp506.taskward.utils.Logger;

/**
 * Helper class for managing the TaskWard database.
 * This class extends SQLiteOpenHelper to manage the creation, update, and deletion
 * of the application's SQLite database. It follows the Singleton design pattern
 * to ensure a single instance of the database is used throughout the application.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "taskward.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;

    private final Context context;

    /**
     * Private constructor to prevent direct instantiation of DatabaseHelper.
     *
     * @param context Application context used to access resources.
     */
    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();;
    }

    /**
     * Returns the singleton instance of DatabaseHelper.
     * Ensures that only one instance of DatabaseHelper is used in the application.
     *
     * @param context Application context.
     * @return The singleton instance of DatabaseHelper.
     */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    /**
     * Called when the database is created for the first time.
     * This method is responsible for creating the initial structure of the database
     * and populating it with any required initial data. It executes SQL scripts
     * from the 'create.sql' and 'insert.sql' files in the 'res/raw' directory.
     *
     * @param db The database being created.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.d(TAG, "Creating database...");
        executeSQLFromResource(db, context, R.raw.create);
        executeSQLFromResource(db, context, R.raw.insert);
        Logger.d(TAG, "Database created successfully.");
    }

    /**
     * Called when the database needs to be upgraded.
     *
     * This method is triggered when the DATABASE_VERSION is incremented.
     * The logic for migrating the database schema should be implemented here.
     *
     * @param db The database being upgraded.
     * @param oldVersion The old version of the database.
     * @param newVersion The new version of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // TODO: Implement logic to handle database migration when the version is updated
        // Example: Altering tables, adding new columns, or migrating data from old structure to new
        Logger.d(TAG, "Database upgraded successfully.");
    }

    /**
     * Deletes the database file.
     * This method is useful for testing purposes or when a full database reset is required.
     * It deletes the database file from the device's internal storage.
     *
     * @return true if the database was successfully deleted, false otherwise.
     */
    public boolean deleteDatabase() {
        boolean isDeleted = context.deleteDatabase(DATABASE_NAME);
        if (isDeleted) Logger.d(TAG, "Database deleted successfully.");
        else Logger.d(TAG, "Failed to delete database.");
        return isDeleted;
    }
}