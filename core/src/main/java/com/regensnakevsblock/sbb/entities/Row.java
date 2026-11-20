package com.regensnakevsblock.sbb.entities;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

public class Row {
    private final ArrayList<Block> blocks;

    private final ArrayList<Line> lines;

    private final ArrayList<WinBody> winBodies;
    private boolean finishLine;
    private float y;

    private boolean empty;
    private int id;



    private boolean cleared;

    public Row() {
        this.blocks= new ArrayList<>();
        this.lines = new ArrayList<>();
        this.winBodies = new ArrayList<>();
    }
    public Row(boolean finishLine){
        this.blocks= new ArrayList<>();
        this.lines = new ArrayList<>();
        this.winBodies = new ArrayList<>();
        this.finishLine=finishLine;
    }

    public void setId(int id) {
        this.id = id;
        for(Block block: blocks){
            block.setRowId(id);
        }
        for(WinBody winBody:winBodies){
            winBody.setRowId(id);
        }
    }

    public int getId() {
        return id;
    }

    public boolean isFinishLine() {
        return finishLine;
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
        winBody.setRowId(id);
        winBodies.add(winBody);
    }
    public void addLine(Line line){
        lines.add(line);
    }
    public void addBlock(Block block){
        block.setRowId(id);
        blocks.add(block);
    }
    public boolean isCleared() {
        return cleared;
    }

    public void setCleared(boolean cleared) {
        this.cleared = cleared;
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
