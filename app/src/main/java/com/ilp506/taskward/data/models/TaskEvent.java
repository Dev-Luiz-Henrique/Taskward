package com.ilp506.taskward.data.models;

import androidx.annotation.NonNull;

import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.utils.DateUtils;

import java.sql.Timestamp;

public class TaskEvent {
    private int id;
    private int userId;
    private int taskId;
    private Timestamp scheduledDate;
    private Timestamp completedDate;
    private int pointsEarned;
    private TaskEventStatusEnum status;
    private Timestamp createdAt;

    /**
     * Constructs a new TaskEvent object with the current timestamp as the creation time.
     * The status is set to SCHEDULED by default.
     */
    public TaskEvent() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.status = TaskEventStatusEnum.SCHEDULED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Timestamp getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(Timestamp scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public Timestamp getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Timestamp completedDate) {
        this.completedDate = completedDate;
    }

    public int getPointsEarned() {
        return pointsEarned;
    }

    public void setPointsEarned(int pointsEarned) {
        this.pointsEarned = pointsEarned;
    }

    public TaskEventStatusEnum getStatus() {
        return status;
    }

    public void setStatus(TaskEventStatusEnum status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns a string representation of the TaskEvent object.
     * This method includes key attributes such as task event ID, user ID, task ID, scheduled and completed dates,
     * points earned, status, and creation timestamp.
     *
     * @return A formatted string containing task event details.
     */
    @NonNull
    @Override
    public String toString() {
        return "TaskEvent { " +
                "id=" + id +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", scheduledDate=" + DateUtils.formatTimestamp(scheduledDate) +
                ", completedDate=" + DateUtils.formatTimestamp(completedDate) +
                ", pointsEarned=" + pointsEarned +
                ", status=" + status.getValue() +
                ", createdAt=" + DateUtils.formatTimestamp(createdAt) +
                " }";
    }

    /**
     * Validates the task event's data to ensure all fields meet the requirements.
     *
     * @throws IllegalArgumentException If any validation rule is violated.
     */
    public void validate() throws IllegalArgumentException {
        if (scheduledDate == null)
            throw new IllegalArgumentException("Scheduled date is required.");
        if (status == null)
            throw new IllegalArgumentException("Status is required.");
        if (status == TaskEventStatusEnum.COMPLETED && completedDate == null)
            throw new IllegalArgumentException("Completed date must be specified if status is 'completed'.");
        if (pointsEarned < 0)
            throw new IllegalArgumentException("Points earned must be greater than or equal to 0.");
    }
}
