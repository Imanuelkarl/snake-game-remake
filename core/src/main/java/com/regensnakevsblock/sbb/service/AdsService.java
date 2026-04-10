package com.regensnakevsblock.sbb.service;

public interface AdsService {
    void showInterstitialAd();
    void showRewardedAd();
    boolean isRewarded();
    void setRewarded(boolean rewarded);
    boolean isBannerAdLoaded();
    boolean isInterstitialAdLoaded();
    boolean isRewardedAdLoaded();
    void loadRewardedAd();
    void loadInterstitialAds();
}
