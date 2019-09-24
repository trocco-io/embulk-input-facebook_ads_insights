package org.embulk.input.facebook_ads_insights;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.APINodeList;
import com.facebook.ads.sdk.Ad;
import com.facebook.ads.sdk.AdAccount;
import com.facebook.ads.sdk.AdReportRun;
import com.facebook.ads.sdk.AdSet;
import com.facebook.ads.sdk.AdsInsights;
import com.facebook.ads.sdk.Campaign;
import com.google.gson.Gson;
import org.embulk.input.facebook_ads_insights.model.ActionAttributionWindow;
import org.embulk.input.facebook_ads_insights.model.ActionBreakdown;
import org.embulk.input.facebook_ads_insights.model.Breakdown;
import org.embulk.spi.ColumnConfig;
import org.embulk.spi.unit.ToStringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class Client
{
    private final Logger logger =  LoggerFactory.getLogger(Client.class);

    private final PluginTask pluginTask;

    public Client(final PluginTask pluginTask)
    {
        this.pluginTask = pluginTask;
    }

    public APINodeList<AdsInsights> getInsights() throws APIException, InterruptedException
    {
        AdReportRun adReportRun;
        switch (pluginTask.getObjectType()) {
            case ACCOUNT: adReportRun = getAdAccountInsights(); break;
            case CAMPAIGN: adReportRun = getCampaignInsights(); break;
            case ADSET: adReportRun = getAdSetInsights(); break;
            case AD: adReportRun = getAdInsights(); break;
            default: throw new APIException();
        }
        while (adReportRun.fetch().getFieldAsyncPercentCompletion() != 100) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.error("Thread interrupted from sleep while waiting for async insights");
                throw e;
            }
        }
        return adReportRun.getInsights().execute();
    }

    // TODO: plz make me dry
    private AdReportRun getAdAccountInsights() throws APIException
    {
        AdAccount.APIRequestGetInsightsAsync request = new AdAccount(pluginTask.getObjectId(), apiContext()).getInsightsAsync();
        request.setFields(fieldNames());
        if (pluginTask.getActionAttributionWindows().isPresent()) {
            request.setActionAttributionWindows(pluginTask.getActionAttributionWindows().get().stream().map(ActionAttributionWindow::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getActionBreakdowns().isPresent()) {
            request.setActionBreakdowns(pluginTask.getActionBreakdowns().get().stream().map(ActionBreakdown::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getActionReportTime().isPresent()) {
            request.setActionReportTime(pluginTask.getActionReportTime().get().getEnum());
        }
        if (pluginTask.getBreakdowns().isPresent()) {
            request.setBreakdowns(pluginTask.getBreakdowns().get().stream().map(Breakdown::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getDatePreset().isPresent()) {
            request.setDatePreset(pluginTask.getDatePreset().get().getEnum());
        }
        if (pluginTask.getFiltering().isPresent()) {
            request.setTimeRanges(stringifyToStringMaps(pluginTask.getFiltering().get()));
        }
        if (pluginTask.getLevel().isPresent()) {
            request.setLevel(pluginTask.getLevel().get().getEnum());
        }
        if (pluginTask.getProductIdLimit().isPresent()) {
            request.setProductIdLimit(pluginTask.getProductIdLimit().get());
        }
        if (pluginTask.getSort().isPresent()) {
            request.setSort(pluginTask.getSort().get());
        }
        if (pluginTask.getTimeIncrement().isPresent()) {
            request.setTimeIncrement(pluginTask.getTimeIncrement().get());
        }
        if (pluginTask.getTimeRanges().isPresent()) {
            request.setTimeRanges(stringifyToStringMaps(pluginTask.getTimeRanges().get()));
        }
        if (pluginTask.getUseAccountAttributionSetting().isPresent()) {
            request.setUseAccountAttributionSetting(pluginTask.getUseAccountAttributionSetting().get());
        }
        return request.execute();
    }

    private AdReportRun getCampaignInsights() throws APIException
    {
        Campaign.APIRequestGetInsightsAsync request = new Campaign(pluginTask.getObjectId(), apiContext()).getInsightsAsync();
        request.setFields(fieldNames());
        if (pluginTask.getActionAttributionWindows().isPresent()) {
            request.setActionAttributionWindows(pluginTask.getActionAttributionWindows().get().stream().map(ActionAttributionWindow::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getActionBreakdowns().isPresent()) {
            request.setActionBreakdowns(pluginTask.getActionBreakdowns().get().stream().map(ActionBreakdown::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getActionReportTime().isPresent()) {
            request.setActionReportTime(pluginTask.getActionReportTime().get().getEnum());
        }
        if (pluginTask.getBreakdowns().isPresent()) {
            request.setBreakdowns(pluginTask.getBreakdowns().get().stream().map(Breakdown::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getDatePreset().isPresent()) {
            request.setDatePreset(pluginTask.getDatePreset().get().getEnum());
        }
        if (pluginTask.getFiltering().isPresent()) {
            request.setTimeRanges(stringifyToStringMaps(pluginTask.getFiltering().get()));
        }
        if (pluginTask.getLevel().isPresent()) {
            request.setLevel(pluginTask.getLevel().get().getEnum());
        }
        if (pluginTask.getProductIdLimit().isPresent()) {
            request.setProductIdLimit(pluginTask.getProductIdLimit().get());
        }
        if (pluginTask.getSort().isPresent()) {
            request.setSort(pluginTask.getSort().get());
        }
        if (pluginTask.getTimeIncrement().isPresent()) {
            request.setTimeIncrement(pluginTask.getTimeIncrement().get());
        }
        if (pluginTask.getTimeRanges().isPresent()) {
            request.setTimeRanges(stringifyToStringMaps(pluginTask.getTimeRanges().get()));
        }
        if (pluginTask.getUseAccountAttributionSetting().isPresent()) {
            request.setUseAccountAttributionSetting(pluginTask.getUseAccountAttributionSetting().get());
        }
        return request.execute();
    }

    private AdReportRun getAdSetInsights() throws APIException
    {
        AdSet.APIRequestGetInsightsAsync request = new AdSet(pluginTask.getObjectId(), apiContext()).getInsightsAsync();
        request.setFields(fieldNames());
        if (pluginTask.getActionAttributionWindows().isPresent()) {
            request.setActionAttributionWindows(pluginTask.getActionAttributionWindows().get().stream().map(ActionAttributionWindow::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getActionBreakdowns().isPresent()) {
            request.setActionBreakdowns(pluginTask.getActionBreakdowns().get().stream().map(ActionBreakdown::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getActionReportTime().isPresent()) {
            request.setActionReportTime(pluginTask.getActionReportTime().get().getEnum());
        }
        if (pluginTask.getBreakdowns().isPresent()) {
            request.setBreakdowns(pluginTask.getBreakdowns().get().stream().map(Breakdown::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getDatePreset().isPresent()) {
            request.setDatePreset(pluginTask.getDatePreset().get().getEnum());
        }
        if (pluginTask.getFiltering().isPresent()) {
            request.setTimeRanges(stringifyToStringMaps(pluginTask.getFiltering().get()));
        }
        if (pluginTask.getLevel().isPresent()) {
            request.setLevel(pluginTask.getLevel().get().getEnum());
        }
        if (pluginTask.getProductIdLimit().isPresent()) {
            request.setProductIdLimit(pluginTask.getProductIdLimit().get());
        }
        if (pluginTask.getSort().isPresent()) {
            request.setSort(pluginTask.getSort().get());
        }
        if (pluginTask.getTimeIncrement().isPresent()) {
            request.setTimeIncrement(pluginTask.getTimeIncrement().get());
        }
        if (pluginTask.getTimeRanges().isPresent()) {
            request.setTimeRanges(stringifyToStringMaps(pluginTask.getTimeRanges().get()));
        }
        if (pluginTask.getUseAccountAttributionSetting().isPresent()) {
            request.setUseAccountAttributionSetting(pluginTask.getUseAccountAttributionSetting().get());
        }
        return request.execute();
    }

    private AdReportRun getAdInsights() throws APIException
    {
        Ad.APIRequestGetInsightsAsync request = new Ad(pluginTask.getObjectId(), apiContext()).getInsightsAsync();
        request.setFields(fieldNames());
        if (pluginTask.getActionAttributionWindows().isPresent()) {
            request.setActionAttributionWindows(pluginTask.getActionAttributionWindows().get().stream().map(ActionAttributionWindow::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getActionBreakdowns().isPresent()) {
            request.setActionBreakdowns(pluginTask.getActionBreakdowns().get().stream().map(ActionBreakdown::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getActionReportTime().isPresent()) {
            request.setActionReportTime(pluginTask.getActionReportTime().get().getEnum());
        }
        if (pluginTask.getBreakdowns().isPresent()) {
            request.setBreakdowns(pluginTask.getBreakdowns().get().stream().map(Breakdown::getEnum).collect(Collectors.toList()));
        }
        if (pluginTask.getDatePreset().isPresent()) {
            request.setDatePreset(pluginTask.getDatePreset().get().getEnum());
        }
        if (pluginTask.getFiltering().isPresent()) {
            request.setTimeRanges(stringifyToStringMaps(pluginTask.getFiltering().get()));
        }
        if (pluginTask.getLevel().isPresent()) {
            request.setLevel(pluginTask.getLevel().get().getEnum());
        }
        if (pluginTask.getProductIdLimit().isPresent()) {
            request.setProductIdLimit(pluginTask.getProductIdLimit().get());
        }
        if (pluginTask.getSort().isPresent()) {
            request.setSort(pluginTask.getSort().get());
        }
        if (pluginTask.getTimeIncrement().isPresent()) {
            request.setTimeIncrement(pluginTask.getTimeIncrement().get());
        }
        if (pluginTask.getTimeRanges().isPresent()) {
            request.setTimeRanges(stringifyToStringMaps(pluginTask.getTimeRanges().get()));
        }
        if (pluginTask.getUseAccountAttributionSetting().isPresent()) {
            request.setUseAccountAttributionSetting(pluginTask.getUseAccountAttributionSetting().get());
        }
        return request.execute();
    }

    private APIContext apiContext()
    {
        return new APIContext(pluginTask.getAccessToken());
    }
    private List<String> fieldNames()
    {
        return pluginTask.getFields().getColumns().stream().map(ColumnConfig::getName).filter(s -> !Breakdown.NAMES.contains(s)).collect(Collectors.toList());
    }
    private String stringifyToStringMaps(List<ToStringMap> value)
    {
        Gson gson = new Gson();
        return "[" + value.stream().map(gson::toJson).collect(Collectors.joining(",")) + "]";
    }
}
