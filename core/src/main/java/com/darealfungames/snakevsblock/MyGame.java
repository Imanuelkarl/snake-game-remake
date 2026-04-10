package com.darealfungames.snakevsblock;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.config.Constants;
import com.darealfungames.snakevsblock.screens.CrossFadeTransition;
import com.darealfungames.snakevsblock.screens.LoadingScreen;
import com.darealfungames.snakevsblock.screens.MainGame;
import com.darealfungames.snakevsblock.screens.TransitionScreen;
import com.darealfungames.snakevsblock.service.SaveService;

import java.util.Stack;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGame extends Game {
    private SpriteBatch batch;
    private Texture image;

    private AssetManager assetManager;
    private Stack<Screen> screenStack;

    private SaveService saveService;

    @Override
    public void create() {
        batch = new SpriteBatch();
        assetManager = new AssetManager();
        saveService = new SaveService();
        screenStack= new Stack<>();

        // Set up game configuration
        Constants.initialize();
        Assets.getInstance().setAssetManager(assetManager);

        // Start with loading screen
        setScreen(new LoadingScreen(this));
    }
    public Screen getFormerScreen() {
        if (screenStack.size() > 1) {
            screenStack.pop();
            return screenStack.get(screenStack.size() - 1);
        }
        return null;
    }


    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        assetManager.dispose();
        saveService.saveGameData();
    }

    @Override
    public void setScreen(Screen screen) {
        screenStack.add(screen);
        super.setScreen(screen);
    }

    public void setScreenWithFade(Screen screen, float duration) {
        setScreen(new TransitionScreen(this, screen, duration));
    }

    public void setScreenWithCrossFade(Screen screen, float duration) {
        setScreen(new CrossFadeTransition(this, screen, duration));
    }

    /*public void setScreenWithSlide(Screen screen, float duration,
                                   SlideTransition.Direction direction) {
        //setScreen(new SlideTransition(this, screen, duration, direction));
    }*/
    public SpriteBatch getBatch() {
        return batch;
    }
    public AssetManager getAssetManager() {
        return assetManager;
    }

    public SaveService getSaveService() {
        return saveService;
    }
}
