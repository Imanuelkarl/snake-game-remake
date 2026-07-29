package com.darealfungames.snakevsblock.gwt;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Timer;
import com.regensnakevsblock.sbb.service.ads.AdsService;
import com.regensnakevsblock.sbb.service.ads.RewardListener;

public class GWTAdsService implements AdsService {

    private RewardListener rewardListener;
    private boolean isInitialized = false;
    private boolean isApiReady = false;

    @Override
    public void initialize() {
        if (isInitialized) {
            return;
        }

        // Use Window.alert for guaranteed visibility
        Window.alert("GWTAdsService.initialize() called!");
        System.out.println("GWTAdsService.initialize() called - System.out");
        logMessage("GWTAdsService.initialize() called - JSNI log");

        // Check API immediately
        checkApiAvailability();

        // Also check after a delay
        Timer timer = new Timer() {
            @Override
            public void run() {
                Window.alert("Timer fired - checking API again");
                checkApiAvailability();
            }
        };
        timer.schedule(1000);

        isInitialized = true;
    }

    private void checkApiAvailability() {
        boolean ready = isAdApiReady();
        Window.alert("API Ready? " + ready);
        logMessage("API Ready? " + ready);

        if (ready) {
            isApiReady = true;
            Window.alert("API is ready! Initializing...");
            initializeAdAPI();
        } else {
            Window.alert("API NOT ready yet. Check your HTML script tags.");
            // Log window object for debugging
            logWindowObject();
        }
    }

    private native void logMessage(String msg) /*-{
        console.log("GWTAdsService: " + msg);
        // Also try to show in the page
        try {
            var div = document.getElementById('debug-log');
            if (!div) {
                div = document.createElement('div');
                div.id = 'debug-log';
                div.style.position = 'fixed';
                div.style.bottom = '10px';
                div.style.left = '10px';
                div.style.background = 'rgba(0,0,0,0.8)';
                div.style.color = 'white';
                div.style.padding = '10px';
                div.style.zIndex = '999999';
                div.style.fontSize = '12px';
                div.style.maxHeight = '200px';
                div.style.overflow = 'auto';
                document.body.appendChild(div);
            }
            var entry = document.createElement('div');
            entry.textContent = new Date().toLocaleTimeString() + ': ' + msg;
            div.appendChild(entry);
            div.scrollTop = div.scrollHeight;
        } catch(e) {
            // Ignore errors
        }
    }-*/;

    private native boolean isAdApiReady() /*-{
        console.log("Checking if Ad API is ready...");
        console.log("typeof window.adBreak: " + typeof $wnd.adBreak);
        console.log("typeof window.adConfig: " + typeof $wnd.adConfig);
        console.log("window.adBreak: " + $wnd.adBreak);
        console.log("window.adConfig: " + $wnd.adConfig);

        return typeof $wnd.adBreak === 'function' &&
               typeof $wnd.adConfig === 'function';
    }-*/;

    private native void logWindowObject() /*-{
        console.log("=== Window object properties ===");
        console.log("window.adBreak:", $wnd.adBreak);
        console.log("window.adConfig:", $wnd.adConfig);
        console.log("window.adsbygoogle:", $wnd.adsbygoogle);
        console.log("window.googletag:", $wnd.googletag);
        console.log("All window keys:", Object.keys($wnd));
    }-*/;

    private native void initializeAdAPI() /*-{
        console.log("Initializing Ad API...");
        if (typeof $wnd.adConfig === 'function') {
            $wnd.adConfig({
                preloadAdBreaks: 'on'
            });
            console.log("Ad API initialized successfully!");
        } else {
            console.warn("adConfig is not a function!");
        }
    }-*/;

    @Override
    public void loadInterstitial() {
        Window.alert("loadInterstitial called");
        logMessage("loadInterstitial called");
        if (!isApiReady) {
            Window.alert("API not ready, cannot load interstitial");
            return;
        }
        loadInterstitialNative();
    }

    private native void loadInterstitialNative() /*-{
        console.log("Interstitial ad preloaded.");
    }-*/;

    @Override
    public boolean isInterstitialLoaded() {
        return isApiReady;
    }

    @Override
    public void showInterstitial() {
        Window.alert("showInterstitial called");
        logMessage("showInterstitial called");
        if (!isApiReady) {
            Window.alert("API not ready, cannot show interstitial");
            return;
        }
        showInterstitialNative();
    }

    private native void showInterstitialNative() /*-{
        console.log("Attempting to show interstitial...");
        try {
            var afterAdCallback = function(placementInfo) {
                console.log("Interstitial finished. View status: " + placementInfo.viewStatus);
            };

            $wnd.adBreak({
                type: 'start',
                name: 'game-over',
                afterAd: afterAdCallback
            });
            console.log("adBreak called successfully!");
        } catch (e) {
            console.error("Error showing interstitial: " + e);
        }
    }-*/;

    @Override
    public void loadRewarded() {
        Window.alert("loadRewarded called");
        logMessage("loadRewarded called");
        if (!isApiReady) {
            Window.alert("API not ready, cannot load rewarded");
            return;
        }
        loadRewardedNative();
    }

    private native void loadRewardedNative() /*-{
        console.log("Rewarded ad preloaded.");
    }-*/;

    @Override
    public boolean isRewardedLoaded() {
        return isApiReady;
    }

    @Override
    public void showRewarded() {
        Window.alert("showRewarded called");
        logMessage("showRewarded called");
        if (!isApiReady) {
            Window.alert("API not ready, cannot show rewarded");
            return;
        }
        showRewardedNative();
    }

    private native void showRewardedNative() /*-{
        console.log("Attempting to show rewarded ad...");
        try {
            var that = this;

            var rewardCallback = function() {
                console.log("User earned a reward!");
                that.@com.darealfungames.snakevsblock.gwt.GWTAdsService::onRewardEarned()();
            };

            var afterAdCallback = function(placementInfo) {
                console.log("Rewarded ad finished. View status: " + placementInfo.viewStatus);
            };

            $wnd.adBreak({
                type: 'reward',
                name: 'free-coin',
                beforeAd: function() { console.log("Ad starting..."); },
                afterAd: afterAdCallback,
                adViewed: rewardCallback
            });
            console.log("Rewarded adBreak called successfully!");
        } catch (e) {
            console.error("Error showing rewarded ad: " + e);
        }
    }-*/;

    private void onRewardEarned() {
        Window.alert("Reward earned!");
        if (rewardListener != null) {
            rewardListener.onRewarded();
        }
    }

    @Override
    public void setRewardListener(RewardListener listener) {
        this.rewardListener = listener;
    }

    // Banner methods (stubs)
    @Override public void loadBanner() {}
    @Override public boolean isBannerLoaded() { return false; }
    @Override public void showBanner() {}
    @Override public void hideBanner() {}
}
