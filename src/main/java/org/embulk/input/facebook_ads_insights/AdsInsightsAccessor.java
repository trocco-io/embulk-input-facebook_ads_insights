package org.embulk.input.facebook_ads_insights;

import com.facebook.ads.sdk.AdsInsights;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdsInsightsAccessor {
    private final Logger logger =  LoggerFactory.getLogger(AdsInsightsAccessor.class);

    private AdsInsights adsInsights;

    public AdsInsightsAccessor(AdsInsights adsInsights) {
        this.adsInsights = adsInsights;
    }

    public String get(String name) {
        switch (name) {
            case "impressions": return adsInsights.getFieldImpressions();
            case "cpc": return adsInsights.getFieldCpc();
            case "ad_id": return adsInsights.getFieldAdId();
            case "ad_name": return adsInsights.getFieldAdName();
            case "campaign_id": return adsInsights.getFieldCampaignId();
            case "campaign_name": return adsInsights.getFieldCampaignName();
            case "adset_id": return adsInsights.getFieldAdsetId();
            case "adset_name": return adsInsights.getFieldAdsetName();
            case "cpp": return adsInsights.getFieldCpp();
            case "ctr": return adsInsights.getFieldCtr();
            case "clicks": return adsInsights.getFieldClicks();
            case "spend": return adsInsights.getFieldSpend();
            default: return "";
        }
    }
}
