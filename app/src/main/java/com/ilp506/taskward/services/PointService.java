package com.ilp506.taskward.services;

import android.content.Context;

import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.data.repositories.UserRepository;
import com.ilp506.taskward.exceptions.codes.DatabaseErrorCode;
import com.ilp506.taskward.exceptions.custom.DatabaseOperationException;

/**
 * Service class responsible for managing point-related operations for users.
 */
public class PointService {
    private final UserRepository userRepository;

    /**
     * Constructs a PointService with the required context.
     *
     * @param context The application context to initialize the UserRepository.
     */
    public PointService(Context context) {
        this.userRepository = new UserRepository(context);
    }

    /**
     * Adds points to a user's total points and updates them in the repository.
     *
     * @param userId The ID of the user.
     * @param points The number of points to add.
     * @return true if the operation was successful, false otherwise.
     * @throws DatabaseOperationException if the user is not found.
     */
    public boolean addPoints(int userId, int points) {
        User user = userRepository.getUserById(userId);
        if (user == null)
            throw DatabaseOperationException.fromError(
                    DatabaseErrorCode.RESOURCE_NOT_FOUND,
                    String.format("User with ID %d not found.", userId)
            );

        user.setPoints(user.getPoints() + points);
        userRepository.updateUser(user);
        return true;
    }

    /**
     * Deducts points from a user's total points and updates them in the repository.
     *
     * @param userId The ID of the user.
     * @param points The number of points to deduct.
     * @return true if the operation was successful, false otherwise.
     * @throws DatabaseOperationException if the user is not found or if the deduction is invalid.
     */
    public boolean deductPoints(int userId, int points) {
        User user = userRepository.getUserById(userId);
        if (user == null) {
            throw DatabaseOperationException.fromError(
                    DatabaseErrorCode.RESOURCE_NOT_FOUND,
                    String.format("User with ID %d not found.", userId)
            );
        }

        if (user.getPoints() - points < 0) {
            throw DatabaseOperationException.fromError(
                    DatabaseErrorCode.DATA_INTEGRITY_VIOLATION,
                    String.format("Cannot deduct %d points from user ID %d. Insufficient points.",
                            points, userId)
            );
        }

        user.setPoints(user.getPoints() - points);
        userRepository.updateUser(user);
        return true;
    }
}
