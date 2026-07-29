package com.regensnakevsblock.sbb.levels;

public class LevelTaskAnimation {
    private boolean isAnimating = false;
    private float animationTimer = 0f;
    private float currentY;
    private float startY;
    private float targetY;
    private float alpha = 1f;
    private static final float ANIMATION_DURATION = 0.8f;

    public void startAnimation(float screenWidth, float screenHeight) {
        isAnimating = true;
        animationTimer = 0f;
        startY = screenHeight / 2f;  // Start at center
        targetY = screenHeight - 100f;  // End at top position
        currentY = startY;
        alpha = 1f;
    }

    public void update(float deltaTime) {
        if (!isAnimating) return;

        animationTimer += deltaTime;

        if (animationTimer >= ANIMATION_DURATION) {
            isAnimating = false;
            currentY = targetY;
        } else {
            // Simple linear interpolation
            float t = animationTimer / ANIMATION_DURATION;
            currentY = startY + (targetY - startY) * t;
        }
    }

    public boolean isAnimating() { return isAnimating; }
    public float getCurrentY() { return currentY; }
    public float getAlpha() { return alpha; }
}
