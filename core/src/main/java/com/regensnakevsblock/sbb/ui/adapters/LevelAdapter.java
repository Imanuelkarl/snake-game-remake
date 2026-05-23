package com.regensnakevsblock.sbb.ui.adapters;

import com.regensnakevsblock.sbb.ui.cards.LevelCard;
import com.regensnakevsblock.sbb.ui.cards.SkinCard;
import com.regensnakevsblock.sbb.ui.core.BaseAdapter;
import com.regensnakevsblock.sbb.ui.data.LevelData;
import com.regensnakevsblock.sbb.ui.views.LevelsView;

public class LevelAdapter extends BaseAdapter<LevelData, LevelCard> {
    @Override
    public LevelCard createView(int position) {
        return new LevelCard();
    }

    @Override
    public void bindView(LevelCard view, int position) {
        LevelData level = getItem(position);
        view.bind(level, position);

    }

    @Override
    public void notifyDataSetChanged() {

    }
}
