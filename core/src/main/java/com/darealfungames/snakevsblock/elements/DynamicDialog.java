package com.darealfungames.snakevsblock.elements;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.utils.ActorFactory;

/**
 * Dynamic game dialog built only with actors and textures (no Skin).
 * Extends Group directly so it can be added to any Stage like any other actor.
 */
public class DynamicDialog extends Group implements Disposable {

    // Core layout actors
    private final Stack contentStack;
    private final Image backgroundImage;
    private final Table innerTable;
    private final Table titlePanel;
    private final Image titleBackgroundImage;
    private final Label titleLabel;
    private final Table contentArea;
    private final Table buttonBar;

    private final ImageButton closeButton;
    private final Image dimOverlay;

    private final Texture blackTexture;

    private Stage attachedStage;
    private boolean isShown = false;

    // Customization
    private boolean titleVisible = true;
    private boolean closeVisible = true;
    private float preferredWidth = -1f;
    private float preferredHeight = -1f;

    public DynamicDialog(BitmapFont font) {  // pass the font you want to use for labels
        // Load textures from Assets
        Assets assets = Assets.getInstance();

        Texture bgTex       = assets.boxDialogTexture;      // main dialog frame
        Texture titleTex    = assets.pauseHeaderTexture;    // title bar background
        Texture closeTex    = assets.closeTexture;          // close icon

        // Create drawables
        Drawable bgDrawable      = new TextureRegionDrawable(new TextureRegion(bgTex));
        Drawable titleDrawable   = new TextureRegionDrawable(new TextureRegion(titleTex));
        Drawable closeUpDrawable = new TextureRegionDrawable(new TextureRegion(closeTex));
        // For pressed state you can create a second texture or use the same for simplicity
        Drawable closeDownDrawable = closeUpDrawable;

        // Dim overlay (black semi-transparent)
        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(0, 0, 0, 0.70f);
        pix.fill();
        blackTexture = new Texture(pix);
        pix.dispose();

        dimOverlay = new Image(new TextureRegionDrawable(new TextureRegion(blackTexture)));
        dimOverlay.setFillParent(true);
        dimOverlay.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

        // Main background
        backgroundImage = new Image(bgDrawable);

        // Stack = background + UI layers
        contentStack = new Stack();
        contentStack.add(backgroundImage);

        // Inner layout table
        innerTable = new Table();
        innerTable.setFillParent(true);

        // Title area
        titlePanel = new Table();
        titleBackgroundImage = new Image(titleDrawable);
        titleBackgroundImage.setFillParent(true);

        titleLabel = new Label("", new Label.LabelStyle(font, Color.WHITE));
        titleLabel.setAlignment(Align.left);

        // Wrap title label in its own container for better control
        Table titleTextContainer = new Table();
        titleTextContainer.add(titleLabel).expandX().left().padLeft(24).padRight(60);

        titlePanel.addActor(titleBackgroundImage);
        titlePanel.addActor(titleTextContainer);

        // Content area (where user adds anything)
        contentArea = new Table();
        contentArea.top();

        // Button bar at bottom
        buttonBar = new Table();

        // Close button (will be positioned manually)
        ImageButton.ImageButtonStyle closeStyle = new ImageButton.ImageButtonStyle();
        closeStyle.imageUp   = closeUpDrawable;
        closeStyle.imageDown = closeDownDrawable;
        closeButton = new ImageButton(closeStyle);
        closeButton.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                close();
            }
        });

        // Build structure
        if (titleVisible) {
            innerTable.add(titlePanel).growX().height(60).row();  // adjust height as needed
        }
        innerTable.add(contentArea).grow().row();
        innerTable.add(buttonBar).growX().padTop(20).padBottom(20).row();

        contentStack.add(innerTable);

        addActor(contentStack);
        addActor(closeButton);  // close is sibling → can position outside bounds

        // Initial visibility
        closeButton.setVisible(closeVisible);
    }

    // ──────────────────────────────────────────────
    //                Public customization API
    // ──────────────────────────────────────────────

    public void setTitle(String text) {
        titleLabel.setText(text);
    }

    public void setTitleVisible(boolean visible) {
        titleVisible = visible;
        titlePanel.setVisible(visible);
        // Optional: force layout update if already shown
        if (isShown) contentStack.invalidateHierarchy();
    }

    public void setCloseVisible(boolean visible) {
        closeVisible = visible;
        closeButton.setVisible(visible);
    }

    public void setBackgroundTexture(Texture texture) {
        backgroundImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void setTitleBackgroundTexture(Texture texture) {
        titleBackgroundImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
    }

    public void setCloseButtonTextures(Texture up, Texture down) {
        ImageButton.ImageButtonStyle style = closeButton.getStyle();
        style.imageUp   = new TextureRegionDrawable(new TextureRegion(up));
        style.imageDown = new TextureRegionDrawable(new TextureRegion(down != null ? down : up));
        closeButton.setStyle(style);
    }

    public void setPreferredSize(float width, float height) {
        this.preferredWidth = width;
        this.preferredHeight = height;
    }

    public Table getContentArea() {
        return contentArea;
    }

    public void clearContent() {
        contentArea.clear();
    }

    public void clearButtons() {
        buttonBar.clear();
    }

    /** Add one button (repeat 1–3 times for horizontal layout) */
    public void addButton(String text, Runnable action, BitmapFont font, Color color) {
        Label lbl = new Label(text, new Label.LabelStyle(font, color));
        ImageButton btn = ActorFactory.createButton(Assets.getInstance().pauseBtnTexture,0,0,2,2 ); // transparent button → acts as hit area
        btn.add(lbl).pad(12, 24, 12, 24);
        btn.addListener(new ClickListener() {
            @Override public void clicked(InputEvent event, float x, float y) {
                if (action != null) action.run();
            }
        });
        buttonBar.add(btn).expandX().uniformX().fillX().pad(0, 10, 0, 10);
    }

    /** Convenience: classic Yes / No / Cancel style */
    public void setThreeButtons(String yesText, Runnable yesAction,
                                String noText, Runnable noAction,
                                String cancelText, Runnable cancelAction,
                                BitmapFont font, Color textColor) {
        clearButtons();
        if (yesText != null)    addButton(yesText,    yesAction,    font, textColor);
        if (noText != null)     addButton(noText,     noAction,     font, textColor);
        if (cancelText != null) addButton(cancelText, cancelAction, font, textColor);
    }

    public void show(Stage stage) {
        if (isShown) return;
        attachedStage = stage;

        float w = preferredWidth > 0 ? preferredWidth : stage.getWidth() * 0.78f;
        float h = preferredHeight > 0 ? preferredHeight : stage.getHeight() * 0.68f;

        setSize(w, h);
        contentStack.setSize(w, h);
        backgroundImage.setSize(w, h);

        // Center
        float cx = (stage.getWidth() - w) / 2f;
        float cy = (stage.getHeight() - h) / 2f;
        setPosition(cx, cy);

        // Position close button outside top-right corner
        if (closeVisible) {
            closeButton.pack();
            float closeOffset = closeButton.getWidth() * 0.4f;
            closeButton.setPosition(
                w - closeButton.getWidth() + closeOffset,
                h - closeButton.getHeight() / 2f + closeOffset
            );
        }

        // Game-style open animation
        setScale(0.4f);
        setOrigin(w / 2f, h / 2f);
        addAction(Actions.sequence(
            Actions.parallel(
                Actions.scaleTo(1f, 1f, 0.38f, com.badlogic.gdx.math.Interpolation.bounceOut),
                Actions.alpha(1f, 0.38f)
            )
        ));

        dimOverlay.setColor(0,0,0,0);
        dimOverlay.addAction(Actions.alpha(0.70f, 0.32f));

        stage.addActor(dimOverlay);
        stage.addActor(this);

        isShown = true;
    }

    public void close() {
        if (!isShown) return;

        addAction(Actions.sequence(
            Actions.parallel(
                Actions.scaleTo(0.4f, 0.4f, 0.28f, com.badlogic.gdx.math.Interpolation.swingIn),
                Actions.alpha(0f, 0.28f)
            ),
            Actions.run(() -> {
                remove();
                isShown = false;
            })
        ));

        dimOverlay.addAction(Actions.sequence(
            Actions.alpha(0f, 0.28f),
            Actions.run(dimOverlay::remove)
        ));
    }

    @Override
    public void dispose() {
        blackTexture.dispose();
        // Note: Do NOT dispose the asset textures here – managed by Assets
    }

    // Optional helpers
    public Stage getAttachedStage() {
        return attachedStage;
    }

    public boolean isShown() {
        return isShown;
    }
}
