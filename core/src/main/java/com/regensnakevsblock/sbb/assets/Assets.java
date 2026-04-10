package com.regensnakevsblock.sbb.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

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
    public Texture snakeSkins;

    public Texture pauseHeaderTexture;
    public Texture soundTexture;

    public Texture musicTexture;
    public Texture replayButtonTexture;
    public Texture upgradeButtonTexture;
    public Texture home2ButtonTexture;
    public Texture navBarTexture;
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
    private AssetManager assetManager;

    // Textures
    public Texture snakeTexture;
    public Texture blockTexture;

    public Texture lineTexture;
    public Texture backgroundTexture;
    public Texture uiTexture;

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
    public Texture powerUpTexture;

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
        // Load textures
        assetManager.load("snake.png", Texture.class);
        assetManager.load("snake_multiple_skins.png", Texture.class);
        assetManager.load("blocks.png", Texture.class);
        assetManager.load("lineBG.png",Texture.class);
        assetManager.load("shaderLayer.png", Texture.class);
        assetManager.load("ui/uiskin.png", Texture.class);

        //Fonts
        assetManager.load("fonts/ImageFonts/FontsHDTwo.png",Texture.class);

        //Home Screen Textures
        //Buttons
        assetManager.load("ui/PlayButtonUpdate.png", Texture.class);
        assetManager.load("ui/settingsIcon2.png", Texture.class);
        assetManager.load("ui/skinIcon.png", Texture.class);
        assetManager.load("ui/shareAppIcon.png", Texture.class);
        assetManager.load("ui/addButton2.png", Texture.class);
        assetManager.load("ui/LevelsButton.png", Texture.class);
        //Static
        assetManager.load("ui/SnakeBlockLogo.png", Texture.class);
        assetManager.load("ui/scoreBoard.png", Texture.class);
        assetManager.load("ui/snakeCoin.png", Texture.class);
        assetManager.load("ui/snakeHealthPortion.png", Texture.class);
        assetManager.load("ui/snakeDiamond.png", Texture.class);
        assetManager.load("ui/bottomNavBackground.png", Texture.class);
        assetManager.load("ui/upLayout.png", Texture.class);

        //Main Game Textures
        assetManager.load("ui/pauseBtn.png", Texture.class);
        assetManager.load("ui/watchAds.png", Texture.class);
        assetManager.load("ui/reviveWithHealth.png", Texture.class);
        assetManager.load("ui/tapToContinue.png", Texture.class);
        assetManager.load("ui/home_p_button.png", Texture.class);
        assetManager.load("ui/restart_p_button.png", Texture.class);
        assetManager.load("ui/play_p_button.png", Texture.class);
        assetManager.load("powerUpTexture.png",Texture.class);

        //Text Assets
        assetManager.load("ui/giveUpText.png", Texture.class);
        assetManager.load("ui/failText.png", Texture.class);
        assetManager.load("ui/continueText.png", Texture.class);
        assetManager.load("ui/termsOfService.png", Texture.class);
        assetManager.load("ui/privacyPolicy.png", Texture.class);
        assetManager.load("ui/licenseAndCredits.png", Texture.class);

        //Pop up dialog box
        assetManager.load("ui/pauseDialogHeader.png", Texture.class);
        assetManager.load("ui/Dialog.png", Texture.class);
        assetManager.load("ui/sound_icon.png", Texture.class);
        assetManager.load("ui/music_icon.png",Texture.class);
        assetManager.load("ui/CancelButton.png", Texture.class);

        assetManager.load("ui/DialogHalf.png", Texture.class);
        assetManager.load("ui/DialogQuarter.png", Texture.class);
        assetManager.load("ui/DialogQuarterOpen.png", Texture.class);
        assetManager.load("ui/DialogNoHeader.png", Texture.class);
        assetManager.load("ui/keep_going_title.png", Texture.class);
        assetManager.load("ui/settingsHeader.png", Texture.class);

        assetManager.load("ui/sliderBackground.png", Texture.class);
        assetManager.load("ui/sliderFill.png", Texture.class);
        assetManager.load("ui/sliderKnob.png", Texture.class);


        //Game Over Screen Textures
        assetManager.load("ui/replay2.png", Texture.class);
        assetManager.load("ui/upgradeBtnImg.png", Texture.class);
        assetManager.load("ui/newHomeBtn.png", Texture.class);

        //Menu Screen Textures
        assetManager.load("ui/menuBG.png", Texture.class);
        assetManager.load("ui/defaultButton.png", Texture.class);

        assetManager.load("ui/equippedBorderBg.png", Texture.class);
        assetManager.load("ui/cardBackground.png", Texture.class);
        assetManager.load("ui/selectedButton.png", Texture.class);
        assetManager.load("ui/selectButton.png", Texture.class);
        assetManager.load("ui/buyButton.png", Texture.class);

        //Bottom Nav
        assetManager.load("ui/defaultItemsLook.png", Texture.class);
        assetManager.load("ui/defaultNavItem.png", Texture.class);
        assetManager.load("ui/selectIndicator.png", Texture.class);
        assetManager.load("ui/downBGBlue.png", Texture.class);

        // Load sounds
        assetManager.load("sounds/collision.mp3", Sound.class);
        assetManager.load("sounds/powerup.mp3", Sound.class);
        assetManager.load("sounds/gameover.mp3", Sound.class);

        // Load music
        //assetManager.load("music/background.mp3", Music.class);
    }

    public void assignAssets() {
        // Assign loaded assets
        snakeTexture = assetManager.get("snake.png", Texture.class);
        snakeSkins  = assetManager.get("snake_multiple_skins.png", Texture.class);
        blockTexture = assetManager.get("blocks.png", Texture.class);
        lineTexture= assetManager.get("lineBG.png",Texture.class);
        backgroundTexture = assetManager.get("shaderLayer.png", Texture.class);
        uiTexture = assetManager.get("ui/uiskin.png", Texture.class);

        fontTexture =assetManager.get("fonts/ImageFonts/FontsHDTwo.png", Texture.class);

        // Home Screen Textures
        playButtonTexture = assetManager.get("ui/PlayButtonUpdate.png", Texture.class);
        settingsButtonTexture = assetManager.get("ui/settingsIcon2.png", Texture.class);
        tapToContinueTexture = assetManager.get("ui/tapToContinue.png", Texture.class);
        snakeSkinButtonTexture=assetManager.get("ui/skinIcon.png", Texture.class);
        shareButtonTexture=assetManager.get("ui/shareAppIcon.png", Texture.class);
        addButtonTexture=assetManager.get("ui/addButton2.png", Texture.class);
        logoTexture=assetManager.get("ui/SnakeBlockLogo.png", Texture.class);
        barTexture=assetManager.get("ui/scoreBoard.png", Texture.class);
        coinTexture=assetManager.get("ui/snakeCoin.png",Texture.class);
        healthTexture=assetManager.get("ui/snakeHealthPortion.png", Texture.class);
        diamondTexture=assetManager.get("ui/snakeDiamond.png", Texture.class);
        navBarTexture = assetManager.get("ui/bottomNavBackground.png", Texture.class);
        levelsButtonTexture = assetManager.get("ui/LevelsButton.png", Texture.class);

        // Main Game Textures
        pauseBtnTexture=assetManager.get("ui/pauseBtn.png", Texture.class);
        reviveAdsTexture=assetManager.get("ui/watchAds.png", Texture.class);
        reviveHealthTexture=assetManager.get("ui/reviveWithHealth.png", Texture.class);
        homeButtonTexture=assetManager.get("ui/home_p_button.png", Texture.class);
        restartButtonTexture=assetManager.get("ui/restart_p_button.png", Texture.class);
        resumeButtonTexture=assetManager.get("ui/play_p_button.png", Texture.class);
        powerUpTexture=assetManager.get("powerUpTexture.png",Texture.class);

        pauseHeaderTexture=assetManager.get("ui/pauseDialogHeader.png", Texture.class);
        boxDialogTexture=assetManager.get("ui/Dialog.png", Texture.class);
        closeTexture=assetManager.get("ui/CancelButton.png", Texture.class);

        dialogHalfSize =assetManager.get("ui/DialogHalf.png", Texture.class);
        dialogQuarterSize = assetManager.get("ui/DialogQuarter.png", Texture.class);
        dialogQuarterSizeOpen =assetManager.get("ui/DialogQuarterOpen.png", Texture.class);
        noHeaderDialog= assetManager.get("ui/DialogNoHeader.png", Texture.class);
        keepGoingTexture =assetManager.get("ui/keep_going_title.png", Texture.class);
        settingHeader =assetManager.get("ui/settingsHeader.png", Texture.class);

        //Text Assets
        giveUpTexture =assetManager.get("ui/giveUpText.png", Texture.class);
        failTexture =assetManager.get("ui/failText.png", Texture.class);
        continueTexture =assetManager.get("ui/continueText.png", Texture.class);
        tosTexture =assetManager.get("ui/termsOfService.png", Texture.class);
        privacyPolicyTexture =assetManager.get("ui/privacyPolicy.png", Texture.class);
        licenseCreditsTexture =assetManager.get("ui/licenseAndCredits.png", Texture.class);


        soundTexture=assetManager.get("ui/sound_icon.png", Texture.class);
        musicTexture=assetManager.get("ui/music_icon.png", Texture.class);
        defaultButton= assetManager.get("ui/defaultButton.png", Texture.class);

        sliderBackgroundTexture=assetManager.get("ui/sliderBackground.png", Texture.class);
        sliderFillTexture=assetManager.get("ui/sliderFill.png", Texture.class);
        sliderKnobTexture= assetManager.get("ui/sliderKnob.png", Texture.class);

        //Game Over Textures
        replayButtonTexture=assetManager.get("ui/replay2.png", Texture.class);
        upgradeButtonTexture=assetManager.get("ui/upgradeBtnImg.png", Texture.class);
        home2ButtonTexture=assetManager.get("ui/newHomeBtn.png", Texture.class);

        //Menu Screen Textures
        menuBGTexture=assetManager.get("ui/menuBG.png", Texture.class);
        equippedBorderTexture = assetManager.get("ui/equippedBorderBg.png", Texture.class);
        cardBackgroundTexture = assetManager.get("ui/cardBackground.png", Texture.class);
        selectedButtonTexture = assetManager.get("ui/selectedButton.png", Texture.class);
        selectButtonTexture = assetManager.get("ui/selectButton.png", Texture.class);
        buyButtonTexture = assetManager.get("ui/buyButton.png", Texture.class);

        //Bottom Nav
        bottomNavBg=assetManager.get("ui/downBGBlue.png", Texture.class);
        upLayout =assetManager.get("ui/upLayout.png", Texture.class);
        defaultItemTexture=assetManager.get("ui/defaultItemsLook.png", Texture.class);
        selectOverlayTexture=assetManager.get("ui/selectIndicator.png", Texture.class);
        defaultOverlayTexture=assetManager.get("ui/defaultNavItem.png", Texture.class);

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
}
