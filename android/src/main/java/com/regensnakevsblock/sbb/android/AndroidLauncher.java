package com.regensnakevsblock.sbb.android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.view.ViewGroup;
import android.view.Gravity;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.regensnakevsblock.sbb.MyGame;
import com.regensnakevsblock.sbb.android.ads.AdsManager;
//import com.regensnakevsblock.sbb.android.ads.LevelsPlayAds;
import com.regensnakevsblock.sbb.android.ads.UnityAdsManager;
import com.regensnakevsblock.sbb.android.purchase.PurchaseManager;
import com.unity3d.ads.BannerAd;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;
import com.unity3d.services.banners.view.BannerPosition;

/** Launches the Android application. */
public class AndroidLauncher extends AndroidApplication {
    private MyGame myGame;

    private FrameLayout layout;
    View bannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        configuration.useImmersiveMode = true; // Recommended, but not required.

        // Create the game instance
        myGame = new MyGame();
        layout=new FrameLayout(this);

        // Initialize managers
        PurchaseManager purchaseManager = new PurchaseManager(this);
        AdsManager adsManager = new AdsManager(this);
        //UnityAdsManager unityAdsManager = new UnityAdsManager(this,layout);
        //LevelsPlayAds levelsPlayAds = new LevelsPlayAds(this);

        //levelsPlayAds.initialize();
        adsManager.initialize();
        //unityAdsManager.initialize();

        Log.d("Unity Ads", "Starting ads service");

        myGame.setPurchaseService(purchaseManager);
        myGame.setAdsService(adsManager);

        // Set the content view
        initialize(myGame, configuration);
    }
    public void ToggleBannerAd () {
        // If no banner exists, show one; otherwise remove the existing one:
        if (bannerView == null) {
            // Optionally specify the banner’s anchor position:
            UnityBanners.setBannerPosition (BannerPosition.BOTTOM_CENTER);
            // Request ad content for your Placement, and load the banner:
            UnityBanners.loadBanner (this, "banner");
        } else {
            UnityBanners.destroy ();
        }
    }

    // Implement the banner listener interface methods:

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

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
