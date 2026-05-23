package com.regensnakevsblock.sbb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.utils.SimpleBitmapFont;

public class LoadingScreen extends InputAdapter implements Screen {

    private final MyGame game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private SimpleBitmapFont font;
    private Texture background;
    private Texture logo;
    private float progress;
    private Texture barBg;
    private Texture barFill;
    private Texture knob;
    float screenWidth = 800;
    float screenHeight = 480;

    public LoadingScreen(MyGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        font = new SimpleBitmapFont(
            Gdx.files.internal("fonts/ImageFonts/FontsHDTwo.fnt"),
            Gdx.files.internal("fonts/ImageFonts/FontsHDTwo.png")
        );
        logo = new Texture("ui/SnakeBlockLogo.png");
        background = new Texture("game_bg_light.png");
        barBg = new Texture("ui/progressBarBg.png");
        barFill = new Texture("ui/progressBarFill.png");
        knob = new Texture("ui/progressBarKnob.png");

        // Start asset loading
        Assets.getInstance().loadAllAssets();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update asset loading progress
        progress = Assets.getInstance().getProgress();
        batch.setProjectionMatrix(camera.combined);
        camera.update();


        float logoWidth = 600;
        float logoHeight = 140;

        float logoX = (screenWidth - logoWidth) / 2f;
        float logoY = 450; // padding from top
        float barWidth = 520;     // bigger (was 400)
        float barHeight = 28;     // thicker (was 20)

// Centered


        float barX = (screenWidth - barWidth) / 2f;
        float barY = -50; // ← pushed downward (key change)

        float fillWidth = barWidth * progress;

        float knobSize = barHeight * 1.6f; // proportional scaling

// position at end of fill
        float knobX = barX + fillWidth - knobSize / 2f;
        float knobY = barY + (barHeight - knobSize) / 2f;

// Clamp so it doesn't go outside
        knobX = Math.max(barX - knobSize / 2f, knobX);
        knobX = Math.min(barX + barWidth - knobSize / 2f, knobX);
        float textY = barY + barHeight + 15;


        batch.begin();
        batch.draw(background, 0, -camera.viewportHeight/2, camera.viewportWidth, camera.viewportHeight*2);
        // Draw background
        batch.draw(barBg, barX, barY, barWidth, barHeight);

// Draw fill (based on progress)

        batch.draw(barFill, barX, barY, fillWidth, barHeight);

        batch.draw(knob, knobX, knobY, knobSize, knobSize);

        // ===== LOGO =====
        batch.draw(logo, logoX, logoY, logoWidth, logoHeight);

// ===== LOADING TEXT =====
        font.setScale(0.3f,0.15f);
        font.draw(batch, "LOADING... " + (int)(progress * 100) + "%",
            screenWidth / 2f - 150, textY);

        batch.end();

        // When loading is complete, switch to menu screen
        if (Assets.getInstance().isLoaded()) {
            Assets.getInstance().assignAssets(); // REQUIRED
            GameInstance.getInstance().loadSkinData();
            GameInstance.getInstance().load();
        }
        if(Assets.getInstance().isLoaded()&&GameInstance.getInstance().isSkinDataLoaded()){
            game.setScreen(new HomeScreen(game));
        }


    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = 800;
        camera.viewportHeight = 480 * ((float)height / width);
        camera.update();
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        font.dispose();
    }

}
