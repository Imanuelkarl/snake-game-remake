package com.regensnakevsblock.sbb.systems;

import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.ui.data.Ach;
import com.regensnakevsblock.sbb.ui.data.AchievementData;
import com.regensnakevsblock.sbb.ui.data.AchievementList;
import com.regensnakevsblock.sbb.world.WorldState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * Tracks and unlocks achievements in real time by polling values out of WorldState.
 *
 * Design: every achievement is just an id + a target number + a Supplier<Integer> that
 * returns the "current value" for that achievement. update() checks every locked
 * achievement each frame and unlocks it once currentValue >= target. This means adding a
 * brand new achievement type is usually just one new registration method below -- no new
 * switch/case branches, no new fields on this class.
 */
public class AchievementSystem extends GlobalSystems {

    public interface AchievementListener {
        void onAchievementUnlocked(Achievement achievement);
    }

    public static class Achievement {
        public final String id;
        public final int target;
        private final Supplier<Integer> currentValueSupplier;
        private boolean unlocked = false;

        public Achievement(String id, int target,boolean unlocked, Supplier<Integer> currentValueSupplier) {
            this.id = id;
            this.target = target;
            this.unlocked = unlocked;
            this.currentValueSupplier = currentValueSupplier;
        }

        public int getCurrentValue() {
            return currentValueSupplier.get();
        }

        public boolean isUnlocked() {
            return unlocked;
        }
    }

    // The type tag your future id-mapped achievement list will carry per entry.
    // registerFromDefinition() below switches on this to know which factory
    // method to call.


    // A plain data holder matching one row of your (still in-progress) achievement
    // list -- id, which checker it maps to, its target, and colorId for the one
    // type that needs it. Build these from wherever that list ends up living
    // (JSON, an enum, a config class) and hand them to loadAchievements().

    private final List<Achievement> achievements = new ArrayList<>();
    private AchievementListener listener;


    // Accumulates real play time for survival-style achievements (timeOnGame).
    // WorldState doesn't track this yet, so we keep it here instead.
    private float elapsedPlayTime = 0f;

    public AchievementSystem(WorldState worldState) {
        super(worldState);
        List<AchievementData> achievements =new ArrayList<>();
        achievements.add(new AchievementData("ach_1", "First Blood", "Kill 10 enemies",
            7, 10, 500, false, false, AchievementData.AchievementType.LIFETIME_COINS));
        loadAchievements(GameInstance.getInstance().getAchievements());
    }

    // Call this once (e.g. right after construction) with your full achievement
    // list. This is what actually invokes the checkFor.../scoreMark/etc. methods
    // below -- each definition turns into exactly one registered Achievement.
    public void loadAchievements(List<AchievementData> definitions) {
        for (AchievementData def : definitions) {
            registerFromDefinition(def);
        }

    }

    private void registerFromDefinition(AchievementData def) {

        switch (def.type) {
            case BLOCKS_DESTROYED_TOTAL: //Confirmed Working Min-100
                checkForBlocksDestroyed(def.id, def.target,def.isCompleted());
                break;
            case COLORED_BLOCKS_DESTROYED://Confirmed Working Min 50 for each color
                coloredBlocksDestroyed(def.id, def.colorId, def.target,def.isCompleted());
                break;
            case BLOCKS_DESTROYED_STREAK://Confirmed Working minimum 2(practically easy) maximum 5
                blocksDestroyedInRow(def.id, def.target,def.isCompleted());
                break;
            case WIN_BODIES_STREAK://Confirmed Working minimum 2 maximum 5
                winBodiesCollectedInRow(def.id, def.target,def.isCompleted());
                break;
            case SCORE_MARK://Confirmed Working min  100
                scoreMark(def.id, def.target,def.isCompleted());
                break;
            case BEAT_OWN_HIGH_SCORE:
                beatOwnHighScore(def.id,def.isCompleted());
                break;
            case LIFETIME_COINS://Confirmed Working min 300
                lifetimeCoinsCollected(def.id, def.target,def.isCompleted());
                break;
            case SNAKE_LENGTH://Confirmed Working min 100
                growSnakeLength(def.id, def.target,def.isCompleted());
                break;
            case TIME_ON_GAME://Confirmed Working min 60
                timeOnGame(def.id, def.target,def.isCompleted());
                break;
            case ROWS_CLEARED://Confirmed Working min 50
                rowsCleared(def.id, def.target,def.isCompleted());
                break;
            case BLOCK_SIZE_DESTROYED://Confirmed Working minimum 40 max 80
                blockSizeDestroyed(def.id, def.target, def.isCompleted());
                break;
        }
    }

    public void setAchievementListener(AchievementListener listener) {
        this.listener = listener;
    }

    private void notifyAchievementUnlocked(Achievement achievement) {
        if (listener != null) {
            listener.onAchievementUnlocked(achievement);
        }
    }

    public List<Achievement> getAchievements() {
        return achievements;
    }

    private void addAchievement(Achievement achievement) {
        achievements.add(achievement);
    }

