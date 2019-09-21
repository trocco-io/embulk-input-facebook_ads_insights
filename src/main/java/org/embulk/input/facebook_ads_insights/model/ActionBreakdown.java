package org.embulk.input.facebook_ads_insights.model;

import com.facebook.ads.sdk.AdsInsights;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.embulk.config.ConfigException;

public class ActionBreakdown
{
    private final AdsInsights.EnumActionBreakdowns enumActionBreakdowns;

    private ActionBreakdown(final AdsInsights.EnumActionBreakdowns enumActionBreakdowns)
    {
        this.enumActionBreakdowns = enumActionBreakdowns;
    }

    @JsonValue
    @Override
    public String toString()
    {
        return this.enumActionBreakdowns.toString();
    }

    @JsonCreator
    public static ActionBreakdown fromString(final String value)
    {
        switch (value) {
            case "action_converted_product_id": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_CONVERTED_PRODUCT_ID);
            case "action_device": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_DEVICE);
            case "action_canvas_component_name": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_CANVAS_COMPONENT_NAME);
            case "action_carousel_card_id": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_CAROUSEL_CARD_ID);
            case "action_carousel_card_name": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_CAROUSEL_CARD_NAME);
            case "action_destination": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_DESTINATION);
            case "action_reaction": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_REACTION);
            case "action_target_id": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_TARGET_ID);
            case "action_type": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_TYPE);
            case "action_video_sound": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_VIDEO_SOUND);
            case "action_video_type": return new ActionBreakdown(AdsInsights.EnumActionBreakdowns.VALUE_ACTION_VIDEO_TYPE);
            default: throw new ConfigException(String.format("Unknown ActionBreakdown value '%s'", value));
        }
    }

    public AdsInsights.EnumActionBreakdowns getEnum()
    {
        return this.enumActionBreakdowns;
    }
}
