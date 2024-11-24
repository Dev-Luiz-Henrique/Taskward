package com.ilp506.taskward.utils;

/**
 * A utility class to represent the result of an operation.
 *
 * @param <T> The type of the data being returned.
 */
public class OperationResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;

    private OperationResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    /**
     * Indicates if the operation was successful.
     *
     * @return true if successful, false otherwise.
     */
    public boolean isSuccessful() {
        return success;
    }

    /**
     * Gets the message associated with the operation.
     *
     * @return A string message providing additional context.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the data associated with the operation, if any.
     *
     * @return The data object or null if none.
     */
    public T getData() {
        return data;
    }

    /**
     * Creates a successful operation response with a message and data.
     *
     * @param message The success message.
     * @param data    The data associated with the success.
     * @param <T>     The type of the data.
     * @return An OperationResponse instance.
     */
    public static <T> OperationResponse<T> success(String message, T data) {
        return new OperationResponse<>(true, message, data);
    }

    /**
     * Creates a successful operation response with only a message.
     *
     * @param message The success message.
     * @param <T>     The type of the data.
     * @return An OperationResponse instance.
     */
    public static <T> OperationResponse<T> success(String message) {
        return new OperationResponse<>(true, message, null);
    }

    /**
     * Creates a failure operation response with a message.
     *
     * @param message The failure message.
     * @param <T>     The type of the data.
     * @return An OperationResponse instance.
     */
    public static <T> OperationResponse<T> failure(String message) {
        return new OperationResponse<>(false, message, null);
    }

    /**
     * Creates a failure operation response with a message and additional data.
     *
     * @param message The failure message.
     * @param data    The data associated with the failure (optional).
     * @param <T>     The type of the data.
     * @return An OperationResponse instance.
     */
    public static <T> OperationResponse<T> failure(String message, T data) {
        return new OperationResponse<>(false, message, data);
    }
}
