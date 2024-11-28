package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.models.Reward;
import com.ilp506.taskward.data.repositories.RewardRepository;
import com.ilp506.taskward.exceptions.DatabaseOperationException;
import com.ilp506.taskward.exceptions.ExceptionHandler;
import com.ilp506.taskward.utils.OperationResponse;

import java.util.List;

/**
 * Controller class responsible for managing Reward operations.
 * This class interacts with the RewardRepository to perform database operations
 * while handling errors and returning structured responses.
 */
public class RewardController {
    private final RewardRepository rewardRepository;

    /**
     * Constructs a RewardController with a RewardRepository instance.
     *
     * @param context The application context used to initialize the RewardRepository.
     */
    public RewardController(Context context) {
        this.rewardRepository = new RewardRepository(context);
    }

    /**
     * Validates the reward ID to ensure it is greater than 0.
     *
     * @param rewardId ID of the reward to validate.
     * @throws IllegalArgumentException If the reward ID is less than or equal to 0.
     */
    private void validateRewardId(int rewardId) {
        if (rewardId <= 0) throw new IllegalArgumentException("Reward ID must be greater than 0.");
    }

    /**
     * Creates a new reward.
     *
     * @param reward Reward object containing reward details.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Reward> createReward(Reward reward) {
        try {
            reward.validate();

            Reward createdReward = rewardRepository.createReward(reward);
            return OperationResponse.success("Reward created successfully", createdReward);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid reward data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while creating reward in the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while creating reward");
        }
    }

    /**
     * Retrieves all rewards.
     *
     * @return OperationResponse containing the list of rewards or failure message.
     */
    public OperationResponse<List<Reward>> getAllRewards() {
        try {
            List<Reward> rewards = rewardRepository.getAllRewards();
            if (rewards.isEmpty())
                return OperationResponse.failure("No rewards found.");

            return OperationResponse.success("Rewards retrieved successfully", rewards);
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving rewards from the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving rewards");
        }
    }


    /**
     * Retrieves a reward by its ID.
     *
     * @param rewardId ID of the reward to retrieve.
     * @return OperationResponse containing the reward object or failure message.
     */
    public OperationResponse<Reward> getRewardById(int rewardId) {
        try {
            validateRewardId(rewardId);
            Reward reward = rewardRepository.getRewardById(rewardId);
            if (reward == null)
                return OperationResponse.failure("Reward not found");

            return OperationResponse.success("Reward retrieved successfully", reward);
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while retrieving reward from the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while retrieving reward");
        }
    }

    /**
     * Updates an existing reward.
     *
     * @param reward Reward object containing updated details.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Reward> updateReward(Reward reward) {
        try {
            validateRewardId(reward.getId());
            Reward existingReward = rewardRepository.getRewardById(reward.getId());
            if (existingReward == null)
                return OperationResponse.failure("Reward not found");

            reward.validate();

            Reward updatedReward = rewardRepository.updateReward(reward);
            return OperationResponse.success("Reward updated successfully", updatedReward);
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid reward data provided");
        } catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while updating reward in the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while updating reward");
        }
    }

    /**
     * Deletes a reward by its ID.
     *
     * @param rewardId ID of the reward to delete.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> deleteReward(int rewardId) {
        try {
            validateRewardId(rewardId);
            Reward existingReward = rewardRepository.getRewardById(rewardId);
            if (existingReward == null)
                return OperationResponse.failure("Reward not found");

            rewardRepository.deleteReward(rewardId);

            return OperationResponse.success("Reward deleted successfully");
        } catch (IllegalArgumentException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Invalid reward ID provided");
        }
        catch (DatabaseOperationException e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Error while deleting reward from the database");
        } catch (Exception e) {
            ExceptionHandler.handleException(e);
            return OperationResponse.failure("Unexpected error occurred while deleting reward");
        }
    }
}
