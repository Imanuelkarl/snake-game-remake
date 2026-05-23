package com.regensnakevsblock.sbb.ui.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class ListManager<T, V extends ListItemView<T> & BaseView> extends Group {
    private BaseAdapter<T, V> adapter;
    private float itemHeight = 0;
    private float itemWidth = 0;
    private int columns = 1;
    private float lastTouchY = 0;
    private Group content ;

    public ListManager(BaseAdapter<T, V> adapter) {
        content = new Group();
        addActor(content);
        setAdapter(adapter);
        setupScrollInput();

    }
    private void setupScrollInput() {
        ListManager<T, V> listManager =this;
        addListener(new InputListener() {

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                lastTouchY = y;

                return true; // MUST return true to receive drag
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                float deltaY = y - lastTouchY;
                System.out.println("List manager y is "+ listManager.getY());
                scrollBy(deltaY);
                lastTouchY = y;

            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if(content.getY()<0){
                    content.setY(0);
                }
                lastTouchY=0;
            }
        });
    }

    public void setAdapter(BaseAdapter<T, V> adapter) {
        this.adapter = adapter;
        adapter.setDataChangeListener(this::bindDataAndUpdate);
        renderAllItems();
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
    }
    public void scrollBy(float delta) {
        float maxScroll = Math.max(0, getTotalContentHeight() - getHeight());

        float newY =content.getY()+ delta;
        if(newY< -100) newY=-100;
        System.out.println(maxScroll);
        if (newY > maxScroll) newY = maxScroll;
        content.setY(newY);


    }
    public void renderAllItems() {
        content.clearChildren();
        for (V item : adapter.getViews()) {
            positionItem(item, item.getPosition());
            content.addActor(item.getRoot());
        }
    }
    private float getTotalContentHeight() {
        int totalRows = (int) Math.ceil((float) adapter.getCount() / columns);
        return totalRows * itemHeight;
    }
    private void positionItem(V item, int position) {
        int row = position / columns;
        int col = position % columns;
        float x = col * itemWidth;
        float y = getHeight() - (row * itemHeight) - itemHeight;
        item.getRoot().setPosition(x, y);
        item.getRoot().setSize(itemWidth, itemHeight);
    }
    @Override
    public void act(float delta) {
        super.act(delta);
        for (V item : adapter.getViews()) {
            item.update(delta);
        }
    }

    public void onResize(float width, float height) {
        setSize(width, height);

        // Recalculate item width if needed
        if (itemHeight > 0 && itemWidth == 0 && columns > 0) {
            itemWidth = width / columns;
        }
        for (int i = 0; i < adapter.getViews().size(); i++) {
            adapter.getViews().get(i).resize(itemWidth, itemHeight);
        }
        renderAllItems();
    }

}
