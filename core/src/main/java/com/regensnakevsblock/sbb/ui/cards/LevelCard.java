package com.regensnakevsblock.sbb.ui.cards;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.ui.core.BaseView;
import com.regensnakevsblock.sbb.ui.core.ListItemView;
import com.regensnakevsblock.sbb.ui.data.LevelData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

public class LevelCard extends Group implements ListItemView<LevelData>, BaseView<LevelData> {
    private ImageButton background;
    private Label index;
    private Image locked;
    private Image selectedBg;
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
        background = ActorFactory.createButton(Assets.getInstance().levelCardBg, 22.5f,30,140,140);
        addActor(background);

        index = ActorFactory.createTextLabel(70,60,90);
        addActor(index);

        locked = ActorFactory.createUiImage(Assets.getInstance().levelLockTexture,70,60,50,60);
        addActor(locked);

        cleared = ActorFactory.createUiImage(Assets.getInstance().levelMarkedTexture,70,60,50,60);
        addActor(cleared);

        selectedBg = ActorFactory.createUiImage(Assets.getInstance().levelSelectedBg,12.5f,20,160,160);
        addActor(selectedBg);


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

        index.remove();
        cleared.remove();
        locked.remove();
        if(!levelData.isSelected()){
            selectedBg.remove();
        }else{
            addActor(selectedBg);
        }
        if(levelData.isCanSelect()){
            if(levelData.isCleared()){
                addActor(cleared);
            }else{
                addActor(index);
            }
        }
        else{
            addActor(locked);
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
    public void updateData(LevelData data, int position) {

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
