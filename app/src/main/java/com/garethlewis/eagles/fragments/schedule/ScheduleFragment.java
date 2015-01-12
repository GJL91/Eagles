package com.garethlewis.eagles.fragments.schedule;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.baoyz.widget.PullRefreshLayout;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.adapters.FixtureListAdapter;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.entities.Fixture;
import com.garethlewis.eagles.util.ContentFetcher;
import com.garethlewis.eagles.util.FetcherPackage;
import com.garethlewis.eagles.util.ScheduleParams;
import com.garethlewis.eagles.waiters.BaseWaiter;
import com.garethlewis.eagles.waiters.ScheduleWaiter;

import java.util.ArrayList;
import java.util.List;

public class ScheduleFragment extends Fragment {

    private PullRefreshLayout pullLayout;
    private FixtureListAdapter adapter;
    private boolean refreshing = false;

    private Spinner weekSpinner;

    private int showing = 1;
    private int teamShowing = 0;
    private boolean programmatic = false;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.schedule_main, container, false);

        pullLayout = (PullRefreshLayout) parent.findViewById(R.id.schedule_swipe_container);
        pullLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshSchedule();
            }
        });

        ListView list = (ListView) parent.findViewById(R.id.fixtures_list);

        View header = inflater.inflate(R.layout.schedule_spinner_header, list, false);
        list.addHeaderView(header);

        adapter = new FixtureListAdapter(getActivity(), new ArrayList<Fixture>());
        list.setAdapter(adapter);

        weekSpinner = (Spinner) parent.findViewById(R.id.week_spinner);
        ArrayAdapter<CharSequence> weekAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weekHeadings, android.R.layout.simple_spinner_item);

        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSpinner.setAdapter(weekAdapter);

        final Spinner teamSpinner = (Spinner) parent.findViewById(R.id.team_spinner);
        ArrayAdapter<CharSequence> teamAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinnerTeams, android.R.layout.simple_spinner_item);

        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(teamAdapter);

        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (weekSpinner.isEnabled() && !programmatic) {
                    if (position + 1 != showing) {
                        Log.e("EAGLES","Changing week");
                        teamSpinner.setEnabled(false);
//                        programmatic = true;
                        teamSpinner.setSelection(0);

                        teamShowing = 0;
                        showing = position + 1;

                        adapter.clearItems();
                        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(getActivity());

                        final List<Fixture> fixtures;
                        if (position == 17) {
                            fixtures = db.getPostseasonFixtures();
                        } else {
                            fixtures = db.getFixturesForWeek(position + 1);
                        }

                        ScheduleViewHelper.displayList(adapter, fixtures, true);

                        teamSpinner.setEnabled(true);
                    }
                } else {
                    programmatic = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (teamSpinner.isEnabled()) {
                    if (position != teamShowing) {
                        Log.e("EAGLES","Changing team");
                        weekSpinner.setEnabled(false);
                        if (showing != 1) programmatic = true;
                        weekSpinner.setSelection(0);

                        teamShowing = position;
                        showing = 1;

                        adapter.clearItems();
                        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(getActivity());

                        final List<Fixture> fixtures;
                        if (position == 0) {
                            fixtures = db.getFixturesForWeek(1);
                        } else {
                            fixtures = db.getFixturesForTeam(position - 1);
                        }

                        ScheduleViewHelper.displayList(adapter, fixtures, false);

                        weekSpinner.setEnabled(true);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        displaySchedule();

        UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
        if (db.needsUpdate("Schedule")) {
            doScheduleFetch();
        }

        teamShowing = 0;
        return parent;
    }

    public void refreshSchedule() {
        if (!refreshing) {
            doScheduleFetch();
        }
    }

    public void finished() {
        refreshing = false;
        pullLayout.setRefreshing(false);
    }

    private void doScheduleFetch() {
        if (!refreshing) {
            refreshing = true;
            pullLayout.setRefreshing(true);
            if (ContentFetcher.isScheduleSyncing()) {
                BaseWaiter waiter = new ScheduleWaiter(getActivity(), null, adapter, weekSpinner, true);
                waiter.startWaiting();
            } else {
                FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), false, null);
                fetcherPackage.setSource(this);
                fetcherPackage.setScheduleAdapter(adapter);
                ContentFetcher.fetchSchedules(fetcherPackage);
            }

            showing = 1;
        }
    }

    private void displaySchedule() {
        ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
        int week = ScheduleParams.getFirstFixture();
        if (week == 0) week = 1;
        weekSpinner.setSelection(week - 1);

        List<Fixture> fixtures = scheduleDB.getFixturesForWeek(week);
        ScheduleViewHelper.displayList(adapter, fixtures, true);

        showing = week;
    }

}