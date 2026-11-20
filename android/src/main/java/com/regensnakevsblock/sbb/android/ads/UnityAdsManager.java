package com.regensnakevsblock.sbb.android.ads;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.Gdx;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.ads.RewardListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.IUnityBannerListener;
import com.unity3d.services.banners.UnityBanners;
import com.unity3d.services.banners.view.BannerPosition;

public class UnityAdsManager implements AdsService {

    private final Activity activity;
    private RewardListener rewardListener;
    private View bannerView;
    // ================= CONFIG =================

    private static final boolean TEST_MODE = false;

    private static final String REWARDED_PLACEMENT = "Rewarded_Android";
    private static final String INTERSTITIAL_PLACEMENT = "Interstitial_Android";
    private static final String BANNER_PLACEMENT = "Banner_Android";

    // ================= STATE =================
    private boolean rewardedLoaded = false;
    private boolean interstitialLoaded = false;
    private boolean bannerLoaded = false;
    private boolean initialized = false;
    private ViewGroup layout;

    public UnityAdsManager(Activity activity,ViewGroup layout) {
        this.activity = activity;
        this.layout=layout;
    }

    // ================= INIT =================
    @Override
    public void initialize() {
        final IUnityBannerListener myBannerListener = new UnityBannerListener ();
        UnityBanners.setBannerListener (myBannerListener);

        UnityAds.initialize(activity, Constants.unityGameId, TEST_MODE,
            new IUnityAdsInitializationListener() {
                @Override
                public void onInitializationComplete() {
                    initialized = true;

                    // preload ads
                    loadRewarded();
                    loadInterstitial();
                    loadBanner();
                }

                @Override
                public void onInitializationFailed(UnityAds.UnityAdsInitializationError error, String message) {
                    initialized = false;
                }
            });
    }

    // ================= BANNER =================
    @Override
    public void loadBanner() {
        // Unity banner API is separate (optional)
        UnityBanners.loadBanner(activity,BANNER_PLACEMENT);
        UnityBanners.setBannerListener(new UnityBannerListener());

    }

    @Override
    public boolean isBannerLoaded() {
        return bannerLoaded;
    }

    @Override
    public void showBanner() {
        // Implement Unity BannerView if needed

    }

    @Override
    public void hideBanner() {
        // Implement hide if using banners
    }

    // ================= INTERSTITIAL =================
    @Override
    public void loadInterstitial() {
        if (!initialized) return;
        Log.d("Unity Ads","Ads are loading now...");
        UnityAds.load(INTERSTITIAL_PLACEMENT, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                Log.d("Unity Ads","Ads is Loaded!!");
                interstitialLoaded = true;
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId, UnityAds.UnityAdsLoadError error, String message) {
                interstitialLoaded = false;
            }
        });
    }

    @Override
    public boolean isInterstitialLoaded() {
        return interstitialLoaded;
    }

    @Override
    public void showInterstitial() {
        if (!interstitialLoaded) return;

        activity.runOnUiThread(() -> {
            UnityAds.show(activity, INTERSTITIAL_PLACEMENT, new IUnityAdsShowListener() {
                @Override public void onUnityAdsShowStart(String placementId) {}

                @Override public void onUnityAdsShowClick(String placementId) {}

                @Override
                public void onUnityAdsShowComplete(String placementId,
                                                   UnityAds.UnityAdsShowCompletionState state) {

                    interstitialLoaded = false;
                    loadInterstitial(); // preload next
                }

                @Override public void onUnityAdsShowFailure(String placementId,
                                                            UnityAds.UnityAdsShowError error, String message) {}
            });
        });
    }

    // ================= REWARDED =================
    @Override
    public void loadRewarded() {
        if (!initialized) return;

        UnityAds.load(REWARDED_PLACEMENT, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
                rewardedLoaded = true;
            }

            @Override
            public void onUnityAdsFailedToLoad(String placementId,
                                               UnityAds.UnityAdsLoadError error, String message) {
                rewardedLoaded = false;
            }
        });
    }

    @Override
    public boolean isRewardedLoaded() {
        return rewardedLoaded;
    }

    @Override
    public void showRewarded() {
        if (!rewardedLoaded) return;

        activity.runOnUiThread(() -> {
            UnityAds.show(activity, REWARDED_PLACEMENT, new IUnityAdsShowListener() {

                @Override public void onUnityAdsShowStart(String placementId) {}

                @Override public void onUnityAdsShowClick(String placementId) {}

                @Override
                public void onUnityAdsShowComplete(String placementId,
                                                   UnityAds.UnityAdsShowCompletionState state) {

                    rewardedLoaded = false;
                    loadRewarded(); // preload next

                    if (placementId.equals(REWARDED_PLACEMENT) &&
                        state == UnityAds.UnityAdsShowCompletionState.COMPLETED) {

                        if (rewardListener != null) {
                            // 🔥 CRITICAL: back to libGDX thread
                            Gdx.app.postRunnable(() ->
                                rewardListener.onRewarded()
                            );
                        }
                    }
                }

                @Override public void onUnityAdsShowFailure(String placementId,
                                                            UnityAds.UnityAdsShowError error, String message) {}
            });
        });
    }

    // ================= LISTENER =================
    @Override
    public void setRewardListener(RewardListener listener) {
        this.rewardListener = listener;
    }

    public void toggleBannerAd () {
        // If no banner exists, show one; otherwise remove the existing one:
        if (bannerView == null) {
            // Optionally specify the banner’s anchor position:
            UnityBanners.setBannerPosition (BannerPosition.BOTTOM_CENTER);
            // Request ad content for your Placement, and load the banner:
            UnityBanners.loadBanner (activity, BANNER_PLACEMENT);
            Log.d("Unity Ads","Banner ad loading...");
        } else {
            UnityBanners.destroy ();
        }
    }

    public View getBannerView() {
        return bannerView;
    }

    private class UnityBannerListener implements IUnityBannerListener {

        @Override
        public void onUnityBannerLoaded (String placementId, View view) {
            // When the banner content loads, add it to the view hierarchy:
            bannerView = view;
            bannerLoaded = true; // placeholder if not using banner API
            layout.addView (view);
            Log.d("Unity Ads","Banner ad loaded and added to view group");
        }

        @Override
        public void onUnityBannerUnloaded (String placementId) {
            // When the banner’s no longer in use, remove it from the view hierarchy:
            bannerView = null;
        }

        @Override
        public void onUnityBannerShow (String placementId) {
            // Called when the banner is first visible to the user.
        }

        @Override
        public void onUnityBannerClick (String placementId) {
            // Called when the banner is clicked.
        }

        @Override
        public void onUnityBannerHide (String placementId) {
            // Called when the banner is hidden from the user.
        }

        @Override
        public void onUnityBannerError (String message) {
            // Called when an error occurred, and the banner failed to load or show.
        }
    }

}
