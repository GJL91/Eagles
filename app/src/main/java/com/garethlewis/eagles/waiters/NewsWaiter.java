package com.garethlewis.eagles.waiters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.database.entities.NewsItem;
import com.garethlewis.eagles.fragments.news.NewsViewHelper;
import com.garethlewis.eagles.util.ContentFetcher;

public class NewsWaiter extends BaseWaiter {

    private Context context;
    private LayoutInflater inflater;
    private View view;
    private LinearLayout linearLayout;
    private LinearLayout progress;

    public NewsWaiter(Context context, LayoutInflater inflater, View view, LinearLayout linearLayout, LinearLayout progress) {
        this.context = context;
        this.inflater = inflater;
        this.view = view;
        this.linearLayout = linearLayout;
        this.progress = progress;
    }

    @Override
    public void startWaiting() {
        progress.setVisibility(View.VISIBLE);
        waitTask(ContentFetcher.NEWS);
    }

    @Override
    public void displayResults() {
        MediaSQLiteHelper mediaDB = MediaSQLiteHelper.getInstance(context);
        NewsItem[] newsItems = mediaDB.getStories();

        NewsViewHelper.displayList(context, inflater, linearLayout, newsItems);
        progress.setVisibility(View.GONE);
    }

}
