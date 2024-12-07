package com.ilp506.taskward.data;

import static com.ilp506.taskward.utils.SQLScriptUtils.executeSQLFromResource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ilp506.taskward.R;
import com.ilp506.taskward.utils.Logger;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "taskward.db";
    private static final int DATABASE_VERSION = 1;

    private static DatabaseHelper instance;

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context.getApplicationContext();;
    }

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Logger.d(TAG, "Creating database...");
        executeSQLFromResource(db, context, R.raw.create);
        executeSQLFromResource(db, context, R.raw.insert);
        Logger.d(TAG, "Database created successfully.");
    }

    // TODO: Implementar a logica de atualizacao do banco de dados
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Logger.d(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion);
        // ...
        Logger.d(TAG, "Database upgraded successfully.");
    }

    public boolean deleteDatabase() {
        boolean isDeleted = context.deleteDatabase(DATABASE_NAME);
        if (isDeleted)
            Logger.d(TAG, "Database deleted successfully.");
        else
            Logger.d(TAG, "Failed to delete database.");
        return isDeleted;
    }
}