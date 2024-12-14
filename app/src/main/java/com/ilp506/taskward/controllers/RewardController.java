package com.ilp506.taskward.controllers;

import android.content.Context;

import com.ilp506.taskward.data.models.Reward;
import com.ilp506.taskward.data.repositories.RewardRepository;
import com.ilp506.taskward.exceptions.handlers.ExceptionHandler;
import com.ilp506.taskward.services.PointService;
import com.ilp506.taskward.utils.OperationResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controller class responsible for managing Reward operations.
 * This class interacts with the RewardRepository to perform database operations
 * while handling errors and returning structured responses.
 */
public class RewardController {
    private final ExceptionHandler exceptionHandler;
    private final RewardRepository rewardRepository;
    private final PointService pointService;

    /**
     * Constructs a RewardController with a RewardRepository and a UserRepository instance.
     *
     * @param context The application context used to initialize the repositories.
     */
    public RewardController(Context context) {
        this.exceptionHandler = ExceptionHandler.getInstance();
        this.rewardRepository = new RewardRepository(context);
        this.pointService = new PointService(context);
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
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to create reward.");
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
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve rewards.");
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
                return OperationResponse.failure("Reward not found.");

            return OperationResponse.success("Reward retrieved successfully", reward);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to retrieve reward.");
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
            reward.validate();

            Reward existingReward = rewardRepository.getRewardById(reward.getId());
            if (existingReward == null)
                return OperationResponse.failure("Reward not found.");

            Reward updatedReward = rewardRepository.updateReward(reward);
            return OperationResponse.success("Reward updated successfully", updatedReward);
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to update reward.");
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
                return OperationResponse.failure("Reward not found.");

            rewardRepository.deleteReward(rewardId);
            return OperationResponse.success("Reward deleted successfully.");
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to delete reward.");
        }
    }

    /**
     * Redeems a reward for a user.
     *
     * @param rewardId ID of the reward to redeem.
     * @return OperationResponse indicating success or failure.
     */
    public OperationResponse<Void> redeemReward(int rewardId) {
        try {
            validateRewardId(rewardId);

            Reward existingReward = rewardRepository.getRewardById(rewardId);
            if (existingReward == null)
                return OperationResponse.failure("Reward not found.");

            if (existingReward.getDateRedeemed() != null)
                return OperationResponse.failure("Reward already redeemed.");

            existingReward.setDateRedeemed(LocalDateTime.now());
            rewardRepository.updateReward(existingReward);

            boolean pointsUpdated = pointService.addPoints(
                    existingReward.getUserId(),
                    existingReward.getPointsRequired()
            );
            if (!pointsUpdated)
                return OperationResponse.failure("Failed to update user points.");

            return OperationResponse.success("Reward redeemed successfully.");
        } catch (Exception e) {
            return exceptionHandler.handleException(e, "Failed to redeem reward.");
        }
    }
}
