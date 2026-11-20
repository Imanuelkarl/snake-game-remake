package com.regensnakevsblock.sbb.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Line;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.Snake;
import com.regensnakevsblock.sbb.entities.SnakeSection;
import com.regensnakevsblock.sbb.entities.WinBody;
import com.regensnakevsblock.sbb.levels.Level;
import com.regensnakevsblock.sbb.ui.data.Ach;
import com.regensnakevsblock.sbb.ui.data.AchievementList;
import com.regensnakevsblock.sbb.utils.ColorResolver;
import com.regensnakevsblock.sbb.utils.FontFactory;
import com.regensnakevsblock.sbb.utils.TextureTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

public class WorldRenderer implements HitListener {
    public OrthographicCamera camera;
    public WorldState worldState;
    public Snake snake;
    private final ArrayList<Row> rows;
    private float textAnimationTime = 0f;
    private final SpriteBatch batch;

    private final Texture blockTexture;
    private final Texture lineTexture;
    private final TextureRegion snakeTexture;
    private final BitmapFont bitmapFont;
    private float radius;
    private int snakeSkinId;
    private BitmapFont smallFont;

    private final ParticleEffect bloodEffect;
    private final ParticleEffect blockDestroy;
    private final ParticleEmitter blockEmitter;

    private final GlyphLayout layout = new GlyphLayout();
    private final TextureRegion magnetPowerUp;
    private final TextureRegion hammerPowerUp;
    private final TextureRegion freezePowerUp;
    private final TextureRegion multiplierPowerUp;

    // --- Shared "collected" flight animation: an icon flies from where it was
    // picked up to its spot on the HUD counter, shrinking as it travels.
    // Used for the objective-block collect effect below, and the intro
    // objective card reuses the same easedLerp() tween for its fly-to-HUD move.
    private static final float COLLECT_FLY_DURATION = 0.5f;
    private final ArrayList<FlyingIcon> flyingCollectIcons = new ArrayList<>();
    ShapeRenderer renderer = new ShapeRenderer();
    BitmapFont achievementFont;
    Texture whiteTex;

    private static final class FlyingIcon {
        final TextureRegion region;
        final float startX, startY, startSize;
        final float endX, endY, endSize;
        float elapsed = 0f;

        FlyingIcon(TextureRegion region, float startX, float startY, float startSize,
                   float endX, float endY, float endSize) {
            this.region = region;
            this.startX = startX; this.startY = startY; this.startSize = startSize;
            this.endX = endX; this.endY = endY; this.endSize = endSize;
        }
    }

    // Last known "remaining" count per color key / for the total-blocks case,
    // used only to detect a decrease and spawn a flying icon when it happens.
    private final Map<Integer, Integer> previousColorRemaining = new HashMap<>();
    private int previousBlocksRemaining = -1;
    private boolean blocksDecreased = false;
    // --- Victory animation ---
    private float victoryAnimationTime = 0f;
    private boolean victoryAnimationStarted = false;

    private ParticleEffect victoryConfettiLeft;
    private ParticleEffect victoryConfettiRight;

    private static final float VICTORY_POP_DURATION = 0.65f;
    private static final float VICTORY_TEXT_Y = 0.12f;

    private Ach currentAchievement;
    private float achievementDisplayTime = 0f;
    private static final float ACH_SLIDE_DURATION = 0.3f;
    private static final float ACH_HOLD_DURATION = 2.5f;

