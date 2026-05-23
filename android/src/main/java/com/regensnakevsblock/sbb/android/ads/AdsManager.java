package com.regensnakevsblock.sbb.android.ads;

import android.app.Activity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.badlogic.gdx.Gdx;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.regensnakevsblock.sbb.config.Constants;
import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.ads.RewardListener;

public class AdsManager implements AdsService {

    private final Activity activity;
    private RewardListener rewardListener;

    // ---------- Test IDs ----------

    // ---------- Banner ----------
    private AdView bannerView;
    private boolean bannerLoaded = false;

    // ---------- Interstitial ----------
    private InterstitialAd interstitialAd;

    // ---------- Rewarded ----------
    private RewardedAd rewardedAd;

    public AdsManager(Activity activity) {
        this.activity = activity;
    }

    // ================= INIT =================
    @Override
    public void initialize() {
        MobileAds.initialize(activity);

        loadBanner();
        loadInterstitial();
        loadRewarded();
    }

    // ================= BANNER =================
    @Override
    public void loadBanner() {
        activity.runOnUiThread(() -> {
            bannerView = new AdView(activity);
            bannerView.setAdUnitId(Constants.googleBannerAdId);
            bannerView.setAdSize(AdSize.BANNER);

            bannerView.setAdListener(new AdListener() {
                @Override public void onAdLoaded() { bannerLoaded = true; }
                @Override public void onAdFailedToLoad(LoadAdError adError) { bannerLoaded = false; }
            });

            AdRequest request = new AdRequest.Builder().build();
            bannerView.loadAd(request);

            // attach to layout
            FrameLayout layout = activity.findViewById(android.R.id.content);
            layout.addView(bannerView,
                new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            );
        });
    }

    @Override
    public boolean isBannerLoaded() {
        return bannerLoaded;
    }

    @Override
    public void showBanner() {
        activity.runOnUiThread(() -> {
            if (bannerView != null) bannerView.setVisibility(AdView.VISIBLE);
        });
    }

    @Override
    public void hideBanner() {
        activity.runOnUiThread(() -> {
            if (bannerView != null) bannerView.setVisibility(AdView.GONE);
        });
    }

    // ================= INTERSTITIAL =================
    @Override
    public void loadInterstitial() {
        AdRequest request = new AdRequest.Builder().build();

        InterstitialAd.load(activity, Constants.googleInterstitialAdId, request,
            new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(InterstitialAd ad) {
                    interstitialAd = ad;
                }

                @Override
                public void onAdFailedToLoad(LoadAdError error) {
                    interstitialAd = null;
                }
            });
    }

    @Override
    public boolean isInterstitialLoaded() {
        return interstitialAd != null;
    }

    @Override
    public void showInterstitial() {
        if (interstitialAd == null) return;

        activity.runOnUiThread(() -> {
            interstitialAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override public void onAdDismissedFullScreenContent() {
                        interstitialAd = null;
                        loadInterstitial(); // preload next
                    }
                });

            interstitialAd.show(activity);
        });
    }

    // ================= REWARDED =================
    @Override
    public void loadRewarded() {
        AdRequest request = new AdRequest.Builder().build();

        RewardedAd.load(activity, Constants.googleRewardedAdId, request,
            new RewardedAdLoadCallback() {
                @Override
                public void onAdLoaded(RewardedAd ad) {
                    rewardedAd = ad;
                }

                @Override
                public void onAdFailedToLoad(LoadAdError error) {
                    rewardedAd = null;
                }
            });
    }

    @Override
    public boolean isRewardedLoaded() {
        return rewardedAd != null;
    }

    @Override
    public void showRewarded() {
        if (rewardedAd == null) return;

        activity.runOnUiThread(() -> {
            rewardedAd.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override public void onAdDismissedFullScreenContent() {
                        rewardedAd = null;
                        loadRewarded(); // preload next
                    }
                });

            rewardedAd.show(activity, rewardItem -> {
                // 🔥 CRITICAL: return to libGDX thread
                if (rewardListener != null) {
                    Gdx.app.postRunnable(() -> rewardListener.onRewarded());
                }
            });
        });
    }

    @Override
    public void setRewardListener(RewardListener listener) {
        this.rewardListener = listener;
    }
}
