package com.regensnakevsblock.sbb.ui.adapters;


import com.regensnakevsblock.sbb.ui.cards.AchievementCard;
import com.regensnakevsblock.sbb.ui.core.BaseAdapter;
import com.regensnakevsblock.sbb.ui.data.AchievementData;

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

