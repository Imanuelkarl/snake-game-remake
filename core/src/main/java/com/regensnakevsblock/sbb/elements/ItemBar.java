package com.regensnakevsblock.sbb.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets2;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class ItemBar extends Group {

    private Image background;
    private Image itemIcon;
    private ImageButton addButton;
    private Label amountDisplay;
    private TextureRegion backgroundTexture;

    private TextureRegion itemTexture;
    private TextureRegion addItemTexture;
    private BitmapFont font;

    private String amount = "0";

    private float padding = 10f;
    private float spacing = 5f;

    private float width;
    private float height;
    private GlyphLayout layout;

    public ItemBar(float x,float y, float width, float height) {
        Assets2 assets = Assets2.getInstance();
        this.setPosition(x,y);
        this.setSize(width,height);
        this.width=width;
        this.height=height;
        // Background
        backgroundTexture=assets.sliderBackgroundTexture;
        itemTexture=assets.defaultItemTexture;
        addItemTexture=assets.addButtonTexture;
        layout=new GlyphLayout();
        build();
        layout();
    }

    private void build() {
        background = new Image(backgroundTexture);
        background.setSize(width, height);
        addActor(background);

        // Item Icon
        itemIcon = new Image(itemTexture);
        addActor(itemIcon);

        // Add Button
        addButton = ActorFactory.createButton(addItemTexture,0,0,width,height);
        addActor(addButton);
        font=Assets2.getInstance().font;
        font.getData().setScale(0.25f,0.25f);
        amountDisplay = ActorFactory.createTextLabel(Assets2.getInstance().font, amount);
        addActor(amountDisplay);
    }

    private void layout() {
        float centerY = height / 2f;

        // ---- ADD BUTTON - centered at the tip/start (left side)
        float addButtonSize = height*0.8f;
        addButton.setSize(addButtonSize, addButtonSize);
        // Center of add button at the left edge + padding
        addButton.setPosition(
            -addButtonSize/2,  // left edge with padding
            centerY - addButtonSize / 2f
        );

        // ---- ITEM ICON - centered at the end (right side)
        float iconSize = height * 0.8f;
        itemIcon.setSize(iconSize, iconSize);
        // Center of item icon at the right edge - padding
        itemIcon.setPosition(
            width - iconSize/1.5f,  // right edge with padding
            centerY - iconSize / 2f
        );

        // ---- AMOUNT DISPLAY - very close to item icon on the left side
        // Position it to the left of the item icon with a small gap
        float amountGap = 5f;  // Small gap between amount display and item icon
        float amountWidth = height*0.7f;  // Estimated width for the amount text
        float amountHeight = height * 0.4f;

        layout.setText(font,amount);
        amountDisplay.setSize(amountWidth, amountHeight);
        amountDisplay.setPosition(
            itemIcon.getX() - amountWidth - amountGap,  // Left of item icon with small gap
            centerY
        );
    }
    public void setBackgroundTexture(Texture texture){
        backgroundTexture=new TextureRegion(texture);
        background.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void setItemTexture(Texture texture) {
        itemTexture = new TextureRegion(texture);
        itemIcon.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        // Re-layout to ensure amount display position updates if needed
        layout();
    }
    public void setItemTexture(TextureRegion texture) {
        itemTexture = new TextureRegion(texture);
        itemIcon.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        // Re-layout to ensure amount display position updates if needed
        layout();
    }

    public void setAddItemTexture(Texture texture){
        addItemTexture=new TextureRegion(texture);
        build();
        layout();
        // You might want to update the button texture here if needed
        // addButton.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void setAmount(Long value) {
        this.amount = String.valueOf(value);
        font.getData().setScale(0.2f);
        layout.setText(font, amount);
        this.amountDisplay.setWidth(layout.width);
        this.amountDisplay.setText(String.valueOf(value));

        // Optional: adjust amount display width based on text length
        // This might require recalculating the position
        layout(); // Re-layout to ensure proper positioning if text width changes
    }

    public String getAmount() {
        return amount;
    }
}
