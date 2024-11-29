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

    /**
     * Constructs a UserController with a UserRepository instance.
     *
     * @param context The application context used to initialize the UserRepository.
     */
    public UserController(Context context) {
        this.userRepository = new UserRepository(context);
    }

    /**
     * Validates the user ID to ensure it is greater than 0.
     *
     * @param userId ID of the user to validate.
     * @throws IllegalArgumentException If the user ID is less than or equal to 0.
     */
    private void validateUserId(int userId) {
        if (userId <= 0) throw new IllegalArgumentException("User ID must be greater than 0.");
    }

    /**
     * Creates a new user.
     *
     * @param user User object containing user details.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<User> createUser(User user) {
        try {
            user.validate();

            User createdUser = userRepository.createUser(user);
            return OperationResponse.success("User created successfully", createdUser);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid user data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while creating user in the database");
        } catch (RuntimeException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while creating user");
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
        } catch (RuntimeException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving user");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving user");
        }
    }

    /**
     * Updates the details of a user.
     *
     * @param user User object containing updated details.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<User> updateUser(User user) {
        try {
            validateUserId(user.getId());
            User existingUser = userRepository.getUserById(user.getId());
            if (existingUser == null)
                return OperationResponse.failure("User not found");

            user.validate();

            User updatedUser = userRepository.updateUser(user);
            return OperationResponse.success("User updated successfully", updatedUser);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid user data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating user in the database");
        } catch (RuntimeException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating user");
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
            validateUserId(userId);
            User existingUser = userRepository.getUserById(userId);
            if (existingUser == null)
                return OperationResponse.failure("User not found");

            userRepository.deleteUser(userId);

            return OperationResponse.success("User deleted successfully");
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid user ID provided");
        }
        catch (DatabaseOperationException e) {
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
            validateUserId(userId);
            User existingUser = userRepository.getUserById(userId);
            if (existingUser == null)
                return OperationResponse.failure("User not found");

            existingUser.setPoints(points);
            existingUser.validate();

            User updatedUser = userRepository.updateUser(existingUser);
            return OperationResponse.success("User points updated successfully", updatedUser);
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
