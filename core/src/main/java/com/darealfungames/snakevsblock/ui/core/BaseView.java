package com.darealfungames.snakevsblock.ui.core;

import com.badlogic.gdx.scenes.scene2d.Group;

public interface BaseView {
    Group getRoot();
    void onShow();
    void onHide();
    void onPause();
    void onResume();
    void onDestroy();
    void update(float delta);
    void resize(float width, float height);

    boolean backPressed();

    // Optional: Add lifecycle constants
    enum ViewState {
        CREATED, SHOWING, HIDDEN, DESTROYED
    }
    ViewState getState();
}