    @Override
    public void update(float deltaTime) {
        elapsedPlayTime += deltaTime;

        for (Achievement achievement : achievements) {
            if (!achievement.unlocked && achievement.getCurrentValue() >= achievement.target) {
                achievement.unlocked = true;
                notifyAchievementUnlocked(achievement);
            }
        }
    }

    private void unlock(Achievement achievement) {
        achievement.unlocked = true;
        if (listener != null) {
            listener.onAchievementUnlocked(achievement);
        }
        System.out.println("Achievement "+ achievement.id+" has been achieved");
        GameInstance.getSaveService().saveAchievementIsCompleted(achievement.id,true);
        worldState.addNewAchievements(new Ach(AchievementList.getTitle(achievement.id),false,achievement.id));
        for (int i=0;i<GameInstance.getInstance().getAchievements().size();i++) {
            AchievementData a = GameInstance.getInstance().getAchievements().get(i);
            if(a.id.equals(achievement.id)){
                GameInstance.getInstance().getAchievements().get(i).completed=true;
                if(GameInstance.getInstance().menuViewManagerAdapter!=null) {
                    //GameInstance.getInstance().menuViewManagerAdapter.getView(2).updateData(GameInstance.getInstance().getAchievementDataById(achievement.id),i);
                } else{
                    GameInstance.getInstance().getAchievements().get(i).completed=true;
                }
            }

        }


    }

    // ---------------------------------------------------------------
    // Registration methods -- call these once (e.g. when loading the
    // achievement list from your id-mapped definitions) to wire each
    // achievement up to the right WorldState value.
    // ---------------------------------------------------------------

    // Total blocks destroyed across the game/session.
    public void checkForBlocksDestroyed(String id, int target,boolean unlocked) {
        addAchievement(new Achievement(id, target,unlocked, worldState::totalBlocksDestroyed));
    }

    // Blocks of a specific color destroyed. Uses the existing per-color map on WorldState.
    public void coloredBlocksDestroyed(String id, int colorId, int target,boolean unlocked) {
        addAchievement(new Achievement(id, target,unlocked,
            () -> worldState.getBlocksDestroyedForColor(colorId)));
        System.out.println("Registered colored block");
    }

    // Consecutive blocks destroyed without a miss.
    // REQUIRES: WorldState.getCurrentBlockStreak() -- add an int field that increments
    // on every successful block hit and resets to 0 the moment the player misses/gets hit.
    public void blocksDestroyedInRow(String id, int target,boolean unlocked) {
        addAchievement(new Achievement(id, target,unlocked, worldState::getCurrentBlockStreak));
    }

    // Consecutive "win bodies" (power-ups/pickups?) collected without a miss.
    // REQUIRES: WorldState.getCurrentWinBodyStreak() -- same pattern as the block streak above.
    public void winBodiesCollectedInRow(String id, int quantity,boolean unlocked) {
        addAchievement(new Achievement(id, quantity,unlocked, worldState::getWinBodyStreak));
    }

    // Raw score threshold.
    public void scoreMark(String id, int target,boolean unlocked) {
        addAchievement(new Achievement(id, target,unlocked, worldState::getScore));
    }

    // One-shot: unlocks the moment current score exceeds the stored high score.
    public void beatOwnHighScore(String id,boolean unlocked) {
        addAchievement(new Achievement(id, 1,unlocked,
            () -> worldState.getScore() > worldState.getHighScore() ? 1 : 0));
    }

    // Lifetime coins collected.
    // REQUIRES: WorldState.getLifetimeCoins() -- getCoins() looks like it's the current
    // run's coin count, so you'll want a separate persisted total (likely via SaveService).
    public void lifetimeCoinsCollected(String id, int target,boolean unlocked) {
        addAchievement(new Achievement(id, target,unlocked, worldState::getLifetimeCoins));
    }

    // Snake length.
    // REQUIRES: Snake.getLength() -- expose the segment count from the Snake entity.
    public void growSnakeLength(String id, int target,boolean unlocked) {
        addAchievement(new Achievement(id, target,unlocked, () -> worldState.getSnake().getLength()));
    }

    // Survival time in seconds, tracked internally via elapsedPlayTime.
    public void timeOnGame(String id, int targetSeconds,boolean unlocked) {
        addAchievement(new Achievement(id, targetSeconds,unlocked, () -> (int) elapsedPlayTime));
    }

    // Rows cleared -- WorldState already tracks this via getClearedRows()/setClearedRows().
    public void rowsCleared(String id, int target,boolean unlocked) {
        addAchievement(new Achievement(id, target,unlocked, worldState::getClearedRows));
    }

    // Largest single block size destroyed in one hit.
    // REQUIRES: WorldState.getLargestBlockSizeDestroyed() -- update it whenever a block is
    // destroyed, comparing against the block's size and keeping the max.
    public void blockSizeDestroyed(String id, int target,boolean unlocked) {
        addAchievement(new Achievement(id, target,unlocked,
            () -> Math.min(worldState.getLargestBlockSizeDestroyed(), target)));
    }
}
