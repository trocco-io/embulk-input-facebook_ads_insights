package org.embulk.input.facebook_ads_insights.model;

import com.facebook.ads.sdk.AdsInsights;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.embulk.config.ConfigException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Breakdown
{
    // Derive from the SDK enum so this list stays in sync with fromString() (and with the
    // SDK across version bumps). NAMES is also used by Client#fieldNames() to tell breakdown
    // columns apart from field columns, so the two must always cover the same set of values.
    public static final List<String> NAMES = Collections.unmodifiableList(
            Arrays.stream(AdsInsights.EnumBreakdowns.values())
                    .map(Object::toString)
                    .collect(Collectors.toList()));
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
        for (AdsInsights.EnumBreakdowns breakdown : AdsInsights.EnumBreakdowns.values()) {
            if (breakdown.toString().equals(value)) {
                return new Breakdown(breakdown);
            }
        }
        throw new ConfigException(String.format("Unknown Breakdown value '%s'", value));
    }

    public AdsInsights.EnumBreakdowns getEnum()
    {
        return this.enumBreakdowns;
    }
}
