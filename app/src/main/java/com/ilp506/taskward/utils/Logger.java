package com.ilp506.taskward.utils;

import android.util.Log;

/**
 * Utility class to log messages to the console.
 *
 * This class provides methods to log debug, info, warning, and error messages.
 * The logs can be enabled or disabled based on the build configuration.
 */
public class Logger {

    private static final boolean ENABLE_LOGS = true;

    /**
     * Logs a debug message.
     *
     * @param tag The tag identifying the source of the log message.
     * @param message The message to be logged.
     */
    public static void d(String tag, String message) {
        if (ENABLE_LOGS) Log.d(tag, message);
    }

    /**
     * Logs an error message with an optional throwable.
     *
     * @param tag The tag identifying the source of the log message.
     * @param message The message to be logged.
     * @param throwable The optional throwable to be logged (can be null).
     */
    public static void e(String tag, String message, Throwable throwable) {
        if (ENABLE_LOGS) Log.e(tag, message, throwable);
    }

    /**
     * Logs an error message.
     *
     * @param tag The tag identifying the source of the log message.
     * @param message The message to be logged.
     */
    public static void e(String tag, String message) {
        if (ENABLE_LOGS) Log.e(tag, message);
    }

    /**
     * Logs an info message.
     *
     * @param tag The tag identifying the source of the log message.
     * @param message The message to be logged.
     */
    public static void i(String tag, String message) {
        if (ENABLE_LOGS) Log.i(tag, message);
    }

    /**
     * Logs a warning message.
     *
     * @param tag The tag identifying the source of the log message.
     * @param message The message to be logged.
     */
    public static void w(String tag, String message) {
        if (ENABLE_LOGS) Log.w(tag, message);
    }
}
