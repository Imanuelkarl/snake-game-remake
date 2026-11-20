package com.regensnakevsblock.sbb.ui.adapters;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.regensnakevsblock.sbb.ui.cards.AchievementCard;
import com.regensnakevsblock.sbb.ui.core.BaseAdapter;
import com.regensnakevsblock.sbb.ui.data.AchievementData;

import java.util.List;

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
        view.actionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (claimListener != null) {
                    claimListener.onClaimReward(getItem(position),position);

                }
                view.bind(getItem(position), position);
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        // Implementation
        reOrderAchievements(getItems());

    }
    private void reOrderAchievements(List<AchievementData> achievements) {



        achievements.sort((a, b) -> {
            int priorityA = getAchievementPriority(a);
            int priorityB = getAchievementPriority(b);

            return Integer.compare(priorityA, priorityB);
        });
    }

    private int getAchievementPriority(AchievementData achievement) {

        // Completed but not claimed → TOP
        if (achievement.completed && !achievement.claimed) {
            return 0;
        }

        // Not completed and not claimed → MIDDLE
        if (!achievement.completed && !achievement.claimed) {
            return 1;
        }

        // Completed and claimed → BOTTOM
        if (achievement.completed && achievement.claimed) {
            return 2;
        }

        return 1;
    }

    public void setOnClaimRewardListener(OnClaimRewardListener listener) {
        this.claimListener = listener;
    }
}

