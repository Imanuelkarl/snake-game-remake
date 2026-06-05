package com.regensnakevsblock.sbb.uifactory;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.regensnakevsblock.sbb.assets.Assets2;
import com.regensnakevsblock.sbb.elements.BottomNavigation;
import com.regensnakevsblock.sbb.elements.ItemBar;
import com.regensnakevsblock.sbb.utils.ActorFactory;

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
    public Image bottomBackground;
    public ImageButton playButton;
    public LevelUiFactory(float width, float height){
        this.width=width;
        this.height=height;
        buildHeader();
    }

    public void build(){
        background = ActorFactory.createUiImage(Assets2.getInstance().menuBGTexture,0,0,width,height);
        bottomBackground = ActorFactory.createUiImage(Assets2.getInstance().bottomNavBg,0,0,width,height*.1f);

        playButton=ActorFactory.createButton(Assets2.getInstance().levelPlayButton, width/2 -width/4, height*0.005f, width/2, height/12);
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
        coinBar.setItemTexture(Assets2.getInstance().coinTexture);
        diamondBar = new ItemBar(barSpace+ barPad,barY,barWidth,barHeight);
        diamondBar.setItemTexture(Assets2.getInstance().diamondTexture);
        healthBar = new ItemBar(2*barSpace+barPad,barY,barWidth,barHeight);
        healthBar.setItemTexture(Assets2.getInstance().healthTexture);
        headerBackground = ActorFactory.createUiImage(Assets2.getInstance().upLayout,0,height-height/12,width,height/12);
        cancelButton = ActorFactory.createButton(Assets2.getInstance().closeTexture,width-width/8,height-width/8,width/12,width/12);
    }



}
