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

    public ParserPackage(Context context, LayoutInflater inflater, ViewGroup container,
                         LinearLayout linearLayout, ProgressBar progress) {
        this.context = context;
        this.inflater = inflater;
        this.container = container;
        this.linearLayout = linearLayout;
        this.progress = progress;
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
}
