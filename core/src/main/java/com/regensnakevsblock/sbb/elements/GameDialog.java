package com.regensnakevsblock.sbb.elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.enumaretors.AnimationType;
import com.regensnakevsblock.sbb.utils.ActorFactory;
import com.regensnakevsblock.sbb.utils.FontActor;
import com.regensnakevsblock.sbb.utils.SimpleBitmapFont;

public class GameDialog extends Group {

    private Stage stage;

    private Image background;
    private Image titleBackground;
    private Label titleLabel;
    private ImageButton closeButton;
    private Group frameGroup;
    private Group buttonsGroup;
    private Image overlay;
    private boolean isOpen=false;
    private boolean titleEnabled = false;
    private boolean closeEnabled = true;

    private float dialogWidth;
    private float dialogHeight;
    private final Vector2 tmpVec = new Vector2();

    private Texture bgTexture;
    private Texture titleTexture;
    private Texture closeTexture;
    SimpleBitmapFont simpleBitmapFont;
    private CloseListener closeListener;
    private FontActor titleFont;

    public GameDialog(Stage stage) {

        this.stage = stage;

        Assets assets = Assets.getInstance();

        // Default textures
        bgTexture = assets.noHeaderDialog;
        titleTexture = assets.noHeaderDialog;
        closeTexture = assets.closeTexture;

        simpleBitmapFont=GameInstance.getInstance().getSimpleBitmapFont();

        // Optional: Enable debug to see character boxes
        simpleBitmapFont.setDebugMode(false);
        initialize();
    }
    public void draw(Batch batch,CharSequence text,float x, float y, float scaleX, float scaleY){
        simpleBitmapFont.setScale(scaleX,scaleY);
        if(isOpen) {
            simpleBitmapFont.draw(batch, text, x, y);
        }
    }

    private void initialize() {

        float stageWidth = stage.getViewport().getWorldWidth();
        float stageHeight = stage.getViewport().getWorldHeight();

        dialogWidth = stageWidth * 0.9f;
        dialogHeight = stageHeight * 0.5f;

        setSize(stageWidth, stageHeight);
        buildOverlay();
        buildFrame();
        buildBackground();
        buildTitle();
        buildButtons();
        buildCloseButton();

        setOrigin(Align.center);
    }
    public void buildFrame(){
        frameGroup= new Group();

        frameGroup.setSize(dialogWidth,dialogHeight);
        frameGroup.setOrigin(Align.center);


        addActor(frameGroup);
        frameGroup.setPosition(
            (getWidth() - dialogWidth) / 2,
            (getHeight() - dialogHeight) / 2
        );
    }

    private void buildBackground() {

        // Semi-transparent black

        background = new Image(new TextureRegionDrawable(new TextureRegion(bgTexture)));
        background.setSize(dialogWidth, dialogHeight);

        frameGroup.addActor(background);
    }

    private void buildTitle() {

        titleBackground = new Image(new TextureRegionDrawable(new TextureRegion(titleTexture)));

        float titleHeight = dialogHeight*0.25f;

        titleBackground.setSize(dialogWidth * 0.8f,titleHeight);
        titleBackground.setPosition(
            dialogWidth / 2f - titleBackground.getWidth() / 2f,
            dialogHeight - titleHeight *0.7f
        );

        titleLabel = ActorFactory.createTextLabel(30, 0, 0);
        titleLabel.setAlignment(Align.center);
        titleLabel.setSize(titleBackground.getWidth(), titleBackground.getHeight());
        titleLabel.setPosition(titleBackground.getX(), titleBackground.getY());

        simpleBitmapFont.setScale(0.3f,0.45f);
        titleFont = new FontActor(simpleBitmapFont, "");
        titleFont.setSize(titleBackground.getWidth(), titleBackground.getHeight());
        //titleFont.setAlignment(FontActor.CENTER);
        titleFont.setPosition(titleBackground.getX(), titleBackground.getY());

        frameGroup.addActor(titleBackground);
        frameGroup.addActor(titleFont);
        //frameGroup.addActor(titleLabel);
    }

    public void setTitleEnabled(boolean enabled) {

        titleEnabled = enabled;

        if (!enabled) {
            titleBackground.remove();
            titleLabel.remove();
            titleFont.remove();
        }
    }



    public void addContentActor(Actor actor) {

        // Get actor's current position (likely in stage or old parent space)
        float x = actor.getX();
        float y = actor.getY();

        // Convert to stage coordinates first
        if (actor.getParent() != null) {
            actor.localToStageCoordinates(tmpVec.set(x, y));
        } else {
            tmpVec.set(x, y);
        }

        // Convert stage → contentGroup local space
        frameGroup.stageToLocalCoordinates(tmpVec);

        // Add actor
        frameGroup.addActor(actor);

        // Apply corrected position
        actor.setPosition(tmpVec.x, tmpVec.y);
        actor.toFront();
        actor.setOrigin(Align.bottomLeft);
        frameGroup.setTransform(false);
    }
    private void buildCloseButton() {

        closeButton = new ImageButton(
            new TextureRegionDrawable(new TextureRegion(closeTexture))
        );
        float size=stage.getWidth()*0.18f;
        closeButton.setSize(size,size);

        closeButton.setPosition(
            stage.getWidth() / 2f - closeButton.getWidth() / 2f,
            closeButton.getHeight()/2);


        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

        addActor(closeButton);
    }

