package com.darealfungames.snakevsblock.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.darealfungames.snakevsblock.config.Constants;
import com.darealfungames.snakevsblock.elements.GameDialog;
import com.darealfungames.snakevsblock.uiactions.GameOverListener;
import com.darealfungames.snakevsblock.uifactory.GameOverUiFactory;
import com.darealfungames.snakevsblock.utils.ActorFactory;

public class GameOverUi {

    private final Stage stage;

    private GameOverListener listener;
    private OrthographicCamera camera;

    private final GameOverUiFactory factory;
    public GameOverUi(GameOverListener listener){
        int gameSize = Constants.SCREEN_SIZE;
        camera=new OrthographicCamera();
        int WORLD_WIDTH=/*Constants.SCREEN_WIDTH;*/(gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        this.stage = new Stage(viewport);

        factory = new GameOverUiFactory(stage);
        this.listener=listener;
        build();
    }
    private void build(){
        ImageButton restartButton = factory.createRestartButton();
        ImageButton homeButton = factory.createHomeButton();
        ImageButton shopButton = factory.createShopButton();

        restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                listener.restartButtonClicked();
            }
        });
        homeButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                listener.homeButtonClicked();
                System.out.println("Home is clicked");
            }
        });
        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                listener.shopButtonClicked();
            }
        });
        GameDialog gameOverBoard= factory.createGameOverDialog(stage);
        Label score = ActorFactory.createTextLabel(24,30,30);
        Label coins = ActorFactory.createTextLabel(24,30,30);
        Label highScore = ActorFactory.createTextLabel(24,30,30);

        gameOverBoard.addContentActor(score);
        gameOverBoard.addContentActor(highScore);
        gameOverBoard.addContentActor(coins);
        stage.addActor(restartButton);
        stage.addActor(homeButton);
        stage.addActor(shopButton);
    }

    public void update(float deltaTime){

    }
    public void render(){
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }
}
