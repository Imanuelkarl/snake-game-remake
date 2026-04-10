package com.regensnakevsblock.sbb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.ui.LevelsUi;
import com.regensnakevsblock.sbb.uiactions.LevelUIListener;

public class LevelsScreen extends InputAdapter implements Screen, LevelUIListener {

    private final MyGame game;
    private LevelsUi levelsUi;
    public LevelsScreen(MyGame game){
        levelsUi = new LevelsUi(this);
        Gdx.input.setInputProcessor(levelsUi.getStage());

        this.game=game;

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glClearColor(0.071f, 0.11f, 0.145f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        levelsUi.update(delta);


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
    public void onPlayButtonClicked(int levelIndex) {

    }

    @Override
    public void onNextLevelButtonClicked() {

    }

    @Override
    public void onLevelSelected(int levelIndex) {

    }

    @Override
    public void onCloseLevelScreen() {
        game.setScreen(new HomeScreen(game));
    }

    @Override
    public void onCloseDialog() {

    }

    @Override
    public void onPlayAgainButtonClicked() {

    }
}
