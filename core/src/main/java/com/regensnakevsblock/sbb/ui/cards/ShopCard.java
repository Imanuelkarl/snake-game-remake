package com.regensnakevsblock.sbb.ui.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.ui.core.BaseView;
import com.regensnakevsblock.sbb.ui.core.ListItemView;
import com.regensnakevsblock.sbb.ui.data.ShopItemData;
import com.regensnakevsblock.sbb.utils.ActorFactory;
import com.regensnakevsblock.sbb.utils.FontActor;

public class ShopCard extends Group implements ListItemView<ShopItemData>, BaseView {
    private Image background;
    private Image icon;
    private Label nameLabel;
    private Label descriptionLabel;
    private Label priceLabel;
    private FontActor priceText;
    private Label itemQuantity;
    private Image priceBackground;
    private Label orignalPriceLabel;
    private Image discountBadge;
    private Image cancelPriceLine;
    private ImageButton actionButton;
    private ShopItemData currentData;

    private int currentPosition;

    public ShopCard() {
        setupUI();
    }

    private void setupUI() {
        // Background
        background = new Image(new TextureRegionDrawable(Assets.getInstance().cardBackgroundTexture));
        addActor(background);

        // Icon
        icon = new Image(new TextureRegionDrawable(Assets.getInstance().defaultItemTexture));
        addActor(icon);

        // Name
        Label.LabelStyle nameStyle = new Label.LabelStyle();
        nameStyle.fontColor = Color.WHITE;
        nameLabel = ActorFactory.createTextLabel("", nameStyle);
        //addActor(nameLabel);

        // Description
        Label.LabelStyle descStyle = new Label.LabelStyle();
        descStyle.fontColor = Color.LIGHT_GRAY;
        descriptionLabel = ActorFactory.createTextLabel("", 30,descStyle);
        //addActor(descriptionLabel);

        //Price Background
        priceBackground = new Image(new TextureRegionDrawable(Assets.getInstance().priceBarTexture));//card background for now
        //addActor(priceBackground);

        actionButton = ActorFactory.createButton(Assets.getInstance().priceBarTexture,12.5f,20,220,80);
        addActor(actionButton);
        // Price
        Label.LabelStyle priceStyle = new Label.LabelStyle();
        priceLabel = ActorFactory.createTextLabel("",45, priceStyle);
        //addActor(priceLabel);

        priceText = new FontActor(GameInstance.getInstance().getSimpleBitmapFont(),"");
        priceText.setSize(70,30);
        addActor(priceText);

        //Item Quantity
        itemQuantity = ActorFactory.createTextLabel("",36, priceStyle);
        addActor(itemQuantity);

        //Action Button

    }

    @Override
    public void bind(ShopItemData item, int position) {
        this.currentData = item;
        this.currentPosition = position;

        icon.setDrawable(new TextureRegionDrawable(item.itemIcon));
        nameLabel.setText(item.name);
        descriptionLabel.setText(item.description);
        itemQuantity.setText(item.itemQuantity > 1 ? "x" + item.itemQuantity : "");

        if (item.isPurchased) {
            priceLabel.setText("PURCHASED");
        } else {
            String currencySymbol = item.currencyType == ShopItemData.CurrencyType.COINS ? "₦" : "$";
            priceLabel.setText(currencySymbol+item.price + " " );
            priceText.setText(currencySymbol+item.price + " " );
        }
    }
    private  void layout() {
        background.setSize(220, 310);
        background.setPosition(12.5f,20);
        // Adjust positions based on content

        icon.setSize(100, 110);
        icon.setPosition(80, 150);

        nameLabel.setPosition(80, 65);

        descriptionLabel.setPosition(80, 45);

        float priceX = this.getWidth()/2 - priceLabel.getPrefWidth()/2+10 ;
        float priceWidth =110;

        priceBackground.setSize(priceWidth,30);
        priceBackground.setPosition(70,115);


        priceLabel.setPosition(priceX, 70);

        priceX =110-priceText.getWidth()/3;
        priceText.setPosition(priceX,40);

        itemQuantity.setPosition(20, 300);
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
        layout();
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
