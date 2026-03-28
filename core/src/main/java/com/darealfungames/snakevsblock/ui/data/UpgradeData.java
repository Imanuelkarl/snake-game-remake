package com.darealfungames.snakevsblock.ui.data;


public class UpgradeData {
    public String id;
    public String name;
    public String description;
    public int currentLevel;
    public int maxLevel;
    public int upgradeCost;
    public float currentValue;
    public float nextValue;

    public UpgradeData(String id, String name, String description, int currentLevel, int maxLevel,
                       int upgradeCost, float currentValue, float nextValue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.currentLevel = currentLevel;
        this.maxLevel = maxLevel;
        this.upgradeCost = upgradeCost;
        this.currentValue = currentValue;
        this.nextValue = nextValue;
    }
}
