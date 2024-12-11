package com.ilp506.taskward.ui.adapters;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ilp506.taskward.R;
import com.ilp506.taskward.data.models.TaskEvent;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Adapter for displaying task events in the TaskWard app.
 */
public class TaskEventAdapter extends RecyclerView.Adapter<TaskEventAdapter.TaskEventViewHolder> {
    private static final String TAG = TaskEventAdapter.class.getSimpleName();

    private final List<TaskEvent> taskEvents;
    private final OnTaskStatusChangeListener statusChangeListener;

    /**
     * Interface for handling task status change events.
     */
    public interface OnTaskStatusChangeListener {
        void onTaskStatusChanged(TaskEvent taskEvent, boolean isChecked);
    }

    /**
     * Constructor for the TaskEventAdapter.
     * This method initializes the adapter with a list of task events and a listener for task status changes.
     *
     * @param taskEvents The list of task events to be displayed
     * @param statusChangeListener The listener for task status change events
     */
    public TaskEventAdapter(List<TaskEvent> taskEvents, OnTaskStatusChangeListener statusChangeListener) {
        this.taskEvents = taskEvents;
        this.statusChangeListener = statusChangeListener;
    }

    /**
     * Creates and returns a new ViewHolder to display a task event.
     * This method inflates the layout for an individual task event item and wraps it in a TaskEventViewHolder.
     *
     * @param parent The parent ViewGroup to which the new View will be attached.
     * @param viewType The view type of the new View.
     * @return A new instance of TaskEventViewHolder containing the inflated View.
     */
    @NonNull
    @Override
    public TaskEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_event_item, parent, false);
        return new TaskEventViewHolder(view);
    }

    /**
     * Binds the data for a specific position to the ViewHolder.
     * This method updates the contents of the ViewHolder to reflect the data at the given position in the dataset.
     *
     * @param holder The ViewHolder that should be updated.
     * @param position The position of the item in the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull TaskEventViewHolder holder, int position) {
        TaskEvent taskEvent = taskEvents.get(position);
        holder.bind(taskEvent);
    }

    /**
     * Returns the total number of task events in the adapter.
     * This method provides the total number of items in the dataset, which is required by RecyclerView.
     *
     * @return The total number of task events in the list.
     */
    @Override
    public int getItemCount() {
        return taskEvents.size();
    }

    /**
     * ViewHolder class for task event items.
     * This inner class holds the views for each item and provides methods to bind data to these views.
     */
    public class TaskEventViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView points;
        private final ImageView star;
        private final CheckBox checkBox;
        private boolean isBinding;

        /**
         * Constructor for the TaskEventViewHolder.
         * This method initializes the views for an individual task event item.
         *
         * @param itemView The root View for the task event item.
         */
        public TaskEventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskEventTitle);
            points = itemView.findViewById(R.id.taskEventPoints);
            star = itemView.findViewById(R.id.taskEventStar);
            checkBox = itemView.findViewById(R.id.taskCheckBox);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isBinding) return;

                if (statusChangeListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        TaskEvent taskEvent = taskEvents.get(position);
                        statusChangeListener.onTaskStatusChanged(taskEvent, isChecked);
                        updateTaskAppearance(isChecked);
                    }
                }
            });
        }

        /**
         * Binds the task event data to the views.
         * This method populates the TextView, CheckBox, and other views with data from a given TaskEvent.
         *
         * @param taskEvent The TaskEvent object containing the data to bind.
         */
        public void bind(@NonNull TaskEvent taskEvent) {
            // TODO temporary implementation for better view, while container is not implemented
            String temporaryImplTitle = taskEvent.getTitle() + " - " +
                    taskEvent.getScheduledDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            isBinding = true;
            title.setText(temporaryImplTitle);
            String pointsText = itemView.getContext().getString(R.string.points_earned, taskEvent.getPointsEarned());
            points.setText(pointsText);
            checkBox.setChecked(taskEvent.isCompleted());
            updateTaskAppearance(taskEvent.isCompleted());
            isBinding = false;
        }

        /**
         * Updates the visual appearance of the task based on its completion status.
         * This method strikes through the task title and hides points and star icons if the task is marked as completed.
         *
         * @param isCompleted True if the task is completed, false otherwise.
         */
        private void updateTaskAppearance(boolean isCompleted) {
            if (isCompleted) {
                title.setPaintFlags(title.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                points.setVisibility(View.GONE);
                star.setVisibility(View.GONE);
            }
            else {
                title.setPaintFlags(title.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                points.setVisibility(View.VISIBLE);
                star.setVisibility(View.VISIBLE);
            }
        }
    }
}
