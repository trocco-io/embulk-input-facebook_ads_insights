package org.embulk.input.facebook_ads_insights;

import com.facebook.ads.sdk.AdsActionStats;
import com.facebook.ads.sdk.AdsInsights;
import com.google.gson.JsonElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdsInsightsAccessor
{
    private final Logger logger =  LoggerFactory.getLogger(AdsInsightsAccessor.class);

    private final AdsInsights adsInsights;

    public AdsInsightsAccessor(final AdsInsights adsInsights)
    {
        this.adsInsights = adsInsights;
    }

    public String get(String name)
    {
        switch (name) {
            case "account_currency": return adsInsights.getFieldAccountCurrency();
            case "account_id": return adsInsights.getFieldAccountId();
            case "account_name": return adsInsights.getFieldAccountName();
            case "action_values": return actionsToString(adsInsights.getFieldActionValues());
            case "actions": return actionsToString(adsInsights.getFieldActions());
            case "ad_id": return adsInsights.getFieldAdId();
            case "ad_name": return adsInsights.getFieldAdName();
            case "adset_id": return adsInsights.getFieldAdsetId();
            case "adset_name": return adsInsights.getFieldAdsetName();
            case "app_store_clicks": return adsInsights.getFieldAppStoreClicks();
            case "buying_type": return adsInsights.getFieldBuyingType();
            case "campaign_id": return adsInsights.getFieldCampaignId();
            case "campaign_name": return adsInsights.getFieldCampaignName();
            case "canvas_avg_view_percent": return adsInsights.getFieldCanvasAvgViewPercent();
            case "canvas_avg_view_time": return adsInsights.getFieldCanvasAvgViewTime();
            case "clicks": return adsInsights.getFieldClicks();
            case "conversion_rate_ranking": return adsInsights.getFieldConversionRateRanking();
            case "conversion_values": return actionsToString(adsInsights.getFieldConversionValues());
            case "conversions": return actionsToString(adsInsights.getFieldConversions());
            case "cost_per_10_sec_video_view": return actionsToString(adsInsights.getFieldCostPer10SecVideoView());
            case "cost_per_action_type": return actionsToString(adsInsights.getFieldCostPerActionType());
            case "cost_per_conversion": return actionsToString(adsInsights.getFieldCostPerConversion());
            case "cost_per_estimated_ad_recallers": return adsInsights.getFieldCostPerEstimatedAdRecallers();
            case "cost_per_inline_link_click": return adsInsights.getFieldCostPerInlineLinkClick();
            case "cost_per_inline_post_engagement": return adsInsights.getFieldCostPerInlinePostEngagement();
            case "cost_per_outbound_click": return actionsToString(adsInsights.getFieldCostPerOutboundClick());
            case "cost_per_thruplay": return actionsToString(adsInsights.getFieldCostPerThruplay());
            case "cost_per_unique_action_type": return actionsToString(adsInsights.getFieldCostPerUniqueActionType());
            case "cost_per_unique_click": return adsInsights.getFieldCostPerUniqueClick();
            case "cost_per_unique_inline_link_click": return adsInsights.getFieldCostPerUniqueInlineLinkClick();
            case "cost_per_unique_outbound_click": return actionsToString(adsInsights.getFieldCostPerUniqueOutboundClick());
            case "cpc": return adsInsights.getFieldCpc();
            case "cpm": return adsInsights.getFieldCpm();
            case "cpp": return adsInsights.getFieldCpp();
            case "ctr": return adsInsights.getFieldCtr();
            case "date_start": return adsInsights.getFieldDateStart();
            case "date_stop": return adsInsights.getFieldDateStop();
            case "deeplink_clicks": return adsInsights.getFieldDeeplinkClicks();
            case "engagement_rate_ranking": return adsInsights.getFieldEngagementRateRanking();
            case "estimated_ad_recall_rate": return adsInsights.getFieldEstimatedAdRecallRate();
            case "estimated_ad_recallers": return adsInsights.getFieldEstimatedAdRecallers();
            case "frequency": return adsInsights.getFieldFrequency();
            case "full_view_impressions": return adsInsights.getFieldFullViewImpressions();
            case "full_view_reach": return adsInsights.getFieldFullViewReach();
            case "impressions": return adsInsights.getFieldImpressions();
            case "inline_link_click_ctr": return adsInsights.getFieldInlineLinkClickCtr();
            case "inline_link_clicks": return adsInsights.getFieldInlineLinkClicks();
            case "inline_post_engagement": return adsInsights.getFieldInlinePostEngagement();
            case "instant_experience_clicks_to_open": return adsInsights.getFieldInstantExperienceClicksToOpen();
            case "instant_experience_clicks_to_start": return adsInsights.getFieldInstantExperienceClicksToStart();
            case "instant_experience_outbound_clicks": return adsInsights.getFieldInstantExperienceOutboundClicks();
            case "mobile_app_purchase_roas": return actionsToString(adsInsights.getFieldMobileAppPurchaseRoas());
            case "newsfeed_avg_position": return adsInsights.getFieldNewsfeedAvgPosition();
            case "newsfeed_clicks": return adsInsights.getFieldNewsfeedClicks();
            case "newsfeed_impressions": return adsInsights.getFieldNewsfeedImpressions();
            case "objective": return adsInsights.getFieldObjective();
            case "outbound_clicks": return actionsToString(adsInsights.getFieldOutboundClicks());
            case "outbound_clicks_ctr": return actionsToString(adsInsights.getFieldOutboundClicksCtr());
            case "purchase_roas": return actionsToString(adsInsights.getFieldPurchaseRoas());
            case "quality_ranking": return adsInsights.getFieldQualityRanking();
            case "reach": return adsInsights.getFieldReach();
            case "relevance_score": return adsInsights.getFieldRelevanceScore().toString();
            case "social_spend": return adsInsights.getFieldSocialSpend();
            case "spend": return adsInsights.getFieldSpend();
            case "unique_actions": return actionsToString(adsInsights.getFieldUniqueActions());
            case "unique_clicks": return adsInsights.getFieldUniqueClicks();
            case "unique_ctr": return adsInsights.getFieldUniqueCtr();
            case "unique_impressions": return adsInsights.getFieldUniqueImpressions();
            case "unique_inline_link_click_ctr": return adsInsights.getFieldUniqueInlineLinkClickCtr();
            case "unique_inline_link_clicks": return adsInsights.getFieldUniqueInlineLinkClicks();
            case "unique_link_clicks_ctr": return adsInsights.getFieldUniqueLinkClicksCtr();
            case "unique_outbound_clicks": return actionsToString(adsInsights.getFieldUniqueOutboundClicks());
            case "unique_outbound_clicks_ctr": return actionsToString(adsInsights.getFieldUniqueOutboundClicksCtr());
            case "video_10_sec_watched_actions": return actionsToString(adsInsights.getFieldVideo10SecWatchedActions());
            case "video_30_sec_watched_actions": return actionsToString(adsInsights.getFieldVideo30SecWatchedActions());
            case "video_avg_time_watched_actions": return actionsToString(adsInsights.getFieldVideoAvgTimeWatchedActions());
            case "video_complete_watched_actions": return actionsToString(adsInsights.getFieldVideoCompleteWatchedActions());
            case "video_p100_watched_actions": return actionsToString(adsInsights.getFieldVideoP100WatchedActions());
            case "video_p25_watched_actions": return actionsToString(adsInsights.getFieldVideoP25WatchedActions());
            case "video_p50_watched_actions": return actionsToString(adsInsights.getFieldVideoP50WatchedActions());
            case "video_p75_watched_actions": return actionsToString(adsInsights.getFieldVideoP75WatchedActions());
            case "video_play_actions": return actionsToString(adsInsights.getFieldVideoPlayActions());
            case "video_thruplay_watched_actions": return actionsToString(adsInsights.getFieldVideoThruplayWatchedActions());
            case "website_clicks": return adsInsights.getFieldWebsiteClicks();
            case "website_ctr": return actionsToString(adsInsights.getFieldWebsiteCtr());
            case "website_purchase_roas": return actionsToString(adsInsights.getFieldWebsitePurchaseRoas());
            // breakdowns
            case "ad_format_asset": return extractBreakdown("ad_format_asset");
            case "age": return extractBreakdown("age");
            case "body_asset": return extractBreakdown("body_asset");
            case "call_to_action_asset": return extractBreakdown("call_to_action_asset");
            case "country": return extractBreakdown("country");
            case "description_asset": return extractBreakdown("description_asset");
            case "gender": return extractBreakdown("gender");
            case "image_asset": return extractBreakdown("image_asset");
            case "impression_device": return extractBreakdown("impression_device");
            case "link_url_asset": return extractBreakdown("link_url_asset");
            case "product_id": return extractBreakdown("product_id");
            case "region": return extractBreakdown("region");
            case "title_asset": return extractBreakdown("title_asset");
            case "video_asset": return extractBreakdown("video_asset");
            case "dma": return extractBreakdown("dma");
            case "frequency_value": return extractBreakdown("frequency_value");
            case "hourly_stats_aggregated_by_advertiser_time_zone": return extractBreakdown("hourly_stats_aggregated_by_advertiser_time_zone");
            case "hourly_stats_aggregated_by_audience_time_zone": return extractBreakdown("hourly_stats_aggregated_by_audience_time_zone");
            case "place_page_id": return extractBreakdown("place_page_id");
            case "publisher_platform": return extractBreakdown("publisher_platform");
            case "platform_position": return extractBreakdown("platform_position");
            case "device_platform": return extractBreakdown("device_platform");
            default: return "";
        }
    }

    private String actionsToString(List<AdsActionStats> actions)
    {
        return Objects.isNull(actions) ? "[]" : "[" + actions.stream().map(AdsActionStats::toString).collect(Collectors.joining(",")) + "]";
    }
    private String extractBreakdown(String name)
    {
        JsonElement data = this.adsInsights.getRawResponseAsJsonObject().get(name);
        return data.isJsonNull() ? "" : data.toString();
    }
}
