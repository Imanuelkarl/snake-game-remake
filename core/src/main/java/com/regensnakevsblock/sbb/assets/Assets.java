package com.regensnakevsblock.sbb.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.regensnakevsblock.sbb.config.GameInstance;

import java.util.HashMap;
import java.util.Map;

public class Assets implements Disposable {

    private static Assets instance;

    private boolean assetsLoaded = false;
    private boolean uiLoaded = false;

    // ---- UI TextureRegions ----
    public TextureRegion menuBGTexture;
    public TextureRegion snakeSkinButtonTexture;
    public TextureRegion shareButtonTexture;
    public TextureRegion addButtonTexture;
    public TextureRegion skipTutorialButton;
    public TextureRegion homeButtonTexture;
    public TextureRegion restartButtonTexture;
    public TextureRegion resumeButtonTexture;
    public TextureRegion tapToContinueTexture;
    public TextureRegion pauseBtnTexture;
    public TextureRegion logoTexture;
    public TextureRegion barTexture;
    public TextureRegion coinTexture;
    public TextureRegion healthTexture;
    public TextureRegion diamondTexture;
    public TextureRegion sliderBackgroundTexture;
    public TextureRegion sliderFillTexture;
    public TextureRegion sliderKnobTexture;
    public TextureRegion boxDialogTexture;
    public TextureRegion pauseHeaderTexture;
    public TextureRegion soundTexture;
    public TextureRegion musicTexture;
    public TextureRegion replayButtonTexture;
    public TextureRegion upgradeButtonTexture;
    public TextureRegion home2ButtonTexture;
    public TextureRegion closeTexture;
    public TextureRegion bottomNavBg;
    public TextureRegion defaultItemTexture;
    public TextureRegion selectOverlayTexture;
    public TextureRegion defaultOverlayTexture;
    public TextureRegion licenseCreditsTexture;
    public TextureRegion privacyPolicyTexture;
    public TextureRegion tosTexture;
    public TextureRegion giveUpTexture;
    public TextureRegion continueTexture;
    public TextureRegion failTexture;
    public TextureRegion upLayout;
    public TextureRegion selectedButtonTexture;
    public TextureRegion equippedBorderTexture;
    public TextureRegion selectButtonTexture;
    public TextureRegion buyButtonTexture;
    public TextureRegion levelsButtonTexture;
    public TextureRegion priceBarTexture;
    public TextureRegion coinPilesTexture;
    public TextureRegion diamondPilesTexture;
    public TextureRegion portionPilesTexture;
    public TextureRegion gameOverHeaderTexture;
    public TextureRegion victoryHeaderTexture;
    public TextureRegion nextLevelTexture;
    public TextureRegion scoreTextBoard;
    public TextureRegion highScoreTextBoard;
    public TextureRegion upgradeProgressBG;
    public TextureRegion upgradeProgressFill;
    public TextureRegion coinsTextBoard;
    public TextureRegion dialogHalfSize;
    public TextureRegion dialogQuarterSize;
    public TextureRegion dialogQuarterSizeOpen;
    public TextureRegion cardBackgroundTexture;
    public TextureRegion noHeaderDialog;
    public TextureRegion keepGoingTexture;
    public TextureRegion defaultButton;
    public TextureRegion settingHeader;
    public TextureRegion powerUpTexture;
    public TextureRegion acceptTerms;
    public TextureRegion rejectTerms;
    public TextureRegion checkedTexture;
    public TextureRegion uncheckedTexture;
    public TextureRegion easyTexture;
    public TextureRegion mediumTexture;
    public TextureRegion hardTexture;
    public TextureRegion difficultyTexture;
    public TextureRegion highScoreBackground;
    public TextureRegion highScoreTrophy;
    public TextureRegion highScoreLabel;
    public TextureRegion bottomNavItems;
    public TextureRegion upgradeCardBG;
    public TextureRegion upgradeItemButton;
    public TextureRegion levelCardBg;
    public TextureRegion levelClearedText;
    public TextureRegion levelLockTexture;
    public TextureRegion levelMarkedTexture;
    public TextureRegion levelSelectedBg;
    public TextureRegion levelPlayButton;
    public TextureRegion levelTaskHeaderBg;
    public TextureRegion defaultBlock;
    public TextureRegion playButtonTexture;
    public TextureRegion settingsButtonTexture;
    public TextureRegion claimButtonTexture;
    public TextureRegion claimedButtonTexture;
    public TextureRegion achievementBackground;
    public TextureRegion goButtonTexture;

