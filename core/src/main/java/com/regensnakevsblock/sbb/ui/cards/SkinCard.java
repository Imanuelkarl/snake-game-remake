package com.regensnakevsblock.sbb.ui.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.ui.core.BaseView;
import com.regensnakevsblock.sbb.ui.core.ListItemView;
import com.regensnakevsblock.sbb.ui.data.SkinData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class SkinCard extends Group implements ListItemView<SkinData>, BaseView {
    private Image background;
    private Image coins;
    private ImageButton actionButton;
    private Label priceLabel;

    private SkinData currentData;
    private SnakePreview snakePreview;
    private int currentPosition;

    public SkinCard() {
        setupUI();
    }

    private void setupUI() {
        // Background
        background = new Image(new TextureRegionDrawable(Assets.getInstance().cardBackgroundTexture));
        background.setSize(220, 310);
        background.setPosition(12.5f,20);
        addActor(background);

        coins = new Image(new TextureRegionDrawable(Assets.getInstance().coinTexture));
        coins.setSize(24, 24);
        coins.setPosition(20, 280);
        addActor(coins);
        snakePreview = new SnakePreview();
        snakePreview.setBounds(100,40,40,240);
        addActor(snakePreview);

        actionButton = ActorFactory.createButton(Assets.getInstance().buyButtonTexture,12.5f,20,220,80);
        addActor(actionButton);



        // Price label
        Label.LabelStyle priceStyle = new Label.LabelStyle();
        priceStyle.fontColor = Color.YELLOW;
        priceLabel = ActorFactory.createTextLabel(30,0,0);
        priceLabel.setPosition(55, 290);
        addActor(priceLabel);

    }

    @Override
    public void bind(SkinData skin, int position) {
        this.currentData = skin;
        this.currentPosition = position;

        //nameLabel.setText(skin.name);
        snakePreview.setSkin(skin.thumbnail);

        if (skin.isOwned) {
            priceLabel.setText("");
            if (skin.isEquipped) {
                background.setDrawable(new TextureRegionDrawable(Assets.getInstance().equippedBorderTexture));
                ActorFactory.updateButtonStyle(actionButton, Assets.getInstance().selectedButtonTexture);
                snakePreview.setSelected(true);
                //statusLabel.setText("EQUIPPED");
                //statusLabel.setColor(Color.GREEN);
            } else {
                ActorFactory.updateButtonStyle(actionButton, Assets.getInstance().selectButtonTexture);
                snakePreview.setSelected(false);
                //statusLabel.setText("OWNED");
                //statusLabel.setColor(Color.BLUE);
            }
        } else {
            priceLabel.setText(skin.price );
            //statusLabel.setText("");
        }
    }

    @Override
    public void onSelected() {
        background.setColor(Color.LIGHT_GRAY);
    }

    @Override
    public void onDeselected() {
        background.setColor(Color.WHITE);
    }

    @Override
    public void onClick() {
        // Handle click - will be triggered by ListManager
    }

    @Override
    public void onLongClick() {
        // Preview skin on long press
    }

    @Override
    public void update(float delta) {
        // Any animations or updates
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
        // Clean up resources
        currentData = null;
    }

    @Override
    public SkinData getItem() {
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
