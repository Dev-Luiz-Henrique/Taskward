package com.ilp506.taskward.data.models;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void constructor_whenCalled_setsCreatedAt() {
        User user = new User();
        assertNotNull(user.getCreatedAt());
        assertTrue(user.getCreatedAt().getTime() <= System.currentTimeMillis());
    }

    @Test
    public void validate_whenValidUser_shouldPass() {
        User user = new User();
        user.setName("John Doe");
        user.setPoints(100);
        user.setPhoto("/path/to/valid/photo");
        user.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_whenNameIsNull_shouldThrowIllegalArgumentException() {
        User user = new User();
        user.setPoints(100);
        user.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_whenNameIsEmpty_shouldThrowIllegalArgumentException() {
        User user = new User();
        user.setName(" ");
        user.setPoints(100);
        user.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_whenPhotoExceedsMaxLength_shouldThrowIllegalArgumentException() {
        User user = new User();
        user.setName("John Doe");
        user.setPoints(100);
        user.setPhoto("a".repeat(256));
        user.validate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_whenPointsAreNegative_shouldThrowIllegalArgumentException() {
        User user = new User();
        user.setName("John Doe");
        user.setPoints(-10);
        user.validate();
    }
}
