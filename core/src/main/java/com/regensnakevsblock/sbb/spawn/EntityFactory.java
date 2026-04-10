package com.regensnakevsblock.sbb.spawn;

import static com.regensnakevsblock.sbb.utils.RandomEngine.getRandom;

import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.PowerUpDimensions;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Line;
import com.regensnakevsblock.sbb.entities.WinBody;
import com.regensnakevsblock.sbb.enumaretors.PowerUp;
import com.regensnakevsblock.sbb.utils.RandomEngine;
import com.regensnakevsblock.sbb.world.WorldState;

public class EntityFactory {

    public Block createBlock(int pos, int score) {
        Block block = new Block(Assets.getInstance().blockTexture,0,0,30,30,score);
        block.setValue(score);
        block.setPosition(pos);
        int powerDet = RandomEngine.getRandom(0,4);
        if(powerDet==3){
            int powerUpType = RandomEngine.getRandom(0,3);
            block.setPowerUp(true, PowerUpDimensions.intToEnum(powerUpType));
            block.setValue(20);
        }
        block.setActive(true);
        //block.setColumn(pos);
        return block;
    }

    public Line createLine(int pos) {
        Line line = new Line(0,0,50,10);
        line.setPosition(pos);
        line.setX(pos*50);
        line.setY(0);
        line.setHeight(30);
        line.setWidth(5);
        line.setRegionX(0);
        line.setRegionY(0);
        //line.setColumn(pos);
        return line;
    }

    public WinBody createWinBody(int pos, WorldState worldState) {
        WinBody win = new WinBody();
        win.setPositionIndex(pos);
        win.setActive(true);
        win.setRadius(worldState.getSnakeRadius());
        win.setValue(getRandom(1,5));
        /*win.setX(pos*15);
        win.setY(0);
        win.setWidth(15);
        win.setHeight(15);
        win.setRegionX(0);
        win.setRegionY(0);
        //win.setColumn(pos);*/
        return win;
    }
}
