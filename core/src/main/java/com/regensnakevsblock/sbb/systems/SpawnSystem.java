package com.regensnakevsblock.sbb.systems;

import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.levels.Level;
import com.regensnakevsblock.sbb.spawn.RowFactory;
import com.regensnakevsblock.sbb.world.WorldState;

public class SpawnSystem extends GlobalSystems {

    private final RowFactory rowFactory;
    private float spawnDistanceTracker = 0f;
    private static float ROW_SPACING = 300f;

    public SpawnSystem(WorldState worldState) {
        super(worldState);
        ROW_SPACING=worldState.getBlockDimension()*2;
        this.rowFactory = new RowFactory(worldState);
    }

    @Override
    public void update(float delta) {
        if (worldState.isPaused() || worldState.isGameOver()) return;

        spawnDistanceTracker += worldState.getGameSpeed()*100* worldState.getMoveFactor() * delta;

        if (spawnDistanceTracker >= ROW_SPACING) {
            spawnDistanceTracker = 0;
            if (worldState.canCreateRows()) {
                worldState.addRows(rowFactory.create());
            }
            else if (worldState.getGameMode() == WorldState.GameMode.LEVELS) {

                if (worldState.getLevel().getLevelType() == Level.LevelType.FINISH_LINE) {

                    if (!worldState.isFinishLineSpawned() &&
                        worldState.getTotalRowsCreated() >= worldState.getLevel().getNoOfRows()) {

                        Row row = new Row(true);
                        row.setY(worldState.getNextRowY());

                        worldState.addRows(row);

                        worldState.setFinishLineSpawned(true);
                    }
                }
            }

        }
    }
}
