// Google Ad Manager Platform
class GoogleAdsPlatform {
  constructor(config) {
    this.config = config;
    this.bannerSlot = null;
    this.interstitialSlot = null;
    this.rewardedSlot = null;
    this.rewardedEvent = null;
    this.bannerLoaded = false;
    this.interstitialLoaded = false;
    this.rewardedLoaded = false;
    this.javaRewardListener = null;
  }

  initialize() {
    return new Promise((resolve) => {
      const self = this;
      window.googletag = window.googletag || { cmd: [] };

      // Load Google GPT script if not already loaded
      if (
        !document.querySelector('script[src*="securepubads.g.doubleclick.net"]')
      ) {
        const script = document.createElement("script");
        script.async = true;
        script.src = "https://securepubads.g.doubleclick.net/tag/js/gpt.js";
        document.head.appendChild(script);
      }

      googletag.cmd.push(function () {
        // Setup Banner Slot
        self.bannerSlot = googletag
          .defineSlot(
            self.config.bannerAdUnit,
            self.config.bannerSize,
            "canvas-banner-overlay",
          )
          .addService(googletag.pubads());

        // Setup Interstitial Slot
        self.interstitialSlot = googletag.defineOutOfPageSlot(
          self.config.interstitialAdUnit,
          googletag.enums.OutOfPageFormat.INTERSTITIAL,
        );
        if (self.interstitialSlot) {
          self.interstitialSlot.addService(googletag.pubads());
        }

        // AD EVENT LISTENERS
        googletag
          .pubads()
          .addEventListener("slotRenderEnded", function (event) {
            if (event.slot === self.bannerSlot) {
              self.bannerLoaded = !event.isEmpty;
            }
          });

        googletag.pubads().addEventListener("slotOnload", function (event) {
          if (event.slot === self.interstitialSlot) {
            console.log("Interstitial fully loaded");
            self.interstitialLoaded = true;
          }
        });

        googletag
          .pubads()
          .addEventListener("rewardedSlotReady", function (event) {
            console.log("Rewarded ad ready");
            self.rewardedEvent = event;
            self.rewardedLoaded = true;
          });

        googletag
          .pubads()
          .addEventListener("rewardedSlotGranted", function (event) {
            if (self.javaRewardListener) {
              self.javaRewardListener(event.payload.amount, event.payload.type);
            }
          });

        googletag.pubads().addEventListener("rewardedSlotClosed", function () {
          self.rewardedLoaded = false;
          self.rewardedEvent = null;
          if (self.rewardedSlot) {
            googletag.destroySlots([self.rewardedSlot]);
            self.rewardedSlot = null;
          }
        });

        // Enable services
        googletag.pubads().enableSingleRequest();
        googletag.enableServices();

        // Display interstitial slot
        if (self.interstitialSlot) {
          googletag.display(self.interstitialSlot);
        }

        resolve();
      });
    });
  }

  loadBanner() {
    const self = this;
    googletag.cmd.push(function () {
      googletag.display("canvas-banner-overlay");
      googletag.pubads().refresh([self.bannerSlot]);
    });
  }

  isBannerLoaded() {
    return this.bannerLoaded;
  }

  showBanner() {
    const el = document.getElementById("canvas-banner-overlay");
    if (el) el.style.display = "block";
  }

  hideBanner() {
    const el = document.getElementById("canvas-banner-overlay");
    if (el) el.style.display = "none";
  }

  loadInterstitial() {
    const self = this;
    console.log("Loading interstitial ads...");
    this.interstitialLoaded = false;
    googletag.cmd.push(function () {
      if (self.interstitialSlot) {
        googletag.pubads().refresh([self.interstitialSlot]);
        console.log("Refreshing interstitial slot data...");
      }
    });
  }

  isInterstitialLoaded() {
    return this.interstitialLoaded;
  }

  showInterstitial() {
    console.log("Showing interstitial ad");
    const trigger = document.getElementById("hidden-interstitial-trigger");
    if (!trigger) {
      const newTrigger = document.createElement("a");
      newTrigger.id = "hidden-interstitial-trigger";
      newTrigger.style.display = "none";
      document.body.appendChild(newTrigger);
    }
    const triggerElement = document.getElementById(
      "hidden-interstitial-trigger",
    );
    triggerElement.href =
      window.location.origin +
      window.location.pathname +
      "#show-interstitial-ad";
    triggerElement.click();
  }

  loadRewarded() {
    const self = this;
    console.log("Loading rewarded ads...");
    this.rewardedLoaded = false;
    this.rewardedEvent = null;
    googletag.cmd.push(function () {
      if (self.rewardedSlot) {
        googletag.destroySlots([self.rewardedSlot]);
      }
      self.rewardedSlot = googletag.defineOutOfPageSlot(
        self.config.rewardedAdUnit,
        googletag.enums.OutOfPageFormat.REWARDED,
      );
      if (self.rewardedSlot) {
        self.rewardedSlot.addService(googletag.pubads());
        googletag.display(self.rewardedSlot);
      }
    });
  }

  isRewardedLoaded() {
    return this.rewardedLoaded;
  }

  showRewarded() {
    if (this.rewardedEvent) {
      this.rewardedEvent.makeRewardedVisible();
    }
  }

  setRewardListener(callback) {
    this.javaRewardListener = callback;
  }
}