    // ---- Gameplay Textures ----
    public Texture snakeSkins;
    public Texture blockTexture;
    public Texture lineTexture;
    public Texture backgroundTexture;
    public Texture fontTexture;
    public Texture finishLineTexture;

    // ---- Particles & Audio ----
    public ParticleEffect bloodEffect;
    public ParticleEffect confettiCanon;
    public ParticleEffect blockDestroy;
    public BitmapFont font;
    public Sound collisionSound;
    public Sound powerUpSound;
    public Sound gameOverSound;
    public Sound winObjectSound;
    public Music backgroundMusic;
    public TextureRegion[] gameTutorialTitles = new TextureRegion[5];
    public TextureRegion levelClearedMessage;

    // ---- Internal managers ----
    private AssetManager assetManager;
    private final Map<String, TextureAtlas> uiAtlases = new HashMap<>();
    private final Map<String, TextureRegion> regionCache = new HashMap<>();
    private final String[] uiGroups = {
        "global", "dialog", "bottomNav", "home", "main_game", "game_over", "menu", "levels"
    };
    private final String[] suffixes = {"", "_candy", "_elements", "_bricks", "_metal", "_wool", "_wood", "_solid"};
    private int blockAssetId = 1;
    private String suffix;

    private Assets() {
        suffix = suffixes[blockAssetId];
    }

    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }
        return instance;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    /**
     * CRITICAL: Call this when the app is resumed (in ApplicationListener.resume())
     * to reload textures that may have been lost.
     */
    public void reloadOnResume() {
        Gdx.app.log("Assets", "Reloading assets on resume");

        // Dispose old OpenGL resources
        disposeTextures();

        // Reload everything
        loadAllUI();
        GameInstance.getInstance().resetInstance();
        // Make sure assetManager is ready
        if (assetManager != null) {
            // Force reload of essential textures
            assetManager.update();
            // Notify that assets are loaded
            assetsLoaded = false;
            uiLoaded = false;
        } else {
            Gdx.app.error("Assets", "AssetManager is null on resume!");
            assetManager= new AssetManager();
            loadAllUI();
            assetManager.update();
        }
    }

    /**
     * Dispose only texture resources (called on context loss)
     */
    private void disposeTextures() {
        // Dispose gameplay textures
        if (snakeSkins != null) { snakeSkins.dispose(); snakeSkins = null; }
        if (blockTexture != null) { blockTexture.dispose(); blockTexture = null; }
        if (lineTexture != null) { lineTexture.dispose(); lineTexture = null; }
        if (backgroundTexture != null) { backgroundTexture.dispose(); backgroundTexture = null; }
        if (fontTexture != null) { fontTexture.dispose(); fontTexture = null; }
        if (finishLineTexture != null) { finishLineTexture.dispose(); finishLineTexture = null; }
        if (powerUpTexture != null && powerUpTexture.getTexture() != null) {
            powerUpTexture.getTexture().dispose();
            powerUpTexture = null;
        }

        // Dispose UI atlases
        for (TextureAtlas atlas : uiAtlases.values()) {
            atlas.dispose();
        }
        uiAtlases.clear();
        regionCache.clear();

        // Nullify UI regions
        nullifyUIRegions();
    }

    private void nullifyUIRegions() {
        menuBGTexture = null;
        snakeSkinButtonTexture = null;
        shareButtonTexture = null;
        addButtonTexture = null;
        skipTutorialButton =null;
        homeButtonTexture = null;
        restartButtonTexture = null;
        resumeButtonTexture = null;
        tapToContinueTexture = null;
        pauseBtnTexture = null;
        logoTexture = null;
        barTexture = null;
        coinTexture = null;
        healthTexture = null;
        diamondTexture = null;
        sliderBackgroundTexture = null;
        sliderFillTexture = null;
        sliderKnobTexture = null;
        boxDialogTexture = null;
        pauseHeaderTexture = null;
        soundTexture = null;
        musicTexture = null;
        replayButtonTexture = null;
        upgradeButtonTexture = null;
        home2ButtonTexture = null;
        closeTexture = null;
        bottomNavBg = null;
        defaultItemTexture = null;
        selectOverlayTexture = null;
        defaultOverlayTexture = null;
        licenseCreditsTexture = null;
        privacyPolicyTexture = null;
        tosTexture = null;
        giveUpTexture = null;
        continueTexture = null;
        failTexture = null;
        upLayout = null;
        selectedButtonTexture = null;
        equippedBorderTexture = null;
        selectButtonTexture = null;
        buyButtonTexture = null;
        levelsButtonTexture = null;
        priceBarTexture = null;
        coinPilesTexture = null;
        diamondPilesTexture = null;
        portionPilesTexture = null;
        gameOverHeaderTexture = null;
        victoryHeaderTexture = null;
        nextLevelTexture = null;
        scoreTextBoard = null;
        highScoreTextBoard = null;
        upgradeProgressBG = null;
        upgradeProgressFill = null;
        coinsTextBoard = null;
        dialogHalfSize = null;
        dialogQuarterSize = null;
        dialogQuarterSizeOpen = null;
        cardBackgroundTexture = null;
        noHeaderDialog = null;
        keepGoingTexture = null;
        defaultButton = null;
        settingHeader = null;
        acceptTerms = null;
        rejectTerms = null;
        checkedTexture = null;
        uncheckedTexture = null;
        easyTexture = null;
        mediumTexture = null;
        hardTexture = null;
        difficultyTexture = null;
        highScoreBackground = null;
        highScoreTrophy = null;
        highScoreLabel = null;
        bottomNavItems = null;
        upgradeCardBG = null;
        upgradeItemButton = null;
        levelCardBg = null;
        levelClearedText = null;
        levelLockTexture = null;
        levelMarkedTexture = null;
        levelSelectedBg = null;
        levelPlayButton = null;
        levelTaskHeaderBg = null;
        achievementBackground = null;
        defaultBlock = null;
        playButtonTexture = null;
        settingsButtonTexture = null;
        gameTutorialTitles = new TextureRegion[5];
        levelClearedMessage = null;
        claimButtonTexture = null;
        claimedButtonTexture = null;
        goButtonTexture = null;
    }

    // -------------------------------------------------------------------------
    // UI Loading Methods (with proper error handling)
    // -------------------------------------------------------------------------
    public void loadGameAssets(){
        assetManager.load("game/snake_multiple_skins.png", Texture.class);
        assetManager.load("game/blocks" + suffix + ".png", Texture.class);
        assetManager.load("game/lineBG.png", Texture.class);
        assetManager.load("game/level_cleared.png", Texture.class);
        assetManager.load("game/finishLine.png", Texture.class);

    }

    public void loadUIGroup(String groupName) {
        if (uiAtlases.containsKey(groupName)) {
            // If atlas exists but may be invalid, dispose and reload
            TextureAtlas existing = uiAtlases.get(groupName);
            if (existing != null) {
                // Check if atlas textures are valid
                try {
                    existing.findRegion("test");
                } catch (Exception e) {
                    Gdx.app.log("Assets", "Reloading invalid atlas: " + groupName);
                    existing.dispose();
                    uiAtlases.remove(groupName);
                    regionCache.clear(); // Clear cache to force reload
                }
            }
            return;
        }

        String atlasPath = "ui/" + groupName + ".atlas";
        try {
            if (Gdx.files.internal(atlasPath).exists()) {
                TextureAtlas atlas = new TextureAtlas(atlasPath);
                uiAtlases.put(groupName, atlas);
                for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
                    regionCache.put(region.name, region);
                }
                Gdx.app.log("Assets", "Loaded UI group: " + groupName);
            } else {
                Gdx.app.error("Assets", "Atlas not found: " + atlasPath);
            }
        } catch (Exception e) {
            Gdx.app.error("Assets", "Error loading atlas: " + atlasPath, e);
        }
    }

    public void loadTutorialUiGroup(String groupName) {
        if (uiAtlases.containsKey(groupName)) return;
        String atlasPath = "game/" + groupName + ".atlas";
        try {
            if (Gdx.files.internal(atlasPath).exists()) {
                TextureAtlas atlas = new TextureAtlas(atlasPath);
                uiAtlases.put(groupName, atlas);
                for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
                    regionCache.put(region.name, region);
                }
                Gdx.app.log("Assets", "Loaded UI group: " + groupName);
            } else {
                Gdx.app.error("Assets", "Atlas not found: " + atlasPath);
            }
        } catch (Exception e) {
            Gdx.app.error("Assets", "Error loading atlas: " + atlasPath, e);
        }
    }

    public void disposeUIGroup(String groupName) {
        TextureAtlas atlas = uiAtlases.remove(groupName);
        if (atlas != null) {
            for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
                regionCache.remove(region.name);
            }
            atlas.dispose();
            Gdx.app.log("Assets", "Disposed UI group: " + groupName);
        }
    }

    public void loadAllUI() {
        uiLoaded = false;
        for (String group : uiGroups) {
            loadUIGroup(group);
        }
        loadTutorialUiGroup("game_tutorial_title");
        uiLoaded = true;
    }

    public void disposeAllUI() {
        for (String group : uiGroups) {
            disposeUIGroup(group);
        }
        uiLoaded = false;
    }

    public TextureRegion getUIRegion(String regionName) {
        TextureRegion region = regionCache.get(regionName);
        if (region == null) {
            Gdx.app.error("Assets", "Missing UI region: " + regionName);
            // Return a placeholder to avoid crashes
            return createPlaceholderRegion();
        }
        return region;
    }

    private TextureRegion createPlaceholderRegion() {
        // Create a small placeholder texture to prevent crashes
        try {
            Texture placeholder = new Texture(1, 1, com.badlogic.gdx.graphics.Pixmap.Format.RGBA8888);
            return new TextureRegion(placeholder);
        } catch (Exception e) {
            return null;
        }
    }

    // -------------------------------------------------------------------------
    // Loading all assets (with proper error handling)
    // -------------------------------------------------------------------------

    public void loadAllAssets() {
        if (assetManager == null) {
            Gdx.app.error("Assets", "AssetManager is null! Call setAssetManager() first.");
            return;
        }

        assetsLoaded = false;
        Gdx.app.log("Assets", "Loading all assets...");

        try {
            // ---- Non‑UI textures ----
            loadGameAssets();
            assetManager.load("game/powerUpTexture.png", Texture.class);
            assetManager.load("shaderLayer.png", Texture.class);
            assetManager.load("fonts/ImageFonts/FontsHDTwo.png", Texture.class);

            // ---- Particles ----
            assetManager.load("particles/snakeBlood.p", ParticleEffect.class);
            assetManager.load("particles/left_victory_particle.p", ParticleEffect.class);
            assetManager.load("particles/right_victory_particle.p", ParticleEffect.class);
            assetManager.load("particles/confetti_cannon.p", ParticleEffect.class);
            assetManager.load("particles/block destroy/block_destroy.p", ParticleEffect.class);

            // ---- Fonts ----
            assetManager.load("fonts/ImageFonts/FontsHDTwo.fnt", BitmapFont.class);

            // ---- Sounds ----
            assetManager.load("sounds/hitObstacle.wav", Sound.class);
            assetManager.load("sounds/winObject.wav", Sound.class);
            assetManager.load("sounds/powerUp.wav", Sound.class);
            assetManager.load("sounds/gameOver.wav", Sound.class);

            // ---- Music ----
            assetManager.load("music/backgroundMusic.mp3", Music.class);

            // Start loading
            assetManager.update();

        } catch (Exception e) {
            Gdx.app.error("Assets", "Error loading assets", e);
        }
    }
    public void assignGameAssets(){
        snakeSkins = assetManager.get("game/snake_multiple_skins.png", Texture.class);
        blockTexture = assetManager.get("game/blocks" + suffix + ".png", Texture.class);
        lineTexture = assetManager.get("game/lineBG.png", Texture.class);
        finishLineTexture = assetManager.get("game/finishLine.png", Texture.class);

        Texture levelClearedTex = assetManager.get("game/level_cleared.png", Texture.class);
        levelClearedMessage = new TextureRegion(levelClearedTex);


    }

    public void assignAssets() {
        if (assetManager == null) {
            Gdx.app.error("Assets", "AssetManager is null! Cannot assign assets.");
            return;
        }

        try {
            // ---- Assign gameplay textures ----
            assignGameAssets();
            // ---- powerUpTexture ----
            Texture powerUpTex = assetManager.get("game/powerUpTexture.png", Texture.class);
            powerUpTexture = new TextureRegion(powerUpTex);
            backgroundTexture = assetManager.get("shaderLayer.png", Texture.class);
            fontTexture = assetManager.get("fonts/ImageFonts/FontsHDTwo.png", Texture.class);


            // ---- Particles ----
            bloodEffect = assetManager.get("particles/snakeBlood.p", ParticleEffect.class);
            confettiCanon = assetManager.get("particles/confetti_cannon.p", ParticleEffect.class);
            blockDestroy = assetManager.get("particles/block destroy/block_destroy.p", ParticleEffect.class);

            // ---- Sounds ----
            collisionSound = assetManager.get("sounds/hitObstacle.wav", Sound.class);
            powerUpSound = assetManager.get("sounds/powerUp.wav", Sound.class);
            winObjectSound = assetManager.get("sounds/winObject.wav", Sound.class);
            gameOverSound = assetManager.get("sounds/gameOver.wav", Sound.class);

            // ---- Music ----
            backgroundMusic = assetManager.get("music/backgroundMusic.mp3", Music.class);

            // ---- Font ----
            font = assetManager.get("fonts/ImageFonts/FontsHDTwo.fnt", BitmapFont.class);

            // ---- UI TextureRegions ----
            assignUIRegions();

            assetsLoaded = true;
            Gdx.app.log("Assets", "Assets assigned successfully.");

        } catch (Exception e) {
            Gdx.app.error("Assets", "Error assigning assets", e);
            assetsLoaded = false;
        }
    }

    private void assignUIRegions() {
        // Home group
        playButtonTexture = getUIRegion("PlayButtonUpdate");
        settingsButtonTexture = getUIRegion("settingsIconNew");
        snakeSkinButtonTexture = getUIRegion("skinIconNew");
        shareButtonTexture = getUIRegion("shareIconNew");
        logoTexture = getUIRegion("SnakeBlockLogo");
        barTexture = getUIRegion("scoreBoard");
        levelsButtonTexture = getUIRegion("LevelsButton");
        acceptTerms = getUIRegion("acceptTerms");
        rejectTerms = getUIRegion("rejectTerms");
        tosTexture = getUIRegion("termsOfService");
        privacyPolicyTexture = getUIRegion("privacyPolicy");
        licenseCreditsTexture = getUIRegion("licenseAndCredits");
        checkedTexture = getUIRegion("checkedCircle");
        uncheckedTexture = getUIRegion("uncheckedCircle");
        easyTexture = getUIRegion("easyLabel");
        mediumTexture = getUIRegion("mediumLabel");
        hardTexture = getUIRegion("hardLabel");
        difficultyTexture = getUIRegion("difficultyLabel");
        highScoreBackground = getUIRegion("highScoreBg");
        highScoreTrophy = getUIRegion("highScoreTrophy");
        highScoreLabel = getUIRegion("highScoreLabel");

        // Main_game group
        pauseBtnTexture = getUIRegion("pauseIconNew");
        skipTutorialButton = getUIRegion("SkipButton");
        homeButtonTexture = getUIRegion("homePIcon");
        restartButtonTexture = getUIRegion("restartPIcon");
        resumeButtonTexture = getUIRegion("playPIcon");
        tapToContinueTexture = getUIRegion("tapToContinue");
        giveUpTexture = getUIRegion("giveUpText");
        failTexture = getUIRegion("failText");
        continueTexture = getUIRegion("continueText");
        defaultBlock = getUIRegion("defaultBlock");
        levelTaskHeaderBg = getUIRegion("levelTaskBg");
        achievementBackground = getUIRegion("achievementPopUp");

        // Tutorial
        gameTutorialTitles[0] = getUIRegion("swipeLeft");
        gameTutorialTitles[1] = getUIRegion("swipeRight");
        gameTutorialTitles[2] = getUIRegion("winBodyCollect");
        gameTutorialTitles[3] = getUIRegion("destroyLowestBlock");
        gameTutorialTitles[4] = getUIRegion("winPowerUp");

        // Dialog group
        pauseHeaderTexture = getUIRegion("pauseDialogHeader");
        boxDialogTexture = getUIRegion("Dialog");
        closeTexture = getUIRegion("CancelButton");
        dialogHalfSize = getUIRegion("DialogHalf");
        dialogQuarterSize = getUIRegion("DialogQuarter");
        dialogQuarterSizeOpen = getUIRegion("DialogQuarterOpen");
        noHeaderDialog = getUIRegion("DialogNoHeader");
        keepGoingTexture = getUIRegion("keep_going_title");
        settingHeader = getUIRegion("settingsHeader");
        soundTexture = getUIRegion("sound_icon");
        musicTexture = getUIRegion("music_icon");
        sliderBackgroundTexture = getUIRegion("sliderBackground");
        sliderFillTexture = getUIRegion("sliderFill");
        sliderKnobTexture = getUIRegion("sliderKnob");
        defaultButton = getUIRegion("defaultButton");

        // Game_over group
        replayButtonTexture = getUIRegion("replayGOButton");
        upgradeButtonTexture = getUIRegion("upgradeBtnImg");
        home2ButtonTexture = getUIRegion("homeGOButton");
        gameOverHeaderTexture = getUIRegion("gameOverTitle");
        scoreTextBoard = getUIRegion("scoreTextBoard");
        highScoreTextBoard = getUIRegion("highScoreTextBoard");
        coinsTextBoard = getUIRegion("coinsTextBoard");
        victoryHeaderTexture = getUIRegion("victoryTitle");
        nextLevelTexture = getUIRegion("nextLevelButton");

        // Menu group
        menuBGTexture = getUIRegion("menuBG");
        equippedBorderTexture = getUIRegion("equippedBorderBg");
        cardBackgroundTexture = getUIRegion("cardBackground");
        selectedButtonTexture = getUIRegion("selectedButton");
        selectButtonTexture = getUIRegion("selectButton");
        buyButtonTexture = getUIRegion("buyButton");
        upgradeCardBG = getUIRegion("upgradeCardBg");
        upgradeItemButton = getUIRegion("upgradeItemBtn");
        upgradeProgressBG = getUIRegion("upgradeProgressBG");
        upgradeProgressFill = getUIRegion("upgradeProgressFill");
        priceBarTexture = getUIRegion("priceBar");
        coinPilesTexture = getUIRegion("coinPiles");
        diamondPilesTexture = getUIRegion("diamondPiles");
        portionPilesTexture = getUIRegion("portionPiles");
        claimButtonTexture = getUIRegion("claimBtn");
        claimedButtonTexture = getUIRegion("claimedBtn");
        goButtonTexture = getUIRegion("goBtn");

        // BottomNav group
        bottomNavBg = getUIRegion("downBGBlue");
        upLayout = getUIRegion("upLayout");
        defaultItemTexture = getUIRegion("defaultItemsLook");
        selectOverlayTexture = getUIRegion("selectIndicator");
        defaultOverlayTexture = getUIRegion("defaultNavItem");
        bottomNavItems = getUIRegion("bottomNavItems");

        // Levels group
        levelCardBg = getUIRegion("levelBg");
        levelClearedText = getUIRegion("levelClearedText");
        levelLockTexture = getUIRegion("levelLock");
        levelMarkedTexture = getUIRegion("levelMarked");
        levelSelectedBg = getUIRegion("selectFrame");
        levelPlayButton = getUIRegion("levelsPlayButton2");

        // Global group
        addButtonTexture = getUIRegion("addButton2");
        coinTexture = getUIRegion("snakeCoin");
        healthTexture = getUIRegion("snakeHealthPortion");
        diamondTexture = getUIRegion("snakeDiamond");
    }

    // -------------------------------------------------------------------------
    // Progress & disposal
    // -------------------------------------------------------------------------

    public float getProgress() {
        if (assetManager == null) return 0f;
        return assetManager.getProgress();
    }

    public boolean isLoaded() {
        if (assetManager == null) return false;
        return assetManager.update();
    }

    public boolean isAssetsLoaded() {
        return assetsLoaded && uiLoaded;
    }

    public void finishLoading() {
        if (assetManager != null) {
            assetManager.finishLoading();
        }
    }

    @Override
    public void dispose() {
        Gdx.app.log("Assets", "Disposing all assets...");

        // Dispose UI atlases
        disposeAllUI();

        // Dispose AssetManager
        if (assetManager != null) {
            assetManager.dispose();
        }

        // Dispose font
        if (font != null) {
            font.dispose();
            font = null;
        }

        // Dispose powerUpTexture
        if (powerUpTexture != null && powerUpTexture.getTexture() != null) {
            powerUpTexture.getTexture().dispose();
            powerUpTexture = null;
        }

        assetsLoaded = false;
        uiLoaded = false;
        instance = null;
    }

    // -------------------------------------------------------------------------
    // Memory logging
    // -------------------------------------------------------------------------

    public static void logMemory() {
        Gdx.app.log("Memory", "Java Heap: " + (Gdx.app.getJavaHeap() / 1024 / 1024) + " MB");
        Gdx.app.log("Memory", "Native Heap: " + (Gdx.app.getNativeHeap() / 1024 / 1024) + " MB");
    }
}
