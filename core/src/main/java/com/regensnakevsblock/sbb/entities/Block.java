package com.regensnakevsblock.sbb.entities;

import static com.regensnakevsblock.sbb.utils.RandomEngine.getRandom;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.regensnakevsblock.sbb.enumaretors.PowerUp;

public class Block {
    private Rectangle bounds;

    private Rectangle leftCollision;
    private boolean powerUp =false;
    private PowerUp powerUpType;
    private String color;

    public void setPowerUp(boolean yes, PowerUp powerUpType){
        powerUp=yes;
        this.powerUpType=powerUpType;
    }
    public void setRightCollision(Rectangle rightCollision) {
        this.rightCollision = rightCollision;
    }
    public PowerUp getPowerUp(){
        return powerUpType;
    }



    public void setLeftCollision(Rectangle leftCollision) {
        this.leftCollision = leftCollision;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    private Rectangle rightCollision;
    private int value;
    private boolean active;
    private TextureRegion region;
    private int colorId;


    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;

    public Block() {

    }


    public boolean hasPowerUp() {
        return powerUp;
    }

    public Rectangle getRight() {
        return rightCollision;
    }

    public Rectangle getLeft() {
        return leftCollision;
    }

    public Block(Texture texture,float x, float y, float width, float height, int value) {
        this.bounds = new Rectangle(x, y, width, height);
        this.leftCollision= new Rectangle(x-width/2,y,width/2,height);
        this.rightCollision=new Rectangle(x+width,y,width/2,height);
        this.value = value;
        this.active = true;
        colorId = getRandom(0,8);
        int cols = 5;
        int rows = 2;
        int posX=(int)getBlockPosition(colorId).x;
        int posY= (int)getBlockPosition(colorId).y;
        int tileWidth  = texture.getWidth() / cols;
        int tileHeight = texture.getHeight() / rows;
        texture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        this.region = new TextureRegion(
            texture,
            posY * tileWidth + 1,
            posX * tileHeight + 1,
            tileWidth - 2,
            tileHeight - 2
        );
    }

    public int getColor(){
        return colorId;

    }
    public static Vector2 getBlockPosition(int position){
        int x= position /5;
        int y=position%5;
        return new Vector2(x,y);
    }
    public Rectangle getBounds() { return bounds; }
    public int getValue() { return value; }
    public boolean isActive() { return active; }

    public void setActive(boolean active) { this.active = active; }
    public void reduceValue() { if (value > 0) value--; }
    public boolean shouldRemove() { return value <= 0; }

    public void setValue(int value) {
        this.value=value;
    }

    public TextureRegion getRegion() {
        return region;
    }
}
