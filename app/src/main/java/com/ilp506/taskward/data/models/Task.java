package com.ilp506.taskward.data.models;

import com.ilp506.taskward.enums.TaskFrequencyEnum;

import java.sql.Timestamp;

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
