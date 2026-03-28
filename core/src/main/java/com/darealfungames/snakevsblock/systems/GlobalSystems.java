package com.darealfungames.snakevsblock.systems;

import com.darealfungames.snakevsblock.world.WorldState;

public abstract class GlobalSystems {

    WorldState worldState;
    public GlobalSystems (WorldState worldState){
        this.worldState=worldState;
    }

    public abstract void update(float deltaTime);
}
