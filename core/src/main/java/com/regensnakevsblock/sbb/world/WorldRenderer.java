package com.regensnakevsblock.sbb.world;

import static com.regensnakevsblock.sbb.utils.RandomEngine.getRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.regensnakevsblock.sbb.assets.Assets;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Line;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.Snake;
import com.regensnakevsblock.sbb.entities.SnakeSection;
import com.regensnakevsblock.sbb.entities.WinBody;
import com.regensnakevsblock.sbb.enumaretors.PowerUp;
import com.regensnakevsblock.sbb.utils.CustomBitmapFont;
import com.regensnakevsblock.sbb.utils.FontFactory;
import com.regensnakevsblock.sbb.utils.SimpleBitmapFont;
import com.regensnakevsblock.sbb.utils.TextureTools;

import java.util.ArrayList;
import java.util.Base64;

public class WorldRenderer implements HitListener {
    public OrthographicCamera camera;
    public WorldState worldState;
    public Snake snake;
    private ArrayList<Row> rows;
    private float textAnimationTime = 0f;
    private boolean isPlayingBloodEffect =false;

    private final SpriteBatch batch;

    private final Texture blockTexture;
    private final Texture lineTexture;
    private final Texture snakeTexture;
    private float radius;

    private BitmapFont scoreFont;
    private BitmapFont blockFont;
    //private CustomBitmapFont testFont;
    private BitmapFont smallFont;

    private ParticleEffect bloodEffect;
    private SimpleBitmapFont font;

    private final GlyphLayout layout = new GlyphLayout();
    private TextureRegion magnetPowerUp;
    private TextureRegion hammerPowerUp;
    private TextureRegion freezePowerUp;
    private TextureRegion multiplierPowerUp;

    public WorldRenderer(WorldState worldState) {
        this.camera= worldState.getCamera();
        this.worldState = worldState;
        this.batch = new SpriteBatch();
        this.snakeTexture = Assets.getInstance().snakeTexture;
        this.blockTexture=Assets.getInstance().blockTexture;
        this.lineTexture=Assets.getInstance().lineTexture;
        radius= worldState.getBlockDimension()/7;
        this.snake=worldState.getSnake();
        snake.setRadius(radius);

        this.rows=worldState.getRows();
        this.scoreFont = FontFactory.getRoboto(34, true);   // Score / High score
        this.blockFont = FontFactory.getRoboto(60, false);   // Block numbers
        this.smallFont = FontFactory.getRoboto(20, false);

        Texture powerUps= Assets.getInstance().powerUpTexture;;
        this.magnetPowerUp = TextureTools.resolveRegion(powerUps,2,2,1,0,0);
        this.hammerPowerUp = TextureTools.resolveRegion(powerUps,2,2,0,0,0);
        this.freezePowerUp = TextureTools.resolveRegion(powerUps,2,2,1,1,0);
        this.multiplierPowerUp=TextureTools.resolveRegion(powerUps,2,2,0,1,0);

        this.bloodEffect =Assets.getInstance().bloodEffect;
        ParticleEmitter emitter=bloodEffect.findEmitter(bloodEffect.getEmitters().get(0).getName());
        emitter.getTint().setColors(Constants.Snake_Texture_Colors[9]);;
        bloodEffect.setDuration(3000);
        font=GameInstance.getInstance().getSimpleBitmapFont();


        // Optional: Enable debug to see character boxes
        font.setDebugMode(false);


        // Check for missing characters
        String testText = "Hello World! 123 €₦¥";
        String missing = font.findMissingCharacters(testText);
        if (!missing.isEmpty()) {
            Gdx.app.log("Font", "Missing characters: " + missing);
        }
        worldState.setOnHitListener(this);
    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderRowItems(batch);
        renderSnake();
        renderBloodParticles();
        renderPowerUp();
        batch.end();
    }


    public void resize(int width, int height) {

    }
    private void renderPowerUp(){
        if(worldState.isPowerUpIsActive()) {
            float size = worldState.getBlockDimension() * 1.1f;
            float powerUpX=worldState.getScreenWidth()/2 - size / 2;
            float powerUpY=worldState.getScreenHeight() - (worldState.getScreenHeight() / 8);
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

                font.setScale(scaleX, scaleY);

                float textX = worldState.getScreenWidth() / 2f -
                    font.getWidth(value) / 2f;

                float textY = worldState.getScreenHeight() * 0.8f;

                font.draw(batch, value, textX, textY);
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

                        layout.setText(blockFont, value);
                        blockFont.setColor(Color.BLACK);
                        font.setScale(0.55f, 0.47f);

                        float textX = blockX
                            + (worldState.getBlockDimension() - font.getWidth(value)) / 2f;

                        float textY = row.getY() + (worldState.getScreenHeight()/Gdx.graphics.getHeight())*0.7f;
                        //+ (worldState.getBlockDimension() + layout.height) / 2f;

                        //blockFont.draw(batch, layout, textX, textY);
                        //font.setFontSize();

                        font.draw(batch, value, textX, textY );
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
                                    System.out.println("Drawing multiplier");
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
                        batch.draw(winBody.getType()== WinBody.Type.COIN?Assets.getInstance().coinTexture : snakeTexture, winBody.getX(), winBody.getY(), radius * 2, radius * 2);
                        float textX = winBody.getX() + radius - layout.width / 2f;
                        float textY = winBody.getY() + radius * 2f + layout.height *2f;
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

        layout.setText(smallFont, value);


        float textX = snake.getPosition().x + radius - layout.width / 2f;
        float textY = snake.getPosition().y + radius * 3f + layout.height / 2f;

        smallFont.draw(batch, layout, textX, textY);
        batch.draw(snakeTexture,snake.getPosition().x,snake.getPosition().y,radius*2,radius*2);
//        System.out.println("Rendering snake head at position: " + snake.getPosition().x + ", " + snake.getPosition().y + " ");
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
    public void dispose() {
        batch.dispose();
        bloodEffect.dispose();
        font.dispose();
        snakeTexture.dispose();
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
