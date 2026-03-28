package com.darealfungames.snakevsblock.ui.core;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter<T, V extends BaseView> {
    protected List<T> items = new ArrayList<>();
    protected OnItemClickListener<T> itemClickListener;
    protected OnItemLongClickListener<T> itemLongClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(T item, int position);
    }

    public interface OnItemLongClickListener<T> {
        boolean onItemLongClick(T item, int position);
    }

    public void setData(List<T> items) {
        this.items.clear();
        if (items != null) {
            this.items.addAll(items);
        }
        notifyDataSetChanged();
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
