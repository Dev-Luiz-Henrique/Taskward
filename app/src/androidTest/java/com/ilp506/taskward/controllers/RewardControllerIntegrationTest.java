package com.ilp506.taskward.controllers;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import com.ilp506.taskward.data.models.Reward;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.data.repositories.UserRepository;
import com.ilp506.taskward.utils.OperationResponse;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

public class RewardControllerIntegrationTest {
    private RewardController rewardController;
    private UserRepository userRepository;
    private User user;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        rewardController = new RewardController(context);

        // Create a test user
        user = new User();
        user.setName("Test User");
        user.setPhoto("path/to/test/user/photo");
        UserRepository userRepository = new UserRepository(context);
        user = userRepository.createUser(user);
    }

    @Test
    public void testCreateReward() {
        Reward newReward = new Reward();
        newReward.setUserId(user.getId());
        newReward.setIcon("test_icon");
        newReward.setTitle("Test Reward");
        newReward.setDescription("Test Description");
        newReward.setPointsRequired(100);

        OperationResponse<Reward> response = rewardController.createReward(newReward);

        assertEquals("Reward created successfully", response.getMessage());
        assertNotNull(response.getData());

        Reward createdReward = response.getData();
        assertEquals(user.getId(), createdReward.getUserId());
        assertEquals("test_icon", createdReward.getIcon());
        assertEquals("Test Reward", createdReward.getTitle());
        assertEquals("Test Description", createdReward.getDescription());
        assertEquals(100, createdReward.getPointsRequired());
    }

    @Test
    public void testGetAllRewards() {
        OperationResponse<List<Reward>> response = rewardController.getAllRewards();

        assertNotNull(response);
        assertEquals("Rewards retrieved successfully", response.getMessage());
        assertNotNull(response.getData());
    }

    @Test
    public void testGetRewardById() {
        int rewardId = 1;
        OperationResponse<Reward> response = rewardController.getRewardById(rewardId);

        if (response.isSuccessful()) {
            assertEquals("Reward retrieved successfully", response.getMessage());
            assertNotNull(response.getData());
            assertEquals(rewardId, response.getData().getId());
        } else {
            assertEquals("Reward not found", response.getMessage());
        }
    }

    @Test
    public void testUpdateReward() {
        Reward reward = new Reward();
        reward.setId(1);
        reward.setUserId(user.getId());
        reward.setIcon("updated_icon");
        reward.setTitle("Updated Reward");
        reward.setDescription("Updated Description");
        reward.setPointsRequired(150);

        OperationResponse<Reward> response = rewardController.updateReward(reward);

        if (response.isSuccessful()) {
            assertEquals("Reward updated successfully", response.getMessage());
            assertNotNull(response.getData());
            assertEquals("Updated Reward", response.getData().getTitle());
        } else {
            assertEquals("Reward not found", response.getMessage());
        }
    }

    @Test
    public void testDeleteReward() {
        int rewardId = 1;
        OperationResponse<Void> response = rewardController.deleteReward(rewardId);

        if (response.isSuccessful()) {
            assertEquals("Reward deleted successfully", response.getMessage());
        } else {
            assertEquals("Reward not found", response.getMessage());
        }
    }
}
