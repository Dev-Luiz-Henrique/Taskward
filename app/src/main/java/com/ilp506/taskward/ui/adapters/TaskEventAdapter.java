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

import java.util.List;

public class TaskEventAdapter extends RecyclerView.Adapter<TaskEventAdapter.TaskEventViewHolder> {
    private static final String TAG = TaskEventAdapter.class.getSimpleName();

    private final List<TaskEvent> taskEvents;
    private final OnTaskStatusChangeListener statusChangeListener;

    public interface OnTaskStatusChangeListener {
        void onTaskStatusChanged(TaskEvent taskEvent, boolean isChecked);
    }

    public TaskEventAdapter(List<TaskEvent> taskEvents, OnTaskStatusChangeListener statusChangeListener) {
        this.taskEvents = taskEvents;
        this.statusChangeListener = statusChangeListener;
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
        private final ImageView star;
        private final CheckBox checkBox;

        public TaskEventViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.taskEventTitle);
            points = itemView.findViewById(R.id.taskEventPoints);
            star = itemView.findViewById(R.id.taskEventStar);
            checkBox = itemView.findViewById(R.id.taskCheckBox);

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
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

        public void bind(@NonNull TaskEvent taskEvent) {
            title.setText(taskEvent.getTitle());
            String pointsText = itemView.getContext().getString(R.string.points_earned, taskEvent.getPointsEarned());
            points.setText(pointsText);
            checkBox.setChecked(taskEvent.isCompleted());
            updateTaskAppearance(taskEvent.isCompleted());
        }

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
