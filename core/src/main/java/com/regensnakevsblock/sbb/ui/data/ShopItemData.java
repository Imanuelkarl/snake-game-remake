package com.regensnakevsblock.sbb.ui.data;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
    public TextureRegion itemIcon;
    public boolean isPurchased;
    public double originalPrice;
    public ShopCategory category;
    public boolean discounted;
    public long itemQuantity;
    public int discountQuantity;


    public ShopItemData(String id, String name, String description, CurrencyType currencyType,TextureRegion itemIcon,
                        int price, Texture icon, boolean isPurchased,long itemQuantity,ShopCategory category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currencyType = currencyType;
        this.price = price;
        this.icon = icon;
        this.isPurchased = isPurchased;
        this.itemQuantity=itemQuantity;
        this.category=category;
        this.itemIcon = itemIcon;
    }
    public void setDiscountQuantity(int discountQuantity) {
        this.discountQuantity = discountQuantity;
    }

     public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }
    public void setDiscounted(boolean discounted) {
        this.discounted = discounted;
    }
    public boolean isDiscounted() {
        return discounted;
    }
}
