package com.garethlewis.eagles.fragments.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.adapters.NewsListAdapter;
import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.entities.NewsItem;
import com.garethlewis.eagles.util.ContentFetcher;
import com.garethlewis.eagles.util.FetcherPackage;
import com.garethlewis.eagles.waiters.BaseWaiter;
import com.garethlewis.eagles.waiters.NewsWaiter;

import java.util.ArrayList;

public class NewsFragment extends Fragment {
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.view = inflater.inflate(R.layout.news_main, container, false);

        ListView list = (ListView) view.findViewById(R.id.news_media_list);
        NewsListAdapter adapter = new NewsListAdapter(getActivity(), new ArrayList<NewsItem>());
        list.setAdapter(adapter);

        UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
        if (db.needsUpdate("Media")) {
            doNewsFetch(inflater, container, adapter);
        } else {
            displayNews(adapter);
        }

        return view;
    }

    public void refreshNews(LayoutInflater inflater) {
        ListView list = (ListView) view.findViewById(R.id.news_media_list);
        ((NewsListAdapter) list.getAdapter()).clearItems();

        doNewsFetch(inflater, null, null);
    }

    private void doNewsFetch(LayoutInflater inflater, ViewGroup container, NewsListAdapter adapter) {
        LinearLayout progress = (LinearLayout) view.findViewById(R.id.news_media_progress);

        if (ContentFetcher.isNewsSyncing()) {
            BaseWaiter waiter = new NewsWaiter(getActivity(), adapter, progress);
            waiter.startWaiting();
        } else {
            FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), inflater, container, null, progress, false, null);
            fetcherPackage.setNewsAdapter(adapter);
            ContentFetcher.fetchNews(fetcherPackage);
        }
    }

    private void displayNews(NewsListAdapter adapter) {
        MediaSQLiteHelper mediaDB = MediaSQLiteHelper.getInstance(getActivity());
        NewsItem[] newsItems = mediaDB.getStories();

        NewsViewHelper.displayList(adapter, newsItems);
    }
}
