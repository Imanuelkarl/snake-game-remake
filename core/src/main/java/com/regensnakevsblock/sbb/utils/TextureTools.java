package com.regensnakevsblock.sbb.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureTools {
    public static TextureRegion resolveRegion(Texture texture, int rows, int columns, int x, int y, int pad ){
        return new TextureRegion(texture,(x*texture.getWidth()/columns )+pad/2,y*texture.getHeight()/rows +pad/2,texture.getWidth()/columns-pad,texture.getHeight()/rows-pad);
    }
    public static TextureRegion resolveRegion(TextureRegion texture, int rows, int columns, int x, int y, int pad ){
        return new TextureRegion(texture,(x*texture.getRegionWidth()/columns )+pad/2,y*texture.getRegionHeight()/rows +pad/2,texture.getRegionWidth()/columns-pad,texture.getRegionHeight()/rows-pad);
    }
    public static TextureRegion resolveRegion(Texture texture, int rows, int columns, int x, int y, int padX,int padY ){
        return new TextureRegion(texture,(x*texture.getWidth()/columns )+padX/2,y*texture.getHeight()/rows +padY/2,texture.getWidth()/columns-padX,texture.getHeight()/rows-padY);
    }
}
