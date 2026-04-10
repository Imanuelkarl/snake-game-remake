package com.darealfungames.snakevsblock.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.darealfungames.snakevsblock.config.GameInstance;
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
        setSize(group.getWidth(), group.getHeight());

        this.width = group.getWidth();
        this.height = group.getHeight();

        // ONLY lightweight setup
        setupUI();
        buildIfNeeded();
    }
    private Group loadingGroup;

    private void showLoading() {
        loadingGroup = new Group();

        Label loadingText = ActorFactory.createTextLabel(50, 20, 0);
        loadingText.setText("Loading skins...");
        loadingText.setPosition(50, height / 2f, Align.center);

        loadingGroup.addActor(loadingText);
        addActor(loadingGroup);
    }

    private void hideLoading() {
        if (loadingGroup != null) {
            loadingGroup.remove();
        }
    }
    private boolean isBuilt = false;

    @Override
    public void buildIfNeeded() {
        if (isBuilt) return;
        isBuilt = true;

        setupList();
        setupTransform();

        // IMPORTANT: defer data load
        showLoading();

        Gdx.app.postRunnable(() -> {
            loadData();
            hideLoading();
        });
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

        addActor(listManager);
    }
    private void setupTransform(){

        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout( 3, 350);
    }

    private void loadData() {
        adapter.setData(GameInstance.getInstance().getSkins());
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
        this.width = width;
        this.height = height;
        setupTransform();
        listManager.renderAllItems();
    }

    @Override
    public boolean backPressed() {
        return false;
    }
}
