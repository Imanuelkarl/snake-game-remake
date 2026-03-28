package com.darealfungames.snakevsblock.ui.core;

import com.badlogic.gdx.scenes.scene2d.Group;

public class ViewManagerNew extends Group {
    private ViewPagerAdapter adapter;


    public void setAdapter(ViewPagerAdapter adapter){
        this.adapter=adapter;
        if(adapter.getCount()>0){
            addActor(adapter.getView(0).getRoot());
        }
    }
}
