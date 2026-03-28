package com.darealfungames.snakevsblock.uiactions;

public interface HomeUiListener {
    void onPlayClicked();
    void onSettingsClicked();
    void onExitClicked();

    void onShareClicked();

    void onSkinClicked();

    void onDialogCancelled();

    void onSoundSwitched(boolean isOn);

    void onMusicSwitched(boolean isOn);

    void onCoinAddClicked();

    void onDiamondAddClicked();

    void onHealthAddClicked();
}
