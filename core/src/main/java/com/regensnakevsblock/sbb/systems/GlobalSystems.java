package com.regensnakevsblock.sbb.systems;

import com.regensnakevsblock.sbb.world.WorldState;

public abstract class GlobalSystems {

    WorldState worldState;
    public GlobalSystems (WorldState worldState){
        this.worldState=worldState;
    }

    public abstract void update(float deltaTime);
}
