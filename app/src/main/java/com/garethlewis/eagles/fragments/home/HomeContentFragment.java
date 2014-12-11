package com.garethlewis.eagles.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.database.entities.Fixture;
import com.garethlewis.eagles.database.entities.NewsItem;
import com.garethlewis.eagles.fragments.news.NewsViewHelper;
import com.garethlewis.eagles.fragments.schedule.ScheduleViewHelper;
import com.garethlewis.eagles.util.ContentFetcher;

import java.util.List;

public class HomeContentFragment extends android.support.v4.app.Fragment {
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
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_media_list);

            UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
            if (db.needsUpdate("Media")) {
                doNewsFetch(inflater, container);
            } else {
                displayNews(inflater, linearLayout);
            }

        } else {
            if (position == 1) {
                // Schedule stuff.
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_schedule_list);

                if (linearLayout != null) {
                    UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
                    if (db.needsUpdate("Schedule")) {
                        LinearLayout progress = (LinearLayout) view.findViewById(R.id.home_schedule_progress);

                        if (ContentFetcher.isScheduleSyncing()) {
                            // Wait for schedule syncing to complete, then display the schedules.
                            // Display the progress bar until that point.
                            progress.setVisibility(View.VISIBLE);

                            // Need an async task to wait and poll the ContentFetcher as otherwise the ui thread will hang.


                        } else {
                            ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, linearLayout, progress, true);
                            ContentFetcher.fetchSchedules(parserPackage);
                        }
                    } else {
                        displaySchedule(inflater, view, linearLayout);
                    }
                }
            }
//            else {
                // Do stuff to get tweets here.
//            }
        }

        return view;
    }

    private void displayNews(LayoutInflater inflater, LinearLayout linearLayout) {
        MediaSQLiteHelper mediaDB = MediaSQLiteHelper.getInstance(getActivity());
        NewsItem[] newsItems = mediaDB.getStories();

        NewsViewHelper.displayList(getActivity(), inflater, linearLayout, newsItems);
    }

    private void displaySchedule(LayoutInflater inflater, View view, LinearLayout linearLayout) {
        TextView recordView = (TextView) view.findViewById(R.id.record_text);
        StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(getActivity());
        String record = standingsDB.getRecord("Eagles");
        recordView.setText(record);

        ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
        List<Fixture> fixtures = scheduleDB.getFixturesForTeam("Eagles");

        ScheduleViewHelper.displayList(getActivity(), inflater, linearLayout, fixtures, false);
    }

    public void doNewsFetch(LayoutInflater inflater) {
        doNewsFetch(inflater, null);
    }

    public void doNewsFetch(LayoutInflater inflater, ViewGroup container) {
        View view = this.view;
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_media_list);

        LinearLayout progress = (LinearLayout) view.findViewById(R.id.home_media_progress);

        if (ContentFetcher.isNewsSyncing()) {

        } else {
            ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, linearLayout, progress, false);
            ContentFetcher.fetchNews(parserPackage);
        }

    }

}
