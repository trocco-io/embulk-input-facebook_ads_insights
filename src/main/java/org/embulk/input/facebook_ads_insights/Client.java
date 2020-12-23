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
import org.embulk.input.facebook_ads_insights.model.ActionAttributionWindow;
import org.embulk.input.facebook_ads_insights.model.ActionBreakdown;
import org.embulk.input.facebook_ads_insights.model.Breakdown;
import org.embulk.spi.ColumnConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Client
{
    private final Logger logger =  LoggerFactory.getLogger(Client.class);
    private static final int ASYNC_SLEEP_TIME = 3000;

    private final PluginTask pluginTask;

    public Client(final PluginTask pluginTask)
    {
        this.pluginTask = pluginTask;
    }

    public List<AdsInsights> getInsights(boolean isPaginationValid) throws APIException, InterruptedException
    {
        AdReportRun adReportRun;
        switch (pluginTask.getObjectType()) {
            case ACCOUNT: {
                adReportRun = getAdAccountInsights();
                break;
            }
            case CAMPAIGN: {
                adReportRun = getCampaignInsights();
                break;
            }
            case ADSET: {
                adReportRun = getAdSetInsights();
                break;
            }
            case AD: {
                adReportRun = getAdInsights();
                break;
            }
            default: throw new IllegalArgumentException();
        }
        int asyncLoopCount = 0;
        while (adReportRun.fetch().getFieldAsyncPercentCompletion() != 100) {
            Thread.sleep(ASYNC_SLEEP_TIME);
            if (adReportRun.getFieldAsyncStatus().equals("Job Skipped")) {
                throw new RuntimeException("Transfer was aborted because the AsyncStatus is \"Job Skipped\"");
            }
            if (++asyncLoopCount >= 300) {
                throw new RuntimeException("Transfer was aborted because the AsyncStatus remains \"Job Not Started\"");
            }
        }
        // extra waiting
        int retryCount = 0;
        boolean succeeded = false;
        APINodeList<AdsInsights> adsInsights = null;
        while (retryCount < pluginTask.getMaxWeightSeconds() && !succeeded) {
            try {
                Thread.sleep(1000);
                adsInsights = adReportRun.getInsights().execute();
                succeeded = true;
            }
            catch (APIException e) {
                retryCount++;
            }
        }
        if (Objects.isNull(adsInsights)) {
            throw new APIException();
        }
        return isPaginationValid ? fetchAllPage(adsInsights) : adsInsights;
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
            request.setFiltering(Util.stringifyToStringMaps(pluginTask.getFiltering().get()));
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
        if (pluginTask.getTimeRange().isPresent()) {
            request.setTimeRange(Util.stringifyToStringMap(pluginTask.getTimeRange().get()));
        }
        if (pluginTask.getTimeRanges().isPresent()) {
            request.setTimeRanges(Util.stringifyToStringMaps(pluginTask.getTimeRanges().get()));
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
            request.setFiltering(Util.stringifyToStringMaps(pluginTask.getFiltering().get()));
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
        if (pluginTask.getTimeRange().isPresent()) {
            request.setTimeRange(Util.stringifyToStringMap(pluginTask.getTimeRange().get()));
        }
        if (pluginTask.getTimeRanges().isPresent()) {
            request.setTimeRanges(Util.stringifyToStringMaps(pluginTask.getTimeRanges().get()));
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
            request.setFiltering(Util.stringifyToStringMaps(pluginTask.getFiltering().get()));
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
        if (pluginTask.getTimeRange().isPresent()) {
            request.setTimeRange(Util.stringifyToStringMap(pluginTask.getTimeRange().get()));
        }
        if (pluginTask.getTimeRanges().isPresent()) {
            request.setTimeRanges(Util.stringifyToStringMaps(pluginTask.getTimeRanges().get()));
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
            request.setFiltering(Util.stringifyToStringMaps(pluginTask.getFiltering().get()));
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
        if (pluginTask.getTimeRange().isPresent()) {
            request.setTimeRange(Util.stringifyToStringMap(pluginTask.getTimeRange().get()));
        }
        if (pluginTask.getTimeRanges().isPresent()) {
            request.setTimeRanges(Util.stringifyToStringMaps(pluginTask.getTimeRanges().get()));
        }
        if (pluginTask.getUseAccountAttributionSetting().isPresent()) {
            request.setUseAccountAttributionSetting(pluginTask.getUseAccountAttributionSetting().get());
        }
        return request.execute();
    }

    private List<AdsInsights> fetchAllPage(APINodeList<AdsInsights> adsInsights) throws APIException
    {
        List<AdsInsights> result = new ArrayList<>();
        APINodeList<AdsInsights> next = adsInsights;
        while (!next.isEmpty()) {
            result.addAll(next);
            next = next.nextPage();
        }
        return result;
    }

    private APIContext apiContext()
    {
        return new APIContext(pluginTask.getAccessToken());
    }
    private List<String> fieldNames()
    {
        return pluginTask.getFields().getColumns().stream().map(ColumnConfig::getName).filter(s -> !Breakdown.NAMES.contains(s)).collect(Collectors.toList());
    }
}
