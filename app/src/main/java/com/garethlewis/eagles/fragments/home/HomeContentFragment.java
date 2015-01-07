package com.garethlewis.eagles.fragments.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import com.garethlewis.eagles.util.ContentFetcher;
import com.garethlewis.eagles.util.FetcherPackage;
import com.garethlewis.eagles.waiters.BaseWaiter;
import com.garethlewis.eagles.waiters.NewsWaiter;

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
            UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
            if (db.needsUpdate("Media")) {
                doNewsFetch(inflater, container);
            } else {
                displayNews();
            }

        } else {
            if (position == 1) {
                // Schedule stuff.
                ListView list = (ListView) view.findViewById(R.id.home_schedule_list);

                if (list != null) {
//                    UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
//                    if (db.needsUpdate("Schedule")) {
//                        doScheduleFetch(inflater, container);
//                    } else {
                        displaySchedule(list);
//                    }
                }
            } else {
                ListView list = (ListView) view.findViewById(R.id.home_twitter_list);

                if (list != null) {
                    doTwitterFetch(inflater, container);
                }
            }
        }

        return view;
    }

    private void displayNews() {
        ListView list = (ListView) view.findViewById(R.id.home_media_list);
        NewsListAdapter adapter = new NewsListAdapter(getActivity(), new ArrayList<NewsItem>());
        list.setAdapter(adapter);

        MediaSQLiteHelper mediaDB = MediaSQLiteHelper.getInstance(getActivity());
        NewsItem[] newsItems = mediaDB.getStories();

        NewsViewHelper.displayList(adapter, newsItems);
    }

    private void displaySchedule(ListView list) {
        TextView recordView = (TextView) view.findViewById(R.id.record_text);
        StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(getActivity());
        String record = standingsDB.getRecord("Eagles");
        recordView.setText(record);

        ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
        List<Fixture> fixtures = scheduleDB.getFixturesForTeam("Eagles");

        FixtureListAdapter adapter = new FixtureListAdapter(getActivity(), new ArrayList<Fixture>());
        list.setAdapter(adapter);

        for (Fixture fixture : fixtures) {
            if ("".equals(fixture.getAwayTeam())) {
                adapter.addByeFixture(fixture);
            } else {
                if (fixture.getHomeScore() == -1) {
                    adapter.addFutureFixture(fixture);
                } else {
                    adapter.addResultFixture(fixture);
                }
            }
        }
    }

//    private void displaySchedule(LayoutInflater inflater, View view, LinearLayout linearLayout) {
//        TextView recordView = (TextView) view.findViewById(R.id.record_text);
//        StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(getActivity());
//        String record = standingsDB.getRecord("Eagles");
//        recordView.setText(record);
//
//        ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
//        List<Fixture> fixtures = scheduleDB.getFixturesForTeam("Eagles");
//
//        ScheduleViewHelper.displayList(getActivity(), inflater, linearLayout, fixtures, false);
//    }

    public void refreshNews(LayoutInflater inflater) {
        View view = this.view;
        ListView list = (ListView) view.findViewById(R.id.home_media_list);

        ((NewsListAdapter) list.getAdapter()).clearItems();

        doNewsFetch(inflater, null);
    }

    private void doNewsFetch(LayoutInflater inflater, ViewGroup container) {
        View view = this.view;
        ListView list = (ListView) view.findViewById(R.id.home_media_list);
        NewsListAdapter adapter = new NewsListAdapter(getActivity(), new ArrayList<NewsItem>());
        list.setAdapter(adapter);

        LinearLayout progress = (LinearLayout) view.findViewById(R.id.home_media_progress);

        if (ContentFetcher.isNewsSyncing()) {
            BaseWaiter waiter = new NewsWaiter(getActivity(), adapter, progress);
            waiter.startWaiting();
        } else {
            FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), inflater, container, null, progress, false, null);
            fetcherPackage.setNewsAdapter(adapter);
            ContentFetcher.fetchNews(fetcherPackage);
        }

    }

    public void refreshSchedule(LayoutInflater inflater) {
        View view = this.view;
        ListView list = (ListView) view.findViewById(R.id.home_schedule_list);

        ((FixtureListAdapter) list.getAdapter()).clearItems();

//        doScheduleFetch(inflater, null);
    }

//    private void doScheduleFetch(LayoutInflater inflater, ViewGroup container) {
//        View view = this.view;
//        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_schedule_list);
//
//        LinearLayout progress = (LinearLayout) view.findViewById(R.id.home_schedule_progress);
//
//        if (ContentFetcher.isScheduleSyncing()) {
//            // Wait for schedule syncing to complete, then display the schedules.
//            // Need an async task to wait and poll the ContentFetcher as otherwise the ui thread will hang.
//            BaseWaiter waiter = new ScheduleWaiter(getActivity(), inflater, view, linearLayout, progress, null, false);
//            waiter.startWaiting();
//        } else {
//            View recordView = view.findViewById(R.id.record_text);
//            FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), inflater, container, linearLayout, progress, true, recordView);
//            ContentFetcher.fetchSchedules(fetcherPackage);
//        }
//    }

    public void refreshTwitter(LayoutInflater inflater) {
        View view = this.view;
        ListView list = (ListView) view.findViewById(R.id.home_twitter_list);

        ((TwitterListAdapter) list.getAdapter()).clearItems();

        doTwitterFetch(inflater, null);
    }

    private void doTwitterFetch(LayoutInflater inflater, ViewGroup container) {
        View view = this.view;
        ListView list = (ListView) view.findViewById(R.id.home_twitter_list);

        TwitterListAdapter adapter = new TwitterListAdapter(getActivity(), new ArrayList<Status>());
        list.setAdapter(adapter);

        LinearLayout progress = (LinearLayout) view.findViewById(R.id.home_twitter_progress);

        if (!ContentFetcher.isTwitterSyncing()) { // Only place that fetches tweets. If syncing is taking place, it's come from here.
            FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), inflater, container, null, progress, false, null);
            fetcherPackage.setTwitterAdapter(adapter);
            ContentFetcher.fetchTwitter(fetcherPackage);
        }
    }

}