    public void setCloseButtonEnabled(boolean enabled) {

        closeEnabled = enabled;

        if (!enabled) {
            closeButton.remove();
        }
    }

    private void buildButtons() {
        buttonsGroup = new Group();

        buttonsGroup.setSize(dialogWidth, 100);
        buttonsGroup.setPosition(0, 40);

        frameGroup.addActor(buttonsGroup);
    }

    public void addButton(Button button) {

        float spacing = 20;
        int count = buttonsGroup.getChildren().size;

        float totalWidth = count * button.getWidth() + (count - 1) * spacing;

        float startX = dialogWidth / 2f - totalWidth / 2f;

        button.setPosition(startX + count * (button.getWidth() + spacing), 0);

        buttonsGroup.addActor(button);
    }
    private void buildOverlay(){
        Texture whiteTex = createWhiteTexture();

        overlay = new Image(new TextureRegionDrawable(new TextureRegion(whiteTex)));

        overlay.setSize(
            stage.getWidth(),
            stage.getHeight()
        );
        overlay.setColor(0f, 0f, 0f, 0.85f);
        addActor(overlay);
    }

    /*public void open() {




        setPosition(
            stage.getWidth()/2f - getWidth()/2f,
            stage.getHeight()/2f - getHeight()/2f
        );
        frameGroup.clearActions();

        frameGroup.setScale(0f);

        frameGroup.addAction(Actions.sequence(
            Actions.scaleTo(1.1f,1.1f,0.15f, Interpolation.swingOut),
            Actions.scaleTo(1f,1f,0.1f)
        ));
        isOpen=true;
    }
    public void close() {
        isOpen=false;
        frameGroup.clearActions();
        frameGroup.addAction(Actions.sequence(

            Actions.scaleTo(0f,0f,0.15f,Interpolation.swingIn),

            Actions.run(this::remove
            )
        ));
    }*/
    private void resetFrameGroupState() {
        frameGroup.clearActions();

        frameGroup.setScale(1f);
        frameGroup.getColor().a = 1f;

        // Reset position to center (important for slide/drop animations)
        frameGroup.setPosition(
            getWidth() / 2f - frameGroup.getWidth() / 2f,
            getHeight() / 2f - frameGroup.getHeight() / 2f
        );
    }
    public void open() {
        open(AnimationType.DROP_BOUNCE); // new default
    }

    public void open(AnimationType type) {
        resetFrameGroupState();
        stage.addActor(this);

        float centerX = stage.getWidth()/2f - getWidth()/2f;
        float centerY = stage.getHeight()/2f - getHeight()/2f;

        setPosition(centerX, centerY);

        frameGroup.clearActions();

        switch (type) {
            case SCALE_POP:
                playScalePopOpen();
                break;
            case DROP_BOUNCE:
                playDropBounceOpen();
                break;
            case FADE:
                playFadeOpen();
                break;
            case SLIDE_LEFT:
                playSlideLeftOpen();
                break;
            case SLIDE_RIGHT:
                playSlideRightOpen();
                break;
            case ZOOM_FADE:
                playZoomFadeOpen();
                break;
        }

        isOpen = true;
    }
    public void close() {
        close(AnimationType.DROP_BOUNCE); // match default open
    }

    public void close(AnimationType type) {
        isOpen = false;

        frameGroup.clearActions();

        switch (type) {
            case SCALE_POP:
                playScalePopClose();
                break;
            case DROP_BOUNCE:
                playDropBounceClose();
                break;
            case FADE:
                playFadeClose();
                break;
            case SLIDE_LEFT:
                playSlideLeftClose();
                break;
            case SLIDE_RIGHT:
                playSlideRightClose();
                break;
            case ZOOM_FADE:
                playZoomFadeClose();
                break;
        }
    }// =========================
    // SCALE POP (Original)
// =========================
    private void playScalePopOpen() {
        frameGroup.clearActions();
        frameGroup.setScale(0f);

        frameGroup.addAction(Actions.sequence(
            Actions.scaleTo(1.1f,1.1f,0.15f, Interpolation.swingOut),
            Actions.scaleTo(1f,1f,0.1f)
        ));
    }

    private void playScalePopClose() {
        frameGroup.clearActions();

        frameGroup.addAction(Actions.sequence(
            Actions.scaleTo(0f,0f,0.15f,Interpolation.swingIn), Actions.run( ()->{
                if(closeListener!=null){
                    this.closeListener.onPopupClosed();
                }
            }),
            Actions.run(this::remove)

        ));
    }


