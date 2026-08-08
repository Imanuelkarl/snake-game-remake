package com.regensnakevsblock.sbb.ui.data;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SkinData {
    public String id;
    public String name;
    public TextureRegion thumbnail;
    public int price;
    public boolean isOwned;
    public boolean isEquipped;
    public String currency;

    public SkinData(String id, String name, TextureRegion thumbnail, int price, boolean isOwned, boolean isEquipped,String currency) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.price = price;
        this.isOwned = isOwned;
        this.isEquipped = isEquipped;
        this.currency=currency;
    }
}
