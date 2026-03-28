package com.darealfungames.snakevsblock.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darealfungames.snakevsblock.ui.adapters.AchievementAdapter;
import com.darealfungames.snakevsblock.ui.cards.AchievementCard;
import com.darealfungames.snakevsblock.ui.core.ListManager;
import com.darealfungames.snakevsblock.ui.data.AchievementData;

public class AchievementsView extends BaseActorView {
    private ListManager<AchievementData, AchievementCard> listManager;
    private AchievementAdapter adapter;
    private Label titleLabel;
    private Label totalRewardsLabel;

    public AchievementsView(Group group) {
        super(group);
        setupUI();
        setupList();
        loadData();
    }

    private void setupUI() {
        // Title
        titleLabel = new Label("ACHIEVEMENTS", new Label.LabelStyle());
        titleLabel.setPosition(20, Gdx.graphics.getHeight() - 50);
        addActor(titleLabel);

        // Total rewards
        totalRewardsLabel = new Label("Total Rewards: 0", new Label.LabelStyle());
        totalRewardsLabel.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 50);
        addActor(totalRewardsLabel);
    }

    private void setupList() {

        adapter = new AchievementAdapter();
        listManager = new ListManager<>(adapter);

        // Vertical layout with larger cards for achievements
        listManager.setLayout(ListManager.ListLayout.VERTICAL, 1, 300, 120);
        listManager.setAdapter(adapter);
        listManager.setSize(Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() - 100);
        listManager.setPosition(20, 20);

        addActor(listManager);

        adapter.setOnItemClickListener((achievement, position) -> {
            if (achievement.isCompleted && !achievement.isClaimed) {
                claimReward(achievement, position);
            }
        });
    }

    private void loadData() {
        java.util.List<AchievementData> achievements = new java.util.ArrayList<>();
        achievements.add(new AchievementData("ach_1", "First Blood", "Kill 10 enemies",
            7, 10, 500, false, false));
        achievements.add(new AchievementData("ach_2", "Rich Player", "Collect 10,000 coins",
            2500, 10000, 2000, false, false));
        achievements.add(new AchievementData("ach_3", "Master Swordsman", "Reach level 10",
            5, 10, 1000, false, false));
        achievements.add(new AchievementData("ach_4", "Explorer", "Visit 5 different locations",
            3, 5, 1500, false, false));

        adapter.setData(achievements);
        updateTotalRewards();
    }

    private void claimReward(AchievementData achievement, int position) {
        if (achievement.isCompleted && !achievement.isClaimed) {
            achievement.isClaimed = true;
            // Add reward to player
            addPlayerCoins(achievement.reward);

            adapter.notifyDataSetChanged();
            updateTotalRewards();
        }
    }

    private void addPlayerCoins(int amount) {
        // Add to game state
    }

    private void updateTotalRewards() {
        int total = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).isClaimed) {
                total += adapter.getItem(i).reward;
            }
        }
        totalRewardsLabel.setText("Total Rewards: " + total);
    }

    @Override
    public void update(float delta) {
        // Update achievement progress based on game events
    }

    @Override
    public void resize(float width, float height) {
        listManager.onResize(width - 40, height - 100);
    }

    @Override
    public boolean backPressed() {
        return false;
    }
}
