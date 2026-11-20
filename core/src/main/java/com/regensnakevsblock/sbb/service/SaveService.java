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
    public void clearData(){
        myPreferences.clear();
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
        return Math.toIntExact(getNumberValue("selectedSkinIndex"));
    }

    public void saveSelectedSkinIndex(int selectedSkinIndex) {
        this.selectedSkinIndex = selectedSkinIndex;
        saveIntegerData("selectedSkinIndex", (long) selectedSkinIndex);
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
        return getBooleanValue("firstTime",true);
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
    private boolean getBooleanValue(String key, boolean defaultValue) {
        return myPreferences.getBoolean(key,defaultValue);
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
    private void saveInteger(String key,int value){
        myPreferences.putInteger(key,value);
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

    public int getSoundVolume() {
        return myPreferences.getInteger("soundVolume",100);
    }
    public void saveSoundVolume(int soundVolume) {
        myPreferences.putInteger("soundVolume",soundVolume);
        myPreferences.flush();
    }

    public int getMusicVolume() {
        return myPreferences.getInteger("musicVolume",100);
    }

    public void saveMusicVolume(int musicVolume) {
        myPreferences.putInteger("musicVolume",musicVolume);
        myPreferences.flush();
    }

    public void saveDifficulty(int index) {
        myPreferences.putInteger("difficulty",index);
        myPreferences.flush();
    }
    public int getDifficulty(){
        return myPreferences.getInteger("difficulty",0);
    }

    public void saveUpgrade(String name, int currentLevel) {
        myPreferences.putInteger(name,currentLevel);
        myPreferences.flush();
    }
    public void savePlayCount(){
        myPreferences.putInteger("playCount",getPlayCount()+1);
        myPreferences.flush();
    }
    public int getPlayCount(){
        return myPreferences.getInteger("playCount",0);
    }
    public int getUpgradeLevel(String name){
        return myPreferences.getInteger(name,1);
    }

    public boolean getSkinIsPurchased(int i) {
        return myPreferences.getBoolean("skin_"+i,false);
    }

    public void saveSkinAsPurchased(String id) {
        myPreferences.putBoolean(id,true);
        myPreferences.flush();
    }

    public void saveTutorialIsPlayed(boolean b) {
        saveBooleanData("tutorialIsPlayed",b);
    }
    public boolean getTutorialIsPlayed(){
        return getBooleanValue("tutorialIsPlayed",false);
    }

    public void saveLifeTimeCoins(long coins){
        saveIntegerData("lifeTimeCoins",coins);
    }
    public long getLifeTimeCoins() {
        return myPreferences.getLong("lifeTimeCoins",0);
    }

    public void saveAchievementIsClaimed(String id,boolean claimed) {
        saveBooleanData(id+"claim",claimed);
    }
    public void saveAchievementIsCompleted(String id,boolean completed){
        saveBooleanData(id+"complete",completed);
    }
    public boolean getAchievementIsClaimed(String id) {
        return myPreferences.getBoolean(id+"claim",false);
    }

    public boolean getAchievementIsCompleted(String id) {
        return myPreferences.getBoolean(id+"complete",false);
    }
}
