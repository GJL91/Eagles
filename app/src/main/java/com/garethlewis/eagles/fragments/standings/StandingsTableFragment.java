package com.garethlewis.eagles.fragments.standings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.R;

public class StandingsTableFragment extends android.support.v4.app.Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private int position = 0;

    public StandingsTableFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(StandingsTableFragment.ARG_SECTION_NUMBER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.fragment_standings_table, container, false);

        LinearLayout namesLayout = (LinearLayout) parent.findViewById(R.id.name_layout);
        createNamesLayout(inflater, namesLayout);

        LinearLayout statsLayout = (LinearLayout) parent.findViewById(R.id.stats_layout);
        createStatsLayout(inflater, statsLayout);

        return parent;
    }

    private void createNamesLayout(LayoutInflater inflater, LinearLayout layout) {
        for (int i = 0; i < 40; i++) {
            TextView view = (TextView) inflater.inflate(R.layout.standings_table_name_item, null, false);
            view.setText("" + i);

            layout.addView(view);
        }
    }

    private void createStatsLayout(LayoutInflater inflater, LinearLayout layout) {
        for (int i = 0; i < 40; i++) {
            View view = inflater.inflate(R.layout.standings_table_stats_item, null, false);

            TextView textView = (TextView) view.findViewById(R.id.wins_text);
            textView.setText("0");

            textView = (TextView) view.findViewById(R.id.losses_text);
            textView.setText("0");

            textView = (TextView) view.findViewById(R.id.ties_text);
            textView.setText("0");

            layout.addView(view);
        }
    }
}