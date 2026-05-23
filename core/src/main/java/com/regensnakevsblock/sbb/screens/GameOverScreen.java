package com.regensnakevsblock.sbb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.ui.GameOverUi;
import com.regensnakevsblock.sbb.uiactions.GameOverListener;

public class GameOverScreen implements Screen, GameOverListener {
    private MyGame game;

    private GameOverUi gameOverUi;

    public GameOverScreen(MyGame game) {
        this.game = game;
        gameOverUi = new GameOverUi(this);
        Gdx.input.setInputProcessor(gameOverUi.getStage());
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.00f, 0.0f, 0.08f, 0.5f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        gameOverUi.update(delta);
        gameOverUi.render();

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

    @Override
    public void restartButtonClicked() {
        game.setScreen(new MainGame(game));
    }

    @Override
    public void homeButtonClicked() {
        game.setScreen(new HomeScreen(game));
    }

    @Override
    public void shopButtonClicked() {
        game.setScreen(new MenuScreen(game));
    }

    @Override
    public void multiplyButtonClicked() {

    }
    public void setCoins(int coins){
        gameOverUi.setCoins(coins);
    }
    public void setScore(int score){
        gameOverUi.setScore(score);
    }
}
