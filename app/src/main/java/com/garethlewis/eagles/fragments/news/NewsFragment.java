package com.garethlewis.eagles.fragments.news;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.database.entities.NewsItem;
import com.garethlewis.eagles.util.ContentFetcher;
import com.garethlewis.eagles.util.FetcherPackage;
import com.garethlewis.eagles.waiters.BaseWaiter;
import com.garethlewis.eagles.waiters.NewsWaiter;

public class NewsFragment extends Fragment {

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_main, container, false);

        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.news_media_list);

        UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
        if (db.needsUpdate("Media")) {
            doNewsFetch(inflater, container);
        } else {
            displayNews(inflater, linearLayout);
        }

        return view;
    }

    public void refreshNews(LayoutInflater inflater) {
        View view = this.view;
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.news_media_list);

        linearLayout.removeAllViews();

        doNewsFetch(inflater, null);
    }

    private void doNewsFetch(LayoutInflater inflater, ViewGroup container) {
        View view = this.view;
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.news_media_list);

        LinearLayout progress = (LinearLayout) view.findViewById(R.id.news_media_progress);

        if (ContentFetcher.isNewsSyncing()) {
            BaseWaiter waiter = new NewsWaiter(getActivity(), inflater, linearLayout, progress);
            waiter.startWaiting();
        } else {
            FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), inflater, container, linearLayout, progress, false, null);
            ContentFetcher.fetchNews(fetcherPackage);
        }

    }

    private void displayNews(LayoutInflater inflater, LinearLayout linearLayout) {
        MediaSQLiteHelper mediaDB = MediaSQLiteHelper.getInstance(getActivity());
        NewsItem[] newsItems = mediaDB.getStories();

        NewsViewHelper.displayList(getActivity(), inflater, linearLayout, newsItems);
    }

}
