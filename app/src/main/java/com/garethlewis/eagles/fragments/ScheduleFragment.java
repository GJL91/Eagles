package com.garethlewis.eagles.fragments;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.parsers.ScheduleParser;

public class ScheduleFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        LinearLayout view = (LinearLayout) parent.findViewById(R.id.fixtures_list);

        ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, view, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new ScheduleParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
        } else {
            new ScheduleParser().execute(parserPackage);
        }

        return parent;
    }

}