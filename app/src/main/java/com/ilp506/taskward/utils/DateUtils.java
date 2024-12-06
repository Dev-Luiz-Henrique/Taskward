package com.ilp506.taskward.utils;

import android.util.Log;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Utility class for handling date and time conversions using java.time API.
 */
public class DateUtils {

    private static final String TAG = "DateUtils";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    private DateUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Parses a string into a LocalDateTime using the default date format.
     *
     * @param dateString The date string to parse.
     * @return A LocalDateTime object if parsing succeeds, or null if it fails.
     */
    public static LocalDateTime parseLocalDateTime(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;

        try {
            return LocalDateTime.parse(dateString, DEFAULT_FORMATTER);
        } catch (Exception e) {
            Log.e(TAG, "Error parsing date string: " + dateString, e);
            return null;
        }
    }

    /**
     * Converts a LocalDateTime into a formatted string using the default date format.
     *
     * @param dateTime The LocalDateTime to format.
     * @return A formatted date string, or null if the LocalDateTime is null.
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(DEFAULT_FORMATTER);
    }

    /**
     * Gets the current date and time as a formatted string.
     *
     * @return The current date and time as a string.
     */
    public static String getCurrentTimestampString() {
        return LocalDateTime.now().format(DEFAULT_FORMATTER);
    }

    /**
     * Converts a LocalDateTime to a timestamp in milliseconds for database storage.
     *
     * @param dateTime The LocalDateTime to convert.
     * @return A long value representing the timestamp in milliseconds, or null if dateTime is null.
     */
    public static Long toEpochMillis(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    /**
     * Converts a timestamp in milliseconds back to a LocalDateTime.
     *
     * @param epochMillis The timestamp in milliseconds.
     * @return A LocalDateTime object representing the timestamp, or null if epochMillis is null.
     */
    public static LocalDateTime fromEpochMillis(Long epochMillis) {
        if (epochMillis == null) return null;
        return LocalDateTime.ofInstant(
                java.time.Instant.ofEpochMilli(epochMillis),
                ZoneId.systemDefault()
        );
    }
}
