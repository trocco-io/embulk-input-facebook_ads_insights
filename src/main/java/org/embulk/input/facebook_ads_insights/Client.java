package org.embulk.input.facebook_ads_insights;

import com.facebook.ads.sdk.*;
import org.embulk.spi.ColumnConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class Client {
    private final Logger logger =  LoggerFactory.getLogger(Client.class);

    private PluginTask pluginTask;

    public Client(PluginTask pluginTask) {
        this.pluginTask = pluginTask;
    }

    public APINodeList<AdsInsights> getInsights() throws APIException {
        switch (pluginTask.getObjectType()) {
            case "account": return getAdAccountInsights();
            case "campaign": return getCampaignInsights();
            case "adset": return getAdSetInsights();
            case "ad": return getAdInsights();
            default: throw new APIException();
        }
    }

    // TODO: plz make me dry
    private APINodeList<AdsInsights> getAdAccountInsights() throws APIException {
        AdAccount.APIRequestGetInsights request = new AdAccount(pluginTask.getObjectId(), apiContext()).getInsights();
        request.setFields(fieldNames());
        if (pluginTask.getLevel().isPresent()) {
            request.setLevel(pluginTask.getLevel().get());
        }
        return request.execute();
    }

    private APINodeList<AdsInsights> getCampaignInsights() throws APIException {
        Campaign.APIRequestGetInsights request = new Campaign(pluginTask.getObjectId(), apiContext()).getInsights();
        request.setFields(fieldNames());
        if (pluginTask.getLevel().isPresent()) {
            request.setLevel(pluginTask.getLevel().get());
        }
        return request.execute();
    }

    private APINodeList<AdsInsights> getAdSetInsights() throws APIException {
        AdSet.APIRequestGetInsights request = new AdSet(pluginTask.getObjectId(), apiContext()).getInsights();
        request.setFields(fieldNames());
        if (pluginTask.getLevel().isPresent()) {
            request.setLevel(pluginTask.getLevel().get());
        }
        return request.execute();
    }

    private APINodeList<AdsInsights> getAdInsights() throws APIException {
        Ad.APIRequestGetInsights request = new Ad(pluginTask.getObjectId(), apiContext()).getInsights();
        request.setFields(fieldNames());
        if (pluginTask.getLevel().isPresent()) {
            request.setLevel(pluginTask.getLevel().get());
        }
        return request.execute();
    }

    private APIContext apiContext() {
        return new APIContext(pluginTask.getAccessToken());
    }
    private List<String> fieldNames() {
        return pluginTask.getFields().getColumns().stream().map(ColumnConfig::getName).collect(Collectors.toList());
    }
}
