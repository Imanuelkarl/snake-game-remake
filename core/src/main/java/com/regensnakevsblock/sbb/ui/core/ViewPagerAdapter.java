package com.regensnakevsblock.sbb.ui.core;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter {
    private List<BaseView> views = new ArrayList<>();
    private OnPageChangeListener pageChangeListener;

    public interface OnPageChangeListener {
        void onPageSelected(int position);
        void onPageScrolled(int position, float positionOffset);
    }

    public void addView(BaseView view) {
        views.add(view);
    }

    public void removeView(BaseView view) {
        views.remove(view);
    }

    public BaseView getView(int position) {
        return views.get(position);
    }

    public int getCount() {
        return views.size();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.pageChangeListener = listener;
    }

    public void notifyPageSelected(int position) {
        if (pageChangeListener != null) {
            pageChangeListener.onPageSelected(position);
        }
    }

    public void notifyPageScrolled(int position, float offset) {
        if (pageChangeListener != null) {
            pageChangeListener.onPageScrolled(position, offset);
        }
    }
}
