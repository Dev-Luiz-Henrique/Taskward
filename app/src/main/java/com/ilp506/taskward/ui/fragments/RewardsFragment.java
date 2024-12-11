package com.ilp506.taskward.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ilp506.taskward.R;
import com.ilp506.taskward.controllers.RewardController;
import com.ilp506.taskward.data.models.Reward;
import com.ilp506.taskward.ui.adapters.RewardsAdapter;
import com.ilp506.taskward.utils.OperationResponse;

import java.util.List;

/**
 * Fragment for displaying rewards in the TaskWard app.
 */
public class RewardsFragment extends Fragment {
    private RewardController rewardController;

    private TextView headerTitle;
    private RecyclerView recyclerView;

    public RewardsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_rewards, container, false);

        initializeUI(view);
        setupComponents();
        loadRewards();
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

            RewardsAdapter adapter = new RewardsAdapter(rewards);
            recyclerView.setAdapter(adapter);
        }
        else
            Toast.makeText(requireContext(), "Error loading rewards", Toast.LENGTH_SHORT).show();
    }
}