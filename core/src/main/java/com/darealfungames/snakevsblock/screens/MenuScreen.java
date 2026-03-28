package com.darealfungames.snakevsblock.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.darealfungames.snakevsblock.MyGame;
import com.darealfungames.snakevsblock.ui.MenuUi;
import com.darealfungames.snakevsblock.uiactions.MenuUIListener;

public class MenuScreen implements Screen, MenuUIListener {

    private final MyGame game;
    private MenuUi menuUi;
    public MenuScreen(MyGame game){
        menuUi = new MenuUi(this);
        Gdx.input.setInputProcessor(menuUi.getStage());

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
        menuUi.update(delta);


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
    public void onNavItemSelected(int index) {


    }

    @Override
    public void onCancelButtonClicked() {
        game.setScreen(new HomeScreen(game));
    }
}
