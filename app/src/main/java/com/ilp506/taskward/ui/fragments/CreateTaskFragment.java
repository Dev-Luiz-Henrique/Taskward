package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.slider.Slider;
import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.TaskController;
import com.ilp506.taskward.data.enums.TaskFrequencyEnum;
import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.utils.DateUtils;
import com.ilp506.taskward.utils.OperationResponse;
import com.ilp506.taskward.utils.UIUtils;

import java.util.Arrays;

/**
 * Fragment for creating tasks in the TaskWard app.
 * Handles user input, validations, and task creation logic.
 */
public class CreateTaskFragment extends Fragment {

    // Controller responsible for task-related operations
    private TaskController taskController;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        taskController = new TaskController(requireContext());

        setupTaskFrequencySpinner(view);
        setupDatePicker(view, R.id.editTaskStartDate, "Select Start Date");
        setupDatePicker(view, R.id.editTaskEndDate, "Select End Date");
        setupAlwaysRepeatCheckbox(view);
        setupCreateTaskButton(view);

        return view;
    }

    /**
     * Sets up the create task button and its click listener.
     * Validates user inputs and triggers task creation.
     *
     * @param view The root view of the fragment.
     */
    private void setupCreateTaskButton(@NonNull View view) {
        ImageButton buttonCreateTask = view.findViewById(R.id.buttonCreateTask);

        buttonCreateTask.setOnClickListener(v -> {
            EditText editTaskTitle = view.findViewById(R.id.editTaskTitle);
            EditText editTaskDescription = view.findViewById(R.id.editTaskDescription);
            Spinner spinnerTaskFrequency = view.findViewById(R.id.spinnerTaskFrequency);
            EditText editTaskFrequencyValue = view.findViewById(R.id.editTaskFrequencyValue);
            EditText editTaskStartDate = view.findViewById(R.id.editTaskStartDate);
            EditText editTaskEndDate = view.findViewById(R.id.editTaskEndDate);
            CheckBox checkTaskFrequencyRepeat = view.findViewById(R.id.checkTaskFrequencyRepeat);
            Slider sliderTaskDifficulty = view.findViewById(R.id.sliderTaskDifficulty);

            String title = editTaskTitle.getText().toString().trim();
            String description = editTaskDescription.getText().toString().trim();
            String frequencyValue = editTaskFrequencyValue.getText().toString().trim();
            String startDate = editTaskStartDate.getText().toString().trim();
            String endDate = editTaskEndDate.getText().toString().trim();
            boolean alwaysRepeat = checkTaskFrequencyRepeat.isChecked();
            float difficulty = sliderTaskDifficulty.getValue();

            int frequencyPosition = spinnerTaskFrequency.getSelectedItemPosition();
            TaskFrequencyEnum frequency = TaskFrequencyEnum.values()[frequencyPosition];

            if (TextUtils.isEmpty(title)) {
                Toast.makeText(requireContext(), "Task title is required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(frequencyValue) || Integer.parseInt(frequencyValue) <= 0) {
                Toast.makeText(requireContext(), "Frequency value must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(startDate)) {
                Toast.makeText(requireContext(), "Start date is required", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!alwaysRepeat && TextUtils.isEmpty(endDate)) {
                Toast.makeText(requireContext(), "End date is required when not repeating", Toast.LENGTH_SHORT).show();
                return;
            }

            Task task = new Task();
            task.setTitle(title);
            task.setIcon("ic_task"); // TODO Implement icon selection
            task.setDescription(description);
            task.setFrequency(frequency);
            task.setFrequencyInterval(Integer.parseInt(frequencyValue));
            task.setStartDate(DateUtils.convertToDefaultFormat(startDate));
            task.setEndDate(alwaysRepeat ? null : DateUtils.convertToDefaultFormat(endDate));
            task.setPointsReward((int) difficulty); // TODO Implement points reward calculation logic

            handleTaskCreation(task);
        });
    }

    /**
     * Handles the task creation process, including feedback to the user.
     * @param task The task object to be created.
     */
    private void handleTaskCreation(Task task) {
        OperationResponse<Task> response = taskController.createTask(task);

        if (response.isSuccessful()) {
            Toast.makeText(requireContext(), "Task created successfully!", Toast.LENGTH_SHORT).show();
            // TODO: Implement navigation to task list or details screen
        } else {
            Toast.makeText(requireContext(), "Failed to create task. Try again.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Configures a MaterialDatePicker for a specific EditText field.
     *
     * @param view The View containing the EditText.
     * @param editTextId The ID of the EditText to configure.
     * @param title The title to display in the date picker.
     */
    private void setupDatePicker(@NonNull View view, int editTextId, String title) {
        EditText editText = view.findViewById(editTextId);
        UIUtils.setupDatePicker(editText, getParentFragmentManager(), title);
    }

    /**
     * Configures the Task Frequency Spinner with values from TaskFrequencyEnum.
     *
     * @param view The View containing the Spinner.
     */
    private void setupTaskFrequencySpinner(@NonNull View view) {
        Spinner spinnerTaskFrequency = view.findViewById(R.id.spinnerTaskFrequency);
        TextView textLabelTaskFrequencyValue = view.findViewById(R.id.textLabelTaskFrequencyValue);

        TaskFrequencyEnum[] options = TaskFrequencyEnum.values();
        String[] optionValues = Arrays.stream(options)
                .map(TaskFrequencyEnum::getValue)
                .toArray(String[]::new);

        UIUtils.setupSpinner(
                spinnerTaskFrequency,
                requireContext(),
                options,
                optionValues,
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View selectedView, int position, long id) {
                        onTaskFrequencySelected(parent, selectedView, position,
                                id, textLabelTaskFrequencyValue, options);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // No action needed
                    }
                }
        );
    }

    /**
     * Callback method for handling task frequency selection.
     *
     * @param parent The AdapterView where the selection happened.
     * @param selectedView The view within the AdapterView that was selected.
     * @param position The position of the selected item.
     * @param id The row id of the selected item.
     * @param textLabelTaskFrequencyValue The TextView to display the frequency value.
     * @param options The TaskFrequencyEnum options.
     */
    private void onTaskFrequencySelected(AdapterView<?> parent, View selectedView, int position, long id,
                                         TextView textLabelTaskFrequencyValue, @NonNull TaskFrequencyEnum[] options) {
        TaskFrequencyEnum selectedFrequency = options[position];
        String frequencyText;

        switch (selectedFrequency) {
            case DAILY: frequencyText = "day(s)"; break;
            case WEEKLY: frequencyText = "week(s)"; break;
            case MONTHLY: frequencyText = "month(s)"; break;
            case YEARLY: frequencyText = "year(s)"; break;
            default: frequencyText = "";
        }
        textLabelTaskFrequencyValue.setText(frequencyText);
    }

    /**
     * Configures the checkbox behavior to show/hide the end date layout.
     *
     * @param view The View containing the checkbox and layout.
     */
    private void setupAlwaysRepeatCheckbox(@NonNull View view) {
        CheckBox checkTaskFrequencyRepeat = view.findViewById(R.id.checkTaskFrequencyRepeat);
        ConstraintLayout layoutTaskEndDate = view.findViewById(R.id.layoutTaskEndDate);

        checkTaskFrequencyRepeat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layoutTaskEndDate.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });
    }
}
