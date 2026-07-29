package com.darealfungames.snakevsblock.lwjgl3;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.regensnakevsblock.sbb.levels.Level;

import java.io.FileWriter;
import java.util.*;

public class LevelGenerator {

    private static final int TOTAL_LEVELS = 100;
    private static final int MAX_COLORS = 5;
    private static final int MAX_CONSECUTIVE_SAME_TYPE = 3;

    // Target percentages: Rows 40%, Blocks 30%, Colored 30%
    private static final double ROWS_PERCENTAGE = 0.40;
    private static final double BLOCKS_PERCENTAGE = 0.30;
    private static final double COLORED_PERCENTAGE = 0.30;

    // Track values used for each level type to ensure uniqueness
    private static Set<Integer> usedRowsValues = new HashSet<>();
    private static Set<Integer> usedBlocksValues = new HashSet<>();
    private static Set<String> usedColoredConfigs = new HashSet<>();

    public static void main(String[] args) throws Exception {

        List<Level> levels = new ArrayList<>();
        Random random = new Random();

        // Calculate target counts
        int targetRows = (int)(TOTAL_LEVELS * ROWS_PERCENTAGE);
        int targetBlocks = (int)(TOTAL_LEVELS * BLOCKS_PERCENTAGE);
        int targetColored = TOTAL_LEVELS - targetRows - targetBlocks;

        System.out.println("Target distribution:");
        System.out.printf("  ROWS: %d levels (%.0f%%)\n", targetRows, ROWS_PERCENTAGE * 100);
        System.out.printf("  BLOCKS: %d levels (%.0f%%)\n", targetBlocks, BLOCKS_PERCENTAGE * 100);
        System.out.printf("  COLORED: %d levels (%.0f%%)\n", targetColored, COLORED_PERCENTAGE * 100);

        // Track remaining counts
        int remainingRows = targetRows;
        int remainingBlocks = targetBlocks;
        int remainingColored = targetColored;

        // Track level indices for each type to calculate progressive values
        List<Integer> rowLevelIndices = new ArrayList<>();
        List<Integer> blockLevelIndices = new ArrayList<>();
        List<Integer> coloredLevelIndices = new ArrayList<>();

        // First pass: determine which level will have which type
        Level.LevelType[] levelTypes = new Level.LevelType[TOTAL_LEVELS + 1];
        Level.LevelType previousType = null;
        int consecutiveCount = 0;

        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            double progress = (double) i / TOTAL_LEVELS;

            Level.LevelType type = null;

            // Force initial ordering for first 10 levels
            if (i == 1 || i == 2 || i == 3) {
                type = Level.LevelType.FINISH_LINE;
            } else if (i == 4 || i == 5) {
                type = Level.LevelType.BLOCKS;
            } else if (i == 6 || i == 7) {
                type = Level.LevelType.COLORED_BLOCKS;
            } else {
                // Determine type based on remaining counts and progression
                List<Level.LevelType> availableTypes = new ArrayList<>();

                if (remainingRows > 0) availableTypes.add(Level.LevelType.FINISH_LINE);
                if (remainingBlocks > 0) availableTypes.add(Level.LevelType.BLOCKS);
                if (remainingColored > 0) availableTypes.add(Level.LevelType.COLORED_BLOCKS);

                if (availableTypes.isEmpty()) {
                    availableTypes.addAll(Arrays.asList(Level.LevelType.values()));
                }

                // Weight selection based on difficulty progression
                Map<Level.LevelType, Double> weights = new HashMap<>();

                for (Level.LevelType t : availableTypes) {
                    double weight = 1.0;

                    if (progress < 0.33) {
                        if (t == Level.LevelType.FINISH_LINE) weight = 3.0;
                        else if (t == Level.LevelType.BLOCKS) weight = 1.5;
                        else if (t == Level.LevelType.COLORED_BLOCKS) weight = 0.5;
                    } else if (progress < 0.66) {
                        if (t == Level.LevelType.BLOCKS) weight = 2.0;
                        else if (t == Level.LevelType.FINISH_LINE) weight = 1.0;
                        else if (t == Level.LevelType.COLORED_BLOCKS) weight = 1.0;
                    } else {
                        if (t == Level.LevelType.COLORED_BLOCKS) weight = 2.5;
                        else if (t == Level.LevelType.BLOCKS) weight = 1.0;
                        else if (t == Level.LevelType.FINISH_LINE) weight = 0.5;
                    }

                    if (t == Level.LevelType.FINISH_LINE) {
                        weight *= (double)remainingRows / targetRows;
                    } else if (t == Level.LevelType.BLOCKS) {
                        weight *= (double)remainingBlocks / targetBlocks;
                    } else {
                        weight *= (double)remainingColored / targetColored;
                    }

                    weights.put(t, weight);
                }

                type = selectWeightedType(availableTypes, weights, random);

                if (type == previousType && consecutiveCount >= MAX_CONSECUTIVE_SAME_TYPE) {
                    List<Level.LevelType> alternatives = new ArrayList<>(availableTypes);
                    alternatives.remove(type);
                    if (!alternatives.isEmpty()) {
                        type = alternatives.get(random.nextInt(alternatives.size()));
                    }
                }
            }

            // FINISH_LINE milestone every 10th level
            if (i % 10 == 0 && type != Level.LevelType.FINISH_LINE) {
                if (previousType == Level.LevelType.FINISH_LINE) remainingRows++;
                else if (previousType == Level.LevelType.BLOCKS) remainingBlocks++;
                else if (previousType == Level.LevelType.COLORED_BLOCKS) remainingColored++;
                type = Level.LevelType.FINISH_LINE;
            }

            // Update remaining counts
            if (type == Level.LevelType.FINISH_LINE) {
                remainingRows--;
                rowLevelIndices.add(i);
            } else if (type == Level.LevelType.BLOCKS) {
                remainingBlocks--;
                blockLevelIndices.add(i);
            } else if (type == Level.LevelType.COLORED_BLOCKS) {
                remainingColored--;
                coloredLevelIndices.add(i);
            }

            // Update consecutive counter
            if (type == previousType) {
                consecutiveCount++;
            } else {
                consecutiveCount = 1;
                previousType = type;
            }

            levelTypes[i] = type;
        }

