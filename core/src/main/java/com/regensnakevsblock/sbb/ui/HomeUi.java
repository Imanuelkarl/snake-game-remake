package com.regensnakevsblock.sbb.ui;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.elements.GameDialog;
import com.regensnakevsblock.sbb.elements.ImageCheckBoxGroup;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.uiactions.HomeUiListener;
import com.regensnakevsblock.sbb.uifactory.HomeUiFactory;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class HomeUi {

    private final Stage stage;
    private final HomeUiListener listener;


    GameDialog  dialog ;
    GameDialog termsOfService;
    private OrthographicCamera camera;
    private Label info;
    private final SaveService saveService;
    private int soundVolume;
    private int musicVolume;

    BitmapFont bitmapFont;
    private final HomeUiFactory homeUiFactory;
    public HomeUi(HomeUiListener listener) {
        this.listener = listener;
        int gameSize = Constants.SCREEN_SIZE;
        camera=new OrthographicCamera();
        int WORLD_WIDTH=/*Constants.SCREEN_WIDTH;*/(gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        this.stage = new Stage(viewport);
        this.homeUiFactory=new HomeUiFactory(stage);
        saveService = new SaveService();
        //saveService.clearData();
        if(saveService.isFirstTimePlaying()){
            saveService.saveHealths(5L);
            saveService.saveFirstTimePlaying(false);
        }
        setup();
        build();
    }

    private void build() {
        bitmapFont = Assets.getInstance().font;

        Table header = homeUiFactory.createHeaderTable();
        header.setBackground(new TextureRegionDrawable(Assets.getInstance().upLayout));
        ImageButton closeBtn = ActorFactory.createButton(Assets.getInstance().closeTexture, stage.getWidth()-70, stage.getHeight()-70, 60, 60);
        ImageButton minimizeBtn = ActorFactory.createButton(Assets.getInstance().sliderFillTexture, stage.getWidth()-140,stage.getHeight()-40,60,20);


        closeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();

            }
        });


        minimizeBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

            }
        });


        // Create Buttons
        ImageButton playButton = homeUiFactory.createPlayButton();
        ImageButton levelsButton = homeUiFactory.createLevelsButton();
        ImageButton settingsButton = homeUiFactory.createSettingsButton();
        ImageButton shareButton = homeUiFactory.createShareButton();
        ImageButton snakeSkinButton = homeUiFactory.createSnakeSkinButton();
        Group highScoreGroup = homeUiFactory.highScoreGroup(saveService.getHighScore());
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
                //termsOfService.open();
            }
        });
        snakeSkinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                listener.onSkinClicked();

            }
        });
        header.add().expandX();
        header.add(minimizeBtn).size(40);
        header.add(closeBtn).size(40);

        header.row();

        //header.add(minimizeBtn).pad(10);
        //header.add(closeBtn).pad(10);


        //stage.addActor(header);

        ImageCheckBoxGroup imageCheckBoxGroup = homeUiFactory.createDifficultyGroup();
        imageCheckBoxGroup.setCheckedItem(saveService.getDifficulty());
        imageCheckBoxGroup.setSelectionListener(new ImageCheckBoxGroup.SelectionListener() {
            @Override
            public void onItemChanged(int index, ImageCheckBoxGroup.Item item) {

            }

            @Override
            public void onItemChecked(int index, ImageCheckBoxGroup.Item item) {

                // Call your functions here
                saveService.saveDifficulty(index);
            }

            @Override
            public void onItemUnchecked(int index, ImageCheckBoxGroup.Item item) {
                System.out.println("Item " + index + " was unchecked!");
            }
        });

       dialog = new GameDialog(stage);

       dialog.setDialogSize(dialog.getWidth(),stage.getHeight()/2.5f);
       dialog.setTitleTexture(Assets.getInstance().settingHeader);
       dialog.setTitle("");
       dialog.setTitleEnabled(true);

        Image musicIcon=ActorFactory.createUiImage(Assets.getInstance().musicTexture , stage.getWidth()/4,dialog.getHeight()*0.55f, stage.getWidth()/12,stage.getWidth()/12);
        Image soundIcon = ActorFactory.createUiImage(Assets.getInstance().soundTexture,stage.getWidth()/4,dialog.getHeight()*0.47f,stage.getWidth()/12, stage.getWidth()/12);
        Slider musicVolumeSlider = ActorFactory.createCustomSlider(stage.getWidth()/2.5f, dialog.getHeight()*0.55f,stage.getWidth()/3,stage.getHeight()/40);
        Slider soundVolumeSlider = ActorFactory.createCustomSlider(stage.getWidth()/2.5f, dialog.getHeight()*0.47f,stage.getWidth()/3,stage.getHeight()/40);

        soundVolumeSlider.setValue(soundVolume);
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveService.saveSoundVolume((int) soundVolumeSlider.getValue());
                soundVolume=(int) soundVolumeSlider.getValue();
            }
        });
        musicVolumeSlider.setValue(musicVolume);
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveService.saveMusicVolume((int) musicVolumeSlider.getValue());
                musicVolume=(int) musicVolumeSlider.getValue();
                Assets.getInstance().backgroundMusic.setVolume((float) musicVolume/100);
            }
        });
        dialog.addContentActor(musicIcon);
        dialog.addContentActor(soundIcon);
        dialog.addContentActor(musicVolumeSlider);
        dialog.addContentActor(soundVolumeSlider);
       dialog.addContentActor(imageCheckBoxGroup);
       dialog.addContentActor(homeUiFactory.createDifficultyTitle());

        stage.addActor(background);

        stage.addActor(homeUiFactory.createLogoImage());
        if(isDesktop()) stage.addActor(header);
        // Add Buttons to Stage
        stage.addActor(highScoreGroup);
        stage.addActor(playButton);
        stage.addActor(levelsButton);
        stage.addActor(settingsButton);
        stage.addActor(shareButton);
        stage.addActor(snakeSkinButton);
        buildTermsOfServiceDialog();
        if(!saveService.getAcceptTerms()){

            termsOfService.open();
        }

    }
    private void buildTermsOfServiceDialog(){
        termsOfService =homeUiFactory.createTermsAndConditionDialog(stage);
        ImageButton tosButton = homeUiFactory.createTermsAndConditionButton();
        ImageButton privacyPolicy = homeUiFactory.createPrivacyPolicyButton();
        ImageButton acceptTerms = homeUiFactory.createAcceptButton();
        ImageButton rejectTerms = homeUiFactory.createRejectButton();

        tosButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(Constants.TERMS_OF_SERVICE_URL);
            }
        });
        privacyPolicy.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.net.openURI(Constants.PRIVACY_POLICY_URL);
            }
        });

        acceptTerms.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveService.saveAcceptTerms(true);
                termsOfService.close();
            }
        });
        rejectTerms.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }

        });
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.fontColor = Color.FIREBRICK;
        info = ActorFactory.createTextLabel("",36,stage.getWidth()*0.05f,stage.getHeight()*0.54f,labelStyle);
        info.setWidth(stage.getWidth()*0.9f);
        info.setWrap(true);
        termsOfService.addContentActor(info);
        termsOfService.addContentActor(tosButton);
        termsOfService.addContentActor(privacyPolicy);
        termsOfService.addContentActor(acceptTerms);
        termsOfService.addContentActor(rejectTerms);
    }
    private void setup(){
        Assets.getInstance().backgroundMusic.setVolume((float) saveService.getMusicVolume()/100);
        soundVolume=saveService.getSoundVolume();
        musicVolume=saveService.getMusicVolume();
    }
    private boolean isDesktop() {
        Application.ApplicationType type = Gdx.app.getType();
        return type == Application.ApplicationType.Desktop;
    }

    public Stage getStage() {
        return stage;
    }

    public void update(float delta) {
        info.setText("In order to play our game you must accept our terms and conditions below:");
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
