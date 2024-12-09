package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.TaskEventController;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.ui.MainActivity;
import com.ilp506.taskward.ui.adapters.TaskEventAdapter;
import com.ilp506.taskward.utils.Logger;
import com.ilp506.taskward.utils.NavigationHelper;
import com.ilp506.taskward.utils.OperationResponse;

import java.util.List;

public class TasksFragment extends Fragment {
    private TaskEventController taskEventController;
    private NavigationHelper navigationHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        taskEventController = new TaskEventController(requireContext());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        setupComponents(view);
        loadTaskEvents(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupNavigationHelper(view);
    }

    /**
     * Sets up the components for the fragment.
     * @param view The view of the fragment
     */
    private void setupComponents(@NonNull View view) {
        TextView headerTitle = view.findViewById(R.id.fragmentTitle);
        headerTitle.setText(R.string.all_tasks_title);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(divider);
    }

    /**
     * Loads the task events and sets up the task adapter.
     * @param view The view of the fragment
     */
    private void loadTaskEvents(@NonNull View view) {
        OperationResponse<List<TaskEvent>> response = taskEventController.getAllTaskEvents();
        List<TaskEvent> taskEvents = response.getData();

        if (response.isSuccessful()) {
            TaskEventAdapter adapter = new TaskEventAdapter(taskEvents, this::onTaskStatusChanged);
            RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
            recyclerView.setAdapter(adapter);
        } else
            Toast.makeText(requireContext(), "Error loading tasks", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles the task status change event.
     * @param taskEvent The task event
     * @param isChecked Whether the task is checked
     */
    private void onTaskStatusChanged(TaskEvent taskEvent, boolean isChecked) {
        OperationResponse<Void> taskEventResponse = isChecked
                ? taskEventController.completeTaskEvent(taskEvent.getId())
                : taskEventController.revertTaskEventCompletion(taskEvent.getId());

        if (taskEventResponse.isSuccessful())
            updateUserPoints(taskEvent.getUserId());
        else {
            Toast.makeText(requireContext(), "Error updating task status", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Updates the user points based on the task event.
     * @param userId The user ID
     */
    private void updateUserPoints(int userId) {
        UserController userController = new UserController(requireContext());
        OperationResponse<User> userResponse = userController.getUserById(userId);

        if (userResponse.isSuccessful())
            navigationHelper.updatePoints(userResponse.getData().getPoints());
        else
            Toast.makeText(requireContext(), "Error updating points", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets up the navigation helper for the fragment.
     * @param view The view of the fragment
     */
    private void setupNavigationHelper(@NonNull View view) {
        MainActivity activity = (MainActivity) requireActivity();
        LiveData<NavigationHelper> helperLiveData = activity.getNavigationHelperLiveData();

        helperLiveData.observe(getViewLifecycleOwner(), helper -> {
            if (helper != null) {
                navigationHelper = helper;
                Button createTaskButton = view.findViewById(R.id.createTaskButton);
                createTaskButton.setOnClickListener(v ->
                        navigationHelper.navigateTo(R.id.action_tasksFragment_to_createTaskFragment)
                );
            }
        });
    }
}
