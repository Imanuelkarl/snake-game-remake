package com.regensnakevsblock.sbb.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.elements.GameDialog;
import com.regensnakevsblock.sbb.screens.GameOverScreen;
import com.regensnakevsblock.sbb.screens.HomeScreen;
import com.regensnakevsblock.sbb.screens.MainGame;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.service.ads.RewardListener;
import com.regensnakevsblock.sbb.utils.ActorFactory;
import com.regensnakevsblock.sbb.utils.FontFactory;
import com.regensnakevsblock.sbb.world.WorldState;

public class UIRenderer implements RewardListener {

    private final Stage stage;
    private final WorldState worldState;

    private float width;
    private float height;

    private final Group pauseGroup;
    private final Group gameOverGroup;
    private Label scoreLabel;
    private Label highScoreLabel;
    private boolean pauseIsOpen=false;
    private Image skipButton;


    private GameDialog dialog;
    private GameDialog reviveDialog;
    private boolean reviveIsOpen;
    private SpriteBatch batch;
    private SaveService saveService;
    private int soundVolume;
    private int musicVolume;
    public UIRenderer(Stage stage, WorldState worldState) {

        this.stage = stage;
        this.worldState=worldState;
        batch =new SpriteBatch();
        this.pauseGroup=new Group();
        this.gameOverGroup=new Group();
        width=stage.getWidth();
        height=stage.getHeight();
        saveService=new SaveService();
        soundVolume = saveService.getSoundVolume();
        musicVolume = saveService.getMusicVolume();

        build();
        if(worldState.getGame().getAdsService()!=null){
            worldState.getGame().getAdsService().setRewardListener(this);
        }
    }

