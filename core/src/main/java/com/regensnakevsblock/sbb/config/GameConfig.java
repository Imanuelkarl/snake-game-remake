package com.regensnakevsblock.sbb.config;

public class GameConfig {
    // Game configuration that can be changed
    public static boolean SOUND_ENABLED = true;
    public static boolean MUSIC_ENABLED = true;
    public static int DIFFICULTY_LEVEL = Constants.DIFFICULTY_NORMAL;
    public static float GAME_SPEED = 1.0f;
    public static int INITIAL_LIVES = 3;

    public static void loadConfig() {
        // Load configuration from save file
    }

    public static void saveConfig() {
        // Save configuration to file
    }
}
