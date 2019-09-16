package org.embulk.input.facebook_ads_insights;

import com.facebook.ads.sdk.AdsInsights;
import org.embulk.spi.Column;
import org.embulk.spi.ColumnVisitor;
import org.embulk.spi.PageBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacebookAdsInsightsColumnVisitor implements ColumnVisitor {
    private final Logger logger =  LoggerFactory.getLogger(FacebookAdsInsightsColumnVisitor.class);

    private AdsInsightsAccessor accessor;
    private PageBuilder pageBuilder;

    public FacebookAdsInsightsColumnVisitor(AdsInsights insights, PageBuilder pageBuilder) {
        this.accessor = new AdsInsightsAccessor(insights);
        this.pageBuilder = pageBuilder;
    }

    @Override
    public void jsonColumn(Column column) {}
    @Override
    public void stringColumn(Column column) {
        pageBuilder.setString(column, accessor.get(column.getName()));
    }
    @Override
    public void timestampColumn(Column column) {}
    @Override
    public void booleanColumn(Column column) {}
    @Override
    public void longColumn(Column column) {
        String data = accessor.get(column.getName());
        try {
            pageBuilder.setLong(column, Long.parseLong(data));
        } catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
    @Override
    public void doubleColumn(Column column) {
        String data = accessor.get(column.getName());
        try {
            pageBuilder.setDouble(column, Double.parseDouble(data));
        } catch (Exception e) {
            pageBuilder.setNull(column);
        }
    }
}
