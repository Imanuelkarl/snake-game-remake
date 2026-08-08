package com.regensnakevsblock.sbb.ui.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.ui.core.BaseView;
import com.regensnakevsblock.sbb.ui.core.ListItemView;
import com.regensnakevsblock.sbb.ui.data.AchievementData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class AchievementCard extends Group implements ListItemView<AchievementData>, BaseView {
    private Label titleLabel;
    private Label descriptionLabel;
    private ProgressBar progressBar;
    private Image background;

    private Label nameLabel;
    private Label progressLabel;
    private Label rewardLabel;
    public ImageButton claimButton;
    private Image currencyIcon;
    private Group buttonGroup;
    private float width =700;
    private float height=300;
    private int currentPosition;

    private AchievementData currentData;

    public AchievementCard() {
        setupUI();
    }

    private void setupUI() {
        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.fontColor = Color.WHITE;
        titleLabel = ActorFactory.createTextLabel("", titleStyle);
        titleLabel.setPosition(10, 70);
        addActor(titleLabel);

        // Description
        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.fontColor = Color.LIGHT_GRAY;
        descriptionLabel = ActorFactory.createTextLabel("", descStyle);
        descriptionLabel.setPosition(10, 50);
        addActor(descriptionLabel);


        float widthRatio = (float) (Constants.SCREEN_SIZE * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        width = widthRatio-20;
        background = new Image(new TextureRegionDrawable(Assets.getInstance().upgradeCardBG));
        background.setSize(width-40, 260);
        background.setPosition(20,20);
        addActor(background);


        Image icon = new Image(new TextureRegionDrawable(Assets.getInstance().coinTexture));
        icon.setSize(height-100, height-100);
        icon.setPosition(20,20);
        addActor(icon);
        // Name
        nameLabel = ActorFactory.createTextLabel(36, 280, 80);
        nameLabel.setPosition(55, height-60);
        addActor(nameLabel);



        //Upgrade button
        claimButton = ActorFactory.createButton(Assets.getInstance().defaultButton, 0,0,230,90);

        //Currency to purchase with
        currencyIcon = ActorFactory.createUiImage(Assets.getInstance().coinTexture, 50,5,30,30);

        // Cost
        rewardLabel =  ActorFactory.createTextLabel(30, 110, 20);
        rewardLabel.setAlignment(Align.center);


        buttonGroup=new Group();
        buttonGroup.addActor(claimButton);
        buttonGroup.addActor(currencyIcon);
        buttonGroup.addActor(rewardLabel);
        buttonGroup.setPosition((width-40)-220,180);
        addActor(buttonGroup);
        float progressStartX = (width-80)/3;

        // Progress bar
        progressBar =ActorFactory.createProgressBar(Assets.getInstance().upgradeProgressBG, Assets.getInstance().upgradeProgressFill,250,40);
        progressBar.setSize(width-width/3, 15);
        progressBar.setPosition(progressStartX, 50);
        addActor(progressBar);

        progressLabel =  ActorFactory.createTextLabel(24, 280, 80);
        progressLabel.setPosition((width-80)/3 *2, 55);
        addActor(progressLabel);
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
    public void buildIfNeeded() {

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
