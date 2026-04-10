package com.regensnakevsblock.sbb.ui.data;

public class LevelData {
    int levelIndex;
    boolean cleared;
    boolean selected;
    boolean canSelect;
    boolean starOne;
    boolean starTwo;


    public LevelData(int levelIndex,boolean cleared,boolean selected,boolean canSelect){
        this.levelIndex=levelIndex;
        this.cleared=cleared;
        this.selected=selected;
        this.canSelect=canSelect;
    }
    public boolean isStarTwo() {
        return starTwo;
    }

    public void setStarTwo(boolean starTwo) {
        this.starTwo = starTwo;
    }

    public boolean isStarOne() {
        return starOne;
    }

    public void setStarOne(boolean starOne) {
        this.starOne = starOne;
    }

    public boolean isCanSelect() {
        return canSelect;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
    }

    public int getLevelIndex() {
        return levelIndex;
    }
    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }
}
