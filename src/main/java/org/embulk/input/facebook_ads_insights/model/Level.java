package org.embulk.input.facebook_ads_insights.model;

import com.facebook.ads.sdk.AdsInsights;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.embulk.config.ConfigException;

public class Level
{
    private final AdsInsights.EnumLevel enumLevel;

    private Level(final AdsInsights.EnumLevel enumLevel)
    {
        this.enumLevel = enumLevel;
    }

    @JsonValue
    @Override
    public String toString()
    {
        return this.enumLevel.toString();
    }

    @JsonCreator
    public static Level fromString(final String value)
    {
        switch (value) {
            case "account": return new Level(AdsInsights.EnumLevel.VALUE_ACCOUNT);
            case "campaign": return new Level(AdsInsights.EnumLevel.VALUE_CAMPAIGN);
            case "adset": return new Level(AdsInsights.EnumLevel.VALUE_ADSET);
            case "ad": return new Level(AdsInsights.EnumLevel.VALUE_AD);
            default: throw new ConfigException(String.format("Unknown Level value '%s'", value));
        }
    }

    public AdsInsights.EnumLevel getEnum()
    {
        return this.enumLevel;
    }
}
