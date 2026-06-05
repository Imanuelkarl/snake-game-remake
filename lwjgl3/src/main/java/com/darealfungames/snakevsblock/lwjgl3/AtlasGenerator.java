package com.darealfungames.snakevsblock.lwjgl3;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class AtlasGenerator {
    public static void main(String[] args) {
        TexturePacker.Settings settings = new TexturePacker.Settings();

        settings.maxWidth = 1024;
        settings.maxHeight = 1024;
        settings.filterMin = Texture.TextureFilter.Linear;
        settings.filterMag = Texture.TextureFilter.Linear;
        settings.duplicatePadding = true;

        TexturePacker.process(
            settings,
            "assets/raw/home",      // input
            "assets/ui",             // output
            "home"                  // atlas name
        );
    }
}
