package com.regensnakevsblock.sbb.uifactory;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.elements.GameDialog;
import com.regensnakevsblock.sbb.utils.FontFactory;

public class HomeUiFactory {
    private float width;
    private float height;
    private float playButtonSize;
    private float actionButtonSize;
    private float logoWidth , logoHeight;
    private float smallBtnW;
    private float largeBtnW;
    private float mediumBtnH;
    private float largeBtnH;
    private float defaultSpacing;
    private float rejectSpacing;
    private float topRowY;
    private float bottomRowY;
    private float topRowStartX;
    private float bottomRowStartX;
    private float bottomRowRejectStartX;

    public HomeUiFactory(Stage stage) {

        resize(stage.getWidth(), stage.getHeight());
        playButtonSize=290f;
        actionButtonSize=120.5f;
        logoHeight =230f;
        logoWidth =602f;
        initButtonLayout();
    }

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;

    }


    // Button creation methods
    public ImageButton createSettingsButton() {
        System.out.println("The width here is "+width/6f);
        return createButton(
            Assets.getInstance().settingsButtonTexture,
            width / 3 - width / 4,
            height / 32,
            actionButtonSize,
            actionButtonSize
        );
    }

    public ImageButton createPlayButton() {

        return createButton(
            Assets.getInstance().playButtonTexture,
            width / 2 - playButtonSize/2,
            height *0.45f,
            playButtonSize,
            playButtonSize
        );
    }
    public Table createHeaderTable(){
        float headerHeight = 80f;
        Table header = new Table();

        header.setSize(width, headerHeight);
        header.setPosition(0, height - headerHeight);

        return header;
    }

    public ImageButton createSnakeSkinButton() {
        return createButton(
            Assets.getInstance().snakeSkinButtonTexture,
            2 * width / 3 - 2 * width / 8,
            height / 32,
            actionButtonSize,
            actionButtonSize
        );
    }

    public ImageButton createShareButton() {
        return createButton(
            Assets.getInstance().shareButtonTexture,
            3 * width / 3 - 3 * width / 12,
            height / 32,
            actionButtonSize,
            actionButtonSize
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
            (width-logoWidth)/2,
            height *0.75f,
            logoWidth,
            logoHeight
        );
    }
    public GameDialog createTermsAndConditionDialog(Stage stage){
        GameDialog dialog = new GameDialog(stage);
        dialog.setDialogSize(stage.getWidth(), stage.getHeight()/3);
        dialog.setTitleEnabled(false);
        dialog.setCloseButtonEnabled(false);
        dialog.setBackground(Assets.getInstance().dialogHalfSize);
        return dialog;
    }

    // Initialize all calculated values once
    private void initButtonLayout() {
        // Widths
        smallBtnW = width * 0.28f;
        largeBtnW = width* 0.4f;


        // Heights
        mediumBtnH = (height/3) * 0.07f;  // Privacy button
        largeBtnH = (height/3) * 0.22f;   // Accept & Reject buttons

        // Spacing
        defaultSpacing = width * 0.04f;
        rejectSpacing = width * 0.1f;

        // Y positions
        topRowY = height * 0.45f;      // Terms & Policy row
        bottomRowY = height * 0.35f;   // Accept & Reject row

        // X starting positions
        float topRowTotalWidth = smallBtnW * 2 + defaultSpacing;
        topRowStartX = (width - topRowTotalWidth) / 2f;


        bottomRowStartX = width*0.05f ;


        bottomRowRejectStartX = width*0.95f -largeBtnW;
    }

// Call initButtonLayout() once in your constructor or initialization method

    public ImageButton createTermsAndConditionButton() {
        return createButton(
            Assets.getInstance().tosTexture,
            topRowStartX, topRowY,
            smallBtnW, mediumBtnH
        );
    }

    public ImageButton createPrivacyPolicyButton() {
        return createButton(
            Assets.getInstance().privacyPolicyTexture,
            topRowStartX + smallBtnW + defaultSpacing, topRowY,
            smallBtnW, mediumBtnH
        );
    }

    public ImageButton createAcceptButton() {
        return createButton(
            Assets.getInstance().acceptTerms,
            bottomRowStartX, bottomRowY,
            largeBtnW, largeBtnH
        );
    }

    public ImageButton createRejectButton() {
        return createButton(
            Assets.getInstance().rejectTerms,
            bottomRowRejectStartX , bottomRowY,
            largeBtnW, largeBtnH
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
