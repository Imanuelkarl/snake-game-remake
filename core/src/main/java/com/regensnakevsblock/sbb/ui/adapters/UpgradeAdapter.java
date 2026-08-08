package com.regensnakevsblock.sbb.ui.adapters;


import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.ui.cards.UpgradeCard;
import com.regensnakevsblock.sbb.ui.core.BaseAdapter;
import com.regensnakevsblock.sbb.ui.data.UpgradeData;

public class UpgradeAdapter extends BaseAdapter<UpgradeData, UpgradeCard> {
    public boolean notEnoughCoins;
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
        UpgradeData upgradeData = getItem(position);
        view.bind(getItem(position), position);

        // Add click listener for upgrade
        view.upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (upgradeListener != null &&
                    getItem(position).currentLevel < getItem(position).maxLevel) {
                    upgradeListener.onUpgrade(getItem(position), position);

                }
                view.bind(upgradeData,position);
            }
        });
    }
    void upgradeItem(UpgradeData data,int position){
        if(data.upgradeItem.equals("coins")){
            if(data.upgradeCost<= GameInstance.getSaveService().getCoins()){
                data.setCurrentLevel(data.currentLevel+1);
                getItem(position).currentLevel++;
                GameInstance.getSaveService().saveUpgrade(data.name,data.currentLevel);
                GameInstance.getSaveService().saveCoins(GameInstance.getSaveService().getCoins()-data.upgradeCost);
            }else{
                notEnoughCoins=true;
            }
        }else{
            if(data.upgradeCost<= GameInstance.getSaveService().getDiamonds()){
                getItem(position).currentLevel++;
                data.setCurrentLevel(data.currentLevel+1);
                GameInstance.getSaveService().saveUpgrade(data.name,data.currentLevel);
                GameInstance.getSaveService().saveCoins(GameInstance.getSaveService().getDiamonds()-data.upgradeCost);
            }else{
                notEnoughCoins=true;
            }
        }
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
