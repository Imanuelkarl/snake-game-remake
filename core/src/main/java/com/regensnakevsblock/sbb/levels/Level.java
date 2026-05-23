package com.regensnakevsblock.sbb.levels;

import com.regensnakevsblock.sbb.config.GameInstance;

import java.awt.Color;
import java.util.Map;

public class Level {


    public enum LevelType{
        FINISH_LINE,
        COLORED_BLOCKS,
        BLOCKS
    }
    public Level(int levelId){
        this.levelId =levelId;
        GameInstance.getInstance().getById(levelId);
    }
    private int noOfBlocks;
    private boolean cleared;
    private Map<Integer,Integer> colorIntegerMap;
    private LevelType levelType;
    private int noOfRows;
    private int levelId;

    public int getNoOfBlocks() {
        return noOfBlocks;
    }

    public void setNoOfBlocks(int noOfBlocks) {
        this.noOfBlocks = noOfBlocks;
    }

    public Map<Integer, Integer> getColorIntegerMap() {
        return colorIntegerMap;
    }

    public void setColorIntegerMap(Map<Integer, Integer> colorIntegerMap) {
        this.colorIntegerMap = colorIntegerMap;
    }

    public LevelType getLevelType() {
        return levelType;
    }

    public void setLevelType(LevelType levelType) {
        this.levelType = levelType;
    }

    public int getNoOfRows() {
        return noOfRows;
    }

    public void setNoOfRows(int noOfRows) {
        this.noOfRows = noOfRows;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }
}

