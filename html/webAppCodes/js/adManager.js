// Main Ad Manager
class AdManager {
  constructor() {
    this.platform = null;
    this.initialized = false;
    this.currentPlatform = window.AD_CONFIG?.platform || "google";
  }

  async initialize() {
    if (this.initialized) return;

    const config = window.AD_CONFIG || {};

    // Select platform based on configuration
    switch (this.currentPlatform) {
      case "google":
        this.platform = new GoogleAdsPlatform(config.google);
        break;
      case "adsgram":
        this.platform = new AdsgramPlatform(config.adsgram);
        break;
      case "poki":
        this.platform = new PokiPlatform(config.poki);
        break;
      default:
        console.warn("Unknown platform, using Google as fallback");
        this.platform = new GoogleAdsPlatform(config.google);
        break;
    }

    await this.platform.initialize();
    this.initialized = true;
    console.log(`Ad Manager initialized with ${this.currentPlatform} platform`);
  }

  // Delegate methods to platform
  loadBanner() {
    this.platform.loadBanner();
  }
  isBannerLoaded() {
    return this.platform.isBannerLoaded();
  }
  showBanner() {
    this.platform.showBanner();
  }
  hideBanner() {
    this.platform.hideBanner();
  }

  loadInterstitial() {
    this.platform.loadInterstitial();
  }
  isInterstitialLoaded() {
    return this.platform.isInterstitialLoaded();
  }
  showInterstitial() {
    this.platform.showInterstitial();
  }

  loadRewarded() {
    this.platform.loadRewarded();
  }
  isRewardedLoaded() {
    return this.platform.isRewardedLoaded();
  }
  showRewarded() {
    this.platform.showRewarded();
  }

  setRewardListener(callback) {
    this.platform.setRewardListener(callback);
  }
}

// Singleton instance
window.adManager = new AdManager();
