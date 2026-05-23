package com.regensnakevsblock.sbb.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.ui.adapters.ShopAdapter;
import com.regensnakevsblock.sbb.ui.cards.ShopCard;
import com.regensnakevsblock.sbb.ui.core.ListManager;
import com.regensnakevsblock.sbb.ui.data.ShopCategory;
import com.regensnakevsblock.sbb.ui.data.ShopItemData;
import com.regensnakevsblock.sbb.utils.ActorFactory;
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
        Texture coins = Assets.getInstance().coinPilesTexture;
        Texture diamonds = Assets.getInstance().diamondPilesTexture;
        Texture portions = Assets.getInstance().portionPilesTexture;

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
            ShopItemData.CurrencyType.COINS,coinPile1, 100, Assets.getInstance().defaultItemTexture, false,1000, ShopCategory.COIN));
        items.add(new ShopItemData("item_2", "Mana Potion", "Restores 30 MP",
            ShopItemData.CurrencyType.COINS,coinPile2, 80, Assets.getInstance().defaultItemTexture, false,5500,ShopCategory.COIN));
        items.add(new ShopItemData("item_3", "Starter Pack", "2000 Coins + 50 Diamonds",
            ShopItemData.CurrencyType.DIAMONDS,coinPile3, 10, Assets.getInstance().defaultItemTexture, false,15000,ShopCategory.COIN));
        items.add(new ShopItemData("item_4", "Double Damage", "Permanent 2x damage boost",
            ShopItemData.CurrencyType.DIAMONDS, diamondPile1,50,Assets.getInstance().defaultItemTexture, false,50,ShopCategory.DIAMOND));
        items.add(new ShopItemData("item_5", "Speed Boost", "Permanent speed increase",
            ShopItemData.CurrencyType.COINS,diamondPile2, 5000, Assets.getInstance().defaultItemTexture, false,275, ShopCategory.DIAMOND));

        adapter.setData(items);
    }
    private void setupTransform(){

        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout( 3, 350);
    }

    private void purchaseItem(ShopItemData item, int position) {
        boolean hasCurrency = false;

        if (item.currencyType == ShopItemData.CurrencyType.COINS) {
            hasCurrency = getPlayerCoins() >= item.price;
        } else {
            hasCurrency = getPlayerDiamonds() >= item.price;
        }

        if (hasCurrency) {
            // Deduct currency and add item to inventory
            if (item.currencyType == ShopItemData.CurrencyType.COINS) {
                deductCoins(item.price);
            } else {
                deductDiamonds(item.price);
            }

            item.isPurchased = true;
            adapter.notifyDataSetChanged();
            updateCurrencyDisplay();

            // Show purchase success message
            showPurchaseSuccess(item.name);
        } else {
            // Show insufficient currency message
            showInsufficientCurrency();
        }
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
