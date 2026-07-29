package com.regensnakevsblock.sbb.world;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.assets.Assets2;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.Snake;
import com.regensnakevsblock.sbb.enumaretors.PowerUp;
import com.regensnakevsblock.sbb.levels.Level;
import com.regensnakevsblock.sbb.levels.TestLevelProvider;
import com.regensnakevsblock.sbb.screens.GameOverScreen;
import com.regensnakevsblock.sbb.service.SaveService;
import com.regensnakevsblock.sbb.utils.TextureTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WorldState {

    private boolean soundEnabled;
    private boolean musicEnabled;
    private int soundVolume;
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

    private boolean powerUpActive;
    private PowerUp powerUp;
    private ArrayList<Row> rows;
    private int coins;
    private int totalDestroyed = 0;
    private int totalRowsCreated = 0;
    private boolean startGame =false;
    private GameMode gameMode;
    private SaveService saveService;
    private final Map<Integer , Integer> blocksDestroyedColor;
    private boolean finishLineSpawned = false;
    private HitListener hitListener;
    float powerUpTimeRemaining =3000;
    private HashMap<Integer, TextureRegion> levelBlocks;

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

    public float getPowerUpTimeRemaining() {
        return powerUpTimeRemaining;
    }

    public void setPowerUpTimeRemaining(float remaining) {
        powerUpTimeRemaining=remaining;
    }

    private boolean showTaskIntro;
    private float taskIntroTime;

    public boolean isShowTaskIntro() {
        return showTaskIntro;
    }

    public void startTaskIntro() {
        showTaskIntro = true;
        taskIntroTime = 0;
    }

    public void updateTaskIntro(float delta) {
        if(!showTaskIntro) return;

        taskIntroTime += delta;

        if(taskIntroTime >= 3f){
            showTaskIntro = false;
            startGame=true;
        }
    }

    public float getTaskIntroTime() {
        return taskIntroTime;
    }

    public void setLevelCleared() {
        if(saveService.getSelectedLevel()==saveService.getMaxLevel()){
            saveService.setMaxLevel(saveService.getMaxLevel()+1);
            saveService.saveSelectedLevel(saveService.getSelectedLevel()+1);
            GameInstance.getInstance().getLevelData().get(level.getLevelId()).setCanSelect(true);
        }
        level.setCleared(true);
        GameInstance.getInstance().clearLevelId(level.getLevelId());
        GameInstance.getInstance().levelsView.rebindView();
        GameOverScreen gameOverScreen =new GameOverScreen(game,true);
        gameOverScreen.setScore(score);
        gameOverScreen.setCoins(coins);
        game.setScreen(gameOverScreen);
    }

    public boolean getStartGame() {
        return startGame;
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
            level = GameInstance.getInstance().getById(saveService.getSelectedLevel());//TestLevelProvider.createTestLevels().get(1);
            buildLevel();
        }else{
            startGame=true;
        }
        nextRowY=camera.viewportHeight+blockDimension ;
    }
    private void buildLevel(){
        if(level.getLevelType()== Level.LevelType.BLOCKS){
            levelBlocks=new HashMap<>();
            //TextureRegion blockTexture = TextureTools.resolveRegion(Assets2.getInstance().blockTexture,2,5,(int)Block.getBlockPosition(0).x,(int)Block.getBlockPosition(0).y,0,0);
            levelBlocks.put(0,Assets2.getInstance().defaultBlock);
        }
        if(level.getLevelType()== Level.LevelType.COLORED_BLOCKS){
            levelBlocks=new HashMap<>();
            for (Map.Entry<Integer, Integer> entry : level.getColorIntegerMap().entrySet()) {

                int colorId = entry.getKey();
                int required = entry.getValue();
                TextureRegion blockTexture = TextureTools.resolveRegion(Assets2.getInstance().blockTexture,2,5,(int)Block.getBlockPosition(colorId).y,(int)Block.getBlockPosition(colorId).x,0,0);
                levelBlocks.put(colorId,blockTexture);
            }
        }
    }
    public HashMap<Integer, TextureRegion> getLevelBlocks() {
        return levelBlocks;
    }
    public void setSoundVolume(int soundVolume) {
        this.soundVolume = soundVolume;
    }
    public int getSoundVolume() {
        return soundVolume;
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
    public int getSnakeSkinId(){
        return saveService.getSelectedSkinIndex();
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
    public boolean isPowerUpActive() {
        return powerUpActive;
    }

    public void setPowerUpActive(boolean powerUpActive) {
        this.powerUpActive = powerUpActive;
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
        updateTaskIntro(deltaTime);
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
        return (powerUpActive &&powerUp==PowerUp.MULTIPLIER)?2:1;
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
