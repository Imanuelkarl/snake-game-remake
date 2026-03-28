package com.darealfungames.snakevsblock.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Row {
    private final ArrayList<Block> blocks;

    private final ArrayList<Line> lines;

    private final ArrayList<WinBody> winBodies;
    private float y;

    private boolean empty;

    public Row() {
        this.blocks= new ArrayList<>();
        this.lines = new ArrayList<>();
        this.winBodies = new ArrayList<>();
    }


    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public ArrayList<Block> getBlocks() {
        return blocks;
    }
    public ArrayList<Line> getLines() {
        return lines;
    }

    public ArrayList<WinBody> getWinBodies() {
        return winBodies;
    }
    public void addWinBody(WinBody winBody){
        winBodies.add(winBody);
    }
    public void addLine(Line line){
        lines.add(line);
    }
    public void addBlock(Block block){
        blocks.add(block);
    }

    public void dispose(){
        blocks.clear();
        lines.clear();
        winBodies.clear();
    }

    public void setEmpty(boolean empty) {

        this.empty=empty;
    }

    public boolean isEmpty() {
        return empty;
    }
}
