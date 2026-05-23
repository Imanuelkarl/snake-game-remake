package com.regensnakevsblock.sbb.ui.cards;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SnakePreview extends Group {

    private static final int SEGMENTS = 4;

    private final Image[] parts = new Image[SEGMENTS];

    private final float segmentSize = 40f;

    private boolean selected = false;
    private float stateTime = 0f;

    public SnakePreview() {
        build();
    }

    private void build() {
        for (int i = 0; i < SEGMENTS; i++) {
            parts[i] = new Image();
            parts[i].setSize(segmentSize, segmentSize);
            addActor(parts[i]);
        }

        layoutStraight(); // default layout
    }

    // 🔹 Assign skin
    public void setSkin(TextureRegion region) {
        for (Image part : parts) {
            part.setDrawable(new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(region));
        }
    }

    // 🔹 Selection state
    public void setSelected(boolean selected) {
        this.selected = selected;

        if (!selected) {
            layoutStraight();
        }
    }

    // 🔹 Static layout (non-selected)
    private void layoutStraight() {
        float totalHeight = SEGMENTS * segmentSize;
        float startY = totalHeight/2 - segmentSize/2;

        for (int i = 0; i < SEGMENTS; i++) {
            float y =  i * (segmentSize);
            parts[i].setPosition(0, y);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!selected) return;

        stateTime += delta;

        for (int i = 0; i < SEGMENTS; i++) {
            float y = i * (segmentSize);

            // horizontal wave (snake-like motion)
            float x = MathUtils.sin(stateTime * 4f + i * 0.5f) * 8f;

            parts[i].setPosition(x, y);
        }
    }
}
