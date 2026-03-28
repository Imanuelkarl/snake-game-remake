package com.darealfungames.snakevsblock.config;

import com.badlogic.gdx.Gdx;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.utils.SimpleBitmapFont;

public class GameInstance {

    private static GameInstance instance;

    private SimpleBitmapFont simpleBitmapFont;

    // Private constructor prevents direct instantiation
    private GameInstance() {}

    public static GameInstance getInstance() {
        if (instance == null) {
            instance = new GameInstance();
        }
        return instance;
    }

    // Lazy initialization (VERY IMPORTANT)
    public SimpleBitmapFont getSimpleBitmapFont() {
        if (simpleBitmapFont == null) {
            simpleBitmapFont = new SimpleBitmapFont(
                Gdx.files.internal("fonts/ImageFonts/FontsHDTwo.fnt"),
                Assets.getInstance().fontTexture
            );

            simpleBitmapFont.setDebugMode(false);
        }
        return simpleBitmapFont;
    }

    // Optional manual override (if you want to inject)
    public void setSimpleBitmapFont(SimpleBitmapFont font) {
        this.simpleBitmapFont = font;
    }

    // Cleanup when game exits
    public void dispose() {
        if (simpleBitmapFont != null) {
            simpleBitmapFont.dispose();
        }
    }
}
