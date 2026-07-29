package com.regensnakevsblock.sbb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.assets.Assets2;

public class NewHighScore extends InputAdapter implements Screen {

    private final MyGame game;
    private final ParticleEffect confettiCanon;
    private final SpriteBatch batch;


    public NewHighScore(MyGame game) {
        this.game = game;
        confettiCanon = Assets2.getInstance().confettiCanon;
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this);
        confettiCanon.start();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        batch.begin();
        Assets2.getInstance().font.draw(batch, "New High Score!", 100, 150);
        confettiCanon.setPosition((float) Gdx.graphics.getWidth() / 2, (float) Gdx.graphics.getHeight() / 2);
        confettiCanon.draw(batch, delta);
        batch.end();

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

    }

}
