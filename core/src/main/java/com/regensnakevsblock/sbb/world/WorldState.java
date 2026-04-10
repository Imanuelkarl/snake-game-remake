package com.regensnakevsblock.sbb.world;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.Snake;
import com.regensnakevsblock.sbb.enumaretors.PowerUp;

import java.util.ArrayList;

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
    private String gameMode;

    private Snake snake;

    private boolean isPaused;
    private boolean isGameOver;
    private MyGame game;
    private float nextRowY=0f;

    private float gameSpeed;

    private int score;
    private int highScore;

    private boolean powerUpIsActive;
    private PowerUp powerUp;
    private ArrayList<Row> rows;

    public WorldState(OrthographicCamera camera,MyGame game){
        this.game=game;
        this.camera=camera;
        this.isPaused=false;
        this.isGameOver=false;
        this.moving=true;
        this.moveFactor=1;
        this.gameSpeed=5.0f;
        rows= new ArrayList<>();
        blockDimension=camera.viewportWidth/5;
        snakeRadius=blockDimension/7;
        snake = new Snake(camera.viewportWidth / 2-snakeRadius, camera.viewportHeight / 2);
        snake.addSegments(5);

        nextRowY=camera.viewportHeight+blockDimension ;
    }


    public void setSnake(Snake snake) {
        this.snake = snake;
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

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
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
}
