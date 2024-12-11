package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
 * This fragment allows users to create a new task by providing task details such as title, description, frequency, and dates.
 * It interacts with the TaskController to handle the task creation process and updates the UI accordingly.
 */
public class CreateTaskFragment extends Fragment {
    private TaskController taskController;

    private EditText editTaskTitle, editTaskDescription,
            editTaskFrequencyValue, editTaskStartDate, editTaskEndDate;
    private CheckBox checkTaskFrequencyRepeat;
    private Slider sliderTaskDifficulty;
    private Spinner spinnerTaskFrequency;
    private TextView textLabelTaskFrequencyValue;
    private ImageButton btnCreateTask;

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskController = new TaskController(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_task, container, false);

        initializeUI(view);
        setupComponents(view);

        return view;
    }

    /**
     * Initializes the UI components of the fragment.
     *
     * @param view The View of the fragment.
     */
    private void initializeUI(@NonNull View view) {
        editTaskTitle = view.findViewById(R.id.editTaskTitle);
        editTaskDescription = view.findViewById(R.id.editTaskDescription);
        editTaskFrequencyValue = view.findViewById(R.id.editTaskFrequencyValue);
        editTaskStartDate = view.findViewById(R.id.editTaskStartDate);
        editTaskEndDate = view.findViewById(R.id.editTaskEndDate);
        checkTaskFrequencyRepeat = view.findViewById(R.id.checkTaskFrequencyRepeat);
        sliderTaskDifficulty = view.findViewById(R.id.sliderTaskDifficulty);
        spinnerTaskFrequency = view.findViewById(R.id.spinnerTaskFrequency);
        textLabelTaskFrequencyValue = view.findViewById(R.id.textLabelTaskFrequencyValue);
        btnCreateTask = view.findViewById(R.id.buttonCreateTask);
    }

    /**
     * Sets up the components of the fragment.
     *
     * @param view The View of the fragment.
     */
    private void setupComponents(@NonNull View view){
        UIUtils.setupDatePicker(editTaskStartDate, getParentFragmentManager(), "Select Start Date");
        UIUtils.setupDatePicker(editTaskEndDate, getParentFragmentManager(), "Select End Date");

        ConstraintLayout layoutTaskEndDate = view.findViewById(R.id.layoutTaskEndDate);
        checkTaskFrequencyRepeat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            layoutTaskEndDate.setVisibility(isChecked ? View.GONE : View.VISIBLE);
        });

        btnCreateTask.setOnClickListener(v -> {
            if (validateTaskInputs()) {
                Task task = buildTaskFromInputs();
                handleTaskCreation(task);
            }
        });

        setupTaskFrequencySpinner();
    }

    /**
     * Configures the Task Frequency Spinner with values from TaskFrequencyEnum.
     */
    private void setupTaskFrequencySpinner() {
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
                        onTaskFrequencySelected(position, options);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // No action needed
                    }
                }
        );
    }

    /**
     * Handles the selected frequency in the Task Frequency Spinner.
     *
     * @param position The position of the selected item
     * @param options The array of TaskFrequencyEnum options
     */
    private void onTaskFrequencySelected(int position, @NonNull TaskFrequencyEnum[] options) {
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
     * Validates the task inputs before creating a new task.
     *
     * @return true if inputs are valid, false otherwise.
     */
    private boolean validateTaskInputs() {
        String title = editTaskTitle.getText().toString().trim();
        String frequencyValue = editTaskFrequencyValue.getText().toString().trim();
        String startDate = editTaskStartDate.getText().toString().trim();
        String endDate = editTaskEndDate.getText().toString().trim();
        boolean alwaysRepeat = checkTaskFrequencyRepeat.isChecked();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(requireContext(), "Title is required", Toast.LENGTH_SHORT).show();
            editTaskTitle.setError("Title is required");
            return false;
        }
        if (TextUtils.isEmpty(frequencyValue) || Integer.parseInt(frequencyValue) <= 0) {
            Toast.makeText(requireContext(), "Frequency value must be greater than 0", Toast.LENGTH_SHORT).show();
            editTaskFrequencyValue.setError("Frequency value must be greater than 0");
            return false;
        }
        if (TextUtils.isEmpty(startDate)) {
            Toast.makeText(requireContext(), "Start date is required", Toast.LENGTH_SHORT).show();
            editTaskStartDate.setError("Start date is required");
            return false;
        }
        if (!alwaysRepeat && TextUtils.isEmpty(endDate)) {
            Toast.makeText(requireContext(), "End date is required", Toast.LENGTH_SHORT).show();
            editTaskEndDate.setError("End date is required");
            return false;
        }
        return true;
    }

    /**
     * Builds a Task object from the user inputs.
     *
     * @return The Task object.
     */
    @NonNull
    private Task buildTaskFromInputs() {
        String title = editTaskTitle.getText().toString().trim();
        String description = editTaskDescription.getText().toString().trim();
        String frequencyValue = editTaskFrequencyValue.getText().toString().trim();
        String startDate = editTaskStartDate.getText().toString().trim();
        String endDate = editTaskEndDate.getText().toString().trim();
        boolean alwaysRepeat = checkTaskFrequencyRepeat.isChecked();
        float difficulty = sliderTaskDifficulty.getValue();

        TaskFrequencyEnum frequency = TaskFrequencyEnum.values()[spinnerTaskFrequency.getSelectedItemPosition()];

        Task task = new Task();
        task.setTitle(title);
        task.setIcon("ic_task"); // TODO implement icon selection logic
        task.setDescription(description);
        task.setFrequency(frequency);
        task.setFrequencyInterval(Integer.parseInt(frequencyValue));
        task.setStartDate(DateUtils.convertToDefaultFormat(startDate));
        task.setEndDate(alwaysRepeat ? null : DateUtils.convertToDefaultFormat(endDate));
        task.setPointsReward(((int) difficulty) * 20); // TODO implement logic for points reward calculation
        return task;
    }

    /**
     * Handles the task creation process, including feedback to the user.
     *
     * @param task The task object to be created.
     */
    private void handleTaskCreation(Task task) {
        OperationResponse<Task> response = taskController.createTask(task);
        if (response.isSuccessful()) {
            Toast.makeText(requireContext(), "Task created successfully!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }
        else
            Toast.makeText(requireContext(), "Failed to create task. Try again.", Toast.LENGTH_SHORT).show();
    }
}
