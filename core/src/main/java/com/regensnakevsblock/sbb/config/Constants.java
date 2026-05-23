package com.regensnakevsblock.sbb.config;

public class Constants {
    // Screen dimensions
    public static final float VIEWPORT_WIDTH = 800;
    public static final float VIEWPORT_HEIGHT = 480;

    // Game settings
    public static final float SNAKE_SPEED = 200;
    public static final float SNAKE_RADIUS = 20;
    public static final float BLOCK_SIZE = 40;
    public static final int INITIAL_SNAKE_LENGTH = 3;
    public static final int ROW_SPACING = 100;

    // Game states
    public static final int STATE_MENU = 0;
    public static final int STATE_PLAYING = 1;
    public static final int STATE_PAUSED = 2;
    public static final int STATE_GAME_OVER = 3;

    // Power-up types
    public static final int POWERUP_MAGNET = 0;
    public static final int POWERUP_HAMMER = 1;
    public static final int POWERUP_MULTIPLIER = 2;
    public static final int POWERUP_FREEZE = 3;

    // Difficulty levels
    public static final int DIFFICULTY_EASY = 0;
    public static final int DIFFICULTY_NORMAL = 1;
    public static final int DIFFICULTY_HARD = 2;
    public static final int SCREEN_SIZE = 1612;
    public static final int SCREEN_WIDTH = 700;
    public static final String TERMS_OF_SERVICE_URL = "https://snakeblockbreaker.vercel.app/terms";
    public static final String PRIVACY_POLICY_URL = "https://snakeblockbreaker.vercel.app/privacy";

    public static final String googleBannerAdId = "ca-app-pub-3940256099942544/6300978111";
    public static final String googleInterstitialAdId = "ca-app-pub-3940256099942544/1033173712";
    public static final String googleRewardedAdId = "ca-app-pub-3940256099942544/5224354917";
    public static final String googleAdAppId = "";
    public static final String unityBannerAdId = "";
    public static final String unityInterstitialAdId = "";
    public static final String unityRewardedAdId = "";
    public static float[][] Snake_Texture_Colors={{1f,0f,0f},{1f,1f,0.6f},{0f,1f,0f},{0f,1f,1f},{0f,0f,1f},{1f,0.7f,1f},{1f,0.1f,0.6f},{1f,1f,1f},{0.3f,0.3f,0.3f},{0.8f,0.6f,0.02f},{1f,0.8f,0.4f},{1f,0.8f,0.4f}};

    public static void initialize() {
        // Initialize any constant values
    }
}
