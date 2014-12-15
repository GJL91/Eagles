package com.garethlewis.eagles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class FetcherPackage {

    private Context context;
    private LayoutInflater inflater;
    private ViewGroup container;
    private LinearLayout linearLayout;
    private LinearLayout progress;

    /** Whether the ScheduleFetcher should display only the Eagles Schedule */
    private boolean mode;

    public FetcherPackage(Context context, LayoutInflater inflater, ViewGroup container,
                          LinearLayout linearLayout, LinearLayout progress, boolean mode) {
        this.context = context;
        this.inflater = inflater;
        this.container = container;
        this.linearLayout = linearLayout;
        this.progress = progress;
        this.mode = mode;
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
}