    public WorldRenderer(WorldState worldState) {
        this.camera= worldState.getCamera();
        this.worldState = worldState;
        this.batch = new SpriteBatch();
        this.blockTexture= Assets.getInstance().blockTexture;
        this.lineTexture= Assets.getInstance().lineTexture;
        radius= worldState.getBlockDimension()/7;
        this.snake=worldState.getSnake();
        snake.setRadius(radius);
        bitmapFont= Assets.getInstance().font;
        snakeSkinId = worldState.getSnakeSkinId();
        int col = snakeSkinId % 6;
        int row = snakeSkinId / 6;

        this.snakeTexture = TextureTools.resolveRegion(Assets.getInstance().snakeSkins,8,6,col,row,12);
        this.rows=worldState.getRows();
        this.smallFont = FontFactory.getRoboto(20, false);

        TextureRegion powerUps= Assets.getInstance().powerUpTexture;;
        this.magnetPowerUp = TextureTools.resolveRegion(powerUps,2,2,1,0,0);
        this.hammerPowerUp = TextureTools.resolveRegion(powerUps,2,2,0,0,0);
        this.freezePowerUp = TextureTools.resolveRegion(powerUps,2,2,1,1,0);
        this.multiplierPowerUp=TextureTools.resolveRegion(powerUps,2,2,0,1,0);

        this.bloodEffect = Assets.getInstance().bloodEffect;
        // Create independent copies so both cannons can animate separately.
        this.victoryConfettiLeft =
            new ParticleEffect(Assets.getInstance().confettiCanon);

        //Achievement Fonts definition
        achievementFont = FontFactory.getRoboto(23,false);

        this.victoryConfettiRight =
            new ParticleEffect(Assets.getInstance().confettiCanon);
        blockDestroy =Assets.getInstance().blockDestroy;
        blockEmitter=blockDestroy.findEmitter(blockDestroy.getEmitters().get(0).getName());
        ParticleEmitter emitter=bloodEffect.findEmitter(bloodEffect.getEmitters().get(0).getName());
        emitter.getTint().setColors(Constants.Snake_Texture_Colors[snakeSkinId]);;
        bloodEffect.setDuration(3000);
        blockDestroy.reset();
        Assets.getInstance().loadUIGroup("game");
        worldState.startTaskIntro();
        worldState.setOnHitListener(this);
        whiteTex = createWhiteTexture();
    }
    private Texture createWhiteTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0,0,0,0.75f);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    /** Eased lerp shared by the intro card's fly-to-HUD move and the collected-icon flight. */
    private float easedLerp(float a, float b, float t){
        return MathUtils.lerp(a, b, Interpolation.exp10Out.apply(t));
    }

    /** Advances and draws one flying icon; returns true once it has arrived. */
    private boolean updateAndDrawFlight(FlyingIcon anim, float dt){
        anim.elapsed += dt;
        float t = Math.min(anim.elapsed / COLLECT_FLY_DURATION, 1f);

        float x = easedLerp(anim.startX, anim.endX, t);
        float y = easedLerp(anim.startY, anim.endY, t);
        float size = easedLerp(anim.startSize, anim.endSize, t);

        batch.draw(anim.region, x - size/2f, y - size/2f, size, size);
        return t >= 1f;
    }

    private void renderObjectiveHud() {
        if(worldState.getGameMode() != WorldState.GameMode.LEVELS)
            return;

        float centerX = worldState.getScreenWidth()/2f;
        float centerY = worldState.getScreenHeight()/2f;

        float hudX = worldState.getScreenWidth()/5f;
        float hudY = worldState.getScreenHeight()-140;

        float cardWidth = worldState.getScreenWidth()/1.75f;
        float cardHeight = 160;

        boolean introActive = worldState.isShowTaskIntro();

        float x;
        float y;
        float scale;

        if(introActive){
            float time = worldState.getTaskIntroTime();

            // Phase 1: pop in
            if(time < 1.5f){
                float t = Math.min(time / 0.5f,1f);
                scale = Interpolation.swingOut.apply(t);
                x = centerX - cardWidth/2f;
                y = centerY - cardHeight/2f;
            }
            // Phase 2: fly to HUD
            else{
                float t = Math.min((time - 1.5f)/1f,1f);
                float startX = centerX - cardWidth/2f;
                float startY = centerY - cardHeight/2f;

                x = easedLerp(startX, hudX, t);
                y = easedLerp(startY, hudY, t);
                scale = easedLerp(1f, 0.85f, t);
            }

            // Dark background behind the popping-in card
            batch.setColor(0,0,0,0.5f);
            batch.draw(Assets.getInstance().backgroundTexture, 0, 0, worldState.getScreenWidth(), worldState.getScreenHeight());
            batch.setColor(Color.WHITE);
        } else {
            x = hudX;
            y = hudY;
            scale = 1f;
        }

        // Draw the card background
        batch.draw(Assets.getInstance().levelTaskHeaderBg, x, y, cardWidth*scale, cardHeight*scale);

        // Draw the objective content (merged rendering)
        renderObjectiveContent(x, y, scale, cardWidth, introActive);

        bitmapFont.getData().setScale(1f);
        bitmapFont.setColor(Color.WHITE);

    }

    private void renderObjectiveContent(float x, float y, float scale, float cardWidth, boolean isIntro) {
        float centerX = x + cardWidth * scale / 2f;
        Level.LevelType levelType = worldState.getLevel().getLevelType();

        if (levelType == Level.LevelType.FINISH_LINE) {
            // Finish line always shows the text, regardless of intro/live state
            bitmapFont.getData().setScale(0.18f * scale);
            String text = "GET TO THE FINISH LINE";
            layout.setText(bitmapFont, text);
            bitmapFont.draw(batch, text, centerX - layout.width/2f, y + 80 * scale);
        }
        else if (levelType == Level.LevelType.BLOCKS || levelType == Level.LevelType.COLORED_BLOCKS) {
            float blockSize = worldState.getBlockDimension() * (isIntro ? 0.45f : 0.35f) * scale;
            float blockY = y + 40 * scale;
            float blockWide = blockSize * worldState.getLevelBlocks().size() * 1.8f;
            int i = 0;

            if (levelType == Level.LevelType.COLORED_BLOCKS) {
                for (Map.Entry<Integer, Integer> entry : worldState.getLevel().getColorIntegerMap().entrySet()) {
                    int colorKey = entry.getKey();
                    int remaining = entry.getValue() - worldState.getBlocksDestroyedForColor(colorKey);

                    float bx = centerX - blockWide/2 + blockSize * 1.8f * i;
                    TextureRegion icon = worldState.getLevelBlocks().get(colorKey);

                    if (!isIntro) {
                        Integer prevRemaining = previousColorRemaining.put(colorKey, remaining);
                        if (prevRemaining != null && remaining < prevRemaining) {
                            spawnCollectFly(icon, bx + blockSize/2f, blockY + blockSize/2f, blockSize);
                        }
                    }

                    drawObjectiveCounter(icon, bx, blockY, blockSize, remaining, scale);
                    i++;
                }
            } else { // BLOCKS type
                int remaining = worldState.getLevel().getNoOfBlocks() - worldState.totalBlocksDestroyed();

                if (!isIntro) {
                    blocksDecreased = previousBlocksRemaining >= 0 && remaining < previousBlocksRemaining;
                    previousBlocksRemaining = remaining;
                }

                for (Map.Entry<Integer, TextureRegion> entry : worldState.getLevelBlocks().entrySet()) {
                    float bx = centerX - blockWide/2 + blockSize * 1.5f * i;
                    TextureRegion icon = entry.getValue();

                    // Only spawn one flying icon per decrease, on the first block type.
                    if (!isIntro && blocksDecreased && i == 0) {
                        spawnCollectFly(icon, bx + blockSize/2f, blockY + blockSize/2f, blockSize);
                    }

                    drawObjectiveCounter(icon, bx, blockY, blockSize, remaining, scale);
                    i++;
                }
            }
        }
    }

    private void drawObjectiveCounter(TextureRegion icon, float bx, float blockY, float blockSize, int remaining, float scale) {
        batch.draw(icon, bx, blockY, blockSize, blockSize);

        if (remaining <= 0) {
            TextureRegion clearedTex = Assets.getInstance().levelMarkedTexture;
            float markSize = blockSize * 0.65f;
            batch.draw(clearedTex, bx + blockSize/2f - markSize/2f, blockY + blockSize/2f - markSize/2f, markSize, markSize);
        } else {
            String text = String.valueOf(remaining);
            bitmapFont.getData().setScale(0.35f * scale);
            bitmapFont.setColor(Color.WHITE);
            layout.setText(bitmapFont, text);
            float textX = bx + blockSize/2f - layout.width/2f;
            float textY = blockY + blockSize/2f + layout.height/2f;
            bitmapFont.draw(batch, text, textX, textY);
        }
    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderRowItems(batch);
        renderSnake();
        renderBloodParticles();
        renderPowerUp();
        renderObjectiveHud();
        renderAchievementUnlocked();
        if(!worldState.isShowTaskIntro()){
            renderFlyingCollectIcons();
        }
        if(worldState.getGameMode()== WorldState.GameMode.TUTORIAL){
           renderTutorialInfo();
        }
        if(worldState.getGameMode()== WorldState.GameMode.LEVELS&&worldState.isGameWon()){
            renderVictoryAnimation();
        }
        batch.end();
    }

    public void resize(int width, int height) {

    }

    private void renderPowerUp(){
        if(worldState.isPowerUpActive()) {
            float size = worldState.getBlockDimension() * 1.1f;
            float powerUpX=worldState.getScreenWidth()/2 - size / 2;
            float powerUpY=worldState.getScreenHeight() - (worldState.getScreenHeight() / 8)-(worldState.getGameMode()== WorldState.GameMode.LEVELS?130:0);
            switch (worldState.getPowerUp()) {
                case MAGNET:
                    batch.draw(magnetPowerUp, powerUpX , powerUpY, size, size);
                    break;
                case HAMMER:
                    batch.draw(hammerPowerUp, powerUpX, powerUpY, size, size);
                    break;
                case MULTIPLIER:
                    batch.draw(multiplierPowerUp, powerUpX, powerUpY,size, size);
                    break;
                case FREEZE:
                    batch.draw(freezePowerUp, powerUpX, powerUpY,size, size);
                    break;
                default:
                    break;
            }
        }

    }

    private void renderRowItems(Batch batch){
        for(Row row: rows){
            if (!worldState.isAwake()) {
                String value = "TAP TO PLAY";

                // Increment time (you can adjust speed)
                textAnimationTime += Gdx.graphics.getDeltaTime();

                // Create pulsing scale using sine wave
                float scaleX = 0.55f + 0.05f * (float)Math.sin(textAnimationTime * 3f); // X scale
                float scaleY = 0.47f + 0.05f * (float)Math.sin(textAnimationTime * 3f); // Y scale
                layout.setText(bitmapFont, value);
                bitmapFont.getData().setScale(scaleX,scaleY);

                float textX = worldState.getScreenWidth() / 2f -
                    layout.width / 2f;

                float textY = worldState.getScreenHeight() * 0.8f;

                bitmapFont.draw(batch,layout,textX,textY);

            }else {
                for (Block block : row.getBlocks()) {
                    float blockX = (block.getPosition()) * camera.viewportWidth / 10 + block.getPosition() * camera.viewportWidth / 10;
                    float blockSize= worldState.getBlockDimension();
                    if (block.isActive()) {
                        batch.draw(
                            block.getRegion(),
                            blockX,
                            row.getY(),
                            (worldState.getBlockDimension()) - 10,
                            (worldState.getBlockDimension()) - 10
                        );

                        String value = String.valueOf(block.getValue());

                        //font.setScale(0.55f, 0.47f);
                        bitmapFont.getData().setScale(0.55f,0.47f);
                        layout.setText(bitmapFont, value);
                        float textX = blockX+blockSize/2 - layout.width / 2f ;

                        //float textY = row.getY() + (worldState.getScreenHeight()/Gdx.graphics.getHeight())*0.7f;
                        float textY = row.getY() + ( layout.height)+ layout.height/8 ;


                        //font.draw(batch, value, textX, textY );
                        bitmapFont.draw(batch, layout, textX, textY );
                        if(block.hasPowerUp()){
                            float powerUpSize =blockSize/2 -blockSize/16;
                            float powerUpY=row.getY()+blockSize/2+powerUpSize/8-10;
                            float powerUpX= blockX+blockSize/2 -powerUpSize/2;
                            switch (block.getPowerUp()){
                                case MAGNET:
                                    batch.draw(magnetPowerUp,powerUpX,powerUpY,powerUpSize,powerUpSize);
                                    break;
                                case HAMMER:
                                    batch.draw(hammerPowerUp,powerUpX,powerUpY,powerUpSize,powerUpSize);
                                    break;
                                case MULTIPLIER:
                                    batch.draw(multiplierPowerUp,powerUpX,powerUpY,powerUpSize,powerUpSize);
                                    break;
                                case FREEZE:
                                    batch.draw(freezePowerUp,powerUpX,powerUpY,powerUpSize,powerUpSize);
                                    break;
                                default:

                                    //batch.draw(multiplierPowerUp,powerUpX,powerUpY,powerUpSize,powerUpSize);
                                    break;
                            }
                        }
                        //testFont.draw(batch, String.valueOf(block.getValue()), textX, textY);
                    }else if(!block.isDestroyed()){
                        if (block.justDied()) {

                        }
                        blockDestroy.setPosition(blockX+blockSize/2,row.getY());
                        block.setJustDied(false);
                        blockEmitter.getTint().setColors(ColorResolver.getColor(block.getColor()));
                        blockDestroy.draw(batch,Gdx.graphics.getDeltaTime());

                        // Check if particle effect has finished
                        if (blockDestroy.isComplete()) {
                            blockDestroy.reset();  // Reset or dispose if no longer needed
                            block.setDestroyed(true);
                        }

                    }
                    //System.out.println("Drawing block at position: " + ((camera.viewportWidth/5)*block.getPosition()) + ", " + row.getY());
                }
                for (WinBody winBody : row.getWinBodies()) {

                    if (winBody.isActive()) {
                        String value = String.valueOf(winBody.getValue());
                        //smallFont.getData().setScale(0.2f);
                        smallFont.getData().setScale(0.4f);
                        layout.setText(smallFont, value);
                        if(winBody.isAdjust()){
                            Rectangle bounds = new Rectangle(winBody.getX(), winBody.getY() + radius * 2, radius * 2, radius * 2);
                            winBody.setBounds(bounds);

                        }
                        else{
                            float winBodyX = (winBody.getPositionIndex() + 1) * camera.viewportWidth / 10 + winBody.getPositionIndex() * camera.viewportWidth / 10 - radius;
                            Rectangle bounds = new Rectangle(winBodyX, row.getY() + radius * 2, radius * 2, radius * 2);
                            winBody.setBounds(bounds);
                            winBody.setPosition(winBodyX,row.getY()+radius*2);

                        }
                        TextureRegion coinRegion =TextureTools.resolveRegion(Assets.getInstance().coinTexture,1,1,0,0,0);
                        batch.draw(winBody.getType()== WinBody.Type.COIN?coinRegion : snakeTexture, winBody.getX(), winBody.getY(), radius * 2, radius * 2);
                        float textX = winBody.getX() + radius - layout.width / 2f;
                        float textY = winBody.getY() + radius * 2f + layout.height ;

                        smallFont.draw(batch, layout, textX, textY);

                    }

                }
                for (Line line : row.getLines()) {
                    float lineX = (line.getPosition() + 1) * camera.viewportWidth / 10 + line.getPosition() * camera.viewportWidth / 10 + worldState.getBlockDimension() / 2;
                    batch.draw(lineTexture, lineX, row.getY(), line.getWidth(), worldState.getBlockDimension());
                }
                if(row.isFinishLine()){
                    batch.draw(Assets.getInstance().finishLineTexture,0,row.getY()+worldState.getBlockDimension()*2,camera.viewportWidth,worldState.getBlockDimension());
                }
            }
        }
    }

    private void renderSnake(){
        for(SnakeSection snakeSection : snake.getBody()){
            batch.draw(snakeTexture,snakeSection.getPosition().x,snakeSection.getPosition().y,radius*2,radius*2);
        }
        String value = String.valueOf(snake.getLength());
        //smallFont.getData().setScale(0.2f);
        smallFont.getData().setScale(0.4f);
        layout.setText(smallFont, value);


        float textX = snake.getPosition().x + radius - layout.width / 2f;
        float textY = snake.getPosition().y + radius * 3f + layout.height / 2f;

        smallFont.draw(batch, layout, textX, textY);
        batch.draw(snakeTexture,snake.getPosition().x,snake.getPosition().y,radius*2,radius*2);
//      System.out.println("Rendering snake head at position: " + snake.getPosition().x + ", " + snake.getPosition().y + " ");
    }

    private void renderBloodParticles() {
        bloodEffect.setPosition(
            snake.getPosition().x + radius,
            snake.getPosition().y + radius
        );
        bloodEffect.draw(batch,Gdx.graphics.getDeltaTime());

        // Check if particle effect has finished
        if (bloodEffect.isComplete()) {
            bloodEffect.reset();  // Reset or dispose if no longer needed
        }
    }

    /** Spawns an icon flying from the snake to its HUD spot, shrinking as it goes. */
    private void spawnCollectFly(TextureRegion icon, float targetX, float targetY, float targetSize){
        float startSize = worldState.getBlockDimension()*0.9f;
        flyingCollectIcons.add(new FlyingIcon(
            icon,
            snake.getPosition().x + radius, snake.getPosition().y + radius, startSize,
            targetX, targetY, targetSize
        ));
    }

    private void renderFlyingCollectIcons(){
        float dt = Gdx.graphics.getDeltaTime();
        for(int idx = flyingCollectIcons.size()-1; idx>=0; idx--){
            if(updateAndDrawFlight(flyingCollectIcons.get(idx), dt)){
                flyingCollectIcons.remove(idx);
            }
        }
    }
    private void renderTutorialInfo() {
        // Increment animation time
        textAnimationTime += Gdx.graphics.getDeltaTime();

        // Get the current tutorial texture
        TextureRegion[] titles = Assets.getInstance().gameTutorialTitles;
        int index = Math.min(
            worldState.getTutorialData().currentStage,
            titles.length - 1
        );

        TextureRegion region = titles[index];

        // Pulse animation
        float scale = 0.95f + 0.05f * (float) Math.sin(textAnimationTime * 3f);

        float width = region.getRegionWidth() * scale;
        float height = region.getRegionHeight() * scale;

        // Position at the top center
        float x = (worldState.getScreenWidth() - width) * 0.5f;
        float y = worldState.getScreenHeight() * 0.8f;

        batch.draw(
            region,
            x,
            y,
            width,
            height
        );
    }
    private void renderVictoryAnimation() {

        float dt = Gdx.graphics.getDeltaTime();
        victoryAnimationTime += dt;

        float screenWidth = worldState.getScreenWidth();
        float screenHeight = worldState.getScreenHeight();

        // ---------------------------------------------------------
        // Start the animation only once
        // ---------------------------------------------------------
        if (!victoryAnimationStarted) {

            victoryAnimationStarted = true;
            victoryAnimationTime = 0f;

            // Position the two confetti cannons.
            float cannonY = screenHeight * 0.55f;

            victoryConfettiLeft.setPosition(
                screenWidth * 0.08f,
                cannonY
            );

            victoryConfettiRight.setPosition(
                screenWidth * 0.92f,
                cannonY
            );

            // Start both explosions.
            victoryConfettiLeft.start();
            victoryConfettiRight.start();
        }

        // ---------------------------------------------------------
        // LEVEL COMPLETED IMAGE
        // ---------------------------------------------------------

        TextureRegion levelCleared =
            Assets.getInstance().levelClearedMessage;

        float imageWidth = levelCleared.getRegionWidth();
        float imageHeight = levelCleared.getRegionHeight();

        // Desired final size.
        float targetWidth = screenWidth * 0.75f;
        float targetHeight =
            targetWidth * imageHeight / imageWidth;

        float centerX = screenWidth / 2f;
        float centerY = screenHeight * 0.57f;

        // Pop-in animation.
        float popProgress =
            Math.min(victoryAnimationTime / VICTORY_POP_DURATION, 1f);

        float scale;

        if (popProgress < 1f) {
            // Gives the image a little overshoot before settling.
            scale = Interpolation.swingOut.apply(popProgress);
        } else {
            // Very subtle breathing after appearing.
            float idleTime = victoryAnimationTime - VICTORY_POP_DURATION;

            scale = 1f +
                0.015f * (float) Math.sin(idleTime * 3f);
        }

        float drawWidth = targetWidth * scale;
        float drawHeight = targetHeight * scale;
        batch.draw(
            whiteTex,
            0,
            0,
            screenWidth,
            screenHeight
        );
        batch.draw(
            levelCleared,
            centerX - drawWidth / 2f,
            centerY - drawHeight / 2f,
            drawWidth,
            drawHeight*1.2f
        );

        // ---------------------------------------------------------
        // CONFETTI
        // ---------------------------------------------------------
        // Position the two confetti cannons.
        float cannonY = screenHeight * 0.55f;

        victoryConfettiLeft.setPosition(
            screenWidth * 0.08f,
            cannonY
        );

        victoryConfettiRight.setPosition(
            screenWidth * 0.92f,
            cannonY
        );

        victoryConfettiLeft.draw(batch, dt);
        victoryConfettiRight.draw(batch, dt);

        // Reset them when finished so they don't keep restarting.
        if (victoryConfettiLeft.isComplete()) {
            victoryConfettiLeft.reset();
        }

        if (victoryConfettiRight.isComplete()) {
            victoryConfettiRight.reset();
        }

        // ---------------------------------------------------------
        // TAP TO CONTINUE
        // ---------------------------------------------------------

        // Delay the instruction slightly so it doesn't appear
        // at exactly the same time as the level-completed graphic.
        if (victoryAnimationTime > 0.8f) {

            float textAnimationTime =
                victoryAnimationTime - 0.8f;

            float pulse =
                0.95f +
                    0.05f * (float) Math.sin(textAnimationTime * 4f);

            String text = "TAP TO CONTINUE";

            bitmapFont.getData().setScale(0.42f * pulse);

            bitmapFont.setColor(Color.WHITE);

            layout.setText(bitmapFont, text);

            float textX =
                screenWidth / 2f -
                    layout.width / 2f;

            float textY =
                screenHeight * VICTORY_TEXT_Y;

            bitmapFont.draw(
                batch,
                layout,
                textX,
                textY
            );

            // Restore the font scale.
            bitmapFont.getData().setScale(1f);
        }
    }
    private void renderAchievementUnlocked() {
        if (currentAchievement == null) {
            Stack<Ach> newAchievements = worldState.getNewAchievements();
            if (newAchievements.isEmpty()) return;
            currentAchievement = newAchievements.pop();
            achievementDisplayTime = 0f;
        }

        achievementDisplayTime += Gdx.graphics.getDeltaTime();

        TextureRegion achievementBack = Assets.getInstance().achievementBackground;
        float cardWidth = worldState.getScreenWidth() * 0.7f;
        float cardHeight = cardWidth * achievementBack.getRegionHeight() / achievementBack.getRegionWidth();

        float x = worldState.getScreenWidth() / 2f - cardWidth / 2f;
        float hiddenY = worldState.getScreenHeight();
        float shownY = worldState.getScreenHeight() - cardHeight - 20;

        float slideIn = ACH_SLIDE_DURATION;
        float holdEnd = slideIn + ACH_HOLD_DURATION;
        float slideOutEnd = holdEnd + ACH_SLIDE_DURATION;

        float y;
        if (achievementDisplayTime < slideIn) {
            float t = achievementDisplayTime / slideIn;
            y = MathUtils.lerp(hiddenY, shownY, Interpolation.swingOut.apply(t));
        } else if (achievementDisplayTime < holdEnd) {
            y = shownY;
        } else if (achievementDisplayTime < slideOutEnd) {
            float t = (achievementDisplayTime - holdEnd) / ACH_SLIDE_DURATION;
            y = MathUtils.lerp(shownY, hiddenY, Interpolation.fastSlow.apply(t));
        } else {
            currentAchievement.setDisplayed(true);
            worldState.getNewAchievements().remove(currentAchievement);
            currentAchievement = null;
            return;
        }

        batch.draw(achievementBack, x, y, cardWidth, cardHeight);

        achievementFont.getData().setScale(0.5f);
        achievementFont.setColor(Color.FIREBRICK);
        layout.setText(achievementFont, currentAchievement.getTitle().toUpperCase(Locale.ROOT));
        achievementFont.draw(batch, layout,
            x + cardWidth / 2f - layout.width / 2f+cardWidth*0.12f,
            y + cardHeight / 2f + layout.height / 2f-20);
        achievementFont.getData().setScale(1f);
    }

    public void dispose() {
        batch.dispose();
        whiteTex.dispose();
        bloodEffect.dispose();
        lineTexture.dispose();
        blockTexture.dispose();
        victoryConfettiLeft.dispose();
        victoryConfettiRight.dispose();
    }

    @Override
    public void onHit() {
        bloodEffect.start();
    }

    @Override
    public void onHitStopped() {
        bloodEffect.reset();
    }
}
