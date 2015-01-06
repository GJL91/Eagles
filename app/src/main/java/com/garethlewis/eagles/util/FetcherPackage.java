package com.garethlewis.eagles.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garethlewis.eagles.adapters.TwitterListAdapter;

public class FetcherPackage {

    private Context context;
    private LayoutInflater inflater;
    private ViewGroup container;
    private LinearLayout linearLayout;
    private LinearLayout progress;
    private View otherView;

    /** Whether the ScheduleFetcher should display only the Eagles Schedule */
    private boolean mode;

    private TwitterListAdapter twitterListAdapter;

    public FetcherPackage(Context context, LayoutInflater inflater, ViewGroup container,
                          LinearLayout linearLayout, LinearLayout progress, boolean mode, View otherView) {
        this.context = context;
        this.inflater = inflater;
        this.container = container;
        this.linearLayout = linearLayout;
        this.progress = progress;
        this.mode = mode;
        this.otherView = otherView;
    }

    public Context getContext() {
        return context;
    }

    public LayoutInflater getInflater() {
        return inflater;
    }

    public ViewGroup getContainer() {
        return container;
    }

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public LinearLayout getProgress() {
        return progress;
    }

    public boolean getMode() {
        return mode;
    }

    public View getOtherView() {
        return otherView;
    }

    public TwitterListAdapter getTwitterAdapter() {
        return twitterListAdapter;
    }

    public void setTwitterAdapter(TwitterListAdapter twitterListAdapter) {
        this.twitterListAdapter = twitterListAdapter;
    }
}
