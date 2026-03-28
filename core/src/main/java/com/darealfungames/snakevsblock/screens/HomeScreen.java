package com.darealfungames.snakevsblock.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darealfungames.snakevsblock.MyGame;
import com.darealfungames.snakevsblock.ui.HomeUi;
import com.darealfungames.snakevsblock.uiactions.HomeUiListener;

public class HomeScreen implements Screen, HomeUiListener {

    private MyGame game;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private HomeUi homeUi;

    public HomeScreen(MyGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        homeUi = new HomeUi(this);
        batch =new SpriteBatch();
        Gdx.input.setInputProcessor(homeUi.getStage());
        this.camera = new OrthographicCamera();
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0.00f, 0.25f, 0.78f, 0.9f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        homeUi.update(delta);
        batch.begin();
        homeUi.render(batch);
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        homeUi.resize(width,height);
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
        homeUi.dispose();
    }

    @Override
    public void onPlayClicked() {
        game.setScreen(new MainGame(game));
    }

    @Override
    public void onSettingsClicked() {

    }

    @Override
    public void onExitClicked() {

    }

    @Override
    public void onShareClicked() {

    }

    @Override
    public void onSkinClicked() {
        game.setScreen(new MenuScreen(game));
    }

    @Override
    public void onDialogCancelled() {

    }

    @Override
    public void onSoundSwitched(boolean isOn) {

    }

    @Override
    public void onMusicSwitched(boolean isOn) {

    }

    @Override
    public void onCoinAddClicked() {

    }

    @Override
    public void onDiamondAddClicked() {

    }

    @Override
    public void onHealthAddClicked() {

    }
}
