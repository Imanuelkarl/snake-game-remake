package com.regensnakevsblock.sbb.utils;

import com.badlogic.gdx.graphics.Color;

public class ColorResolver {

    public static Color[] blockColor = {Color.CYAN,Color.YELLOW,Color.GREEN,
        Color.ORANGE,Color.RED,Color.BLUE,
        Color.MAGENTA,Color.PURPLE,Color.LIME,Color.BROWN};
    public static float[] getColor(int id){
        return new float[]{blockColor[id].r,blockColor[id].g,blockColor[id].b,blockColor[id].a};
    }
}
