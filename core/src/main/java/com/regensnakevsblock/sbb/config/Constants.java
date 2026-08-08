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
    public static final String unityGameId= "6156095";
    public static final String unityBannerAdId = "";
    public static final String unityInterstitialAdId = "";
    public static String[] POWER_UPS = {"magnet","hammer","multiplier","freeze"};
    public static final String unityRewardedAdId = "";
    //public static float[][] Snake_Texture_Colors={{1f,0f,0f},{1f,1f,0.6f},{0f,1f,0f},{0f,1f,1f},{0f,0f,1f},{1f,0.7f,1f},{1f,0.1f,0.6f},{1f,1f,1f},{0.3f,0.3f,0.3f},{0.8f,0.6f,0.02f},{1f,0.8f,0.4f},{1f,0.8f,0.4f}};
    public static float[][] Snake_Texture_Colors = {
        {1.000f, 0.200f, 0.200f}, // 1. Red ball
        {0.120f, 0.390f, 0.650f}, // 2. Blue ball
        {0.630f, 0.570f, 0.200f}, // 3. Yellow ball
        {0.160f, 0.540f, 0.160f}, // 4. Green ball
        {0.660f, 0.360f, 0.180f}, // 5. Orange ball
        {0.450f, 0.230f, 0.640f}, // 6. Purple ball

        {0.660f, 0.330f, 0.550f}, // 7. Pink ball
        {0.170f, 0.560f, 0.540f}, // 8. Cyan ball
        {0.350f, 0.240f, 0.170f}, // 9. Brown ball
        {0.370f, 0.360f, 0.370f}, // 10. Gray ball
        {0.630f, 0.630f, 0.630f}, // 11. White ball
        {0.150f, 0.150f, 0.150f}, // 12. Black ball

        {0.400f, 0.310f, 0.430f}, // 13. Red/Blue stripes
        {0.430f, 0.560f, 0.200f}, // 14. Green/Yellow checkered
        {0.560f, 0.270f, 0.620f}, // 15. Purple swirl
        {0.680f, 0.420f, 0.290f}, // 16. Orange polka
        {0.310f, 0.470f, 0.620f}, // 17. Blue stripes
        {0.410f, 0.470f, 0.390f}, // 18. Rainbow

        {0.540f, 0.560f, 0.550f}, // 19. Multi-color globe
        {0.440f, 0.440f, 0.440f}, // 20. Zebra
        {0.270f, 0.280f, 0.430f}, // 21. Blue with stars
        {0.650f, 0.410f, 0.430f}, // 22. Red plaid
        {0.290f, 0.410f, 0.200f}, // 23. Camouflage
        {0.560f, 0.310f, 0.620f}, // 24. Purple swirl 2

        {0.540f, 0.170f, 0.170f}, // 25. Red truck
        {0.360f, 0.310f, 0.160f}, // 26. Green snake
        {0.170f, 0.400f, 0.290f}, // 27. Blue train
        {0.390f, 0.530f, 0.390f}, // 28. Yellow snake
        {0.140f, 0.340f, 0.170f}, // 29. Black train
        {0.400f, 0.240f, 0.460f}, // 30. Purple snake

        {0.340f, 0.250f, 0.190f}, // 31. Brown rail/bridge
        {0.140f, 0.410f, 0.340f}, // 32. Blue snake
        {0.550f, 0.500f, 0.250f}, // 33. Yellow megaphone
        {0.200f, 0.510f, 0.440f}, // 34. Cyan snake
        {0.630f, 0.520f, 0.450f}, // 35. Orange X
        {0.280f, 0.400f, 0.200f}, // 36. Green scales

        {0.560f, 0.150f, 0.150f}, // 37. Red apple
        {0.610f, 0.290f, 0.170f}, // 38. Banana
        {0.110f, 0.450f, 0.180f}, // 39. Green leaf
        {0.150f, 0.370f, 0.580f}, // 40. Blue fish
        {0.600f, 0.350f, 0.140f}, // 41. Orange carrot
        {0.350f, 0.210f, 0.500f}, // 42. Purple grapes

        {0.580f, 0.160f, 0.160f}, // 43. Red heart
        {0.580f, 0.500f, 0.200f}, // 44. Smiley
        {0.560f, 0.520f, 0.290f}, // 45. Nerd face
        {0.260f, 0.530f, 0.600f}, // 46. Blue diamond
        {0.580f, 0.460f, 0.210f}, // 47. Grinning sun
        {0.460f, 0.320f, 0.500f}  // 48. Moon
    };
    public static String[] tutorialInstructions ={
        "Swipe Left to avoid blocks",
        "Swipe Right to avoid Blocks",
        "Swipe into win item to collect length",
        "Move to the center to Destroy small blocks",
        "Win PowerUp"
    };
    public static String[] successMessage = {
        "Great Job",
        "You are a natural!!",
        "Nice moves",
        "You're Killing it",
        "Almost there"
    };

    public static int calculatePowerUpTime(int level){
        return 3*level*1000;
    }

    public static void initialize() {
        // Initialize any constant values
    }

    public static String getUpgradeItem(int upgradeLevel) {
        return upgradeLevel<8?"coins":"diamonds";
    }

    public static float calculatePowerUpPrice(int upgradeLevel) {
        return upgradeLevel<8?100*upgradeLevel+2400:upgradeLevel*10;
    }
}
