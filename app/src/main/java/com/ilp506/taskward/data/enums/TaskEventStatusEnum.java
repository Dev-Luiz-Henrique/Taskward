package com.ilp506.taskward.data.enums;

import androidx.annotation.NonNull;

/**
 * This enum represents the status of a task event.
 * It has three possible values: SCHEDULED, COMPLETED, and CANCELLED.
 */
public enum TaskEventStatusEnum {
    SCHEDULED("scheduled"),
    COMPLETED("completed"),
    CANCELLED("cancelled");

    private final String value;

    /**
     * Constructor to initialize the enum with a string value
     */
    TaskEventStatusEnum(String value) {
        this.value = value;
    }

    /**
     * Getter method to retrieve the string value associated with the enum constant
     */
    public String getValue() {
        return value;
    }

    /**
     * Overriding the toString() method to return the string value of the enum.
     * This is used when printing or logging the enum instance.
     *
     * @return The string value of the enum (e.g., "scheduled", "completed", or "cancelled").
     */
    @NonNull
    @Override
    public String toString() {
        return value;
    }

    /**
     * Converts a string to its corresponding TaskEventStatusEnum constant.
     * This method is useful when retrieving a status from the database (as a string)
     * and converting it back into the appropriate enum constant.
     *
     * @param value The string value to be converted into an enum constant.
     * @return The corresponding TaskEventStatusEnum constant (e.g., TaskEventStatusEnum.SCHEDULED).
     * @throws IllegalArgumentException if the provided string does not match any of the enum values.
     */
    public static TaskEventStatusEnum fromString(String value) {
        for (TaskEventStatusEnum status : TaskEventStatusEnum.values()) {
            if (status.value.equalsIgnoreCase(value)) return status;
        }
        throw new IllegalArgumentException("Unknown status: " + value);
    }
}
