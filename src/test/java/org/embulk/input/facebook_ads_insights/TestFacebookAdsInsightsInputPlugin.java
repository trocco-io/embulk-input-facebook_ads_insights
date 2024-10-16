package org.embulk.input.facebook_ads_insights;

import org.embulk.EmbulkSystemProperties;
import org.embulk.config.ConfigSource;
import org.embulk.formatter.csv.CsvFormatterPlugin;
import org.embulk.output.file.LocalFileOutputPlugin;
import org.embulk.spi.*;
import org.embulk.test.TestingEmbulk;
import org.embulk.util.config.ConfigMapper;
import org.embulk.util.config.ConfigMapperFactory;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

public class TestFacebookAdsInsightsInputPlugin
{
    protected static final EmbulkSystemProperties EMBULK_SYSTEM_PROPERTIES =
            EmbulkSystemProperties.of(new Properties());

    protected static final ConfigMapperFactory CONFIG_MAPPER_FACTORY =
            ConfigMapperFactory.builder().addDefaultModules().build();

    @Rule public final TemporaryFolder testFolder = new TemporaryFolder();

    @Rule
    public final TestingEmbulk embulk =
            TestingEmbulk.builder()
                    .setEmbulkSystemProperties(EMBULK_SYSTEM_PROPERTIES)
                    .registerPlugin(InputPlugin.class, "facebook_ads_insights", FacebookAdsInsightsInputPlugin.class)
                    .registerPlugin(FileOutputPlugin.class, "file",LocalFileOutputPlugin .class)
                    .registerPlugin(FormatterPlugin.class, "csv", CsvFormatterPlugin.class)
                    .build();

    @Test public void runEmptyFields() throws IOException {
        File outputFile = testFolder.newFile("output_test.csv");
        embulk.runInput(newConfigSource(), outputFile.toPath());
        Assert.assertEquals("", readAllText(outputFile));
    }

    private ConfigSource newConfigSource(ColumnConfig... columnConfigs) {
        return CONFIG_MAPPER_FACTORY
                .newConfigSource()
                .set("type", "facebook_ads_insights")
                .set("access_token", "EAACmJQyzJZBYBOx24y0LLZC8MSmWQfByhUyczNO1aHNb6v34ncNZAiSiSio0YJsluaQJPsfRgCduipMfMWMBHbSc0wJ1ZCQgcwJ8QBqjLvSj38UxtYrRhl5jNGNXmVKepT4BqGGWZBn9LSiMZAJ7Ss99ys1ttqVTCtT7RU3xkWmiZCfxyrUvE502HhqkIPnGhegHvTh9LTi7OKSZCEPsn6rJyk5KQpYW9N7nEmTbfsEZD")
                .set("object_type", "account")
                .set("object_id", "324034549246466")
                .set("fields", columnConfigs);
    }

    protected String readAllText(File outputFile) throws IOException {
        return new String(Files.readAllBytes(outputFile.toPath()));
    }

}
