Embulk::JavaPlugin.register_input(
  "facebook_ads_insights", "org.embulk.input.facebook_ads_insights.FacebookAdsInsightsInputPlugin",
  File.expand_path('../../../../classpath', __FILE__))
