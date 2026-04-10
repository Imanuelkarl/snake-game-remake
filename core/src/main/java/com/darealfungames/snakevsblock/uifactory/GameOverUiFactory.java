package com.darealfungames.snakevsblock.uifactory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.elements.GameDialog;
import com.darealfungames.snakevsblock.utils.ActorFactory;
import com.darealfungames.snakevsblock.utils.FontActor;

public class GameOverUiFactory {
    private float width;
    private float height;

    public GameOverUiFactory(Stage stage){
        width=stage.getWidth();
        height=stage.getHeight();
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
    public ImageButton createRestartButton(){
        return ActorFactory.createButton(Assets.getInstance().replayButtonTexture,0,height/8-height/12,width/2.2f,height/12);
    }
    public ImageButton createHomeButton(){
        return ActorFactory.createButton(Assets.getInstance().home2ButtonTexture,width/2.2f+width/20,height/8-height/12,width/2.2f,height/12);
    }
    public ImageButton createShopButton(){
        return ActorFactory.createButton(Assets.getInstance().upgradeButtonTexture,width/2-width/4.4f,height/16+height/12,width/2.2f,height/12);
    }
}
