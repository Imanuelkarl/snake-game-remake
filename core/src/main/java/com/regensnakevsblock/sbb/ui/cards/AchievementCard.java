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

public class AchievementCard extends Group implements ListItemView<AchievementData>, BaseView<AchievementData> {
    private Label titleLabel;
    private Label descriptionLabel;
    private ProgressBar progressBar;
    private Image background;

    private Label nameLabel;
    private Label progressLabel;
    private Label rewardLabel;
    public ImageButton actionButton;
    private Image currencyIcon;
    private Group buttonGroup;
    private float width =700;
    private float height=200;
    private int currentPosition;

    private AchievementData currentData;

    public AchievementCard() {
        setupUI();
    }

    private void setupUI() {
        float widthRatio = (float) (Constants.SCREEN_SIZE * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        width = widthRatio-20;
        background = new Image(new TextureRegionDrawable(Assets.getInstance().upgradeCardBG));
        background.setSize(width-40, 180);
        background.setPosition(20,0);
        addActor(background);
        // Title
        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.fontColor = Color.GREEN;//new Color(0.5f,0.8f,0.9f,1);
        titleLabel = ActorFactory.createTextLabel("",44, titleStyle);
        titleLabel.setPosition(36, 130);
        addActor(titleLabel);

        // Description
        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.fontColor = Color.WHITE;
        descriptionLabel = ActorFactory.createTextLabel("", 27,descStyle);
        descriptionLabel.setPosition(40, 90);
        addActor(descriptionLabel);





        Image icon = new Image(new TextureRegionDrawable(Assets.getInstance().diamondTexture));
        icon.setSize(height-100, height-100);
        icon.setPosition(20,20);
        //addActor(icon);
        // Name
        nameLabel = ActorFactory.createTextLabel(36, 280, 80);
        nameLabel.setPosition(55, height-60);
        addActor(nameLabel);



        //Upgrade button
        actionButton = ActorFactory.createButton(Assets.getInstance().goButtonTexture, 110,0,200,90);

        //Currency to purchase with
        currencyIcon = ActorFactory.createUiImage(Assets.getInstance().diamondTexture, 0,20,30,30);

        // Reward
        rewardLabel =  ActorFactory.createTextLabel(30, 60, 35);
        rewardLabel.setAlignment(Align.center);


        buttonGroup=new Group();
        buttonGroup.addActor(actionButton);
        buttonGroup.addActor(currencyIcon);
        buttonGroup.addActor(rewardLabel);
        buttonGroup.setPosition((width-40)-300,60);
        addActor(buttonGroup);
        float progressStartX = 30;

        // Progress bar
        progressBar =ActorFactory.createProgressBar(Assets.getInstance().upgradeProgressBG, Assets.getInstance().upgradeProgressFill,250,40);
        progressBar.setSize(width-60 , 15);
        progressBar.setPosition(progressStartX, 20);
        //addActor(progressBar);

        progressLabel =  ActorFactory.createTextLabel(24, 280, 80);
        progressLabel.setPosition((width-60)/3 *1.5f, 25);
        //addActor(progressLabel);
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

        if (achievement.completed && !achievement.claimed) {

            ActorFactory.updateButtonStyle(actionButton,Assets.getInstance().claimButtonTexture);

        } else if (achievement.claimed) {
            ActorFactory.updateButtonStyle(actionButton,Assets.getInstance().claimedButtonTexture) ;
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
        if (currentData.completed && !currentData.claimed) {
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
    public void updateData(AchievementData data, int position) {

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
