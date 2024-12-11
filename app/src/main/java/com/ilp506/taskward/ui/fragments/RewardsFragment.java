package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.RewardController;
import com.ilp506.taskward.controllers.UserController;
import com.ilp506.taskward.data.models.Reward;
import com.ilp506.taskward.data.models.User;
import com.ilp506.taskward.ui.MainActivity;
import com.ilp506.taskward.ui.adapters.RewardsAdapter;
import com.ilp506.taskward.utils.NavigationHelper;
import com.ilp506.taskward.utils.OperationResponse;

import java.util.List;

/**
 * Fragment for displaying rewards in the TaskWard app.
 */
public class RewardsFragment extends Fragment {
    private RewardController rewardController;
    private NavigationHelper navigationHelper;

    private TextView headerTitle;
    private RecyclerView recyclerView;
    private Button createRewardButton;

    public RewardsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rewardController = new RewardController(requireContext());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupNavigationHelper(view);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        initializeUI(view);
        setupComponents();
        return view;
    }

    /**
     * Initializes the UI components of the fragment.
     *
     * @param view The view of the fragment
     */
    private void initializeUI(@NonNull View view) {
        headerTitle = view.findViewById(R.id.fragmentTitle);
        recyclerView = view.findViewById(R.id.recyclerViewRewards);
        createRewardButton = view.findViewById(R.id.createRewardButton);
    }

    /**
     * Sets up the components of the fragment.
     */
    private void setupComponents() {
        headerTitle.setText(R.string.to_get_rewards_header);
    }

    /**
     * Loads the rewards and sets up the rewards adapter.
     */
    private void loadRewards() {
        OperationResponse<List<Reward>> response = rewardController.getAllRewards();

        if (response.isSuccessful()) {
            List<Reward> rewards = response.getData();

            GridLayoutManager gridLayoutManager = new GridLayoutManager(requireContext(), 2);
            recyclerView.setLayoutManager(gridLayoutManager);

            RewardsAdapter adapter = new RewardsAdapter(rewards, this::onRewardClick);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(requireContext(), "Error loading rewards", Toast.LENGTH_SHORT).show();
    }

    /**
     * Handles click events on reward items.
     *
     * @param reward The clicked reward
     */
    public void onRewardClick(@NonNull Reward reward) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Confirm Redemption")
                .setMessage("Do you want to redeem " + reward.getTitle() +
                        " for " + reward.getPointsRequired() + " points?")
                .setPositiveButton("Yes", (dialog, which) -> redeemReward(reward))
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Redeems a reward if the user confirms.
     *
     * @param reward The reward to redeem
     */
    private void redeemReward(@NonNull Reward reward) {
        UserController userController = new UserController(requireContext());
        OperationResponse<User> userResponse = userController.getUserById(reward.getUserId());

        if (!userResponse.isSuccessful()) {
            Toast.makeText(requireContext(), "Error loading user for redeem reward", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userResponse.getData();
        if (user.getPoints() < reward.getPointsRequired()) {
            Toast.makeText(requireContext(), "You don't have enough points", Toast.LENGTH_SHORT).show();
            return;
        }

        OperationResponse<Void> redeemResponse = rewardController.redeemReward(reward.getId());
        if (redeemResponse.isSuccessful())
            updateUserPoints(reward.getUserId());
        else
            Toast.makeText(requireContext(), "Error redeeming reward", Toast.LENGTH_SHORT).show();
    }

    /**
     * Updates the user points based on the task event.
     *
     * @param userId The user ID
     */
    private void updateUserPoints(int userId) {
        UserController userController = new UserController(requireContext());
        OperationResponse<User> userResponse = userController.getUserById(userId);

        if (userResponse.isSuccessful())
            navigationHelper.updatePoints(userResponse.getData().getPoints());
        else
            Toast.makeText(requireContext(), "Error updating points", Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets up the navigation helper for the fragment.
     * @param view The view of the fragment
     */
    private void setupNavigationHelper(@NonNull View view) {
        MainActivity activity = (MainActivity) requireActivity();
        LiveData<NavigationHelper> helperLiveData = activity.getNavigationHelperLiveData();

        helperLiveData.observe(getViewLifecycleOwner(), helper -> {
            if (helper != null) {
                navigationHelper = helper;
                loadRewards();
                createRewardButton.setOnClickListener(v ->
                        navigationHelper.navigateTo(R.id.action_rewardsFragment_to_createRewardsFragment)
                );
            }
        });
    }
}