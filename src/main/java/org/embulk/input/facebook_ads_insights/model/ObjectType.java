package org.embulk.input.facebook_ads_insights.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.embulk.config.ConfigException;

public enum ObjectType {
    ACCOUNT,
    CAMPAIGN,
    ADSET,
    AD;
    @JsonValue
    @Override
    public String toString()
    {
        return this.name().toLowerCase();
    }

    @JsonCreator
    public static ObjectType fromString(final String value)
    {
        switch (value) {
            case "account": return ACCOUNT;
            case "campaign": return CAMPAIGN;
            case "adset": return ADSET;
            case "ad": return AD;
            default: throw new ConfigException(String.format("Unknown ObjectType value '%s'", value));
        }
    }
}