    public void render() {
        batch.setProjectionMatrix(worldState.getCamera().combined);
        pauseGroup.setVisible(worldState.isPaused());
        //gameOverGroup.setVisible(worldState.isGameOver());
        scoreLabel.setFontScale(1.2f);
        scoreLabel.setText(worldState.getScore());

        if(worldState.isGameOver()&&!reviveIsOpen){
            reviveDialog.open();
            reviveIsOpen=true;
        }
        if(worldState.isPaused()&&!pauseIsOpen){
            dialog.open();
            pauseIsOpen=true;
        }
        if(worldState.getGameMode()!= WorldState.GameMode.TUTORIAL){
            skipButton.remove();
        }
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }
    private void build(){
        ImageButton pauseButton = createPauseButton();
        dialog = new GameDialog(stage);

        dialog.setTitle("");
        dialog.setCloseButtonEnabled(false);
        dialog.setTitleTexture(Assets.getInstance().pauseHeaderTexture);

        reviveDialog = new GameDialog(stage);
        reviveDialog.setDialogSize(stage.getWidth(), stage.getHeight()/3);
        reviveDialog.setTitleEnabled(false);
        reviveDialog.setCloseButtonEnabled(false);
        reviveDialog.setBackground(Assets.getInstance().dialogHalfSize);


        scoreLabel = createTextLabel(80,stage.getWidth()/24,
            stage.getHeight() - stage.getHeight()/32 );

        highScoreLabel = createTextLabel(80,stage.getWidth()/24+60,
            stage.getHeight() - stage.getHeight()/32 -80 );

        highScoreLabel.setColor(Color.GOLD);
        highScoreLabel.setFontScale(0.6f);
        highScoreLabel.setText(String.valueOf(saveService.getHighScore()));

        Image trophy= ActorFactory.createUiImage(Assets.getInstance().highScoreTrophy,stage.getWidth()/24,
            stage.getHeight() - stage.getHeight()/32-100,40,40);



        float pad =width/32;
        float segment =dialog.getWidth()/3;
        float size=width/7;
        ImageButton homeButton = ActorFactory.createButton(
            Assets.getInstance().homeButtonTexture,
            (segment/2) -size/2 +pad,dialog.getHeight()*0.3f,
            size,size
        );
        ImageButton restartButton = ActorFactory.createButton(
            Assets.getInstance().restartButtonTexture,
            width/2f-size/2,dialog.getHeight()*0.3f,

            size,size
        );
        ImageButton resumeButton = ActorFactory.createButton(
            Assets.getInstance().resumeButtonTexture,
            width-segment/2-size/2-pad,dialog.getHeight()*0.3f,
            size,size
        );
        float reviveBtnWidth =stage.getWidth()/3f;
        float reviveBtnHeight =stage.getHeight()*0.07f;
        float segmentMiddle = stage.getWidth()/4;
        float reviveY=stage.getHeight()/2-reviveBtnHeight/2;
        ImageButton reviveWithAdsButton = ActorFactory.createButton(
            Assets.getInstance().defaultButton,
            segmentMiddle-reviveBtnWidth/2,reviveY
            ,
            reviveBtnWidth,
            reviveBtnHeight
        );
        ImageButton reviveWithHealthButton = ActorFactory.createButton(
            Assets.getInstance().defaultButton,
            stage.getWidth()-segmentMiddle-reviveBtnWidth/2,
            reviveY,
            reviveBtnWidth,
            reviveBtnHeight
        );
        Image portion = ActorFactory.createUiImage(Assets.getInstance().healthTexture, stage.getWidth()-segmentMiddle-reviveBtnHeight/4,reviveY+reviveBtnHeight/4,reviveBtnHeight/2,reviveBtnHeight/2);
        Label reviveWithAdsText = ActorFactory.createTextLabel(
            40
            ,
            reviveWithAdsButton.getX()+pad,reviveY+reviveBtnHeight/2
        );
        reviveWithAdsText.setText("WATCH AD");
        Label reviveWithHealthText = ActorFactory.createTextLabel(
            40, portion.getX()-pad,
            reviveY+reviveBtnHeight/2
        );
        reviveWithHealthText.setText(worldState.getHealthScore());
        ImageButton tapToContinueButton = ActorFactory.createButton(
            Assets.getInstance().giveUpTexture,
            stage.getWidth()/2 - stage.getWidth()/11,
            stage.getHeight()/2 - stage.getHeight()/8,
            stage.getWidth()/6,
            stage.getWidth()/18
        );
        Image keepMovingTitle = ActorFactory.createUiImage(Assets.getInstance().keepGoingTexture,
            stage.getWidth()/2 - stage.getWidth()/4,
            stage.getHeight()/2 +stage.getHeight()/10,
            stage.getWidth()/2,
            stage.getHeight()*0.03f
            );
        Texture whiteTex = createWhiteTexture();

        Image overlay = new Image(new TextureRegionDrawable(new TextureRegion(whiteTex)));

        overlay.setSize(
            stage.getWidth(),
            stage.getHeight()
        );
        // Semi-transparent black
        overlay.setColor(0f, 0f, 0f, 0.6f);

        Image overlay2 = new Image(new TextureRegionDrawable(new TextureRegion(whiteTex)));

        overlay2.setSize(
            stage.getWidth(),
            stage.getHeight()
        );
        // Semi-transparent black
        overlay2.setColor(0f, 0f, 0f, 0.6f);

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldState.setPaused(false);
                dialog.close();
            }
        });




        pauseGroup.addActor(overlay2);


        pauseGroup.setVisible(false);


        reviveDialog.addContentActor(keepMovingTitle);
        reviveDialog.addContentActor(reviveWithAdsButton);
        reviveDialog.addContentActor(reviveWithAdsText);
        reviveDialog.addContentActor(reviveWithHealthButton);
        reviveDialog.addContentActor(reviveWithHealthText);
        reviveDialog.addContentActor(portion);
        reviveDialog.addContentActor(tapToContinueButton);

        //gameOverGroup.addActor(overlay);
        //gameOverGroup.addActor(reviveWithAdsButton);
        //gameOverGroup.addActor(reviveWithHealthButton);
        //gameOverGroup.addActor(tapToContinueButton);


        gameOverGroup.setVisible(false);

        reviveWithHealthButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(worldState.getHealthScore()<=saveService.getHealths()){
                    saveService.saveHealths(saveService.getHealths()-(long)worldState.getHealthScore());
                    reviveDialog.addCloseListener(() -> {
                        worldState.reviveWithHealth();
                        reviveIsOpen=false;
                    });
                }else{
                    endGame();
                }

                reviveDialog.close();


            }
        });

        reviveWithAdsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                /*reviveDialog.addCloseListener(new GameDialog.CloseListener() {
                    @Override
                    public void onPopupClosed() {
                        worldState.reviveSnake(10);
                        reviveIsOpen=false;
                    }
                });
                reviveDialog.close();*/
                if(worldState.getGame().getAdsService()!=null){
                    if(worldState.getGame().getAdsService().isRewardedLoaded()){
                        worldState.getGame().getAdsService().showRewarded();
                    }
                    else{
                        reviveDialog.addCloseListener(() -> endGame());
                        reviveDialog.close();
                    }
                }

            }
        });
        if(worldState.getGame().getAdsService()==null||!worldState.getGame().getAdsService().isRewardedLoaded()){
                reviveWithAdsButton.remove();
                reviveWithAdsText.remove();
                reviveWithHealthButton.setX(stage.getWidth()-segmentMiddle-reviveBtnWidth/2-reviveBtnWidth+pad*2);
                reviveWithHealthText.setX(portion.getX()-pad-reviveBtnWidth+pad*2);
                portion.setX(stage.getWidth()-segmentMiddle-reviveBtnHeight/4-reviveBtnWidth+pad*2);

        }else{
            reviveWithHealthButton.setX(stage.getWidth()-segmentMiddle-reviveBtnWidth/2);
            reviveWithHealthText.setX(portion.getX()-pad);
            portion.setX(stage.getWidth()-segmentMiddle-reviveBtnHeight/4);
        }
        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldState.setPaused(true);
                dialog.open();
                pauseIsOpen=true;
            }
        });
        restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                worldState.getGame().setScreen(new MainGame(worldState.getGame(),worldState.getGameMode()));
            }
        });
        homeButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                worldState.getGame().setScreen(new HomeScreen(worldState.getGame()));

            }
        });
        tapToContinueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                reviveDialog.addCloseListener(() -> endGame());
                reviveDialog.close();

            }
        });
        float skipBtnHeight =stage.getHeight() *0.08f;
        skipButton = ActorFactory.createUiImage(Assets.getInstance().skipTutorialButton,stage.getWidth()/24,
              stage.getHeight()/32,stage.getWidth()/3,
            skipBtnHeight);

        skipButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                //worldState.getRows().add(generateRandomBlocks());
                worldState.setGameMode(WorldState.GameMode.ENDLESS);
                worldState.getSnake().addSegments(5);
                GameInstance.getSaveService().saveTutorialIsPlayed(true);
            }
        });
        //Image boxDialog = createImage(Assets.getInstance().boxDialogTexture,stage.getWidth()/16, stage.getHeight()/3.5f, stage.getWidth() -stage.getWidth()/8, stage.getHeight()/2.3f);
        //Image boxDialogHeader = createImage(Assets.getInstance().pauseHeaderTexture,stage.getWidth()/4, stage.getHeight()/1.55f, stage.getWidth()/2f, stage.getHeight()/10);

        Image musicIcon=ActorFactory.createUiImage(Assets.getInstance().musicTexture ,width/4,dialog.getHeight()*0.55f,width/12,width/12);
        Image soundIcon = ActorFactory.createUiImage(Assets.getInstance().soundTexture,width/4,dialog.getHeight()*0.43f,width/12, width/12);
        Slider musicVolumeSlider = createCustomSlider(stage.getWidth()/2.5f, dialog.getHeight()*0.55f,stage.getWidth()/3,stage.getHeight()/40);
        Slider soundVolumeSlider = createCustomSlider(stage.getWidth()/2.5f, dialog.getHeight()*0.43f,stage.getWidth()/3,stage.getHeight()/40);

        worldState.setSoundVolume(soundVolume);
        soundVolumeSlider.setValue(soundVolume);
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                saveService.saveSoundVolume((int) soundVolumeSlider.getValue());
                soundVolume=(int) soundVolumeSlider.getValue();
                worldState.setSoundVolume(soundVolume);
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
        dialog.addContentActor(resumeButton);
        dialog.addContentActor(restartButton);
        dialog.addContentActor(homeButton);

        stage.addActor(pauseButton);
        stage.addActor(scoreLabel);
        stage.addActor(trophy);
        stage.addActor(highScoreLabel);
        stage.addActor(pauseGroup);
        if(worldState.getGameMode()== WorldState.GameMode.TUTORIAL){
            stage.addActor(skipButton);
        }

        //stage.addActor(gameOverGroup);
    }
    private void endGame(){
        GameOverScreen gameOverScreen =new GameOverScreen(worldState.getGame(),false);
        gameOverScreen.setScore(worldState.getScore());
        gameOverScreen.setCoins(worldState.getCoins());
        if(saveService.getPlayCount()>5){
            if(worldState.getGame().getAdsService()!=null){
                if(worldState.getGame().getAdsService().isInterstitialLoaded()){
                    worldState.getGame().getAdsService().showInterstitial();
                }
            }

        }
        if(worldState.canShowAds()){
            worldState.getGame().getAdsService().showInterstitial();
        }
        saveService.savePlayCount();
        worldState.getGame().setScreen(gameOverScreen);
        reviveIsOpen=false;


    }

    private Texture createWhiteTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        this.width=width;
        this.height=height;
    }
    private ImageButton createPauseButton(){
        return ActorFactory.createButton(
            Assets.getInstance().pauseBtnTexture,
            stage.getWidth() - stage.getWidth()/8 - stage.getWidth()/32,
            stage.getHeight() - stage.getWidth()/8 - stage.getWidth()/32,
            stage.getWidth()/8,
            stage.getWidth()/8
        );
    }

    private Slider createCustomSlider(float x, float y, float width, float height) {

        TextureRegionDrawable background =
            new TextureRegionDrawable(new TextureRegion(Assets.getInstance().sliderBackgroundTexture));

        TextureRegionDrawable fill =
            new TextureRegionDrawable(new TextureRegion(Assets.getInstance().sliderFillTexture));

        TextureRegionDrawable knob =
            new TextureRegionDrawable(new TextureRegion(Assets.getInstance().sliderKnobTexture));

        // track thickness
        background.setMinHeight(height);
        fill.setMinHeight(height-height/6);

        // slim knob
        knob.setMinHeight(height * 1.5f);   // knob taller than track
        knob.setMinWidth(height );    // narrow knob

        Slider.SliderStyle style = new Slider.SliderStyle();
        style.background = background;
        style.knobBefore = fill;
        style.knob = knob;

        Slider slider = new Slider(0f, 100f, 1f, false, style);

        slider.setPosition(x, y);
        slider.setWidth(width);

        return slider;
    }
    public Label createTextLabel(int fontSize, float x, float y) {
        BitmapFont font = FontFactory.getRoboto(fontSize, true);
        Label.LabelStyle style = new Label.LabelStyle();
        style.font = font;

        Label label = new Label("", style);
        label.setPosition(x, y);
        return label;
    }

    public void dispose() {

    }

    @Override
    public void onRewarded() {
        reviveDialog.addCloseListener(new GameDialog.CloseListener() {
            @Override
            public void onPopupClosed() {
                worldState.reviveSnake(10);
                reviveIsOpen=false;
            }
        });
        reviveDialog.close();
    }
}
