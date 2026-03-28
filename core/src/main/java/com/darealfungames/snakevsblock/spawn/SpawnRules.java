package com.darealfungames.snakevsblock.spawn;

import static com.darealfungames.snakevsblock.utils.RandomEngine.getRandom;

import com.darealfungames.snakevsblock.entities.Block;
import com.darealfungames.snakevsblock.entities.Row;
import com.darealfungames.snakevsblock.world.WorldState;

import java.util.ArrayList;
import java.util.List;
public class SpawnRules {

    private static final int MAX_BLOCKS = 5;
    private static final int MAX_LINES = 3;
    private static final int MAX_WIN_BODIES = 3;
    private static final int MAX_SCORE_CAP = 10;

    private final WorldState worldState;
    private final EntityFactory entityFactory;

    public SpawnRules(WorldState worldState) {
        this.worldState = worldState;
        this.entityFactory = new EntityFactory();
    }

    public RowSpawnData generate() {
        RowSpawnData data = new RowSpawnData();

        if (worldState.getRows().isEmpty()) {
            generateFirstRow(data);
            return data;
        }

        generateDynamicRow(data);
        return data;
    }

    private void generateFirstRow(RowSpawnData data) {
        for (int i = 0; i < MAX_BLOCKS; i++) {
            Block block = entityFactory.createBlock(i, getRandom(1, 2));
            data.blocks.add(block);
        }
    }

    private void generateDynamicRow(RowSpawnData data) {

        int blockCount = weightedBlockCount();
        int lineCount = Math.min(getRandom(0, MAX_LINES), MAX_LINES);
        int winCount = Math.min(getRandom(0,MAX_WIN_BODIES),MAX_WIN_BODIES);

        List<Integer> positions = createPositions(MAX_BLOCKS);

        // Blocks
        for (int i = 0; i < blockCount && !positions.isEmpty(); i++) {
            int pos = takeRandomPosition(positions);

            data.blocks.add(
                entityFactory.createBlock(
                    pos,
                    getRandom(worldState.getMinScore(), worldState.getMaxScore())
                )
            );
        }

        if (blockCount == MAX_BLOCKS) {
            //modifyOneBlock(data.blocks);
            modifyOneBlock(data.blocks);
        }

        // Win bodies
        for (int i = 0; i < winCount && !positions.isEmpty(); i++) {
            data.winBodies.add(
                entityFactory.createWinBody(takeRandomPosition(positions),worldState)
            );
        }

        // Lines
        for (int i = 0; i < lineCount && !positions.isEmpty(); i++) {
            data.lines.add(
                entityFactory.createLine(takeRandomPosition(positions))
            );
        }
    }
    private int weightedBlockCount() {
        int roll=getRandom(0,10);
        if(roll>MAX_BLOCKS){
            roll=getRandom(0,4);
        }
        return roll;
    }
    private List<Integer> createPositions(int size) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < size; i++) positions.add(i);
        return positions;
    }

    private int takeRandomPosition(List<Integer> pool) {
        int index = getRandom(0, pool.size() - 1);
        return pool.remove(index);
    }

    private void modifyOneBlock(List<Block> blocks) {
        int index = getRandom(0, blocks.size() - 1);
        Block block = blocks.get(index);

        if (block.hasPowerUp()) {
            index = (index + 1) % blocks.size();
            block = blocks.get(index);
        }

        int maxScore = Math.min(worldState.getSnake().getLength()+4, MAX_SCORE_CAP);
        block.setValue(getRandom(1, maxScore));
    }


}
//Generate Row Logic Rules
// 1. Maximum 5 blocks per row
// 2. First Row always has 5 blocks with score 1 or 2
// 3. There should be higher probability of getting 0-3 blocks in a row
// 4. If 5 blocks are present, one block should be modified to have score equal to snake length or less
// 5. Maximum 3 win bodies per row
// 6. Maximum 3 lines per row
// 7. Positions of blocks, win bodies and lines should not overlap
// 8. If snake length is less than 40, block scores should not exceed snake length