        // Generate values for each level type with uniqueness
        Map<Integer, Integer> rowValues = generateUniqueProgressiveValues(rowLevelIndices, 10, 120, random);
        Map<Integer, Integer> blockValues = generateUniqueProgressiveValues(blockLevelIndices, 8, 80, random);
        Map<Integer, Integer> coloredTotalValues = new HashMap<>();
        Map<Integer, Map<Integer, Integer>> coloredConfigs = new HashMap<>();

        // Generate colored block configs
        for (int levelIndex : coloredLevelIndices) {
            Map<Integer, Integer> colorMap = generateUniqueColoredConfig(levelIndex, coloredConfigs.values(), random);
            int totalBlocks = colorMap.values().stream().mapToInt(Integer::intValue).sum();
            coloredTotalValues.put(levelIndex, totalBlocks);
            coloredConfigs.put(levelIndex, colorMap);
        }

        // Create levels
        for (int i = 1; i <= TOTAL_LEVELS; i++) {
            Level level = new Level();
            level.setLevelType(levelTypes[i]);
            level.setLevelId(i);
            level.setCleared(false);

            switch (levelTypes[i]) {
                case FINISH_LINE:
                    level.setNoOfRows(rowValues.get(i));
                    level.setNoOfBlocks(0);
                    level.setColorIntegerMap(null);
                    break;

                case BLOCKS:
                    level.setNoOfBlocks(blockValues.get(i));
                    level.setNoOfRows(0);
                    level.setColorIntegerMap(null);
                    break;

                case COLORED_BLOCKS:
                    level.setColorIntegerMap(coloredConfigs.get(i));
                    level.setNoOfBlocks(coloredTotalValues.get(i));
                    level.setNoOfRows(0);
                    break;
            }

            levels.add(level);
        }

        verifyDistribution(levels);

        // ===== SAVE JSON =====
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

        FileWriter writer = new FileWriter("assets/levels.json");
        gson.toJson(levels, writer);
        writer.close();

