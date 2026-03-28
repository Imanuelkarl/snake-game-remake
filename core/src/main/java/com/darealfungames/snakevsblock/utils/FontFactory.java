package com.darealfungames.snakevsblock.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontFactory {

    private static FreeTypeFontGenerator generator;

    public static BitmapFont getRoboto(int size, boolean medium) {
        if (generator == null) {
            generator = new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/" +
                    (medium ? "/Roboto/static/Roboto-Regular.ttf" : "/Smooch_Sans/static/SmoochSans-Thin.ttf")));
        }

        FreeTypeFontGenerator.FreeTypeFontParameter param =
            new FreeTypeFontGenerator.FreeTypeFontParameter();

        param.size = size;
        param.color = Color.WHITE;
        param.minFilter = Texture.TextureFilter.Linear;
        param.magFilter = Texture.TextureFilter.Linear;

        return generator.generateFont(param);
    }

    public static void dispose() {
        if (generator != null) generator.dispose();
    }
}

