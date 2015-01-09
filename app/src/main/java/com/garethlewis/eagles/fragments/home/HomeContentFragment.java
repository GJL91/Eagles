package com.garethlewis.eagles.fragments.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.adapters.FixtureListAdapter;
import com.garethlewis.eagles.adapters.NewsListAdapter;
import com.garethlewis.eagles.adapters.TwitterListAdapter;
import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.entities.Fixture;
import com.garethlewis.eagles.entities.NewsItem;
import com.garethlewis.eagles.fragments.news.NewsViewHelper;
import com.garethlewis.eagles.fragments.schedule.ScheduleViewHelper;
import com.garethlewis.eagles.util.ContentFetcher;
import com.garethlewis.eagles.util.FetcherPackage;
import com.garethlewis.eagles.waiters.BaseWaiter;
import com.garethlewis.eagles.waiters.NewsWaiter;
import com.garethlewis.eagles.waiters.ScheduleWaiter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;

public class HomeContentFragment extends Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private int position = 0;
    private int layout[] = new int[] {
            R.layout.fragment_home_media,
            R.layout.fragment_home_schedule,
            R.layout.fragment_home_twitter
    };

    private View view;

    private BaseAdapter adapter;

    private PullRefreshLayout pullLayout;
    private boolean refreshing = false;

    public HomeContentFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(HomeContentFragment.ARG_SECTION_NUMBER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout[position], container, false);

        this.view = view;
        if (position == 0) {
            // News stuff

            pullLayout = (PullRefreshLayout) view.findViewById(R.id.home_media_swipe_container);
            pullLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshNews();
                }
            });
//            pullLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);

            ListView list = (ListView) view.findViewById(R.id.home_media_list);
            list.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return refreshing;
                }
            });

            View header = inflater.inflate(R.layout.news_list_header, list, false);
            list.addHeaderView(header);

            adapter = new NewsListAdapter(getActivity(), new ArrayList<NewsItem>());
            list.setAdapter(adapter);

            displayNews();

            UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
            if (db.needsUpdate("Media")) {
                doNewsFetch();
            }

        } else {
            if (position == 1) {
                // Schedule stuff.

                pullLayout = (PullRefreshLayout) view.findViewById(R.id.home_schedule_swipe_container);
                pullLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshSchedule();
                    }
                });

                ListView list = (ListView) view.findViewById(R.id.home_schedule_list);
                list.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return refreshing;
                    }
                });

                View header = inflater.inflate(R.layout.home_schedule_header, list, false);
                list.addHeaderView(header);

                adapter = new FixtureListAdapter(getActivity(), new ArrayList<Fixture>());
                list.setAdapter(adapter);

                displaySchedule();

                UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
                if (db.needsUpdate("Schedule")) {
                    doScheduleFetch();
                }

            } else {
                // Twitter stuff.

                pullLayout = (PullRefreshLayout) view.findViewById(R.id.home_twitter_swipe_container);
                pullLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        refreshTwitter();
                    }
                });

                ListView list = (ListView) view.findViewById(R.id.home_twitter_list);
                list.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return refreshing;
                    }
                });

                View header = inflater.inflate(R.layout.twitter_list_header, list, false);
                list.addHeaderView(header);

                adapter = new TwitterListAdapter(getActivity(), new ArrayList<Status>());
                list.setAdapter(adapter);

                doTwitterFetch();
            }
        }

        return view;
    }

    public void finished() {
        refreshing = false;
        pullLayout.setRefreshing(false);
    }

    private void displayNews() {
        MediaSQLiteHelper mediaDB = MediaSQLiteHelper.getInstance(getActivity());
        NewsItem[] newsItems = mediaDB.getStories();

        NewsViewHelper.displayList((NewsListAdapter) adapter, newsItems);
    }

    private void displaySchedule() {
        TextView recordView = (TextView) view.findViewById(R.id.record_text);
        StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(getActivity());
        String record = standingsDB.getRecord("Eagles");
        recordView.setText(record);

        ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
        List<Fixture> fixtures = scheduleDB.getFixturesForTeam("Eagles");

        ScheduleViewHelper.displayList((FixtureListAdapter) adapter, fixtures, false);
    }

    public void refreshNews() {
        doNewsFetch();
    }

    private void doNewsFetch() {
        if (!refreshing) {
            refreshing = true;
            pullLayout.setRefreshing(true);
            if (ContentFetcher.isNewsSyncing()) {
                BaseWaiter waiter = new NewsWaiter(getActivity(), (NewsListAdapter) adapter, this);
                waiter.startWaiting();
            } else {
                FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), false, null);
                fetcherPackage.setSource(this);
                fetcherPackage.setNewsAdapter((NewsListAdapter) adapter);
                ContentFetcher.fetchNews(fetcherPackage);
            }
        }
    }

    public void refreshSchedule() {
        doScheduleFetch();
    }

    private void doScheduleFetch() {
        if (!refreshing) {
            refreshing = true;
            pullLayout.setRefreshing(true);

            View view = this.view;

            if (ContentFetcher.isScheduleSyncing()) {
                // Wait for schedule syncing to complete, then display the schedules.
                // Need an async task to wait and poll the ContentFetcher as otherwise the ui thread will hang.
                BaseWaiter waiter = new ScheduleWaiter(getActivity(), view, (FixtureListAdapter) adapter, null, false);
                waiter.startWaiting();
            } else {
                View recordView = view.findViewById(R.id.record_text);
                FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), true, recordView);
                fetcherPackage.setSource(this);
                fetcherPackage.setScheduleAdapter((FixtureListAdapter) adapter);
                ContentFetcher.fetchSchedules(fetcherPackage);
            }
        }
    }

    public void refreshTwitter() {
//        View view = this.view;
//        ListView list = (ListView) view.findViewById(R.id.home_twitter_list);
//
//        ((TwitterListAdapter) list.getAdapter()).clearItems();

        doTwitterFetch();
    }

    private void doTwitterFetch() {
        if (!refreshing) {
            refreshing = true;
            pullLayout.setRefreshing(true);
            if (!ContentFetcher.isTwitterSyncing()) { // Only place that fetches tweets. If syncing is taking place, it's come from here.
                FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), false, null);
                fetcherPackage.setSource(this);
                fetcherPackage.setTwitterAdapter((TwitterListAdapter) adapter);
                ContentFetcher.fetchTwitter(fetcherPackage);
            }
        }
    }

}
