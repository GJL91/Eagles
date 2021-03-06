package com.garethlewis.eagles.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.garethlewis.eagles.adapters.AllStatsListAdapter;
import com.garethlewis.eagles.adapters.FixtureListAdapter;
import com.garethlewis.eagles.adapters.NewsListAdapter;
import com.garethlewis.eagles.adapters.StatsNameListAdapter;
import com.garethlewis.eagles.adapters.TwitterListAdapter;

public class FetcherPackage {

    private Context context;
    private View otherView;

    /** Whether the ScheduleFetcher should display only the Eagles Schedule */
    private boolean mode;
    private int extra = -1;

    private Fragment source;

    private NewsListAdapter newsListAdapter;
    private FixtureListAdapter fixtureListAdapter;
    private TwitterListAdapter twitterListAdapter;

    private StatsNameListAdapter[] statsNameListAdapters;
    private AllStatsListAdapter[] allStatsListAdapters;

    public FetcherPackage(Context context, boolean mode, View otherView) {
        this.context = context;
        this.mode = mode;
        this.otherView = otherView;
    }

    public Context getContext() {
        return context;
    }

    public boolean getMode() {
        return mode;
    }

    public int getExtra() {
        return extra;
    }

    public void setExtra(int extra) {
        this.extra = extra;
    }

    public View getOtherView() {
        return otherView;
    }

    public NewsListAdapter getNewsAdapter() {
        return newsListAdapter;
    }

    public void setNewsAdapter(NewsListAdapter newsListAdapter) {
        this.newsListAdapter = newsListAdapter;
    }

    public FixtureListAdapter getScheduleAdapter() {
        return fixtureListAdapter;
    }

    public void setScheduleAdapter(FixtureListAdapter scheduleAdapter) {
        this.fixtureListAdapter = scheduleAdapter;
    }

    public TwitterListAdapter getTwitterAdapter() {
        return twitterListAdapter;
    }

    public void setTwitterAdapter(TwitterListAdapter twitterListAdapter) {
        this.twitterListAdapter = twitterListAdapter;
    }

    public StatsNameListAdapter[] getStatsNameAdapters() {
        return statsNameListAdapters;
    }

    public void setStatsNameAdapters(StatsNameListAdapter[] statsNameListAdapters) {
        this.statsNameListAdapters = statsNameListAdapters;
    }

    public AllStatsListAdapter[] getAllStatsAdapters() {
        return allStatsListAdapters;
    }

    public void setAllStatsAdapters(AllStatsListAdapter[] allStatsListAdapters) {
        this.allStatsListAdapters = allStatsListAdapters;
    }

    public Fragment getSource() {
        return source;
    }

    public void setSource(Fragment source) {
        this.source = source;
    }
}
