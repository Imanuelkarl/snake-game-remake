package com.regensnakevsblock.sbb.spawn;

import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Line;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.WinBody;
import com.regensnakevsblock.sbb.world.WorldState;

public class RowFactory {

    private final WorldState worldState;
    private final SpawnRules spawnData;

    public RowFactory(WorldState worldState) {
        this.worldState = worldState;
        this.spawnData = new SpawnRules(worldState);
    }

    public Row create() {
        Row row = new Row();
        row.setY(worldState.getNextRowY());

        RowSpawnData data = spawnData.generate();

        for (Block block : data.blocks) {
            row.addBlock(block);
        }

        for (WinBody winBody : data.winBodies) {
            row.addWinBody(winBody);
        }

        for (Line line : data.lines) {
            row.addLine(line);
        }

        return row;
    }
}
