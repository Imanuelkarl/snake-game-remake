package com.regensnakevsblock.sbb.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class Assets {
    public Texture menuBGTexture;
    private static Assets instance;
    public Texture snakeSkinButtonTexture;
    public Texture shareButtonTexture;
    public Texture addButtonTexture;
    public Texture reviveAdsTexture;
    public Texture reviveHealthTexture;
    public Texture homeButtonTexture;
    public Texture restartButtonTexture;
    public Texture resumeButtonTexture;
    public Texture tapToContinueTexture;
    public Texture pauseBtnTexture;
    public Texture logoTexture;
    public Texture barTexture;
    public Texture coinTexture;
    public Texture healthTexture;
    public Texture diamondTexture;
    public Texture sliderBackgroundTexture;
    public Texture sliderFillTexture;
    public Texture sliderKnobTexture;
    public Texture boxDialogTexture;
    //Multiple Textures here already
    public Texture snakeSkins;

    public Texture pauseHeaderTexture;
    public Texture soundTexture;

    public Texture musicTexture;
    public Texture replayButtonTexture;
    public Texture upgradeButtonTexture;
    public Texture home2ButtonTexture;
    public Texture closeTexture;
    public Texture defaultTitleTexture;
    public Texture bottomNavBg;
    public Texture defaultItemTexture;
    public Texture selectOverlayTexture;
    public Texture defaultOverlayTexture;
    public Texture fontTexture;

    public Texture licenseCreditsTexture;
    public Texture privacyPolicyTexture;
    public Texture tosTexture;
    public Texture giveUpTexture;
    public Texture continueTexture;
    public Texture failTexture;
    public Texture upLayout;
    public Texture selectedButtonTexture;
    public Texture equippedBorderTexture;
    public Texture selectButtonTexture;
    public Texture buyButtonTexture;
    public Texture levelsButtonTexture;
    public Texture priceBarTexture;
    //Multiple Textures here already
    public Texture coinPilesTexture;
    //Multiple Textures here already
    public Texture diamondPilesTexture;
    //Multiple Textures here already
    public Texture portionPilesTexture;
    public Texture gameOverHeaderTexture;
    public Texture scoreTextBoard;
    public Texture highScoreTextBoard;
    public Texture upgradeProgressBG;
    public Texture upgradeProgressFill;
    public Texture coinsTextBoard;
    public Texture finishLineTexture;
    private AssetManager assetManager;
    //Multiple Textures here already
    public Texture blockTexture;

    public Texture lineTexture;
    public Texture backgroundTexture;

    public Texture playButtonTexture;
    public Texture settingsButtonTexture;

    //Dialog Textures
    public Texture dialogHalfSize;
    public Texture dialogQuarterSize;
    public Texture dialogQuarterSizeOpen;
    public Texture cardBackgroundTexture;
    public Texture noHeaderDialog;
    public Texture keepGoingTexture;
    public Texture defaultButton;
    public Texture settingHeader;
    //Multiple Textures here already
    public Texture powerUpTexture;
    public Texture acceptTerms;
    public Texture rejectTerms;
    //Multiple Textures here already
    public Texture bottomNavItems;
    public Texture upgradeCardBG;
    public Texture upgradeItemButton;
    public Texture levelCardBg;
    public Texture levelClearedText;
    public Texture levelLockTexture;
    public Texture levelMarkedTexture;

    public ParticleEffect bloodEffect;

    public ParticleEffect leftVictoryEffect;
    public ParticleEffect rightVictoryEffect;
    // Fonts
    public BitmapFont font;

    // Sounds
    public Sound collisionSound;
    public Sound powerUpSound;
    public Sound gameOverSound;

    // Music
    public Music backgroundMusic;

    private Assets() {
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

    public void loadAllAssets() {


        // Game Play Textures folder /game/**
        assetManager.load("snake_multiple_skins.png", Texture.class);
        assetManager.load("blocks.png", Texture.class);
        assetManager.load("lineBG.png",Texture.class);
        assetManager.load("game/finishLine.png", Texture.class);

        //Global Ui textures /raw/global/**
        assetManager.load("shaderLayer.png", Texture.class);
        assetManager.load("fonts/ImageFonts/FontsHDTwo.png",Texture.class); //Font should remain in this folder as it is for now
        assetManager.load("raw/addButton2.png", Texture.class);
        assetManager.load("raw/snakeCoin.png", Texture.class);
        assetManager.load("raw/snakeHealthPortion.png", Texture.class);
        assetManager.load("raw/snakeDiamond.png", Texture.class);
        assetManager.load("raw/upLayout.png", Texture.class);
        assetManager.load("raw/menuBG.png", Texture.class);
        assetManager.load("raw/defaultButton.png", Texture.class);
        assetManager.load("raw/defaultItemsLook.png", Texture.class);
        assetManager.load("powerUpTexture.png",Texture.class);
        assetManager.load("raw/downBGBlue.png", Texture.class);

        //Pop up dialog box /raw/dialog/**
        assetManager.load("raw/pauseDialogHeader.png", Texture.class);
        assetManager.load("raw/Dialog.png", Texture.class);
        assetManager.load("raw/sound_icon.png", Texture.class);
        assetManager.load("raw/music_icon.png",Texture.class);
        assetManager.load("raw/CancelButton.png", Texture.class);
        assetManager.load("raw/DialogHalf.png", Texture.class);
        assetManager.load("raw/DialogQuarter.png", Texture.class);
        assetManager.load("raw/DialogQuarterOpen.png", Texture.class);
        assetManager.load("raw/DialogNoHeader.png", Texture.class);
        assetManager.load("raw/keep_going_title.png", Texture.class);
        assetManager.load("raw/settingsHeader.png", Texture.class);
        assetManager.load("raw/sliderBackground.png", Texture.class);
        assetManager.load("raw/sliderFill.png", Texture.class);
        assetManager.load("raw/sliderKnob.png", Texture.class);

        //Bottom Nav /raw/bottomNav/**
        assetManager.load("raw/defaultNavItem.png", Texture.class);
        assetManager.load("raw/selectIndicator.png", Texture.class);
        assetManager.load("raw/bottomNavItems.png", Texture.class);

        //Home Screen UI Textures /raw/home/**
        assetManager.load("raw/PlayButtonUpdate.png", Texture.class);
        assetManager.load("raw/settingsIconNew.png", Texture.class);
        assetManager.load("raw/skinIconNew.png", Texture.class);
        assetManager.load("raw/shareIconNew.png", Texture.class);
        assetManager.load("raw/LevelsButton.png", Texture.class);
        assetManager.load("raw/termsOfService.png", Texture.class);
        assetManager.load("raw/privacyPolicy.png", Texture.class);
        assetManager.load("raw/licenseAndCredits.png", Texture.class);
        assetManager.load("raw/acceptTerms.png", Texture.class);
        assetManager.load("raw/rejectTerms.png", Texture.class);
        assetManager.load("raw/SnakeBlockLogo.png", Texture.class);
        assetManager.load("raw/scoreBoard.png", Texture.class);

        //Main Game UI Textures /raw/main_game/**
        assetManager.load("raw/pauseIconNew.png", Texture.class);
        assetManager.load("raw/watchAds.png", Texture.class);
        assetManager.load("raw/reviveWithHealth.png", Texture.class);
        assetManager.load("raw/tapToContinue.png", Texture.class);
        assetManager.load("raw/homePIcon.png", Texture.class);
        assetManager.load("raw/restartPIcon.png", Texture.class);
        assetManager.load("raw/playPIcon.png", Texture.class);
        assetManager.load("raw/giveUpText.png", Texture.class);
        assetManager.load("raw/failText.png", Texture.class);
        assetManager.load("raw/continueText.png", Texture.class);

        //Game Over Screen UI Textures /raw/game_over/**
        assetManager.load("raw/replayGOButton.png", Texture.class);
        assetManager.load("raw/upgradeBtnImg.png", Texture.class);
        assetManager.load("raw/homeGOButton.png", Texture.class);
        assetManager.load("raw/gameOverTitle.png", Texture.class);
        assetManager.load("raw/scoreTextBoard.png", Texture.class);
        assetManager.load("raw/highScoreTextBoard.png", Texture.class);
        assetManager.load("raw/coinsTextBoard.png", Texture.class);

        //Menu Screen UI Textures /raw/menu/**
        assetManager.load("raw/equippedBorderBg.png", Texture.class);
        assetManager.load("raw/cardBackground.png", Texture.class);
        assetManager.load("raw/selectedButton.png", Texture.class);
        assetManager.load("raw/selectButton.png", Texture.class);
        assetManager.load("raw/buyButton.png", Texture.class);
        assetManager.load("raw/upgradeCardBg.png", Texture.class);
        assetManager.load("raw/upgradeItemBtn.png", Texture.class);
        assetManager.load("raw/upgradeProgressBG.png", Texture.class);
        assetManager.load("raw/upgradeProgressFill.png", Texture.class);
        assetManager.load("raw/priceBar.png", Texture.class);
        assetManager.load("raw/coinPiles.png", Texture.class);
        assetManager.load("raw/diamondPiles.png", Texture.class);
        assetManager.load("raw/portionPiles.png", Texture.class);

        //Levels Screen UI Textures /raw/levels/**
        assetManager.load("raw/levelBg.png", Texture.class);
        assetManager.load("raw/levelClearedText.png", Texture.class);
        assetManager.load("raw/levelLock.png", Texture.class);
        assetManager.load("raw/levelMarked.png", Texture.class);

        //Load Particles
        assetManager.load("particles/snakeBlood.p", ParticleEffect.class);
        assetManager.load("particles/left_victory_particle.p", ParticleEffect.class);
        assetManager.load("particles/right_victory_particle.p", ParticleEffect.class);

        // Load sounds
        assetManager.load("sounds/collision.mp3", Sound.class);
        assetManager.load("sounds/powerup.mp3", Sound.class);
        assetManager.load("sounds/gameover.mp3", Sound.class);

        // Load music
        //assetManager.load("music/background.mp3", Music.class);
    }

    public void assignAssets() {
        // Assign loaded assets
        snakeSkins  = assetManager.get("snake_multiple_skins.png", Texture.class);
        blockTexture = assetManager.get("blocks.png", Texture.class);
        lineTexture= assetManager.get("lineBG.png",Texture.class);
        backgroundTexture = assetManager.get("shaderLayer.png", Texture.class);

        fontTexture =assetManager.get("fonts/ImageFonts/FontsHDTwo.png", Texture.class);

        // Home Screen Textures
        playButtonTexture = assetManager.get("raw/PlayButtonUpdate.png", Texture.class);
        settingsButtonTexture = assetManager.get("raw/settingsIconNew.png", Texture.class);
        tapToContinueTexture = assetManager.get("raw/tapToContinue.png", Texture.class);
        snakeSkinButtonTexture=assetManager.get("raw/skinIconNew.png", Texture.class);
        shareButtonTexture=assetManager.get("raw/shareIconNew.png", Texture.class);
        addButtonTexture=assetManager.get("raw/addButton2.png", Texture.class);
        logoTexture=assetManager.get("raw/SnakeBlockLogo.png", Texture.class);
        barTexture=assetManager.get("raw/scoreBoard.png", Texture.class);
        coinTexture=assetManager.get("raw/snakeCoin.png",Texture.class);
        healthTexture=assetManager.get("raw/snakeHealthPortion.png", Texture.class);
        diamondTexture=assetManager.get("raw/snakeDiamond.png", Texture.class);
        levelsButtonTexture = assetManager.get("raw/LevelsButton.png", Texture.class);

        acceptTerms = assetManager.get("raw/acceptTerms.png", Texture.class);
        rejectTerms =assetManager.get("raw/rejectTerms.png", Texture.class);

        // Main Game Textures
        pauseBtnTexture=assetManager.get("raw/pauseIconNew.png", Texture.class);
        reviveAdsTexture=assetManager.get("raw/watchAds.png", Texture.class);
        reviveHealthTexture=assetManager.get("raw/reviveWithHealth.png", Texture.class);
        homeButtonTexture=assetManager.get("raw/homePIcon.png", Texture.class);
        restartButtonTexture=assetManager.get("raw/restartPIcon.png", Texture.class);
        resumeButtonTexture=assetManager.get("raw/playPIcon.png", Texture.class);
        powerUpTexture=assetManager.get("powerUpTexture.png",Texture.class);
        finishLineTexture = assetManager.get("game/finishLine.png", Texture.class);

        pauseHeaderTexture=assetManager.get("raw/pauseDialogHeader.png", Texture.class);
        boxDialogTexture=assetManager.get("raw/Dialog.png", Texture.class);
        closeTexture=assetManager.get("raw/CancelButton.png", Texture.class);

        dialogHalfSize =assetManager.get("raw/DialogHalf.png", Texture.class);
        dialogQuarterSize = assetManager.get("raw/DialogQuarter.png", Texture.class);
        dialogQuarterSizeOpen =assetManager.get("raw/DialogQuarterOpen.png", Texture.class);
        noHeaderDialog= assetManager.get("raw/DialogNoHeader.png", Texture.class);
        keepGoingTexture =assetManager.get("raw/keep_going_title.png", Texture.class);
        settingHeader =assetManager.get("raw/settingsHeader.png", Texture.class);

        //Text Assets
        giveUpTexture =assetManager.get("raw/giveUpText.png", Texture.class);
        failTexture =assetManager.get("raw/failText.png", Texture.class);
        continueTexture =assetManager.get("raw/continueText.png", Texture.class);
        tosTexture =assetManager.get("raw/termsOfService.png", Texture.class);
        privacyPolicyTexture =assetManager.get("raw/privacyPolicy.png", Texture.class);
        licenseCreditsTexture =assetManager.get("raw/licenseAndCredits.png", Texture.class);


        soundTexture=assetManager.get("raw/sound_icon.png", Texture.class);
        musicTexture=assetManager.get("raw/music_icon.png", Texture.class);
        defaultButton= assetManager.get("raw/defaultButton.png", Texture.class);

        sliderBackgroundTexture=assetManager.get("raw/sliderBackground.png", Texture.class);
        sliderFillTexture=assetManager.get("raw/sliderFill.png", Texture.class);
        sliderKnobTexture= assetManager.get("raw/sliderKnob.png", Texture.class);

        //Game Over Textures
        replayButtonTexture=assetManager.get("raw/replayGOButton.png", Texture.class);
        upgradeButtonTexture=assetManager.get("raw/upgradeBtnImg.png", Texture.class);
        home2ButtonTexture=assetManager.get("raw/homeGOButton.png", Texture.class);
        gameOverHeaderTexture =assetManager.get("raw/gameOverTitle.png", Texture.class);
        scoreTextBoard = assetManager.get("raw/scoreTextBoard.png", Texture.class);
        highScoreTextBoard = assetManager.get("raw/highScoreTextBoard.png", Texture.class);
        coinsTextBoard = assetManager.get("raw/coinsTextBoard.png", Texture.class);

        //Menu Screen Textures
        menuBGTexture=assetManager.get("raw/menuBG.png", Texture.class);
        equippedBorderTexture = assetManager.get("raw/equippedBorderBg.png", Texture.class);
        cardBackgroundTexture = assetManager.get("raw/cardBackground.png", Texture.class);
        selectedButtonTexture = assetManager.get("raw/selectedButton.png", Texture.class);
        selectButtonTexture = assetManager.get("raw/selectButton.png", Texture.class);
        buyButtonTexture = assetManager.get("raw/buyButton.png", Texture.class);

        upgradeCardBG=assetManager.get("raw/upgradeCardBg.png", Texture.class);
        upgradeItemButton =assetManager.get("raw/upgradeItemBtn.png", Texture.class);
        upgradeProgressBG = assetManager.get("raw/upgradeProgressBG.png", Texture.class);
        upgradeProgressFill = assetManager.get("raw/upgradeProgressFill.png", Texture.class);

        //shop
        priceBarTexture= assetManager.get("raw/priceBar.png", Texture.class);
        coinPilesTexture =assetManager.get("raw/coinPiles.png", Texture.class);
        diamondPilesTexture =assetManager.get("raw/diamondPiles.png", Texture.class);
        portionPilesTexture =assetManager.get("raw/portionPiles.png", Texture.class);

        //Bottom Nav
        bottomNavBg=assetManager.get("raw/downBGBlue.png", Texture.class);
        upLayout =assetManager.get("raw/upLayout.png", Texture.class);
        defaultItemTexture=assetManager.get("raw/defaultItemsLook.png", Texture.class);
        selectOverlayTexture=assetManager.get("raw/selectIndicator.png", Texture.class);
        defaultOverlayTexture=assetManager.get("raw/defaultNavItem.png", Texture.class);
        bottomNavItems=assetManager.get("raw/bottomNavItems.png", Texture.class);

        //Level Screen
        levelCardBg = assetManager.get("raw/levelBg.png", Texture.class);
        levelClearedText = assetManager.get("raw/levelClearedText.png", Texture.class);
        levelLockTexture = assetManager.get("raw/levelLock.png", Texture.class);
        levelMarkedTexture = assetManager.get("raw/levelMarked.png", Texture.class);

        //Assign Particles
        bloodEffect = assetManager.get("particles/snakeBlood.p", ParticleEffect.class);
        leftVictoryEffect = assetManager.get("particles/left_victory_particle.p", ParticleEffect.class);
        rightVictoryEffect =assetManager.get("particles/right_victory_particle.p", ParticleEffect.class);

        collisionSound = assetManager.get("sounds/collision.mp3", Sound.class);
        powerUpSound = assetManager.get("sounds/powerup.mp3", Sound.class);
        gameOverSound = assetManager.get("sounds/gameover.mp3", Sound.class);

        //backgroundMusic = assetManager.get("music/background.mp3", Music.class);

        // Create font
        font = new BitmapFont();
    }

    public float getProgress() {
        return assetManager.getProgress();
    }

    public boolean isLoaded() {
        return assetManager.update();
    }

    public void dispose() {
        assetManager.dispose();
        font.dispose();
    }
    public static void logMemory() {
        Gdx.app.log("Memory", "Java Heap: " + (Gdx.app.getJavaHeap() / 1024 / 1024) + " MB");
        Gdx.app.log("Memory", "Native Heap: " + (Gdx.app.getNativeHeap() / 1024 / 1024) + " MB");
    }
}
