package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.TaskEventController;
import com.ilp506.taskward.data.models.TaskEvent;
import com.ilp506.taskward.ui.adapters.TaskEventAdapter;
import com.ilp506.taskward.utils.OperationResponse;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    public TasksFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Handle arguments if needed
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tasks, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        TaskEventController taskEventController = new TaskEventController(requireContext());
        OperationResponse<List<TaskEvent>> response = taskEventController.getAllTaskEvents();

        if (response.isSuccessful()) {
            List<TaskEvent> taskEvents = response.getData();
            TaskEventAdapter adapter = new TaskEventAdapter(taskEvents, taskEvent -> {
                onTaskEventClick(taskEvent);
            });
            recyclerView.setAdapter(adapter);
        }
        else {
            Toast.makeText(requireContext(), "Error fetching task events", Toast.LENGTH_SHORT).show();
            System.out.println("Error fetching task events: " + response.getMessage());
        }

        return view;
    }

    private void onTaskEventClick(@NonNull TaskEvent taskEvent) {
        Toast.makeText(requireContext(), "Clicked on task: " + taskEvent.getTaskId(), Toast.LENGTH_SHORT).show();
        System.out.println("Clicked on task: " + taskEvent.getTaskId());
    }
}
