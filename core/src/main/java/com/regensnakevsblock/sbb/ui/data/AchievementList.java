package com.regensnakevsblock.sbb.ui.data;

import java.util.Arrays;
import java.util.List;

public class AchievementList {

    public static final List<AchievementData> ACHIEVEMENTS = Arrays.asList(

        // =========================================================
        // BLOCKS DESTROYED - LIFETIME
        // =========================================================

        new AchievementData(
            "blocks_100",
            "First Smash",
            "Destroy 100 blocks.",
            0, 100, 1,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_TOTAL
        ),

        new AchievementData(
            "blocks_500",
            "Block Breaker",
            "Destroy 500 blocks.",
            0, 500, 2,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_TOTAL
        ),

        new AchievementData(
            "blocks_2000",
            "Wrecking Ball",
            "Destroy 2,000 blocks.",
            0, 2000, 3,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_TOTAL
        ),

        new AchievementData(
            "blocks_10000",
            "Block Terminator",
            "Destroy 10,000 blocks.",
            0, 10000, 5,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_TOTAL
        ),

        new AchievementData(
            "blocks_50000",
            "World Destroyer",
            "Destroy 50,000 blocks.",
            0, 50000, 10,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_TOTAL
        ),


        // =========================================================
        // COLORED BLOCKS
        // =========================================================

        // RED
        new AchievementData(
            "red_50",
            "Seeing Red",
            "Destroy 50 red blocks.",
            0, 50, 1,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            0
        ),

        new AchievementData(
            "red_200",
            "I Hate Red",
            "Destroy 200 red blocks.",
            0, 200, 2,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            0
        ),

        new AchievementData(
            "red_500",
            "Red Exterminator",
            "Destroy 500 red blocks.",
            0, 500, 4,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            0
        ),

        // BLUE
        new AchievementData(
            "blue_50",
            "Blue Breaker",
            "Destroy 50 blue blocks.",
            0, 50, 1,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            1
        ),

        new AchievementData(
            "blue_200",
            "Ice Breaker",
            "Destroy 200 blue blocks.",
            0, 200, 2,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            1
        ),

        new AchievementData(
            "blue_500",
            "Blue Buster",
            "Destroy 500 blue blocks.",
            0, 500, 4,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            1
        ),

        // GREEN
        new AchievementData(
            "green_50",
            "Going Green",
            "Destroy 50 green blocks.",
            0, 50, 1,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            2
        ),

        new AchievementData(
            "green_200",
            "Weed Killer",
            "Destroy 200 green blocks.",
            0, 200, 2,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            2
        ),

        new AchievementData(
            "green_500",
            "Nature's Nightmare",
            "Destroy 500 green blocks.",
            0, 500, 4,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            2
        ),

        // YELLOW
        new AchievementData(
            "yellow_50",
            "Yellow Fever",
            "Destroy 50 yellow blocks.",
            0, 50, 1,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            3
        ),

        new AchievementData(
            "yellow_200",
            "Gold Rush",
            "Destroy 200 yellow blocks.",
            0, 200, 2,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            3
        ),

        new AchievementData(
            "yellow_500",
            "Sun Destroyer",
            "Destroy 500 yellow blocks.",
            0, 500, 4,
            false, false,
            AchievementData.AchievementType.COLORED_BLOCKS_DESTROYED,
            3
        ),


        // =========================================================
        // BLOCKS DESTROYED IN A ROW
        // =========================================================

        new AchievementData(
            "streak_2",
            "Double Trouble",
            "Destroy 2 blocks in a row.",
            0, 2, 1,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_STREAK
        ),

        new AchievementData(
            "streak_3",
            "Triple Trouble",
            "Destroy 3 blocks in a row.",
            0, 3, 2,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_STREAK
        ),

        new AchievementData(
            "streak_4",
            "Block Breaker",
            "Destroy 4 blocks in a row.",
            0, 4, 3,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_STREAK
        ),

        new AchievementData(
            "streak_5",
            "Unstoppable",
            "Destroy 5 blocks in a row.",
            0, 5, 5,
            false, false,
            AchievementData.AchievementType.BLOCKS_DESTROYED_STREAK
        ),

        // =========================================================
        // SNAKE BODIES IN A ROW
        // =========================================================

        new AchievementData(
            "bodies_2",
            "Getting Bigger",
            "Collect 2 snake bodies in a row.",
            0, 2, 1,
            false, false,
            AchievementData.AchievementType.WIN_BODIES_STREAK
        ),

        new AchievementData(
            "bodies_3",
            "Growing Strong",
            "Collect 3 snake bodies in a row.",
            0, 3, 2,
            false, false,
            AchievementData.AchievementType.WIN_BODIES_STREAK
        ),

        new AchievementData(
            "bodies_4",
            "Growing Fast",
            "Collect 4 snake bodies in a row.",
            0, 4, 3,
            false, false,
            AchievementData.AchievementType.WIN_BODIES_STREAK
        ),

        new AchievementData(
            "bodies_5",
            "Feed the Beast",
            "Collect 5 snake bodies in a row.",
            0, 5, 5,
            false, false,
            AchievementData.AchievementType.WIN_BODIES_STREAK
        ),

        // =========================================================
        // SCORE MARKS
        // =========================================================

        new AchievementData(
            "score_100",
            "Getting Started",
            "Score 100 points in a game.",
            0, 100, 1,
            false, false,
            AchievementData.AchievementType.SCORE_MARK
        ),

        new AchievementData(
            "score_500",
            "Score Chaser",
            "Score 500 points in a game.",
            0, 500, 2,
            false, false,
            AchievementData.AchievementType.SCORE_MARK
        ),

        new AchievementData(
            "score_1000",
            "Point Collector",
            "Score 1,000 points in a game.",
            0, 1000, 4,
            false, false,
            AchievementData.AchievementType.SCORE_MARK
        ),

        new AchievementData(
            "score_5000",
            "Point Machine",
            "Score 5,000 points in a game.",
            0, 5000, 8,
            false, false,
            AchievementData.AchievementType.SCORE_MARK
        ),


        // =========================================================
        // LIFETIME COINS
        // =========================================================

        new AchievementData(
            "coins_300",
            "Pocket Change",
            "Collect 300 coins.",
            0, 300, 1,
            false, false,
            AchievementData.AchievementType.LIFETIME_COINS
        ),

        new AchievementData(
            "coins_1000",
            "Coin Collector",
            "Collect 1,000 coins.",
            0, 1000, 2,
            false, false,
            AchievementData.AchievementType.LIFETIME_COINS
        ),

        new AchievementData(
            "coins_5000",
            "Treasure Hunter",
            "Collect 5,000 coins.",
            0, 5000, 4,
            false, false,
            AchievementData.AchievementType.LIFETIME_COINS
        ),

        new AchievementData(
            "coins_20000",
            "Gold Hoarder",
            "Collect 20,000 coins.",
            0, 20000, 7,
            false, false,
            AchievementData.AchievementType.LIFETIME_COINS
        ),

        new AchievementData(
            "coins_100000",
            "Snake Tycoon",
            "Collect 100,000 coins.",
            0, 100000, 10,
            false, false,
            AchievementData.AchievementType.LIFETIME_COINS
        ),


        // =========================================================
        // SNAKE LENGTH
        // =========================================================

        new AchievementData(
            "length_100",
            "Growing Strong",
            "Reach a snake length of 100.",
            0, 100, 2,
            false, false,
            AchievementData.AchievementType.SNAKE_LENGTH
        ),

        new AchievementData(
            "length_150",
            "Long Snake",
            "Reach a snake length of 150.",
            0, 150, 4,
            false, false,
            AchievementData.AchievementType.SNAKE_LENGTH
        ),

        new AchievementData(
            "length_200",
            "Giant Snake",
            "Reach a snake length of 200.",
            0, 200, 7,
            false, false,
            AchievementData.AchievementType.SNAKE_LENGTH
        ),

        new AchievementData(
            "length_300",
            "Legendary Serpent",
            "Reach a snake length of 300.",
            0, 300, 10,
            false, false,
            AchievementData.AchievementType.SNAKE_LENGTH
        ),


        // =========================================================
        // TIME PLAYED
        // =========================================================

        new AchievementData(
            "time_60",
            "Keep Alive",
            "Play for a total of 1 minute.",
            0, 60, 1,
            false, false,
            AchievementData.AchievementType.TIME_ON_GAME
        ),

        new AchievementData(
            "time_300",
            "Steady Energy",
            "Play for a total of 5 minutes.",
            0, 300, 2,
            false, false,
            AchievementData.AchievementType.TIME_ON_GAME
        ),

        new AchievementData(
            "time_1800",
            "Dedicated",
            "Play for a total of 30 minutes.",
            0, 1800, 5,
            false, false,
            AchievementData.AchievementType.TIME_ON_GAME
        ),

        new AchievementData(
            "time_3600",
            "Snake Addict",
            "Play for a total of 1 hour.",
            0, 3600, 8,
            false, false,
            AchievementData.AchievementType.TIME_ON_GAME
        ),


        // =========================================================
        // ROWS CLEARED
        // =========================================================

        new AchievementData(
            "rows_50",
            "Row Cleaner",
            "Clear 50 rows.",
            0, 50, 2,
            false, false,
            AchievementData.AchievementType.ROWS_CLEARED
        ),

        new AchievementData(
            "rows_100",
            "Line Breaker",
            "Clear 100 rows.",
            0, 100, 3,
            false, false,
            AchievementData.AchievementType.ROWS_CLEARED
        ),

        new AchievementData(
            "rows_500",
            "Row Master",
            "Clear 500 rows.",
            0, 500, 6,
            false, false,
            AchievementData.AchievementType.ROWS_CLEARED
        ),

        new AchievementData(
            "rows_2000",
            "Clean Sweep",
            "Clear 2,000 rows.",
            0, 2000, 10,
            false, false,
            AchievementData.AchievementType.ROWS_CLEARED
        ),


        // =========================================================
        // BLOCK SIZE
        // =========================================================

        new AchievementData(
            "block_size_40",
            "Heavy Hitter",
            "Destroy a block with a value of 40 or more.",
            0, 40, 2,
            false, false,
            AchievementData.AchievementType.BLOCK_SIZE_DESTROYED
        ),

        new AchievementData(
            "block_size_50",
            "Brick Breaker",
            "Destroy a block with a value of 50 or more.",
            0, 50, 4,
            false, false,
            AchievementData.AchievementType.BLOCK_SIZE_DESTROYED
        ),

        new AchievementData(
            "block_size_60",
            "Hard as Rock",
            "Destroy a block with a value of 60 or more.",
            0, 60, 7,
            false, false,
            AchievementData.AchievementType.BLOCK_SIZE_DESTROYED
        ),

        new AchievementData(
            "block_size_80",
            "Titan Breaker",
            "Destroy a block with a value of 80 or more.",
            0, 80, 10,
            false, false,
            AchievementData.AchievementType.BLOCK_SIZE_DESTROYED
        )
    );
    public static String getTitle(String id) {
        if (id == null) return "";

        for (AchievementData a : ACHIEVEMENTS) {
            if (a.id.equals(id)) {
                return a.title;
            }
        }
        return id; // fallback so it never returns null
    }
}
