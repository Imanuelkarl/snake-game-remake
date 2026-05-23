package com.regensnakevsblock.sbb.uifactory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.elements.GameDialog;
import com.regensnakevsblock.sbb.utils.ActorFactory;
import com.regensnakevsblock.sbb.utils.FontActor;

public class GameOverUiFactory {
    private float width;
    private float height;
    private GameOverLayout gameOverLayout;

    public GameOverUiFactory(Stage stage){
        width=stage.getWidth();
        height=stage.getHeight();
        gameOverLayout = new GameOverLayout(width,height);
    }
    public GameDialog createGameOverDialog(Stage stage,int score,int highScore,int coins){
        GameDialog gameOverBoard = new GameDialog(stage);
        Label scoreLabel = ActorFactory.createTextLabel(24,30,30);
        Label coinsLabel = ActorFactory.createTextLabel(24,30,30);
        Label highScoreLabel = ActorFactory.createTextLabel(24,30,30);
        gameOverBoard.addContentActor(scoreLabel);
        gameOverBoard.addContentActor(highScoreLabel);
        gameOverBoard.addContentActor(coinsLabel);
        gameOverBoard.setTitle("");
        return gameOverBoard;
    }
    public GameDialog createGameOverDialog(Stage stage){
        GameDialog gameOverBoard = new GameDialog(stage);
        Label scoreLabel = ActorFactory.createTextLabel(24,30,30);
        Label coinsLabel = ActorFactory.createTextLabel(24,30,30);
        Label highScoreLabel = ActorFactory.createTextLabel(24,30,30);
        gameOverBoard.addContentActor(scoreLabel);
        gameOverBoard.addContentActor(highScoreLabel);
        gameOverBoard.addContentActor(coinsLabel);
        gameOverBoard.setTitle("");
        return gameOverBoard;
    }
    public Image createGameOverTitle(){
        return ActorFactory.createUiImage(Assets.getInstance().gameOverHeaderTexture,gameOverLayout.title.x,gameOverLayout.title.y,gameOverLayout.title.width,gameOverLayout.title.height);
    }
    public Image createGameOverDialog(){
        return ActorFactory.createUiImage(Assets.getInstance().noHeaderDialog,gameOverLayout.dialog.x,gameOverLayout.dialog.y,gameOverLayout.dialog.width,gameOverLayout.dialog.height);
    }
    public Image createScoreBox(){
        return ActorFactory.createUiImage(Assets.getInstance().scoreTextBoard, gameOverLayout.score.x,gameOverLayout.score.y,gameOverLayout.score.width,gameOverLayout.score.height);
    }
    public Image createHighScoreBox(){
        return ActorFactory.createUiImage(Assets.getInstance().highScoreTextBoard, gameOverLayout.highScore.x,gameOverLayout.highScore.y,gameOverLayout.highScore.width,gameOverLayout.highScore.height);
    }
    public Image createCoinsBox(){
        return ActorFactory.createUiImage(Assets.getInstance().coinsTextBoard, gameOverLayout.coins.x,gameOverLayout.coins.y,gameOverLayout.coins.width,gameOverLayout.coins.height);
    }
    public ImageButton createRestartButton(){
        return ActorFactory.createButton(Assets.getInstance().replayButtonTexture,0,height/8-height/12,width/2.2f,height/12);
    }
    public ImageButton createHomeButton(){
        return ActorFactory.createButton(Assets.getInstance().home2ButtonTexture,width/2.2f+width/20,height/8-height/12,width/2.2f,height/12);
    }
    public ImageButton createShopButton(){
        return ActorFactory.createButton(Assets.getInstance().upgradeButtonTexture,width/2-width/4.4f,height/16+height/12,width/2.2f,height/12);
    }
    public void resize(float width,float height){
        this.width=width;
        this.height=height;
        this.gameOverLayout = new GameOverLayout(width,height);
    }
}
