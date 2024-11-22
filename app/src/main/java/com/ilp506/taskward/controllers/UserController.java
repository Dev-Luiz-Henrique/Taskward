package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.data.repositories.UserRepository;
import com.ilp506.taskward.utils.Logger;

public class UserController {

    private static final String TAG = UserController.class.getSimpleName();
    private final UserRepository userRepository;

    public UserController(Context context) {
        this.userRepository = new UserRepository(context);
    }

    public boolean createUser(String name, String photo) {
        try {
            User user = new User();
            user.setName(name);
            user.setPhoto(photo);
            user.validate();

            userRepository.createUser(user);
            return true;

        } catch (IllegalArgumentException e) {
            Logger.e(TAG, "Validation error: " + e.getMessage(), e);
            return false;
        } catch (RuntimeException e) {
            Logger.e(TAG, "Database error: " + e.getMessage(), e);
            return false;
        }
    }

    public User getUserById(int userId) {
        try {
            return userRepository.getUserById(userId);
        } catch (RuntimeException e) {
            Logger.e(TAG, "Database error while fetching user: " + e.getMessage(), e);
            return null;
        }
    }
}
