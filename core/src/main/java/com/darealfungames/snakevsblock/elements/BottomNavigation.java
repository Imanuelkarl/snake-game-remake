package com.darealfungames.snakevsblock.elements;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.darealfungames.snakevsblock.assets.Assets;

public class BottomNavigation extends Group {

    private Image background;
    private Texture whiteTexture;
    private Group indicatorGroup;  // Group for indicators (behind items)
    private Group itemsGroup;       // Group for navigation items (front)
    private Array<NavigationItem> items;
    private NavigationItem selectedItem;
    private Image selectionIndicator;
    private Vector2 indicatorTargetPosition;

    private int columns;
    private float itemWidth;
    private float itemHeight;
    private float itemDisplaySize; // Size of the actual item icon (50-65% of item area)
    private float itemDisplayOffset; // Offset from bottom for item display

    private Texture backgroundTexture;
    private Texture defaultItemTexture;
    private Texture selectedItemTexture;
    private Texture defaultIndicatorTexture;

    private float animationDuration = 0.25f;
    private float selectedScale = 1.2f;
    private float selectedYOffset = 20f;
    private float indicatorGap = 10f;
    private float itemSizeRatio = 0.45f; // 60% of item area by default (between 50-65%)

    public interface OnItemSelectedListener {
        void onItemSelected(int index);
    }

    private Array<OnItemSelectedListener> listeners;

