package com.darealfungames.snakevsblock.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.regensnakevsblock.sbb.MyGame;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
        @Override
    public GwtApplicationConfiguration getConfig () {
        int baseW = 480;
        int baseH = 760;
        double aspect = (double) baseH / baseW;

        int clientW = com.google.gwt.user.client.Window.getClientWidth();
        int clientH = com.google.gwt.user.client.Window.getClientHeight();

        int width = Math.min(clientW, baseW);
        int height = (int) Math.round(width * aspect);

        if (clientH > clientW) {
            height = clientH;
            width = clientW;
        }

        GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(width, height);
        cfg.padVertical = 0;
        cfg.padHorizontal = 0;
        return cfg;
    }
        @Override
        public ApplicationListener createApplicationListener () {
            MyGame game = new MyGame();
            GoogleAdsClass adsService = new GoogleAdsClass();
            adsService.initialize();
            adsService.loadInterstitial();
            adsService.loadRewarded();
            WebPurchaseService webPurchaseService = new WebPurchaseService();
            game.setAdsService(adsService);
            game.setPurchaseService(webPurchaseService);
            return game;
        }
}
