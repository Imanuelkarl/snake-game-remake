package com.darealfungames.snakevsblock.gwt;

import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.ads.RewardListener;

public class GoogleAdsClass implements AdsService {

    private RewardListener rewardListener;

    @Override
    public void initialize() {
        jsInitialize();
    }

    private native void jsInitialize() /*-{
        $wnd.adBridge.initialize();
    }-*/;

    // --- Banner Implementation ---

    @Override
    public native void loadBanner() /*-{
        $wnd.adBridge.loadBanner();
    }-*/;

    @Override
    public native boolean isBannerLoaded() /*-{
        return $wnd.adBridge.isBannerLoaded();
    }-*/;

    @Override
    public native void showBanner() /*-{
        $wnd.adBridge.showBanner();
    }-*/;

    @Override
    public native void hideBanner() /*-{
        $wnd.adBridge.hideBanner();
    }-*/;

    // --- Interstitial Implementation ---

    @Override
    public native void loadInterstitial() /*-{
        $wnd.adBridge.loadInterstitial();
    }-*/;

    @Override
    public native boolean isInterstitialLoaded() /*-{
        return $wnd.adBridge.isInterstitialLoaded();
    }-*/;

    @Override
    public native void showInterstitial() /*-{
        $wnd.adBridge.addTrigger("elementId");
        $wnd.adBridge.showInterstitial();
    }-*/;

    // --- Rewarded Implementation ---

    @Override
    public native void loadRewarded() /*-{
        $wnd.adBridge.loadRewarded();
    }-*/;

    @Override
    public native boolean isRewardedLoaded() /*-{
        return $wnd.adBridge.isRewardedLoaded();
    }-*/;

    @Override
    public native void showRewarded() /*-{
        $wnd.adBridge.showRewarded();
    }-*/;

    // --- Listener Binding & Reverse GWT Communication ---

    @Override
    public void setRewardListener(RewardListener listener) {
        this.rewardListener = listener;
        jsSetRewardListener(this);

    }

    private native void jsSetRewardListener(GoogleAdsClass instance) /*-{
        $wnd.adBridge.setRewardListener(function(amount, type) {
            // Call back into Java runtime environment safely
            instance.@com.darealfungames.snakevsblock.gwt.GoogleAdsClass::handleRewardFromJS(ILjava/lang/String;)(amount, type);
        });
    }-*/;

    /**
     * This method is executed by JavaScript when an ad is completed successfully.
     */
    public void handleRewardFromJS(int amount, String type) {
        if (rewardListener != null) {
            // Update game states inside the LibGDX framework thread loop safely
            rewardListener.onRewarded();
        }
    }
}
