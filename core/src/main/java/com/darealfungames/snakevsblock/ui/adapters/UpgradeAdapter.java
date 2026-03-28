package com.darealfungames.snakevsblock.ui.adapters;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.darealfungames.snakevsblock.ui.cards.UpgradeCard;
import com.darealfungames.snakevsblock.ui.core.BaseAdapter;
import com.darealfungames.snakevsblock.ui.data.UpgradeData;

public class UpgradeAdapter extends BaseAdapter<UpgradeData, UpgradeCard> {
    private OnUpgradeListener upgradeListener;

    public interface OnUpgradeListener {
        void onUpgrade(UpgradeData upgrade, int position);
    }

    @Override
    public UpgradeCard createView(int position) {
        UpgradeCard card = new UpgradeCard();
        card.setSize(300, 100);
        return card;
    }

    @Override
    public void bindView(UpgradeCard view, int position) {
        view.bind(getItem(position), position);

        // Add click listener for upgrade
        view.getRoot().addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (upgradeListener != null &&
                    getItem(position).currentLevel < getItem(position).maxLevel) {
                    upgradeListener.onUpgrade(getItem(position), position);
                }
            }
        });
    }

    @Override
    public void notifyDataSetChanged() {
        // Implementation
    }

    public void setOnUpgradeListener(OnUpgradeListener listener) {
        this.upgradeListener = listener;
    }

    public void updateUpgrade(int position, UpgradeData newData) {
        items.set(position, newData);
        notifyDataSetChanged();
    }
}
