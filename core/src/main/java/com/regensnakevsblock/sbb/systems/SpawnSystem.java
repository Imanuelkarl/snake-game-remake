package com.regensnakevsblock.sbb.systems;

import com.regensnakevsblock.sbb.spawn.RowFactory;
import com.regensnakevsblock.sbb.world.WorldState;

public class SpawnSystem extends GlobalSystems {

    private final RowFactory rowFactory;
    private float spawnDistanceTracker = 0f;
    private static final float ROW_SPACING = 300f;

    public SpawnSystem(WorldState worldState) {
        super(worldState);
        this.rowFactory = new RowFactory(worldState);
    }

    @Override
    public void update(float delta) {
        if (worldState.isPaused() || worldState.isGameOver()) return;

        spawnDistanceTracker += worldState.getGameSpeed()*100* worldState.getMoveFactor() * delta;

        if (spawnDistanceTracker >= ROW_SPACING) {
            spawnDistanceTracker = 0;
            if(worldState.getRows().size()<24){
                worldState.addRows(rowFactory.create());
            }

        }
    }
}
