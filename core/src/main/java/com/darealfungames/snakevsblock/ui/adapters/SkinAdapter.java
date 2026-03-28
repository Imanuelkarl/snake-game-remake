package com.darealfungames.snakevsblock.ui.adapters;

import com.badlogic.gdx.utils.Array;
import com.darealfungames.snakevsblock.ui.cards.SkinCard;
import com.darealfungames.snakevsblock.ui.core.BaseAdapter;
import com.darealfungames.snakevsblock.ui.data.SkinData;

public class SkinAdapter extends BaseAdapter<SkinData, SkinCard> {
    private Array<SkinCard> activeViews = new Array<>();

    @Override
    public SkinCard createView(int position) {
        return new SkinCard();
    }

    @Override
    public void bindView(SkinCard view, int position) {
        SkinData skin = getItem(position);
        view.bind(skin, position);

        // Track active views
        if (!activeViews.contains(view, true)) {
            activeViews.add(view);
        }
    }

    @Override
    public void notifyDataSetChanged() {
        // Rebind all active views
        for (int i = 0; i < activeViews.size; i++) {
            SkinCard view = activeViews.get(i);
            int position = view.getPosition();
            if (position >= 0 && position < getCount()) {
                bindView(view, position);
            }
        }
    }

    public void updateSkinOwnership(String skinId, boolean isOwned) {
        for (int i = 0; i < getCount(); i++) {
            if (getItem(i).id.equals(skinId)) {
                getItem(i).isOwned = isOwned;
                break;
            }
        }
        notifyDataSetChanged();
    }

    public void equipSkin(String skinId) {
        for (int i = 0; i < getCount(); i++) {
            getItem(i).isEquipped = getItem(i).id.equals(skinId);
        }
        notifyDataSetChanged();
    }
}

