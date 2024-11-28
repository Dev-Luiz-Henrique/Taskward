package com.ilp506.taskward.data.models;

import androidx.annotation.NonNull;

import com.ilp506.taskward.utils.DateUtils;

import java.sql.Timestamp;

/**
 * Represents a reward in the system.
 * This class encapsulates the properties and behaviors of a reward entity.
 * It includes fields for the reward's ID, user ID, icon, title, description, points required, date redeemed, and creation timestamp.
 */
public class Reward {
    private int id;
    private int userId;
    private String icon;
    private String title;
    private String description;
    private int pointsRequired;
    private Timestamp dateRedeemed;
    private Timestamp createdAt;

    /**
     * Constructs a new Reward object with the current timestamp as the creation time.
     */
    public Reward() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
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

    public int getPointsRequired() {
        return pointsRequired;
    }

    public void setPointsRequired(int pointsRequired) {
        this.pointsRequired = pointsRequired;
    }

    public Timestamp getDateRedeemed() {
        return dateRedeemed;
    }

    public void setDateRedeemed(Timestamp dateRedeemed) {
        this.dateRedeemed = dateRedeemed;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns a string representation of the reward object.
     * This method includes key attributes such as reward ID, user ID, icon, title, description,
     * points required, date redeemed, and creation timestamp.
     *
     * @return A formatted string containing reward details.
     */
    @NonNull
    @Override
    public String toString() {
        return "Reward { " +
                "id=" + id +
                ", userId=" + userId +
                ", icon=" + icon +
                ", title=" + title +
                ", description=" + description +
                ", pointsRequired=" + pointsRequired +
                ", dateRedeemed=" + DateUtils.formatTimestamp(dateRedeemed) +
                ", createdAt=" + DateUtils.formatTimestamp(createdAt) +
                " }";
    }

    /**
     * Validates the reward's data to ensure all fields meet the requirements.
     *
     * @throws IllegalArgumentException If any validation rule is violated.
     */
    public void validate() throws IllegalArgumentException {
        if (icon == null || icon.trim().isEmpty())
            throw new IllegalArgumentException("Icon is required.");
        if (title == null || title.trim().isEmpty())
            throw new IllegalArgumentException("Title is required.");
        if (description != null && description.length() > 255)
            throw new IllegalArgumentException("Description must be less than 255 characters");
        if (pointsRequired <= 0)
            throw new IllegalArgumentException("Points required must be greater than 0");
        if (dateRedeemed != null && dateRedeemed.after(new Timestamp(System.currentTimeMillis())))
            throw new IllegalArgumentException("Date redeemed must be in the present or the past if specified.");
    }
}