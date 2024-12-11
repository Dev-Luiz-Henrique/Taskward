package com.ilp506.taskward.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilp506.taskward.R;
import com.ilp506.taskward.data.models.Task;

import java.util.List;

/**
 * Adapter for displaying tasks in the TaskWard app.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>  {
    private static final String TAG = TaskAdapter.class.getSimpleName();
    private final List<Task> tasks;

    /**
     * Constructor for the TaskAdapter.
     * This method initializes the list of tasks to be displayed.
     *
     * @param tasks The list of tasks to be displayed
     */
    public TaskAdapter(List<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Creates and returns a new ViewHolder to display a task.
     * This method inflates the layout layout for an individual task item and wraps it in a TaskViewHolder.
     *
     * @param parent The parent ViewGroup to which the new View will be attached.
     * @param viewType The view type of the new View.
     * @return A new instance of TaskViewHolder containing the inflated View.
     */
    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    /**
     * Binds the data for a specific position to the ViewHolder.
     * This method updates the contents of the ViewHolder to reflect the data at the given position in the dataset.
     *
     * @param holder The ViewHolder that should be updated.
     * @param position The position of the item in the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task);
    }

    /**
     * Returns the total number of tasks in the adapter.
     * This method provides the total number of items in the dataset, which is required by RecyclerView.
     *
     * @return The total number of tasks in the list.
     */
    @Override
    public int getItemCount() {
        return tasks.size();
    }

    /**
     * ViewHolder class for tasks items.
     * This inner class holds the views for each item and provides methods to bind data to these views.
     */
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView taskEventsDone;

        /**
         * Constructor for the TaskViewHolder.
         * This method initializes the views for an individual task item.
         *
         * @param itemView The root View for the task item.
         */
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.statisticsTaskTitle);
            taskEventsDone = itemView.findViewById(R.id.statisticsTaskEventsDone);
        }

        /**
         * Binds the task data to the views.
         * This method populates the TextViews with data from a given Task.
         *
         * @param task The Task object containing the data to bind.
         */
        public void bind(@NonNull Task task) {
            title.setText(task.getTitle());
            taskEventsDone.setText(String.valueOf(task.getCompletedEvents().size()));
        }
    }
}
