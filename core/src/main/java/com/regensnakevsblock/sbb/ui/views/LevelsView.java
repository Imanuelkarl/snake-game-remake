package com.regensnakevsblock.sbb.ui.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.ui.adapters.LevelAdapter;
import com.regensnakevsblock.sbb.ui.adapters.SkinAdapter;
import com.regensnakevsblock.sbb.ui.cards.LevelCard;
import com.regensnakevsblock.sbb.ui.core.ListManager;
import com.regensnakevsblock.sbb.ui.data.LevelData;
import com.regensnakevsblock.sbb.utils.ActorFactory;

import java.util.ArrayList;
import java.util.List;

public class LevelsView extends BaseActorView {
    private ListManager<LevelData, LevelCard> listManager;
    private LevelAdapter adapter;
    private float width;
    private float height;


    public LevelsView(){
        super(null);

        setupList();
        setupTransform();
        loadData();
    }

    @Override
    public void setParentView(Group parent) {
        super.setParentView(parent);
        this.width = parent.getWidth();
        this.height = parent.getHeight();
        setupTransform();
    }

    private void setupList() {
        adapter = new LevelAdapter();
        listManager = new ListManager<>(adapter);
        addActor(listManager);
    }
    private void setupTransform(){

        listManager.setSize(width,height);
        listManager.setPosition(0, 0);
        listManager.setLayout( 4, 200);
    }
    private void loadData() {
        List<LevelCard> cards = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            LevelCard card = new LevelCard();
            System.out.println("Creating first items yahoo "+i);
            cards.add(card);
        }
        //adapter.setData(GameInstance.getInstance().getLevelData());
        adapter.setOnItemClickListener((level, position) -> {
            if(level.isCanSelect()){
                level.setSelected(true);
            }
        });
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void resize(float width, float height) {
        listManager.onResize(width, height);
    }

    @Override
    public void buildIfNeeded() {

    }
}
