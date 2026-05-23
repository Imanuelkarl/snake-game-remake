package com.regensnakevsblock.sbb.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class SaveService {

    private boolean firstTimePlaying;
    Preferences myPreferences;

    private boolean musicOn;

    private boolean soundOn;

    private int highScore;

    private Long coins;

    private int selectedSkinIndex;

    private int[] ownedSkins;
    private boolean acceptTerms;

    private Long healths;
    private Long diamonds;

    public Long getDiamonds() {
        return getNumberValue("diamonds");
    }

    public void saveDiamonds(Long diamonds) {
        saveIntegerData("diamonds",diamonds);
        this.diamonds = diamonds;
    }

    public Long getHealths() {
        return getNumberValue("health");
    }

    public void saveHealths(Long healths) {
        saveIntegerData("health",healths);
        this.healths = healths;
    }

    public int[] getOwnedSkins() {
        return ownedSkins;
    }

    public void saveOwnedSkins(int[] ownedSkins) {
        this.ownedSkins = ownedSkins;
    }

    public int getSelectedSkinIndex() {
        return selectedSkinIndex;
    }

    public void saveSelectedSkinIndex(int selectedSkinIndex) {
        this.selectedSkinIndex = selectedSkinIndex;
    }
    public void saveAcceptTerms(boolean acceptTerms){
        saveBooleanData("acceptTerms",acceptTerms);
    }
    public boolean getAcceptTerms(){
        return getBooleanValue("acceptTerms");
    }



    public Long getCoins() {
        return getNumberValue("coins");
    }

    public void saveCoins(Long coins) {
        saveIntegerData("coins", coins);
        this.coins = coins;
    }

    public Long getHighScore() {
        return getNumberValue("highScore");
    }

    public void saveHighScore(int highScore) {
        saveIntegerData("highScore", (long) highScore);
        this.highScore = highScore;
    }

    public boolean isSoundOn() {
        return soundOn;
    }

    public void saveSoundOn(boolean soundOn) {
        this.soundOn = soundOn;
    }

    public boolean isMusicOn() {
        return musicOn;
    }

    public void saveMusicOn(boolean musicOn) {
        this.musicOn = musicOn;
    }

    public boolean isFirstTimePlaying() {
        return getBooleanValue("firstTime");
    }

    public void saveFirstTimePlaying(boolean firstTimePlaying) {
        saveBooleanData("firstTime",firstTimePlaying);
        this.firstTimePlaying = firstTimePlaying;
    }


    public SaveService() {
        // Initialize default values
        myPreferences= Gdx.app.getPreferences("GameData");
        firstTimePlaying=myPreferences.getBoolean("firstTimePlaying",true);
        musicOn=myPreferences.getBoolean("musicOn",true);
        soundOn=myPreferences.getBoolean("soundOn",true);
        highScore= (int) myPreferences.getLong("highScore",0);
        coins=myPreferences.getLong("coins",0);
    }
    public void saveGameData() {

    }
    public void loadGameData() {

    }
    private void saveBooleanData(String key, boolean value) {
        myPreferences.putBoolean(key,value);
        myPreferences.flush();
    }
    private boolean getBooleanValue(String key) {
        return myPreferences.getBoolean(key);
    }
    private void saveStringData(String key, String value) {
        myPreferences.putString(key,value);
        myPreferences.flush();
    }
    private void saveIntegerData(String key, Long value) {
        myPreferences.putLong(key,value);
        myPreferences.flush();
    }
    private Long getNumberValue(String key){
        return myPreferences.getLong(key);
    }

    public int getSelectedLevel() {
        return myPreferences.getInteger("selectedLevel",1);
    }

    public void saveSelectedLevel(int selectedLevel) {
        myPreferences.putInteger("selectedLevel",selectedLevel);
        myPreferences.flush();
    }
    public void setMaxLevel(int maxLevel){
        myPreferences.putInteger("maxLevel",maxLevel);
        myPreferences.flush();
    }
    public int getMaxLevel(){
        return myPreferences.getInteger("maxLevel",1);
    }
}
