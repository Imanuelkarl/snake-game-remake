package com.darealfungames.snakevsblock.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.service.AdsService;
import com.darealfungames.snakevsblock.service.PurchaseService;
import com.darealfungames.snakevsblock.ui.adapters.SkinAdapter;
import com.darealfungames.snakevsblock.ui.core.ViewManager;
import com.darealfungames.snakevsblock.ui.core.ViewPagerAdapter;
import com.darealfungames.snakevsblock.ui.data.SkinData;
import com.darealfungames.snakevsblock.ui.views.AchievementsView;
import com.darealfungames.snakevsblock.ui.views.ShopView;
import com.darealfungames.snakevsblock.ui.views.SkinsView;
import com.darealfungames.snakevsblock.ui.views.UpgradesView;
import com.darealfungames.snakevsblock.utils.SimpleBitmapFont;

import java.util.ArrayList;
import java.util.List;

public class GameInstance {

    private static GameInstance instance;

    // ===== LOAD STATES =====
    public enum LoadState {
        NOT_LOADED,
        LOADING,
        LOADED
    }
    public SkinAdapter adapter;
    public AdsService adsService;
    public PurchaseService purchaseService;

    private LoadState skinDataState = LoadState.NOT_LOADED;

    // ===== DATA =====
    private List<SkinData> skins;

    // ===== CALLBACK =====
    private Runnable onSkinDataLoaded;

    // ===== ASSETS =====
    private SimpleBitmapFont simpleBitmapFont;
    public ViewManager menuViewManager;
    public ViewPagerAdapter menuViewManagerAdapter;

    // ===== SINGLETON =====
    private GameInstance() {}

    public static GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }


    // =====================================================
    // FONT (LAZY LOADED)
    // =====================================================
    public SimpleBitmapFont getSimpleBitmapFont() {

        return simpleBitmapFont;
    }

    public void setSimpleBitmapFont(SimpleBitmapFont font) {
        this.simpleBitmapFont = font;
    }

    // =====================================================
    // SKIN DATA LOADING (ASYNC + CACHED)
    // =====================================================
    public void loadSkinData() {
        if (skinDataState == LoadState.LOADING || skinDataState == LoadState.LOADED) {
            return;
        }

        skinDataState = LoadState.LOADING;
        if (simpleBitmapFont == null) {
            simpleBitmapFont = new SimpleBitmapFont(
                Gdx.files.internal("fonts/ImageFonts/FontsHDTwo.fnt"),
                Gdx.files.internal("fonts/ImageFonts/FontsHDTwo.png")
            );
            simpleBitmapFont.setDebugMode(false);
        }

        new Thread(() -> {
            // Simulate heavy or future network operation
            List<SkinData> loadedData = createSkinData();

            // Switch back to render thread
            Gdx.app.postRunnable(() -> {
                skins = loadedData;
                setupViewPager();
                skinDataState = LoadState.LOADED;
                adapter=loadAdapter();


                if (onSkinDataLoaded != null) {
                    onSkinDataLoaded.run();
                }
            });
        }).start();
    }

    // =====================================================
    // DATA ACCESS
    // =====================================================
    public List<SkinData> getSkins() {
        return skins;
    }

    public boolean isSkinDataLoaded() {
        return skinDataState == LoadState.LOADED;
    }

    public boolean isSkinDataLoading() {
        return skinDataState == LoadState.LOADING;
    }

    public LoadState getSkinDataState() {
        return skinDataState;
    }

    // =====================================================
    // CALLBACK REGISTRATION
    // =====================================================
    public void setOnSkinDataLoaded(Runnable callback) {
        this.onSkinDataLoaded = callback;
    }
    private void setupViewPager() {
        menuViewManager = new ViewManager();
        menuViewManagerAdapter = new ViewPagerAdapter();

        //menuViewManager.setSize(Constants.SCREEN_WIDTH, Constants.SCREEN_SIZE- (float) Constants.SCREEN_SIZE /12 -Constants.SCREEN_SIZE*0.1f);
        //menuViewManager.setPosition(0,Constants.SCREEN_SIZE*0.1f);
        // Add the 4 views
        menuViewManagerAdapter.addView(new SkinsView(menuViewManager));
        menuViewManagerAdapter.addView(new UpgradesView(menuViewManager));
        menuViewManagerAdapter.addView(new AchievementsView(menuViewManager));
        menuViewManagerAdapter.addView(new ShopView(menuViewManager));
        menuViewManager.setAdapter(menuViewManagerAdapter);





    }

    // =====================================================
    // INTERNAL DATA CREATION (REPLACE WITH API LATER)
    // =====================================================
    private List<SkinData> createSkinData() {

        List<SkinData> skins = new ArrayList<>();
        Texture texture = Assets.getInstance().snakeSkins;
        int columns = 6;
        int rows = 8;

        int tileWidth = texture.getWidth() / columns;
        int tileHeight = texture.getHeight() / rows;
        TextureRegion[][] regions = TextureRegion.split(texture, tileWidth, tileHeight);

        for (int i = 0; i < 16; i++) {
            int col = i % columns;
            int row = i / columns;

            TextureRegion region = regions[row][col]; // already top-left aligned

            skins.add(new SkinData(
                "skin_" + i,
                "Skin " + (i + 1),
                region,
                (i + 1) * 1000,
                i == 0,
                i == 0
            ));
        }

        return skins;
    }

    // =====================================================
    // CLEANUP
    // =====================================================
    public void dispose() {
        if (simpleBitmapFont != null) {
            simpleBitmapFont.dispose();
        }
    }
    public SkinAdapter loadAdapter() {
        // Placeholder for future adapter access if needed
        SkinAdapter adapter =new SkinAdapter();
        adapter.setData(getSkins());
        adapter.setOnItemClickListener((skin, position) -> {
            if (!skin.isOwned) {
                // Show purchase dialog
                //purchaseSkin(skin, position);
            } else if (!skin.isEquipped) {
                // Equip skin
                adapter.equipSkin(skin.id);
            }
        });
        return adapter;
    }
    private void loadViewPager(){

    }
}
