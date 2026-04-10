package com.regensnakevsblock.sbb.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class ActorFactory {

    public static Label createTextLabel(int fontSize, float x, float y) {
        return createTextLabel("",fontSize,x,y);
    }
    public static Label createTextLabel(String text,int fontSize, float x, float y) {

        Label.LabelStyle style = new Label.LabelStyle();

        return createTextLabel(text,fontSize,x,y,style);
    }
    public static Label createTextLabel(String text, int fontSize, float x, float y, Label.LabelStyle style) {

        style.font = FontFactory.getRoboto(fontSize, true);

        Label label = new Label(text, style);
        label.setPosition(x, y);
        return label;
    }
    public static Label createTextLabel( float x, float y) {
        return createTextLabel(24,x,y);
    }
    public static Label createTextLabel( String text, Label.LabelStyle style, float x, float y) {
        return createTextLabel(text,24,x,y,style);
    }
    public static Label createTextLabel(String s, Label.LabelStyle style) {
        return createTextLabel(s,24,style);
    }
    public static Label createTextLabel(String s,int fontSize, Label.LabelStyle style) {
        return createTextLabel(s,fontSize,0,0,style);
    }
    private static Texture createDarkenedTexture(Texture original, float overlayAlpha) {

        if (!original.getTextureData().isPrepared()) {
            original.getTextureData().prepare();
        }

        Pixmap originalPixmap = original.getTextureData().consumePixmap();

        Pixmap newPixmap = new Pixmap(
            originalPixmap.getWidth(),
            originalPixmap.getHeight(),
            originalPixmap.getFormat()
        );

        int width = originalPixmap.getWidth();
        int height = originalPixmap.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {

                int pixel = originalPixmap.getPixel(x, y);

                int r = (pixel >> 24) & 0xff;
                int g = (pixel >> 16) & 0xff;
                int b = (pixel >> 8) & 0xff;
                int a = pixel & 0xff;

                if (a == 0) {
                    // Fully transparent → keep as is
                    newPixmap.drawPixel(x, y, pixel);
                    continue;
                }

                // Apply darkening (blend toward black)
                float factor = 1f - overlayAlpha;

                int newR = (int)(r * factor);
                int newG = (int)(g * factor);
                int newB = (int)(b * factor);

                int newPixel =
                    (newR << 24) |
                        (newG << 16) |
                        (newB << 8) |
                        a;

                newPixmap.drawPixel(x, y, newPixel);
            }
        }

        Texture result = new Texture(newPixmap);
        newPixmap.dispose();

        return result;
    }

    public static ImageButton createButton(Texture texture, float x, float y, float width, float height) {

        Texture darkTexture = createDarkenedTexture(texture, 0.4f);

        Drawable up = new TextureRegionDrawable(new TextureRegion(texture));
        Drawable down = new TextureRegionDrawable(new TextureRegion(darkTexture));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = up;
        style.down = down;

        ImageButton button = new ImageButton(style);
        button.setPosition(x, y);
        button.setSize(width, height);

        return button;
    }
    public static void updateButtonStyle(ImageButton button, Texture newTexture) {
        Texture darkTexture = createDarkenedTexture(newTexture, 0.4f);

        Drawable up = new TextureRegionDrawable(new TextureRegion(newTexture));
        Drawable down = new TextureRegionDrawable(new TextureRegion(darkTexture));

        ImageButton.ImageButtonStyle style = button.getStyle();
        style.up = up;
        style.down = down;

        button.setStyle(style);
    }

    /*public static ImageButton createButton(Texture texture, float x, float y, float width, float height) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(texture));

        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle();
        style.up = drawable;
        style.down = drawable;

        ImageButton button = new ImageButton(style);
        button.setPosition(x, y);
        button.setSize(width, height);

        return button;
    }*/
    public static Image createUiImage(Texture texture, float x, float y, float width, float height) {
        Image image = new Image(texture);
        image.setPosition(x, y);
        image.setSize(width, height);
        return image;
    }

    public static Label createTextLabel(String s) {
        return createTextLabel(s,24,0,0);
    }
}
