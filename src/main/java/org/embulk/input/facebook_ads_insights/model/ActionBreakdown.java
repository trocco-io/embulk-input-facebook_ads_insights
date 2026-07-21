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
        for (AdsInsights.EnumActionBreakdowns breakdown : AdsInsights.EnumActionBreakdowns.values()) {
            if (breakdown.toString().equals(value)) {
                return new ActionBreakdown(breakdown);
            }
        }
        throw new ConfigException(String.format("Unknown ActionBreakdown value '%s'", value));
    }

    public AdsInsights.EnumActionBreakdowns getEnum()
    {
        return this.enumActionBreakdowns;
    }
}
