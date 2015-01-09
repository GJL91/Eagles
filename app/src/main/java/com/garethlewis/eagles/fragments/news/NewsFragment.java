package com.garethlewis.eagles.fragments.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
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
    private PullRefreshLayout pullLayout;
    private NewsListAdapter adapter;
    private boolean refreshing = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_main, container, false);

        pullLayout = (PullRefreshLayout) view.findViewById(R.id.news_swipe_container);
        pullLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNews();
            }
        });

        ListView list = (ListView) view.findViewById(R.id.news_media_list);

        View header = inflater.inflate(R.layout.news_list_header, list, false);
        list.addHeaderView(header);

        adapter = new NewsListAdapter(getActivity(), new ArrayList<NewsItem>());
        list.setAdapter(adapter);

        UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
        if (db.needsUpdate("Media")) {
            doNewsFetch();
        } else {
            displayNews();
        }

        return view;
    }

    public void refreshNews() {
        if (!refreshing) {
            doNewsFetch();
        }
    }

    public void finished() {
        refreshing = false;
        pullLayout.setRefreshing(false);
    }

    private void doNewsFetch() {
        if (!refreshing) {
            refreshing = true;
            if (ContentFetcher.isNewsSyncing()) {
                BaseWaiter waiter = new NewsWaiter(getActivity(), adapter, this);
                waiter.startWaiting();
            } else {
                FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), null, null, null, null, false, null);
                fetcherPackage.setSource(this);
                fetcherPackage.setNewsAdapter(adapter);
                ContentFetcher.fetchNews(fetcherPackage);
            }
        }
    }

    private void displayNews() {
        MediaSQLiteHelper mediaDB = MediaSQLiteHelper.getInstance(getActivity());
        NewsItem[] newsItems = mediaDB.getStories();

        NewsViewHelper.displayList(adapter, newsItems);
    }
}
