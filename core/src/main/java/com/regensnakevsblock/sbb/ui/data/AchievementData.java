package com.regensnakevsblock.sbb.ui.data;


public class AchievementData {
    public String id;
    public String title;
    public String description;
    public int progress;
    public int target;
    public int reward;
    public int colorId=-1;
    public boolean claimed;
    public boolean completed;
    public final AchievementType type;


    public AchievementData(String id, String title, String description, int progress, int target,
                           int reward, boolean isClaimed, boolean isCompleted, AchievementType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.progress = progress;
        this.target = target;
        this.reward = reward;
        this.claimed = isClaimed;
        this.completed = isCompleted;
        this.type = type;
    }
    public AchievementData(String id, String title, String description, int progress, int target,
                           int reward, boolean isClaimed, boolean isCompleted, AchievementType type, int colorId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.progress = progress;
        this.target = target;
        this.reward = reward;
        this.claimed = isClaimed;
        this.completed = isCompleted;
        this.type = type;
        this.colorId =colorId;
    }
    public enum AchievementType {
        BLOCKS_DESTROYED_TOTAL,
        COLORED_BLOCKS_DESTROYED,
        BLOCKS_DESTROYED_STREAK,
        WIN_BODIES_STREAK,
        SCORE_MARK,
        BEAT_OWN_HIGH_SCORE,
        LIFETIME_COINS,
        SNAKE_LENGTH,
        TIME_ON_GAME,
        ROWS_CLEARED,
        BLOCK_SIZE_DESTROYED,
        TEST
    }
    public boolean isCompleted() {
        return completed;
    }

    public boolean isClaimed() {
        return claimed;
    }

    public boolean canClaim() {
        return completed && !claimed;
    }

    public void updateProgress(int value) {
        if (claimed) {
            return;
        }

        progress = Math.min(value, target);

        if (progress >= target) {
            completed = true;
        }
    }

    public void claim() {
        if (completed && !claimed) {
            claimed = true;
        }
    }
}
