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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

import java.util.HashMap;
import java.util.Map;

/**
 * New asset class that loads UI textures as TextureRegions from atlas files.
 * Gameplay textures remain as plain Textures loaded via AssetManager.
 * Atlas files are expected in assets/ui/ with names: global, dialog, bottomNav,
 * home, main_game, game_over, menu, levels (each .atlas + .png).
 */
public class Assets2 implements Disposable {

    private static Assets2 instance;

    // ---- UI TextureRegions (all fields that were Texture become TextureRegion) ----
    public TextureRegion menuBGTexture;
    public TextureRegion snakeSkinButtonTexture;
    public TextureRegion shareButtonTexture;
    public TextureRegion addButtonTexture;
    public TextureRegion reviveAdsTexture;
    public TextureRegion reviveHealthTexture;
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
    public TextureRegion defaultTitleTexture;
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
    public TextureRegion powerUpTexture;      // Note: this is still a TextureRegion, loaded from atlas? Actually it's a standalone PNG – we'll keep as Texture? But user may want region. We'll load from atlas if exists, else fallback.
    public TextureRegion acceptTerms;
    public TextureRegion rejectTerms;
    public TextureRegion checkedTexture;
    public TextureRegion uncheckedTexture;
    public TextureRegion easyTexture;
    public TextureRegion mediumTexture;
    public TextureRegion hardTexture;
    public TextureRegion difficultyTexture;
    public TextureRegion bottomNavItems;
    public TextureRegion upgradeCardBG;
    public TextureRegion upgradeItemButton;
    public TextureRegion levelCardBg;
    public TextureRegion levelClearedText;
    public TextureRegion levelLockTexture;
    public TextureRegion levelMarkedTexture;
    public TextureRegion levelSelectedBg;
    public TextureRegion levelPlayButton;
    // Note: finishLineTexture is gameplay, remains Texture (see below)

    // ---- Gameplay Textures (unchanged type: Texture) ----
    public Texture snakeSkins;
    public Texture blockTexture;
    public Texture lineTexture;
    public Texture backgroundTexture;
    public Texture fontTexture;
    public Texture finishLineTexture;     // gameplay, not UI

    // ---- Particles & Audio (unchanged) ----
    public ParticleEffect bloodEffect;
    public ParticleEffect leftVictoryEffect;
    public ParticleEffect rightVictoryEffect;
    public BitmapFont font;
    public Sound collisionSound;
    public Sound powerUpSound;
    public Sound gameOverSound;
    public Sound winObjectSound;
    public Music backgroundMusic;



    // ---- Internal managers ----
    private AssetManager assetManager;          // for gameplay textures, sounds, particles, music
    private final Map<String, TextureAtlas> uiAtlases = new HashMap<>();   // groupName -> atlas
    private final Map<String, TextureRegion> regionCache = new HashMap<>(); // regionName -> region (fast lookup)
    private final String[] uiGroups = {
        "global", "dialog", "bottomNav", "home", "main_game", "game_over", "menu", "levels"
    };

    private Assets2() {}

    public static Assets2 getInstance() {
        if (instance == null) {
            instance = new Assets2();
        }
        return instance;
    }

    public void setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    // -------------------------------------------------------------------------
    // Public UI loading / disposal (separate groups or all together)
    // -------------------------------------------------------------------------

