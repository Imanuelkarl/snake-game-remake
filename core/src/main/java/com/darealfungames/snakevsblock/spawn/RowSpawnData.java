package com.darealfungames.snakevsblock.spawn;

import com.darealfungames.snakevsblock.entities.Block;
import com.darealfungames.snakevsblock.entities.Line;
import com.darealfungames.snakevsblock.entities.WinBody;

import java.util.ArrayList;
import java.util.List;

public final class RowSpawnData {
    public final List<Block> blocks = new ArrayList<>();
    public final List<WinBody> winBodies = new ArrayList<>();
    public final List<Line> lines = new ArrayList<>();
}
