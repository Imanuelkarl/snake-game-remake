package com.regensnakevsblock.sbb.systems;

import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.levels.Level;
import com.regensnakevsblock.sbb.world.WorldState;

import java.util.Map;

public class ScoringSystem extends GlobalSystems{


    public ScoringSystem(WorldState worldState) {
        super(worldState);
    }

    @Override
    public void update(float deltaTime) {
        if(worldState.isPaused() || worldState.isGameOver()|| worldState.isGameWon()){
            return;
        }
        if(worldState.getGameMode()== WorldState.GameMode.LEVELS) {
            if (worldState.getLevel().getLevelType() == Level.LevelType.BLOCKS) {
                if (worldState.totalBlocksDestroyed() >= worldState.getLevel().getNoOfBlocks()) {
                    System.out.println("No of blocks to destroy are "+worldState.getLevel().getNoOfBlocks() +"and number of blocks destroyed so far are "+worldState.totalBlocksDestroyed());
                    worldState.setGameWon(true);
                    worldState.setLevelCleared();
                    System.out.println("Victory !!!.\n\n total blocks destroyed " + worldState.totalBlocksDestroyed());
                    return;
                }
            }
            if (worldState.getLevel().getLevelType() == Level.LevelType.COLORED_BLOCKS) {

                boolean allMet = true;

                for (Map.Entry<Integer, Integer> entry :
                    worldState.getLevel().getColorIntegerMap().entrySet()) {

                    int colorId = entry.getKey();
                    int required = entry.getValue();

                    int current = worldState.getBlocksDestroyedForColor(colorId);

                    if (current < required) {
                        allMet = false;
                        break;
                    }
                }

                if (allMet) {
                    worldState.setGameWon(true);
                    worldState.setLevelCleared();
                    System.out.println("Victory !!! (Colored Blocks)");
                }
            }
        }

    }
}
