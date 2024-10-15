package org.embulk.input.facebook_ads_insights;

import com.facebook.ads.sdk.AdsInsights;
import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.PageBuilder;
import org.embulk.util.config.units.ColumnConfig;
import org.embulk.util.timestamp.TimestampFormatter;
import org.msgpack.value.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class FacebookAdsInsightsColumnVisitor implements ColumnVisitor
{
    private static final String DEFAULT_TIMESTAMP_PATTERN = "%Y-%m-%dT%H:%M:%S.%L%z";
    private final Logger logger =  LoggerFactory.getLogger(FacebookAdsInsightsColumnVisitor.class);

    private final AdsInsightsAccessor accessor;
    private final PageBuilder pageBuilder;
    private final PluginTask pluginTask;

    public FacebookAdsInsightsColumnVisitor(final AdsInsights insights, final PageBuilder pageBuilder, final PluginTask pluginTask)
    {
        this.accessor = new AdsInsightsAccessor(insights);
        this.pageBuilder = pageBuilder;
        this.pluginTask = pluginTask;
    }

    @Override
    public void stringColumn(Column column)
    {
        try {
            String data = accessor.get(column.getName());
            if (Objects.isNull(data)) {
                pageBuilder.setNull(column);
            }
            else {
                pageBuilder.setString(column, data);
            }
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
    @Override
    public void booleanColumn(Column column)
    {
        try {
            String data = accessor.get(column.getName());
            pageBuilder.setBoolean(column, Boolean.parseBoolean(data));
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
    @Override
    public void longColumn(Column column)
    {
        try {
            String data = accessor.get(column.getName());
            pageBuilder.setLong(column, Long.parseLong(data));
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
    @Override
    public void doubleColumn(Column column)
    {
        try {
            String data = accessor.get(column.getName());
            pageBuilder.setDouble(column, Double.parseDouble(data));
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }

    @SuppressWarnings("deprecation") // For the use of org.embulk.spi.time.Timestamp.
    @Override
    public void timestampColumn(Column column)
    {
        try {
            List<ColumnConfig> columnConfigs = pluginTask.getFields().getColumns();
            String pattern = DEFAULT_TIMESTAMP_PATTERN;
            for (ColumnConfig config : columnConfigs) {
                if (config.getName().equals(column.getName())
                        && config.getConfigSource() != null
                        && !config.getConfigSource().isEmpty()
                        && config.getConfigSource().has("format")
                        && config.getConfigSource().get(String.class, "format") != null) {
                    pattern = config.getConfigSource().get(String.class, "format");
                    break;
                }
            }
            TimestampFormatter timestampFormatter = TimestampFormatter.builder(pattern, true).setDefaultZoneFromString("UTC").build();
            org.embulk.spi.time.Timestamp result = org.embulk.spi.time.Timestamp.ofInstant(timestampFormatter.parse(column.getName()));
            pageBuilder.setTimestamp(column, result);
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }

    @SuppressWarnings("deprecation") // For the use of org.embulk.util.json.JsonParser
    @Override
    public void jsonColumn(Column column)
    {
        try {
            Value data = new org.embulk.util.json.JsonParser().parse(accessor.get(column.getName()));
            if (data.isNilValue() || (!data.isMapValue() && !data.isArrayValue())) {
                pageBuilder.setNull(column);
            }
            else {
                pageBuilder.setJson(column, new org.embulk.util.json.JsonParser().parse(data.toString()));
            }
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
}
