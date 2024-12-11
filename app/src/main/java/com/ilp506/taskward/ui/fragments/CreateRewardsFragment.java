package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.RewardController;
import com.ilp506.taskward.data.models.Reward;
import com.ilp506.taskward.utils.CacheManager;
import com.ilp506.taskward.utils.OperationResponse;

/**
 * Fragment for creating rewards in the TaskWard app.
 * This fragment allows users to create new rewards by providing details such as title, description, and points.
 * It interacts with the RewardController to handle the reward creation process and updates the UI accordingly.
 */
public class CreateRewardsFragment extends Fragment {

    private RewardController rewardController;

    private EditText editRewardTitle, editRewardDescription, editRewardPoints;
    private ImageButton buttonCreateReward;

    public CreateRewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rewardController = new RewardController(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_rewards, container, false);

        initializeUI(view);
        setupComponents();
        return view;
    }

    /**
     * Initializes the UI components of the fragment.
     *
     * @param view The View of the fragment.
     */
    private void initializeUI(@NonNull View view) {
        editRewardTitle = view.findViewById(R.id.editRewardTitle);
        editRewardDescription = view.findViewById(R.id.editRewardDescription);
        editRewardPoints = view.findViewById(R.id.editRewardPoints);
        buttonCreateReward = view.findViewById(R.id.buttonCreateReward);
    }

    /**
     * Sets up the components of the fragment.
     */
    private void setupComponents() {
        buttonCreateReward.setOnClickListener(v -> {
            if (validateRewardInputs()) {
                Reward reward = buildRewardFromInputs();
                handleRewardCreation(reward);
            }
        });
    }

    /**
     * Validates the reward inputs before creating a new reward.
     *
     * @return true if inputs are valid, false otherwise.
     */
    private boolean validateRewardInputs() {
        String title = editRewardTitle.getText().toString().trim();
        String description = editRewardDescription.getText().toString().trim();
        String points = editRewardPoints.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            Toast.makeText(requireContext(), "Title is required", Toast.LENGTH_SHORT).show();
            editRewardTitle.setError("Title is required");
            return false;
        }
        if (TextUtils.isEmpty(description)) {
            Toast.makeText(requireContext(), "Description is required", Toast.LENGTH_SHORT).show();
            editRewardDescription.setError("Description is required");
            return false;
        }
        if (TextUtils.isEmpty(points)) {
            Toast.makeText(requireContext(), "Points is required", Toast.LENGTH_SHORT).show();
            editRewardPoints.setError("Points is required");
            return false;
        }
        return true;
    }

    /**
     * Builds a Reward object from the user inputs.
     *
     * @return The Reward object.
     */
    @NonNull
    private Reward buildRewardFromInputs() {
        String title = editRewardTitle.getText().toString().trim();
        String description = editRewardDescription.getText().toString().trim();
        int points = Integer.parseInt(editRewardPoints.getText().toString().trim());

        CacheManager cacheManager = new CacheManager(requireContext());

        Reward reward = new Reward();
        reward.setUserId(cacheManager.getUserId());
        reward.setTitle(title);
        reward.setDescription(description);
        reward.setPointsRequired(points);
        reward.setIcon("ic_reward"); // TODO implement icon selection logic
        return reward;
    }

    /**
     * Handles the reward creation process, including feedback to the user.
     *
     * @param reward The reward object to be created.
     */
    private void handleRewardCreation(Reward reward) {
        OperationResponse<Reward> response = rewardController.createReward(reward);
        if (response.isSuccessful()) {
            Toast.makeText(requireContext(), "Reward created successfully!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        }
        else
            Toast.makeText(requireContext(), "Failed to create reward. Try again.", Toast.LENGTH_SHORT).show();
    }
}