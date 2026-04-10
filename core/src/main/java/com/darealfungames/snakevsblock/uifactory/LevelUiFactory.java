package com.darealfungames.snakevsblock.uifactory;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.elements.BottomNavigation;
import com.darealfungames.snakevsblock.elements.ItemBar;
import com.darealfungames.snakevsblock.utils.ActorFactory;

public class LevelUiFactory {
    private float width;
    private float height;

    public Image background;
    public ItemBar coinBar;
    public ItemBar diamondBar;
    public ItemBar healthBar;
    public Group header;
    public ImageButton cancelButton;
    public Image headerBackground;
    public BottomNavigation bottomNavigation;
    public LevelUiFactory(float width, float height){
        this.width=width;
        this.height=height;
        buildHeader();
    }

    public void build(){
        background = ActorFactory.createUiImage(Assets.getInstance().menuBGTexture,0,0,width,height);
        bottomNavigation= new BottomNavigation(4);
        bottomNavigation.setSize(width, height*.1f);
        bottomNavigation.setPosition(0, 0);
        bottomNavigation.selectItem(0);
    }
    public void resize(float width, float height){
        this.width=width;
        this.height=height;
        build();
        buildHeader();

    }

    //Create Header
    public void buildHeader(){
        float barWidth=width/5;
        float barSpace=(width-width/8)/3;
        float barHeight =height/36;
        float barPad=(barSpace -barWidth)/2;
        float barY=height-height/18;
        coinBar = new ItemBar(barPad,barY,barWidth,barHeight);
        coinBar.setItemTexture(Assets.getInstance().coinTexture);
        diamondBar = new ItemBar(barSpace+ barPad,barY,barWidth,barHeight);
        diamondBar.setItemTexture(Assets.getInstance().diamondTexture);
        healthBar = new ItemBar(2*barSpace+barPad,barY,barWidth,barHeight);
        healthBar.setItemTexture(Assets.getInstance().healthTexture);
        headerBackground = ActorFactory.createUiImage(Assets.getInstance().upLayout,0,height-height/12,width,height/12);
        cancelButton = ActorFactory.createButton(Assets.getInstance().closeTexture,width-width/8,height-width/8,width/12,width/12);
    }



}
