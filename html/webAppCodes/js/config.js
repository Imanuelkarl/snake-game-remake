// Platform configuration with auto-refresh settings
window.AD_CONFIG = {
  // Set this to 'google', 'adsgram', 'poki', or 'custom'
  platform: "google",

  // Auto-refresh settings (global)
  autoRefresh: {
    enabled: true,
    delay: 2000, // milliseconds
  },

  // Google Ads Configuration
  google: {
    bannerAdUnit: "/6355419/Travel/Europe",
    interstitialAdUnit: "/21775744923/example/interstitial",
    rewardedAdUnit: "/22639388115/rewarded_web_example",
    bannerSize: [300, 250],
  },

  // Adsgram (Telegram) Configuration
  adsgram: {
    blockId: "your-adsgram-block-id",
    rewardId: "your-adsgram-reward-id",
  },

  // Poki Configuration
  poki: {
    gameId: "your-poki-game-id",
  },

  // Custom platform configuration
  custom: {
    // Add your custom platform settings here
  },
};
