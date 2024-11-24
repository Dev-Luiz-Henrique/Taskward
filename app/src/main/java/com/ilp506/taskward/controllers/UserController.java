package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.data.repositories.UserRepository;
import com.ilp506.taskward.exceptions.DatabaseOperationException;
import com.ilp506.taskward.exceptions.ExceptionHandler;
import com.ilp506.taskward.utils.OperationResponse;

/**
 * Controller class responsible for managing User operations.
 * This class interacts with the UserRepository to perform database operations
 * while handling errors and returning structured responses.
 */
public class UserController {

    private final UserRepository userRepository;

    public UserController(Context context) {
        this.userRepository = new UserRepository(context);
    }

    /**
     * Creates a new user.
     *
     * @param name  User's name.
     * @param photo User's photo (optional).
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<User> createUser(String name, String photo) {
        try {
            User user = new User();
            user.setName(name);
            user.setPhoto(photo);
            user.validate();

            userRepository.createUser(user);
            return OperationResponse.success("User created successfully", user);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid user data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while creating user in the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while creating user");
        }
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId ID of the user to retrieve.
     * @return OperationResponse containing the user object or failure message.
     */
    public OperationResponse<User> getUserById(int userId) {
        try {
            User user = userRepository.getUserById(userId);
            if (user == null)
                return OperationResponse.failure("User not found");

            return OperationResponse.success("User retrieved successfully", user);
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving user from the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving user");
        }
    }

    /**
     * Updates the details of a user.
     *
     * @param userId ID of the user to update.
     * @param name   New name of the user.
     * @param photo  New photo URL of the user.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<User> updateUser(int userId, String name, String photo) {
        try {
            User user = userRepository.getUserById(userId);
            if (user == null)
                return OperationResponse.failure("User not found");

            user.setName(name);
            user.setPhoto(photo);
            user.validate();
            userRepository.updateUser(user);

            return OperationResponse.success("User updated successfully", user);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid user data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating user in the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while updating user");
        }
    }

    /**
     * Deletes a user by their ID.
     *
     * @param userId ID of the user to delete.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> deleteUser(int userId) {
        try {
            User user = userRepository.getUserById(userId);
            if (user == null)
                return OperationResponse.failure("User not found");

            userRepository.deleteUser(userId);
            return OperationResponse.success("User deleted successfully");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while deleting user from the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while deleting user");
        }
    }

    /**
     * Updates the points of a user.
     *
     * @param userId ID of the user to update.
     * @param points New points value.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<User> updateUserPoints(int userId, int points) {
        try {
            User user = userRepository.getUserById(userId);
            if (user == null)
                return OperationResponse.failure("User not found");

            user.setPoints(points);
            user.validate();
            userRepository.updateUserPoints(userId, points);
            return OperationResponse.success("User points updated successfully", user);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid user data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating user points in the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while updating user points");
        }
    }
}
