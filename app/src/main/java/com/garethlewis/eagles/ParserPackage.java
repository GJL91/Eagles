package com.garethlewis.eagles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class ParserPackage {

    private Context context;
    private LayoutInflater inflater;
    private ViewGroup container;
    private LinearLayout linearLayout;
    private ProgressBar progress;

    /** Whether the ScheduleParser should display only the Eagles Schedule */
    private boolean mode;

    public ParserPackage(Context context, LayoutInflater inflater, ViewGroup container,
                         LinearLayout linearLayout, ProgressBar progress, boolean mode) {
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

    public ProgressBar getProgress() {
        return progress;
    }

    public boolean getMode() {
        return mode;
    }
}
