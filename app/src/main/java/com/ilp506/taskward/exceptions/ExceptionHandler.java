package com.ilp506.taskward.exceptions;

import androidx.annotation.NonNull;

import com.ilp506.taskward.utils.Logger;

public class ExceptionHandler {

    private static final String TAG = ExceptionHandler.class.getSimpleName();

    /**
     * Handles an exception by logging its details and reporting to external services.
     *
     * @param e The exception to handle.
     */
    public static void handleException(@NonNull Exception e) {
        Logger.e(TAG, e.getMessage(), e);
    }

    /**
     * Handles an unknown error.
     *
     * @param message A custom message describing the error.
     */
    public static void handleUnknownError(@NonNull String message) {
        Logger.e(TAG, "Unknown error occurred: " + message);
    }
}
