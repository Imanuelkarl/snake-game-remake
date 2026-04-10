package com.regensnakevsblock.sbb.ui.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.ui.core.BaseView;
import com.regensnakevsblock.sbb.ui.core.ListItemView;
import com.regensnakevsblock.sbb.ui.data.UpgradeData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class UpgradeCard extends Group implements ListItemView<UpgradeData>, BaseView {

    private Image background;

    private Image icon;
    private Label nameLabel;
    private Label descriptionLabel;
    private Label levelLabel;
    private Label costLabel;
    private Label valueLabel;
    private ProgressBar progressBar;
    private UpgradeData currentData;
    private float width =700;
    private float height=300;
    private int currentPosition;

    public UpgradeCard() {
        setupUI();
    }

    private void setupUI() {

        background = new Image(new TextureRegionDrawable(Assets.getInstance().cardBackgroundTexture));
        background.setSize(width-40, 260);
        background.setPosition(20,20);
        addActor(background);

        icon = new Image(new TextureRegionDrawable(Assets.getInstance().coinTexture));
        icon.setSize(height-80, height-80);
        icon.setPosition(40,40);
        addActor(icon);
        // Name
        nameLabel = ActorFactory.createTextLabel(24, 280, 80);
        nameLabel.setPosition(height-40, height-80);
        addActor(nameLabel);

        // Description

        descriptionLabel =  ActorFactory.createTextLabel(24, 280, 80);
        descriptionLabel.setPosition(height-40, height-120);
        addActor(descriptionLabel);


        levelLabel =  ActorFactory.createTextLabel(24, 280, 80);
        levelLabel.setPosition(height-40, height-160);
        addActor(levelLabel);


        valueLabel =  ActorFactory.createTextLabel(24, 280, 80);
        valueLabel.setPosition(height-40, height-200);
        addActor(valueLabel);

        // Cost
        costLabel =  ActorFactory.createTextLabel(24, 280, 80);
        costLabel.setPosition(height+80, height-200);
        addActor(costLabel);

        // Progress bar
        /*progressBar = new ProgressBar(0, 1, 0.01f, false, new Skin());
        progressBar.setSize(280, 15);
        progressBar.setPosition(10, 10);
        addActor(progressBar);*/
    }

    @Override
    public void bind(UpgradeData upgrade, int position) {
        this.currentData = upgrade;
        this.currentPosition = position;

        nameLabel.setText(upgrade.name);
        descriptionLabel.setText(upgrade.description);
        levelLabel.setText("Lv." + upgrade.currentLevel + "/" + upgrade.maxLevel);
        valueLabel.setText(upgrade.currentValue + " → " + upgrade.nextValue);

        if (upgrade.currentLevel < upgrade.maxLevel) {
            costLabel.setText(upgrade.upgradeCost + " COINS");
            costLabel.setColor(Color.YELLOW);
        } else {
            costLabel.setText("MAXED");
            costLabel.setColor(Color.GREEN);
        }

        //progressBar.setValue((float) upgrade.currentLevel / upgrade.maxLevel);
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
