console.log("Initializing CrazyGames SDK...");
window.crazyGamesAds = (function () {
  var sdk = null;
  var ready = false;
  var bannerLoaded = false;
  var interstitialLoaded = false;
  var rewardedLoaded = false;
  var rewardCallback = null;

  function init() {
    if (ready) return;
    if (typeof window.CrazyGames === "undefined" || !window.CrazyGames.SDK) {
      console.warn("CrazyGames SDK not found. Ads will be skipped.");
      ready = true;
      return;
    }
    window.CrazyGames.SDK.init()
      .then(function (initializedSdk) {
        sdk = initializedSdk;
        ready = true;
        console.log("CrazyGames SDK initialized.");
      })
      .catch(function (err) {
        console.error("CrazyGames SDK init failed:", err);
        ready = true;
      });
  }

  return {
    init: init,

    loadBanner: function () {
      bannerLoaded = !!sdk;
    },
    isBannerLoaded: function () {
      return bannerLoaded;
    },
    showBanner: function () {
      if (!sdk) return;
      sdk.banner
        .requestBanner({
          id: "canvas-banner-overlay",
          width: 300,
          height: 250,
        })
        .then(function () {
          var el = document.getElementById("canvas-banner-overlay");
          if (el) el.style.display = "block";
          bannerLoaded = true;
        })
        .catch(function (err) {
          console.warn("Banner error:", err);
          bannerLoaded = false;
        });
    },
    hideBanner: function () {
      if (sdk) {
        try {
          sdk.banner.clearBanner("canvas-banner-overlay");
        } catch (e) {}
      }
      var el = document.getElementById("canvas-banner-overlay");
      if (el) el.style.display = "none";
      bannerLoaded = false;
    },

    loadInterstitial: function () {
      interstitialLoaded = ready;
    },
    isInterstitialLoaded: function () {
      return interstitialLoaded;
    },
    showInterstitial: function () {
      if (!sdk) {
        console.log("Interstitial skipped: SDK not available.");
        return;
      }
      sdk.ad.requestAd("midgame", {
        adStarted: function () {
          console.log("Interstitial started");
        },
        adFinished: function () {
          console.log("Interstitial finished");
        },
        adError: function (err) {
          console.warn("Interstitial error:", err);
        },
      });
    },

    loadRewarded: function () {
      rewardedLoaded = ready;
    },
    isRewardedLoaded: function () {
      return rewardedLoaded;
    },
    showRewarded: function () {
      if (!sdk) {
        console.log("Rewarded skipped: SDK not available.");
        return;
      }
      var cb = rewardCallback;
      sdk.ad.requestAd("rewarded", {
        adStarted: function () {
          console.log("Rewarded started");
        },
        adFinished: function () {
          console.log("Rewarded finished. Granting reward.");
          if (cb) cb();
        },
        adError: function (err) {
          console.warn("Rewarded error:", err);
        },
      });
    },
    setRewardListener: function (callback) {
      rewardCallback = callback;
    },
  };
})();

// Start initialization immediately
window.crazyGamesAds.init();
