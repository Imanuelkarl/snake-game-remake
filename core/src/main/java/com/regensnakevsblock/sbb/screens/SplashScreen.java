package com.regensnakevsblock.sbb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.MyGame;

public class SplashScreen implements Screen {

    private enum State {
        LOGO1_IN,
        TEXT_IN,
        LOGO2_IN,
        HOLD,
        FADE_OUT,
        DONE
    }

    private  float WORLD_HEIGHT;
    private  float WORLD_WIDTH;

    private OrthographicCamera camera;
    private SpriteBatch batch;

    private Texture logoMain;
    private Texture logoPartner;
    private Texture collaborationText;

    private Sprite spriteMain;
    private Sprite spritePartner;
    private Sprite spriteText;
    private final float delayBetweenSteps = 0.4f;

    private State state = State.LOGO1_IN;

    private float timer = 0f;

    // timing (tweak for feel)
    private final float fadeDuration = 0.8f;
    private final float holdDuration = 1.2f;
    private final MyGame game;

    public SplashScreen(MyGame game) {
        this.game =game;
        configureSize();
    }
    private void configureSize(){
        this.WORLD_WIDTH = Constants.SCREEN_SIZE*((float) Gdx.graphics.getWidth() /Gdx.graphics.getHeight());;
        this.WORLD_HEIGHT = Constants.SCREEN_SIZE;
    }

    @Override
    public void show() {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, WORLD_WIDTH, WORLD_HEIGHT);

        batch = new SpriteBatch();

        // Load textures (replace with your asset manager if needed)
        logoMain = new Texture("logo_main.png");
        logoPartner = new Texture("logo_partner.png");
        collaborationText = new Texture("collaboration_text.png");

        spriteMain = new Sprite(logoMain);
        spritePartner = new Sprite(logoPartner);
        spriteText = new Sprite(collaborationText);

        centerSprite(spriteMain, WORLD_HEIGHT * 0.65f, 0.55f);      // BIGGER MAIN LOGO
        centerSprite(spriteText, WORLD_HEIGHT * 0.50f, 0.35f);      // SMALL TEXT
        centerSprite(spritePartner, WORLD_HEIGHT * 0.35f, 0.45f);   // MEDIUM PARTNER

        setAlpha(spriteMain, 0f);
        setAlpha(spriteText, 0f);
        setAlpha(spritePartner, 0f);
    }

    private void centerSprite(Sprite sprite, float y, float scaleMultiplier) {

        float baseScale = Math.min(
            WORLD_WIDTH / sprite.getWidth(),
            WORLD_HEIGHT / sprite.getHeight()
        );

        float scale = baseScale * scaleMultiplier;

        sprite.setSize(sprite.getWidth() * scale, sprite.getHeight() * scale);

        sprite.setPosition(
            (WORLD_WIDTH - sprite.getWidth()) / 2f,
            y - sprite.getHeight() / 2f
        );
    }

    private void setAlpha(Sprite sprite, float a) {
        sprite.setColor(1f, 1f, 1f, a);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        spriteMain.draw(batch);
        spriteText.draw(batch);
        spritePartner.draw(batch);
        batch.end();
    }

    private void update(float delta) {
        timer += delta;

        switch (state) {

            case LOGO1_IN:
                fadeIn(spriteMain, delta);
                if (spriteMain.getColor().a >= 1f) {
                    state = State.TEXT_IN;
                    timer = 0;
                }
                break;

            case TEXT_IN:
                fadeIn(spriteText, delta);
                if (spriteText.getColor().a >= 1f) {
                    state = State.LOGO2_IN;
                    timer = 0;
                }
                break;

            case LOGO2_IN:
                fadeIn(spritePartner, delta);
                if (spritePartner.getColor().a >= 1f) {
                    state = State.HOLD;
                    timer = 0;
                }
                break;

            case HOLD:
                if (timer >= holdDuration) {
                    state = State.FADE_OUT;
                }
                break;

            case FADE_OUT:
                fadeOutAll(delta);

                if (spriteMain.getColor().a <= 0f &&
                    spriteText.getColor().a <= 0f &&
                    spritePartner.getColor().a <= 0f) {

                    state = State.DONE;
                    goToLoadingScreen();
                }
                break;

            case DONE:
                break;
        }
    }

    private void fadeIn(Sprite sprite, float delta) {
        float a = sprite.getColor().a;
        a += delta / fadeDuration;
        a = MathUtils.clamp(a, 0f, 1f);
        setAlpha(sprite, a);
    }

    private void fadeOutAll(float delta) {
        fadeOut(spriteMain, delta);
        fadeOut(spriteText, delta);
        fadeOut(spritePartner, delta);
    }

    private void fadeOut(Sprite sprite, float delta) {
        float a = sprite.getColor().a;
        a -= delta / fadeDuration;
        a = MathUtils.clamp(a, 0f, 1f);
        setAlpha(sprite, a);
    }

    private void goToLoadingScreen() {
        // Replace with your actual screen manager
        // Example:
        // ((Game) Gdx.app.getApplicationListener()).setScreen(new LoadingScreen());
        game.setScreen(new LoadingScreen(game));
        System.out.println("Splash done -> switch to LoadingScreen");
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        logoMain.dispose();
        logoPartner.dispose();
        collaborationText.dispose();
    }
}
