package com.regensnakevsblock.sbb.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.Gson;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.levels.Level;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.purchase.PurchaseService;
import com.regensnakevsblock.sbb.ui.adapters.SkinAdapter;
import com.regensnakevsblock.sbb.ui.core.ViewManager;
import com.regensnakevsblock.sbb.ui.core.ViewPagerAdapter;
import com.regensnakevsblock.sbb.ui.data.LevelData;
import com.regensnakevsblock.sbb.ui.data.SkinData;
import com.regensnakevsblock.sbb.ui.views.AchievementsView;
import com.regensnakevsblock.sbb.ui.views.LevelsView;
import com.regensnakevsblock.sbb.ui.views.ShopView;
import com.regensnakevsblock.sbb.ui.views.SkinsView;
import com.regensnakevsblock.sbb.ui.views.UpgradesView;
import com.regensnakevsblock.sbb.utils.SimpleBitmapFont;
import com.regensnakevsblock.sbb.utils.TextureTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameInstance {

    private static GameInstance instance;
    private static SaveService saveService;
    public LevelsView levelsView;

    public List<LevelData> getLevelData() {
        List<LevelData> levelDataList = new ArrayList<>();
        int currentLevel = saveService.getSelectedLevel();
        int maxLevel = saveService.getMaxLevel();
        for(int i=0;i<levels.size();i++){
            Level level = levels.get(i);
            LevelData data = new LevelData(level.getLevelId(),level.isCleared(),currentLevel==level.getLevelId(),level.getLevelId()<=maxLevel);
            levelDataList.add(data);
        }
        return levelDataList;
    }

    // ===== LOAD STATES =====
    public enum LoadState {
        NOT_LOADED,
        LOADING,
        LOADED
    }
    //public SkinAdapter adapter;
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
            saveService = new SaveService();
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

        for (int i = 0; i < (columns*rows); i++) {
            int col = i % columns;
            int row = i / columns;

            TextureRegion region = TextureTools.resolveRegion(texture,rows,columns,col,row,8);

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

    private final List<Level> levels = new ArrayList<>();

    public void load() {
        // from assets (android-safe)
        String json = Gdx.files.internal("levels.json").readString();

        // Gson example
        Gson gson = new Gson();
        Level[] loaded = gson.fromJson(json, Level[].class);

        levels.clear();
        Collections.addAll(levels, loaded);

        // Optional: sort or index by id
        levels.sort(Comparator.comparingInt(Level::getLevelId));
        this.levelsView = new LevelsView();
    }

    public Level getByIndex(int index) {
        return levels.get(index);
    }

    public Level getById(int id) {
        // if called often, replace with a Map<Integer, Level>
        for (Level l : levels) {
            if (l.getLevelId() == id) return l;
        }
        return null;
    }

    public int size() {
        return levels.size();
    }
}
