package com.regensnakevsblock.sbb.uifactory;

public class GameOverLayout {

    public static class Rect {
        public float x, y, width, height;

        public Rect(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

    public Rect dialog;
    public Rect title;

    public Rect score;
    public Rect highScore;
    public Rect coins;

    public Rect homeBtn;
    public Rect replayBtn;

    public GameOverLayout(float W, float H) {

        // Dialog
        dialog = new Rect(
            0.05f * W,
            0.40f * H,
            0.9f * W,
            0.42f * H
        );

        // Title bar

        title = new Rect(
            0.18f * W,
            0.78f * H,
            0.64f * W,
            0.085f * H
        );

        // Score rows
        float rowWidth = 0.56f * W;
        float rowHeight = 0.075f * H;
        float rowX = 0.22f * W;

        score = new Rect(rowX, 0.67f * H, rowWidth, rowHeight);
        highScore = new Rect(rowX, 0.575f * H, rowWidth, rowHeight);
        coins = new Rect(rowX, 0.48f * H, rowWidth, rowHeight);

        // Buttons
        float btnWidth = 0.32f * W;
        float btnHeight = 0.11f * H;

        homeBtn = new Rect(
            0.08f * W,
            0.12f * H,
            btnWidth,
            btnHeight
        );

        replayBtn = new Rect(
            0.60f * W,
            0.12f * H,
            btnWidth,
            btnHeight
        );
    }
}
