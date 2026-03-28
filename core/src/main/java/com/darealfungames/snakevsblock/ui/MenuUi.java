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
        int WORLD_WIDTH=(gameSize * Gdx.graphics.getWidth()) /Gdx.graphics.getHeight();
        FitViewport viewport = new FitViewport(WORLD_WIDTH, gameSize, camera);
        this.stage = new Stage(viewport);

        this.menuUIListener=menuUIListener;
        this.menuUiFactory=new MenuUiFactory(stage.getWidth(),stage.getHeight());
        build();
    }
    private void build(){
        menuUiFactory.build();
        stage.addActor(menuUiFactory.background);
        setupViewPager();
        menuUiFactory.bottomNavigation.addOnItemSelectedListener(menuUIListener::onNavItemSelected);
        menuUiFactory.cancelButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                menuUIListener.onCancelButtonClicked();
            }
        });

        menuUiFactory.bottomNavigation.addOnItemSelectedListener(new BottomNavigation.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                viewPager.setCurrentItem(index,true);
            }
        });
        viewPager.setOnPageChangeListener(new ViewManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                menuUiFactory.bottomNavigation.selectItem(position);
                System.out.println("Selecting");
            }

            @Override
            public void onPageChanged(int oldPosition, int newPosition) {

            }
        });

        stage.addActor(menuUiFactory.headerBackground);
        stage.addActor(menuUiFactory.cancelButton);
        stage.addActor(menuUiFactory.coinBar);
        stage.addActor(menuUiFactory.diamondBar);
        stage.addActor(menuUiFactory.healthBar);
        stage.addActor(menuUiFactory.bottomNavigation);

    }
    private void setupViewPager() {
        viewPager = new ViewManager();
        pagerAdapter = new ViewPagerAdapter();



        viewPager.setSize(stage.getWidth(), stage.getHeight()-menuUiFactory.headerBackground.getHeight()-menuUiFactory.bottomNavigation.getHeight());
        viewPager.setPosition(0,menuUiFactory.bottomNavigation.getHeight());

        // Add the 4 views
        pagerAdapter.addView(new SkinsView(viewPager));
        pagerAdapter.addView(new UpgradesView(viewPager));
        viewPager.setAdapter(pagerAdapter);
        //pagerAdapter.addView(new AchievementsView());
        //pagerAdapter.addView(new ShopView());




        // Optional: Add page change listener
        viewPager.setOnPageChangeListener(new ViewManager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                String[] titles = {"Skins", "Upgrades", "Achievements", "Shop"};
                System.out.println("Selected: " + titles[position]);
            }

            @Override
            public void onPageChanged(int oldPosition, int newPosition) {
                // Handle page change
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
