package com.regensnakevsblock.sbb.android;

import android.os.Bundle;
import android.util.Log;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.android.ads.AdsManager;
import com.regensnakevsblock.sbb.android.ads.UnityAdsManager;
import com.regensnakevsblock.sbb.android.purchase.PurchaseManager;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    private MyGame myGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.
        myGame = new MyGame();
        PurchaseManager purchaseManager = new PurchaseManager(this);
        AdsManager adsManager = new AdsManager(this);
        UnityAdsManager unityAdsManager = new UnityAdsManager(this);
        adsManager.initialize();
        unityAdsManager.initialize();
        //adsManager.loadBanner();
        //adsManager.loadInterstitial();
        //adsManager.loadRewarded();
        Log.d("Unity Ads"," Staring ads service");
        unityAdsManager.loadInterstitial();
        unityAdsManager.loadRewarded();
        myGame.setPurchaseService(purchaseManager);
        myGame.setAdsService(unityAdsManager);
        initialize(myGame, configuration);
    }

    @Override
    public void onBackPressed() {
        myGame.backPressed();

    }

    @Override
    protected void onPause() {
        myGame.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        myGame.resume();
        super.onResume();
    }
}
