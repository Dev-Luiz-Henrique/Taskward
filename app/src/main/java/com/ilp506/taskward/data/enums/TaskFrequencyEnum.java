package com.ilp506.taskward.data.enums;

import androidx.annotation.NonNull;

/**
 * This enum represents the frequency of a task.
 * It has four possible values: DAILY, WEEKLY, MONTHLY, and YEARLY.
 */
public enum TaskFrequencyEnum {
    DAILY("daily"),
    WEEKLY("weekly"),
    MONTHLY("monthly"),
    YEARLY("yearly");

    private final String value;

    /**
     * Constructor to initialize the enum with a string value
     */
    TaskFrequencyEnum(String value) {
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
     * @return The string value of the enum (e.g., "daily", "weekly", "monthly", "yearly").
     */
    @NonNull
    @Override
    public String toString() {
        return value;
    }

    /**
     * Converts a string to its corresponding TaskFrequencyEnum constant.
     * This method is useful when retrieving a frequency from the database (as a string)
     * and converting it back into the appropriate enum constant.
     *
     * @param value The string value to be converted into an enum constant.
     * @return The corresponding TaskFrequencyEnum constant (e.g., TaskFrequencyEnum.DAILY).
     * @throws IllegalArgumentException if the provided string does not match any of the enum values.
     */
    public static TaskFrequencyEnum fromString(String value) {
        for (TaskFrequencyEnum frequency : TaskFrequencyEnum.values()) {
            if (frequency.value.equalsIgnoreCase(value)) return frequency;
        }
        throw new IllegalArgumentException("Unknown frequency: " + value);
    }
}
