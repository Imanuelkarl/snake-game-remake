package com.regensnakevsblock.sbb.world;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.Snake;
import com.regensnakevsblock.sbb.enumaretors.PowerUp;
import com.regensnakevsblock.sbb.levels.Level;
import com.regensnakevsblock.sbb.levels.TestLevelProvider;
import com.regensnakevsblock.sbb.service.SaveService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorldState {

    private boolean soundEnabled;
    private boolean musicEnabled;
    private Block activeBlock;
    private boolean moving;


    private int maxScore=50;
    private int minScore=1;
    private boolean awake =true;

    private int moveFactor;

    private float blockDimension;
    private float snakeRadius;

    private Snake snake;

    private Level level;
    private boolean isPaused;
    private boolean isGameOver;
    //private boolean isVictory;
    private final MyGame game;
    private float nextRowY=0f;

    private float gameSpeed;

    private int score;
    private int highScore;

    private boolean powerUpIsActive;
    private PowerUp powerUp;
    private ArrayList<Row> rows;
    private int coins;
    private int totalDestroyed = 0;
    private int totalRowsCreated = 0;
    private GameMode gameMode;
    private SaveService saveService;
    private final Map<Integer , Integer> blocksDestroyedColor;
    private boolean finishLineSpawned = false;
    private HitListener hitListener;

    public boolean isFinishLineSpawned() {
        return finishLineSpawned;
    }

    public void setFinishLineSpawned(boolean value) {
        this.finishLineSpawned = value;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public Level getLevel() {
        return level;
    }

    public enum GameMode{
        ENDLESS,
        LEVELS
    }

    public WorldState(OrthographicCamera camera,MyGame game,GameMode gameMode){
        this.game=game;
        this.camera=camera;
        this.isPaused=false;
        this.isGameOver=false;
        //this.isVictory=false;
        this.moving=true;
        this.moveFactor=1;
        this.gameSpeed=5.0f;
        rows= new ArrayList<>();
        blockDimension=camera.viewportWidth/5;
        snakeRadius=blockDimension/7;
        snake = new Snake(camera.viewportWidth / 2-snakeRadius, camera.viewportHeight / 2);
        snake.addSegments(5);
        this.blocksDestroyedColor=new HashMap<>();
        this.gameMode = gameMode;
        saveService = new SaveService();
        if(gameMode==GameMode.LEVELS){
            level =  TestLevelProvider.createTestLevels().get(2);
        }

        nextRowY=camera.viewportHeight+blockDimension ;
    }
    public void setOnHitListener(HitListener hitListener){
        this.hitListener = hitListener;
    }
    public void addColorDestroyed(Integer color){
        if(blocksDestroyedColor.containsKey(color)){
            blocksDestroyedColor.put(color,blocksDestroyedColor.get(color)+1);
        }
        else{
            blocksDestroyedColor.put(color,1);
        }
        totalDestroyed++;
    }


    // O(1), no branching
    public int getBlocksDestroyedForColor(int color) {
        return blocksDestroyedColor.getOrDefault(color, 0);
    }
    public int totalBlocksDestroyed(){

        return totalDestroyed;
    }
    public int getTotalRowsCreated(){
        return totalRowsCreated;
    }
    public Snake getSnake() {
        return snake;
    }

    public void setAwake(boolean awake) {
        this.awake = awake;
    }

    public void setSoundEnabled(boolean soundEnabled){
        this.soundEnabled=soundEnabled;
    }
    public void setMusicEnabled(boolean musicEnabled){
        this.musicEnabled=musicEnabled;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }
    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getMinScore() {
        return minScore;
    }
    public boolean isPowerUpIsActive() {
        return powerUpIsActive;
    }

    public void setPowerUpIsActive(boolean powerUpIsActive) {
        this.powerUpIsActive = powerUpIsActive;
    }

    public PowerUp getPowerUp() {
        return powerUp;
    }

    public void setPowerUp(PowerUp powerUp) {
        this.powerUp = powerUp;
    }

    public void setBlockDimension(float blockDimension){
        this.blockDimension=blockDimension;
    }
    public float getSnakeRadius() {
        return snakeRadius;
    }
    public MyGame getGame(){
        return game;
    }

    public void reviveSnake(int length) {
        isGameOver=false;
        isPaused=false;
        this.setBreakingBlock(null);
        this.setMoving(false);
        this.setAwake(false);
        snake.addSegments(length);
    }

    public void setSnakeRadius(float snakeRadius) {
        this.snakeRadius = snakeRadius;
    }
    public float getBlockDimension() {
        return blockDimension;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }



    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }


    public OrthographicCamera getCamera() {
        return camera;
    }
    public int getMoveFactor() {
        return moveFactor;
    }


    public void setMoveFactor(int moveFactor) {
        this.moveFactor = moveFactor;
    }


    private final OrthographicCamera camera;


    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }

    public void update(float deltaTime){

    }

    public float getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(float gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public void setRows(ArrayList<Row> rows) {
        this.rows = rows;
    }

    public void addRows(Row row){
        this.rows.add(row);
        totalRowsCreated++;
    }
    public boolean canCreateRows(){
        if(gameMode ==GameMode.LEVELS){
          if(level.getLevelType() == Level.LevelType.FINISH_LINE){
              return rows.size()<=16&&totalRowsCreated<=level.getNoOfRows();
          }
        }
        return rows.size()<=16;
    }
    public void removeRow(int id){
        this.rows.remove(id);
    }

    public float getNextRowY() {
        return nextRowY;
    }

    public void setNextRowY(float nextRowY) {
        this.nextRowY = nextRowY;
    }

    public float getScreenWidth() {
        return camera.viewportWidth;
    }
    public float getScreenHeight(){
        return camera.viewportHeight;
    }

    public boolean isSoundEnabled() {
        return this.soundEnabled;
    }

    public void setBreakingBlock(Block block) {
        this.activeBlock=block;
    }

    public Block getActiveBlock() {
        return activeBlock;
    }

    public void setMoving(boolean b) {
        if(!awake){
            return;
        }
        this.moving=b;
        moveFactor=moving?1:0;
    }

    public boolean isMoving() {
        return moving;
    }

    public boolean isAwake() {
        return awake;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getHighScore() {
        return highScore;
    }

    public void setHighScore(int highScore) {
        this.highScore = highScore;
    }

    public float getSpeed() {
        return gameSpeed*this.getMoveFactor() * Gdx.graphics.getDeltaTime() * 100;
    }

    public int getScoreMultiplier() {
        return (powerUpIsActive&&powerUp==PowerUp.MULTIPLIER)?2:1;
    }

    public int getCoins() {
        return coins;
    }
    public void setCoins(int coins){
        this.coins = coins;
    }

    public HitListener getHitListener() {
        return hitListener;
    }
}
