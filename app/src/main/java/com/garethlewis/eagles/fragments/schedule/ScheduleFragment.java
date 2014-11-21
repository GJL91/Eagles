package com.garethlewis.eagles.fragments.schedule;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.database.entities.Fixture;
import com.garethlewis.eagles.parsers.ScheduleParser;

import java.util.List;

public class ScheduleFragment extends Fragment {

    private LinearLayout fixturesList;
    private int showing = 1;
    private int teamShowing = 0;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.schedule_main, container, false);

        final Spinner weekSpinner = (Spinner) parent.findViewById(R.id.week_spinner);
        ArrayAdapter<CharSequence> weekAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weekHeadings, android.R.layout.simple_spinner_item);

        weekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSpinner.setAdapter(weekAdapter);
        weekSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (weekSpinner.isEnabled()) {
                    if (position + 1 != showing) {
                        if (position == 17) {
                            fixturesList.removeAllViews();
                            ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(getActivity());
                            List<Fixture> fixtures = db.getPostseasonFixtures();
                            ScheduleViewHelper.displayList(getActivity(), inflater, fixturesList, fixtures, false);

                            teamShowing = 0;
                            showing = position + 1;
                            return;
                        }

                        fixturesList.removeAllViews();
                        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(getActivity());
                        List<Fixture> fixtures = db.getFixturesForWeek(position + 1);
                        ScheduleViewHelper.displayList(getActivity(), inflater, fixturesList, fixtures, true);

                        teamShowing = 0;
                        showing = position + 1;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Spinner teamSpinner = (Spinner) parent.findViewById(R.id.team_spinner);
        ArrayAdapter<CharSequence> teamAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinnerTeams, android.R.layout.simple_spinner_item);

        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(teamAdapter);
        teamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != teamShowing) {
                    if (position == 0) {
                        fixturesList.removeAllViews();
                        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(getActivity());
                        List<Fixture> fixtures = db.getFixturesForWeek(1);

                        ScheduleViewHelper.displayList(getActivity(), inflater, fixturesList, fixtures, true);
                        weekSpinner.setSelection(0);
                        weekSpinner.setEnabled(true);

                        teamShowing = 0;
                        showing = 1;
                        return;
                    }

                    weekSpinner.setEnabled(false);
                    weekSpinner.setSelection(0);

                    fixturesList.removeAllViews();
                    ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(getActivity());
                    List<Fixture> fixtures = db.getFixturesForTeam(getResources().getStringArray(R.array.spinnerTeams)[position]);
                    ScheduleViewHelper.displayList(getActivity(), inflater, fixturesList, fixtures, false);

                    teamShowing = position;
                    showing = position + 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        LinearLayout view = (LinearLayout) parent.findViewById(R.id.fixtures_list);
        fixturesList = view;

        UpdatedSQLiteHelper db = UpdatedSQLiteHelper.getInstance(getActivity());
        if (db.needsUpdate("Schedule")) {
            ProgressBar progressBar = (ProgressBar) parent.findViewById(R.id.schedule_progress);
            progressBar.setVisibility(View.VISIBLE);

            ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, view, progressBar, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new ScheduleParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
            } else {
                new ScheduleParser().execute(parserPackage);
            }

        } else {
            ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
            List<Fixture> fixtures = scheduleDB.getFixturesForWeek(1);

            ScheduleViewHelper.displayList(getActivity(), inflater, fixturesList, fixtures, true);
        }

        teamShowing = 0;
        showing = 1;

        return parent;
    }

}