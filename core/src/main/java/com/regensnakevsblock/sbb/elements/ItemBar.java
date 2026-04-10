package com.regensnakevsblock.sbb.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.utils.ActorFactory;
import com.regensnakevsblock.sbb.utils.FontActor;
import com.regensnakevsblock.sbb.utils.SimpleBitmapFont;

public class ItemBar extends Group {

    private Image background;
    private Image itemIcon;
    private ImageButton addButton;
    private FontActor amountDisplay;
    private Texture backgroundTexture;

    private Texture itemTexture;
    private Texture addItemTexture;
    private SimpleBitmapFont font;

    private String amount = "0";

    private float padding = 10f;
    private float spacing = 5f;

    private float width;
    private float height;

    public ItemBar(float x,float y, float width, float height) {
        Assets assets = Assets.getInstance();
        this.setPosition(x,y);
        this.setSize(width,height);
        this.width=width;
        this.height=height;
        // Background
        backgroundTexture=assets.sliderBackgroundTexture;
        itemTexture=assets.defaultItemTexture;
        addItemTexture=assets.addButtonTexture;

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
        if(GameInstance.getInstance().getSimpleBitmapFont()==null){
            font = GameInstance.getInstance().getSimpleBitmapFont();
            //GameInstance.getInstance().setSimpleBitmapFont(font);
        }
        else{
            font=GameInstance.getInstance().getSimpleBitmapFont();
        }
        font.setScale(0.25f,0.25f);
        amountDisplay = new FontActor(font, amount);
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


        amountDisplay.setSize(amountWidth, amountHeight);
        amountDisplay.setPosition(
            itemIcon.getX() - amountWidth - amountGap,  // Left of item icon with small gap
            centerY - font.getHeight(amount)/3
        );
    }
    public void setBackgroundTexture(Texture texture){
        backgroundTexture=texture;
        background.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void setItemTexture(Texture texture) {
        itemTexture = texture;
        itemIcon.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
        // Re-layout to ensure amount display position updates if needed
        layout();
    }

    public void setAddItemTexture(Texture texture){
        addItemTexture=texture;
        build();
        layout();
        // You might want to update the button texture here if needed
        // addButton.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void setAmount(int value) {
        this.amount = String.valueOf(value);
        this.amountDisplay.setWidth(font.getWidth(String.valueOf(value)));
        this.amountDisplay.setText(String.valueOf(value));

        // Optional: adjust amount display width based on text length
        // This might require recalculating the position
        layout(); // Re-layout to ensure proper positioning if text width changes
    }

    public String getAmount() {
        return amount;
    }
}
