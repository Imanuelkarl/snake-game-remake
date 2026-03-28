package com.darealfungames.snakevsblock.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darealfungames.snakevsblock.MyGame;
import com.darealfungames.snakevsblock.assets.Assets;

public class LoadingScreen extends InputAdapter implements Screen {

    private final MyGame game;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private BitmapFont font;
    private Texture loadingTexture;
    private float progress;

    public LoadingScreen(MyGame game) {
        this.game = game;
        this.batch = game.getBatch();
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        font = new BitmapFont();

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


        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        font.draw(batch, "Loading... " + (int)(progress * 100) + "%", 350, 240);

        batch.end();

        // When loading is complete, switch to menu screen
        if (Assets.getInstance().isLoaded()) {
            Assets.getInstance().assignAssets(); // REQUIRED
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
