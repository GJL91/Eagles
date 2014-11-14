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
    private int showing;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.schedule_main, container, false);

        Spinner teamSpinner = (Spinner) parent.findViewById(R.id.team_spinner);
        ArrayAdapter<CharSequence> teamAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.spinnerTeams, android.R.layout.simple_spinner_item);
        teamAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamSpinner.setAdapter(teamAdapter);

        Spinner spinner = (Spinner) parent.findViewById(R.id.week_spinner);
//        new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, values);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.weekHeadings, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position + 1 != showing) {
                    fixturesList.removeAllViewsInLayout();
                    ScheduleSQLiteHelper db = new ScheduleSQLiteHelper(getActivity());
                    List<Fixture> fixtures = db.getFixturesForWeek(position + 1);
                    for (Fixture f : fixtures) {
                        View fixtureView = ScheduleViewHelper.setupViewForFixture(getActivity(), inflater, f, true);
                        fixturesList.addView(fixtureView);
                    }
                    showing = position + 1;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        LinearLayout view = (LinearLayout) parent.findViewById(R.id.fixtures_list);
        fixturesList = view;

        UpdatedSQLiteHelper db = new UpdatedSQLiteHelper(getActivity());
        if (db.needsUpdate("Schedule")) {

            ProgressBar progressBar = (ProgressBar) parent.findViewById(R.id.schedule_progress);

            ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, view, progressBar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new ScheduleParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
            } else {
                new ScheduleParser().execute(parserPackage);
            }
        } else {
            ScheduleSQLiteHelper scheduleDB = new ScheduleSQLiteHelper(getActivity());
            List<Fixture> fixtures = scheduleDB.getFixturesForWeek(1);
            for (Fixture f : fixtures) {
                View fixtureView = ScheduleViewHelper.setupViewForFixture(getActivity(), inflater, f, true);
                fixturesList.addView(fixtureView);
            }
        }

        showing = 1;

        return parent;
    }

}