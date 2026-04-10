package com.regensnakevsblock.sbb.inputs;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.regensnakevsblock.sbb.world.WorldController;

public class InputController implements InputProcessor {
    private final WorldController worldController;

    private float initialTouchX;
    public InputController(WorldController worldController) {
        this.worldController=worldController;

    }

    @Override
    public boolean keyDown(int keycode) {

        if(keycode== Input.Keys.LEFT){
            worldController.setGoLeft(true);

        }
        if(keycode== Input.Keys.RIGHT){

            worldController.setGoRight(true);
        }

        initialTouchX =worldController.getInitialXValue();
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode== Input.Keys.LEFT){
            worldController.setGoLeft(false);
        }
        if(keycode== Input.Keys.RIGHT){

            worldController.setGoRight(false);
        }
        initialTouchX=0;
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        initialTouchX =screenX;
        worldController.wake();
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        initialTouchX =0;
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        float deltaX = screenX - initialTouchX;
        worldController.moveSnakeX(deltaX);
        initialTouchX = screenX;


        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
