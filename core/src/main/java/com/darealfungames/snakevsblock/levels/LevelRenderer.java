package com.darealfungames.snakevsblock.levels;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelRenderer {
    SpriteBatch batch;

    public LevelRenderer() {
        this.batch = new SpriteBatch();

    }

    public void render() {
        batch.begin();
        batch.end();
    }
}
