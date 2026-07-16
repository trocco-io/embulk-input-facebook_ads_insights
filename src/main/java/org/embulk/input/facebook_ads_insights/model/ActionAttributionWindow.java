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
        for (AdsInsights.EnumActionAttributionWindows window : AdsInsights.EnumActionAttributionWindows.values()) {
            if (window.toString().equals(value)) {
                return new ActionAttributionWindow(window);
            }
        }
        throw new ConfigException(String.format("Unknown ActionAttributionWindow value '%s'", value));
    }

    public AdsInsights.EnumActionAttributionWindows getEnum()
    {
        return this.enumActionAttributionWindows;
    }
}
