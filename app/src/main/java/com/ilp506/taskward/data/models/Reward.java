package com.ilp506.taskward.data.models;

import androidx.annotation.NonNull;

import com.ilp506.taskward.utils.DateUtils;

import java.time.LocalDateTime;

/**
 * Represents a reward in the system.
 * This class encapsulates the properties and behaviors of a reward entity.
 */
public class Reward {
    private int id;
    private int userId;
    private String icon;
    private String title;
    private String description;
    private int pointsRequired;
    private LocalDateTime dateRedeemed;
    private LocalDateTime createdAt;

    /**
     * Constructs a new Reward object with the current timestamp as the creation time.
     */
    public Reward() {
        this.createdAt = LocalDateTime.now();
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

    public LocalDateTime getDateRedeemed() {
        return dateRedeemed;
    }

    public void setDateRedeemed(LocalDateTime dateRedeemed) {
        this.dateRedeemed = dateRedeemed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns a string representation of the reward object.
     */
    @NonNull
    @Override
    public String toString() {
        return "Reward { " +
                "id=" + id +
                ", userId=" + userId +
                ", icon='" + icon +
                ", title='" + title +
                ", description='" + description +
                ", pointsRequired=" + pointsRequired +
                ", dateRedeemed=" + DateUtils.formatLocalDateTime(dateRedeemed) +
                ", createdAt=" + DateUtils.formatLocalDateTime(createdAt) +
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
            throw new IllegalArgumentException("Description must be less than 255 characters.");
        if (pointsRequired <= 0)
            throw new IllegalArgumentException("Points required must be greater than 0.");
        if (dateRedeemed != null && dateRedeemed.isAfter(LocalDateTime.now()))
            throw new IllegalArgumentException("Date redeemed must be in the present or the past if specified.");
    }
}
