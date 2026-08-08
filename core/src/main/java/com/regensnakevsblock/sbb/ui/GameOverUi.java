package com.regensnakevsblock.sbb.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.screens.MainGame;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.uiactions.GameOverListener;
import com.regensnakevsblock.sbb.uifactory.GameOverUiFactory;
import com.regensnakevsblock.sbb.utils.ActorFactory;
import com.regensnakevsblock.sbb.world.WorldState;

public class GameOverUi {

    private final Stage stage;
    private GameOverListener listener;
    private OrthographicCamera camera;
    private int scoreValue =0;
    private int highScoreValue=0;
    private int coinsValue=0;
    private boolean levelIsWon =false;
    private int soundVolume;

    private final SaveService saveService;

    private final GameOverUiFactory factory;
    private Label coins;
    private Label highScore;
    private Label score;

    // Confetti particle effect
    private ParticleEffect confettiEffect;
    private float timeSinceLastBurst = 0f;
    private float burstInterval = 2f; // Burst every 2 seconds
    private boolean effectStarted = false;

    public GameOverUi(GameOverListener listener,boolean levelIsWon){
        saveService = new SaveService();
        int gameSize = Constants.SCREEN_SIZE;
        camera=new OrthographicCamera();
        soundVolume = saveService.getSoundVolume();
        Assets.getInstance().backgroundMusic.pause();
        Assets.getInstance().gameOverSound.play((float) soundVolume /100);
        int WORLD_WIDTH=/*Constants.SCREEN_WIDTH;*/(gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        this.stage = new Stage(viewport);
        this.levelIsWon=levelIsWon;

        // Initialize confetti effect if level is won
        if(levelIsWon && Assets.getInstance().confettiCanon != null) {
            confettiEffect = new ParticleEffect(Assets.getInstance().confettiCanon);

            // Position at center of screen
            float centerX = stage.getWidth() / 2f;
            float centerY = stage.getHeight() / 2f;
            confettiEffect.setPosition(centerX, centerY);

            // Start the effect
            confettiEffect.start();
            effectStarted = true;
        }

        factory = new GameOverUiFactory(stage);
        this.listener=listener;
        build();
    }

    private void build(){
        //Build Images

        ImageButton restartButton = factory.createRestartButton();
        ImageButton homeButton = factory.createHomeButton();
        ImageButton shopButton = factory.createShopButton();
        ImageButton nextLevelButton = factory.createNextLevelButton();


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
        nextLevelButton.addListener(new ClickListener(){
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                listener.nextLevelButtonClicked();
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

        if(MainGame.gameMode== WorldState.GameMode.LEVELS){
            if(levelIsWon){
                stage.addActor(factory.createVictoryTitle());
                stage.addActor(nextLevelButton);
            }else{
                stage.addActor(factory.createGameOverTitle());
                stage.addActor(restartButton);
            }
        }else{
            stage.addActor(factory.createGameOverTitle());
            stage.addActor(restartButton);
        }
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
        // Update confetti effect
        if(levelIsWon && confettiEffect != null && effectStarted) {
            // Update the particle effect
            confettiEffect.update(deltaTime);

            // Check for periodic bursts
            timeSinceLastBurst += deltaTime;
            if(timeSinceLastBurst >= burstInterval) {
                // Restart the effect for another burst
                float centerX = stage.getWidth() / 2f;
                float centerY = stage.getHeight() / 2f;
                confettiEffect.setPosition(centerX, centerY);
                confettiEffect.start();
                timeSinceLastBurst = 0f;
            }

            // Stop the effect if it's finished and reset for next burst
            if(confettiEffect.isComplete()) {
                confettiEffect.reset();
            }
        }
    }

    public void render(){
        coins.setText(coinsValue);
        highScore.setText(highScoreValue);
        score.setText(scoreValue);

        // Draw stage first
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        // Draw confetti effect on top if level is won
        if(levelIsWon && confettiEffect != null && effectStarted) {
            // Get the stage's batch and draw the particle effect
            stage.getBatch().setProjectionMatrix(stage.getCamera().combined);
            stage.getBatch().begin();
            confettiEffect.draw(stage.getBatch(), Gdx.graphics.getDeltaTime());
            stage.getBatch().end();
        }
    }

    public void dispose() {
        if(confettiEffect != null) {
            confettiEffect.dispose();
        }
        stage.dispose();
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
