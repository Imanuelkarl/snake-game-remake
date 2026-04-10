package com.darealfungames.snakevsblock.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.darealfungames.snakevsblock.MyGame;
import com.darealfungames.snakevsblock.config.Constants;
import com.darealfungames.snakevsblock.inputs.InputController;
import com.darealfungames.snakevsblock.ui.UIRenderer;
import com.darealfungames.snakevsblock.world.WorldController;
import com.darealfungames.snakevsblock.world.WorldRenderer;
import com.darealfungames.snakevsblock.world.WorldState;

public class MainGame implements Screen {

    private final MyGame game;
    private OrthographicCamera camera;
    private WorldController worldController;
   private WorldRenderer worldRenderer;
   private UIRenderer uiRenderer;
    private InputController inputController;
    private WorldState worldState;

    // Game state
    private boolean isPaused = false;
    private boolean gameOver = false;

    private final int gameSize;

    private Stage stage;
    private float speed =5;
    private ShapeRenderer shapeRenderer;

    private Group pauseMenuGroup;

    private Group gameOverGroup;

    public MainGame(MyGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        gameSize= Constants.SCREEN_SIZE;
        camera.setToOrtho(false,  (float) (gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight(),gameSize);
        // Initialize game world
        initializeWorld();
    }

    private void initializeWorld() {
        worldState = new WorldState(camera,game);
        worldController = new WorldController(worldState);
        worldRenderer = new WorldRenderer( worldState);
        int gameSize = Constants.SCREEN_SIZE;
        int WORLD_WIDTH=/*Constants.SCREEN_WIDTH;*/(gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        this.stage = new Stage(viewport);
        uiRenderer = new UIRenderer(stage, worldState);
        inputController = new InputController(worldController);


        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);          // UI FIRST
        multiplexer.addProcessor(inputController);      // Your InputProcessor
        // Set input processor
        Gdx.input.setInputProcessor(multiplexer);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Clear screen
        Gdx.gl.glClearColor(0.085f, 0.04f, 0.06f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        if (!isPaused && !gameOver) {
            // Update game logic
            worldController.update(delta);

            // Check game over condition

        }
        gameOver=worldState.isGameOver();

        // Render game world
        worldRenderer.render();

        //Render Game UI
        uiRenderer.render();

        // Render pause menu if needed
        if (isPaused) {
            renderPauseMenu();
        }

        // Render game over screen if needed
        if (gameOver) {
            renderGameOverScreen();
        }
    }

    private void renderPauseMenu() {
        // Implement pause menu rendering
    }

    private void renderGameOverScreen() {
        // Implement game over screen rendering
    }

    public void pauseGame() {
        isPaused = true;
    }

    public void resumeGame() {
        isPaused = false;
    }

    public void restartGame() {
        gameOver = false;
        isPaused = false;
        initializeWorld();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = gameSize*((float) width /height);
        camera.viewportHeight = gameSize;
        camera.update();
    }

    @Override
    public void pause() {
        pauseGame();
    }

    @Override
    public void resume() {
        resumeGame();
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        worldRenderer.dispose();
        uiRenderer.dispose();
    }
}
