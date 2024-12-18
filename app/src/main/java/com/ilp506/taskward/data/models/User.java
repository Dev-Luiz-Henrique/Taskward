package com.ilp506.taskward.data.models;

import androidx.annotation.NonNull;

import com.ilp506.taskward.utils.DateUtils;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Represents a user in the system.
 * This class encapsulates the properties and behaviors of a user entity.
 */
public class User {

    private int id;
    private String name;
    private String email;
    private String photo;
    private int points;
    private LocalDateTime createdAt;

    /**
     * Constructs a new User object, setting the creation time to the current date and time.
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
                ", email='" + email +
                ", photo='" + photo +
                ", points=" + points +
                ", createdAt=" + DateUtils.formatLocalDateTime(createdAt) +
                " }";
    }

    /**
     * Checks if the current User object is equal to another object.
     *
     * @param o The object to compare with.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                points == user.points &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(photo, user.photo);
    }

    /**
     * Returns a hash code value for the User object.
     *
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, photo, points);
    }

    /**
     * Validates the user's data to ensure all fields meet the requirements.
     *
     * @throws IllegalArgumentException If any validation rule is violated.
     */
    public void validate() throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty() || name.length() > 100)
            throw new IllegalArgumentException("Name is required and must be less than 100 characters.");
        if (email == null || email.trim().isEmpty() || email.length() > 255)
            throw new IllegalArgumentException("Email is required and must be less than 255 characters.");
        if (photo != null && photo.length() > 255)
            throw new IllegalArgumentException("Photo must be less than 255 characters.");
        if (points < 0)
            throw new IllegalArgumentException("Points must be greater than or equal to 0.");
    }
}
