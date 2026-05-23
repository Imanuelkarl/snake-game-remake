package com.regensnakevsblock.sbb.levels;

import java.util.*;

public class TestLevelProvider {

    public static List<Level> createTestLevels() {

        List<Level> levels = new ArrayList<>();

        // ================= FINISH LINE =================
        Level finishLevel = new Level(1);
        finishLevel.setLevelType(Level.LevelType.FINISH_LINE);
        finishLevel.setNoOfRows(20); // minimal safe value
        finishLevel.setNoOfBlocks(0);
        finishLevel.setColorIntegerMap(null);
        finishLevel.setCleared(false);

        levels.add(finishLevel);

        // ================= BLOCKS =================
        Level blocksLevel = new Level(2);
        blocksLevel.setLevelType(Level.LevelType.BLOCKS);
        blocksLevel.setNoOfBlocks(10); // beginner level
        blocksLevel.setNoOfRows(0);
        blocksLevel.setColorIntegerMap(null);
        blocksLevel.setCleared(false);

        levels.add(blocksLevel);

        // ================= COLORED BLOCKS (2 colors) =================
        Level coloredLevelEasy = new Level(3);
        coloredLevelEasy.setLevelType(Level.LevelType.COLORED_BLOCKS);

        Map<Integer, Integer> map1 = new HashMap<>();
        map1.put(1, 5); // colorId 1
        map1.put(2, 4);  // colorId 2

        coloredLevelEasy.setColorIntegerMap(map1);
        coloredLevelEasy.setNoOfBlocks(18);
        coloredLevelEasy.setNoOfRows(0);
        coloredLevelEasy.setCleared(false);

        levels.add(coloredLevelEasy);

        // ================= COLORED BLOCKS (3 colors) =================
        Level coloredLevelMid = new Level(4);
        coloredLevelMid.setLevelType(Level.LevelType.COLORED_BLOCKS);

        Map<Integer, Integer> map2 = new HashMap<>();
        map2.put(1, 12);
        map2.put(2, 10);
        map2.put(3, 8);

        coloredLevelMid.setColorIntegerMap(map2);
        coloredLevelMid.setNoOfBlocks(30);
        coloredLevelMid.setNoOfRows(0);
        coloredLevelMid.setCleared(false);

        levels.add(coloredLevelMid);

        // ================= COLORED BLOCKS (5 colors - HARD) =================
        Level coloredLevelHard = new Level(5);
        coloredLevelHard.setLevelType(Level.LevelType.COLORED_BLOCKS);

        Map<Integer, Integer> map3 = new HashMap<>();
        map3.put(1, 15);
        map3.put(2, 15);
        map3.put(3, 15);
        map3.put(4, 10);
        map3.put(5, 10);

        coloredLevelHard.setColorIntegerMap(map3);
        coloredLevelHard.setNoOfBlocks(65);
        coloredLevelHard.setNoOfRows(0);
        coloredLevelHard.setCleared(false);

        levels.add(coloredLevelHard);

        return levels;
    }
}
