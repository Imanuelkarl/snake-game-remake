package com.regensnakevsblock.sbb.spawn;

import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Line;
import com.regensnakevsblock.sbb.entities.WinBody;

import java.util.ArrayList;
import java.util.List;

public final class RowSpawnData {
    public final List<Block> blocks = new ArrayList<>();
    public final List<WinBody> winBodies = new ArrayList<>();
    public final List<Line> lines = new ArrayList<>();
}
