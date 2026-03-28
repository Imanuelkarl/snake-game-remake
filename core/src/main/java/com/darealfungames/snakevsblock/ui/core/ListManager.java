package com.darealfungames.snakevsblock.ui.core;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ListManager<T, V extends ListItemView<T> & BaseView> extends Group {
    private BaseAdapter<T, V> adapter;
    private ListLayout layout;
    private Array<V> visibleItems = new Array<>();
    private Pool<V> viewPool;
    private int firstVisiblePosition = 0;
    private int lastVisiblePosition = -1;
    private float scrollY = 0;
    private float itemHeight = 0;
    private float itemWidth = 0;
    private boolean isVertical = true;
    private int columns = 1;

    public enum ListLayout {
        VERTICAL, GRID
    }

    public ListManager(BaseAdapter<T, V> adapter) {
        setAdapter(adapter);
        setupScrollInput();
    }

    private void setupScrollInput() {
        addListener(new ClickListener() {
            private float lastY;
            private float lastX;
            private boolean dragging = false;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lastY = y;
                lastX = x;
                dragging = true;
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (dragging) {
                    float deltaY = y - lastY;
                    float deltaX = x - lastX;

                    if (isVertical) {
                        scrollBy(-deltaY);
                    } else {
                        scrollBy(-deltaX);
                    }

                    lastY = y;
                    lastX = x;
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dragging = false;
            }
        });
    }

    public void setAdapter(BaseAdapter<T, V> adapter) {
        this.adapter = adapter;
        this.viewPool = new Pool<V>() {
            @Override
            protected V newObject() {
                return adapter.createView(0);
            }
        };
        reload();
    }

    public void setLayout(ListLayout layout, int columns, float itemWidth, float itemHeight) {
        this.isVertical = layout == ListLayout.VERTICAL;
        this.columns = layout == ListLayout.GRID ? columns : 1;
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        reload();
        if (getWidth() > 0 && getHeight() > 0) {
            updateVisibleItems();
        }
    }
    public void setLayout(ListLayout layout, int columns, float itemHeight) {
        this.isVertical = layout == ListLayout.VERTICAL;
        this.columns = layout == ListLayout.GRID ? columns : 1;
        this.itemWidth = getWidth()/columns;
        this.itemHeight = itemHeight;
        reload();
        if (getWidth() > 0 && getHeight() > 0) {
            updateVisibleItems();
        }
    }


    public void scrollBy(float delta) {
        float maxScroll = getMaxScroll();
        scrollY = Math.max(0, Math.min(maxScroll, scrollY + delta));
        updateVisibleItems();
    }

    private float getMaxScroll() {
        int totalItems = adapter.getCount();
        int totalRows = isVertical ? totalItems : (int) Math.ceil((float) totalItems / columns);
        float contentHeight = totalRows * itemHeight;
        return Math.max(0, contentHeight - getHeight());
    }

    private void updateVisibleItems() {
        int startRow = (int) (scrollY / itemHeight);
        int endRow = (int) ((scrollY + getHeight()) / itemHeight) + 1;

        int startIndex = startRow * columns;
        int endIndex = Math.min(endRow * columns, adapter.getCount());

        // Recycle views that are no longer visible
        for (int i = 0; i < visibleItems.size; i++) {
            V item = visibleItems.get(i);
            int position = item.getPosition();
            if (position < startIndex || position >= endIndex) {
                visibleItems.removeIndex(i);
                removeActor(item.getRoot());
                item.recycle();
                viewPool.free(item);
                i--;
            }
        }

        // Add new visible items
        for (int position = startIndex; position < endIndex; position++) {
            if (!isItemVisible(position)) {
                V item = viewPool.obtain();
                item.bind(adapter.getItem(position), position);
                positionItem(item, position);
                visibleItems.add(item);
                addActor(item.getRoot());

                final int finalPosition = position;
                item.getRoot().addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        if (adapter.itemClickListener != null) {
                            adapter.itemClickListener.onItemClick(
                                adapter.getItem(finalPosition), finalPosition
                            );
                        }
                    }


                    public boolean longPress(float x, float y) {
                        if (adapter.itemLongClickListener != null) {
                            return adapter.itemLongClickListener.onItemLongClick(
                                adapter.getItem(finalPosition), finalPosition
                            );
                        }
                        return false;
                    }
                });
            }
        }
    }

    private void positionItem(V item, int position) {
        int row = position / columns;
        int col = position % columns;
        float x = col * itemWidth;
        float y = getHeight() - (row * itemHeight) - itemHeight + scrollY;

        item.getRoot().setPosition(x, y);
        item.getRoot().setSize(itemWidth, itemHeight);
    }

    private boolean isItemVisible(int position) {
        for (V item : visibleItems) {
            if (item.getPosition() == position) {
                return true;
            }
        }
        return false;
    }

    public void reload() {
        clearChildren();
        visibleItems.clear();
        scrollY = 0;
        updateVisibleItems();
    }

    public void notifyDataSetChanged() {
        reload();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        for (V item : visibleItems) {
            item.update(delta);
        }
    }

    public void onResize(float width, float height) {
        setSize(width, height);
        updateVisibleItems();
    }
}
