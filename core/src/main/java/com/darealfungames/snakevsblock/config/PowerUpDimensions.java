package com.darealfungames.snakevsblock.config;

import com.darealfungames.snakevsblock.enumaretors.PowerUp;

public class PowerUpDimensions {

    public static int enumToPosition(PowerUp powerUp){
        switch (powerUp){
            case HAMMER:
                return 1;
                case FREEZE:
                return 2;
            case MULTIPLIER:
                return 3;
            default:
                return 0;

        }
    }
    public static PowerUp intToEnum(int powerUp){
        switch (powerUp){
            case 1:
                return PowerUp.HAMMER;
            case 2:
                return PowerUp.FREEZE;
            case 3:
                return PowerUp.MULTIPLIER;
            default:
                return PowerUp.MAGNET;
        }
    }
}
