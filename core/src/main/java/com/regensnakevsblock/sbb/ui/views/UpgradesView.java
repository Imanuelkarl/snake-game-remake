package com.regensnakevsblock.sbb.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.regensnakevsblock.sbb.ui.adapters.UpgradeAdapter;
import com.regensnakevsblock.sbb.ui.cards.UpgradeCard;
import com.regensnakevsblock.sbb.ui.core.ListManager;
import com.regensnakevsblock.sbb.ui.data.UpgradeData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class UpgradesView extends BaseActorView {
    private ListManager<UpgradeData, UpgradeCard> listManager;
    private UpgradeAdapter adapter;
    private Label titleLabel;
    private Label coinsLabel;
    private float width;
    private float height;

    public UpgradesView(Group group) {
        super(group);
        setSize(group.getWidth(),group.getHeight());
        //setPosition(group.getX(),group.getY());
        this.width=group.getWidth();
        this.height=group.getHeight();
        setupUI();
        setupList();
        setupTransform();

        Gdx.app.postRunnable(this::loadData);
    }
    private Group loadingGroup;

    private void showLoading() {
        loadingGroup = new Group();

        Label loadingText = ActorFactory.createTextLabel(20, 0, 0);
        loadingText.setText("Loading skins...");
        loadingText.setPosition(width / 2f, height / 2f, Align.center);

        loadingGroup.addActor(loadingText);
        addActor(loadingGroup);
    }

    private void hideLoading() {
        if (loadingGroup != null) {
            loadingGroup.remove();
        }
    }

    private void setupUI() {
        // Title
        titleLabel = ActorFactory.createTextLabel(24,20, Gdx.graphics.getHeight() - 50);
        titleLabel.setText("UPGRADES");
        titleLabel.setPosition(20, Gdx.graphics.getHeight() - 50);
        addActor(titleLabel);

        // Player coins display
        coinsLabel = ActorFactory.createTextLabel(24,Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 50);
        coinsLabel.setText("COINS 5000");
        coinsLabel.setPosition(Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 50);
        addActor(coinsLabel);
    }

    private void setupTransform(){

        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout( 1, 300);
    }
    private void setupList() {
        adapter = new UpgradeAdapter();
        listManager = new ListManager<>(adapter);


        // Vertical layout: 1 column, each card 300x100
        listManager.setLayout( 1, 300, 100);
        listManager.setSize(Gdx.graphics.getWidth() - 40, Gdx.graphics.getHeight() - 100);
        listManager.setPosition(20, 20);

        addActor(listManager);

        adapter.setOnUpgradeListener((upgrade, position) -> {
            // Check if player has enough coins
            // Apply upgrade
            upgrade.currentLevel++;
            upgrade.currentValue = upgrade.nextValue;
            upgrade.nextValue *= 1.2f; // 20% increase per level
            upgrade.upgradeCost = (int)(upgrade.upgradeCost * 1.5f);

            adapter.updateUpgrade(position, upgrade);
            updateCoinsDisplay();
        });
    }

    private void loadData() {
        java.util.List<UpgradeData> upgrades = new java.util.ArrayList<>();
        upgrades.add(new UpgradeData("upg_1", "Sword Damage", "Increases sword damage",
            1, 10, 500, 100, 120));
        upgrades.add(new UpgradeData("upg_2", "Health Regeneration", "Regenerate health faster",
            0, 5, 300, 0, 5));
        upgrades.add(new UpgradeData("upg_3", "Critical Chance", "Increase critical hit chance",
            2, 10, 800, 15, 18));
        upgrades.add(new UpgradeData("upg_4", "Movement Speed", "Faster movement",
            1, 8, 400, 5, 6));

        adapter.setData(upgrades);
    }

    private void updateCoinsDisplay() {
        // Update coins label with current player coins
        coinsLabel.setText("Coins: " + getPlayerCoins());
    }

    private int getPlayerCoins() {
        // Get from game state
        return 5000;
    }

    @Override
    public void update(float delta) {
        // Update animations or effects
    }

    @Override
    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
        setupTransform();
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
