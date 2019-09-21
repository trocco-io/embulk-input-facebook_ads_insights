package org.embulk.input.facebook_ads_insights;

import com.facebook.ads.sdk.AdsInsights;
import com.google.gson.JsonElement;
import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.PageBuilder;
import org.embulk.spi.json.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class FacebookAdsInsightsColumnVisitor implements ColumnVisitor
{
    private final Logger logger =  LoggerFactory.getLogger(FacebookAdsInsightsColumnVisitor.class);

    private final AdsInsightsAccessor accessor;
    private final PageBuilder pageBuilder;

    public FacebookAdsInsightsColumnVisitor(final AdsInsights insights, final PageBuilder pageBuilder)
    {
        this.accessor = new AdsInsightsAccessor(insights);
        this.pageBuilder = pageBuilder;
    }

    @Override
    public void stringColumn(Column column)
    {
        String data = accessor.get(column.getName());
        if (Objects.isNull(data)) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setString(column, data);
        }
    }
    @Override
    public void booleanColumn(Column column)
    {
        String data = accessor.get(column.getName());
        try {
            pageBuilder.setBoolean(column, Boolean.parseBoolean(data));
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
    @Override
    public void longColumn(Column column)
    {
        String data = accessor.get(column.getName());
        try {
            pageBuilder.setLong(column, Long.parseLong(data));
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
    @Override
    public void doubleColumn(Column column)
    {
        String data = accessor.get(column.getName());
        try {
            pageBuilder.setDouble(column, Double.parseDouble(data));
        }
        catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
    @Override
    public void timestampColumn(Column column)
    {
        // TODO: plz impl me
    }
    @Override
    public void jsonColumn(Column column)
    {
        JsonElement data = new com.google.gson.JsonParser().parse(column.getName());
        if (data.isJsonNull() || data.isJsonPrimitive()) {
            pageBuilder.setNull(column);
        }
        else {
            pageBuilder.setJson(column, new JsonParser().parse(data.toString()));
        }
    }
}