        System.out.println("\nlevels.json generated successfully!");
        printLevelSummary(levels);
        printConsecutiveAnalysis(levels);
        printUniquenessCheck(levels);
    }

    private static Map<Integer, Integer> generateUniqueProgressiveValues(List<Integer> levelIndices,
                                                                         int minValue, int maxValue,
                                                                         Random random) {
        Map<Integer, Integer> values = new HashMap<>();
        if (levelIndices.isEmpty()) return values;

        Collections.sort(levelIndices);
        int count = levelIndices.size();

        // Calculate progressive values that are guaranteed to be unique
        for (int idx = 0; idx < count; idx++) {
            int levelIndex = levelIndices.get(idx);
            float t = (float) idx / (count - 1); // 0 to 1

            // Calculate base value for this position in sequence
            int baseValue = minValue + (int)(t * (maxValue - minValue));

            // Add some variation but ensure uniqueness
            int value = baseValue;
            boolean unique = false;
            int attempts = 0;

            while (!unique && attempts < 20) {
                // Add random offset between -3 and +3, but ensure within bounds
                int offset = random.nextInt(7) - 3;
                value = baseValue + offset;
                value = Math.max(minValue + idx, Math.min(value, maxValue - (count - idx - 1)));

                // Check if this value is already used
                if (!values.containsValue(value)) {
                    unique = true;
                }
                attempts++;
            }

            // If still not unique, just use the base value + idx as fallback
            if (!unique) {
                value = minValue + idx;
            }

            values.put(levelIndex, value);
        }

        return values;
    }

    private static Map<Integer, Integer> generateUniqueColoredConfig(int levelIndex,
                                                                     Collection<Map<Integer, Integer>> existingConfigs,
                                                                     Random random) {
        Map<Integer, Integer> colorMap = new HashMap<>();

        // Determine difficulty based on level index
        int levelPosition;
        List<Integer> allColoredLevels = new ArrayList<>();
        // This is simplified - in practice you'd track order globally

        int colorCount;
        int minPerColor;
        int maxPerColor;

        if (levelIndex <= 30) {
            // Easy: 2 colors
            colorCount = 2;
            minPerColor = 3;
            maxPerColor = 7;
        } else if (levelIndex <= 70) {
            // Medium: 3 colors
            colorCount = 3;
            minPerColor = 6;
            maxPerColor = 9;
        } else {
            // Hard: 4-5 colors
            colorCount = 4 + (levelIndex > 90 ? 1 : 0);
            minPerColor = 8;
            maxPerColor = 12 + (levelIndex - 70) / 5;
        }

        colorCount = Math.min(colorCount, MAX_COLORS);

        // Try to generate a unique configuration
        boolean unique = false;
        int attempts = 0;

        while (!unique && attempts < 30) {
            colorMap.clear();

            // Pick unique color IDs
            List<Integer> allColors = Arrays.asList(1, 2, 3, 4, 5);
            Collections.shuffle(allColors, new Random(levelIndex * (attempts + 1)));
            Set<Integer> chosenColors = new HashSet<>(allColors.subList(0, colorCount));

            // Calculate total available range
            int totalRange = 0;
            for (int i = minPerColor; i <= maxPerColor; i++) {
                totalRange += i;
            }

            // Distribute values progressively
            List<Integer> colors = new ArrayList<>(chosenColors);
            for (int j = 0; j < colors.size(); j++) {
                int colorId = colors.get(j);
                int required;

                if (colorCount == 2) {
                    // For 2 colors, first color gets 3-7, second gets 3-7 but ensure total is unique
                    required = minPerColor + random.nextInt(maxPerColor - minPerColor + 1);
                } else {
                    float t = (float) j / (colors.size() - 1);
                    int range = maxPerColor - minPerColor;
                    required = minPerColor + (int)(t * range) + random.nextInt(3) - 1;
                    required = Math.max(minPerColor, Math.min(required, maxPerColor));
                }

                colorMap.put(colorId, required);
            }

            // Check if this configuration is unique
            String configKey = generateConfigKey(colorMap);
            if (!existingConfigs.stream().anyMatch(existing -> generateConfigKey(existing).equals(configKey))) {
                unique = true;
            }
            attempts++;
        }

        return colorMap;
    }

    private static String generateConfigKey(Map<Integer, Integer> config) {
        TreeMap<Integer, Integer> sorted = new TreeMap<>(config);
        return sorted.toString();
    }

    private static Level.LevelType selectWeightedType(List<Level.LevelType> types,
                                                      Map<Level.LevelType, Double> weights,
                                                      Random random) {
        double totalWeight = 0;
        for (Level.LevelType type : types) {
            totalWeight += weights.get(type);
        }

        double randomValue = random.nextDouble() * totalWeight;
        double cumulative = 0;

        for (Level.LevelType type : types) {
            cumulative += weights.get(type);
            if (randomValue <= cumulative) {
                return type;
            }
        }

        return types.get(0);
    }

    private static void verifyDistribution(List<Level> levels) {
        int rowsCount = 0;
        int blocksCount = 0;
        int coloredCount = 0;

        for (Level level : levels) {
            switch (level.getLevelType()) {
                case FINISH_LINE:
                    rowsCount++;
                    break;
                case BLOCKS:
                    blocksCount++;
                    break;
                case COLORED_BLOCKS:
                    coloredCount++;
                    break;
            }
        }

        System.out.println("\n=== ACTUAL DISTRIBUTION ===");
        System.out.printf("ROWS: %d levels (%.1f%%)\n", rowsCount, (rowsCount * 100.0 / TOTAL_LEVELS));
        System.out.printf("BLOCKS: %d levels (%.1f%%)\n", blocksCount, (blocksCount * 100.0 / TOTAL_LEVELS));
        System.out.printf("COLORED: %d levels (%.1f%%)\n", coloredCount, (coloredCount * 100.0 / TOTAL_LEVELS));
    }

    private static void printLevelSummary(List<Level> levels) {
        System.out.println("\n=== FIRST 15 LEVELS ===");
        for (int i = 0; i < Math.min(15, levels.size()); i++) {
            printLevel(i + 1, levels.get(i));
        }

        System.out.println("\n=== LEVELS 16-30 ===");
        for (int i = 15; i < Math.min(30, levels.size()); i++) {
            printLevel(i + 1, levels.get(i));
        }

        System.out.println("\n=== LAST 15 LEVELS ===");
        for (int i = levels.size() - 15; i < levels.size(); i++) {
            printLevel(i + 1, levels.get(i));
        }
    }

    private static void printLevel(int levelNumber, Level level) {
        switch (level.getLevelType()) {
            case FINISH_LINE:
                System.out.printf("Level %3d: %-12s | Rows: %3d%n",
                    levelNumber, "ROWS", level.getNoOfRows());
                break;
            case BLOCKS:
                System.out.printf("Level %3d: %-12s | Blocks: %3d%n",
                    levelNumber, "BLOCKS", level.getNoOfBlocks());
                break;
            case COLORED_BLOCKS:
                int totalBlocks = level.getColorIntegerMap().values().stream().mapToInt(Integer::intValue).sum();
                System.out.printf("Level %3d: %-12s | Colors: %d | Total Blocks: %3d | Config: %s%n",
                    levelNumber, "COLORED", level.getColorIntegerMap().size(), totalBlocks,
                    level.getColorIntegerMap());
                break;
        }
    }

    private static void printConsecutiveAnalysis(List<Level> levels) {
        System.out.println("\n=== CONSECUTIVE LEVEL ANALYSIS ===");

        Level.LevelType currentType = null;
        int currentRun = 0;
        int maxRun = 0;

        for (Level level : levels) {
            if (level.getLevelType() == currentType) {
                currentRun++;
            } else {
                if (currentType != null) {
                    maxRun = Math.max(maxRun, currentRun);
                }
                currentType = level.getLevelType();
                currentRun = 1;
            }
        }

        if (currentType != null) {
            maxRun = Math.max(maxRun, currentRun);
        }

        System.out.println("Maximum consecutive levels of same type: " + maxRun);

        if (maxRun <= MAX_CONSECUTIVE_SAME_TYPE) {
            System.out.println("✓ Good! No more than " + MAX_CONSECUTIVE_SAME_TYPE + " consecutive levels.");
        } else {
            System.out.println("⚠ Warning: Found " + maxRun + " consecutive levels (limit is " + MAX_CONSECUTIVE_SAME_TYPE + ")");
        }
    }

    private static void printUniquenessCheck(List<Level> levels) {
        System.out.println("\n=== UNIQUENESS CHECK ===");

        Set<Integer> rowValues = new HashSet<>();
        Set<Integer> blockValues = new HashSet<>();
        Set<String> coloredConfigs = new HashSet<>();

        boolean rowsUnique = true;
        boolean blocksUnique = true;
        boolean coloredUnique = true;

        for (Level level : levels) {
            switch (level.getLevelType()) {
                case FINISH_LINE:
                    int rows = level.getNoOfRows();
                    if (rowValues.contains(rows)) {
                        rowsUnique = false;
                        System.out.println("⚠ Duplicate row value found: " + rows);
                    }
                    rowValues.add(rows);
                    break;
                case BLOCKS:
                    int blocks = level.getNoOfBlocks();
                    if (blockValues.contains(blocks)) {
                        blocksUnique = false;
                        System.out.println("⚠ Duplicate blocks value found: " + blocks);
                    }
                    blockValues.add(blocks);
                    break;
                case COLORED_BLOCKS:
                    String config = level.getColorIntegerMap().toString();
                    if (coloredConfigs.contains(config)) {
                        coloredUnique = false;
                        System.out.println("⚠ Duplicate colored config found: " + config);
                    }
                    coloredConfigs.add(config);
                    break;
            }
        }

        System.out.printf("ROWS values: %s (Total: %d)%n", rowsUnique ? "✓ ALL UNIQUE" : "✗ HAS DUPLICATES", rowValues.size());
        System.out.printf("BLOCKS values: %s (Total: %d)%n", blocksUnique ? "✓ ALL UNIQUE" : "✗ HAS DUPLICATES", blockValues.size());
        System.out.printf("COLORED configs: %s (Total: %d)%n", coloredUnique ? "✓ ALL UNIQUE" : "✗ HAS DUPLICATES", coloredConfigs.size());
    }
}