    public BottomNavigation(int columns) {
        this.columns = columns;
        this.items = new Array<>();
        this.listeners = new Array<>();

        Assets assets = Assets.getInstance();

        // Default textures - you should replace these with your actual default textures
        backgroundTexture = assets.bottomNavBg;
        defaultItemTexture = assets.defaultItemTexture;
        selectedItemTexture = assets.selectOverlayTexture;
        defaultIndicatorTexture = assets.defaultOverlayTexture;
        Pixmap pixmap = new Pixmap(2, 2, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fill();
        whiteTexture = new Texture(pixmap);
        pixmap.dispose();

        initialize();

    }

    private void initialize() {
        listeners = new Array<>();

        // Create groups for proper layering (indicators behind, items in front)
        indicatorGroup = new Group();
        itemsGroup = new Group();

        addActor(indicatorGroup);
        addActor(itemsGroup);

        buildBackground();
        buildItems();
        buildSelectionIndicator();
    }

    private void buildBackground() {
        background = new Image(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
        background.setSize(getWidth(), getHeight());
        // Add background at the very back
        addActorAt(0, background);
    }

    private void buildItems() {
        for (int i = 0; i < columns; i++) {
            NavigationItem item = new NavigationItem(i);
            items.add(item);
            itemsGroup.addActor(item);

            final int index = i;
            item.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectItem(index);
                }
            });
        }
    }

    private void buildSelectionIndicator() {
        selectionIndicator = new Image(new TextureRegionDrawable(new TextureRegion(selectedItemTexture)));
        selectionIndicator.setOrigin(Align.bottomLeft);
        indicatorGroup.addActor(selectionIndicator);
        indicatorTargetPosition = new Vector2();
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);

        if (background != null) {
            background.setSize(width, height);
        }

        if (columns > 0) {
            itemWidth = width / columns;
            itemHeight = height;

            // Calculate item display size (50-65% of the item area)
            itemDisplaySize = Math.min(itemWidth, itemHeight) * itemSizeRatio;
            // Center the item display in its column, with a slight bottom offset for the upward animation
            itemDisplayOffset = (itemHeight - itemDisplaySize) / 4; // Offset from bottom

            if (items.size > 0) {
                for (int i = 0; i < items.size; i++) {
                    NavigationItem item = items.get(i);
                    item.setDisplaySize(itemWidth,itemHeight);
                    item.setPosition(i * itemWidth, itemDisplayOffset);
                }
            }

            if (selectionIndicator != null) {

                float indicatorWidth = itemWidth;
                float indicatorHeight = itemHeight ; // same as your overlay

                selectionIndicator.setSize(indicatorWidth, indicatorHeight);

                // Align bottom-left for simpler positioning
                selectionIndicator.setOrigin(Align.bottomLeft);
            }
        }
    }

    public void selectItem(int index) {
        if (index < 0 || index >= items.size) return;

        NavigationItem newSelectedItem = items.get(index);
        if (selectedItem == newSelectedItem) return;

        // Deselect current item
        if (selectedItem != null) {
            selectedItem.setSelected(false);

            selectedItem.clearActions();
            selectedItem.customGroup.clearActions();

            selectedItem.addAction(Actions.moveTo(
                selectedItem.getX(),
                selectedItem.getOriginalY(),
                animationDuration,
                Interpolation.pow2Out
            ));

            selectedItem.customGroup.addAction(Actions.parallel(
                Actions.scaleTo(1f, 1f, animationDuration, Interpolation.pow2Out),
                Actions.run(() -> selectedItem.setDefaultTexture())
            ));
        }

        // Select new item
        selectedItem = newSelectedItem;
        selectedItem.setSelected(true);

        // Indicator movement (this is fine)
        float targetX = selectedItem.getX();
        float targetY = indicatorGap; // or 0 if you want it flush

        targetY = Math.max(0, Math.min(targetY, getHeight() - selectionIndicator.getHeight()));

        indicatorTargetPosition.set(targetX, targetY);

        selectionIndicator.clearActions();
        selectionIndicator.addAction(Actions.moveTo(targetX, targetY, animationDuration, Interpolation.pow2Out));

        // Animate selected item
        selectedItem.clearActions();
        selectedItem.customGroup.clearActions();

        float maxYOffset = Math.min(
            selectedYOffset,
            getHeight() - selectedItem.getHeight() - selectedItem.getY()
        );

        // ✅ MOVE THE ITEM (not the group)
        selectedItem.addAction(Actions.moveTo(
            selectedItem.getX(),
            selectedItem.getY() + maxYOffset,
            animationDuration,
            Interpolation.pow2Out
        ));

        // ✅ SCALE ONLY CONTENT
        selectedItem.customGroup.addAction(
            Actions.scaleTo(selectedScale, selectedScale, animationDuration, Interpolation.pow2Out)
        );

        // Notify listeners
        for (OnItemSelectedListener listener : listeners) {
            listener.onItemSelected(index);
        }
    }

    public void setItemTexture(int index, Texture texture) {
        if (index >= 0 && index < items.size) {
            items.get(index).setCustomTexture(texture);
        }
    }

    public void setItemTextures(Texture[] textures) {
        for (int i = 0; i < Math.min(textures.length, items.size); i++) {
            items.get(i).setCustomTexture(textures[i]);
        }
    }

    public void addOnItemSelectedListener(OnItemSelectedListener listener) {
        if (!listeners.contains(listener, true)) {
            listeners.add(listener);
        }
    }

    public void removeOnItemSelectedListener(OnItemSelectedListener listener) {
        listeners.removeValue(listener, true);
    }

    public void setBackgroundTexture(Texture texture) {
        this.backgroundTexture = texture;
        background.setDrawable(new TextureRegionDrawable(texture));
    }

    public void setDefaultItemTexture(Texture texture) {
        this.defaultItemTexture = texture;
        for (NavigationItem item : items) {
            if (!item.isSelected() && !item.hasCustomTexture()) {
                item.setDefaultTexture();
            }
        }
    }

    public void setSelectedItemTexture(Texture texture) {
        this.selectedItemTexture = texture;
        if (selectedItem != null) {
            selectedItem.setSelectedTexture();
        }
    }

    public void setSelectionIndicatorTexture(Texture texture) {
        this.defaultIndicatorTexture = texture;
        selectionIndicator.setDrawable(new TextureRegionDrawable(texture));
    }

    public void setAnimationDuration(float duration) {
        this.animationDuration = duration;
    }

    public void setSelectedScale(float scale) {
        this.selectedScale = scale;
        if (selectionIndicator != null && itemDisplaySize > 0) {
            float indicatorSize = Math.min(itemDisplaySize * scale, getHeight() - indicatorGap * 2);
            selectionIndicator.setSize(indicatorSize, indicatorSize);
        }
    }

    public void setSelectedYOffset(float offset) {
        this.selectedYOffset = offset;
    }

    public void setIndicatorGap(float gap) {
        this.indicatorGap = gap;
    }

    public void setItemSizeRatio(float ratio) {
        // Ratio should be between 0.5 and 0.65 (50-65%)
        this.itemSizeRatio = Math.min(0.65f, Math.max(0.5f, ratio));
        if (getWidth() > 0 && getHeight() > 0) {
            setSize(getWidth(), getHeight()); // Recalculate sizes
        }
    }

    public int getSelectedIndex() {
        return selectedItem != null ? selectedItem.getIndex() : -1;
    }

    public void clearListeners() {
        listeners.clear();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    private class NavigationItem extends Group {

        private Image defaultImage;
        public Group customGroup;
        private Image boundaryImage; // now used as overlay
        private Image customImage;

        private int index;
        private boolean isSelected;
        private boolean hasCustomTexture;

        private float originalY;
        private float displaySize;
        private float displayHeight;
        private float displayWidth;

        public NavigationItem(int index) {
            this.index = index;
            this.isSelected = false;
            this.hasCustomTexture = false;
            this.displaySize = 0;
            this.displayHeight = 0;
            this.displayWidth = 0;

            customGroup = new Group();
            customGroup.setTransform(true); // IMPORTANT for scaling

            buildImages();
        }

        private void buildImages() {

            // 🔵 Overlay (replaces boundary texture safely)
            boundaryImage = new Image(whiteTexture);
            boundaryImage.setColor(1f, 1f, 1f, 0.3f); // transparent white
            boundaryImage.setOrigin(Align.center);
            addActor(boundaryImage);

            // Content group (ONLY this scales)
            addActor(customGroup);

            // Default image
            defaultImage = new Image(new TextureRegionDrawable(new TextureRegion(defaultItemTexture)));
            defaultImage.setOrigin(Align.center);
            customGroup.addActor(defaultImage);
        }

        public void setDisplaySize(float itemWidth, float itemHeight) {
            this.displayHeight = itemHeight;
            this.displayWidth = itemWidth;

            customGroup.setSize(itemWidth, itemHeight);

            this.displaySize = Math.min(itemWidth, itemHeight) * itemSizeRatio;

            // ✅ Proper centering (FIXED)
            float iconX = (itemWidth - displaySize) / 2f;
            float iconY = (itemHeight - displaySize) / 2f;

            // Overlay fills item area
            if (boundaryImage != null) {
                float lineWidth = 2f; // tweak (1–3px depending on density)

                boundaryImage.setSize(lineWidth, itemHeight * 0.95f);
                boundaryImage.setPosition(itemWidth - lineWidth / 2f, 0);
            }

            if (defaultImage != null) {
                defaultImage.setSize(displaySize, displaySize);
                defaultImage.setPosition(iconX, iconY);
            }

            if (customImage != null) {
                customImage.setSize(displaySize, displaySize);
                customImage.setPosition(iconX, iconY);
            }

            // ✅ Ensure scaling happens from center
            customGroup.setOrigin(itemWidth / 2f, itemHeight / 2f);
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
        }
        @Override
        public void act(float delta) {
            super.act(delta);

            if (boundaryImage != null) {
                // keep boundary fixed relative to parent (cancel vertical movement)
                boundaryImage.setY(-getY());
            }
        }

        public void setDefaultTexture() {
            if (hasCustomTexture && customImage != null && !isSelected) {
                customImage.setDrawable(new TextureRegionDrawable(new TextureRegion(defaultItemTexture)));
            } else {
                defaultImage.setDrawable(new TextureRegionDrawable(new TextureRegion(defaultItemTexture)));
            }
        }

        public void setSelectedTexture() {
            // No-op now (overlay replaces boundary texture behavior)
        }

        public void setCustomTexture(Texture texture) {
            if (customImage == null) {
                customImage = new Image(new TextureRegionDrawable(new TextureRegion(texture)));
                customImage.setOrigin(Align.center);
                customGroup.addActor(customImage); // ✅ stays inside scalable group
            } else {
                customImage.setDrawable(new TextureRegionDrawable(new TextureRegion(texture)));
            }

            hasCustomTexture = true;
            defaultImage.setVisible(false);

            if (!isSelected) {
                customImage.setVisible(true);
            }

            if (displaySize > 0) {
                float iconX = (displayWidth - displaySize) / 2f;
                float iconY = (displayHeight - displaySize) / 2f;

                customImage.setSize(displaySize, displaySize);
                customImage.setPosition(iconX, iconY);
            }
        }

        public boolean hasCustomTexture() {
            return hasCustomTexture;
        }

        public int getIndex() {
            return index;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public float getOriginalY() {
            return originalY;
        }

        @Override
        public void setPosition(float x, float y) {
            super.setPosition(x, y);
            this.originalY = y;
        }

        @Override
        public void setSize(float width, float height) {
            super.setSize(width, height);
            // Intentionally empty (layout handled in setDisplaySize)
        }
    }
}
