package com.ilp506.taskward.utils;

import android.util.Log;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Utility class for handling date and time conversions using java.time API.
 */
public class DateUtils {

    private static final String TAG = "DateUtils";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String SIMPLE_DATE_FORMAT = "dd/MM/yyyy";

    private DateUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Gets a DateTimeFormatter for the default format with the current locale.
     *
     * @return A DateTimeFormatter instance.
     */
    private static DateTimeFormatter getDefaultFormatter() {
        return DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT, Locale.getDefault());
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
            return LocalDateTime.parse(dateString, getDefaultFormatter());
        } catch (DateTimeParseException e) {
            Log.e(TAG, "Error parsing date string: " + dateString, e);
            return null;
        }
    }

    /**
     * Converts a LocalDateTime into a formatted string using the default format.
     *
     * @param dateTime The LocalDateTime to format.
     * @return A formatted date string, or null if the LocalDateTime is null.
     */
    public static String formatLocalDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return null;
        return dateTime.format(getDefaultFormatter());
    }

    /**
     * Converts a date string from the format dd/MM/yyyy to the default format yyyy-MM-dd HH:mm:ss.
     *
     * @param dateString The date string in dd/MM/yyyy format.
     * @return A formatted date string in yyyy-MM-dd HH:mm:ss format, or null if parsing fails.
     */
    public static LocalDateTime convertToDefaultFormat(String dateString) {
        if (dateString == null || dateString.isEmpty()) return null;

        try {
            LocalDate date = LocalDate.parse(
                    dateString,
                    DateTimeFormatter.ofPattern(SIMPLE_DATE_FORMAT, Locale.getDefault())
            );

            return parseLocalDateTime(date.atStartOfDay().format(getDefaultFormatter()));
        } catch (DateTimeParseException e) {
            Log.e(TAG, "Error converting date string: " + dateString, e);
            return null;
        }
    }
}
