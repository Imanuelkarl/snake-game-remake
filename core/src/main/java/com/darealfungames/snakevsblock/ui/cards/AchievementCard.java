package com.darealfungames.snakevsblock.ui.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.darealfungames.snakevsblock.ui.core.BaseView;
import com.darealfungames.snakevsblock.ui.core.ListItemView;
import com.darealfungames.snakevsblock.ui.data.AchievementData;

public class AchievementCard extends Group implements ListItemView<AchievementData>, BaseView {
    private Label titleLabel;
    private Label descriptionLabel;
    private Label progressLabel;
    private Label rewardLabel;
    private ProgressBar progressBar;
    private TextButton claimButton;
    private AchievementData currentData;
    private int currentPosition;

    public AchievementCard() {
        setupUI();
    }

    private void setupUI() {
        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.fontColor = Color.WHITE;
        titleLabel = new Label("", titleStyle);
        titleLabel.setPosition(10, 70);
        addActor(titleLabel);

        // Description
        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.fontColor = Color.LIGHT_GRAY;
        descStyle.font.getData().setScale(0.8f);
        descriptionLabel = new Label("", descStyle);
        descriptionLabel.setPosition(10, 50);
        addActor(descriptionLabel);

        // Progress label
        Label.LabelStyle progressStyle = new Label.LabelStyle();
        progressStyle.fontColor = Color.CYAN;
        progressLabel = new Label("", progressStyle);
        progressLabel.setPosition(10, 30);
        addActor(progressLabel);

        // Reward label
        Label.LabelStyle rewardStyle = new Label.LabelStyle();
        rewardStyle.fontColor = Color.YELLOW;
        rewardLabel = new Label("", rewardStyle);
        rewardLabel.setPosition(200, 30);
        addActor(rewardLabel);

        // Progress bar
        progressBar = new ProgressBar(0, 1, 0.01f, false, new Skin());
        progressBar.setSize(200, 10);
        progressBar.setPosition(10, 15);
        addActor(progressBar);

        // Claim button
        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        claimButton = new TextButton("CLAIM", buttonStyle);
        claimButton.setSize(80, 25);
        claimButton.setPosition(210, 10);
        addActor(claimButton);
    }

    @Override
    public void bind(AchievementData achievement, int position) {
        this.currentData = achievement;
        this.currentPosition = position;

        titleLabel.setText(achievement.title);
        descriptionLabel.setText(achievement.description);

        float percentage = (float) achievement.progress / achievement.target;
        progressLabel.setText(achievement.progress + "/" + achievement.target);
        rewardLabel.setText("+" + achievement.reward);
        progressBar.setValue(percentage);

        if (achievement.isCompleted && !achievement.isClaimed) {
            claimButton.setVisible(true);
            claimButton.setColor(Color.GREEN);
        } else if (achievement.isClaimed) {
            claimButton.setVisible(true);
            claimButton.setText("CLAIMED");
            claimButton.setColor(Color.GRAY);
        } else {
            claimButton.setVisible(false);
        }
    }

    @Override
    public void onSelected() {
        setColor(Color.LIGHT_GRAY);
    }

    @Override
    public void onDeselected() {
        setColor(Color.WHITE);
    }

    @Override
    public void onClick() {
        if (currentData.isCompleted && !currentData.isClaimed) {
            // Claim reward
        }
    }

    @Override
    public void onLongClick() {
        // Show achievement details
    }

    @Override
    public void update(float delta) {
        // Update progress animations
    }

    @Override
    public void resize(float width, float height) {

    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public ViewState getState() {
        return null;
    }

    @Override
    public void recycle() {
        currentData = null;
    }

    @Override
    public AchievementData getItem() {
        return currentData;
    }

    @Override
    public int getPosition() {
        return currentPosition;
    }

    @Override
    public Group getRoot() {
        return this;
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }
}
