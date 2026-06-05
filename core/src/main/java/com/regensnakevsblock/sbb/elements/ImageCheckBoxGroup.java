package com.regensnakevsblock.sbb.elements;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;

public class ImageCheckBoxGroup extends Group {

    public enum LayoutType {
        VERTICAL,
        HORIZONTAL,
        GRID
    }

    public enum SelectionMode {
        SINGLE,
        MULTIPLE
    }

    public static class Item {

        private final TextureRegion labelRegion;
        private boolean checked;
        private float customWidth = -1;
        private float customHeight = -1;

        private final Rectangle bounds = new Rectangle();

        public Item(TextureRegion labelRegion) {
            this.labelRegion = labelRegion;
        }

        public TextureRegion getLabelRegion() {
            return labelRegion;
        }

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public Rectangle getBounds() {
            return bounds;
        }

        public void setSize(float width, float height) {
            this.customWidth = width;
            this.customHeight = height;
        }

        public float getWidth() {
            return customWidth > 0 ? customWidth : labelRegion.getRegionWidth();
        }

        public float getHeight() {
            return customHeight > 0 ? customHeight : labelRegion.getRegionHeight();
        }
    }

    public interface SelectionListener {
        void onItemChanged(int index, Item item);
        void onItemChecked(int index, Item item);
        void onItemUnchecked(int index, Item item);
    }

    private final Array<Item> items = new Array<>();

    private final TextureRegion checkedRegion;
    private final TextureRegion uncheckedRegion;

    private LayoutType layoutType = LayoutType.VERTICAL;
    private SelectionMode selectionMode = SelectionMode.MULTIPLE;

    private SelectionListener listener;

    private boolean layoutDirty = true;

    private float spacing = 10f;
    private float checkboxSpacing = 10f;

    private float checkboxSize = 32f;
    private boolean useTextureSize = true;

    private float itemWidth = -1;
    private float itemHeight = -1;

    private float frameWidth = 200f; // Fixed frame width for each item
    private float frameHeight = 60f; // Fixed frame height for each item

    private int columns = 3;

    public ImageCheckBoxGroup(
        TextureRegion checkedRegion,
        TextureRegion uncheckedRegion) {

        this.checkedRegion = checkedRegion;
        this.uncheckedRegion = uncheckedRegion;

        if (checkedRegion != null) {
            this.checkboxSize = checkedRegion.getRegionWidth();
        }

        addListener(new InputListener() {

            @Override
            public boolean touchDown(
                InputEvent event,
                float x,
                float y,
                int pointer,
                int button) {

                handleClick(x, y);
                return true;
            }
        });
    }

    public void setCheckboxSize(float size) {
        this.checkboxSize = size;
        this.useTextureSize = false;
        invalidateLayout();
    }

    public void resetCheckboxToTextureSize() {
        if (checkedRegion != null) {
            this.checkboxSize = checkedRegion.getRegionWidth();
        }
        this.useTextureSize = true;
        invalidateLayout();
    }

    public void setItemSize(float width, float height) {
        this.itemWidth = width;
        this.itemHeight = height;
        invalidateLayout();
    }

    public void resetItemsToTextureSize() {
        this.itemWidth = -1;
        this.itemHeight = -1;
        invalidateLayout();
    }

    public void setItemSize(int index, float width, float height) {
        if (index >= 0 && index < items.size) {
            items.get(index).setSize(width, height);
            invalidateLayout();
        }
    }

    public void setFrameSize(float width, float height) {
        this.frameWidth = width;
        this.frameHeight = height;
        invalidateLayout();
    }

    public float getCheckboxSize() {
        return checkboxSize;
    }

    private float getItemWidth(Item item) {
        return itemWidth > 0 ? itemWidth : item.getWidth();
    }

    private float getItemHeight(Item item) {
        return itemHeight > 0 ? itemHeight : item.getHeight();
    }

    public void addItem(TextureRegion labelRegion) {
        Item item = new Item(labelRegion);

        if (selectionMode == SelectionMode.SINGLE && items.size == 0) {
            item.setChecked(true);
        }

        items.add(item);
        invalidateLayout();
    }

