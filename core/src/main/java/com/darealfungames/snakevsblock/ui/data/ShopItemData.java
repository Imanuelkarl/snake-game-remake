package com.darealfungames.snakevsblock.ui.data;


import com.badlogic.gdx.graphics.Texture;

public class ShopItemData {
    public enum CurrencyType {
        COINS, DIAMONDS
    }

    public String id;
    public String name;
    public String description;
    public CurrencyType currencyType;
    public int price;
    public Texture icon;
    public boolean isPurchased;

    public ShopItemData(String id, String name, String description, CurrencyType currencyType,
                        int price, Texture icon, boolean isPurchased) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currencyType = currencyType;
        this.price = price;
        this.icon = icon;
        this.isPurchased = isPurchased;
    }
}
