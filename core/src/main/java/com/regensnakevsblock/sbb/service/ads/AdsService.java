package com.regensnakevsblock.sbb.service.ads;

public interface AdsService {
    // Initialization
    void initialize();

    // Banner
    void loadBanner();
    boolean isBannerLoaded();
    void showBanner();
    void hideBanner();

    // Interstitial
    void loadInterstitial();
    boolean isInterstitialLoaded();
    void showInterstitial();

    // Rewarded
    void loadRewarded();
    boolean isRewardedLoaded();
    void showRewarded();

    // Reward callback binding
    void setRewardListener(RewardListener listener);
}
