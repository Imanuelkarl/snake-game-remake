package com.regensnakevsblock.sbb.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.regensnakevsblock.sbb.assets.Assets2;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Line;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.Snake;
import com.regensnakevsblock.sbb.entities.SnakeSection;
import com.regensnakevsblock.sbb.entities.WinBody;
import com.regensnakevsblock.sbb.levels.Level;
import com.regensnakevsblock.sbb.utils.ActorFactory;
import com.regensnakevsblock.sbb.utils.FontFactory;
import com.regensnakevsblock.sbb.utils.TextureTools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private final GlyphLayout layout = new GlyphLayout();
    private final TextureRegion magnetPowerUp;
    private final TextureRegion hammerPowerUp;
    private final TextureRegion freezePowerUp;
    private final TextureRegion multiplierPowerUp;

    public WorldRenderer(WorldState worldState) {
        this.camera= worldState.getCamera();
        this.worldState = worldState;
        this.batch = new SpriteBatch();

        this.blockTexture= Assets2.getInstance().blockTexture;
        this.lineTexture= Assets2.getInstance().lineTexture;
        radius= worldState.getBlockDimension()/7;
        this.snake=worldState.getSnake();
        snake.setRadius(radius);
        bitmapFont=Assets2.getInstance().font;
        snakeSkinId = worldState.getSnakeSkinId();
        int col = snakeSkinId % 6;
        int row = snakeSkinId / 6;

        this.snakeTexture = TextureTools.resolveRegion(Assets2.getInstance().snakeSkins,8,6,col,row,12);
        this.rows=worldState.getRows();
        /*//this.scoreFont = FontFactory.getRoboto(34, true);   // Score / High score
        //this.blockFont = FontFactory.getRoboto(60, false);   // Block numbers*/
        this.smallFont = FontFactory.getRoboto(20, false);

        TextureRegion powerUps= Assets2.getInstance().powerUpTexture;;
        this.magnetPowerUp = TextureTools.resolveRegion(powerUps,2,2,1,0,0);
        this.hammerPowerUp = TextureTools.resolveRegion(powerUps,2,2,0,0,0);
        this.freezePowerUp = TextureTools.resolveRegion(powerUps,2,2,1,1,0);
        this.multiplierPowerUp=TextureTools.resolveRegion(powerUps,2,2,0,1,0);

        this.bloodEffect = Assets2.getInstance().bloodEffect;
        ParticleEmitter emitter=bloodEffect.findEmitter(bloodEffect.getEmitters().get(0).getName());
        emitter.getTint().setColors(Constants.Snake_Texture_Colors[snakeSkinId]);;
        bloodEffect.setDuration(3000);
        worldState.startTaskIntro();
        // Check for missing characters
        worldState.setOnHitListener(this);
    }
    private float easeOutBack(float x) {
        float c1 = 1.70158f;
        float c3 = c1 + 1f;

        return 1 + c3 * (float)Math.pow(x - 1, 3)
            + c1 * (float)Math.pow(x - 1, 2);
    }

    private float lerp(float a, float b, float t){
        return a + (b - a) * t;
    }
    private void renderTaskIntro() {

        if(worldState.getGameMode() != WorldState.GameMode.LEVELS)
            return;

        float time = worldState.getTaskIntroTime();

        float centerX = worldState.getScreenWidth()/2f;
        float centerY = worldState.getScreenHeight()/2f;

        float hudX = worldState.getScreenWidth()/5f;
        float hudY = worldState.getScreenHeight()-140;

        float cardWidth =
            worldState.getScreenWidth()/1.75f;

        float cardHeight = 160;

        float x;
        float y;
        float scale;

        // Phase 1: pop in
        if(time < 1.5f){

            float t = Math.min(time / 0.5f,1f);

            scale = Interpolation.swingOut.apply(t);

            x = centerX - cardWidth/2f;
            y = centerY - cardHeight/2f;

        }
        // Phase 2: fly to HUD
        else{

            float t =
                Math.min((time - 1.5f)/1f,1f);

            t = Interpolation.exp10Out.apply(t);

            float startX =
                centerX - cardWidth/2f;

            float startY =
                centerY - cardHeight/2f;

            x = MathUtils.lerp(
                startX,
                hudX,
                t
            );

            y = MathUtils.lerp(
                startY,
                hudY,
                t
            );

            scale = MathUtils.lerp(
                1f,
                0.85f,
                t
            );
        }

        // Dark background
        batch.setColor(0,0,0,0.5f);

        batch.draw(
            Assets2.getInstance().backgroundTexture,
            0,
            0,
            worldState.getScreenWidth(),
            worldState.getScreenHeight()
        );

        batch.setColor(Color.WHITE);

        // Draw objective card
        batch.draw(
            Assets2.getInstance().levelTaskHeaderBg,
            x,
            y,
            cardWidth*scale,
            cardHeight*scale
        );

        bitmapFont.getData().setScale(0.25f * scale);

        String title = "OBJECTIVE";

        layout.setText(bitmapFont,title);

        bitmapFont.draw(
            batch,
            title,
            x + (cardWidth*scale-layout.width)/2f,
            y + cardHeight*scale - 20
        );

        renderTaskIntroContents(
            x,
            y,
            scale
        );

        bitmapFont.getData().setScale(1f);
    }
    private void renderTaskIntroContents(
        float x,
        float y,
        float scale){

        float cardWidth =
            worldState.getScreenWidth()/1.75f;

        float centerX =
            x + cardWidth*scale/2f;

        if(worldState.getLevel().getLevelType()
            == Level.LevelType.FINISH_LINE){

            bitmapFont.getData().setScale(
                0.25f*scale);

            String text =
                "GET TO THE FINISH LINE";

            layout.setText(bitmapFont,text);

            bitmapFont.draw(
                batch,
                text,
                centerX-layout.width/2f,
                y+80*scale
            );
        }

        else if(worldState.getLevel().getLevelType()
            == Level.LevelType.BLOCKS){

            float blockSize =
                worldState.getBlockDimension()
                    *0.45f
                    *scale;

            TextureRegion block =
                worldState.getLevelBlocks()
                    .values()
                    .iterator()
                    .next();

            batch.draw(
                block,
                centerX-blockSize/2f,
                y+40*scale,
                blockSize,
                blockSize
            );

            String text =
                String.valueOf(
                    worldState.getLevel()
                        .getNoOfBlocks());

            bitmapFont.getData().setScale(
                0.45f*scale);

            layout.setText(bitmapFont,text);

            bitmapFont.draw(
                batch,
                text,
                centerX-layout.width,
                y+45*scale
            );
        }

        else if(worldState.getLevel().getLevelType()
            == Level.LevelType.COLORED_BLOCKS){

            System.out.println("Animating intro for colored blocks");
            float blockSize =
                worldState.getBlockDimension()
                    *0.35f
                    *scale;

            int i = 0;

            int total =
                worldState.getLevel()
                    .getColorIntegerMap()
                    .size();

            float spacing =
                blockSize*2f;

            float startX =
                centerX -
                    ((total-1)*spacing)/2f;

            for(Map.Entry<Integer,Integer> entry :
                worldState.getLevel()
                    .getColorIntegerMap()
                    .entrySet()){

                TextureRegion block =
                    worldState.getLevelBlocks()
                        .get(entry.getKey());

                float bx =
                    startX +
                        i*spacing;

                batch.draw(
                    block,
                    bx,
                    y+40*scale,
                    blockSize,
                    blockSize
                );

                String count =
                    String.valueOf(
                        entry.getValue());

                bitmapFont.getData().setScale(
                    0.3f*scale);

                layout.setText(
                    bitmapFont,
                    count
                );

                bitmapFont.draw(
                    batch,
                    count,
                    bx-layout.width/2f,
                    y+35*scale
                );

                i++;
            }
        }
    }


    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderRowItems(batch);
        renderSnake();
        renderBloodParticles();
        renderPowerUp();
        if(worldState.isShowTaskIntro()){
            renderTaskIntro();
        }
        else{
            renderLevelTask();
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
                        TextureRegion coinRegion =TextureTools.resolveRegion(Assets2.getInstance().coinTexture,1,1,0,0,0);
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
                    batch.draw(Assets2.getInstance().finishLineTexture,0,row.getY()+worldState.getBlockDimension()*2,camera.viewportWidth,worldState.getBlockDimension());
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
    private void renderLevelTask(){
        if(worldState.getGameMode()==WorldState.GameMode.LEVELS){
            batch.draw(Assets2.getInstance().levelTaskHeaderBg,worldState.getScreenWidth()/5,worldState.getScreenHeight()-140,worldState.getScreenWidth()/1.75f,160);
            float textY = worldState.getScreenHeight() -40;
            if(worldState.getLevel().getLevelType()== Level.LevelType.BLOCKS||(worldState.getLevel().getLevelType()== Level.LevelType.COLORED_BLOCKS)){
                float blockSize = worldState.getBlockDimension()*0.35f;
                float blockWide = blockSize*worldState.getLevelBlocks().size()*1.8f;
                int i =0;

                if(worldState.getLevel().getLevelType()== Level.LevelType.COLORED_BLOCKS){
                    for (Map.Entry<Integer, Integer> entry :worldState.getLevel().getColorIntegerMap().entrySet()){
                        String finishLineText=  String.valueOf(entry.getValue()-worldState.getBlocksDestroyedForColor(entry.getKey()));//String.valueOf(worldState.getBlocksDestroyedForColor());
                        batch.draw(worldState.getLevelBlocks().get(entry.getKey()),worldState.getScreenWidth()/2-blockWide/2+blockSize*1.8f*i,worldState.getScreenHeight()-120,blockSize,blockSize);
                        //String finishLineText=  String.valueOf(entry.getValue());//String.valueOf(worldState.getBlocksDestroyedForColor());
                        bitmapFont.getData().setScale(0.35f);
                        bitmapFont.setColor(Color.WHITE);
                        layout.setText(bitmapFont,finishLineText);
                        //float textY = worldState.getScreenHeight() -110+layout.height;
                        float textX = worldState.getScreenWidth()/2-blockWide/2+blockSize*1.8f*i - layout.width ;
                        bitmapFont.draw(batch,finishLineText,textX,textY);
                        i++;
                    }
                }else{
                    for (Map.Entry<Integer, TextureRegion> entry :worldState.getLevelBlocks().entrySet()){
                        batch.draw(worldState.getLevelBlocks().get(entry.getKey()),worldState.getScreenWidth()/2-blockWide/2+blockSize*1.5f*i,worldState.getScreenHeight()-120,blockSize,blockSize);
                        String finishLineText=  String.valueOf(worldState.getLevel().getNoOfBlocks()-worldState.totalBlocksDestroyed());//String.valueOf(worldState.getBlocksDestroyedForColor());
                        bitmapFont.getData().setScale(0.35f);
                        bitmapFont.setColor(Color.WHITE);
                        layout.setText(bitmapFont,finishLineText);
                        //float textY = worldState.getScreenHeight()-120+layout.height;
                        float textX = worldState.getScreenWidth()/2-blockWide/2+blockSize*1.5f*i - layout.width ;
                        bitmapFont.draw(batch,finishLineText,textX,textY);
                        i++;
                    }
                }
            } else {
                if(worldState.getLevel().getLevelType() == Level.LevelType.FINISH_LINE){
                    String finishLineText=  "GET TO THE FINISH LINE";
                    bitmapFont.getData().setScale(0.25f);
                    bitmapFont.setColor(Color.WHITE);
                    layout.setText(bitmapFont,finishLineText);
                    //float textY = worldState.getScreenHeight() -120;
                    float textX = worldState.getScreenWidth() / 2f - layout.width / 2f;
                    bitmapFont.draw(batch,finishLineText,textX,textY);


                }

            }
            bitmapFont.setColor(Color.WHITE);


        }
    }
    public void dispose() {
        batch.dispose();
        bloodEffect.dispose();
        //font.dispose();
        //snakeTexture.dispose();
        lineTexture.dispose();
        blockTexture.dispose();
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
