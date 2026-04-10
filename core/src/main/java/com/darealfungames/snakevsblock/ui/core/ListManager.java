package com.darealfungames.snakevsblock.ui.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ListManager<T, V extends ListItemView<T> & BaseView> extends Group {
    private BaseAdapter<T, V> adapter;
    private Array<V> visibleItems = new Array<>();
    private Array<V> allItems = new Array<>();
    private Pool<V> viewPool;
    private int firstVisiblePosition = 0;
    private int lastVisiblePosition = -1;
    private float scrollY = 0;
    private float itemHeight = 0;
    private float itemWidth = 0;
    private int columns = 1;
    private float lastTouchY = 0;
    private boolean isDragging = false;

    private float totalContentHeight = 0;

    public ListManager(BaseAdapter<T, V> adapter) {
        setAdapter(adapter);
        setupScrollInput();
    }

    private void setupScrollInput() {
        ListManager<T, V> listManager =this;
        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lastTouchY = y;
                isDragging = true;
                return true; // MUST return true to receive drag
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float deltaY = y - lastTouchY;

                scrollBy(deltaY);
                lastTouchY = y;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDragging = false;
                if(listManager.getY()<0){
                    listManager.setY(0);
                }
            }
        });
    }

    public void setAdapter(BaseAdapter<T, V> adapter) {
        this.adapter = adapter;
        this.viewPool = new Pool<V>() {
            @Override
            protected V newObject() {
                return createItemView(0);

            }
        };
        // or smarter update
        adapter.setDataChangeListener(this::bindDataAndUpdate);

        reload();
    }

    public void setLayout(int columns, float itemWidth, float itemHeight) {
        this.columns = Math.max(1, columns);
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        //reload();
    }
    public void bindDataAndUpdate(){
        for (int i = 0; i <adapter.getViews().size(); i++){
            V item = adapter.getView(i);
            item.bind(adapter.getItem(i), i);
        }
        renderAllItems();
    }

    public void setLayout(int columns, float itemHeight) {
        this.columns = Math.max(1, columns);
        this.itemHeight = itemHeight;
        if (getWidth() > 0) {
            this.itemWidth = getWidth() / this.columns;
        }
        //reload();
    }

    /*public void scrollBy(float delta) {
        float maxScroll = getMaxScroll();
        float newScrollY = scrollY + delta;

        // Apply bounds checking with smooth limits
        if (newScrollY < 0) {
            newScrollY = 0;
        } else if (newScrollY > maxScroll) {
            newScrollY = maxScroll;
        }

        if (Math.abs(scrollY - newScrollY) > 0.01f) {
            scrollY = newScrollY;
            updateVisibleItems();
        }
    }*/
    public void scrollBy(float delta) {

        float maxScroll = Math.max(0, getTotalContentHeight() - getHeight());
        float newY =this.getY()+delta;
        if (newY > maxScroll) {
            newY = maxScroll;
        }
        this.setY(newY);


    }

    private float getMaxScroll() {
        if (adapter.getCount() == 0) return 0;

        int totalRows = (int) Math.ceil((float) adapter.getCount() / columns);
        totalContentHeight = totalRows * itemHeight;

        float viewHeight = getHeight();
        if (viewHeight <= 0) return 0;

        return Math.max(0, totalContentHeight - viewHeight);
    }


    private void updateVisibleItems() {
        if (adapter.getCount() == 0 || getHeight() <= 0 || itemHeight <= 0) return;

        int totalRows = (int) Math.ceil((float) adapter.getCount() / columns);

        // calculate visible rows
        int startRow = Math.max(0, (int) (scrollY / itemHeight));
        int endRow = Math.min(
            (int) Math.ceil((scrollY + getHeight()) / itemHeight),
            totalRows
        );

        int startIndex = startRow * columns;
        int endIndex = Math.min(endRow * columns, adapter.getCount());

        // Step 1: mark which positions should stay
        boolean[] stillVisible = new boolean[adapter.getCount()];
        for (int i = startIndex; i < endIndex; i++) stillVisible[i] = true;

        // Step 2: recycle items no longer visible
        for (int i = visibleItems.size - 1; i >= 0; i--) {
            V item = visibleItems.get(i);
            int pos = item.getPosition();
            if (!stillVisible[pos]) {
                visibleItems.removeIndex(i);
                removeActor(item.getRoot());
                item.recycle();
                viewPool.free(item);
            }
        }

        // Step 3: add new visible items
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
                            adapter.itemClickListener.onItemClick(adapter.getItem(finalPosition), finalPosition);
                        }
                    }
                });
            } else {
                // update existing item positions if scrollY changed
                for (V item : visibleItems) {
                    if (item.getPosition() == position) positionItem(item, position);
                }
            }
        }

        // Step 4: update first/last visible positions
        firstVisiblePosition = startIndex;
        lastVisiblePosition = endIndex - 1;
    }
    public void renderAllItems() {
        clearChildren(); // remove from stage
        for (V item : adapter.getViews()) {
            positionItem(item, item.getPosition());
            addActor(item.getRoot());
        }
    }

    private void recycleAllItems() {
        for (int i = visibleItems.size - 1; i >= 0; i--) {
            V item = visibleItems.get(i);
            visibleItems.removeIndex(i);
            removeActor(item.getRoot());
            item.recycle();
            viewPool.free(item);
        }
    }
    private float getTotalContentHeight() {
        int totalRows = (int) Math.ceil((float) adapter.getCount() / columns);
        return totalRows * itemHeight;
    }
    /**
     * Fast layout: just display the items that fit on screen.
     * Call this when data is first set or completely changed.
     */
    public void layoutVisibleItems() {
        System.out.println("Starting layoutVisibleItems: adapter count=" + adapter.getCount() + ", view height=" + getHeight() + ", item height=" + itemHeight);
        if (adapter.getCount() == 0 || getHeight() <= 0 || itemHeight <= 0) return;

        // Clear any existing actors
        recycleAllItems();

        // Calculate how many rows can fit on screen
        int rowsOnScreen = (int) Math.ceil(getHeight() / itemHeight) + 1; // +1 for partial row
        int itemsOnScreen = Math.min(adapter.getCount(), rowsOnScreen * columns);

        visibleItems.clear();

        for (int position = 0; position < itemsOnScreen; position++) {
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
                        adapter.itemClickListener.onItemClick(adapter.getItem(finalPosition), finalPosition);
                    }
                }
            });
        }

        firstVisiblePosition = 0;
        lastVisiblePosition = itemsOnScreen - 1;
        scrollY = 0;
        System.out.println("Finished layoutVisibleItems: firstVisiblePosition=" + firstVisiblePosition + ", lastVisiblePosition=" + lastVisiblePosition + ", visibleItems=" + visibleItems.size);
    }

    private int getMinVisiblePosition() {
        int min = Integer.MAX_VALUE;
        for (V item : visibleItems) {
            min = Math.min(min, item.getPosition());
        }
        return min == Integer.MAX_VALUE ? 0 : min;
    }

    private int getMaxVisiblePosition() {
        int max = -1;
        for (V item : visibleItems) {
            max = Math.max(max, item.getPosition());
        }
        return max;
    }

    private void positionItem(V item, int position) {
        int row = position / columns;
        int col = position % columns;
        float x = col * itemWidth;
        float y = getHeight() - (row * itemHeight) - itemHeight - scrollY;

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
        recycleAllItems();
        scrollY = 0;
        lastVisiblePosition = -1;
        firstVisiblePosition = 0;
        if (getWidth() > 0 && getHeight() > 0 && itemHeight > 0) {
            updateVisibleItems();

        }
    }
    // In your list manager class
    private V createItemView(int position) {
        long startTime = System.currentTimeMillis();

        V item = adapter.createView(position); // actually create the view
        allItems.add(item);                    // track it in allItems

        long endTime = System.currentTimeMillis();
        Gdx.app.log("ListManager", "Created item at position " + position + " in " + (endTime - startTime) + " ms");

        return item;
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

        // Recalculate item width if needed
        if (itemHeight > 0 && itemWidth == 0 && columns > 0) {
            itemWidth = width / columns;
        }

        // Force a complete refresh on resize
        if (adapter != null && adapter.getCount() > 0) {
            float oldScrollY = scrollY;
            updateVisibleItems();
            // Ensure scroll position stays valid after resize
            //scrollY = Math.min(scrollY, getMaxScroll());
            if (Math.abs(oldScrollY - scrollY) > 0.01f) {
                updateVisibleItems();
            }
        }
    }

}
