package com.ilp506.taskward.data.models;

import androidx.annotation.NonNull;

import com.ilp506.taskward.utils.DateUtils;

import java.time.LocalDateTime;

/**
 * Represents a user in the system.
 * This class encapsulates the properties and behaviors of a user entity.
 */
public class User {

    private int id;
    private String name;
    private String photo;
    private int points;
    private LocalDateTime createdAt;

    /**
     * Constructs a new User object with the current timestamp as the creation time.
     */
    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Returns a string representation of the user object.
     * This method includes key attributes such as user ID, name, photo, points, and creation timestamp.
     *
     * @return A formatted string containing user details.
     */
    @NonNull
    @Override
    public String toString() {
        return "User { " +
                "id=" + id +
                ", name='" + name +
                ", photo='" + photo +
                ", points=" + points +
                ", createdAt=" + DateUtils.formatLocalDateTime(createdAt) +
                " }";
    }

    /**
     * Validates the user's data to ensure all fields meet the requirements.
     *
     * @throws IllegalArgumentException If any validation rule is violated.
     */
    public void validate() throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name is required.");
        if (photo != null && photo.length() > 255)
            throw new IllegalArgumentException("Photo must be less than 255 characters.");
        if (points < 0)
            throw new IllegalArgumentException("Points must be greater than or equal to 0.");
    }
}
