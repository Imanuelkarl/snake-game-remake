package com.regensnakevsblock.sbb.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.systems.AchievementSystem;
import com.regensnakevsblock.sbb.ui.adapters.AchievementAdapter;
import com.regensnakevsblock.sbb.ui.cards.AchievementCard;
import com.regensnakevsblock.sbb.ui.core.ListManager;
import com.regensnakevsblock.sbb.ui.data.AchievementData;
import com.regensnakevsblock.sbb.ui.data.AchievementList;
import com.regensnakevsblock.sbb.utils.ActorFactory;

import java.util.List;

public class AchievementsView extends BaseActorView<AchievementData> implements AchievementSystem.AchievementListener {
    private ListManager<AchievementData, AchievementCard> listManager;
    private AchievementAdapter adapter;
    private Label loadingText;
    private float width,height;
    public AchievementsView(Group group) {
        super(group);
        setSize(group.getWidth(), group.getHeight());
        //setPosition(group.getX(),group.getY());
        this.width=group.getWidth();
        this.height=group.getHeight();

        setupUI();
        setupList();
        setupTransform();
        Gdx.app.postRunnable(this::loadData);
    }

    @Override
    public void updateData(AchievementData data, int position) {
        adapter.updateData(data,position);
    }

    private void setupUI() {
        // Title

        loadingText = ActorFactory.createTextLabel(36, 20, height/2);
        loadingText.setText("Achievement View Is In Development \\:) ");
        loadingText.setPosition(50, height / 2f, Align.center);

        //addActor(loadingText);
    }

    private void setupList() {

        adapter = new AchievementAdapter();
        listManager = new ListManager<>(adapter);

        // Vertical layout with larger cards for achievements
        listManager.setLayout( 1, 300, 120);
        listManager.setSize(Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() - 100);
        listManager.setPosition(20, 20);

        addActor(listManager);

        adapter.setOnClaimRewardListener(new AchievementAdapter.OnClaimRewardListener() {
            @Override
            public void onClaimReward(AchievementData achievement, int position) {
                claimReward(achievement, position);
            }
        });
    }

    private void loadData() {
        adapter.setData(GameInstance.getInstance().getAchievements());
        updateTotalRewards();
    }
    private void setupTransform(){
        loadingText.setPosition(50, height / 2f, Align.center);
        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout( 1, 200);
    }



    private void claimReward(AchievementData achievement, int position) {
        if (achievement.completed && !achievement.claimed) {
            achievement.claimed = true;
            SaveService saveService= GameInstance.getSaveService();
            saveService.saveAchievementIsClaimed(achievement.id,true);
            // Add reward to player
           saveService.saveDiamonds(saveService.getDiamonds()+achievement.reward);
            adapter.notifyDataSetChanged();
        }
    }



    private void updateTotalRewards() {
        int total = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).claimed) {
                total += adapter.getItem(i).reward;
            }
        }

    }

    @Override
    public void update(float delta) {
        // Update achievement progress based on game events
    }

    @Override
    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
        setupTransform();
        listManager.renderAllItems();
    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public void buildIfNeeded() {

    }

    @Override
    public void onAchievementUnlocked(AchievementSystem.Achievement achievement) {
        for (int i = 0; i < GameInstance.getInstance().getAchievements().size(); i++) {
            AchievementData data = GameInstance.getInstance().getAchievements().get(i);
            if (data.id.equals(achievement.id)) {
                updateData(data, i);
                break;
            }
        }
    }

    public void registerWithAchievementSystem(AchievementSystem achievementSystem) {
        achievementSystem.setAchievementListener(this);
    }
}
