package com.regensnakevsblock.sbb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.regensnakevsblock.sbb.MyGame;

public class CrossFadeTransition implements Screen {
    private final MyGame game;
    private final Screen nextScreen;
    private final Screen currentScreen;
    private final float duration;
    private float timer = 0;
    private final SpriteBatch batch;
    private final FrameBuffer currentFbo;
    private final FrameBuffer nextFbo;

    public CrossFadeTransition(MyGame game, Screen nextScreen, float duration) {
        this.game = game;
        this.currentScreen = game.getScreen();
        this.nextScreen = nextScreen;
        this.duration = duration;
        this.batch = new SpriteBatch();

        // Create framebuffers
        currentFbo = new FrameBuffer(Pixmap.Format.RGBA8888,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        nextFbo = new FrameBuffer(Pixmap.Format.RGBA8888,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);

        // Render current screen to framebuffer
        currentFbo.begin();
        currentScreen.render(0);
        currentFbo.end();

        // Initialize next screen
        nextScreen.show();
        nextScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Render next screen to framebuffer
        nextFbo.begin();
        nextScreen.render(0);
        nextFbo.end();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        timer += delta;
        float alpha = Math.min(1f, timer / duration);

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Draw current screen with decreasing alpha
        batch.setColor(1, 1, 1, 1 - alpha);
        batch.draw(currentFbo.getColorBufferTexture(), 0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
            0, 0, currentFbo.getColorBufferTexture().getWidth(),
            currentFbo.getColorBufferTexture().getHeight(), false, true);

        // Draw next screen with increasing alpha
        batch.setColor(1, 1, 1, alpha);
        batch.draw(nextFbo.getColorBufferTexture(), 0, 0,
            Gdx.graphics.getWidth(), Gdx.graphics.getHeight(),
            0, 0, nextFbo.getColorBufferTexture().getWidth(),
            nextFbo.getColorBufferTexture().getHeight(), false, true);
        batch.end();

        if (timer >= duration) {
            game.setScreen(nextScreen);
            currentScreen.dispose();
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {

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
        batch.dispose();
        currentFbo.dispose();
        nextFbo.dispose();
    }

    // Other required methods...
}
