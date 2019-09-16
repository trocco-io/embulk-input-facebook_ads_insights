package org.embulk.input.facebook_ads_insights;

import org.embulk.config.Config;
import org.embulk.config.ConfigDefault;
import org.embulk.config.Task;
import org.embulk.spi.SchemaConfig;

import java.util.Optional;

public interface PluginTask extends Task {
    @Config("object_id")
    public String getObjectId();

    @Config("access_token")
    public String getAccessToken();

    @Config("object_type")
    public String getObjectType();

    @Config("level")
    @ConfigDefault("null")
    public Optional<String> getLevel();

    @Config("fields")
    public SchemaConfig getFields();
}
