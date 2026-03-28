package com.darealfungames.snakevsblock.systems;


import com.badlogic.gdx.math.Rectangle;
import com.darealfungames.snakevsblock.entities.Block;
import com.darealfungames.snakevsblock.entities.Line;
import com.darealfungames.snakevsblock.entities.Pickup;
import com.darealfungames.snakevsblock.entities.Row;
import com.darealfungames.snakevsblock.entities.WinBody;
import com.darealfungames.snakevsblock.world.WorldState;

public class MovementSystem extends GlobalSystems {


    public MovementSystem(WorldState worldState) {
        super(worldState);
    }

    public void update(float deltaTime) {
        if (worldState.isPaused() || worldState.isGameOver()) {
            return;
        }

        // Move all rows down
        for (Row row : worldState.getRows()) {


            row.setY(row.getY() - worldState.getGameSpeed()*worldState.getMoveFactor() * deltaTime * 100);


            for (Line line : row.getLines()) {
                float x=(line.getPosition()+1)*worldState.getScreenWidth()/10+line.getPosition()*worldState.getScreenWidth()/10+worldState.getBlockDimension()/2;
                float y= row.getY();
                float width= worldState.getBlockDimension();
                float height= worldState.getBlockDimension();
                line.setWidth(width/16);
                Rectangle left = new Rectangle(x-width/8, y+height/16,width/8,height-height/16);
                Rectangle right = new Rectangle(x+ line.getWidth(),y+height/16,width/8, height-height/16);
                line.setLeftCollision(left);
                line.setRightCollision(right);
                Rectangle bounds = new Rectangle(x, y,width,height/8);
                line.setBounds(bounds);
            }
            for (WinBody winBody : row.getWinBodies()) {
                // winBody.getBounds().y = row.getY();
            }
            for (Block block : row.getBlocks()) {
                float x=block.getPosition()*worldState.getBlockDimension();
                float y= row.getY()-worldState.getBlockDimension()*0.1f;
                float width= worldState.getBlockDimension();
                float height= worldState.getBlockDimension();
                Rectangle bounds = new Rectangle(x, y,width, height/8);
                block.setBounds(bounds);
                Rectangle left = new Rectangle(x-width/8, y+height/8,width/8,height-height/16);
                Rectangle right = new Rectangle(x+width,y+height/8,width/8, height-height/16);
                block.setLeftCollision(left);
                block.setRightCollision(right);
            }

        }
//Help me auto complete a code to remove rows that are off the screen safely my current setup is crashing
        for (int i = worldState.getRows().size() - 1; i >= 0; i--) {
            if (worldState.getRows().get(i).getY() < -worldState.getBlockDimension()) {
                worldState.removeRow(i);
            }
        }

        worldState.getSnake().adjust(worldState.getGameSpeed() * deltaTime * 100);

    }
}
