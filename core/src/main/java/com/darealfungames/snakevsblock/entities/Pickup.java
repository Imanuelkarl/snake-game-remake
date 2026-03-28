package com.darealfungames.snakevsblock.entities;


import com.badlogic.gdx.math.Vector2;

public class Pickup {

    Vector2 position;
    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position){
        this.position=position;
    }

    public Pickup(Vector2 position){
        this.position=position;
    }
}
