package com.ilp506.taskward.utils;

import android.content.Context;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.ilp506.taskward.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility class for UI-related operations.
 */
public class UIUtils {

    /**
     * Configures a MaterialDatePicker for an EditText.
     *
     * @param editText The EditText to attach the date picker to.
     * @param fragmentManager The FragmentManager to use for showing the date picker.
     * @param title The title to display in the date picker.
     */
    public static void setupDatePicker(@NonNull EditText editText,
                                       @NonNull FragmentManager fragmentManager,
                                       @NonNull String title) {
        editText.setOnClickListener(v -> {
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText(title)
                    .build();

            datePicker.show(fragmentManager, title);

            datePicker.addOnPositiveButtonClickListener(selection -> {
                String formattedDate = formatDate(selection);
                editText.setText(formattedDate);
            });
        });
    }

    /**
     * Configures a Spinner with options and a callback.
     * @param spinner The Spinner to configure.
     * @param context The Context to use.
     * @param options The options to display in the Spinner.
     * @param displayValues The values to display for each option.
     * @param listener The callback to invoke when an option is selected.
     */
    public static <T> void setupSpinner(@NonNull Spinner spinner,
                                        @NonNull Context context,
                                        @NonNull T[] options,
                                        @NonNull String[] displayValues,
                                        @NonNull AdapterView.OnItemSelectedListener listener) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                context,
                R.layout.custom_spinner_item,
                displayValues
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);
    }

    // TODO review move to DateUtils
    /**
     * Formats a timestamp into a date string in the format "dd/MM/yyyy".
     */
    @NonNull
    private static String formatDate(long timestamp) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return format.format(new Date(timestamp));
    }
}
