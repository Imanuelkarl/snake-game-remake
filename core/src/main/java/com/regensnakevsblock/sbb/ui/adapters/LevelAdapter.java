package com.regensnakevsblock.sbb.ui.adapters;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.screens.MainGame;
import com.regensnakevsblock.sbb.ui.cards.LevelCard;
import com.regensnakevsblock.sbb.ui.cards.SkinCard;
import com.regensnakevsblock.sbb.ui.core.BaseAdapter;
import com.regensnakevsblock.sbb.ui.data.LevelData;
import com.regensnakevsblock.sbb.ui.views.LevelsView;
import com.regensnakevsblock.sbb.world.WorldState;

public class LevelAdapter extends BaseAdapter<LevelData, LevelCard> {
    @Override
    public LevelCard createView(int position) {

        return new LevelCard();
    }

    @Override
    public void bindView(LevelCard view, int position) {
        LevelData level = getItem(position);
        view.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(level.isCanSelect()){
                    for (LevelData levelData : getItems()) {
                        levelData.setSelected(false);
                    }
                    //getItem(position).setSelected(true);
                    level.setSelected(true);
                    GameInstance.getSaveService().saveSelectedLevel(level.getLevelIndex());
                    notifyDataSetChanged();

                }
                super.clicked(event, x, y);

            }
        });
        view.bind(level, position);

    }

    @Override
    public void notifyDataSetChanged() {
        for (int i = 0; i < getViews().size(); i++) {
            LevelCard view = getView(i);
            int position = view.getPosition();
            if (position >= 0 && position < getCount()) {
                view.bind(getItem(position), position);
                // bindView(view, position);
            }
        }
    }
}
