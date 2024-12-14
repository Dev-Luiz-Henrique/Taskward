package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.data.repositories.UserRepository;
import com.ilp506.taskward.exceptions.handlers.ExceptionHandler;
import com.ilp506.taskward.utils.CacheManager;
import com.ilp506.taskward.utils.OperationResponse;

/**
 * Controller class responsible for managing User operations.
 * This class interacts with the UserRepository to perform database operations
 * while handling errors and returning structured responses.
 */
public class UserController {
    private final ExceptionHandler exceptionHandler;
    private final UserRepository userRepository;
    private final CacheManager cacheManager;

    /**
     * Constructs a UserController with a UserRepository instance.
     *
     * @param context The application context used to initialize the UserRepository.
     */
    public UserController(Context context) {
        this.exceptionHandler = ExceptionHandler.getInstance();
        this.userRepository = new UserRepository(context);
        this.cacheManager = new CacheManager(context);
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
            if(createdUser != null)
                cacheManager.saveUserId(createdUser.getId());

            return OperationResponse.success("User created successfully", createdUser);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to create user.");
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
            validateUserId(userId);
            User user = userRepository.getUserById(userId);

            if (user == null)
                return OperationResponse.failure("User not found");

            return OperationResponse.success("User retrieved successfully", user);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve user.");
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
            user.validate();

            User existingUser = userRepository.getUserById(user.getId());
            if (existingUser == null)
                return OperationResponse.failure("User not found");

            User updatedUser = userRepository.updateUser(user);
            return OperationResponse.success("User updated successfully", updatedUser);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to update user.");
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
            if (userRepository.getUserById(userId) == null && userId == cacheManager.getUserId())
                cacheManager.clearCache();

            return OperationResponse.success("User deleted successfully");
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to delete user.");
        }
    }
}
