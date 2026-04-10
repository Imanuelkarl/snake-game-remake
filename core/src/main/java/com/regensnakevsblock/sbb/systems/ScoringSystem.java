package com.regensnakevsblock.sbb.systems;

import com.regensnakevsblock.sbb.world.WorldState;

public class ScoringSystem extends GlobalSystems{

    private int numberOfBlocksDestroyed;
    public ScoringSystem(WorldState worldState) {
        super(worldState);
    }

    @Override
    public void update(float deltaTime) {
        if(worldState.getActiveBlock()!=null&&!worldState.getActiveBlock().isActive()){
            numberOfBlocksDestroyed++;
            System.out.println("Number of blocks destroyed is "+ numberOfBlocksDestroyed);
            worldState.setBreakingBlock(null);
        }

    }
}
