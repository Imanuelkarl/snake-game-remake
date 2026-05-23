package com.regensnakevsblock.sbb.systems;

import com.badlogic.gdx.Gdx;
import com.regensnakevsblock.sbb.config.PowerUpDimensions;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.WinBody;
import com.regensnakevsblock.sbb.enumaretors.PowerUp;
import com.regensnakevsblock.sbb.world.WorldState;

public class PowerUpSystem extends GlobalSystems{
    public PowerUpSystem(WorldState worldState) {
        super(worldState);
    }

    @Override
    public void update(float deltaTime) {
        float width = worldState.getScreenWidth();
        float height =worldState.getScreenHeight();
        for (Row row: worldState.getRows()) {
            for (WinBody winBody : row.getWinBodies()) {
                float dx = worldState.getSnake().getX() - winBody.getX();
                float dy = worldState.getSnake().getY() - winBody.getY();

                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                // Magnet activation
                if (worldState.isPowerUpIsActive() &&
                    worldState.getPowerUp().equals(PowerUp.MAGNET)) {

                    float magnetRadius = width * 0.5f; // or define properly

                    if (distance <= magnetRadius) {
                        winBody.setAdjustable(true);
                    }
                }

                // Attraction
                if (winBody.isAdjust()) {

                    if (distance > 2f) {

                        float speed = 1000f * Gdx.graphics.getDeltaTime();

                        float vx = dx / distance;
                        float vy = dy / distance;

                        winBody.setX(winBody.getX() + vx * speed);
                        winBody.setY(winBody.getY() + vy * speed);
                    }
                }

            }
        }
    }

    public void activatePowerUp(int powerUpId){

    }
}
