package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.TaskController;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.models.Task;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.ui.adapters.TaskAdapter;
import com.ilp506.taskward.utils.Logger;
import com.ilp506.taskward.utils.OperationResponse;

import java.util.List;

/**
 * Fragment for displaying user profiles in the TaskWard app.
 */
public class ProfileFragment extends Fragment {
    private UserController userController; // TODO Implement logic for user profile data view
    private TaskController taskController;

    private TextView headerTitle;
    private RecyclerView recyclerView;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userController = new UserController(requireContext());
        taskController = new TaskController(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initializeUI(view);
        setupComponents();
        loadTasks();
        return view;
    }

    /**
     * Initializes the UI components of the fragment.
     *
     * @param view The view of the fragment
     */
    private void initializeUI(@NonNull View view) {
        headerTitle = view.findViewById(R.id.fragmentTitle);
        recyclerView = view.findViewById(R.id.recyclerViewTasks);
    }

    /**
     * Sets up the components of the fragment.
     */
    private void setupComponents() {
        headerTitle.setText(R.string.profile_statistics_header);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    /**
     * Loads the task events and sets up the task adapter.
     */
    private void loadTasks() {
        OperationResponse<List<Task>> response = taskController.getAllTasksWithTaskEvents();

        if (response.isSuccessful()) {
            List<Task> tasks = response.getData();
            TaskAdapter adapter = new TaskAdapter(tasks);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(requireContext(), "Error loading tasks", Toast.LENGTH_SHORT).show();
    }
}