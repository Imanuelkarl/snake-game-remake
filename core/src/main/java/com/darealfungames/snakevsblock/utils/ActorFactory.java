package com.darealfungames.snakevsblock.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ActorFactory {

    public static Label createTextLabel(int fontSize, float x, float y) {
        BitmapFont font = FontFactory.getRoboto(fontSize, true);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        Label label = new Label("", style);
        label.setPosition(x, y);
        return label;
    }
    public static ImageButton createButton(Texture texture, float x, float y, float width, float height) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = drawable;
        style.down = drawable;

        ImageButton button = new ImageButton(style);
        button.setPosition(x, y);
        button.setSize(width, height);

        return button;
    }
    public static Image createUiImage(Texture texture, float x, float y, float width, float height) {
        Image image = new Image(texture);
        image.setPosition(x, y);
        image.setSize(width, height);
        return image;
    }

}
