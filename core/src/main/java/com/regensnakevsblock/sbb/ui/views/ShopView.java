package com.regensnakevsblock.sbb.ui.views;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.regensnakevsblock.sbb.assets.Assets2;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.service.purchase.PurchaseService;
import com.regensnakevsblock.sbb.ui.adapters.ShopAdapter;
import com.regensnakevsblock.sbb.ui.cards.ShopCard;
import com.regensnakevsblock.sbb.ui.core.ListManager;
import com.regensnakevsblock.sbb.ui.data.ShopCategory;
import com.regensnakevsblock.sbb.ui.data.ShopItemData;
import com.regensnakevsblock.sbb.utils.TextureTools;

public class ShopView extends BaseActorView {
    private ListManager<ShopItemData, ShopCard> listManager;
    private ShopAdapter adapter;
    private Label titleLabel;
    private float width;
    private float height;


    public ShopView(Group group) {
        super(group);
        setSize(group.getWidth(),group.getHeight());
        //setPosition(group.getX(),group.getY());
        this.width=group.getWidth();
        this.height=group.getHeight();

        setupUI();
        setupList();
        setupTransform();
        loadData();
    }

    private void setupUI() {

    }

    private void setupList() {
        adapter = new ShopAdapter();
        listManager = new ListManager<>(adapter);

        addActor(listManager);

        adapter.setOnItemClickListener((item, position) -> {
            if (!item.isPurchased) {
                purchaseItem(item, position);
            }
        });
    }

    private void loadData() {
        TextureRegion coins = Assets2.getInstance().coinPilesTexture;
        TextureRegion diamonds = Assets2.getInstance().diamondPilesTexture;
        TextureRegion portions = Assets2.getInstance().portionPilesTexture;

        TextureRegion coinPile1 = TextureTools.resolveRegion(coins,2,2,0,0,0);
        TextureRegion coinPile2 = TextureTools.resolveRegion(coins,2,2,0,1,0);
        TextureRegion coinPile3 = TextureTools.resolveRegion(coins,2,2,1,0,0);
        TextureRegion diamondPile1 = TextureTools.resolveRegion(diamonds, 2, 2, 0, 0, 0);
        TextureRegion diamondPile2 = TextureTools.resolveRegion(diamonds,2,2,0,1,0);
        TextureRegion diamondPile3 = TextureTools.resolveRegion(diamonds,2,2,1,0,0);
        TextureRegion portionPile1 = TextureTools.resolveRegion(portions,2,2,0,0,0);
        TextureRegion portionPile2 = TextureTools.resolveRegion(portions,2,2,0,1,0);
        TextureRegion portionPile3 = TextureTools.resolveRegion(portions,2,2,1,0,0);
        java.util.List<ShopItemData> items = new java.util.ArrayList<>();
        items.add(new ShopItemData("item_1", "Health Potion", "Restores 50 HP",
            "N",coinPile1, 100d, false,1000, ShopCategory.COIN));
        items.add(new ShopItemData("item_2", "Mana Potion", "Restores 30 MP",
            "N",coinPile2, 80d, false,5500,ShopCategory.COIN));
        items.add(new ShopItemData("item_3", "Starter Pack", "2000 Coins + 50 Diamonds",
            "N",coinPile3, 10d, false,15000,ShopCategory.COIN));
        items.add(new ShopItemData("item_4", "Double Damage", "Permanent 2x damage boost",
            "N", diamondPile1,50d, false,50,ShopCategory.DIAMOND));
        items.add(new ShopItemData("item_5", "Speed Boost", "Permanent speed increase",
            "N",diamondPile2, 5000d, false,275, ShopCategory.DIAMOND));

        //adapter.setData(items);
        adapter.setData(GameInstance.getInstance().shopItems!=null?GameInstance.getInstance().shopItems:items);
    }
    private void setupTransform(){

        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout( 3, 350);
    }

    private void purchaseItem(ShopItemData item, int position) {
        boolean hasCurrency = false;


    }

    private int getPlayerCoins() {
        return 5000;
    }

    private int getPlayerDiamonds() {
        return 100;
    }

    private void deductCoins(int amount) {
        // Deduct from game state
    }

    private void deductDiamonds(int amount) {
        // Deduct from game state
    }

    private void updateCurrencyDisplay() {

    }

    private void showPurchaseSuccess(String itemName) {
        // Show toast or dialog
        System.out.println("Purchased: " + itemName);
    }

    private void showInsufficientCurrency() {
        // Show error dialog
        System.out.println("Insufficient currency!");
    }

    @Override
    public void update(float delta) {
        // Update any shop animations
    }

    @Override
    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
        setupTransform();
        listManager.onResize(width,height);
        listManager.renderAllItems();

    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public void buildIfNeeded() {

    }
}
