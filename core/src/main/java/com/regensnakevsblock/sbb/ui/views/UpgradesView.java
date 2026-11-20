package com.regensnakevsblock.sbb.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.elements.GameDialog;
import com.regensnakevsblock.sbb.ui.adapters.UpgradeAdapter;
import com.regensnakevsblock.sbb.ui.cards.UpgradeCard;
import com.regensnakevsblock.sbb.ui.core.ListManager;
import com.regensnakevsblock.sbb.ui.data.UpgradeData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class UpgradesView extends BaseActorView<UpgradeData> {
    private ListManager<UpgradeData, UpgradeCard> listManager;
    private UpgradeAdapter adapter;
    private Label titleLabel;
    private Label coinsLabel;
    private float width;
    private float height;
    private Label noCoinsPop;
    private GameDialog dialog;

    public UpgradesView(Group group) {
        super(group);
        setSize(group.getWidth(),group.getHeight());
        setPosition(group.getX(),group.getY());
        this.width=group.getWidth();
        this.height=group.getHeight();
        setupUI();
        setupList();
        setupTransform();

        Gdx.app.postRunnable(this::loadData);
    }

    @Override
    public void updateData(UpgradeData data, int position) {
        adapter.updateData(data,position);
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
    private void setupDialog(Stage stage){
        dialog = new GameDialog(stage);
        dialog.setDialogSize(width,height/8);
        dialog.setTitleEnabled(false);
        dialog.addContentActor(noCoinsPop);

        dialog.setBackground(Assets.getInstance().dialogQuarterSizeOpen);
        dialog.open();
    }


    private void hideLoading() {
        if (loadingGroup != null) {
            loadingGroup.remove();
        }
    }

    private void setupUI() {


        // Player coins display

        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor= Color.FIREBRICK;
        noCoinsPop = ActorFactory.createTextLabel("24",style,20, Gdx.graphics.getHeight() - 50);
        noCoinsPop.setFontScale(0.6f);
        noCoinsPop.setWrap(true);
        noCoinsPop.setText("Insufficient Coins");
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

        adapter.setOnUpgradeListener(this::upgradeItem);
    }

    void upgradeItem(UpgradeData upgrade,int position){
            if(upgrade.upgradeItem.equals("coins")) {
                if ((upgrade.upgradeCost <= GameInstance.getSaveService().getCoins() )) {

                    upgrade(upgrade);
                } else {
                    adapter.notEnoughCoins = true;
                    noCoinsPop.setText("Insufficient Coins");
                }
            }else{
                if((upgrade.upgradeCost<= GameInstance.getSaveService().getDiamonds())){
                upgrade(upgrade);
            }else{
                adapter.notEnoughCoins=true;
                noCoinsPop.setText("Insufficient Diamonds");
            }}
    }
    void upgrade(UpgradeData upgrade){
            // Charge the price of the CURRENT level, in the currency shown on the card
            int cost = upgrade.upgradeCost;
            if (upgrade.upgradeItem.equals("coins")) {
                GameInstance.getSaveService().saveCoins(GameInstance.getSaveService().getCoins() - cost);
            } else {
                GameInstance.getSaveService().saveDiamonds(GameInstance.getSaveService().getDiamonds() - cost);
            }

            // Then move to the next level and compute its price/currency
            upgrade.currentLevel++;
            upgrade.currentValue = Constants.calculatePowerUpTime(upgrade.currentLevel);
            upgrade.upgradeCost = (int)Constants.calculatePowerUpPrice(upgrade.currentLevel);
            upgrade.upgradeItem = Constants.getUpgradeItem(upgrade.currentLevel);
            GameInstance.getSaveService().saveUpgrade(upgrade.name,upgrade.currentLevel);
    }

    private void loadData() {
        adapter.setData(GameInstance.getInstance().getUpgradeDataList());
    }

    private void updateCoinsDisplay() {
        // Update coins label with current player coins
        //coinsLabel.setText("Coins: " + getPlayerCoins());
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
        noCoinsPop.setPosition(60,60);
        noCoinsPop.setWidth(width*0.75f);
        listManager.renderAllItems();
        if(adapter.notEnoughCoins){
            setupDialog(this.getRoot().getStage());
            adapter.notEnoughCoins=false;
        }
    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public void buildIfNeeded() {

    }
}
