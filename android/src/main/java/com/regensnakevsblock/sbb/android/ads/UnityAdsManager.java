package com.regensnakevsblock.sbb.android.ads;

import android.app.Activity;

import com.badlogic.gdx.Gdx;
import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.ads.RewardListener;
import com.unity3d.ads.IUnityAdsInitializationListener;
import com.unity3d.ads.IUnityAdsLoadListener;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;

public class UnityAdsManager implements AdsService {

    private final Activity activity;
    private RewardListener rewardListener;

    // ================= CONFIG =================
    private static final String GAME_ID = "1234567"; // replace later
    private static final boolean TEST_MODE = true;

    private static final String REWARDED_PLACEMENT = "rewardedVideo";
    private static final String INTERSTITIAL_PLACEMENT = "video";

    // ================= STATE =================
    private boolean rewardedLoaded = false;
    private boolean interstitialLoaded = false;
    private boolean bannerLoaded = false;
    private boolean initialized = false;

    public UnityAdsManager(Activity activity) {
        this.activity = activity;
    }

    // ================= INIT =================
    @Override
    public void initialize() {
        UnityAds.initialize(activity, GAME_ID, TEST_MODE,
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
        bannerLoaded = true; // placeholder if not using banner API
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

        UnityAds.load(INTERSTITIAL_PLACEMENT, new IUnityAdsLoadListener() {
            @Override
            public void onUnityAdsAdLoaded(String placementId) {
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
}