    public void removeItem(int index) {
        items.removeIndex(index);

        if (selectionMode == SelectionMode.SINGLE && items.size > 0) {
            boolean hasChecked = false;
            for (Item item : items) {
                if (item.isChecked()) {
                    hasChecked = true;
                    break;
                }
            }
            if (!hasChecked) {
                items.first().setChecked(true);
                if (listener != null) {
                    listener.onItemChecked(0, items.first());
                    listener.onItemChanged(0, items.first());
                }
            }
        }

        invalidateLayout();
    }

    public Array<Item> getItems() {
        return items;
    }

    public void clearItems() {
        items.clear();
        invalidateLayout();
    }

    public void setLayoutType(LayoutType layoutType) {
        this.layoutType = layoutType;
        invalidateLayout();
    }

    public LayoutType getLayoutType() {
        return layoutType;
    }

    public void setSelectionMode(SelectionMode selectionMode) {
        this.selectionMode = selectionMode;

        if (selectionMode == SelectionMode.SINGLE) {
            boolean foundChecked = false;
            for (Item item : items) {
                if (item.isChecked()) {
                    if (foundChecked) {
                        item.setChecked(false);
                    } else {
                        foundChecked = true;
                    }
                }
            }

            if (!foundChecked && items.size > 0) {
                items.first().setChecked(true);
                if (listener != null) {
                    listener.onItemChecked(0, items.first());
                    listener.onItemChanged(0, items.first());
                }
            }
        }

        invalidateLayout();
    }

    public SelectionMode getSelectionMode() {
        return selectionMode;
    }

    public void setCheckedItem(int index) {
        if (index < 0 || index >= items.size) return;

        if (selectionMode == SelectionMode.SINGLE) {
            for (int i = 0; i < items.size; i++) {
                Item item = items.get(i);
                if (item.isChecked() && i != index) {
                    item.setChecked(false);
                    if (listener != null) {
                        listener.onItemUnchecked(i, item);
                        listener.onItemChanged(i, item);
                    }
                }
            }

            Item targetItem = items.get(index);
            if (!targetItem.isChecked()) {
                targetItem.setChecked(true);
                if (listener != null) {
                    listener.onItemChecked(index, targetItem);
                    listener.onItemChanged(index, targetItem);
                }
            }
        } else {
            Item item = items.get(index);
            if (!item.isChecked()) {
                item.setChecked(true);
                if (listener != null) {
                    listener.onItemChecked(index, item);
                    listener.onItemChanged(index, item);
                }
            }
        }

        invalidateLayout();
    }

    public void setCheckedItem(Item item) {
        int index = items.indexOf(item, true);
        if (index != -1) {
            setCheckedItem(index);
        }
    }

    public int getCheckedIndex() {
        if (selectionMode == SelectionMode.SINGLE) {
            for (int i = 0; i < items.size; i++) {
                if (items.get(i).isChecked()) {
                    return i;
                }
            }
        }
        return -1;
    }

    public Item getCheckedItem() {
        int index = getCheckedIndex();
        return index != -1 ? items.get(index) : null;
    }

    public Array<Item> getCheckedItems() {
        Array<Item> checked = new Array<>();
        for (Item item : items) {
            if (item.isChecked()) {
                checked.add(item);
            }
        }
        return checked;
    }

    public void setColumns(int columns) {
        this.columns = Math.max(1, columns);
        invalidateLayout();
    }

    public void setSpacing(float spacing) {
        this.spacing = spacing;
        invalidateLayout();
    }

    public void setCheckboxSpacing(float checkboxSpacing) {
        this.checkboxSpacing = checkboxSpacing;
        invalidateLayout();
    }

    public void setSelectionListener(SelectionListener listener) {
        this.listener = listener;
    }

    public void invalidateLayout() {
        layoutDirty = true;
    }

