package org.embulk.input.facebook_ads_insights.model;

import com.facebook.ads.sdk.AdsInsights;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.embulk.config.ConfigException;

public class ActionReportTime
{
    private final AdsInsights.EnumActionReportTime enumActionReportTime;

    private ActionReportTime(final AdsInsights.EnumActionReportTime enumActionReportTime)
    {
        this.enumActionReportTime = enumActionReportTime;
    }

    @JsonValue
    @Override
    public String toString()
    {
        return this.enumActionReportTime.toString();
    }

    @JsonCreator
    public static ActionReportTime fromString(final String value)
    {
        switch (value) {
            case "conversion": return new ActionReportTime(AdsInsights.EnumActionReportTime.VALUE_CONVERSION);
            case "impression": return new ActionReportTime(AdsInsights.EnumActionReportTime.VALUE_IMPRESSION);
            default: throw new ConfigException(String.format("Unknown ActionReportTime value '%s'", value));
        }
    }

    public AdsInsights.EnumActionReportTime getEnum()
    {
        return this.enumActionReportTime;
    }
}
