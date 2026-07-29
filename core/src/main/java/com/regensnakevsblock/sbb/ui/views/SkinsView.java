package com.regensnakevsblock.sbb.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.regensnakevsblock.sbb.assets.Assets2;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.elements.GameDialog;
import com.regensnakevsblock.sbb.ui.adapters.SkinAdapter;
import com.regensnakevsblock.sbb.ui.cards.SkinCard;
import com.regensnakevsblock.sbb.ui.cards.SnakePreview;
import com.regensnakevsblock.sbb.ui.core.ListManager;
import com.regensnakevsblock.sbb.ui.data.SkinData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class SkinsView extends BaseActorView {
    private ListManager<SkinData, SkinCard> listManager;
    private SkinAdapter adapter;
    private Label titleLabel;
    private float width;
    private float height;
    private Label noCoinsPop;
    private GameDialog dialog;

    public SkinsView(Group group) {
        super(group);
        setSize(group.getWidth(), group.getHeight());

        this.width = group.getWidth();
        this.height = group.getHeight();

        // ONLY lightweight setup
        setupUI();
        buildIfNeeded();
    }
    private Group loadingGroup;

    private void showLoading() {
        loadingGroup = new Group();

        Label loadingText = ActorFactory.createTextLabel(50, 20, 0);
        loadingText.setText("Loading skins...");
        loadingText.setPosition(50, 800, Align.center);

        loadingGroup.addActor(loadingText);
        addActor(loadingGroup);
    }

    private void hideLoading() {
        if (loadingGroup != null) {
            loadingGroup.remove();
        }
    }
    private boolean isBuilt = false;

    @Override
    public void buildIfNeeded() {
        if (isBuilt) return;
        isBuilt = true;

        setupList();
        setupTransform();

        // IMPORTANT: defer data load
        showLoading();

        Gdx.app.postRunnable(() -> {
            loadData();
            hideLoading();
        });
    }

    private void setupUI() {
        // Title
        titleLabel = ActorFactory.createTextLabel(24,20, Gdx.graphics.getHeight() - 50);
        titleLabel.setPosition(20, Gdx.graphics.getHeight() - 50);
        addActor(titleLabel);
        /*addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Clicking on skins");
                if (dialog != null) {
                    dialog.open();
                }else{
                    setupDialog();
                }
            }
        });*/
        Label.LabelStyle style = new Label.LabelStyle();
        style.fontColor= Color.FIREBRICK;
        noCoinsPop = ActorFactory.createTextLabel("24",style,20, Gdx.graphics.getHeight() - 50);
        noCoinsPop.setFontScale(0.6f);

        noCoinsPop.setWrap(true);
        noCoinsPop.setText("You don't have enough coins to buy this skin");

    }
    private void setupDialog(Stage stage){
        dialog = new GameDialog(stage);
        dialog.setDialogSize(width,height/8);
        dialog.setTitleEnabled(false);
        dialog.addContentActor(noCoinsPop);

        dialog.setBackground(Assets2.getInstance().dialogQuarterSizeOpen);
        dialog.open();
    }

    private void setupList() {
        adapter = new SkinAdapter();
        listManager = new ListManager<>(adapter);

        addActor(listManager);
    }
    private void setupTransform(){

        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout( 3, 350);
    }

    private void loadData() {
        adapter.setData(GameInstance.getInstance().getSkins());
    }

    @Override
    public void update(float delta) {
        // Update any animations
    }

    @Override
    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
        noCoinsPop.setPosition(60,60);
        setupTransform();
        noCoinsPop.setWidth(width*0.75f);
        //dialog = new GameDialog(getStage());
        listManager.renderAllItems();
        if(adapter.notEnoughCoins){
            setupDialog(this.getRoot().getStage());
            adapter.notEnoughCoins=false;
        }
    }

    @Override
    public boolean backPressed() {
        return false;
    }

}
