package com.garethlewis.eagles.waiters;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.garethlewis.eagles.adapters.NewsListAdapter;
import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.entities.NewsItem;
import com.garethlewis.eagles.fragments.news.NewsViewHelper;
import com.garethlewis.eagles.util.ContentFetcher;

public class NewsWaiter extends BaseWaiter {

    private Context context;
    private NewsListAdapter adapter;
    private LinearLayout progress;

    public NewsWaiter(Context context, NewsListAdapter adapter, LinearLayout progress) {
        this.context = context;
        this.adapter = adapter;
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

        NewsViewHelper.displayList(adapter, newsItems);
        progress.setVisibility(View.GONE);
    }

}
