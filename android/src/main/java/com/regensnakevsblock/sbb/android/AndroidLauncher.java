package com.regensnakevsblock.sbb.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.android.ads.AdsManager;
import com.regensnakevsblock.sbb.android.ads.UnityAdsManager;
import com.regensnakevsblock.sbb.android.purchase.PurchaseManager;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        MyGame myGame = new MyGame();
        PurchaseManager purchaseManager = new PurchaseManager(this);
        AdsManager adsManager = new AdsManager(this);
        UnityAdsManager unityAdsManager = new UnityAdsManager(this);
        adsManager.initialize();
        unityAdsManager.initialize();
        adsManager.loadBanner();
        adsManager.loadInterstitial();
        adsManager.loadRewarded();
        myGame.setPurchaseService(purchaseManager);
        myGame.setAdsService(adsManager);
        initialize(myGame, configuration);
    }
}
