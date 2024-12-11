package com.ilp506.taskward.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ilp506.taskward.R;
import com.ilp506.taskward.data.models.Reward;

import java.util.List;

public class RewardsAdapter extends RecyclerView.Adapter<RewardsAdapter.RewardViewHolder> {
    private static final String TAG = RewardsAdapter.class.getSimpleName();

    private final List<Reward> rewards;
    private final OnRewardClickListener onRewardClickListener;

    public interface OnRewardClickListener {
        void onRewardClick(Reward reward);
    }

    public RewardsAdapter(List<Reward> rewards, OnRewardClickListener onRewardClickListener) {
        this.rewards = rewards;
        this.onRewardClickListener = onRewardClickListener;
    }

    @NonNull
    @Override
    public RewardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reward_item, parent, false);
        return new RewardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RewardViewHolder holder, int position) {
        Reward reward = rewards.get(position);
        holder.bind(reward);
    }

    @Override
    public int getItemCount() {
        return rewards.size();
    }

    public class RewardViewHolder extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView points;

        public RewardViewHolder(android.view.View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.rewardTitle);
            points = itemView.findViewById(R.id.rewardPoints);

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

        public void bind(@NonNull Reward reward) {
            title.setText(reward.getTitle());
            points.setText(String.valueOf(reward.getPointsRequired()));
        }
    }
}
