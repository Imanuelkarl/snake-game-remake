package com.darealfungames.snakevsblock.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.ui.adapters.ShopAdapter;
import com.darealfungames.snakevsblock.ui.cards.ShopCard;
import com.darealfungames.snakevsblock.ui.core.ListManager;
import com.darealfungames.snakevsblock.ui.data.ShopItemData;
import com.darealfungames.snakevsblock.utils.ActorFactory;

public class ShopView extends BaseActorView {
    private ListManager<ShopItemData, ShopCard> listManager;
    private ShopAdapter adapter;
    private Label titleLabel;
    private float width;
    private float height;
    private Label coinsLabel;
    private Label diamondsLabel;
    private Table currencyPanel;

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
        // Title
        titleLabel = ActorFactory.createTextLabel("SHOP");
        titleLabel.setPosition(20, Gdx.graphics.getHeight() - 50);
        addActor(titleLabel);

        // Currency panel
        currencyPanel = new Table();
        currencyPanel.setPosition(Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 50);
        currencyPanel.setSize(180, 40);

        coinsLabel = ActorFactory.createTextLabel("🪙 5000");
        diamondsLabel = ActorFactory.createTextLabel("💎 100");

        currencyPanel.add(coinsLabel).padRight(10);
        currencyPanel.add(diamondsLabel);
        addActor(currencyPanel);
    }

    private void setupList() {
        adapter = new ShopAdapter();
        listManager = new ListManager<>(adapter);


        // Grid layout: 2 columns for shop items
        listManager.setLayout( 2, 280, 100);
        listManager.setSize(Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() - 100);
        listManager.setPosition(20, 20);

        addActor(listManager);

        adapter.setOnItemClickListener((item, position) -> {
            if (!item.isPurchased) {
                purchaseItem(item, position);
            }
        });
    }

    private void loadData() {
        java.util.List<ShopItemData> items = new java.util.ArrayList<>();
        items.add(new ShopItemData("item_1", "Health Potion", "Restores 50 HP",
            ShopItemData.CurrencyType.COINS, 100, Assets.getInstance().defaultItemTexture, false));
        items.add(new ShopItemData("item_2", "Mana Potion", "Restores 30 MP",
            ShopItemData.CurrencyType.COINS, 80, Assets.getInstance().defaultItemTexture, false));
        items.add(new ShopItemData("item_3", "Starter Pack", "2000 Coins + 50 Diamonds",
            ShopItemData.CurrencyType.DIAMONDS, 10, Assets.getInstance().defaultItemTexture, false));
        items.add(new ShopItemData("item_4", "Double Damage", "Permanent 2x damage boost",
            ShopItemData.CurrencyType.DIAMONDS, 50,Assets.getInstance().defaultItemTexture, false));
        items.add(new ShopItemData("item_5", "Speed Boost", "Permanent speed increase",
            ShopItemData.CurrencyType.COINS, 5000, Assets.getInstance().defaultItemTexture, false));

        adapter.setData(items);
    }
    private void setupTransform(){

        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout(3, 100);
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
        coinsLabel.setText("🪙 " + getPlayerCoins());
        diamondsLabel.setText("💎 " + getPlayerDiamonds());
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
        listManager.renderAllItems();
        currencyPanel.setPosition(width - 200, height - 50);
    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public void buildIfNeeded() {

    }
}
