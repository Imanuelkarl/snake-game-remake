package com.regensnakevsblock.sbb.ui.core;

import com.badlogic.gdx.scenes.scene2d.Group;

public interface ListItemView<T> {
    Group getRoot();
    void bind(T item, int position);
    void onSelected();
    void onDeselected();
    void onClick();
    void onLongClick();
    void update(float delta);
    void recycle();

    // Optional: Return the data this item represents
    T getItem();
    int getPosition();
}
