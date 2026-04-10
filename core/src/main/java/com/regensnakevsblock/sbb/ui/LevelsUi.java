package com.regensnakevsblock.sbb.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.uiactions.LevelUIListener;
import com.regensnakevsblock.sbb.uifactory.LevelUiFactory;

public class LevelsUi {
    private final Stage stage;
    private final LevelUIListener levelUIListener;

    private final LevelUiFactory levelUiFactory;


    public LevelsUi(LevelUIListener levelUIListener) {
        int gameSize = Constants.SCREEN_SIZE;
        OrthographicCamera camera = new OrthographicCamera();
        int WORLD_WIDTH=/*Constants.SCREEN_WIDTH;*/(gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        this.stage = new Stage(viewport);

        this.levelUIListener=levelUIListener;
        this.levelUiFactory=new LevelUiFactory(stage.getWidth(),stage.getHeight());
        build();
    }
    private void build() {

        levelUiFactory.build();



        stage.addActor(levelUiFactory.background);


        //levelUiFactory.bottomNavigation.addOnItemSelectedListener(levelUIListener::onNavItemSelected);

        levelUiFactory.cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                levelUIListener.onCloseLevelScreen();
            }
        });

        stage.addActor(levelUiFactory.headerBackground);
        stage.addActor(levelUiFactory.cancelButton);
        stage.addActor(levelUiFactory.coinBar);
        stage.addActor(levelUiFactory.diamondBar);
        stage.addActor(levelUiFactory.healthBar);
        stage.addActor(levelUiFactory.bottomNavigation);

    }
    public void update(float deltaTime){

        stage.act(deltaTime);
        stage.draw();
    }
    public void resize(float width, float height){
        int gameSize = Constants.SCREEN_SIZE;
        OrthographicCamera camera = new OrthographicCamera();
        int WORLD_WIDTH=/*Constants.SCREEN_WIDTH;*/(int) ((int)(gameSize * width) /height);
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        stage.getViewport().update(viewport.getScreenWidth(), viewport.getScreenHeight(), true);
    }

    public Stage getStage() {
        return stage;
    }
}
