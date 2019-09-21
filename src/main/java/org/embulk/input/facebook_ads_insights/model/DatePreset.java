package org.embulk.input.facebook_ads_insights.model;

import com.facebook.ads.sdk.AdsInsights;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.embulk.config.ConfigException;

public class DatePreset
{
    private final AdsInsights.EnumDatePreset enumDatePreset;

    private DatePreset(final AdsInsights.EnumDatePreset enumDatePreset)
    {
        this.enumDatePreset = enumDatePreset;
    }

    @JsonValue
    @Override
    public String toString()
    {
        return this.enumDatePreset.toString();
    }

    @JsonCreator
    public static DatePreset fromString(final String value)
    {
        switch (value) {
            case "today": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_TODAY);
            case "yesterday": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_YESTERDAY);
            case "this_month": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_THIS_MONTH);
            case "last_month": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_MONTH);
            case "this_quarter": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_THIS_QUARTER);
            case "lifetime": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LIFETIME);
            case "last_3d": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_3D);
            case "last_7d": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_7D);
            case "last_14d": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_14D);
            case "last_28d": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_28D);
            case "last_30d": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_30D);
            case "last_90d": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_90D);
            case "last_week_mon_sun": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_WEEK_MON_SUN);
            case "last_week_sun_sat": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_WEEK_SUN_SAT);
            case "last_quarter": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_QUARTER);
            case "last_year": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_LAST_YEAR);
            case "this_week_mon_today": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_THIS_WEEK_MON_TODAY);
            case "this_week_sun_today": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_THIS_WEEK_SUN_TODAY);
            case "this_year": return new DatePreset(AdsInsights.EnumDatePreset.VALUE_THIS_YEAR);
            default: throw new ConfigException(String.format("Unknown DatePreset value '%s'", value));
        }
    }

    public AdsInsights.EnumDatePreset getEnum()
    {
        return this.enumDatePreset;
    }
}
