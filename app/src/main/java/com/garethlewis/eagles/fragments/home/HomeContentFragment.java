package com.garethlewis.eagles.fragments.home;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.database.entities.Fixture;
import com.garethlewis.eagles.fragments.schedule.ScheduleViewHelper;
import com.garethlewis.eagles.parsers.NewsParser;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.parsers.ScheduleParser;

import java.util.List;

public class HomeContentFragment extends android.support.v4.app.Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private int position = 0;
    private int layout[] = new int[] {
            R.layout.fragment_home_media,
            R.layout.fragment_home_schedule,
            R.layout.fragment_home_twitter
    };

    public HomeContentFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(HomeContentFragment.ARG_SECTION_NUMBER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout[position], container, false);

        if (position == 0) {
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_media_list);

            UpdatedSQLiteHelper db = new UpdatedSQLiteHelper(getActivity());
            if (db.needsUpdate("Media")) {
                ProgressBar progress = (ProgressBar) view.findViewById(R.id.media_progress);
                progress.setVisibility(View.VISIBLE);

                ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, linearLayout, progress, false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    new NewsParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
                } else {
                    new NewsParser().execute(parserPackage);
                }

            } else {

            }

        } else {
            if (position == 1) {
                // Schedule stuff.
                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_schedule_list);

                if (linearLayout != null) {
                    UpdatedSQLiteHelper db = new UpdatedSQLiteHelper(getActivity());
                    if (db.needsUpdate("Schedule")) {
                        ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.schedule_progress);
                        progressBar.setVisibility(View.VISIBLE);

                        ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, linearLayout, progressBar, true);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            new ScheduleParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
                        } else {
                            new ScheduleParser().execute(parserPackage);
                        }

                    } else {
                        ScheduleSQLiteHelper scheduleDB = new ScheduleSQLiteHelper(getActivity());
                        List<Fixture> fixtures = scheduleDB.getFixturesForTeam("Philadelphia");

                        ScheduleViewHelper.displayList(getActivity(), inflater, linearLayout, fixtures, false);
                    }
                }

            } else {
                // Do stuff to get tweets here.
            }
        }

        return view;
    }
}
