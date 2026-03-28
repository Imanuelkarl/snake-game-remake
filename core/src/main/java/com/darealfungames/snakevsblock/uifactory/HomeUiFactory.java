package com.darealfungames.snakevsblock.uifactory;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.utils.FontFactory;

public class HomeUiFactory {
    private float width;
    private float height;

    // Font size will be calculated dynamically
    private float fontSize;
    private final GlyphLayout glyphLayout;

    // UI component positions (will be calculated in resize)
    private float leftBarX, leftBarY, leftBarWidth;
    private float centerBarX, centerBarY, centerBarWidth;
    private float rightBarX, rightBarY, rightBarWidth;
    private float leftIconX, leftIconY;
    private float centerIconX, centerIconY;
    private float rightIconX, rightIconY;
    private float leftAddX, leftAddY;
    private float centerAddX, centerAddY;
    private float rightAddX, rightAddY;
    private float leftLabelX, leftLabelY;
    private float centerLabelX, centerLabelY;
    private float rightLabelX, rightLabelY;

    public HomeUiFactory(Stage stage) {
        glyphLayout = new GlyphLayout();
        resize(stage.getWidth(), stage.getHeight());
    }

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;

        // Calculate font size based on bar height (so text fits nicely in bars)
        float barHeight = height / 48;
        this.fontSize = barHeight * 0.85f; // Font is 60% of bar height

        calculatePositions();
    }

    private void calculatePositions() {
        // Bar positions - EXACTLY matching original calculations
        leftBarX = width / 32;
        leftBarY = height - height / 64 - height / 48;
        leftBarWidth = width / 3.5f;

        centerBarX = width / 2 - width / 7;
        centerBarY = leftBarY;
        centerBarWidth = leftBarWidth;

        rightBarX = width - width / 32 - leftBarWidth;
        rightBarY = leftBarY;
        rightBarWidth = leftBarWidth;

        // Icon positions - EXACTLY matching original calculations
        float iconSize = height / 48;
        float iconY = height - height / 80 - height / 40;

        // Left icon (health) - original: width/32+width/3.5f-height/64
        leftIconX = leftBarX + leftBarWidth - height / 64;
        leftIconY = iconY;

        // Center icon (coin) - original: width/2+width/7-height/64
        centerIconX = centerBarX + centerBarWidth - height / 64;
        centerIconY = iconY;

        // Right icon (diamond) - original: width-width/32-height/64
        rightIconX = rightBarX + rightBarWidth - height / 64;
        rightIconY = iconY;

        // Add button positions - EXACTLY matching original calculations
        float addButtonSize = height / 48;
        float addY = height - height / 80 - addButtonSize;

        leftAddX = width / 32;
        leftAddY = addY;

        centerAddX = width / 2 - width / 7;
        centerAddY = addY;

        rightAddX = width - rightBarWidth - addButtonSize;
        rightAddY = addY;

        // Label positions - adjusted for centering within bars
        float barHeight = height / 48;

        leftLabelX = leftBarX + leftBarWidth / 2;
        leftLabelY = leftBarY + barHeight / 2;

        centerLabelX = centerBarX + centerBarWidth / 2;
        centerLabelY = centerBarY + barHeight / 2;

        rightLabelX = rightBarX + rightBarWidth / 2;
        rightLabelY = rightBarY + barHeight / 2;
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

    public ImageButton createAddCoinButton() {
        return createAddButton(centerAddX, centerAddY);
    }

    public ImageButton createAddDiamondButton() {
        return createAddButton(rightAddX, rightAddY);
    }

    public ImageButton createAddHealthButton() {
        return createAddButton(leftAddX, leftAddY);
    }

    private ImageButton createAddButton(float x, float y) {
        return createButton(
            Assets.getInstance().addButtonTexture,
            x,
            y,
            height / 48,
            height / 48
        );
    }
    private Texture createDarkenedTexture(Texture original, float overlayAlpha) {

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

    private ImageButton createButton(Texture texture, float x, float y, float width, float height) {

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
    public Image createBottomNavBar() {
        return createUiImage(Assets.getInstance().navBarTexture, -width/8, 0, width+width/4, height / 10);
    }

    public Image createCoinBar() {
        return createUiImage(Assets.getInstance().barTexture, centerBarX, centerBarY, centerBarWidth, height / 48);
    }

    public Image createHealthBar() {
        return createUiImage(Assets.getInstance().barTexture, leftBarX, leftBarY, leftBarWidth, height / 48);
    }

    public Image createDiamondBar() {
        return createUiImage(Assets.getInstance().barTexture, rightBarX, rightBarY, rightBarWidth, height / 48);
    }

    public Image createCoinImage() {
        return createUiImage(Assets.getInstance().coinTexture, centerIconX, centerIconY, height / 48, height / 48);
    }

    public Image createHealthImage() {
        return createUiImage(Assets.getInstance().healthTexture, leftIconX, leftIconY, height / 48, height / 48);
    }

    public Image createDiamondImage() {
        return createUiImage(Assets.getInstance().diamondTexture, rightIconX, rightIconY, height / 60, height / 48);
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

    public Label createCoinLabel() {
        return createCenteredLabel(centerLabelX, centerLabelY);
    }

    public Label createDiamondLabel() {
        return createCenteredLabel(rightLabelX, rightLabelY);
    }

    public Label createHealthLabel() {
        return createCenteredLabel(leftLabelX, leftLabelY);
    }

    private Label createCenteredLabel(float centerX, float centerY) {
        BitmapFont font = FontFactory.getRoboto((int) fontSize, true);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        Label label = new Label("", style);

        // Use GlyphLayout to measure text dimensions
        glyphLayout.setText(font, "0");
        float textWidth = glyphLayout.width;
        float textHeight = glyphLayout.height;

        // Center the label perfectly
        label.setPosition(centerX - textWidth / 2, centerY - textHeight / 8);

        return label;
    }
}
