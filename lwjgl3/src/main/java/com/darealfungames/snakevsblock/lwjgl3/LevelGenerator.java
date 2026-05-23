package com.darealfungames.snakevsblock.lwjgl3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.regensnakevsblock.sbb.levels.Level;

import java.io.FileWriter;
import java.util.*;

public class LevelGenerator {

    private static final int TOTAL_LEVELS = 100;
    private static final int MAX_COLORS = 5;

    public static void main(String[] args) throws Exception {

        List<Level> levels = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= TOTAL_LEVELS; i++) {

            Level level = new Level(i);

            // ===== LEVEL TYPE DISTRIBUTION =====
            Level.LevelType type;

            if (i % 10 == 0) {
                type = Level.LevelType.FINISH_LINE;
            } else if (i % 3 == 0) {
                type = Level.LevelType.COLORED_BLOCKS;
            } else {
                type = Level.LevelType.BLOCKS;
            }

            level.setLevelType(type);

            // ===== APPLY RULES =====
            switch (type) {

                // ================= FINISH LINE =================
                case FINISH_LINE:
                    int rows = 50 + (i * 5); // starts at 50, increases
                    level.setNoOfRows(rows);
                    level.setNoOfBlocks(0);
                    level.setColorIntegerMap(null);
                    break;

                // ================= NORMAL BLOCKS =================
                case BLOCKS:
                    int blocks = 10 + (i * 2); // min 10, scales
                    level.setNoOfBlocks(blocks);
                    level.setNoOfRows(0);
                    level.setColorIntegerMap(null);
                    break;

                // ================= COLORED BLOCKS =================
                case COLORED_BLOCKS:

                    int colorCount = 2 + random.nextInt(4); // 2–5 colors

                    Map<Integer, Integer> colorMap =
                        generateColorTargets(colorCount, i, random);

                    int totalBlocks = colorMap.values()
                        .stream()
                        .mapToInt(Integer::intValue)
                        .sum();

                    level.setColorIntegerMap(colorMap);
                    level.setNoOfBlocks(totalBlocks); // optional but useful
                    level.setNoOfRows(0);
                    break;
            }

            level.setCleared(false);
            levels.add(level);
        }

        // ===== SAVE JSON =====
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

        FileWriter writer = new FileWriter("assets/levels.json");
        gson.toJson(levels, writer);
        writer.close();

        System.out.println("levels.json generated successfully!");
    }

    // ===== COLOR GENERATION =====
    private static Map<Integer, Integer> generateColorTargets(
        int colorCount, int levelIndex, Random random) {

        Map<Integer, Integer> map = new HashMap<>();

        // pick unique color IDs
        Set<Integer> chosenColors = new HashSet<>();
        while (chosenColors.size() < colorCount) {
            int colorId = 1 + random.nextInt(MAX_COLORS); // 1–5
            chosenColors.add(colorId);
        }

        // assign values (difficulty scaling)
        for (int colorId : chosenColors) {

            int base = 5 + levelIndex; // scaling
            int variance = random.nextInt(6); // randomness

            int required = base + variance;

            map.put(colorId, required);
        }

        return map;
    }
}
