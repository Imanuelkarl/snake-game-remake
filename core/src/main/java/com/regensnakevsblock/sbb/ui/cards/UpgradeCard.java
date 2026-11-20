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
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.ui.core.BaseView;
import com.regensnakevsblock.sbb.ui.core.ListItemView;
import com.regensnakevsblock.sbb.ui.data.UpgradeData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class UpgradeCard extends Group implements ListItemView<UpgradeData>, BaseView<UpgradeData> {

    private Image background;

    private Image icon;
    private Label nameLabel;
    private Label levelLabel;
    private Label costLabel;
    public ImageButton upgradeButton;
    private Image currencyIcon;
    private Group buttonGroup;
    private ProgressBar progressBar;
    private UpgradeData currentData;
    private float width =700;
    private float height=300;
    private int currentPosition;

    public UpgradeCard() {
        setupUI();
    }

    private void setupUI() {
        float widthRatio = (float) (Constants.SCREEN_SIZE * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        width = widthRatio-20;
        background = new Image(new TextureRegionDrawable(Assets.getInstance().upgradeCardBG));
        background.setSize(width-40, 260);
        background.setPosition(20,20);
        addActor(background);


        icon = new Image(new TextureRegionDrawable(Assets.getInstance().coinTexture));
        icon.setSize(height-100, height-100);
        icon.setPosition(20,20);
        addActor(icon);
        // Name
        nameLabel = ActorFactory.createTextLabel(36, 280, 80);
        nameLabel.setPosition(55, height-60);
        addActor(nameLabel);



        //Upgrade button
        upgradeButton = ActorFactory.createButton(Assets.getInstance().upgradeItemButton, 0,0,230,90);

        //Currency to purchase with
        currencyIcon = ActorFactory.createUiImage(Assets.getInstance().coinTexture, 50,5,30,30);

        // Cost
        costLabel =  ActorFactory.createTextLabel(30, 110, 20);
        costLabel.setAlignment(Align.center);


        buttonGroup=new Group();
        buttonGroup.addActor(upgradeButton);
        buttonGroup.addActor(currencyIcon);
        buttonGroup.addActor(costLabel);
        buttonGroup.setPosition((width-40)-220,180);
        addActor(buttonGroup);
        float progressStartX = (width-80)/3;

        // Progress bar
        progressBar =ActorFactory.createProgressBar(Assets.getInstance().upgradeProgressBG, Assets.getInstance().upgradeProgressFill,250,40);
        progressBar.setSize(width-width/3, 15);
        progressBar.setPosition(progressStartX, 50);
        addActor(progressBar);

        levelLabel =  ActorFactory.createTextLabel(24, 280, 80);
        levelLabel.setPosition((width-80)/3 *2, 55);
        addActor(levelLabel);
    }

    @Override
    public void bind(UpgradeData upgrade, int position) {
        this.currentData = upgrade;
        this.currentPosition = position;

        nameLabel.setText(upgrade.name);
        levelLabel.setText("Lv." + upgrade.currentLevel + "/" + upgrade.maxLevel);
        levelLabel.setColor(Color.WHITE);
        icon.setDrawable(new TextureRegionDrawable(GameInstance.getInstance().getPowerUpRegion(upgrade.name)));
        if(upgrade.currentLevel>=8){
            currencyIcon.setDrawable(new TextureRegionDrawable(Assets.getInstance().diamondTexture));
        }
        if (upgrade.currentLevel < upgrade.maxLevel) {
            costLabel.setText(upgrade.upgradeCost);
            costLabel.setColor(Color.DARK_GRAY);
        } else {
            currencyIcon.remove();
            costLabel.setText("MAXED");
            costLabel.setColor(new Color(0,0.1f,0,1));
        }

        progressBar.setValue((float) upgrade.currentLevel / upgrade.maxLevel);
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
        // Trigger upgrade action
    }

    @Override
    public void onLongClick() {
        // Show upgrade details
    }

    @Override
    public void update(float delta) {
        // Animate progress bar or value changes
    }

    @Override
    public void updateData(UpgradeData data, int position) {

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
    public UpgradeData getItem() {
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
