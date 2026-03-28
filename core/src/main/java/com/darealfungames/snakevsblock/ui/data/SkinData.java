package com.darealfungames.snakevsblock.ui.data;

import com.badlogic.gdx.graphics.Texture;

public class SkinData {
    public String id;
    public String name;
    public Texture thumbnail;
    public int price;
    public boolean isOwned;
    public boolean isEquipped;

    public SkinData(String id, String name, Texture thumbnail, int price, boolean isOwned, boolean isEquipped) {
        this.id = id;
        this.name = name;
        this.thumbnail = thumbnail;
        this.price = price;
        this.isOwned = isOwned;
        this.isEquipped = isEquipped;
    }
}
