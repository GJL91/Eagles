package com.garethlewis.eagles.waiters;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.garethlewis.eagles.adapters.NewsListAdapter;
import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.entities.NewsItem;
import com.garethlewis.eagles.fragments.home.HomeContentFragment;
import com.garethlewis.eagles.fragments.news.NewsFragment;
import com.garethlewis.eagles.fragments.news.NewsViewHelper;
import com.garethlewis.eagles.util.ContentFetcher;

public class NewsWaiter extends BaseWaiter {

    private Context context;
    private NewsListAdapter adapter;
    private Fragment source;

    public NewsWaiter(Context context, NewsListAdapter adapter, Fragment fragment) {
        this.context = context;
        this.adapter = adapter;
        this.source = fragment;
    }

    @Override
    public void startWaiting() {
        waitTask(ContentFetcher.NEWS);
    }

    @Override
    public void displayResults() {
        MediaSQLiteHelper mediaDB = MediaSQLiteHelper.getInstance(context);
        NewsItem[] newsItems = mediaDB.getStories();

        NewsViewHelper.displayList(adapter, newsItems);

        if (source instanceof NewsFragment) {
            ((NewsFragment) source).finished();
        } else {
            ((HomeContentFragment) source).finished();
        }
    }

}
