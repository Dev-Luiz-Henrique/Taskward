package com.ilp506.taskward.data.models;

import java.sql.Timestamp;

public class User {

    public int id;
    public String name;
    public String photo; // TODO: implementar a funcionalidade de colocar fotos
    public int points;
    public Timestamp createdAt;

    public User() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public int getId() {
        return id;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void validate() throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name is required.");
        if (photo != null && (photo.length() > 255))
            throw new IllegalArgumentException("Photo must be less than 255 characters.");
        if (points < 0)
            throw new IllegalArgumentException("Points must be greater than or equal to 0.");
    }
}
