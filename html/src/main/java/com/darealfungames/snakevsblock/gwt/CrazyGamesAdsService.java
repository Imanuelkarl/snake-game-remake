package com.darealfungames.snakevsblock.gwt;

import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.ads.RewardListener;

public class CrazyGamesAdsService implements AdsService {

    private RewardListener rewardListener;

    @Override
    public void initialize() {
        initNative();
    }

    private native void initNative() /*-{
        if ($wnd.crazyGamesAds) {
            $wnd.crazyGamesAds.init();
        }
    }-*/;

    // -----------------------------------------------------------------
    // Banner
    // -----------------------------------------------------------------
    @Override
    public void loadBanner() {
        loadBannerNative();
    }

    private native void loadBannerNative() /*-{
        if ($wnd.crazyGamesAds) $wnd.crazyGamesAds.loadBanner();
    }-*/;

    @Override
    public native boolean isBannerLoaded() /*-{
        return $wnd.crazyGamesAds ? $wnd.crazyGamesAds.isBannerLoaded() : false;
    }-*/;

    @Override
    public void showBanner() {
        showBannerNative();
    }

    private native void showBannerNative() /*-{
        if ($wnd.crazyGamesAds) $wnd.crazyGamesAds.showBanner();
    }-*/;

    @Override
    public void hideBanner() {
        hideBannerNative();
    }

    private native void hideBannerNative() /*-{
        if ($wnd.crazyGamesAds) $wnd.crazyGamesAds.hideBanner();
    }-*/;

    // -----------------------------------------------------------------
    // Interstitial
    // -----------------------------------------------------------------
    @Override
    public void loadInterstitial() {
        loadInterstitialNative();
    }

    private native void loadInterstitialNative() /*-{
        if ($wnd.crazyGamesAds) $wnd.crazyGamesAds.loadInterstitial();
    }-*/;

    @Override
    public native boolean isInterstitialLoaded() /*-{
        return $wnd.crazyGamesAds ? $wnd.crazyGamesAds.isInterstitialLoaded() : false;
    }-*/;

    @Override
    public void showInterstitial() {
        showInterstitialNative();
    }

    private native void showInterstitialNative() /*-{
        if ($wnd.crazyGamesAds) $wnd.crazyGamesAds.showInterstitial();
    }-*/;

    // -----------------------------------------------------------------
    // Rewarded
    // -----------------------------------------------------------------
    @Override
    public void loadRewarded() {
        loadRewardedNative();
    }

    private native void loadRewardedNative() /*-{
        if ($wnd.crazyGamesAds) $wnd.crazyGamesAds.loadRewarded();
    }-*/;

    @Override
    public native boolean isRewardedLoaded() /*-{
        return $wnd.crazyGamesAds ? $wnd.crazyGamesAds.isRewardedLoaded() : false;
    }-*/;

    @Override
    public void showRewarded() {
        showRewardedNative();
    }

    private native void showRewardedNative() /*-{
        if ($wnd.crazyGamesAds) $wnd.crazyGamesAds.showRewarded();
    }-*/;

    @Override
    public void setRewardListener(RewardListener listener) {
        this.rewardListener = listener;
        setRewardListenerNative();
    }

    private native void setRewardListenerNative() /*-{
        var self = this;
        if ($wnd.crazyGamesAds) {
            $wnd.crazyGamesAds.setRewardListener($entry(function() {
                self.@com.darealfungames.snakevsblock.gwt.CrazyGamesAdsService::onRewardEarned()();
            }));
        }
    }-*/;

    private void onRewardEarned() {
        if (rewardListener != null) {
            rewardListener.onRewarded();
        }
    }
}
