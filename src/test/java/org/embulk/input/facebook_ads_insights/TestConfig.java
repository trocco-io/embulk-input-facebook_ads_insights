package org.embulk.input.facebook_ads_insights;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import org.embulk.config.ConfigLoader;
import org.embulk.config.ConfigSource;
import org.embulk.input.facebook_ads_insights.model.ObjectType;
import org.embulk.input.facebook_ads_insights.unit.ToStringMap;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.Types;
import org.embulk.util.config.ConfigMapper;
import org.embulk.util.config.ConfigMapperFactory;
import org.embulk.util.config.units.ColumnConfig;
import org.junit.Test;

public class TestConfig {
    protected static final ConfigMapperFactory CONFIG_MAPPER_FACTORY =
            ConfigMapperFactory.builder().addDefaultModules().build();

    protected static final ConfigMapper CONFIG_MAPPER = CONFIG_MAPPER_FACTORY.createConfigMapper();

    private static final String BASIC_RESOURCE_PATH = "/org/embulk/input/facebook_ads_insights/test_config/";

    @Test
    public void testMin() throws IOException {
        PluginTask pluginTask = getPluginTask("min.yml");
        assertEquals("min_at", pluginTask.getAccessToken());
        assertEquals(ObjectType.ACCOUNT, pluginTask.getObjectType());
        assertEquals("1", pluginTask.getObjectId());
        assertColumnEquals(pluginTask, newColumnConfig("account_id", Types.STRING));
        assertFalse(pluginTask.getActionAttributionWindows().isPresent());
        assertFalse(pluginTask.getActionReportTime().isPresent());
        assertFalse(pluginTask.getBreakdowns().isPresent());
        assertFalse(pluginTask.getDatePreset().isPresent());
        assertFalse(pluginTask.getFiltering().isPresent());
        assertFalse(pluginTask.getLevel().isPresent());
        assertFalse(pluginTask.getProductIdLimit().isPresent());
        assertFalse(pluginTask.getSort().isPresent());
        assertFalse(pluginTask.getTimeIncrement().isPresent());
        assertFalse(pluginTask.getTimeRange().isPresent());
        assertFalse(pluginTask.getTimeRanges().isPresent());
        assertFalse(pluginTask.getUseAccountAttributionSetting().isPresent());
        assertFalse(pluginTask.getUseUnifiedAttributionSetting().isPresent());
        assertEquals(pluginTask.getMaxWaitSeconds().longValue(), 60L);
        assertFalse(pluginTask.getEnableDebug());
    }

    @Test
    public void testAll() throws IOException {
        PluginTask pluginTask = getPluginTask("all.yml");
        assertEquals("all_at", pluginTask.getAccessToken());
        assertEquals(ObjectType.CAMPAIGN, pluginTask.getObjectType());
        assertEquals("123", pluginTask.getObjectId());
        assertColumnEquals(
                pluginTask,
                newColumnConfig("account_id", Types.STRING),
                newColumnConfig("date_start", Types.TIMESTAMP, "%Y-%m-%d"));
        assertEnumListEquals(pluginTask.getActionAttributionWindows(), "1d_click", "28d_view");
        assertEnumListEquals(pluginTask.getActionBreakdowns(), "action_device", "action_type");
        assertEquals(pluginTask.getActionReportTime().get().toString(), "conversion");
        assertEnumListEquals(pluginTask.getBreakdowns(), "age", "gender");
        assertEquals(pluginTask.getDatePreset().get().toString(), "today");
        assertToStringMapEquals(
                pluginTask.getFiltering(),
                newStringHashMap("field", "f0", "operator", "o0", "value", "v0"),
                newStringHashMap("field", "f1", "operator", "o1", "value", "v1"));
        assertEquals(pluginTask.getLevel().get().toString(), "campaign");
        assertEquals(pluginTask.getProductIdLimit().get().longValue(), 23L);
        assertArrayEquals(pluginTask.getSort().get().toArray(), new String[] { "s0", "s1" });
        assertEquals(pluginTask.getTimeIncrement().get(), "456");
        assertEquals(
                pluginTask.getTimeRange().get(),
                newStringHashMap("since", "2019-09-01", "until", "2019-09-07"));
        assertToStringMapEquals(
                pluginTask.getTimeRanges(),
                newStringHashMap("since", "2019-09-01", "until", "2019-09-07"),
                newStringHashMap("since", "2020-09-01", "until", "2020-09-07"));
        assertEquals(pluginTask.getUseAccountAttributionSetting().get(), true);
        assertEquals(pluginTask.getUseUnifiedAttributionSetting().get(), false);
        assertEquals(pluginTask.getMaxWaitSeconds().longValue(), 100L);
        assertEquals(pluginTask.getEnableDebug(), true);
    }

    @SafeVarargs
    private final void assertToStringMapEquals(final Optional<List<ToStringMap>> configs, final HashMap<String, String>... expecteds) {
        assert configs.isPresent();
        assertArrayEquals(expecteds, configs.get().toArray());
    }

    private <T> void assertEnumListEquals(Optional<List<T>> configs, String... expecteds) {
        assert configs.isPresent();
        assertArrayEquals(expecteds, configs.get().stream().map(Object::toString).toArray());
    }

    private void assertColumnEquals(PluginTask pluginTask, ColumnConfig... columnConfigs) {
        List<ColumnConfig> columns = pluginTask.getFields().getColumns();
        assertEquals(columns.size(), columnConfigs.length);
        for (int i = 0; i < columns.size(); i++) {
            assertEquals(columns.get(i), columnConfigs[i]);
        }
    }

    private HashMap<String, String> newStringHashMap(String... keyValues) {
        HashMap<String, String> h = new HashMap<>();
        for (int i = 0; i < keyValues.length / 2; i++) {
            h.put(keyValues[2 * i], keyValues[2 * i + 1]);
        }
        return h;
    }

    private ColumnConfig newColumnConfig(String name, Type type, String format) {
        return new ColumnConfig(name, type, CONFIG_MAPPER_FACTORY.newConfigSource().set("format", format));
    }

    private ColumnConfig newColumnConfig(String name, Type type) {
        return new ColumnConfig(name, type, CONFIG_MAPPER_FACTORY.newConfigSource());
    }

    @SuppressWarnings("deprecation") // For the use of org.embulk.config.ModelManager
    private PluginTask getPluginTask(String resourceFilename) throws IOException {
        URL url = TestConfig.class.getResource(BASIC_RESOURCE_PATH + resourceFilename);
        String pathString = Objects.requireNonNull(url).getPath();
        ConfigSource configSource = new ConfigLoader(new org.embulk.config.ModelManager()).fromYamlFile(new File(pathString));
        return CONFIG_MAPPER.map(configSource, PluginTask.class);
    }
}
