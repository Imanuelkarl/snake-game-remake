package com.regensnakevsblock.sbb.ui.cards;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.ui.core.BaseView;
import com.regensnakevsblock.sbb.ui.core.ListItemView;
import com.regensnakevsblock.sbb.ui.data.LevelData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class LevelCard extends Group implements ListItemView<LevelData>, BaseView {
    private ImageButton background;
    private Label index;
    private Image locked;
    private Image cleared;
    private Image star1;
    private Image star2;
    private Image star3;
    private LevelData levelData;
    private int position;

    public LevelCard() {
        setupUI();
    }
    private void setupUI() {
        // Background
        background = ActorFactory.createButton(Assets.getInstance().levelCardBg, 12.5f,20,160,160);
        addActor(background);

        index = ActorFactory.createTextLabel(70,60,90);
        addActor(index);

        locked = ActorFactory.createUiImage(Assets.getInstance().levelLockTexture,70,60,50,60);
        addActor(locked);


    }
    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void resize(float width, float height) {
        setSize(width,height);
        
    }

    @Override
    public boolean backPressed() {
        return false;
    }

    @Override
    public void buildIfNeeded() {

    }

    @Override
    public ViewState getState() {
        return null;
    }

    @Override
    public Group getRoot() {
        return this;
    }

    @Override
    public void bind(LevelData levelData, int position) {
        this.levelData=levelData;
        this.position=position;

        String text =String.valueOf(levelData.getLevelIndex());
        index.setPosition(-20 *text.length()+100,80);
        index.setText(levelData.getLevelIndex());
        if(levelData.isCanSelect()){
            locked.remove();
        }
        else{
            index.remove();
        }

    }

    @Override
    public void onSelected() {

    }

    @Override
    public void onDeselected() {

    }

    @Override
    public void onClick() {

    }

    @Override
    public void onLongClick() {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void recycle() {

    }

    @Override
    public LevelData getItem() {
        return levelData;
    }

    @Override
    public int getPosition() {
        return position;
    }
}
