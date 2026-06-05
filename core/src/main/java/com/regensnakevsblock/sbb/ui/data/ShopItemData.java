package com.regensnakevsblock.sbb.ui.data;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ShopItemData {
    public enum CurrencyType {
        COINS, DIAMONDS, PORTIONS
    }

    public String id;
    public String name;
    public String description;
    public String currencySign;
    public Double price;
    public TextureRegion icon;
    public TextureRegion itemIcon;
    public boolean isPurchased;
    public double originalPrice;
    public ShopCategory category;
    public boolean discounted;
    public long itemQuantity;
    public int discountQuantity;



    public ShopItemData(String id, String name, String description, String currencyType,Texture itemIcon,
                        Double price,  boolean isPurchased,long itemQuantity,ShopCategory category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currencySign = currencyType;
        this.price = price;
        this.isPurchased = isPurchased;
        this.itemQuantity=itemQuantity;
        this.category=category;
        this.itemIcon = new TextureRegion(itemIcon);
    }
    public ShopItemData(String id, String name, String description, String currencyType,TextureRegion itemIcon,
                        Double price, boolean isPurchased,long itemQuantity,ShopCategory category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currencySign = currencyType;
        this.price = price;
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
