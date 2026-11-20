//package com.regensnakevsblock.sbb.android.ads;
//
//import android.app.Activity;
//import android.util.Log;
//
//import androidx.annotation.NonNull;
//
//import com.regensnakevsblock.sbb.service.ads.AdsService;
//import com.regensnakevsblock.sbb.service.ads.RewardListener;
//import com.unity3d.mediation.LevelPlay;
//import com.unity3d.mediation.LevelPlayAdError;
//import com.unity3d.mediation.LevelPlayAdInfo;
//import com.unity3d.mediation.LevelPlayConfiguration;
//import com.unity3d.mediation.LevelPlayInitError;
//import com.unity3d.mediation.LevelPlayInitListener;
//import com.unity3d.mediation.LevelPlayInitRequest;
//import com.unity3d.mediation.banner.*;
//import com.unity3d.mediation.banner.LevelPlayBannerAdView;
//import com.unity3d.mediation.interstitial.LevelPlayInterstitialAd;
//import com.unity3d.mediation.interstitial.LevelPlayInterstitialAdListener;
//import com.unity3d.mediation.rewarded.LevelPlayReward;
//import com.unity3d.mediation.rewarded.LevelPlayRewardedAd;
//import com.unity3d.mediation.rewarded.LevelPlayRewardedAdListener;
//
//public class LevelsPlayAds implements AdsService {
//
//    private static final String TAG = "LevelsPlayAds";
//    private static final String APP_KEY = "2784e3d5d"; // Replace with your LevelPlay app key
//    private static final String BANNER_AD_UNIT = "6ecikpr7u053dsvp"; // Replace with your banner ad unit ID
//    private static final String INTERSTITIAL_AD_UNIT = "7512nti8o31zw3oh"; // Replace with your interstitial ad unit ID
//    private static final String REWARDED_AD_UNIT = "742el9jn8wc9gi58"; // Replace with your rewarded ad unit ID
//
//    private Activity activity;
//    private boolean isInitialized = false;
//
//    // Ad instances
//    private LevelPlayBannerAdView bannerAd;
//    private LevelPlayInterstitialAd interstitialAd;
//    private LevelPlayRewardedAd rewardedAd;
//
//    // Ad loaded states
//    private boolean bannerLoaded = false;
//    private boolean interstitialLoaded = false;
//    private boolean rewardedLoaded = false;
//
//    // Reward listener
//    private RewardListener rewardListener;
//
//    public LevelsPlayAds(Activity activity) {
//        this.activity = activity;
//    }
//
//    @Override
//    public void initialize() {
//        LevelPlayInitRequest initRequest = new LevelPlayInitRequest.Builder(APP_KEY)
//            .withUserId("UserID")
//            .build();
//
//        LevelPlayInitListener initListener = new LevelPlayInitListener() {
//            @Override
//            public void onInitFailed(@NonNull LevelPlayInitError error) {
//                Log.e(TAG, "Initialization failed: " + error.getErrorMessage());
//                isInitialized = false;
//                // Recommended to initialize again
//                // You could implement retry logic here
//            }
//
//            @Override
//            public void onInitSuccess(LevelPlayConfiguration configuration) {
//                Log.i(TAG, "Initialization successful");
//                isInitialized = true;
//                // Create ad objects and load ads
//                createAdObjects();
//                // Load initial ads
//
//                LevelPlay.setMetaData("is_test_suite", "enable");
//                LevelPlay.launchTestSuite(activity);
//                loadBanner();
//                loadInterstitial();
//                loadRewarded();
//
//            }
//        };
//        LevelPlay.init(activity, initRequest, initListener);
//    }
//
//    private void createAdObjects() {
//        // Create Banner Ad
//        bannerAd = new LevelPlayBannerAdView(activity, BANNER_AD_UNIT);
//        bannerAd.setBannerListener(new LevelPlayBannerAdViewListener() {
//
//
//            @Override
//            public void onAdLoaded(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//
//            }
//
//            @Override
//            public void onAdLoadFailed(@NonNull LevelPlayAdError levelPlayAdError) {
//                Log.e(TAG, "Banner ad failed to load: " + levelPlayAdError.getErrorMessage());
//                bannerLoaded = false;
//            }
//
//
//            @Override
//            public void onAdDisplayed(@NonNull LevelPlayAdInfo bannerAd) {
//                Log.i(TAG, "Banner ad displayed");
//            }
//
//            @Override
//            public void onAdDisplayFailed(@NonNull LevelPlayAdInfo levelPlayAdInfo, @NonNull LevelPlayAdError levelPlayAdError) {
//                LevelPlayBannerAdViewListener.super.onAdDisplayFailed(levelPlayAdInfo, levelPlayAdError);
//            }
//
//            @Override
//            public void onAdClicked(@NonNull LevelPlayAdInfo bannerAd) {
//                Log.i(TAG, "Banner ad clicked");
//            }
//
//            @Override
//            public void onAdExpanded(@NonNull LevelPlayAdInfo bannerAd) {
//                Log.i(TAG, "Banner ad expanded");
//            }
//
//            @Override
//            public void onAdCollapsed(@NonNull LevelPlayAdInfo bannerAd) {
//                Log.i(TAG, "Banner ad collapsed");
//            }
//
//            @Override
//            public void onAdLeftApplication(@NonNull LevelPlayAdInfo bannerAd) {
//                Log.i(TAG, "Banner ad left application");
//            }
//        });
//
//        // Create Interstitial Ad
//        interstitialAd = new LevelPlayInterstitialAd( INTERSTITIAL_AD_UNIT);
//        interstitialAd.setListener(new LevelPlayInterstitialAdListener() {
//            @Override
//            public void onAdLoaded(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Interstitial ad loaded");
//                interstitialLoaded = true;
//            }
//
//            @Override
//            public void onAdLoadFailed(@NonNull LevelPlayAdError levelPlayAdError) {
//                Log.e(TAG, "Interstitial ad failed to load: " + levelPlayAdError.getErrorMessage());
//                interstitialLoaded = false;
//            }
//
//            @Override
//            public void onAdDisplayed(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Interstitial ad displayed");
//            }
//
//            @Override
//            public void onAdDisplayFailed(@NonNull LevelPlayAdError levelPlayAdError, @NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                LevelPlayInterstitialAdListener.super.onAdDisplayFailed(levelPlayAdError, levelPlayAdInfo);
//                Log.e(TAG, "Interstitial ad failed to display: " + levelPlayAdError.getErrorMessage());
//            }
//
//            @Override
//            public void onAdClicked(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                LevelPlayInterstitialAdListener.super.onAdClicked(levelPlayAdInfo);
//                Log.i(TAG, "Interstitial ad clicked");
//            }
//
//            @Override
//            public void onAdClosed(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Interstitial ad closed");
//                // Load next interstitial
//                loadInterstitial();
//                LevelPlayInterstitialAdListener.super.onAdClosed(levelPlayAdInfo);
//            }
//
//            @Override
//            public void onAdInfoChanged(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Interstitial ad left application");
//                LevelPlayInterstitialAdListener.super.onAdInfoChanged(levelPlayAdInfo);
//            }
//        });
//
//        // Create Rewarded Ad
//        rewardedAd = new LevelPlayRewardedAd(REWARDED_AD_UNIT);
//        rewardedAd.setListener(new LevelPlayRewardedAdListener() {
//            @Override
//            public void onAdLoaded(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Rewarded ad loaded");
//                rewardedLoaded = true;
//            }
//
//            @Override
//            public void onAdLoadFailed(@NonNull LevelPlayAdError levelPlayAdError) {
//                Log.e(TAG, "Rewarded ad failed to load: " + levelPlayAdError.getErrorMessage());
//                rewardedLoaded = false;
//            }
//
//            @Override
//            public void onAdDisplayed(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Rewarded ad displayed");
//            }
//
//            @Override
//            public void onAdRewarded(@NonNull LevelPlayReward levelPlayReward, @NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Rewarded ad rewarded: " + levelPlayReward.getAmount() + " " + levelPlayReward.getName());
//                if (rewardListener != null) {
//                    rewardListener.onRewarded();
//                }
//            }
//
//            @Override
//            public void onAdDisplayFailed(@NonNull LevelPlayAdError levelPlayAdError, @NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.e(TAG, "Rewarded ad failed to display: " + levelPlayAdError.getErrorMessage());
//                LevelPlayRewardedAdListener.super.onAdDisplayFailed(levelPlayAdError, levelPlayAdInfo);
//            }
//
//            @Override
//            public void onAdClicked(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Rewarded ad clicked");
//                LevelPlayRewardedAdListener.super.onAdClicked(levelPlayAdInfo);
//            }
//
//            @Override
//            public void onAdClosed(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Rewarded ad closed");
//                // Load next rewarded ad
//                loadRewarded();
//                LevelPlayRewardedAdListener.super.onAdClosed(levelPlayAdInfo);
//            }
//
//            @Override
//            public void onAdInfoChanged(@NonNull LevelPlayAdInfo levelPlayAdInfo) {
//                Log.i(TAG, "Rewarded ad left application");
//                LevelPlayRewardedAdListener.super.onAdInfoChanged(levelPlayAdInfo);
//            }
//
//        });
//    }
//
//    @Override
//    public void loadBanner() {
//        if (!isInitialized || bannerAd == null) {
//            Log.w(TAG, "Cannot load banner: SDK not initialized or banner ad not created");
//            return;
//        }
//        bannerAd.loadAd();
//    }
//
//    @Override
//    public boolean isBannerLoaded() {
//        return bannerLoaded;
//    }
//
//    @Override
//    public void showBanner() {
//        if (!isInitialized || bannerAd == null) {
//            Log.w(TAG, "Cannot show banner: SDK not initialized or banner ad not created");
//            return;
//        }
//        // The banner should already be attached to a container in your layout
//        // You need to add the bannerAd view to your layout before showing it
//        // This implementation assumes the banner is already added to the view hierarchy
//        // and we're just showing it
//        if (bannerAd.getParent() != null) {
//            bannerAd.setVisibility(android.view.View.VISIBLE);
//        } else {
//            Log.w(TAG, "Banner ad not attached to any view. Please add it to a container first.");
//        }
//    }
//
//    @Override
//    public void hideBanner() {
//        if (bannerAd != null) {
//            bannerAd.setVisibility(android.view.View.GONE);
//        }
//    }
//
//    @Override
//    public void loadInterstitial() {
//        if (!isInitialized || interstitialAd == null) {
//            Log.w(TAG, "Cannot load interstitial: SDK not initialized or interstitial ad not created");
//            return;
//        }
//        interstitialAd.loadAd();
//    }
//
//    @Override
//    public boolean isInterstitialLoaded() {
//        return interstitialLoaded;
//    }
//
//    @Override
//    public void showInterstitial() {
//        if (!isInitialized || interstitialAd == null) {
//            Log.w(TAG, "Cannot show interstitial: SDK not initialized or interstitial ad not created");
//            return;
//        }
//        if (interstitialLoaded) {
//            interstitialAd.showAd(activity);
//        } else {
//            Log.w(TAG, "Interstitial ad not loaded, loading now...");
//            loadInterstitial();
//        }
//    }
//
//    @Override
//    public void loadRewarded() {
//        if (!isInitialized || rewardedAd == null) {
//            Log.w(TAG, "Cannot load rewarded ad: SDK not initialized or rewarded ad not created");
//            return;
//        }
//        rewardedAd.loadAd();
//    }
//
//    @Override
//    public boolean isRewardedLoaded() {
//        return rewardedLoaded;
//    }
//
//    @Override
//    public void showRewarded() {
//        if (!isInitialized || rewardedAd == null) {
//            Log.w(TAG, "Cannot show rewarded ad: SDK not initialized or rewarded ad not created");
//            return;
//        }
//        if (rewardedLoaded) {
//            rewardedAd.showAd(activity);
//        } else {
//            Log.w(TAG, "Rewarded ad not loaded, loading now...");
//            loadRewarded();
//        }
//    }
//
//    @Override
//    public void setRewardListener(RewardListener listener) {
//        this.rewardListener = listener;
//    }
//
//    // Optional: Clean up method to release resources
//    public void destroy() {
//        if (bannerAd != null) {
//            bannerAd.destroy();
//        }
//        // Interstitial and rewarded ads don't have explicit destroy methods
//        // They will be garbage collected when the activity is destroyed
//    }
//}
