package com.ilp506.taskward.data.models;

import java.sql.Timestamp;

public class User {

    private int id;
    private String name;
    private String photo; // TODO: implementar a funcionalidade de colocar fotos
    private int points;
    private Timestamp createdAt;

    public User() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void validate() throws IllegalArgumentException {
        if (id < 0)
            throw new IllegalArgumentException("ID must be greater than or equal to 0.");
        if (name == null || name.trim().isEmpty())
            throw new IllegalArgumentException("Name is required.");
        if (photo != null && (photo.length() > 255))
            throw new IllegalArgumentException("Photo must be less than 255 characters.");
        if (points < 0)
            throw new IllegalArgumentException("Points must be greater than or equal to 0.");
    }
}
