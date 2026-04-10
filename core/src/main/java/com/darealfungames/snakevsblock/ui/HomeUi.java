package com.darealfungames.snakevsblock.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.config.Constants;
import com.darealfungames.snakevsblock.config.GameInstance;
import com.darealfungames.snakevsblock.elements.BottomNavigation;
import com.darealfungames.snakevsblock.elements.DynamicDialog;
import com.darealfungames.snakevsblock.elements.GameDialog;
import com.darealfungames.snakevsblock.uiactions.HomeUiListener;
import com.darealfungames.snakevsblock.uifactory.HomeUiFactory;
import com.darealfungames.snakevsblock.utils.ActorFactory;
import com.darealfungames.snakevsblock.utils.FontFactory;
import com.darealfungames.snakevsblock.utils.SimpleBitmapFont;

public class HomeUi {

    private final Stage stage;
    private final HomeUiListener listener;


    GameDialog  dialog ;
    private OrthographicCamera camera;

    SimpleBitmapFont simpleBitmapFont;
    private final HomeUiFactory homeUiFactory;
    public HomeUi(HomeUiListener listener) {
        this.listener = listener;
        int gameSize = Constants.SCREEN_SIZE;
        camera=new OrthographicCamera();
        int WORLD_WIDTH=/*Constants.SCREEN_WIDTH;*/(gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        this.stage = new Stage(viewport);
        this.homeUiFactory=new HomeUiFactory(stage);
        build();
    }

    private void build() {
        simpleBitmapFont = GameInstance.getInstance().getSimpleBitmapFont();

        // Optional: Enable debug to see character boxes
        simpleBitmapFont.setDebugMode(false);


        // Create Buttons
        ImageButton playButton = homeUiFactory.createPlayButton();
        ImageButton levelsButton = homeUiFactory.createLevelsButton();
        ImageButton settingsButton = homeUiFactory.createSettingsButton();
        ImageButton shareButton = homeUiFactory.createShareButton();
        ImageButton snakeSkinButton = homeUiFactory.createSnakeSkinButton();
        Image background = ActorFactory.createUiImage(Assets.getInstance().backgroundTexture,0,0,stage.getWidth(),stage.getHeight());
        //Set Buttons Actions/ Listeners
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onPlayClicked();
            }
        });
        levelsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onLevelsClicked();
            }
        });


        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onSettingsClicked();
                dialog.open();

            }
        });
        shareButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onShareClicked();
            }
        });
        snakeSkinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onSkinClicked();

            }
        });


        //dialog =new DynamicDialog(new BitmapFont());

// Customize before showing (optional)
       dialog = new GameDialog(stage);

       dialog.setDialogSize(dialog.getWidth(),stage.getHeight()/2.5f);
       dialog.setTitleTexture(Assets.getInstance().settingHeader);
       dialog.setTitle("");
       dialog.setTitleEnabled(true);

        stage.addActor(background);

        stage.addActor(homeUiFactory.createLogoImage());
        // Add Buttons to Stage
        stage.addActor(playButton);
        stage.addActor(levelsButton);
        stage.addActor(settingsButton);
        stage.addActor(shareButton);
        stage.addActor(snakeSkinButton);


    }

    public Stage getStage() {
        return stage;
    }

    public void update(float delta) {
        stage.act(delta);
        stage.draw();
    }
    public void render(Batch batch){
        //dialog.draw(batch,"SETTINGS",dialog.getX()+dialog.getWidth()*0.1f,dialog.getY()+dialog.getHeight()*0.9f,0.2f,0.15f);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        homeUiFactory.resize(width,height);
    }

    public void dispose() {
        stage.dispose();
    }
}
