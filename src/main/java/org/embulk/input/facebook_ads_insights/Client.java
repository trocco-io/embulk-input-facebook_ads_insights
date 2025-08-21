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
import org.embulk.util.config.units.ColumnConfig;
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
    private static final int ASYNC_SLEEP_TIME_LIMIT = 900_000;

    private final PluginTask pluginTask;

    public Client(final PluginTask pluginTask)
    {
        this.pluginTask = pluginTask;
    }

    public List<AdsInsights> getInsights(boolean isPaginationValid) throws APIException, InterruptedException
    {
        int elapsedTime = 0;
        boolean asyncCompleted = false;
        AdReportRun adReportRun = null;
        while (!asyncCompleted) {
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

            String jobStatus = "";
            try {
                while (fetchAdReportRunSafely(adReportRun.getId()).getFieldAsyncPercentCompletion() != 100) {
                    AdReportRun currentReport = fetchAdReportRunSafely(adReportRun.getId());
                    Thread.sleep(ASYNC_SLEEP_TIME);
                    elapsedTime += ASYNC_SLEEP_TIME;
                    if (currentReport.getFieldAsyncStatus().equals("Job Skipped")) {
                        jobStatus = "skipped";
                        throw new RuntimeException("async was aborted because the AsyncStatus is \"Job Skipped\"");
                    }
                    if (currentReport.getFieldAsyncStatus().equals("Job Failed")) {
                        jobStatus = "failed";
                        throw new RuntimeException("async was aborted because the AsyncStatus is \"Job Failed\"");
                    }
                    if (elapsedTime >= ASYNC_SLEEP_TIME_LIMIT) {
                        jobStatus = "aborted";
                        throw new RuntimeException("async was aborted because the number of retries exceeded the limit");
                    }
                }
                asyncCompleted = true;
            }
            catch (RuntimeException e) {
                if (jobStatus.equals("failed")) {
                    continue;
                }
                throw new APIException(e);
            }
        }
        AdReportRun finalReport = fetchAdReportRunSafely(adReportRun.getId());
        if (adReportRun == null || finalReport.getFieldAsyncPercentCompletion() != 100) {
            throw new APIException();
        }

        // extra waiting
        int retryCount = 0;
        boolean succeeded = false;
        APINodeList<AdsInsights> adsInsights = null;
        AdReportRun latestReport = fetchAdReportRunSafely(adReportRun.getId());
        while (retryCount < pluginTask.getMaxWaitSeconds() && !succeeded) {
            try {
                Thread.sleep(1000);
                adsInsights = latestReport.getInsights().execute();
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
        if (pluginTask.getUseUnifiedAttributionSetting().isPresent()) {
            request.setUseUnifiedAttributionSetting(pluginTask.getUseUnifiedAttributionSetting().get());
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
        if (pluginTask.getUseUnifiedAttributionSetting().isPresent()) {
            request.setUseUnifiedAttributionSetting(pluginTask.getUseUnifiedAttributionSetting().get());
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
        if (pluginTask.getUseUnifiedAttributionSetting().isPresent()) {
            request.setUseUnifiedAttributionSetting(pluginTask.getUseUnifiedAttributionSetting().get());
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
        if (pluginTask.getUseUnifiedAttributionSetting().isPresent()) {
            request.setUseUnifiedAttributionSetting(pluginTask.getUseUnifiedAttributionSetting().get());
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
        APIContext context = new APIContext(pluginTask.getAccessToken());
        context.enableDebug(this.pluginTask.getEnableDebug());
        return context;
    }
    private List<String> fieldNames()
    {
        return pluginTask.getFields().getColumns().stream().map(ColumnConfig::getName).filter(s -> !Breakdown.NAMES.contains(s)).collect(Collectors.toList());
    }

    private AdReportRun fetchAdReportRunSafely(String reportRunId) {
        try {
            return new AdReportRun(reportRunId, apiContext())
                .get()
                .requestAccountIdField()
                .requestAsyncPercentCompletionField()
                .requestAsyncStatusField()
                .requestDateStartField()
                .requestDateStopField()
                .requestEmailsField()
                .requestFriendlyNameField()
                .requestIdField()
                .requestIsBookmarkedField()
                .requestIsRunningField()
                .requestScheduleIdField()
                .requestTimeCompletedField()
                .requestTimeRefField()
                // v23.0で権限エラーを起こすフィールドを明示的に除外する
                .requestAsyncReportUrlField(false)
                .requestErrorCodeField(false)
                .requestIsAsyncExportField(false)
                .execute();
        } catch (Exception e) {
            logger.error("Failed to fetch AdReportRun with ID: {}", reportRunId, e);
            throw new RuntimeException("AdReportRun fetch failed for ID: " + reportRunId, e);
        }
    }
}
