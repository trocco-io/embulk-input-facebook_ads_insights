package org.embulk.input.facebook_ads_insights.model;

import com.facebook.ads.sdk.AdsInsights;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.embulk.config.ConfigException;

public class ActionAttributionWindow
{
    private final AdsInsights.EnumActionAttributionWindows enumActionAttributionWindows;

    private ActionAttributionWindow(final AdsInsights.EnumActionAttributionWindows enumActionAttributionWindows)
    {
        this.enumActionAttributionWindows = enumActionAttributionWindows;
    }

    @JsonValue
    @Override
    public String toString()
    {
        return this.enumActionAttributionWindows.toString();
    }

    @JsonCreator
    public static ActionAttributionWindow fromString(final String value)
    {
        switch (value) {
            case "1d_click": return new ActionAttributionWindow(AdsInsights.EnumActionAttributionWindows.VALUE_1D_CLICK);
            case "1d_view": return new ActionAttributionWindow(AdsInsights.EnumActionAttributionWindows.VALUE_1D_VIEW);
            case "7d_click": return new ActionAttributionWindow(AdsInsights.EnumActionAttributionWindows.VALUE_7D_CLICK);
            case "7d_view": return new ActionAttributionWindow(AdsInsights.EnumActionAttributionWindows.VALUE_7D_VIEW);
            case "28d_click": return new ActionAttributionWindow(AdsInsights.EnumActionAttributionWindows.VALUE_28D_CLICK);
            case "28d_view": return new ActionAttributionWindow(AdsInsights.EnumActionAttributionWindows.VALUE_28D_VIEW);
            case "default": return new ActionAttributionWindow(AdsInsights.EnumActionAttributionWindows.VALUE_DEFAULT);
            default: throw new ConfigException(String.format("Unknown ActionAttributionWindow value '%s'", value));
        }
    }

    public AdsInsights.EnumActionAttributionWindows getEnum()
    {
        return this.enumActionAttributionWindows;
    }
}
