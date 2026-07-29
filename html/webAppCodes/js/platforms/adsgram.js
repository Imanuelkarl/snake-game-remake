// Adsgram Platform for Telegram
class AdsgramPlatform {
    constructor(config) {
        this.config = config;
        this.rewardedLoaded = false;
        this.bannerLoaded = false;
        this.interstitialLoaded = false;
        this.javaRewardListener = null;
        this.currentAd = null;
    }

    initialize() {
        return new Promise((resolve) => {
            // Load Adsgram SDK
            if (typeof window.Adsgram === 'undefined') {
                const script = document.createElement('script');
                script.src = 'https://adsgram.ai/js/adsgram.min.js';
                script.async = true;
                script.onload = () => resolve();
                script.onerror = () => {
                    console.warn('Adsgram SDK failed to load, using fallback');
                    resolve();
                };
                document.head.appendChild(script);
            } else {
                resolve();
            }
        });
    }

    loadBanner() {
        // Adsgram uses different approach for banners
        console.log('Loading Adsgram banner...');
        // Implementation depends on Adsgram's specific API
        // This is a placeholder
        this.bannerLoaded = true;
    }

    isBannerLoaded() {
        return this.bannerLoaded;
    }

    showBanner() {
        // Show banner implementation
        console.log('Showing Adsgram banner');
    }

    hideBanner() {
        console.log('Hiding Adsgram banner');
    }

    loadInterstitial() {
        console.log('Loading Adsgram interstitial...');
        this.interstitialLoaded = true;
        // Implementation for interstitial ads
    }

    isInterstitialLoaded() {
        return this.interstitialLoaded;
    }

    showInterstitial() {
        console.log('Showing Adsgram interstitial');
        // Show interstitial
    }

    loadRewarded() {
        console.log('Loading Adsgram rewarded ad...');
        // Load rewarded ad
        this.rewardedLoaded = true;
    }

    isRewardedLoaded() {
        return this.rewardedLoaded;
    }

    showRewarded() {
        console.log('Showing Adsgram rewarded ad');
        // Show rewarded ad
        // On reward, call listener
        if (this.javaRewardListener) {
            this.javaRewardListener(100, 'coins');
        }
    }

    setRewardListener(callback) {
        this.javaRewardListener = callback;
    }
}