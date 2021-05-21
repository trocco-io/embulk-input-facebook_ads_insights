package org.embulk.input.facebook_ads_insights;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.Task;
import org.embulk.input.facebook_ads_insights.model.ActionAttributionWindow;
import org.embulk.input.facebook_ads_insights.model.ActionBreakdown;
import org.embulk.input.facebook_ads_insights.model.ActionReportTime;
import org.embulk.input.facebook_ads_insights.model.Breakdown;
import org.embulk.input.facebook_ads_insights.model.DatePreset;
import org.embulk.input.facebook_ads_insights.model.Level;
import org.embulk.input.facebook_ads_insights.model.ObjectType;
import org.embulk.spi.SchemaConfig;
import org.embulk.spi.unit.ToStringMap;

import java.util.List;
import java.util.Optional;

public interface PluginTask extends Task
{
    @Config("access_token")
    public String getAccessToken();

    @Config("object_type")
    public ObjectType getObjectType();

    @Config("object_id")
    public String getObjectId();

    @Config("fields")
    public SchemaConfig getFields();

    @Config("action_attribution_windows")
    @ConfigDefault("null")
    public Optional<List<ActionAttributionWindow>> getActionAttributionWindows();

    @Config("action_breakdowns")
    @ConfigDefault("null")
    public Optional<List<ActionBreakdown>> getActionBreakdowns();

    @Config("action_report_time")
    @ConfigDefault("null")
    public Optional<ActionReportTime> getActionReportTime();

    @Config("breakdowns")
    @ConfigDefault("null")
    public Optional<List<Breakdown>> getBreakdowns();

    @Config("date_preset")
    @ConfigDefault("null")
    public Optional<DatePreset> getDatePreset();

    @Config("filtering")
    @ConfigDefault("null")
    public Optional<List<ToStringMap>> getFiltering();

    @Config("level")
    @ConfigDefault("null")
    public Optional<Level> getLevel();

    @Config("product_id_limit")
    @ConfigDefault("null")
    public Optional<Long> getProductIdLimit();

    @Config("sort")
    @ConfigDefault("null")
    public Optional<List<String>> getSort();

    @Config("time_increment")
    @ConfigDefault("null")
    public Optional<String> getTimeIncrement();

    @Config("time_range")
    @ConfigDefault("null")
    public Optional<ToStringMap> getTimeRange();

    @Config("time_ranges")
    @ConfigDefault("null")
    public Optional<List<ToStringMap>> getTimeRanges();

    @Config("use_account_attribution_setting")
    @ConfigDefault("null")
    public Optional<Boolean> getUseAccountAttributionSetting();

    @Config("max_wait_seconds")
    @ConfigDefault("60")
    public Integer getMaxWaitSeconds();
}
