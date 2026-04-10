package com.darealfungames.snakevsblock.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.darealfungames.snakevsblock.config.Constants;
import com.darealfungames.snakevsblock.config.GameInstance;
import com.darealfungames.snakevsblock.elements.BottomNavigation;
import com.darealfungames.snakevsblock.ui.core.ViewManager;
import com.darealfungames.snakevsblock.ui.core.ViewPagerAdapter;
import com.darealfungames.snakevsblock.ui.views.AchievementsView;
import com.darealfungames.snakevsblock.ui.views.ShopView;
import com.darealfungames.snakevsblock.ui.views.SkinsView;
import com.darealfungames.snakevsblock.ui.views.UpgradesView;
import com.darealfungames.snakevsblock.uiactions.MenuUIListener;
import com.darealfungames.snakevsblock.uifactory.HomeUiFactory;
import com.darealfungames.snakevsblock.uifactory.MenuUiFactory;

public class MenuUi {
    private final Stage stage;
    private final MenuUIListener menuUIListener;

    private final MenuUiFactory menuUiFactory;
    private ViewManager viewPager;
    private ViewPagerAdapter pagerAdapter;
    private OrthographicCamera camera;


    public MenuUi(MenuUIListener menuUIListener) {
        int gameSize = Constants.SCREEN_SIZE;
        camera=new OrthographicCamera();
        int WORLD_WIDTH=/*Constants.SCREEN_WIDTH;*/(gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        this.stage = new Stage(viewport);

        this.menuUIListener=menuUIListener;
        this.menuUiFactory=new MenuUiFactory(stage.getWidth(),stage.getHeight());
        build();
    }
    private void build() {
        long totalStart = System.nanoTime();

        long start;

        System.out.println("Building Menu UI...");
        start = System.nanoTime();
        menuUiFactory.build();
        logTime("menuUiFactory.build()", start);

        System.out.println("Adding background to stage...");
        start = System.nanoTime();
        stage.addActor(menuUiFactory.background);
        logTime("add background", start);

        System.out.println("Setting up ViewPager...");
        start = System.nanoTime();
        setupViewPager();
        logTime("setupViewPager()", start);

        System.out.println("Setting up listeners...");
        start = System.nanoTime();

        menuUiFactory.bottomNavigation.addOnItemSelectedListener(menuUIListener::onNavItemSelected);

        menuUiFactory.cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                menuUIListener.onCancelButtonClicked();
            }
        });

        menuUiFactory.bottomNavigation.addOnItemSelectedListener(new BottomNavigation.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                viewPager.setCurrentItem(index, true);
            }
        });
        menuUiFactory.bottomNavigation.selectItem(viewPager.getCurrentPosition());

        logTime("setup listeners", start);

        System.out.println("Adding UI elements to stage...");
        start = System.nanoTime();

        stage.addActor(menuUiFactory.headerBackground);
        stage.addActor(menuUiFactory.cancelButton);
        stage.addActor(menuUiFactory.coinBar);
        stage.addActor(menuUiFactory.diamondBar);
        stage.addActor(menuUiFactory.healthBar);
        stage.addActor(menuUiFactory.bottomNavigation);

        logTime("add UI actors", start);

        logTime("TOTAL build()", totalStart);

        System.out.println("Menu UI built successfully.");
    }
    private void logTime(String label, long startNano) {
        long durationNs = System.nanoTime() - startNano;
        double ms = durationNs / 1_000_000.0;
        System.out.println(label + " took: " + ms + " ms");
    }
    private void setupViewPager() {
        viewPager = GameInstance.getInstance().menuViewManager;
        pagerAdapter = GameInstance.getInstance().menuViewManagerAdapter;



        viewPager.setSize(stage.getWidth(), stage.getHeight()-menuUiFactory.headerBackground.getHeight()-menuUiFactory.bottomNavigation.getHeight());
        viewPager.setPosition(0,menuUiFactory.bottomNavigation.getHeight());
        //viewPager.resize(stage.getWidth(), stage.getHeight()-menuUiFactory.headerBackground.getHeight()-menuUiFactory.bottomNavigation.getHeight());

        /*// Add the 4 views
        pagerAdapter.addView(new SkinsView(viewPager));
        pagerAdapter.addView(new UpgradesView(viewPager));
        pagerAdapter.addView(new AchievementsView(viewPager));
        pagerAdapter.addView(new ShopView(viewPager));*/
        //viewPager.setAdapter(pagerAdapter);





        // Optional: Add page change listener
        viewPager.setOnPageChangeListener(new ViewManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                String[] titles = {"Skins", "Upgrades", "Achievements", "Shop"};
                System.out.println("Selected: " + titles[position]);
                menuUiFactory.bottomNavigation.selectItem(position);
            }

            @Override
            public void onPageChanged(int oldPosition, int newPosition) {
                // Handle page change
                menuUiFactory.bottomNavigation.selectItem(newPosition);
            }
        });


        stage.addActor(viewPager);
    }
    public void update(float deltaTime){

        stage.act(deltaTime);
        stage.draw();
    }
    public void resize(float width, float height){
        for (int i = 0; i < pagerAdapter.getCount(); i++) {
            pagerAdapter.getView(i).resize(width, height);
        }
    }

    public Stage getStage() {
        return stage;
    }
}
