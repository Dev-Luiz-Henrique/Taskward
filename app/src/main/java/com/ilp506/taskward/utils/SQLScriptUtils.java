package com.ilp506.taskward.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.drop.Drop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SQLScriptUtils {

    private static final String TAG = SQLScriptUtils.class.getSimpleName();

    /**
     * Reads and executes an SQL script from a raw resource file.
     * @param db         the SQLiteDatabase instance on which the SQL script will be executed.
     * @param context    the Context used to access the application resources.
     * @param resourceId the resource ID of the raw SQL script to be executed.
     * @throws RuntimeException if an error occurs while reading the resource or executing the SQL script.
     * @throws IllegalArgumentException if the provided database or context is null.
     */
    public static void executeSQLFromResource(SQLiteDatabase db, Context context, int resourceId) {
        if (db == null || context == null)
            throw new IllegalArgumentException("Database and context must not be null.");

        Logger.d(TAG, "Starting execution of SQL script from resource: " + resourceId);

        db.beginTransaction();
        try (InputStream inputStream = context.getResources().openRawResource(resourceId);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            StringBuilder sqlBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("--") || line.startsWith("/*")) continue;
                sqlBuilder.append(line).append("\n");
            }

            executeParsedSQLCommands(sqlBuilder.toString(), db);
            db.setTransactionSuccessful();
            Logger.d(TAG, "SQL script from resource " + resourceId + " executed successfully.");

        } catch (IOException e) {
            Logger.e(TAG, "Error reading SQL script from resource: " + resourceId, e);
            throw new RuntimeException("Error reading SQL script from resource.", e);
        } catch (Exception e) {
            Logger.e(TAG, "Error executing SQL script from resource: " + resourceId, e);
            throw new RuntimeException("Error executing SQL script from resource.", e);
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Parses and executes SQL commands using JSQLParser.
     *
     * @param sql the SQL commands as a string
     * @param db  the SQLiteDatabase instance
     */
    private static void executeParsedSQLCommands(String sql, SQLiteDatabase db) {
        try {
            List<Statement> statements = CCJSqlParserUtil.parseStatements(sql).getStatements();

            for (Statement stmt : statements) {
                Logger.d(TAG, "Executing SQL: " + stmt);

                if (stmt instanceof Select)
                    executeSelect(db, stmt.toString());
                else if (stmt instanceof Insert || stmt instanceof Update ||
                         stmt instanceof Delete || stmt instanceof CreateTable ||
                         stmt instanceof Drop)
                    executeModification(db, stmt.toString());
                else
                    Logger.w(TAG, "Unsupported SQL command: " + stmt);
            }
        } catch (Exception e) {
            Logger.e(TAG, "Error parsing SQL with JSQLParser.", e);
            throw new RuntimeException("Error parsing SQL with JSQLParser.", e);
        }
    }

    /**
     * Executes a SELECT command.
     *
     * @param db  the SQLiteDatabase instance
     * @param sql the SELECT command as a string
     */
    private static void executeSelect(SQLiteDatabase db, String sql) {
        try (Cursor cursor = db.rawQuery(sql, null)) {
            Logger.d(TAG, "Select executed successfully.");
        } catch (Exception e) {
            Logger.e(TAG, "Error executing SELECT command: " + sql, e);
        }
    }

    /**
     * Executes a command that modifies the database (INSERT, UPDATE, CREATE, DROP).
     *
     * @param db  the SQLiteDatabase instance
     * @param sql the command as a string
     */
    private static void executeModification(SQLiteDatabase db, String sql) {
        try {
            db.execSQL(sql);
            Logger.d(TAG, "Command executed successfully.");
        } catch (Exception e) {
            Logger.e(TAG, "Error executing command: " + sql, e);
        }
    }
}
