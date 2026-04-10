package com.regensnakevsblock.sbb.elements;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

import java.awt.Label;

public class ConsumableStat extends Group {

    private Label quantity;
    private ImageButton add;
    private Image icon;
    private Image bar;

    private Texture barTexture, iconTexture, addTexture;

    public ConsumableStat(Texture barTexture, Texture iconTexture, Texture addTexture) {
        this.barTexture = barTexture;
        this.iconTexture = iconTexture;
        this.addTexture = addTexture;
        initialize();
    }
    public void initialize(){

    }
    public void setQuantity(int quantity) {
        this.quantity.setText(String.valueOf(quantity));
    }

}
