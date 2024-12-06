package com.ilp506.taskward.data.models;

import androidx.annotation.NonNull;

import com.ilp506.taskward.data.enums.TaskEventStatusEnum;
import com.ilp506.taskward.utils.DateUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task event in the application.
 * This class encapsulates the properties and behavior of a task event entity.
 */
public class TaskEvent {
    private int id;
    private int userId;
    private int taskId;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private int pointsEarned;
    private TaskEventStatusEnum status;
    private LocalDateTime createdAt;

    private String title;

    /**
     * Constructs a new TaskEvent object with the current time as the creation timestamp.
     * The status is set to SCHEDULED by default.
     */
    public TaskEvent() {
        this.createdAt = LocalDateTime.now();
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

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns a string representation of the TaskEvent object.
     */
    @NonNull
    @Override
    public String toString() {
        return "TaskEvent { " +
                "id=" + id +
                ", userId=" + userId +
                ", taskId=" + taskId +
                ", scheduledDate=" + DateUtils.formatLocalDateTime(scheduledDate) +
                ", completedDate=" + DateUtils.formatLocalDateTime(completedDate) +
                ", pointsEarned=" + pointsEarned +
                ", status=" + status.getValue() +
                ", createdAt=" + DateUtils.formatLocalDateTime(createdAt) +
                " }";
    }


    private String formatLocalDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime != null ? dateTime.format(formatter) : "null";
    }

    /**
     * Validates the task event's data to ensure all fields meet the requirements.
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

    /**
     * Checks if the task event is completed.
     */
    public boolean isCompleted() {
        return status == TaskEventStatusEnum.COMPLETED;
    }
}
