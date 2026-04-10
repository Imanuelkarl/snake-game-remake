package com.regensnakevsblock.sbb.ui.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.ui.core.BaseView;
import com.regensnakevsblock.sbb.ui.core.ListItemView;
import com.regensnakevsblock.sbb.ui.data.ShopItemData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class ShopCard extends Group implements ListItemView<ShopItemData>, BaseView {
    private Image background;
    private Image icon;
    private Label nameLabel;
    private Label descriptionLabel;
    private Label priceLabel;
    private ShopItemData currentData;
    private int currentPosition;

    public ShopCard() {
        setupUI();
    }

    private void setupUI() {
        // Background
        background = new Image(new TextureRegionDrawable(Assets.getInstance().menuBGTexture));
        background.setSize(280, 90);
        addActor(background);

        // Icon
        icon = new Image();
        icon.setSize(60, 60);
        icon.setPosition(10, 15);
        addActor(icon);

        // Name
        Label.LabelStyle nameStyle = new Label.LabelStyle();
        nameStyle.fontColor = Color.WHITE;
        nameLabel = ActorFactory.createTextLabel("", nameStyle);
        nameLabel.setPosition(80, 65);
        addActor(nameLabel);

        // Description
        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.fontColor = Color.LIGHT_GRAY;
        descriptionLabel = ActorFactory.createTextLabel("", descStyle);
        descriptionLabel.setPosition(80, 45);
        addActor(descriptionLabel);

        // Price
        Label.LabelStyle priceStyle = new Label.LabelStyle();
        priceStyle.fontColor = Color.YELLOW;
        priceLabel = ActorFactory.createTextLabel("", priceStyle);
        priceLabel.setPosition(80, 20);
        addActor(priceLabel);
    }

    @Override
    public void bind(ShopItemData item, int position) {
        this.currentData = item;
        this.currentPosition = position;

        icon.setDrawable(new TextureRegionDrawable(item.icon));
        nameLabel.setText(item.name);
        descriptionLabel.setText(item.description);

        if (item.isPurchased) {
            priceLabel.setText("PURCHASED");
            priceLabel.setColor(Color.GREEN);
        } else {
            String currencySymbol = item.currencyType == ShopItemData.CurrencyType.COINS ? "🪙" : "💎";
            priceLabel.setText(item.price + " " + currencySymbol);
            priceLabel.setColor(item.currencyType == ShopItemData.CurrencyType.COINS ? Color.YELLOW : Color.CYAN);
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
        // Handle purchase
    }

    @Override
    public void onLongClick() {
        // Show item details
    }

    @Override
    public void update(float delta) {
        // Animate if needed
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
        icon.setDrawable(null);
        currentData = null;
    }

    @Override
    public ShopItemData getItem() {
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
