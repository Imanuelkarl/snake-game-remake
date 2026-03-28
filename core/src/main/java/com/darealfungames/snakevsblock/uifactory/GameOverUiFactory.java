package com.darealfungames.snakevsblock.uifactory;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.utils.ActorFactory;

public class GameOverUiFactory {
    private float width;
    private float height;

    public GameOverUiFactory(Stage stage){
        width=stage.getWidth();
        height=stage.getHeight();
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
