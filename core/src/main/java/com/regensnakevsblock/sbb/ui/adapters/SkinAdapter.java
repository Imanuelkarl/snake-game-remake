package com.regensnakevsblock.sbb.ui.adapters;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.ui.cards.SkinCard;
import com.regensnakevsblock.sbb.ui.core.BaseAdapter;
import com.regensnakevsblock.sbb.ui.data.SkinData;

public class SkinAdapter extends BaseAdapter<SkinData, SkinCard> {
    public boolean notEnoughCoins = false;
    private Array<SkinCard> activeViews = new Array<>();

    @Override
    public SkinCard createView(int position) {
        return new SkinCard();
    }

    @Override
    public void bindView(SkinCard view, int position) {
        SkinData skin = getItem(position);

        view.actionButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(skin.isOwned){
                    equipSkin(skin,position);
                }else{
                    purchaseSkin(skin,position);
                }
                notifyDataSetChanged();
            }
        });
        // Track active views
        if (!activeViews.contains(view, true)) {
            activeViews.add(view);
        }
    }
    private void equipSkin(SkinData skin, int position){
        for(int i=0;i<items.size();i++){
            items.get(i).isEquipped =false;
        }
        skin.isEquipped=true;
        GameInstance.getSaveService().saveSelectedSkinIndex(position);
    }
    @Override
    public void notifyDataSetChanged() {
        // Rebind all active views
        for (int i = 0; i < activeViews.size; i++) {
            SkinCard view = activeViews.get(i);
            int position = view.getPosition();
            if (position >= 0 && position < getCount()) {
                view.bind(getItem(position), position);
               // bindView(view, position);
            }
        }

    }
    public void purchaseSkin(SkinData skin, int position) {
        if(skin.currency.equals("coins")){
            if(skin.price< GameInstance.getSaveService().getCoins()){
                for(int i=0;i<items.size();i++){
                    items.get(i).isEquipped =false;
                }
                skin.isOwned = true;
                equipSkin(skin,position);
                GameInstance.getSaveService().saveSkinAsPurchased(skin.id);
                GameInstance.getSaveService().saveCoins(GameInstance.getSaveService().getCoins()-skin.price);
            }else{
                notEnoughCoins = true;
            }
        }else{
            if(skin.price< GameInstance.getSaveService().getDiamonds()){
                for(int i=0;i<items.size();i++){
                    items.get(i).isEquipped =false;
                }
                skin.isOwned = true;
                equipSkin(skin,position);
                GameInstance.getSaveService().saveCoins(GameInstance.getSaveService().getDiamonds()-skin.price);
            }else{
                notEnoughCoins = true;
            }
        }
    }

}

