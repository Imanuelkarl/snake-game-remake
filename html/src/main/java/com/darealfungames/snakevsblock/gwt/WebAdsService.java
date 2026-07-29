package com.darealfungames.snakevsblock.gwt;


import com.google.gwt.core.client.GWT;
import com.badlogic.gdx.Gdx;
import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.ads.RewardListener;
public class WebAdsService implements AdsService {

    private RewardListener rewardListener;

    private boolean bannerLoaded;
    private boolean interstitialLoaded;
    private boolean rewardedLoaded;

    @Override
    public void initialize() {
        injectJsHooks();

        loadBanner();
        loadInterstitial();
        loadRewarded();
        registerRewardCallback();
    }

    // =====================
    // BANNER
    // =====================
    @Override
    public void loadBanner() {
        bannerLoaded = true;
    }

    @Override
    public boolean isBannerLoaded() {
        return bannerLoaded;
    }

    @Override
    public void showBanner() {
        jsShowBanner();
    }

    @Override
    public void hideBanner() {
        jsHideBanner();
    }

    // =====================
    // INTERSTITIAL
    // =====================
    @Override
    public void loadInterstitial() {
        interstitialLoaded = true;
    }

    @Override
    public boolean isInterstitialLoaded() {
        return interstitialLoaded;
    }

    @Override
    public void showInterstitial() {
        jsShowInterstitial();
    }

    // =====================
    // REWARDED (SIMULATED)
    // =====================
    @Override
    public void loadRewarded() {
        rewardedLoaded = true;
    }

    @Override
    public boolean isRewardedLoaded() {
        return rewardedLoaded;
    }

    @Override
    public void showRewarded() {
        simulateReward();
    }

    @Override
    public void setRewardListener(RewardListener listener) {
        this.rewardListener = listener;
    }

    // =====================
    // JS INTEROP
    // =====================
    private native void jsShowBanner() /*-{
        if ($wnd.showBannerAd) $wnd.showBannerAd();
        else console.log("Banner not loaded");
    }-*/;

    private native void jsHideBanner() /*-{
        if ($wnd.hideBannerAd) $wnd.hideBannerAd();
    }-*/;

    private native void jsShowInterstitial() /*-{
        if ($wnd.showInterstitialAd) $wnd.showInterstitialAd();
        else console.log("Interstitial not loaded");
    }-*/;

    // =====================
    // REWARD SIMULATION
    // =====================
    private void simulateReward() {
        Gdx.app.postRunnable(() -> {
            if (rewardListener != null) {
                rewardListener.onRewarded();
            }
        });
    }
    private native void registerRewardCallback() /*-{
    var self = this;

    $wnd.onRewardGranted = function () {
        self.@com.darealfungames.snakevsblock.gwt.WebAdsService::grantReward()();
    };
}-*/;

    private void grantReward() {
        if (rewardListener != null) {
            rewardListener.onRewarded();
        }
    }
    // =====================
    // JS HOOKS
    // =====================
    private native void injectJsHooks() /*-{
        $wnd.showBannerAd = function () {
            console.log("Banner Ad triggered");
        };

        $wnd.showInterstitialAd = function () {
            console.log("Interstitial Ad triggered");
        };

        $wnd.hideBannerAd = function () {
            console.log("Banner hidden");
        };
    }-*/;
}
