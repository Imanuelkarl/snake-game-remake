package com.regensnakevsblock.sbb.config;

import com.regensnakevsblock.sbb.enumaretors.PowerUp;

public class PowerUpDimensions {

    public static int enumToPosition(PowerUp powerUp){
        int value =-1;
        switch (powerUp){
            case MAGNET:
                value=0;
                break;
            case HAMMER:
                value= 1;
                break;
            case MULTIPLIER:
                value= 2;
                break;
            case FREEZE:
                value= 3;
                break;
            default:
                 break;

        }
        return value;
    }
    public static PowerUp intToEnum(int powerUp){
        PowerUp powerUp1 = null;
        switch (powerUp){
            case 0:
                powerUp1= PowerUp.MAGNET;
                break;
            case 1:
                powerUp1= PowerUp.HAMMER;
                break;
            case 2:
                powerUp1= PowerUp.MULTIPLIER;
                break;
            case 3:
                powerUp1= PowerUp.FREEZE;
                break;
            default:
                break;
        }
        return powerUp1;
    }
}