    // =========================
// DROP FROM TOP + BOUNCE (DEFAULT)
// =========================
    private void playDropBounceOpen() {
        frameGroup.clearActions();

        float centerY = frameGroup.getY();
        float startY = stage.getHeight();

        frameGroup.setY(startY);

        frameGroup.addAction(Actions.sequence(
            Actions.moveTo(frameGroup.getX(), centerY, 0.35f, Interpolation.swingOut),
            Actions.moveTo(frameGroup.getX(), centerY + 20f, 0.08f),
            Actions.moveTo(frameGroup.getX(), centerY, 0.08f)
        ));
    }

    private void playDropBounceClose() {
        frameGroup.clearActions();

        float endY = stage.getHeight();

        frameGroup.addAction(Actions.sequence(
            Actions.moveTo(frameGroup.getX(), endY, 0.25f, Interpolation.swingIn),
            Actions.run( ()->{
                if(closeListener!=null){
                    this.closeListener.onPopupClosed();
                }
            }),
            Actions.run(this::remove)
        ));
    }


    // =========================
// FADE
// =========================
    private void playFadeOpen() {
        frameGroup.clearActions();

        frameGroup.getColor().a = 0f;

        frameGroup.addAction(Actions.fadeIn(0.25f));
    }

    private void playFadeClose() {
        frameGroup.clearActions();

        frameGroup.addAction(Actions.sequence(
            Actions.fadeOut(0.2f),
            Actions.run( ()->{
                if(closeListener!=null){
                    this.closeListener.onPopupClosed();
                }
            }),
            Actions.run(this::remove)
        ));
    }


    // =========================
// SLIDE FROM LEFT
// =========================
    private void playSlideLeftOpen() {
        frameGroup.clearActions();

        float centerX = frameGroup.getX();
        float startX = -frameGroup.getWidth();

        frameGroup.setX(startX);

        frameGroup.addAction(
            Actions.moveTo(centerX, frameGroup.getY(), 0.3f, Interpolation.swingOut)
        );
    }

    private void playSlideLeftClose() {
        frameGroup.clearActions();

        frameGroup.addAction(Actions.sequence(
            Actions.moveTo(-frameGroup.getWidth(), frameGroup.getY(), 0.25f, Interpolation.swingIn),
            Actions.run( ()->{
                if(closeListener!=null){
                    this.closeListener.onPopupClosed();
                }
            }),
            Actions.run(this::remove)
        ));
    }

    public boolean isOpen() {
        return isOpen;
    }

    public interface CloseListener{
         void onPopupClosed();
    }
    public void addCloseListener(CloseListener closeListener){
        this.closeListener=closeListener;
    }


    // =========================
// SLIDE FROM RIGHT
// =========================
    private void playSlideRightOpen() {
        frameGroup.clearActions();

        float centerX = frameGroup.getX();
        float startX = stage.getWidth();

        frameGroup.setX(startX);

        frameGroup.addAction(
            Actions.moveTo(centerX, frameGroup.getY(), 0.3f, Interpolation.swingOut)
        );
    }

    private void playSlideRightClose() {
        frameGroup.clearActions();

        frameGroup.addAction(Actions.sequence(
            Actions.moveTo(stage.getWidth(), frameGroup.getY(), 0.25f, Interpolation.swingIn),
            Actions.run( ()->{
                if(closeListener!=null){
                    this.closeListener.onPopupClosed();
                }
            }),
            Actions.run(this::remove)
        ));
    }


    // =========================
// ZOOM + FADE
// =========================
    private void playZoomFadeOpen() {
        frameGroup.clearActions();

        frameGroup.setScale(0.7f);
        frameGroup.getColor().a = 0f;

        frameGroup.addAction(Actions.parallel(
            Actions.scaleTo(1f,1f,0.25f, Interpolation.fade),
            Actions.fadeIn(0.25f)
        ));
    }

    private void playZoomFadeClose() {
        frameGroup.clearActions();

        frameGroup.addAction(Actions.sequence(
            Actions.parallel(
                Actions.scaleTo(0.7f,0.7f,0.2f),
                Actions.fadeOut(0.2f)
            ),
            Actions.run( ()->{
                if(closeListener!=null){
                    this.closeListener.onPopupClosed();
                }
            }),
            Actions.run(this::remove)
        ));
    }
    public void setBackground(Texture texture) {
        background.setDrawable(new TextureRegionDrawable(texture));
    }
    public Texture createWhiteTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    public void setTitleTexture(Texture texture) {

        titleBackground.setDrawable(new TextureRegionDrawable(texture));
    }

    public void setCloseTexture(Texture texture) {
        closeButton.getStyle().imageUp = new TextureRegionDrawable(texture);
    }

    public void setDialogSize(float width, float height) {

        this.dialogWidth = width;
        this.dialogHeight = height;

        frameGroup.setSize(width, height);

        frameGroup.remove();
        buildFrame();
        buildBackground();
        buildTitle();
        buildButtons();
    }
    public void setFont(SimpleBitmapFont font){
        titleFont.setFont(font);
    }

    public void setTitle(String title) {
        this.titleLabel.setText(title);
        this.titleFont.setText(title);
    }
}
