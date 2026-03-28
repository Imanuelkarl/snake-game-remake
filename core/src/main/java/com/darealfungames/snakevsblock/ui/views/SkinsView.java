package com.darealfungames.snakevsblock.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.ui.adapters.SkinAdapter;
import com.darealfungames.snakevsblock.ui.cards.SkinCard;
import com.darealfungames.snakevsblock.ui.core.ListManager;
import com.darealfungames.snakevsblock.ui.data.SkinData;
import com.darealfungames.snakevsblock.utils.ActorFactory;

public class SkinsView extends BaseActorView {
    private ListManager<SkinData, SkinCard> listManager;
    private SkinAdapter adapter;
    private Label titleLabel;
    private float width;
    private float height;

    public SkinsView(Group group) {
        super(group);
        setSize(group.getWidth(),group.getHeight());
        //setPosition(group.getX(),group.getY());
        this.width=group.getWidth();
        this.height=group.getHeight();

        setupUI();
        setupList();
        loadData();
        setupTransform();
    }

    private void setupUI() {
        // Title
        titleLabel = ActorFactory.createTextLabel(24,20, Gdx.graphics.getHeight() - 50);
        titleLabel.setPosition(20, Gdx.graphics.getHeight() - 50);
        addActor(titleLabel);
    }

    private void setupList() {
        adapter = new SkinAdapter();
        listManager = new ListManager<>(adapter);


        // Configure grid layout: 2 columns, each card 300x120
        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout(ListManager.ListLayout.GRID, 1, 100);

        addActor(listManager);
    }
    private void setupTransform(){

        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout(ListManager.ListLayout.GRID, 1, 100);
    }

    private void loadData() {
        // Load skins from game data
        java.util.List<SkinData> skins = new java.util.ArrayList<>();
        skins.add(new SkinData("skin_1", "Default Warrior",
            Assets.getInstance().defaultItemTexture, 0, true, true));
        skins.add(new SkinData("skin_2", "Knight Armor",
            Assets.getInstance().defaultItemTexture, 5000, false, false));
        skins.add(new SkinData("skin_3", "Shadow Assassin",
            Assets.getInstance().defaultItemTexture, 10000, false, false));
        skins.add(new SkinData("skin_4", "Dragon Lord",
            Assets.getInstance().defaultItemTexture, 25000, false, false));

        adapter.setData(skins);

        adapter.setOnItemClickListener((skin, position) -> {
            if (!skin.isOwned) {
                // Show purchase dialog
                purchaseSkin(skin, position);
            } else if (!skin.isEquipped) {
                // Equip skin
                adapter.equipSkin(skin.id);
            }
        });
    }

    private void purchaseSkin(SkinData skin, int position) {
        // Check if player has enough coins
        // Show confirmation dialog
        // On confirm:
        skin.isOwned = true;
        adapter.updateSkinOwnership(skin.id, true);
    }

    @Override
    public void update(float delta) {
        // Update any animations
    }

    @Override
    public void resize(float width, float height) {
        listManager.onResize(width - 40, height - 100);
    }

    @Override
    public boolean backPressed() {
        return false;
    }
}
