package com.ilp506.taskward.utils;

import android.util.Log;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for handling date and time conversions.
 */
public class DateUtils {

    private static final String TAG = "DateUtils";
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private DateUtils() {
        // Private constructor to prevent instantiation
    }

    /**
     * Parses a string into a Timestamp using the default date format.
     *
     * @param dateString The date string to parse.
     * @return A Timestamp object if parsing succeeds, or null if it fails.
     */
    public static Timestamp parseTimestamp(String dateString) {
        if (dateString == null || dateString.isEmpty())
            return null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
            Date parsedDate = dateFormat.parse(dateString);
            return (parsedDate != null) ? new Timestamp(parsedDate.getTime()) : null;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing date string: " + dateString, e);
            return null;
        }
    }

    /**
     * Converts a Timestamp into a formatted string using the default date format.
     *
     * @param timestamp The Timestamp to format.
     * @return A formatted date string, or null if the Timestamp is null.
     */
    public static String formatTimestamp(Timestamp timestamp) {
        if (timestamp == null)
            return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(new Date(timestamp.getTime()));
    }

    /**
     * Gets the current date and time as a formatted string.
     *
     * @return The current date and time as a string.
     */
    public static String getCurrentTimestampString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());
        return dateFormat.format(new Date());
    }
}
