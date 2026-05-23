package com.regensnakevsblock.sbb.ui.views;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.regensnakevsblock.sbb.ui.core.BaseView;

public abstract class BaseActorView extends Group implements BaseView {
    protected ViewState state = ViewState.CREATED;
    private Group parent;
    protected boolean isVisible = false;
    public BaseActorView(Group parent){
        this.parent=parent;
    }
    public void setParentView(Group parent){
        this.parent=parent;
    }
    public Group getParentView(){
       return parent;
    }

    @Override
    public Group getRoot() {
        return this;
    }

    @Override
    public void onShow() {
        isVisible = true;
        state = ViewState.SHOWING;
        setVisible(true);
    }

    @Override
    public void onHide() {
        isVisible = false;
        state = ViewState.HIDDEN;
        setVisible(false);
    }

    @Override
    public void onPause() {
        isVisible = false;
    }

    @Override
    public void onResume() {
        isVisible = true;
    }

    @Override
    public void onDestroy() {
        state = ViewState.DESTROYED;
        clear();
    }

    @Override
    public ViewState getState() {
        return state;
    }


    @Override
    public boolean backPressed() {
        return false; // Override to handle back button
    }
}
