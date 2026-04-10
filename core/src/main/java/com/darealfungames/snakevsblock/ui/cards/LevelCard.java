package com.darealfungames.snakevsblock.ui.cards;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.darealfungames.snakevsblock.ui.core.BaseView;
import com.darealfungames.snakevsblock.ui.core.ListItemView;
import com.darealfungames.snakevsblock.ui.data.LevelData;

public class LevelCard extends Group implements ListItemView<LevelData>, BaseView {
    private Image background;
    private Label index;
    private Image cleared;
    private Image star1;
    private Image star2;
    private Image star3;
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
        return null;
    }

    @Override
    public void bind(LevelData item, int position) {

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
        return null;
    }

    @Override
    public int getPosition() {
        return 0;
    }
}
