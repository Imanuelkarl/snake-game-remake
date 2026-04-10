package com.regensnakevsblock.sbb.ui.data;


public class AchievementData {
    public String id;
    public String title;
    public String description;
    public int progress;
    public int target;
    public int reward;
    public boolean isClaimed;
    public boolean isCompleted;

    public AchievementData(String id, String title, String description, int progress, int target,
                           int reward, boolean isClaimed, boolean isCompleted) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.progress = progress;
        this.target = target;
        this.reward = reward;
        this.isClaimed = isClaimed;
        this.isCompleted = isCompleted;
    }
}
