package com.regensnakevsblock.sbb.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.regensnakevsblock.sbb.controllers.ScreenController;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Snake;
import com.regensnakevsblock.sbb.systems.CollisionSystem;
import com.regensnakevsblock.sbb.systems.DifficultySystem;
import com.regensnakevsblock.sbb.systems.MovementSystem;
import com.regensnakevsblock.sbb.systems.PowerUpSystem;
import com.regensnakevsblock.sbb.systems.ScoringSystem;
import com.regensnakevsblock.sbb.systems.SpawnSystem;

import java.util.ArrayList;

public class WorldController implements ScreenController {
    private WorldState worldState;
    private MovementSystem movementSystem;
    private CollisionSystem collisionSystem;
    private SpawnSystem spawnSystem;
    private PowerUpSystem powerUpSystem;
    private ScoringSystem scoringSystem;
    private DifficultySystem difficultySystem;

    private OrthographicCamera camera;
    float speed = 5f;
    float startSpeed =speed;
    float maxSpeed = 20.0f;
    float lerpSpeed = 0.08f;
    float step = 0.1f;
    float timer = 0f;
    float interval = 0.2f;

    float timeToMax = 220f;
    float increaseRate = (maxSpeed - startSpeed) / timeToMax;
    private boolean goLeft;

    public boolean isGoRight() {
        return goRight;
    }

    public void setGoRight(boolean goRight) {
        this.goRight = goRight;
    }

    public boolean isGoLeft() {
        return goLeft;
    }

    public void setGoLeft(boolean goLeft) {
        this.goLeft = goLeft;
    }

    private boolean goRight;

    private float initialXValue;

    //private int dir = 1;

    public WorldController(WorldState worldState) {
        this.worldState = worldState;
        this.camera=worldState.getCamera();
        this.movementSystem = new MovementSystem(worldState);
        this.collisionSystem = new CollisionSystem(worldState);
        this.spawnSystem = new SpawnSystem(worldState);
        this.powerUpSystem = new PowerUpSystem(worldState);
        this.scoringSystem = new ScoringSystem(worldState);
        this.difficultySystem = new DifficultySystem(worldState);
    }

    @Override
    public void update(float deltaTime) {
        // Update all game systems
        movementSystem.update(deltaTime);
        collisionSystem.update(deltaTime);
        spawnSystem.update(deltaTime);
        //powerUpSystem.update(deltaTime);
        scoringSystem.update(deltaTime);
        //difficultySystem.update(deltaTime);
        // Keyboard snake movement
        initialXValue=keyboardMoveSnakePosition();
        // Update world state


        if (speed < maxSpeed) {
            speed += increaseRate * deltaTime;
            if (speed > maxSpeed) {
                speed = maxSpeed;
            }
        }

        worldState.setMinScore(Math.max(1, Math.min(worldState.getSnake().getLength()/2+1,worldState.getMaxScore()-10)));
        worldState.setGameSpeed(speed);
        worldState.update(deltaTime);

    }

    public void usePowerUp(int powerUpId) {
        powerUpSystem.activatePowerUp(powerUpId);
    }

    public void pauseGame() {
        worldState.setPaused(true);
    }

    public void resumeGame() {
        worldState.setPaused(false);
    }


    public void moveSnakeX(float x) {
        Snake snake=worldState.getSnake();

        if(!worldState.isPaused()&&snake!=null){
            float newX = snake.getPosition().x + x;

            /*if(newX<snake.getX()){
                dir=+1;
            }

            else{
                dir=-1;
            }*/
            if(newX>0+snake.getWidth()/2&&newX<worldState.getScreenWidth()-snake.getWidth()/2){
                snake.setX(newX);
            }
        }
        else{
            System.out.println("Game is paused or snake is null, cannot move.");
        }
    }
    public float keyboardMoveSnakePosition(){
        Snake snake =worldState.getSnake();
        float newPositionX;
        float initialTouchX =snake.getPosition().x;
        if(goLeft){
            //System.out.println("Moving snake to the left");
            newPositionX=initialTouchX-12* Gdx.graphics.getDeltaTime()* worldState.getScreenWidth()/10;
            if(newPositionX>0+snake.getWidth()/2&&newPositionX< worldState.getScreenWidth() -snake.getWidth()/2){
                snake.setX(newPositionX);
                initialTouchX= snake.getPosition().x;

            }
        }
        if(goRight){
            newPositionX=initialTouchX+12*Gdx.graphics.getDeltaTime()* worldState.getScreenWidth()/10;
            if(newPositionX>0+snake.getWidth()/2&&newPositionX<worldState.getScreenWidth()-snake.getWidth()/2){
                snake.setX(newPositionX);
                initialTouchX=snake.getPosition().x;

            }
        }
        return initialTouchX;
    }

    public float getInitialXValue(){
        return initialXValue;
    }

    public void wake() {
        worldState.setAwake(true);
    }
}
