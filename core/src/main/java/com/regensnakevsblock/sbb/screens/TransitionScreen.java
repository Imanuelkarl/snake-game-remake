package com.regensnakevsblock.sbb.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.regensnakevsblock.sbb.MyGame;

public class TransitionScreen implements Screen {
    private final MyGame game;
    private final Screen nextScreen;
    private final Screen currentScreen;
    private final float duration;
    private float timer = 0;
    private final ShapeRenderer shapeRenderer;
    private boolean transitioning = true;

    public TransitionScreen(MyGame game, Screen nextScreen, float duration) {
        this.game = game;
        this.currentScreen = game.getScreen();
        this.nextScreen = nextScreen;
        this.duration = duration;
        this.shapeRenderer = new ShapeRenderer();

        // Initialize next screen
        nextScreen.show();
        nextScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        // Update current screen
        if (currentScreen != null) {
            currentScreen.render(delta);
        }

        // Render next screen on top with alpha
        if (nextScreen != null) {
            nextScreen.render(delta);
        }

        // Draw fade overlay
        timer += delta;
        float alpha = Math.min(1f, timer / duration);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, alpha);
        shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shapeRenderer.end();

        // Complete transition
        if (timer >= duration) {
            game.setScreen(nextScreen);
            if (currentScreen != null) {
                currentScreen.dispose();
            }
            dispose();
        }
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }

    // Other required Screen methods (empty implementations)
    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
