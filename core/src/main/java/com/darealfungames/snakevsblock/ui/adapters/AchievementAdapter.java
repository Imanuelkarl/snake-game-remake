package com.darealfungames.snakevsblock.ui.adapters;


import com.darealfungames.snakevsblock.ui.cards.AchievementCard;
import com.darealfungames.snakevsblock.ui.core.BaseAdapter;
import com.darealfungames.snakevsblock.ui.data.AchievementData;

public class AchievementAdapter extends BaseAdapter<AchievementData, AchievementCard> {
    private OnClaimRewardListener claimListener;

    public interface OnClaimRewardListener {
        void onClaimReward(AchievementData achievement, int position);
    }

    @Override
    public AchievementCard createView(int position) {
        return new AchievementCard();
    }

    @Override
    public void bindView(AchievementCard view, int position) {
        view.bind(getItem(position), position);
    }

    @Override
    public void notifyDataSetChanged() {
        // Implementation
    }

    public void setOnClaimRewardListener(OnClaimRewardListener listener) {
        this.claimListener = listener;
    }
}

