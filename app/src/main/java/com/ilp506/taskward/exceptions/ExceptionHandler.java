package com.ilp506.taskward.exceptions;

import androidx.annotation.NonNull;

import com.ilp506.taskward.utils.Logger;
import com.ilp506.taskward.utils.OperationResponse;

/**
 * ExceptionHandler is responsible for handling various exceptions throughout the application.
 * It provides a mechanism to log exceptions and notify users through a registered ErrorNotifier.
 */
public class ExceptionHandler {

    private static final String TAG = ExceptionHandler.class.getSimpleName();

    private static ExceptionHandler instance;
    private ErrorNotifier errorNotifier;

    /**
     * Private constructor to enforce Singleton pattern.
     */
    private ExceptionHandler() {}

    /**
     * Returns the singleton instance of ExceptionHandler.
     *
     * @return the singleton instance of ExceptionHandler.
     */
    public static synchronized ExceptionHandler getInstance() {
        if (instance == null)
            instance = new ExceptionHandler();
        return instance;
    }

    /**
     * Sets the ErrorNotifier to be used for notifying user-facing error messages.
     *
     * @param errorNotifier the ErrorNotifier instance.
     */
    public void setErrorNotifier(@NonNull ErrorNotifier errorNotifier) {
        this.errorNotifier = errorNotifier;
    }

    /**
     * Handles the provided exception and generates an appropriate OperationResponse.
     *
     * @param e the exception to handle.
     * @param userMessage a user-friendly message describing the error.
     * @param <T> the type of the result associated with the operation.
     * @return the OperationResponse indicating failure along with the userMessage.
     */
    public <T> OperationResponse<T> handleException(Exception e, String userMessage) {
        logException(e, userMessage);
        if (errorNotifier != null)
            errorNotifier.notify(userMessage);

        return OperationResponse.failure(userMessage);
    }

    /**
     * Logs the exception based on its type and message.
     *
     * @param e the exception to log.
     * @param userMessage a user-friendly message describing the error.
     */
    private void logException(Exception e, String userMessage) {
        if (e instanceof DatabaseOperationException) {
            Logger.e(TAG, "Database error: " + e.getMessage(), e);
        } else if (e instanceof IllegalArgumentException) {
            Logger.e(TAG, "Invalid input: " + e.getMessage(), e);
        } else if (e instanceof RuntimeException) {
            Logger.e(TAG, "Runtime error: " + e.getMessage(), e);
        } else {
            Logger.e(TAG, "Unexpected error: " + e.getMessage(), e);
        }
    }

    /**
     * Interface for notifying user-facing errors.
     */
    public interface ErrorNotifier {
        void notify(String message);
    }
}
