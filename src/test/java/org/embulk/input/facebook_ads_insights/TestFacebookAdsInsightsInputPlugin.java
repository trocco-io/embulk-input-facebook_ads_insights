package org.embulk.input.facebook_ads_insights;

import org.embulk.EmbulkSystemProperties;
import org.embulk.config.ConfigSource;
import org.embulk.formatter.csv.CsvFormatterPlugin;
import org.embulk.output.file.LocalFileOutputPlugin;
import org.embulk.spi.*;
import org.embulk.spi.type.Type;
import org.embulk.spi.type.Types;
import org.embulk.test.EmbulkTests;
import org.embulk.test.TestingEmbulk;
import org.embulk.util.config.Config;
import org.embulk.util.config.ConfigMapperFactory;
import org.embulk.util.config.Task;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Properties;

public class TestFacebookAdsInsightsInputPlugin
{
    private static final String configEnvName = "EMBULK_INPUT_FACEBOOK_ADS_INSIGTHS_TEST_CONFIG";

    protected static final EmbulkSystemProperties EMBULK_SYSTEM_PROPERTIES =
            EmbulkSystemProperties.of(new Properties());

    protected static final ConfigMapperFactory CONFIG_MAPPER_FACTORY =
            ConfigMapperFactory.builder().addDefaultModules().build();

    public interface TestTask extends Task {
        @Config("access_token")
        public String getAccessToken();

        @Config("object_id_of_object_type_account")
        public String getObjectIdOfObjectTypeAccount();
    }

    @Rule public final TemporaryFolder testFolder = new TemporaryFolder();

    @Rule
    public final TestingEmbulk embulk =
            TestingEmbulk.builder()
                    .setEmbulkSystemProperties(EMBULK_SYSTEM_PROPERTIES)
                    .registerPlugin(InputPlugin.class, "facebook_ads_insights", FacebookAdsInsightsInputPlugin.class)
                    .registerPlugin(FileOutputPlugin.class, "file",LocalFileOutputPlugin .class)
                    .registerPlugin(FormatterPlugin.class, "csv", CsvFormatterPlugin.class)
                    .build();

    @Test public void testRun() throws IOException {
        File outputFile = testFolder.newFile("output_test.csv");
        embulk.runInput(newConfigSource(newColumn("account_id", Types.STRING)), outputFile.toPath());
        String[] lines = Arrays.stream(readAllText(outputFile).split("\n")).filter(x -> !x.isEmpty()).toArray(String[]::new);
        Assume.assumeTrue("not empty result", lines.length > 0);
        Assert.assertEquals(getTestTask().getObjectIdOfObjectTypeAccount(), lines[0]);
    }

    private ColumnConfig newColumn(String name, Type type) {
        return new ColumnConfig(name, type, (ConfigSource) null);
    }

    private TestTask getTestTask() {
        return CONFIG_MAPPER_FACTORY.createConfigMapper().map(EmbulkTests.config(configEnvName), TestTask.class);
    }

    private ConfigSource newConfigSource(ColumnConfig... columnConfigs) {
        TestTask testTask = getTestTask();
        ConfigSource columns = CONFIG_MAPPER_FACTORY.newConfigSource();
        HashMap<String, String> test = new HashMap<>();
        test.put("name", "account_id");
        test.put("type", "string");
        return CONFIG_MAPPER_FACTORY
                .newConfigSource()
                .set("type", "facebook_ads_insights")
                .set("access_token", testTask.getAccessToken())
                .set("object_type", "account")
                .set("object_id", testTask.getObjectIdOfObjectTypeAccount())
                .set("fields", new HashMap[] { test });
    }

    protected String readAllText(File outputFile) throws IOException {
        return new String(Files.readAllBytes(outputFile.toPath()));
    }

}
