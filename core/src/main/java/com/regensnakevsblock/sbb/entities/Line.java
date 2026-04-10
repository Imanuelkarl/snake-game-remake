package com.regensnakevsblock.sbb.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;

public class Line {
    private Rectangle bounds;

    float x;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    private int position;
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getRegionX() {
        return regionX;
    }

    public void setRegionX(float regionX) {
        this.regionX = regionX;
    }

    public float getRegionY() {
        return regionY;
    }

    public void setRegionY(float regionY) {
        this.regionY = regionY;
    }
    public float getRegionWidth() {
        return regionWidth;
    }

    public void setRegionWidth(float regionWidth) {
        this.regionWidth = regionWidth;
    }

    public float getRegionHeight() {
        return regionHeight;
    }

    public void setRegionHeight(float regionHeight) {
        this.regionHeight = regionHeight;
    }

    float y;

    float width;

    float height;

    float regionX;

    float regionY;

    float regionWidth;

    float regionHeight;

    boolean active;
    private Rectangle leftCollision;

    public void setRightCollision(Rectangle rightCollision) {
        this.rightCollision = rightCollision;
    }



    public void setLeftCollision(Rectangle leftCollision) {
        this.leftCollision = leftCollision;
    }


    private Rectangle rightCollision;
    public Line(float x, float y, float width, float height) {
        this.bounds = new Rectangle(x, y, width, height);
        this.leftCollision= new Rectangle(x-width*2,y,width*2,height);
        this.rightCollision=new Rectangle(x+width,y,width*2,height);
    }

    public Rectangle getBounds() { return bounds; }

    public Rectangle getRight() {
        return rightCollision;
    }

    public Rectangle getLeft() {
        return leftCollision;
    }
}
