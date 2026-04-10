package com.regensnakevsblock.sbb.systems;

import com.badlogic.gdx.math.Rectangle;
import com.regensnakevsblock.sbb.entities.*;
import com.regensnakevsblock.sbb.world.WorldState;

public class CollisionSystem extends GlobalSystems {

    private Block lastCollidedBlock;
    private Line lastLeftBlocker;
    private Line lastRightBlocker;

    // Fixed timestep for consistent block breaking
    private static final float BLOCK_BREAK_INTERVAL = 0.125f; // 8 times per second
    private float blockBreakTimer = 0;

    // Collision resolution constants
    private static final float POSITION_CORRECTION_FACTOR = 0.001f;

    public CollisionSystem(WorldState worldState) {
        super(worldState);
    }

    @Override
    public void update(float deltaTime) {
        if (worldState.isPaused() || worldState.isGameOver()) return;

        Snake snake = worldState.getSnake();

        // Store previous position for collision resolution
        Rectangle previousBounds = new Rectangle(snake.getBounds());

        // Update movement first (handled by other systems)

        // ───────────────────────────────────────────────
        //  A. Continuous Collision Detection (CCD) for fast movement
        // ───────────────────────────────────────────────
        boolean hasCollision = handleContinuousCollision(snake, previousBounds);

        // ───────────────────────────────────────────────
        //  B. Block & Line side collisions with proper resolution
        // ───────────────────────────────────────────────
        boolean leftBlocked = false;
        boolean rightBlocked = false;

        for (Row row : worldState.getRows()) {
            // Lines (permanent walls)
            for (Line line : row.getLines()) {
                // Check left wall collision
                if (checkAndResolveWallCollision(snake, line.getLeft(), true)) {
                    leftBlocked = true;
                    lastLeftBlocker = line;
                }
                // Check right wall collision
                if (checkAndResolveWallCollision(snake, line.getRight(), false)) {
                    rightBlocked = true;
                    lastRightBlocker = line;
                }
            }

            // Blocks – side collisions with position correction
            for (Block block : row.getBlocks()) {
                if (!block.isActive()) continue;

                // Check left side collision
                if (checkAndResolveBlockSideCollision(snake, block.getLeft(), true)) {
                    leftBlocked = true;
                }
                // Check right side collision
                if (checkAndResolveBlockSideCollision(snake, block.getRight(), false)) {
                    rightBlocked = true;
                }
            }
        }

        // Apply blocking flags after position correction
        snake.stopLeft(leftBlocked);
        snake.stopRight(rightBlocked);

        // ───────────────────────────────────────────────
        //  C. Main body collision with blocks (with proper timing)
        // ───────────────────────────────────────────────
        Block currentCollision = null;

        for (Row row : worldState.getRows()) {
            for (Block block : row.getBlocks()) {
                if (!block.isActive()) continue;

                if (snake.getBounds().overlaps(block.getBounds())) {
                    // Stop rows while any collision exists
                    if (!snake.isBreaking()) {
                        worldState.setMoving(false);
                        snake.setBreaking(true);
                    }

                    // Update timer for this block (frame-independent)
                    blockBreakTimer += deltaTime;
                    if(snake.getLength()!=0) {
                        while (blockBreakTimer >= BLOCK_BREAK_INTERVAL) {

                            block.reduceValue();
                            snake.removeSegment();
                            worldState.setScore(worldState.getScore() + 1);
                            blockBreakTimer -= BLOCK_BREAK_INTERVAL;
                        }
                    }else{
                        worldState.setGameOver(true);
                        this.worldState.setMoving(false);
                        block.setActive(false);
                        resetCollisionState(); // 🔥 critical
                        return;
                    }


                    // If block destroyed
                    if (block.getValue() <= 0) {

                        if(block.hasPowerUp()){
                            worldState.setPowerUp(block.getPowerUp());
                            worldState.setPowerUpIsActive(true);
                        }
                        block.setActive(false);

                    }
                }
            }
        }


// If no collisions this frame
        boolean anyBlockOverlapping = worldState.getRows().stream()
            .flatMap(row -> row.getBlocks().stream())
            .anyMatch(block -> block.isActive() && snake.getBounds().overlaps(block.getBounds()));

        if (!anyBlockOverlapping) {
            // Resume movement
            worldState.setMoving(true);
            snake.setBreaking(false);
            blockBreakTimer = 0;
        }

        // ───────────────────────────────────────────────
        //  D. WinBody collection
        // ───────────────────────────────────────────────
        for (Row row : worldState.getRows()) {
            for (WinBody winBody : row.getWinBodies()) {
                if (!winBody.isActive()) continue;

                if (snake.getBounds().overlaps(winBody.getBounds())) {
                    winBody.setActive(false);
                    snake.addSegments(winBody.getValue());
                }
            }
        }

        // ───────────────────────────────────────────────
        //  E. Clear collision flags when no longer overlapping
        // ───────────────────────────────────────────────
        if (lastCollidedBlock != null && currentCollision == null) {
            if (!snake.getBounds().overlaps(lastCollidedBlock.getBounds())) {
                worldState.setMoving(true);
                snake.setBreaking(false);
                worldState.setBreakingBlock(null);
                lastCollidedBlock = null;
                blockBreakTimer = 0; // Reset timer when leaving block
            }
        }

        lastCollidedBlock = currentCollision;
    }
    public void resetCollisionState() {
        blockBreakTimer = 0f;

        lastCollidedBlock = null;
        lastLeftBlocker = null;
        lastRightBlocker = null;

        Snake snake = worldState.getSnake();
        if (snake != null) {
            snake.setBreaking(false);
            snake.stopLeft(false);
            snake.stopRight(false);
        }
        worldState.setBreakingBlock(null);
    }

