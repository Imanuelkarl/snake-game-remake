package com.darealfungames.snakevsblock.ui.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.ui.core.BaseView;
import com.darealfungames.snakevsblock.ui.core.ListItemView;
import com.darealfungames.snakevsblock.ui.data.SkinData;
import com.darealfungames.snakevsblock.utils.ActorFactory;

public class SkinCard extends Group implements ListItemView<SkinData>, BaseView {
    private Image background;
    private Image thumbnail;
    private Label nameLabel;
    private Label priceLabel;
    private Label statusLabel;
    private SkinData currentData;
    private int currentPosition;

    public SkinCard() {
        setupUI();
    }

    private void setupUI() {
        // Background
        background = new Image(new TextureRegionDrawable(Assets.getInstance().defaultItemTexture));
        background.setSize(280, 100);
        addActor(background);

        // Thumbnail
        thumbnail = new Image();
        thumbnail.setSize(10, 10);
        thumbnail.setPosition(10, 10);
        addActor(thumbnail);

        // Name label
        Label.LabelStyle nameStyle = new Label.LabelStyle();
        nameStyle.fontColor = Color.WHITE;
        nameLabel = ActorFactory.createTextLabel(24,100,60);
        nameLabel.setPosition(100, 60);
        addActor(nameLabel);

        // Price label
        Label.LabelStyle priceStyle = new Label.LabelStyle();
        priceStyle.fontColor = Color.YELLOW;
        priceLabel = ActorFactory.createTextLabel(24,0,0);
        priceLabel.setPosition(100, 35);
        addActor(priceLabel);

        // Status label (Owned/Equipped)
        Label.LabelStyle statusStyle = new Label.LabelStyle();
        statusStyle.fontColor = Color.GREEN;
        statusLabel = ActorFactory.createTextLabel(24,0,0);
        statusLabel.setPosition(100, 10);
        addActor(statusLabel);
    }

    @Override
    public void bind(SkinData skin, int position) {
        this.currentData = skin;
        this.currentPosition = position;

        thumbnail.setDrawable(new TextureRegionDrawable(skin.thumbnail));
        nameLabel.setText(skin.name);

        if (skin.isOwned) {
            priceLabel.setText("");
            if (skin.isEquipped) {
                statusLabel.setText("EQUIPPED");
                statusLabel.setColor(Color.GREEN);
            } else {
                statusLabel.setText("OWNED");
                statusLabel.setColor(Color.BLUE);
            }
        } else {
            priceLabel.setText(skin.price + " COINS");
            statusLabel.setText("");
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
    public ViewState getState() {
        return null;
    }

    @Override
    public void recycle() {
        // Clean up resources
        thumbnail.setDrawable(null);
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
