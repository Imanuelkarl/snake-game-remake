package com.regensnakevsblock.sbb.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.gson.Gson;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.assets.Assets2;
import com.regensnakevsblock.sbb.levels.Level;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.purchase.PurchaseItem;
import com.regensnakevsblock.sbb.service.purchase.PurchaseService;
import com.regensnakevsblock.sbb.ui.core.ViewManager;
import com.regensnakevsblock.sbb.ui.core.ViewPagerAdapter;
import com.regensnakevsblock.sbb.ui.data.LevelData;
import com.regensnakevsblock.sbb.ui.data.ShopCategory;
import com.regensnakevsblock.sbb.ui.data.ShopItemData;
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
import java.util.Locale;

public class GameInstance {

    private static GameInstance instance;
    private static SaveService saveService;
    public LevelsView levelsView;
    private MyGame game;

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
    public List<ShopItemData> shopItems;

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

    public static SaveService getSaveService() {
        return saveService;
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
    private ShopCategory dataToShopCategory(String data) {
        switch (data.toUpperCase(Locale.ROOT)){
            case "COINS":
                return ShopCategory.COIN;
            case "DIAMONDS":
                return ShopCategory.DIAMOND;
            default:
                return ShopCategory.PORTION;
        }
    }
    private TextureRegion quantityToTexture(String item, Long quantity){
        TextureRegion coins = Assets2.getInstance().coinPilesTexture;
        TextureRegion diamonds = Assets2.getInstance().diamondPilesTexture;
        TextureRegion portions = Assets2.getInstance().portionPilesTexture;

        TextureRegion coinPile1 = TextureTools.resolveRegion(coins,2,2,0,0,0);
        TextureRegion coinPile2 = TextureTools.resolveRegion(coins,2,2,0,1,0);
        TextureRegion coinPile3 = TextureTools.resolveRegion(coins,2,2,1,0,0);
        TextureRegion coinPile4 = TextureTools.resolveRegion(coins,2,2,1,1,0);
        TextureRegion diamondPile1 = TextureTools.resolveRegion(diamonds, 2, 2, 0, 0, 0);
        TextureRegion diamondPile2 = TextureTools.resolveRegion(diamonds,2,2,0,1,0);
        TextureRegion diamondPile3 = TextureTools.resolveRegion(diamonds,2,2,1,0,0);
        TextureRegion diamondPile4 = TextureTools.resolveRegion(diamonds,2,2,1,1,0);
        TextureRegion portionPile1 = TextureTools.resolveRegion(portions,2,2,0,0,0);
        TextureRegion portionPile2 = TextureTools.resolveRegion(portions,2,2,0,1,0);
        TextureRegion portionPile3 = TextureTools.resolveRegion(portions,2,2,1,0,0);
        TextureRegion portionPile4 = TextureTools.resolveRegion(portions,2,2,1,1,0);
        switch (item.toUpperCase(Locale.ROOT)){
            case "COINS":
                if(quantity>=10000){
                    return coinPile4;
                }else if(quantity>=5000){
                    return coinPile3;
                }else if(quantity>=2000){
                    return coinPile2;
                }else{
                    return coinPile1;
                }
            case "POTIONS":
                if(quantity>=1500){
                    return portionPile4;
                }else if(quantity>=600){
                    return portionPile3;
                }else if(quantity>=250){
                    return portionPile2;
                }else{
                    return portionPile1;
                }
            case "DIAMONDS":
                if(quantity>=5000){
                    return diamondPile4;
                }else if(quantity>=2000){
                    return diamondPile3;
                }else if(quantity>=1000){
                    return diamondPile2;
                }else{
                    return diamondPile1;
                }
            default:
                return portionPile1;
        }
    }
    private List<ShopItemData> normalizePurchaseItems(List<PurchaseItem> items){
        List<ShopItemData> shopItems = new ArrayList<>();
        for (int i = 0; i <items.size(); i++) {
            PurchaseItem item = items.get(i);
            System.out.println("Product price is "+item.getAmount());
            String gift = item.getProductId().split("_")[1];
            long quantity = Long.parseLong(item.getProductId().split("_")[0]);
            TextureRegion itemTexture =quantityToTexture(gift,quantity);
            ShopItemData shopItemData = new ShopItemData(item.getProductId(),item.getTitle().replace('_',' '),item.getDescription(),item.getCurrency(),
                itemTexture,item.getAmount()/1000000,false,quantity,dataToShopCategory(gift));
            shopItems.add(shopItemData);
        }
        return shopItems;
    }

    public void loadSkinData(MyGame game) {
        this.game = game;
        //game.getPurchaseService().initializeBillingService();
        if(game.getPurchaseService()!=null){
            List<PurchaseItem> items = game.getPurchaseService().getPurchaseItems();
            shopItems = normalizePurchaseItems(items);
            System.out.println("Total items found are "+shopItems.size());
        }


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
                skinDataState = LoadState.LOADED;
                if (onSkinDataLoaded != null) {
                    onSkinDataLoaded.run();
                }
            });
        }).start();
    }
    public void setPurchaseService(PurchaseService purchaseService) {
        if (instance == null) {
            instance = new GameInstance();
            saveService = new SaveService();
            instance.purchaseService = purchaseService;
        }
        else{
            instance.purchaseService = purchaseService;
        }
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
    public void setupViewPager() {
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
        Texture texture = Assets2.getInstance().snakeSkins;
        int columns = 6;
        int rows = 8;

        int tileWidth = texture.getWidth() / columns;
        int tileHeight = texture.getHeight() / rows;
        TextureRegion[][] regions = TextureRegion.split(texture, tileWidth, tileHeight);

        for (int i = 0; i < (columns*rows); i++) {
            int col = i % columns;
            int row = i / columns;

            TextureRegion region = TextureTools.resolveRegion(texture,rows,columns,col,row,12);

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