    /** Load one UI atlas group (e.g., "home"). */
    public void loadUIGroup(String groupName) {
        if (uiAtlases.containsKey(groupName)) return;
        String atlasPath = "ui/" + groupName + ".atlas";
        if (Gdx.files.internal(atlasPath).exists()) {
            TextureAtlas atlas = new TextureAtlas(atlasPath);
            uiAtlases.put(groupName, atlas);
            // Cache all regions from this atlas
            for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
                regionCache.put(region.name, region);
            }
            Gdx.app.log("Assets2", "Loaded UI group: " + groupName);
        } else {
            Gdx.app.error("Assets2", "Atlas not found: " + atlasPath);
        }
    }

    /** Dispose one UI atlas group. */
    public void disposeUIGroup(String groupName) {
        TextureAtlas atlas = uiAtlases.remove(groupName);
        if (atlas != null) {
            // Remove all regions that came from this atlas
            for (TextureAtlas.AtlasRegion region : atlas.getRegions()) {
                regionCache.remove(region.name);
            }
            atlas.dispose();
            Gdx.app.log("Assets2", "Disposed UI group: " + groupName);
        }
    }

    /** Load all known UI groups (for quick testing). */
    public void loadAllUI() {
        for (String group : uiGroups) {
            loadUIGroup(group);
        }
    }

    /** Dispose all UI groups. */
    public void disposeAllUI() {
        for (String group : uiGroups) {
            disposeUIGroup(group);
        }
    }

    /** Retrieve a TextureRegion by its original PNG name (without extension). */
    public TextureRegion getUIRegion(String regionName) {
        TextureRegion region = regionCache.get(regionName);
        if (region == null) {
            Gdx.app.error("Assets2", "Missing UI region: " + regionName);
        }
        return region;
    }

    // -------------------------------------------------------------------------
    // Loading all assets (non‑UI via AssetManager + UI atlases)
    // -------------------------------------------------------------------------

    public void loadAllAssets() {
        // ---- Non‑UI textures (gameplay) ----
        assetManager.load("snake_multiple_skins.png", Texture.class);
        assetManager.load("blocks.png", Texture.class);
        assetManager.load("lineBG.png", Texture.class);
        assetManager.load("shaderLayer.png", Texture.class);
        assetManager.load("fonts/ImageFonts/FontsHDTwo.png", Texture.class);
        assetManager.load("game/finishLine.png", Texture.class);
        // powerUpTexture is used as UI but not in raw/ – we keep it as TextureRegion but load via AssetManager as Texture then wrap? Let's keep simple: load as Texture and later convert to region? Actually it's a standalone UI element. We'll load it as Texture and then create a region from it.
        assetManager.load("powerUpTexture.png", Texture.class);

        // ---- Particles ----
        assetManager.load("particles/snakeBlood.p", ParticleEffect.class);
        assetManager.load("particles/left_victory_particle.p", ParticleEffect.class);
        assetManager.load("particles/right_victory_particle.p", ParticleEffect.class);

        // ---- Sounds ----
        assetManager.load("sounds/hitObstacle.wav", Sound.class);
        assetManager.load("sounds/winObject.wav", Sound.class);
        assetManager.load("sounds/powerUp.wav", Sound.class);
        assetManager.load("sounds/gameOver.wav", Sound.class);

        // ---- Music (optional) ----
        assetManager.load("music/backgroundMusic.mp3", Music.class);
    }

    /** Assign all loaded assets to their fields. Must be called after assetManager.finishLoading() and after loading desired UI groups. */
    public void assignAssets() {
        // ---- Assign gameplay textures ----
        snakeSkins = assetManager.get("snake_multiple_skins.png", Texture.class);
        blockTexture = assetManager.get("blocks.png", Texture.class);
        lineTexture = assetManager.get("lineBG.png", Texture.class);
        backgroundTexture = assetManager.get("shaderLayer.png", Texture.class);
        fontTexture = assetManager.get("fonts/ImageFonts/FontsHDTwo.png", Texture.class);
        finishLineTexture = assetManager.get("game/finishLine.png", Texture.class);

        // ---- powerUpTexture: convert loaded Texture to a TextureRegion ----
        Texture powerUpTex = assetManager.get("powerUpTexture.png", Texture.class);
        powerUpTexture = new TextureRegion(powerUpTex);

        // ---- Particles ----
        bloodEffect = assetManager.get("particles/snakeBlood.p", ParticleEffect.class);
        leftVictoryEffect = assetManager.get("particles/left_victory_particle.p", ParticleEffect.class);
        rightVictoryEffect = assetManager.get("particles/right_victory_particle.p", ParticleEffect.class);

        // ---- Sounds ----
        collisionSound = assetManager.get("sounds/hitObstacle.wav", Sound.class);
        powerUpSound = assetManager.get("sounds/powerUp.wav", Sound.class);
        winObjectSound = assetManager.get("sounds/winObject.wav", Sound.class);
        gameOverSound = assetManager.get("sounds/gameOver.wav", Sound.class);

        // ---- Music ----
        backgroundMusic = assetManager.get("music/backgroundMusic.mp3", Music.class);

        // ---- Font ----
        font = new BitmapFont();

        // ---- UI TextureRegions (all from atlases) ----
        // home group
        playButtonTexture       = getUIRegion("PlayButtonUpdate");
        settingsButtonTexture   = getUIRegion("settingsIconNew");
        snakeSkinButtonTexture  = getUIRegion("skinIconNew");
        shareButtonTexture      = getUIRegion("shareIconNew");
        logoTexture             = getUIRegion("SnakeBlockLogo");
        barTexture              = getUIRegion("scoreBoard");
        levelsButtonTexture     = getUIRegion("LevelsButton");
        acceptTerms             = getUIRegion("acceptTerms");
        rejectTerms             = getUIRegion("rejectTerms");
        tosTexture              = getUIRegion("termsOfService");
        privacyPolicyTexture    = getUIRegion("privacyPolicy");
        licenseCreditsTexture   = getUIRegion("licenseAndCredits");
        checkedTexture          = getUIRegion("checkedCircle");
        uncheckedTexture        = getUIRegion("uncheckedCircle");
        easyTexture             = getUIRegion("easyLabel");
        mediumTexture           = getUIRegion("mediumLabel");
        hardTexture             = getUIRegion("hardLabel");
        difficultyTexture       = getUIRegion("difficultyLabel");


        // main_game group
        pauseBtnTexture         = getUIRegion("pauseIconNew");
        reviveAdsTexture        = getUIRegion("watchAds");
        reviveHealthTexture     = getUIRegion("reviveWithHealth");
        homeButtonTexture       = getUIRegion("homePIcon");
        restartButtonTexture    = getUIRegion("restartPIcon");
        resumeButtonTexture     = getUIRegion("playPIcon");
        tapToContinueTexture    = getUIRegion("tapToContinue");
        giveUpTexture           = getUIRegion("giveUpText");
        failTexture             = getUIRegion("failText");
        continueTexture         = getUIRegion("continueText");

        // dialog group
        pauseHeaderTexture      = getUIRegion("pauseDialogHeader");
        boxDialogTexture        = getUIRegion("Dialog");
        closeTexture            = getUIRegion("CancelButton");
        dialogHalfSize          = getUIRegion("DialogHalf");
        dialogQuarterSize       = getUIRegion("DialogQuarter");
        dialogQuarterSizeOpen   = getUIRegion("DialogQuarterOpen");
        noHeaderDialog          = getUIRegion("DialogNoHeader");
        keepGoingTexture        = getUIRegion("keep_going_title");
        settingHeader           = getUIRegion("settingsHeader");
        soundTexture            = getUIRegion("sound_icon");
        musicTexture            = getUIRegion("music_icon");
        sliderBackgroundTexture = getUIRegion("sliderBackground");
        sliderFillTexture       = getUIRegion("sliderFill");
        sliderKnobTexture       = getUIRegion("sliderKnob");
        defaultButton           = getUIRegion("defaultButton");

        // game_over group
        replayButtonTexture     = getUIRegion("replayGOButton");
        upgradeButtonTexture    = getUIRegion("upgradeBtnImg");
        home2ButtonTexture      = getUIRegion("homeGOButton");
        gameOverHeaderTexture   = getUIRegion("gameOverTitle");
        scoreTextBoard          = getUIRegion("scoreTextBoard");
        highScoreTextBoard      = getUIRegion("highScoreTextBoard");
        coinsTextBoard          = getUIRegion("coinsTextBoard");

        // menu group
        menuBGTexture           = getUIRegion("menuBG");
        equippedBorderTexture   = getUIRegion("equippedBorderBg");
        cardBackgroundTexture   = getUIRegion("cardBackground");
        selectedButtonTexture   = getUIRegion("selectedButton");
        selectButtonTexture     = getUIRegion("selectButton");
        buyButtonTexture        = getUIRegion("buyButton");
        upgradeCardBG           = getUIRegion("upgradeCardBg");
        upgradeItemButton       = getUIRegion("upgradeItemBtn");
        upgradeProgressBG       = getUIRegion("upgradeProgressBG");
        upgradeProgressFill     = getUIRegion("upgradeProgressFill");
        priceBarTexture         = getUIRegion("priceBar");
        coinPilesTexture        = getUIRegion("coinPiles");
        diamondPilesTexture     = getUIRegion("diamondPiles");
        portionPilesTexture     = getUIRegion("portionPiles");

        // bottomNav group
        bottomNavBg             = getUIRegion("downBGBlue");
        upLayout                = getUIRegion("upLayout");
        defaultItemTexture      = getUIRegion("defaultItemsLook");
        selectOverlayTexture    = getUIRegion("selectIndicator");
        defaultOverlayTexture   = getUIRegion("defaultNavItem");
        bottomNavItems          = getUIRegion("bottomNavItems");

        // levels group
        levelCardBg             = getUIRegion("levelBg");
        levelClearedText        = getUIRegion("levelClearedText");
        levelLockTexture        = getUIRegion("levelLock");
        levelMarkedTexture      = getUIRegion("levelMarked");
        levelSelectedBg         = getUIRegion("selectFrame");
        levelPlayButton         = getUIRegion("levelsPlayButton2");


        // global group (if any)
        addButtonTexture        = getUIRegion("addButton2");
        coinTexture             = getUIRegion("snakeCoin");
        healthTexture           = getUIRegion("snakeHealthPortion");
        diamondTexture          = getUIRegion("snakeDiamond");
    }

    // -------------------------------------------------------------------------
    // Progress & disposal
    // -------------------------------------------------------------------------

    public float getProgress() {
        return assetManager.getProgress();
    }

    public boolean isLoaded() {
        return assetManager.update();
    }

    @Override
    public void dispose() {
        // Dispose UI atlases
        disposeAllUI();
        // Dispose AssetManager (gameplay textures, sounds, particles)
        assetManager.dispose();
        // Dispose font
        if (font != null) font.dispose();
        // Dispose the powerUpTexture's underlying texture if we created a region from it
        if (powerUpTexture != null && powerUpTexture.getTexture() != null) {
            powerUpTexture.getTexture().dispose();
        }
    }

    public static void logMemory() {
        Gdx.app.log("Memory", "Java Heap: " + (Gdx.app.getJavaHeap() / 1024 / 1024) + " MB");
        Gdx.app.log("Memory", "Native Heap: " + (Gdx.app.getNativeHeap() / 1024 / 1024) + " MB");
    }

    // ---- Temporary fields to keep compatibility with old field names (added for clarity) ----
    // The following fields were referenced in the original Assets class but not declared above.
    // They are now added here to avoid compilation errors. Remove if not needed.
    public TextureRegion playButtonTexture;
    public TextureRegion settingsButtonTexture;
}
