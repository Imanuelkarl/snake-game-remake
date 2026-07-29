package com.regensnakevsblock.sbb.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

public class FontActor extends Widget {

    private SimpleBitmapFont font;
    private boolean drawStyle=false;
    private String text;

    private float width;
    private float height;
    private float padY;
    private float padX;
    public static int CENTER=1;
    public static int topRight=3;
    public static int bottomLeft=2;

    public FontActor(SimpleBitmapFont font, String text) {
        this.font = font;
        this.text = text;
        updateSize();
    }

    public void setText(String text) {
        this.text = text;

        updateSize();
    }
    public void setAlignment(int align){
        switch (align){
            case 1:
                padX = (this.getWidth()-font.getWidth(text))/2;
                padY =(this.getHeight()-font.getHeight(text))/2;
                break;
            case 2:
                padX=0;
                padY=0;
                break;
            case 3:
                padY =(this.getHeight()-font.getHeight(text));
                padX=(this.getWidth()-font.getWidth(text));
                break;
            default:
                padX=0;
                padY=0;

        }
    }

    private void updateSize() {
        setWidth(font.getWidth(text));
        setHeight(font.getHeight(text));
    }

    @Override
    public void setSize(float width, float height) {
        super.setSize(width, height);
        this.width=width;
        this.height=height;
        drawStyle=true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        if(drawStyle){

            font.drawWrapped(batch, text, getX()+((width-font.getWidth(text))/2), getY()+height/8 ,this.width);
        }else{
            font.draw(batch, text, getX(), getY());
        }

        batch.setColor(Color.WHITE);
    }

    @Override
    public float getWidth() {

        return drawStyle?width:font.getWidth(text);

    }

    public void setFont(SimpleBitmapFont font) {
        this.font=font;
    }
}
