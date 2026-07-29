// Poki Platform
class PokiPlatform {
    constructor(config) {
        this.config = config;
        this.rewardedLoaded = false;
        this.bannerLoaded = false;
        this.interstitialLoaded = false;
        this.javaRewardListener = null;
    }

    initialize() {
        return new Promise((resolve) => {
            // Initialize Poki SDK
            if (typeof window.PokiAPI !== 'undefined') {
                window.PokiAPI.init({
                    gameId: this.config.gameId
                });
                resolve();
            } else {
                console.warn('Poki SDK not found, using fallback');
                resolve();
            }
        });
    }

    loadBanner() {
        console.log('Loading Poki banner...');
        this.bannerLoaded = true;
        // Poki banner implementation
    }

    isBannerLoaded() {
        return this.bannerLoaded;
    }

    showBanner() {
        console.log('Showing Poki banner');
        // Show Poki banner
    }

    hideBanner() {
        console.log('Hiding Poki banner');
        // Hide Poki banner
    }

    loadInterstitial() {
        console.log('Loading Poki interstitial...');
        this.interstitialLoaded = true;
        // Poki interstitial implementation
    }

    isInterstitialLoaded() {
        return this.interstitialLoaded;
    }

    showInterstitial() {
        console.log('Showing Poki interstitial');
        // Show Poki interstitial
    }

    loadRewarded() {
        console.log('Loading Poki rewarded ad...');
        this.rewardedLoaded = true;
        // Poki rewarded implementation
    }

    isRewardedLoaded() {
        return this.rewardedLoaded;
    }

    showRewarded() {
        console.log('Showing Poki rewarded ad');
        // Show Poki rewarded ad
        if (this.javaRewardListener) {
            this.javaRewardListener(100, 'coins');
        }
    }

    setRewardListener(callback) {
        this.javaRewardListener = callback;
    }
}