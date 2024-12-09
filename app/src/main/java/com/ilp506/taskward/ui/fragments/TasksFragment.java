package com.ilp506.taskward.ui.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.TaskEventController;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.ui.MainActivity;
import com.ilp506.taskward.ui.adapters.TaskEventAdapter;
import com.ilp506.taskward.utils.Logger;
import com.ilp506.taskward.utils.NavigationHelper;
import com.ilp506.taskward.utils.OperationResponse;

import java.util.List;

public class TasksFragment extends Fragment {
    private static final String TAG = TasksFragment.class.getSimpleName();

    private TaskEventController taskEventController;

    private NavigationHelper navigationHelper;

    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        TextView headerTitle = view.findViewById(R.id.fragmentTitle);
        headerTitle.setText("all");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        DividerItemDecoration divider = new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(divider);

        taskEventController = new TaskEventController(requireContext());
        OperationResponse<List<TaskEvent>> response = taskEventController.getAllTaskEvents();

        if (response.isSuccessful()) {
            List<TaskEvent> taskEvents = response.getData();
            TaskEventAdapter adapter = new TaskEventAdapter(taskEvents, this::onTaskStatusChanged);
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(requireContext(), "Error fetching task events", Toast.LENGTH_SHORT).show();
            System.out.println("Error fetching task events: " + response.getMessage());
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navigationHelper = ((MainActivity) requireActivity()).getNavigationHelper();

        MainActivity activity = (MainActivity) requireActivity();
        activity.getNavigationHelperLiveData().observe(getViewLifecycleOwner(), helper -> {
            if (helper != null) {
                navigationHelper = helper;
                setupButtonClickListener(view);
            }
        });
    }

    private void onTaskStatusChanged(TaskEvent taskEvent, boolean isChecked) {
        OperationResponse<Void> taskEventResponse;

        if (isChecked)
            taskEventResponse = taskEventController.completeTaskEvent(taskEvent.getId());
        else
            taskEventResponse = taskEventController.revertTaskEventCompletion(taskEvent.getId());

        if (taskEventResponse.isSuccessful()) {
            UserController userController = new UserController(requireContext());
            OperationResponse<User> userResponse = userController.getUserById(taskEvent.getUserId());

            if (userResponse.isSuccessful()) {
                User user = userResponse.getData();
                navigationHelper.updatePoints(user.getPoints());
            }
            else {
                Toast.makeText(requireContext(), "Error fetching updated user points", Toast.LENGTH_SHORT).show();
                Logger.e("TasksFragment", "Error fetching user: " + userResponse.getMessage());
            }
        }
        else {
            Toast.makeText(requireContext(), "Error updating task event", Toast.LENGTH_SHORT).show();
            Logger.e("TasksFragment", "Error updating task event: " + taskEventResponse.getMessage());
        }
    }

    private void setupButtonClickListener(@NonNull View view) {
        Button createTaskButton = view.findViewById(R.id.createTaskButton);
        createTaskButton.setOnClickListener(v -> navigationHelper.navigateTo(R.id.action_tasksFragment_to_createTaskFragment));
    }
}
