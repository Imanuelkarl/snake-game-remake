package com.darealfungames.snakevsblock.entities;

import com.badlogic.gdx.math.Vector2;

public class SnakeSection {
    // Make position final to avoid reassignment, just modify values
    private final Vector2 position = new Vector2();
    private float radius;

    public SnakeSection() {
        // position already initialized
    }

    // Return the actual Vector2 reference (not a copy) for performance
    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(float x, float y) {
        position.set(x, y);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
