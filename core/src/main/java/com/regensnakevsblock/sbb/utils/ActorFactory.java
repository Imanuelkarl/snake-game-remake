package com.regensnakevsblock.sbb.utils;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
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
    public static ProgressBar createProgressBar(
        Texture bgTexture,
        Texture fillTexture,
        Texture knobTexture,     // nullable
        Texture afterTexture,    // nullable
        float min,
        float max,
        float step,
        float width,
        float height,
        boolean vertical,
        float fillOffset
    ) {
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();

        // --- Background ---
        if (bgTexture != null) {
            TextureRegionDrawable background =
                new TextureRegionDrawable(new TextureRegion(bgTexture));
            background.setMinHeight(height);
            background.setMinWidth(width);
            style.background = background;
        }

        // --- Fill (progress) ---
        if (fillTexture != null) {
            TextureRegion fillRegion = new TextureRegion(fillTexture);
            TextureRegionDrawable originalFill = new TextureRegionDrawable(fillRegion);
            originalFill.setMinHeight(height - height / 7f);

            // Create a wrapper that shifts the fill to start at the offset
            style.knobBefore = new TextureRegionDrawable(fillRegion) {
                @Override
                public void draw(Batch batch, float x, float y, float width, float height) {
                    // Draw the fill shifted right by fillOffset, and reduce its width
                    originalFill.draw(batch, x + fillOffset, y+originalFill.getMinHeight()*2, width , originalFill.getMinHeight());
                }
            };
        }

        //

        // --- Remaining (optional) ---
        if (afterTexture != null) {
            TextureRegionDrawable after =
                new TextureRegionDrawable(new TextureRegion(afterTexture));

            after.setMinHeight(height - height / 6f);
            after.setMinWidth(0);

            style.knobAfter = after;
        }

        // --- Knob (handle) ---
        if (knobTexture != null) {
            TextureRegionDrawable knob =
                new TextureRegionDrawable(new TextureRegion(knobTexture));

            // Taller than bar for emphasis
            knob.setMinHeight(height * 1.5f);
            knob.setMinWidth(height); // square-ish knob

            style.knob = knob;
        }

        ProgressBar bar = new ProgressBar(min, max, step, vertical, style);

        // --- CRITICAL: Set actual actor size ---
        bar.setSize(width, height);

        return bar;
    }
    public static ProgressBar createProgressBar(Texture bgTexture, Texture fillTexture, float width, float height) {
        return createProgressBar(bgTexture, fillTexture, null, null, 0, 1, 0.01f, width,height,false,5);
    }
    public static ProgressBar createProgressBar(Texture bgTexture, Texture fillTexture,Texture knobTexture,float width, float height) {
        return createProgressBar(bgTexture, fillTexture, knobTexture, null, 0, 1, 0.01f,width,height, false,50);
    }
    private static Texture createDarkenedTexture(Texture original) {

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
                float factor = 1f - (float) 0.4;

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

        Texture darkTexture = createDarkenedTexture(texture);

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
        Texture darkTexture = createDarkenedTexture(newTexture);

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
