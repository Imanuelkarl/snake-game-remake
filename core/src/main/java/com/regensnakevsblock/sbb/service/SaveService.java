package com.regensnakevsblock.sbb.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveService {

    private boolean firstTimePlaying;

    private boolean musicOn;

    private boolean soundOn;

    private int highScore;

    private int coins;

    private int selectedSkinIndex;

    private int[] ownedSkins;

    private int healths;

    private int diamonds;
    public SaveService() {
        // Initialize default values
        Preferences myPreferences= Gdx.app.getPreferences("GameData");
        firstTimePlaying=myPreferences.getBoolean("firstTimePlaying",true);
        musicOn=myPreferences.getBoolean("musicOn",true);
        soundOn=myPreferences.getBoolean("soundOn",true);
        highScore=myPreferences.getInteger("highScore",0);
        coins=myPreferences.getInteger("coins",0);
    }
    public void saveGameData() {

    }
    public void loadGameData() {

    }

}
