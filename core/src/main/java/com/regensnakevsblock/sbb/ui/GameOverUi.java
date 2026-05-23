package com.regensnakevsblock.sbb.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.elements.GameDialog;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.uiactions.GameOverListener;
import com.regensnakevsblock.sbb.uifactory.GameOverUiFactory;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class GameOverUi {

    private final Stage stage;
    private GameOverListener listener;
    private OrthographicCamera camera;
    private int scoreValue =0;
    private int highScoreValue=0;
    private int coinsValue=0;

    private final SaveService saveService;

    private final GameOverUiFactory factory;
    private Label coins;
    private Label highScore;
    private Label score;
    public GameOverUi(GameOverListener listener){
        saveService = new SaveService();
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
        //Build Images

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
            }
        });
        shopButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                listener.shopButtonClicked();
            }
        });
        score = ActorFactory.createTextLabel(60, stage.getWidth()*0.550f, stage.getHeight()*0.72f);
        highScore = ActorFactory.createTextLabel(60,stage.getWidth()*0.55f,stage.getHeight()*0.62f);
        coins = ActorFactory.createTextLabel(60,stage.getWidth()*0.55f,stage.getHeight()*0.52f);
        highScoreValue = Math.toIntExact(saveService.getHighScore());

        coins.setText(coinsValue);
        highScore.setText(highScoreValue);
        score.setText(scoreValue);

        stage.addActor(factory.createGameOverDialog());
        stage.addActor(factory.createGameOverTitle());
        stage.addActor(factory.createScoreBox());
        stage.addActor(factory.createHighScoreBox());
        stage.addActor(factory.createCoinsBox());
        stage.addActor(score);
        stage.addActor(highScore);
        stage.addActor(coins);
        stage.addActor(restartButton);
        stage.addActor(homeButton);
        //stage.addActor(shopButton);
        //gameOverBoard.open();
    }

    public void update(float deltaTime){

    }
    public void render(){
        coins.setText(coinsValue);
        highScore.setText(highScoreValue);
        score.setText(scoreValue);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public Stage getStage() {
        return stage;
    }

    public void setCoins(int coins) {
        this.coinsValue=coins;
    }
    public void setScore(int score){
        this.scoreValue=score;
    }
}
