# Facebook Ads Insights input plugin for Embulk

embulk-input-facebook-ads-insights is the Embulk input plugin for [Facebook Ads Insights API](https://developers.facebook.com/docs/marketing-api/insights).

## Overview

Required Embulk version >= 0.9

* **Plugin type**: input
* **Resume supported**: no
* **Cleanup supported**: no
* **Guess supported**: no

## Configuration

| Name                            | Required | Type                                                             | Description                                                                                                                |
|:--------------------------------|:---------|:-----------------------------------------------------------------|:---------------------------------------------------------------------------------------------------------------------------|
| access_token                    | yes      | string                                                           | Facebook access token.                                                                                                     |
| object_type                     | yes      | ObjectEnum                                                       | ads object type.                                                                                                           |
| object_id                       | yes      | string                                                           | ads object id.                                                                                                             |
| fields                          | yes      | array({name:{FieldEnum, BreakdownEnum}, type:string})            | columns to fetch.                                                                                                          |
| action_attribution_windows      | no       | array(ActionAttributionWindowEnum)                               | see [action_attribution_windows](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.      |
| action_breakdowns               | no       | array(ActionBreakdownEnum)                                       | see [action_breakdowns](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.               |
| action_report_time              | no       | enum{impression, conversion}                                     | see [action_report_time](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.              |
| breakdowns                      | no       | array(BreakdownEnum)                                             | see [breakdowns](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                      |
| date_preset                     | no       | DatePresetEnum                                                   | see [date_preset](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                     |
| filtering                       | no       | array({field:string,operator:FilteringOperatorEnum,value:string})| see [filtering](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                       |
| level                           | no       | ObjectEnum                                                       | see [level](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                           |
| product_id_limit                | no       | integer                                                          | see [product_id_limit](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                |
| sort                            | no       | array(string)                                                    | see [sort](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                            |
| time_increment                  | no       | enum{monthly, all_days} or integer(1-90)                         | see [time_increment](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                  |
| time_range                      | no       | {since:YYYY-MM-DD, until:YYYY-MM-DD}                             | see [time_range](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                      |
| time_ranges                     | no       | array({since:YYYY-MM-DD, until:YYYY-MM-DD})                      | see [time_ranges](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details.                     |
| use_account_attribution_setting | no       | boolean                                                          | see [use_account_attribution_setting](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details. |
| use_unified_attribution_setting | no       | boolean                                                          | see [use_unified_attribution_setting](https://developers.facebook.com/docs/marketing-api/insights/parameters) for details. |

### Enums

| Name                        | Values                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |         
|:----------------------------|:--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| ObjectEnum                  | account, campaign, adset, ad                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| FieldEnum                   | account_currency, account_id, account_name, action_values, actions, ad_click_actions, ad_id, ad_impression_actions, ad_name, adset_end, adset_id, adset_name, adset_start, age_targeting, auction_bid, auction_competitiveness, auction_max_competitor_bid, buying_type, campaign_id, campaign_name, canvas_avg_view_percent, canvas_avg_view_time, clicks, conversion_rate_ranking, conversion_values, conversions, cost_per_15_sec_video_view, cost_per_2_sec_continuous_video_view, cost_per_action_type, cost_per_ad_click, cost_per_conversion, cost_per_dda_countby_convs, cost_per_estimated_ad_recallers, cost_per_inline_link_click, cost_per_inline_post_engagement, cost_per_one_thousand_ad_impression, cost_per_outbound_click, cost_per_thruplay, cost_per_unique_action_type, cost_per_unique_click, cost_per_unique_conversion, cost_per_unique_inline_link_click, cost_per_unique_outbound_click, cpc, cpm, cpp, created_time, ctr, date_start, date_stop, dda_countby_convs, engagement_rate_ranking, estimated_ad_recall_rate, estimated_ad_recall_rate_lower_bound, estimated_ad_recall_rate_upper_bound, estimated_ad_recallers, estimated_ad_recallers_lower_bound, estimated_ad_recallers_upper_bound, frequency, full_view_impressions, full_view_reach, gender_targeting, impressions, inline_link_click_ctr, inline_link_clicks, inline_post_engagement, instant_experience_clicks_to_open, instant_experience_clicks_to_start, instant_experience_outbound_clicks, labels, location, mobile_app_purchase_roas, objective, outbound_clicks, outbound_clicks_ctr, place_page_name, purchase_roas, quality_ranking, quality_score_ectr, quality_score_ecvr, quality_score_organic, reach, social_spend, spend, unique_actions, unique_clicks, unique_conversions, unique_ctr, unique_inline_link_click_ctr, unique_inline_link_clicks, unique_link_clicks_ctr, unique_outbound_clicks, unique_outbound_clicks_ctr, unique_video_continuous_2_sec_watched_actions, unique_video_view_15_sec, updated_time, video_15_sec_watched_actions, video_30_sec_watched_actions, video_avg_time_watched_actions, video_continuous_2_sec_watched_actions, video_p100_watched_actions, video_p25_watched_actions, video_p50_watched_actions, video_p75_watched_actions, video_p95_watched_actions, video_play_actions, video_thruplay_watched_actions, video_time_watched_actions, website_ctr, website_purchase_roas, wish_bid |
| ActionAttributionWindowEnum | 1d_view, 7d_view, 28d_view, 1d_click, 7d_click, 28d_click, default                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
| ActionBreakdownEnum         | action_device, action_canvas_component_name, action_carousel_card_id, action_carousel_card_name, action_destination, action_reaction, action_target_id, action_type, action_video_sound, action_video_type                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| BreakdownEnum               | ad_format_asset, age, body_asset, call_to_action_asset, country, description_asset, gender, image_asset, impression_device, link_url_asset, product_id, region, title_asset, video_asset, dma, frequency_value, hourly_stats_aggregated_by_advertiser_time_zone, hourly_stats_aggregated_by_audience_time_zone, place_page_id, publisher_platform, platform_position, device_platform                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| DatePresetEnum              | today, yesterday, this_month, last_month, this_quarter, lifetime, maximum, last_3d, last_7d, last_14d, last_28d, last_30d, last_90d, last_week_mon_sun, last_week_sun_sat, last_quarter, last_year, this_week_mon_today, this_week_sun_today, this_year                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| FilteringOperatorEnum       | EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL, LESS_THAN, LESS_THAN_OR_EQUAL, IN_RANGE, NOT_IN_RANGE, CONTAIN, NOT_CONTAIN, IN, NOT_IN, STARTS_WITH, ANY, ALL, AFTER, BEFORE, NONE}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |

## Example

```yaml
in:
  type: facebook_ads_insights
  access_token: <YOUR_ACCESS_TOKEN>
  object_type: account
  object_id: 1234567890123456
  level: campaign
  breakdowns:
    - age
  filtering:
    - {field: campaign_name, operator: EQUAL, value: hoge}
  sort:
    - impressions_descending
  time_ranges:
    - {since: 2019-09-01, until: 2019-09-07}
  fields:
    - {name: age, type: string}
    - {name: campaign_id, type: long}
    - {name: campaign_name, type: string}
    - {name: impressions, type: long}
    - {name: spend, type: double}
    - {name: actions, type: string}
```


## Build

```
$ ./gradlew gem  # -t to watch change of files and rebuild continuously
```

## TEST

```
$ ./gradlew test
```

If you want to test using the actual API, create a file referring to example/test.yml.example and set the EMBULK_INPUT_FACEBOOK_ADS_INSIGTHS_TEST_CONFIG environment variable.

```
$ EMBULK_INPUT_FACEBOOK_ADS_INSIGTHS_TEST_CONFIG="example/test.yml" ./gradlew test # Create example/test.yml based on example/test.yml.example
```