    private void rebuildLayout() {

        if (!layoutDirty)
            return;

        layoutDirty = false;

        switch (layoutType) {

            case VERTICAL:
                float currentY = getHeight();

                for (Item item : items) {
                    // Each item gets the fixed frame width, but we use the full width available
                    float itemFrameWidth = getWidth();
                    float itemFrameHeight = frameHeight;

                    float labelWidth = Math.min(getItemWidth(item), itemFrameWidth - checkboxSize - checkboxSpacing);
                    float labelHeight = getItemHeight(item);

                    currentY -= itemFrameHeight;

                    item.bounds.set(0, currentY, itemFrameWidth, itemFrameHeight);

                    currentY -= spacing;
                }
                break;

            case HORIZONTAL:
                if (items.size == 0) break;

                float availableWidth = getWidth();
                float totalSpacing = spacing * (items.size - 1);
                float frameWidthPerItem = (availableWidth - totalSpacing) / items.size;

                float currentX = 0;

                for (Item item : items) {
                    float itemFrameHeight = frameHeight;

                    item.bounds.set(currentX, 0, frameWidthPerItem, itemFrameHeight);

                    currentX += frameWidthPerItem + spacing;
                }
                break;

            case GRID:
                if (items.size == 0 || columns == 0) break;

                int rows = (int) Math.ceil((float) items.size / columns);
                availableWidth = getWidth();
                totalSpacing = spacing * (columns - 1);
                frameWidthPerItem = (availableWidth - totalSpacing) / columns;
                float frameHeightPerItem = frameHeight;

                for (int i = 0; i < items.size; i++) {
                    Item item = items.get(i);

                    int row = i / columns;
                    int col = i % columns;

                    float x = col * (frameWidthPerItem + spacing);
                    float y = getHeight() - frameHeightPerItem - row * (frameHeightPerItem + spacing);

                    item.bounds.set(x, y, frameWidthPerItem, frameHeightPerItem);
                }
                break;
        }
    }

    private void handleClick(float x, float y) {

        rebuildLayout();

        for (int i = 0; i < items.size; i++) {

            Item item = items.get(i);

            if (item.bounds.contains(x, y)) {

                if (selectionMode == SelectionMode.SINGLE) {
                    if (!item.isChecked()) {
                        for (int j = 0; j < items.size; j++) {
                            Item otherItem = items.get(j);
                            if (otherItem.isChecked()) {
                                otherItem.setChecked(false);
                                if (listener != null && j != i) {
                                    listener.onItemUnchecked(j, otherItem);
                                    listener.onItemChanged(j, otherItem);
                                }
                            }
                        }

                        item.setChecked(true);

                        if (listener != null) {
                            listener.onItemChecked(i, item);
                            listener.onItemChanged(i, item);
                        }
                    }
                } else {
                    item.checked = !item.checked;

                    if (listener != null) {
                        if (item.isChecked()) {
                            listener.onItemChecked(i, item);
                        } else {
                            listener.onItemUnchecked(i, item);
                        }
                        listener.onItemChanged(i, item);
                    }
                }

                break;
            }
        }
    }

    @Override
    protected void sizeChanged() {
        super.sizeChanged();
        invalidateLayout();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        rebuildLayout();

        for (Item item : items) {

            TextureRegion checkbox = item.checked ? checkedRegion : uncheckedRegion;

            float drawX = getX() + item.bounds.x;
            float drawY = getY() + item.bounds.y;
            float frameW = item.bounds.width;
            float frameH = item.bounds.height;

            float availableLabelWidth = frameW - checkboxSize - checkboxSpacing;
            float labelWidth = Math.min(getItemWidth(item), availableLabelWidth);
            float labelHeight = getItemHeight(item);

            // Center checkbox vertically in frame
            float checkboxDrawY = drawY + (frameH - checkboxSize) / 2;

            // Draw checkbox
            batch.draw(checkbox, drawX, checkboxDrawY, checkboxSize, checkboxSize);

            // Center label vertically in frame
            float labelDrawY = drawY + (frameH - labelHeight) / 2;

            // Scale label if needed to fit (maintain aspect ratio)
            float drawLabelWidth = labelWidth;
            float drawLabelHeight = labelHeight;

            if (labelWidth > availableLabelWidth) {
                float ratio = availableLabelWidth / labelWidth;
                drawLabelWidth = availableLabelWidth;
                drawLabelHeight = labelHeight * ratio;
                labelDrawY = drawY + (frameH - drawLabelHeight) / 2;
            }

            // Draw label
            batch.draw(
                item.labelRegion,
                drawX + checkboxSize + checkboxSpacing,
                labelDrawY,
                drawLabelWidth,
                drawLabelHeight);
        }
    }
}
