package com.ilp506.taskward.ui.adapters;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ilp506.taskward.R;
import com.ilp506.taskward.data.models.Reward;

import java.util.List;

/**
 * Adapter for displaying rewards in the TaskWard app.
 */
public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardViewHolder> {
    private static final String TAG = RewardsAdapter.class.getSimpleName();

    private final List<Reward> rewards;
    private final OnRewardClickListener onRewardClickListener;

    /**
     * Interface for handling reward click events.
     */
    public interface OnRewardClickListener {
        void onRewardClick(Reward reward);
    }

    /**
     * Constructor for the RewardsAdapter.
     * This method initializes the adapter with a list of rewards and a listener for reward click events.
     *
     * @param rewards The list of rewards to be displayed.
     * @param onRewardClickListener The listener for handling reward click events.
     */
    public RewardsAdapter(List<Reward> rewards, OnRewardClickListener onRewardClickListener) {
        this.rewards = rewards;
        this.onRewardClickListener = onRewardClickListener;
    }

    /**
     * Creates and returns a new ViewHolder to display a reward.
     * This method inflates the layout for an individual reward item and wraps it in a RewardViewHolder.
     *
     * @param parent The parent ViewGroup to which the new View will be attached.
     * @param viewType The view type of the new View.
     * @return A new instance of RewardViewHolder containing the inflated View.
     */
    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_item, parent, false);
        return new RewardViewHolder(view);
    }

    /**
     * Binds the data for a specific position to the ViewHolder.
     * This method updates the contents of the ViewHolder to reflect the data at the given position in the dataset.
     *
     * @param holder The ViewHolder that should be updated.
     * @param position The position of the item in the dataset.
     */
    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward reward = rewards.get(position);
        holder.bind(reward);
    }

    /**
     * Returns the total number of rewards in the adapter.
     * This method provides the total number of items in the dataset, which is required by RecyclerView.
     *
     * @return The total number of rewards in the list.
     */
    @Override
    public int getItemCount() {
        return rewards.size();
    }

    /**
     * ViewHolder class for rewards items.
     * This inner class holds the views for each item and provides methods to bind data to these views.
     */
    public class RewardViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView points;
        private final ImageView pointsIcon;

        /**
         * Constructor for the RewardViewHolder.
         * This method initializes the views for an individual reward item.
         *
         * @param itemView The root View for the reward item.
         */
        public RewardViewHolder(android.view.View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rewardTitle);
            points = itemView.findViewById(R.id.rewardPoints);
            pointsIcon = itemView.findViewById(R.id.rewardPointsIcon);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    if (onRewardClickListener != null) {
                        Reward reward = rewards.get(position);
                        onRewardClickListener.onRewardClick(reward);
                    }
                }
            });
        }

        /**
         * Binds the reward data to the views.
         * This method populates the TextViews with data from a given Reward.
         *
         * @param reward The Reward object containing the data to bind.
         */
        public void bind(@NonNull Reward reward) {
            title.setText(reward.getTitle());
            points.setText(String.valueOf(reward.getPointsRequired()));
            updateRewardAppearance(reward.isRedeemed());
        }

        /**
         * Updates the visual appearance of the reward based on its redemption status.
         * This method changes the background and text colors of the views based on whether the reward has been redeemed or not.
         *
         * @param isRedeemed True if the reward has been redeemed, false otherwise.
         */
        private void updateRewardAppearance(boolean isRedeemed) {
            if (isRedeemed) {
                itemView.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.gray_500_50))
                );
                title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white_100));
                points.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.white_100));
                pointsIcon.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.white_100))
                );
            }
            else {
                itemView.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.blue_300))
                );
                title.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blue_500));
                points.setTextColor(ContextCompat.getColor(itemView.getContext(), R.color.blue_500));
                pointsIcon.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(itemView.getContext(), R.color.blue_500))
                );
            }
        }
    }
}
