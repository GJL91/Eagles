package com.garethlewis.eagles.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;

import com.garethlewis.eagles.adapters.FixtureListAdapter;
import com.garethlewis.eagles.adapters.NewsListAdapter;
import com.garethlewis.eagles.adapters.TwitterListAdapter;

public class FetcherPackage {

    private Context context;
    private View otherView;

    /** Whether the ScheduleFetcher should display only the Eagles Schedule */
    private boolean mode;

    private Fragment source;

    private NewsListAdapter newsListAdapter;
    private FixtureListAdapter fixtureListAdapter;
    private TwitterListAdapter twitterListAdapter;

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

    public Fragment getSource() {
        return source;
    }

    public void setSource(Fragment source) {
        this.source = source;
    }
}
