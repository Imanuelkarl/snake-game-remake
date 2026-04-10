package com.regensnakevsblock.sbb.ui.core;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.SnapshotArray;

public class ViewManager extends Group {
    private ViewPagerAdapter adapter;
    private int currentPosition = 0;
    private float dragStartX = 0;
    private boolean dragging = false;
    private float dragThreshold = 250; // pixels
    private float initialX;
    private float swipeVelocity = 0;
    private float dragStartY = 0;
    private boolean pendingReload = false;
    private boolean isHorizontalScroll = false;
    private static final float SWIPE_TIME = 0.3f;
    private static final float SWIPE_DISTANCE = 300;
    private float scrollX = 0;
    private float startScrollX = 0;

    private OnPageChangeListener pageChangeListener;

    public void resize(float width, float v) {
        this.setSize(width, v);
        if (adapter != null && adapter.getCount() > 0) {
            layoutPages();
        }
    }

    public interface OnPageChangeListener {
        void onPageSelected(int position);
        void onPageChanged(int oldPosition, int newPosition);
    }

    public ViewManager() {
        setupInput();
    }

    private void setupInput() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (adapter == null) return false;

                startScrollX = scrollX;
                dragStartX = x;
                dragStartY = y;
                dragging = true;
                isHorizontalScroll = false; // Reset
                return true;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (!dragging) return;

                // Determine scroll direction if not yet determined
                if (!isHorizontalScroll) {
                    float dx = Math.abs(x - dragStartX);
                    float dy = Math.abs(y - dragStartY);

                    if (dx > dy && dx > 10) { // 10px threshold
                        isHorizontalScroll = true;
                    } else if (dy > dx && dy > 10) {
                        isHorizontalScroll = false;
                        // If vertical, stop processing horizontal drag
                        return;
                    }
                }

                // Only process horizontal drag if we determined it's horizontal
                if (!isHorizontalScroll) return;

                float delta = x - dragStartX;
                scrollX = startScrollX - delta;

                float maxScroll = (adapter.getCount() - 1) * getWidth();
                scrollX = Math.max(0, Math.min(maxScroll, scrollX));

                layoutPages();
            }
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!dragging) return;

                dragging = false;

                float delta = x - dragStartX;
                completeSwipe(delta);
            }
        });
    }
    private void layoutPages() {
        float width = getWidth();
        float height = getHeight();

        for (int i = 0; i < adapter.getCount(); i++) {
            BaseView view = adapter.getView(i);
            if (view.getRoot().getParent() == null) {
                addActor(view.getRoot());
            }

            float x = (i * width) - scrollX;

            // <<< THIS WAS MISSING >>>
            view.getRoot().setSize(width, height);
            view.getRoot().setPosition(x, 0);
            view.resize(width,height);
        }
    }
    private void completeSwipe(float delta) {
        float width = getWidth();

        int targetPage = Math.round(scrollX / width);

        if (Math.abs(delta) > dragThreshold) {
            if (delta < 0) targetPage++;
            else targetPage--;
        }

        targetPage = Math.max(0, Math.min(adapter.getCount() - 1, targetPage));

        animateToPage(targetPage);
    }
    private void animateToPage(int page) {
        final float start = scrollX;
        final float target = page * getWidth();
        final float distance = target - start;

        addAction(new com.badlogic.gdx.scenes.scene2d.Action() {
            float time = 0;

            @Override
            public boolean act(float delta) {
                time += delta;
                float alpha = Math.min(time / SWIPE_TIME, 1f);

                // Smooth interpolation
                scrollX = start + distance * alpha;
                layoutPages();

                if (alpha >= 1f) {
                    int old = currentPosition;
                    currentPosition = page;

                    adapter.notifyPageSelected(page);

                    if (pageChangeListener != null) {
                        pageChangeListener.onPageChanged(old, page);
                    }

                    return true; // finished
                }
                return false; // continue
            }
        });
    }
    public void setSize(float width, float height) {
        super.setSize(width, height);
        if (adapter != null && adapter.getCount() > 0) {
            layoutPages();
        }
    }
    public void reload() {
        if (adapter == null) return;

        // If size not ready, defer
        if (getWidth() == 0 || getHeight() == 0) {
            pendingReload = true;
            return;
        }

        pendingReload = false;

        clearChildren();

        scrollX = currentPosition * getWidth();

        for (int i = 0; i < adapter.getCount(); i++) {
            BaseView view = adapter.getView(i);
            addActor(view.getRoot());
        }

        layoutPages();

        adapter.notifyPageSelected(currentPosition);

        if (pageChangeListener != null) {
            pageChangeListener.onPageChanged(-1, currentPosition);
        }
    }

    private float getChildX(int position) {
        BaseView view = adapter.getView(position);
        if (view != null && view.getRoot().getParent() != null) {
            return view.getRoot().getX();
        }
        return 0;
    }

    public void setAdapter(ViewPagerAdapter adapter) {
        this.adapter = adapter;
        clearChildren();
        System.out.println("Adapter set with " + adapter.getCount() + " pages.");
        if (adapter.getCount() > 0) {
            currentPosition = 0;
            scrollX = 0;
            layoutPages();
            adapter.getView(0).onResume();
            adapter.notifyPageSelected(0);               // add
            if (pageChangeListener != null) {
                pageChangeListener.onPageChanged(-1, 0); // optional
            }
        }
    }

    @Override
    public void setPosition(float x, float y) {
        super.setPosition(x, y);
        if (adapter != null && adapter.getCount() > 0) {
            layoutPages();
        }
    }

    public void setCurrentItem(int position, boolean animate) {
        if (position < 0 || position >= adapter.getCount()) return;


        if (animate) {
            animateToPage(position);
        } else {
            currentPosition = position;
            scrollX = position * getWidth();

            layoutPages();
            adapter.notifyPageSelected(position);

        }
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.pageChangeListener = listener;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (adapter != null) {
            for (int i = 0; i < adapter.getCount(); i++) {
                BaseView view = adapter.getView(i);

                if (view.getRoot().getParent() != null) {

                    view.update(delta);
                }
            }
        }
    }

    public boolean backPressed() {
        if (adapter != null && adapter.getView(currentPosition) != null) {
            return adapter.getView(currentPosition).backPressed();
        }
        return false;
    }

}
