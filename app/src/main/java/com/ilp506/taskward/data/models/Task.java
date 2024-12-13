package com.ilp506.taskward.data.models;

import androidx.annotation.NonNull;

import com.ilp506.taskward.data.enums.TaskFrequencyEnum;
import com.ilp506.taskward.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a task in the system.
 * This class encapsulates the properties and behaviors of a task entity.
 */
public class Task {
    private int id;
    private String icon;
    private String title;
    private String description;
    private TaskFrequencyEnum frequency;
    private int frequencyInterval;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private int pointsReward;
    private LocalDateTime createdAt;
    private List<TaskEvent> taskEvents;

    /**
     * Constructs a new Task object, setting the creation time to the current date and time.
     */
    public Task() {
        this.createdAt = LocalDateTime.now();
        this.startDate = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskFrequencyEnum getFrequency() {
        return frequency;
    }

    public void setFrequency(TaskFrequencyEnum frequency) {
        this.frequency = frequency;
    }

    public int getFrequencyInterval() {
        return frequencyInterval;
    }

    public void setFrequencyInterval(int frequencyInterval) {
        this.frequencyInterval = frequencyInterval;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public int getPointsReward() {
        return pointsReward;
    }

    public void setPointsReward(int pointsReward) {
        this.pointsReward = pointsReward;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<TaskEvent> getTaskEvents() {
        return taskEvents;
    }

    public void setTaskEvents(List<TaskEvent> events) {
        this.taskEvents = events;
    }

    /**
     * Returns a string representation of the task object.
     */
    @NonNull
    @Override
    public String toString() {
        return "Task { " +
                "id=" + id +
                ", icon='" + icon +
                ", title='" + title +
                ", description='" + description +
                ", frequency=" + (frequency != null ? frequency.getValue() : "null") +
                ", frequencyInterval=" + frequencyInterval +
                ", startDate=" + DateUtils.formatLocalDateTime(startDate) +
                ", endDate=" + DateUtils.formatLocalDateTime(endDate) +
                ", pointsReward=" + pointsReward +
                ", createdAt=" + DateUtils.formatLocalDateTime(createdAt) +
                " }";
    }

    /**
     * Checks if the current Task object is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id &&
                pointsReward == task.pointsReward &&
                Objects.equals(icon, task.icon) &&
                Objects.equals(title, task.title) &&
                Objects.equals(description, task.description) &&
                frequency == task.frequency &&
                frequencyInterval == task.frequencyInterval &&
                Objects.equals(startDate, task.startDate) &&
                Objects.equals(endDate, task.endDate) &&
                Objects.equals(taskEvents, task.taskEvents);
    }

    /**
     * Returns a hash code value for the Task object.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, icon, title, description, frequency, frequencyInterval,
                startDate, endDate, pointsReward, taskEvents);
    }

    /**
     * Validates the task's data to ensure all fields meet the requirements.
     *
     * @throws IllegalArgumentException If any validation rule is violated.
     */
    public void validate() throws IllegalArgumentException {
        if (id < 0)
            throw new IllegalArgumentException("ID must be greater than or equal to 0.");
        if (icon == null || icon.trim().isEmpty())
            throw new IllegalArgumentException("Icon is required.");
        if (title == null || title.trim().isEmpty() || title.length() > 100)
            throw new IllegalArgumentException("Title is required and must be less than 100 characters.");
        if (description != null && description.length() > 255)
            throw new IllegalArgumentException("Description must be less than 255 characters.");
        if (frequency == null)
            throw new IllegalArgumentException("Frequency is required.");
        if (frequencyInterval <= 0)
            throw new IllegalArgumentException("Frequency interval must be greater than 0.");
        if (pointsReward < 0)
            throw new IllegalArgumentException("Points reward must be greater than or equal to 0.");
        if (startDate.isBefore(LocalDateTime.now()))
            throw new IllegalArgumentException("Start date must be now or in the future.");
        if (endDate != null && endDate.isBefore(startDate))
            throw new IllegalArgumentException("End date must be after start date.");
    }

    /**
     * Returns a list of completed task events associated with this task.
     *
     * @return An unmodifiable list of completed task events.
     */
    @NonNull
    public List<TaskEvent> getCompletedEvents() {
        if (taskEvents == null)
            return Collections.emptyList();
        return taskEvents.stream()
                .filter(TaskEvent::isCompleted)
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
    }
}
