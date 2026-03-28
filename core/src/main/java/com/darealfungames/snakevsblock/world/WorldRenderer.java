package com.darealfungames.snakevsblock.world;

import static com.darealfungames.snakevsblock.utils.RandomEngine.getRandom;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.darealfungames.snakevsblock.assets.Assets;
import com.darealfungames.snakevsblock.config.GameInstance;
import com.darealfungames.snakevsblock.entities.Block;
import com.darealfungames.snakevsblock.entities.Line;
import com.darealfungames.snakevsblock.entities.Row;
import com.darealfungames.snakevsblock.entities.Snake;
import com.darealfungames.snakevsblock.entities.SnakeSection;
import com.darealfungames.snakevsblock.entities.WinBody;
import com.darealfungames.snakevsblock.utils.CustomBitmapFont;
import com.darealfungames.snakevsblock.utils.FontFactory;
import com.darealfungames.snakevsblock.utils.SimpleBitmapFont;

import java.util.ArrayList;
import java.util.Base64;

public class WorldRenderer {
    public OrthographicCamera camera;
    public WorldState worldState;
    public Snake snake;
    private ArrayList<Row> rows;
    private float textAnimationTime = 0f;

    private final SpriteBatch batch;

    private final Texture blockTexture;
    private final Texture lineTexture;
    private final Texture snakeTexture;
    private float radius;

    private BitmapFont scoreFont;
    private BitmapFont blockFont;
    //private CustomBitmapFont testFont;
    private BitmapFont smallFont;
    private SimpleBitmapFont font;

    private final GlyphLayout layout = new GlyphLayout();

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

        // Enable debug mode to see what's happening
        //testFont.setDebugMode(false);

        // Print all loaded glyphs to see if they're loading correctly
        //testFont.printGlyphs();

        if(GameInstance.getInstance().getSimpleBitmapFont()==null){
            font = new SimpleBitmapFont(
            Gdx.files.internal("fonts/ImageFonts/FontsHDTwo.fnt"),
            Assets.getInstance().fontTexture
            );
            GameInstance.getInstance().setSimpleBitmapFont(font);
        }else{
            font=GameInstance.getInstance().getSimpleBitmapFont();
        }

        // Optional: Enable debug to see character boxes
        font.setDebugMode(false);

        // Print all loaded glyphs to verify
        font.printGlyphs();


        // Check for missing characters
        String testText = "Hello World! 123 €₦¥";
        String missing = font.findMissingCharacters(testText);
        if (!missing.isEmpty()) {
            Gdx.app.log("Font", "Missing characters: " + missing);
        }
    }

    public void render() {
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        renderRowItems(batch);
        renderSnake();
        batch.end();
    }


    public void resize(int width, int height) {

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

                        float textY = row.getY() + font.getHeight(value);
                        //+ (worldState.getBlockDimension() + layout.height) / 2f;

                        //blockFont.draw(batch, layout, textX, textY);
                        //font.setFontSize();

                        font.draw(batch, value, textX, textY - worldState.getBlockDimension() * 0.55f);
                        //testFont.draw(batch, String.valueOf(block.getValue()), textX, textY);
                    }
                    //System.out.println("Drawing block at position: " + ((camera.viewportWidth/5)*block.getPosition()) + ", " + row.getY());
                }
                for (WinBody winBody : row.getWinBodies()) {
                    float winBodyX = (winBody.getPositionIndex() + 1) * camera.viewportWidth / 10 + winBody.getPositionIndex() * camera.viewportWidth / 10 - radius;
                    Rectangle bounds = new Rectangle(winBodyX, row.getY() + radius * 2, radius * 2, radius * 2);
                    winBody.setBounds(bounds);
                    if (winBody.isActive()) {
                        batch.draw(snakeTexture, winBodyX, row.getY() + radius * 2, radius * 2, radius * 2);
                        String value = String.valueOf(winBody.getValue());

                        layout.setText(smallFont, value);

                        float textX = winBodyX + radius - layout.width / 2f;
                        float textY = row.getY() + radius * 5f + layout.height / 2f;

                        smallFont.draw(batch, layout, textX, textY);
                    }


                }
                for (Line line : row.getLines()) {
                    float lineX = (line.getPosition() + 1) * camera.viewportWidth / 10 + line.getPosition() * camera.viewportWidth / 10 + worldState.getBlockDimension() / 2;
                    batch.draw(lineTexture, lineX, row.getY(), line.getWidth(), worldState.getBlockDimension());
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
    public void dispose() {
        batch.dispose();
        snakeTexture.dispose();
        lineTexture.dispose();
        blockTexture.dispose();
    }
}
