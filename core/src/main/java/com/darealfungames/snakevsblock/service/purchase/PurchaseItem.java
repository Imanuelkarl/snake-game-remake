package com.darealfungames.snakevsblock.service.purchase;

import com.darealfungames.snakevsblock.enumaretors.StoreID;

public class PurchaseItem {
    private StoreID storeId;
    private String productId;
    private String title;
    private String description;
    private Double amount;
    private String currency;
    private String productJson;
    public PurchaseItem(StoreID storeId, String productId, String title, String description, Double amount, String currency){
        this.storeId=storeId;
        this.productId=productId;
        this.title=title;
        this.description=description;
        this.amount=amount;
        this.currency=currency;
    }
    public StoreID getStoreId() {
        return storeId;
    }

    public void setStoreId(StoreID storeId) {
        this.storeId = storeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getProductJson() {
        return productJson;
    }

    public void setProductJson(String productJson) {
        this.productJson = productJson;
    }


}
