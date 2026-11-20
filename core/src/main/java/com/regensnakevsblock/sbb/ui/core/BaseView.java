package com.regensnakevsblock.sbb.ui.core;

import com.badlogic.gdx.scenes.scene2d.Group;

public interface BaseView<T> {
    Group getRoot();
    void onShow();
    void onHide();
    void onPause();
    void onResume();
    void onDestroy();
    void update(float delta);

    void updateData(T data,int position);

    void resize(float width, float height);

    boolean backPressed();

    void buildIfNeeded();



    // Optional: Add lifecycle constants
    enum ViewState {
        CREATED, SHOWING, HIDDEN, DESTROYED
    }
    ViewState getState();
}
