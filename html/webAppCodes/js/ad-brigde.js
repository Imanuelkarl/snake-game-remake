// Bridge between libGDX and ad manager
window.adBridge = {
  initialize: async function () {
    await window.adManager.initialize();
    console.log("Ad Bridge initialized");
  },

  // These methods are called from libGDX
  loadBanner: function () {
    window.adManager.loadBanner();
  },

  isBannerLoaded: function () {
    return window.adManager.isBannerLoaded();
  },

  showBanner: function () {
    window.adManager.showBanner();
  },

  hideBanner: function () {
    window.adManager.hideBanner();
  },

  loadInterstitial: function () {
    window.adManager.loadInterstitial();
  },

  isInterstitialLoaded: function () {
    return window.adManager.isInterstitialLoaded();
  },

  showInterstitial: function () {
    window.adManager.showInterstitial();
  },

  loadRewarded: function () {
    window.adManager.loadRewarded();
  },

  isRewardedLoaded: function () {
    return window.adManager.isRewardedLoaded();
  },

  showRewarded: function () {
    window.adManager.showRewarded();
  },

  setRewardListener: function (callback) {
    window.adManager.setRewardListener(callback);
  },
};

// Auto-initialize when page loads
if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", () => {
    window.adBridge.initialize();
  });
} else {
  window.adBridge.initialize();
}
