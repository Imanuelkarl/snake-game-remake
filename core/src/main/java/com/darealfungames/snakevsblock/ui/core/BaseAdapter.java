package com.darealfungames.snakevsblock.ui.core;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, V extends BaseView> {
    protected List<T> items = new ArrayList<>();
    private final List<V> views = new ArrayList<>();
    protected OnItemClickListener<T> itemClickListener;
    protected OnItemLongClickListener<T> itemLongClickListener;

    public List<V> getViews() {
        return views;
    }

    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(T item, int position);
    }
    public interface DataChangeListener {
        void onDataChanged();
    }

    public V getView(int position) {
        return views.get(position);
    }

    private DataChangeListener dataChangeListener;
    public void setDataChangeListener(DataChangeListener listener) {
        this.dataChangeListener = listener;
    }

    /*public void setData(List<T> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);

        }
        if (dataChangeListener != null) {
            dataChangeListener.onDataChanged();
        }
        notifyDataSetChanged();
    }*/
    public void setData(List<T> newItems) {
        this.items.clear();
        this.items.addAll(newItems);

        // Clear existing views
        views.clear();

        // Create view for every item
        for (int i = 0; i < items.size(); i++) {
            V view = this.createView(i);
            bindView(view, i); // bind initial data
            views.add(view);

            // Track creation (logging, analytics, etc.)
            System.out.println("Created view for item " + i);
        }

        // Notify listener
        if (dataChangeListener != null) dataChangeListener.onDataChanged();
    }

    public void addItem(T item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        items.remove(position);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public int getCount() {
        return items.size();
    }

    public abstract V createView(int position);
    public abstract void bindView(V view, int position);
    public abstract void notifyDataSetChanged();

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
        this.itemLongClickListener = listener;
    }
}
