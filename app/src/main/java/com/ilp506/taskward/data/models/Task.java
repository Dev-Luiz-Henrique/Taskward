package com.ilp506.taskward.data.models;

import androidx.annotation.NonNull;

import com.ilp506.taskward.data.enums.TaskFrequencyEnum;
import com.ilp506.taskward.utils.DateUtils;

import java.sql.Timestamp;

/**
 * Represents a task in the system.
 * This class encapsulates the properties and behaviors of a task entity.
 * It includes fields for the task's ID, icon, title, description, frequency, frequency interval,
 * start date, end date, points reward, and creation timestamp.
 */
public class Task {
    private int id;
    private String icon;
    private String title;
    private String description;
    private TaskFrequencyEnum frequency;
    private int frequencyInterval;
    private Timestamp startDate;
    private Timestamp endDate;
    private int pointsReward;
    private Timestamp createdAt;

    /**
     * Constructs a new Task object with the current timestamp as the creation time.
     */
    public Task() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.startDate = new Timestamp(System.currentTimeMillis());
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

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public int getPointsReward() {
        return pointsReward;
    }

    public void setPointsReward(int pointsReward) {
        this.pointsReward = pointsReward;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns a string representation of the task object.
     * This method includes key attributes such as task ID, icon, title, description, frequency,
     * frequency interval, start date, end date, points reward, and creation timestamp.
     *
     * @return A formatted string containing task details.
     */
    @NonNull
    @Override
    public String toString() {
        return "Task { " +
                "id=" + id +
                ", icon=" + icon +
                ", title=" + title +
                ", description=" + description +
                ", frequency=" + frequency.getValue() +
                ", frequencyInterval=" + frequencyInterval +
                ", startDate=" + DateUtils.formatTimestamp(startDate) +
                ", endDate=" + DateUtils.formatTimestamp(endDate) +
                ", pointsReward=" + pointsReward +
                ", createdAt=" + DateUtils.formatTimestamp(createdAt) +
                " }";
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
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title is required.");
        if (description != null && description.length() > 255)
            throw new IllegalArgumentException("Description must be less than 255 characters");
        if (frequency == null)
            throw new IllegalArgumentException("Frequency is required.");
        if (frequencyInterval <= 0)
            throw new IllegalArgumentException("Frequency interval must be greater than 0");
        if (pointsReward < 0)
            throw new IllegalArgumentException("Points reward must be greater than or equal to 0");
        if (startDate.before(new Timestamp(System.currentTimeMillis())))
            throw new IllegalArgumentException("Start date must be now or in the future.");
        if (endDate != null && endDate.before(startDate))
            throw new IllegalArgumentException("End date must be after start date.");
    }
}
