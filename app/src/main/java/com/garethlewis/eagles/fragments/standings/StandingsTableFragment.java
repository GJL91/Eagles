package com.garethlewis.eagles.fragments.standings;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.util.TeamHelper;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.database.entities.Standing;

import java.util.Arrays;

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

        int[][] divTeams = TeamHelper.getAllDivisionTeams(getActivity());
        Standing[] standings = calculateTables(divTeams);

        LinearLayout namesLayout = (LinearLayout) parent.findViewById(R.id.name_layout);
        createNamesLayout(inflater, namesLayout, standings);

        LinearLayout statsLayout = (LinearLayout) parent.findViewById(R.id.stats_layout);
        createStatsLayout(inflater, statsLayout, standings);

        return parent;
    }

    private void createNamesLayout(LayoutInflater inflater, LinearLayout layout, Standing[] standings) {
        String[] titles = new String[] {"NFC East", "NFC North", "NFC South", "NFC West",
                                        "AFC East", "AFC North", "AFC South", "AFC West"};

//        String[] teamNames = getResources().getStringArray(R.array.spinnerTeams);

        for (int i = 0; i < 40; i++) {
            TextView view = (TextView) inflater.inflate(R.layout.standings_table_name_item, null, false);
            if (i % 5 == 0) {
                view.setText(titles[i / 5]);
                view.setTypeface(null, Typeface.BOLD);
                view.setBackgroundColor(getResources().getColor(R.color.grey));
//                view.setGravity(Gravity.CENTER);

                int padding = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
//                Log.e("EAGLES", "Padding in pixels = " + padding);
//
//                view.setPadding(0,padding,0,padding);
                view.setPadding(55,padding,0,padding);
            } else {
//                int index = divTeams[i / 5][(i % 5) - 1] + 1;
                view.setText((i % 5) + "  " + standings[i].getName());
            }

            layout.addView(view);
        }
    }

    private void createStatsLayout(LayoutInflater inflater, LinearLayout layout, Standing[] standings) {
        for (int i = 0; i < 40; i++) {
            View view = inflater.inflate(R.layout.standings_table_stats_item, null, false);

            if (i % 5 == 0) {
                setupHeader(view);
            } else {
                TextView textView = (TextView) view.findViewById(R.id.wins_text);
                textView.setText("" + standings[i].getWins());

                textView = (TextView) view.findViewById(R.id.losses_text);
                textView.setText("" + standings[i].getLosses());

                textView = (TextView) view.findViewById(R.id.ties_text);
                textView.setText("" + standings[i].getTies());

                float pct = (float) standings[i].getWins() /  (float) (standings[i].getWins() + standings[i].getLosses() + standings[i].getTies());
                textView = (TextView) view.findViewById(R.id.percent_text);
                textView.setText(String.format("%.3f", pct));

                textView = (TextView) view.findViewById(R.id.home_text);
                textView.setText("" + standings[i].getHomeRecord());

                textView = (TextView) view.findViewById(R.id.road_text);
                textView.setText("" + standings[i].getRoadRecord());

                textView = (TextView) view.findViewById(R.id.division_text);
                textView.setText("" + standings[i].getDivisionRecord());

                textView = (TextView) view.findViewById(R.id.conference_text);
                textView.setText("" + standings[i].getConferenceRecord());

                textView = (TextView) view.findViewById(R.id.points_for_text);
                textView.setText("" + standings[i].getPointsFor());

                textView = (TextView) view.findViewById(R.id.points_against_text);
                textView.setText("" + standings[i].getPointsAgainst());

                int diff = standings[i].getPointsFor() - standings[i].getPointsAgainst();
                textView = (TextView) view.findViewById(R.id.diff_text);
                textView.setText("" + diff);

                textView = (TextView) view.findViewById(R.id.streak_text);
                textView.setText("" + standings[i].getStreak());
            }

            layout.addView(view);
        }
    }

    private void setupHeader(View view) {
        int grey = getResources().getColor(R.color.grey);

        TextView textView = (TextView) view.findViewById(R.id.wins_text);
        textView.setText("W");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.losses_text);
        textView.setText("L");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.ties_text);
        textView.setText("T");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.percent_text);
        textView.setText("PCT");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.home_text);
        textView.setText("HOME");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.road_text);
        textView.setText("ROAD");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.division_text);
        textView.setText("DIV");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.conference_text);
        textView.setText("CONF");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.points_for_text);
        textView.setText("PF");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.points_against_text);
        textView.setText("PA");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.diff_text);
        textView.setText("DIFF");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);

        textView = (TextView) view.findViewById(R.id.streak_text);
        textView.setText("STRK");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setBackgroundColor(grey);
    }

    private Standing[] calculateTables(int[][] divTeams) {
        StandingsSQLiteHelper db = StandingsSQLiteHelper.getInstance(getActivity());
        Standing[] sortedStandings = new Standing[40];
        Standing[] tableStandings = db.getAllTeams();

//        String[] teamNames = getResources().getStringArray(R.array.spinnerTeams);
//        for (int i = 0; i < 32; i++) {
//            tableStandings[i].setName(teamNames[i+1]);
//        }

        String[] teamNames = getResources().getStringArray(R.array.team_nicknames);
        for (int i = 0; i < 32; i++) {
            tableStandings[i].setName(teamNames[i]);
        }

        int index = 1;
        for (int i = 0; i < 8; i++) {
            Standing[] temp = new Standing[4];
            for (int j = 0; j < 4; j++) temp[j] = tableStandings[divTeams[i][j]];

            Arrays.sort(temp);

            for (int j = 0; j < 4; j++) {
                sortedStandings[index++] = temp[j];
            }
            index++;
        }

        return sortedStandings;
    }
}