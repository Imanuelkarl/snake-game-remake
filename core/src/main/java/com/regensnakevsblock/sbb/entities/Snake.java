package com.regensnakevsblock.sbb.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;

public class Snake {
    ArrayList<SnakeSection> body;
    private Vector2 position;
    private final Array<Vector2> vectorPool = new Array<>();
    // 🔥 Add these fields
    private final Vector2 tempResult = new Vector2();
    private final Vector2 tempA = new Vector2();
    private final Vector2 tempB = new Vector2();
    private float radius;
    private int length;
    private float speed;
    private boolean isMoving;
    private Rectangle bounds;

    private boolean breaking;
    // Add these new fields
    private Array<Vector2> path = new Array<>();
    private float virtualY = 0f;
    private float spacing;

    private boolean moveLeft;

    private boolean moveRight;
    public Snake() {
        this(0, 0);
    }

    public Snake(float x, float y) {
        position = new Vector2(x, y);
        body = new ArrayList<>();
        radius = 20;
        speed = 200;
        isMoving = true;
        bounds = new Rectangle();
        spacing = radius * 2;
        // Create initial snake segments
    }

    public void addSegments(int count) {
        for (int i = 0; i < count; i++) {
            SnakeSection segment = new SnakeSection();

            float x = this.getPosition().x;
            float y = this.getPosition().y;
            if (!this.body.isEmpty()) {
                x=body.get(body.size()-1).getPosition().x;
                y=body.get(body.size()-1).getPosition().y-getHeight();
            }
            segment.setPosition(x, y);
            segment.setRadius(radius);
            body.add(segment);
            length++;
        }
    }
    public float getHeight(){
        return this.radius*2;
    }

    public void removeSegment() {
        if (!body.isEmpty()) {
            body.remove(0);
            length--;
        }
    }
    // Getters
    public ArrayList<SnakeSection> getBody() {
        return body;
    }

    public Vector2 getPosition() {
        return position;
    }

    public float getRadius() {
        return radius;
    }

    public int getLength() {
        return length;
    }

    public Rectangle getBounds() {
        bounds = new Rectangle(this.getPosition().x,this.getPosition().y,radius*2,radius*2);
        return bounds;
    }


    // Setters
    public void setPosition(float x, float y) {
        position.set(x, y);

    }
    public void setX(float x) {
        if(moveLeft||moveRight){
            if(moveLeft){
                if(x>getX()){

                }
                else{
                    position.x = x;
                }
            }
            if(moveRight){
                if(x<getX()){

                }
                else{

                    position.x = x;
                }
            }
        }
        else {
            position.x = x;
        }

    }
    public float getX(){
        return position.x;
    }

    public void setRadius(float radius) {
        this.radius = radius;
        this.spacing = radius * 2;
        for (SnakeSection segment : body) {
            segment.setRadius(radius);
        }
    }

    public float getWidth() {
        return radius * 2;
    }


    public void setBreaking(boolean breaking) {
        this.breaking = breaking;
    }

    public void adjust(float distance) {
        float desiredSpacing = spacing;

        // Advance virtual Y
        if (!breaking) {
            virtualY += distance;
        }

        Vector2 headPos = position;

        // 🔥 Reuse object instead of creating new
        Vector2 node;
        if (vectorPool.size > 0) {
            node = vectorPool.pop();
            node.set(headPos.x, virtualY);
        } else {
            node = new Vector2(headPos.x, virtualY);
        }
        path.insert(0, node);
        // (We’ll optimize this further below if needed)

        float baseY = headPos.y;

        for (int i = 0; i < body.size(); i++) {

            float targetDist = i * desiredSpacing;

            Vector2 virtualPos = getPositionAlongPath(targetDist);

            float virtualDY = virtualY - virtualPos.y;
            float screenY = baseY - virtualDY;

            body.get(i).getPosition().set(virtualPos.x, screenY);
        }

        // 🔥 Trim path (slightly faster access)
        if (!body.isEmpty()) {
            float maxNeeded = body.size() * spacing * 1.5f;

            while (path.size > 1 &&
                (virtualY - path.peek().y) > maxNeeded) {
                path.pop(); // faster than removeIndex(size-1)
            }
        }
    }

    private Vector2 getPositionAlongPath(float targetDist) {

        Vector2 first = path.get(0);
        tempResult.set(first); // reuse instead of new

        float accumulatedDist = 0f;

        for (int j = 0; j < path.size - 1; j++) {
            Vector2 a = path.get(j);
            Vector2 b = path.get(j + 1);

            float dx = b.x - a.x;
            float dy = b.y - a.y;

            float segLen = (float)Math.sqrt(dx * dx + dy * dy);

            if (accumulatedDist + segLen >= targetDist) {
                float t = (targetDist - accumulatedDist) / segLen;

                tempResult.set(
                    a.x + dx * t,
                    a.y + dy * t
                );
                return tempResult;
            }

            accumulatedDist += segLen;
        }

        // fallback (reuse instead of new)
        return tempResult.set(path.peek());
    }

    public void stopLeft(boolean leftBlocked) {
        this.moveLeft=leftBlocked;
    }

    public void stopRight(boolean rightBlocked) {
        this.moveRight=rightBlocked;
    }

    public boolean isBreaking() {
        return breaking;
    }

    public void setY(float v) {
        position.y=v;
    }

    public void shiftX(float v) {
        float distance =position.x-v;
        position.x=v;
        for(SnakeSection section: body){
            section.getPosition().x+=distance;
        }
    }
}
