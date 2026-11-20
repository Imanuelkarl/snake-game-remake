package com.regensnakevsblock.sbb.systems;

import static com.regensnakevsblock.sbb.spawn.SpawnRules.MAX_BLOCKS;
import static com.regensnakevsblock.sbb.utils.RandomEngine.getRandom;

import com.regensnakevsblock.sbb.config.GameInstance;
import com.regensnakevsblock.sbb.entities.Block;
import com.regensnakevsblock.sbb.entities.Row;
import com.regensnakevsblock.sbb.entities.Snake;
import com.regensnakevsblock.sbb.entities.WinBody;
import com.regensnakevsblock.sbb.enumaretors.PowerUp;
import com.regensnakevsblock.sbb.spawn.EntityFactory;
import com.regensnakevsblock.sbb.world.WorldState;

public class TutorialSystem extends GlobalSystems {
    private EntityFactory entityFactory;
    private TutorialData data;
    private final int delay =100;
    private boolean increase = false;

    private final String[] tutorialInstructions ={
        "Swipe Left to avoid blocks",
        "Swipe Right to avoid Blocks",
        "Swipe into win item to collect length",
        "Move to the center to Destroy small blocks",
        "Win PowerUp",
        "Great Job"
    };
    private final String[] successMessage = {
        "Great Job",
        "You are a natural!!",
        "Nice moves",
        "You're Killing it",
        "Almost there"
    };

    public TutorialSystem(WorldState worldState) {
        super(worldState);
        this.entityFactory = new EntityFactory();
        data=worldState.getTutorialData();

    }

    @Override
    public void update(float deltaTime) {
        if (worldState.isPaused() || worldState.isGameOver()|| worldState.isGameWon()) return;

        if(worldState.getGameMode()!= WorldState.GameMode.TUTORIAL) return;

        if(worldState.getRows().isEmpty()){
            tutorialControl();

        }
        increaseTutorial();
        if(data.currentStage==data.maxClearedStage){
            worldState.getRows().add(generateRandomBlocks());
            worldState.setGameMode(WorldState.GameMode.ENDLESS);
            worldState.getSnake().addSegments(5);
            GameInstance.getSaveService().saveTutorialIsPlayed(true);
        }
        worldState.setTutorialData(data);

    }

    public Row generateLargeLeftBlocks(){
        Row row = new Row();
        row.setY(worldState.getNextRowY()+delay);
        for (int i = MAX_BLOCKS-1; i >0; i--) {
            Block block = entityFactory.createBlock(i, 20,1,0);
            //data.blocks.add(block);
            row.addBlock(block);
        }
        return row;
    }
    public Row generateLargeRightBlocks(){
        Row row = new Row();
        row.setY(worldState.getNextRowY()+delay);
        for (int i = 0; i < MAX_BLOCKS-1; i++) {
            Block block = entityFactory.createBlock(i, 20,1,0);
            //data.blocks.add(block);
            row.addBlock(block);
        }
        return row;

    }
    public Row generateCenterWinBodies(){
        Row row = new Row();
        row.setY(worldState.getNextRowY()+delay);
        for (int i =0;i < MAX_BLOCKS; i++) {
            WinBody winBody = entityFactory.createWinBody(i,worldState);
            //data.blocks.add(block);
            row.addWinBody(winBody);

        }
        return row;

    }
    public Row generateFullRowBlocks(){
        Row row = new Row();
        row.setY(worldState.getNextRowY()+delay);
        for (int i = 0; i < MAX_BLOCKS; i++) {
            Block block = entityFactory.createBlock(i, 20);
            if(i==2){
                block.setValue(5);
            }
            row.addBlock(block);
            //data.blocks.add(block);
        }
        return row;

    }
    public Row generatePowerUpBlock(){
        Row row = new Row();
        row.setY(worldState.getNextRowY()+delay);
        for (int i = 0; i < MAX_BLOCKS; i++) {
            Block block = entityFactory.createBlock(i, 2,3,1);
            if(i==2){
                block.setValue(5);
            }
            row.addBlock(block);
            //data.blocks.add(block);
        }
        return row;
    }
    private void tutorialControl(){

        switch (data.currentStage){
            case 1:
                worldState.addRows(generateLargeRightBlocks());
                increase=true;
                break;
            case 2:
                worldState.addRows(generateCenterWinBodies());
                increase=true;
                break;
            case 3:
                worldState.addRows(generateFullRowBlocks());
                increase=true;
                break;
            case 4:
                worldState.addRows(generatePowerUpBlock());
                increase=true;
            case 0:
                worldState.addRows(generateLargeLeftBlocks());
                increase=true;
                break;
            default:
                break;

        }
    }
    public void increaseTutorial(){
        Snake snake = worldState.getSnake();
        for (Row row : worldState.getRows()){
            if(data.currentStage!=2&&data.currentStage<5){
                if(snake.getPosition().y-worldState.getBlockDimension()>row.getY()&&increase){
                    data.currentStage++;
                    data.stageTitle=tutorialInstructions[data.currentStage];
                    increase=false;
                }
            }
            if(data.currentStage==2){
                for(WinBody winBody: row.getWinBodies()){
                    if(snake.getBounds().overlaps(winBody.getBounds())){
                        data.currentStage++;
                        data.stageTitle=tutorialInstructions[data.currentStage];
                        increase=false;
                    }
                }
            }
        }
    }
    public Row generateRandomBlocks(){
        Row row = new Row();
        row.setY(worldState.getNextRowY());
        for (int i = 0; i < MAX_BLOCKS; i++) {
            Block block = entityFactory.createBlock(i, getRandom(1, 2));
            row.addBlock(block);
        }
        return row;
    }
}
