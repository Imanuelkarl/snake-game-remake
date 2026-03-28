package com.darealfungames.snakevsblock.entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class WinBody {

    private boolean active;

    private Vector2 position;

    private Rectangle bounds;

    private float height;

    private float width;

    private float radius;

    private int positionIndex;

    private int value;
    public int getPositionIndex() {
        return positionIndex;
    }

    public void setPositionIndex(int positionIndex) {
        this.positionIndex = positionIndex;
    }
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x,float y) {
        this.position=new Vector2(x,y);
    }

    public int getValue() {
        return value;
    }

    public void setActive(boolean b) {
        this.active=b;
    }

    public boolean isActive() {
        return active;
    }

    public Rectangle getBounds() {
        return bounds;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
