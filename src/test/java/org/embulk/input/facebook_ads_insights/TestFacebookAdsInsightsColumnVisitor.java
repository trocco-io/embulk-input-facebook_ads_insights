package org.embulk.input.facebook_ads_insights;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.facebook.ads.sdk.AdsInsights;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.embulk.EmbulkTestRuntime;
import org.embulk.config.ConfigSource;
import org.embulk.spi.*;
import org.embulk.spi.json.*;
import org.embulk.util.config.ConfigMapper;
import org.embulk.util.config.ConfigMapperFactory;
import org.junit.Rule;
import org.junit.Test;

public class TestFacebookAdsInsightsColumnVisitor {
    protected static final ConfigMapperFactory CONFIG_MAPPER_FACTORY =
            ConfigMapperFactory.builder().addDefaultModules().build();
    @Rule
    public EmbulkTestRuntime runtime = new EmbulkTestRuntime();

    @Test
    public void stringColumn() {
        visitOnce("😄", "string", null, x -> x.setString(0, "😄"));
        visitOnce("", "string", null, x -> x.setString(0, ""));
        visitOnce(null, "string", null, x -> x.setNull(0));
    }

    @Test
    public void booleanColumn() {
        visitOnce("true", "boolean", null, x -> x.setBoolean(0, true));
        visitOnce("false", "boolean", null, x -> x.setBoolean(0, false));
        visitOnce(null, "boolean", null, x -> x.setBoolean(0, false));
    }

    @Test
    public void longColumn() {
        visitOnce("100", "long", null, x -> x.setLong(0, 100));
        visitOnce("あ", "long", null, x -> x.setNull(0));
        visitOnce(null, "long", null, x -> x.setNull(0));
    }

    @Test
    public void doubleColumn() {
        visitOnce("0.25", "double", null, x -> x.setDouble(0, 0.25));
        visitOnce("あ", "double", null, x -> x.setNull(0));
        visitOnce(null, "double", null, x -> x.setNull(0));
    }

    @Test
    public void timestampColumn() {
        visitOnce("1970-01-01T00:00:10.000+00:00", "timestamp", null, x -> x.setTimestamp(0, Instant.ofEpochSecond(10)));
        visitOnce("19700101000100", "timestamp", "%Y%m%d%H%M%S", x -> x.setTimestamp(0, Instant.ofEpochSecond(60)));
        visitOnce("00000000000000", "timestamp", "%Y%m%d%H%M%S", x -> x.setNull(0));
    }

    @Test
    public void jsonColumn() {
        visitOnce("\"\"", "json", null, x -> x.setNull(0));
        visitOnce("true", "json", null, x -> x.setNull(0));
        visitOnce("100", "json", null, x -> x.setNull(0));
        visitOnce(null, "json", null, x -> x.setNull(0));
        visitOnce("[\"s\",true,2,0.5,{\"foo\":10},[3],null]", "json", null,
                x -> x.setJson(0,
                        JsonArray.of(
                                JsonString.of("s"),
                                JsonBoolean.TRUE,
                                JsonLong.of(2),
                                JsonDouble.of(0.5),
                                JsonObject.of(JsonString.of("foo"), JsonLong.of(10)),
                                JsonArray.of(JsonLong.of(3)),
                                JsonNull.of()
                        )));
        visitOnce("{\"k0\":{\"😄\":\"😊\"},\"k1\":[3]}", "json", null,
                x -> x.setJson(0,
                        JsonObject.of(
                                JsonString.of("k0"),
                                JsonObject.of(
                                        JsonString.of("😄"),
                                        JsonString.of("😊")
                                ),
                                JsonString.of("k1"),
                                JsonArray.of(JsonLong.of(3))
                        )));
    }

    public void visitOnce(String value, String type, Object format, Consumer<PageBuilder> check) {
        PageOutputForTest pageOutput = new PageOutputForTest();
        ConfigMapper configMapper = CONFIG_MAPPER_FACTORY.createConfigMapper();
        ConfigSource configSource = CONFIG_MAPPER_FACTORY.newConfigSource();
        configSource.set("access_token", "test");
        configSource.set("object_type", "account");
        configSource.set("object_id", "0123");
        List<ColumnConfig> columnConfigs = new ArrayList<>();
        ColumnConfig columnConfig = new ColumnConfig(CONFIG_MAPPER_FACTORY.newConfigSource().set("name", "account_id").set("type", type).set("format", format));
        columnConfigs.add(columnConfig);
        configSource.set("fields", columnConfigs.toArray(new ColumnConfig[0]));
        Column column = new Column(0, columnConfig.getName(), columnConfig.getType());
        Schema schema = new Schema(Collections.singletonList(column));
        try (PageBuilder pageBuilder = spy(Exec.getPageBuilder(runtime.getBufferAllocator(), schema, pageOutput))) {
            FacebookAdsInsightsColumnVisitor columnVisitor = new FacebookAdsInsightsColumnVisitor(new AdsInsightsForTest(value), pageBuilder, configMapper.map(configSource, PluginTask.class));
            column.visit(columnVisitor);
            pageBuilder.addRecord();
            pageBuilder.finish();
            check.accept(verify(pageBuilder, times(1)));
        }
        assertEquals(1, pageOutput.pages.size());
    }

    private static class AdsInsightsForTest extends AdsInsights {
        final String val;

        AdsInsightsForTest(String val) {
            this.val = val;
        }

        @Override
        public String getFieldAccountId() {
            return val;
        }
    }

    private static class PageOutputForTest implements PageOutput {
        public List<Page> pages = new ArrayList<>();
        @Override
        public void add(Page page) {
            pages.add(page);
        }

        @Override
        public void finish() {

        }

        @Override
        public void close() {

        }
    }
}
