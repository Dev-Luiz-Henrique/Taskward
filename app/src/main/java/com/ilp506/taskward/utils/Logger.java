package com.ilp506.taskward.utils;

import android.util.Log;

public class Logger {

    private static final boolean ENABLE_LOGS = true;

    public static void d(String tag, String message) {
        if (ENABLE_LOGS) Log.d(tag, message);
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (ENABLE_LOGS) Log.e(tag, message, throwable);
    }

    public static void i(String tag, String message) {
        if (ENABLE_LOGS) Log.i(tag, message);
    }

    public static void w(String tag, String message) {
        if (ENABLE_LOGS) Log.w(tag, message);
    }
}
