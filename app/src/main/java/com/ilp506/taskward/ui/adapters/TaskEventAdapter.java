package com.ilp506.taskward.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.ilp506.taskward.R;
import com.ilp506.taskward.data.models.TaskEvent;

import java.util.List;

public class TaskEventAdapter extends RecyclerView.Adapter<TaskEventAdapter.TaskEventViewHolder> {
    private static final String TAG = TaskEventAdapter.class.getSimpleName();

    private final List<TaskEvent> taskEvents;
    private final OnTaskEventClickListener listener;

    public interface OnTaskEventClickListener {
        void onTaskEventClick(TaskEvent taskEvent);
    }

    public TaskEventAdapter(List<TaskEvent> taskEvents, OnTaskEventClickListener listener) {
        this.taskEvents = taskEvents;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TaskEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_event_item, parent, false);
        return new TaskEventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskEventViewHolder holder, int position) {
        TaskEvent taskEvent = taskEvents.get(position);
        holder.bind(taskEvent);
    }

    @Override
    public int getItemCount() {
        return taskEvents.size();
    }

    public class TaskEventViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView points;

        public TaskEventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskEventTitle);
            points = itemView.findViewById(R.id.taskEventPoints);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION)
                        listener.onTaskEventClick(taskEvents.get(position));
                }
            });
        }

        public void bind(@NonNull TaskEvent taskEvent) {
            title.setText(taskEvent.getTitle());
            points.setText(String.valueOf(taskEvent.getPointsEarned()));

            /*if (taskEvent.getStatus() == TaskEventStatusEnum.COMPLETED) {
                status.setTextColor(itemView.getResources().getColor(android.R.color.holo_green_dark));
            } else if (taskEvent.getStatus() == TaskEventStatusEnum.CANCELLED) {
                status.setTextColor(itemView.getResources().getColor(android.R.color.holo_red_dark));
            } else {
                status.setTextColor(itemView.getResources().getColor(android.R.color.black));
            }*/
        }
    }
}