    /**
     * Continuous Collision Detection for fast-moving rows
     */
    private boolean handleContinuousCollision(Snake snake, Rectangle previousBounds) {
        Rectangle currentBounds = snake.getBounds();
        boolean hasCollision = false;

        // Check if the snake moved significantly since last frame
        float deltaY = currentBounds.y - previousBounds.y;
        if (Math.abs(deltaY) > currentBounds.height) {
            // Perform sweep test between previous and current position
            for (Row row : worldState.getRows()) {
                for (Block block : row.getBlocks()) {
                    if (!block.isActive()) continue;

                    if (sweepTest(previousBounds, currentBounds, block.getBounds())) {
                        // Collision detected during movement
                        hasCollision = true;

                        // Resolve by moving snake to collision point
                        resolveSweepCollision(snake, previousBounds, currentBounds, block.getBounds());
                        break;
                    }
                }
            }
        }

        return hasCollision;
    }

    /**
     * Sweep test between two rectangles
     */
    private boolean sweepTest(Rectangle from, Rectangle to, Rectangle target) {
        // Simple AABB sweep test - can be optimized with SAT
        Rectangle expandedTarget = new Rectangle(
            target.x - from.width,
            target.y - from.height,
            target.width + from.width * 2,
            target.height + from.height * 2
        );

        return expandedTarget.contains(to.x + to.width / 2, to.y + to.height / 2);
    }

    /**
     * Resolve sweep collision by positioning snake at collision point
     */
    private void resolveSweepCollision(Snake snake, Rectangle from, Rectangle to, Rectangle target) {
        if (to.y + to.height > target.y && from.y + from.height <= target.y) {
            // Collision from above
            snake.setY(target.y - snake.getHeight());
        } else if (to.y < target.y + target.height && from.y >= target.y + target.height) {
            // Collision from below
            snake.setY(target.y + target.height);
        }
    }

    /**
     * Check and resolve wall collision with position correction
     */
    private boolean checkAndResolveWallCollision(Snake snake, Rectangle wall, boolean isLeft) {
        Rectangle snakeBounds = snake.getBounds();

        if (snakeBounds.overlaps(wall)) {
            // Calculate penetration depth
            float overlap = isLeft ?
                (snakeBounds.x + snakeBounds.width) - wall.x :
                wall.x + wall.width - snakeBounds.x;

            if (overlap > 0) {
                // Correct position to prevent sticking
                if (isLeft) {
                    snake.shiftX(wall.x+wall.width - snakeBounds.width);
                } else {
                    snake.shiftX(wall.x );
                }
                return true;
            }
        }

        return false;
    }

    /**
     * Check and resolve block side collision
     */
    private boolean checkAndResolveBlockSideCollision(Snake snake, Rectangle blockSide, boolean isLeft) {
        Rectangle snakeBounds = snake.getBounds();

        if (snakeBounds.overlaps(blockSide)) {
            // Check if it's a valid side collision (not top/bottom)
            float verticalOverlap = Math.min(
                snakeBounds.y + snakeBounds.height - blockSide.y,
                blockSide.y + blockSide.height - snakeBounds.y
            );

            // Only resolve if it's primarily a side collision
            if (verticalOverlap > snakeBounds.height * 0.5f) {
                float overlap = isLeft ?
                    (snakeBounds.x + snakeBounds.width) - blockSide.x :
                    blockSide.x + blockSide.width - snakeBounds.x;

                if (overlap > 0) {
                    if (isLeft) {
                        snake.setX(blockSide.x - snakeBounds.width);
                    } else {
                        snake.setX(blockSide.x + blockSide.width);
                    }
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Handle block collision with consistent timing
     */
    private void handleBlockCollision(Snake snake, Block block, float deltaTime) {
        if (worldState.isMoving()) {
            // First contact with block
            worldState.setMoving(false);
            snake.setBreaking(true);
            worldState.setBreakingBlock(block);
            blockBreakTimer = 0;
        }

        if (snake.isBreaking() && worldState.getActiveBlock() == block) {
            // Update timer with deltaTime for frame-independent speed
            blockBreakTimer += deltaTime;

            // Break at consistent intervals regardless of frame rate
            while (blockBreakTimer >= BLOCK_BREAK_INTERVAL) {
                if (block.getValue() > 0) {
                    block.reduceValue();
                    snake.removeSegment();
                    worldState.setScore(worldState.getScore() + 1);

                    // Visual feedback can be added here
                    if (worldState.isSoundEnabled()) {
                        // playBreakSound();
                    }
                }

                blockBreakTimer -= BLOCK_BREAK_INTERVAL;
            }

            // Check if block is destroyed
            if (block.getValue() <= 0) {
                block.setActive(false);
                snake.setBreaking(false);
                worldState.setMoving(true);
                worldState.setBreakingBlock(null);
                blockBreakTimer = 0;
            }
        }
    }
}
