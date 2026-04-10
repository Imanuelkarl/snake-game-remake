package com.darealfungames.snakevsblock.uiactions;

public interface LevelUIListener {
    void onPlayButtonClicked(int levelIndex);
    void onNextLevelButtonClicked();
    void onLevelSelected(int levelIndex);
    void onCloseLevelScreen();
    void onCloseDialog();
    void onPlayAgainButtonClicked();

}
