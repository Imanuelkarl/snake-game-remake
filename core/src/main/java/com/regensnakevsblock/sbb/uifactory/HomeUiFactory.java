package com.regensnakevsblock.sbb.uifactory;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.utils.FontFactory;

public class HomeUiFactory {
    private float width;
    private float height;

    // Font size will be calculated dynamically



    public HomeUiFactory(Stage stage) {

        resize(stage.getWidth(), stage.getHeight());
    }

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;

    }


    // Button creation methods
    public ImageButton createSettingsButton() {
        return createButton(
            Assets.getInstance().settingsButtonTexture,
            width / 3 - width / 4,
            height / 32,
            width / 6,
            width / 6
        );
    }

    public ImageButton createPlayButton() {
        return createButton(
            Assets.getInstance().playButtonTexture,
            width / 2 - width / 5f,
            height *0.45f,
            width / 2.5f,
            width / 2.5f
        );
    }

    public ImageButton createSnakeSkinButton() {
        return createButton(
            Assets.getInstance().snakeSkinButtonTexture,
            2 * width / 3 - 2 * width / 8,
            height / 32,
            width / 6,
            width / 6
        );
    }

    public ImageButton createShareButton() {
        return createButton(
            Assets.getInstance().shareButtonTexture,
            3 * width / 3 - 3 * width / 12,
            height / 32,
            width / 6,
            width / 6
        );
    }
    private Texture createDarkenedTexture(Texture original) {

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

    private ImageButton createButton(Texture texture, float x, float y, float width, float height) {

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

    // Image creation methods
    public Image createLogoImage() {
        return createUiImage(
            Assets.getInstance().logoTexture,
            width / 12,
            height *0.75f,
            width -width/6,
            height / 7
        );
    }

    private Image createUiImage(Texture texture, float x, float y, float width, float height) {
        Image image = new Image(texture);
        image.setPosition(x, y);
        image.setSize(width, height);
        return image;
    }

    // Label creation methods
    public Label createTextLabel(int fontSize, float x, float y) {
        BitmapFont font = FontFactory.getRoboto(fontSize, true);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        Label label = new Label("", style);
        label.setPosition(x, y);
        return label;
    }
    public ImageButton createLevelsButton() {

        return createButton(Assets.getInstance().levelsButtonTexture, width/2 -width/4, height/4, width/2, height/12);
    }

}
