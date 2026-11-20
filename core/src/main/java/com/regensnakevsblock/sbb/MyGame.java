package com.regensnakevsblock.sbb;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.screens.CrossFadeTransition;
import com.regensnakevsblock.sbb.screens.HomeScreen;
import com.regensnakevsblock.sbb.screens.LevelsScreen;
import com.regensnakevsblock.sbb.screens.SplashScreen;
import com.regensnakevsblock.sbb.screens.TransitionScreen;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.purchase.PurchaseService;

import java.util.Stack;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class MyGame extends Game {
    private SpriteBatch batch;
    private Texture image;

    private AssetManager assetManager;
    private Stack<Screen> screenStack;

    private SaveService saveService;
    private AdsService adsService;
    private PurchaseService purchaseService;
    public boolean loaded=false;


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
        setScreen(new SplashScreen(this));
    }
    public void setIsLoaded(boolean loaded){
        this.loaded=loaded;
    }

    public Screen getFormerScreen(Screen screen) {

        if(screen instanceof HomeScreen){
            System.out.println("it is home screen");
            return new HomeScreen(this);
        }
        if(screen instanceof LevelsScreen){
            System.out.println("it is level screen");
            return new LevelsScreen(this);
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
        // Home is the root screen: once we're back on it, back should exit the app
        if(screen instanceof HomeScreen){
            screenStack.clear();
        }
        super.setScreen(screen);
    }
    public void addToScreenStack(Screen screen){
        screenStack.add(screen);
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

    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    public void setAdsService(AdsService adsService) {
        this.adsService =adsService;
    }
    public AdsService getAdsService(){
        return adsService;
    }

    public PurchaseService getPurchaseService() {
        return purchaseService;
    }
    public void backPressed(){
        // Called from the Android UI thread -> switch screens on the render thread
        Gdx.app.postRunnable(() -> {
            if(!screenStack.isEmpty()){
                // Former screens were disposed on hide(), so rebuild a fresh instance
                Screen previous = getFormerScreen(screenStack.pop());
                if(previous!=null){
                    setScreen(previous);
                }
            }else{
                Gdx.app.exit();
            }
            System.out.println("Back was pressed");
        });
    }

    @Override
    public void resume() {
        if(!loaded)Assets.getInstance().reloadOnResume();
        super.resume();
    }
}
