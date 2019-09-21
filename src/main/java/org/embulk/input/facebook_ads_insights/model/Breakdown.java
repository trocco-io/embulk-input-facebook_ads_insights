package org.embulk.input.facebook_ads_insights.model;

import com.facebook.ads.sdk.AdsInsights;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.embulk.config.ConfigException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Breakdown
{
    public static final List<String> NAMES = new ArrayList<>(Arrays.asList(
            "ad_format_asset", "age", "body_asset", "call_to_action_asset", "country",
            "description_asset", "gender", "image_asset", "impression_device", "link_url_asset",
            "product_id", "region", "title_asset", "video_asset", "dma", "frequency_value",
            "hourly_stats_aggregated_by_advertiser_time_zone", "hourly_stats_aggregated_by_audience_time_zone",
            "place_page_id", "publisher_platform", "platform_position", "device_platform"
    ));
    private final AdsInsights.EnumBreakdowns enumBreakdowns;

    private Breakdown(final AdsInsights.EnumBreakdowns enumBreakdowns)
    {
        this.enumBreakdowns = enumBreakdowns;
    }

    @JsonValue
    @Override
    public String toString()
    {
        return this.enumBreakdowns.toString();
    }

    @JsonCreator
    public static Breakdown fromString(final String value)
    {
        switch (value) {
            case "ad_format_asset": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_AD_FORMAT_ASSET);
            case "age": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_AGE);
            case "body_asset": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_BODY_ASSET);
            case "call_to_action_asset": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_CALL_TO_ACTION_ASSET);
            case "country": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_COUNTRY);
            case "description_asset": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_DESCRIPTION_ASSET);
            case "gender": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_GENDER);
            case "image_asset": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_IMAGE_ASSET);
            case "impression_device": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_IMPRESSION_DEVICE);
            case "link_url_asset": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_LINK_URL_ASSET);
            case "product_id": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_PRODUCT_ID);
            case "region": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_REGION);
            case "title_asset": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_TITLE_ASSET);
            case "video_asset": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_VIDEO_ASSET);
            case "dma": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_DMA);
            case "frequency_value": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_FREQUENCY_VALUE);
            case "hourly_stats_aggregated_by_advertiser_time_zone": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_HOURLY_STATS_AGGREGATED_BY_ADVERTISER_TIME_ZONE);
            case "hourly_stats_aggregated_by_audience_time_zone": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_HOURLY_STATS_AGGREGATED_BY_AUDIENCE_TIME_ZONE);
            case "place_page_id": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_PLACE_PAGE_ID);
            case "publisher_platform": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_PUBLISHER_PLATFORM);
            case "platform_position": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_PLATFORM_POSITION);
            case "device_platform": return new Breakdown(AdsInsights.EnumBreakdowns.VALUE_DEVICE_PLATFORM);
            default: throw new ConfigException(String.format("Unknown Breakdown value '%s'", value));
        }
    }

    public AdsInsights.EnumBreakdowns getEnum()
    {
        return this.enumBreakdowns;
    }
}
