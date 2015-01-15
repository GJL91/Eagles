package com.garethlewis.eagles.fragments.standings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.adapters.StandingsNamesListAdapter;
import com.garethlewis.eagles.adapters.StandingsStatsListAdapter;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.entities.Standing;
import com.garethlewis.eagles.util.TeamHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        List<Standing> stats = new ArrayList<>();

        ListView namesList = (ListView) parent.findViewById(R.id.standings_name_layout);
        StandingsNamesListAdapter namesAdapter = new StandingsNamesListAdapter(getActivity(), new ArrayList<String>());

        View header = inflater.inflate(R.layout.standings_table_blank_name_item, namesList, false);
        namesList.addHeaderView(header);

        namesList.setAdapter(namesAdapter);

        ListView statsList = (ListView) parent.findViewById(R.id.standings_stats_layout);
        StandingsStatsListAdapter statsAdapter = new StandingsStatsListAdapter(getActivity(), new ArrayList<Standing>());

        header = inflater.inflate(R.layout.standings_table_blank_stats_item, statsList, false);
        statsList.addHeaderView(header);

        statsList.setAdapter(statsAdapter);

        if (position == 0) {
            int[][] divTeams = TeamHelper.getAllDivisionTeams(getActivity());
            stats = calculateDivisionalTables(divTeams, stats);
        } else {
            int[][] confTeams = TeamHelper.getAllConferenceTeams(getActivity());
            stats = calculateConferenceTables(confTeams, stats);
        }

        createTables(namesAdapter, statsAdapter, stats, position == 1);

        namesAdapter.setHeight(namesList);
        statsAdapter.setHeight(statsList);

        return parent;
    }

    private void createTables(StandingsNamesListAdapter namesAdapter, StandingsStatsListAdapter statsAdapter, List<Standing> stats, boolean conference) {
        String[] titles;
        if (conference) {
            titles = new String[] {"NFC", "AFC"};
        } else {
            titles = new String[] {
                    "NFC East", "NFC North", "NFC South", "NFC West",
                    "AFC East", "AFC North", "AFC South", "AFC West"
            };
        }

        int count = stats.size();
        int modulo = conference ? 17 : 5;
        for (int i = 0; i < count; i++) {
            Standing standing = stats.get(i);
            if (standing == null) {
                namesAdapter.addHeader(titles[i / modulo]);
                statsAdapter.addHeader();
            } else {
                namesAdapter.addTeam(standing.getName());
                statsAdapter.addStanding(standing);
            }
        }
    }

    private List<Standing> calculateDivisionalTables(int[][] divTeams, List<Standing> stats) {
        StandingsSQLiteHelper db = StandingsSQLiteHelper.getInstance(getActivity());
        Standing[] tableStandings = db.getAllTeams();

        String[] teamNames = getResources().getStringArray(R.array.team_nicknames);
        for (int i = 0; i < 32; i++) {
            tableStandings[i].setName(teamNames[i]);
        }

        List<String> divisionalChampions = new ArrayList<>();

        stats.add(null);
        for (int i = 0; i < 8; i++) {
            Standing[] temp = new Standing[4];
            for (int j = 0; j < 4; j++) temp[j] = tableStandings[divTeams[i][j]];

            Arrays.sort(temp);
            divisionalChampions.add(temp[0].getName());

            stats.addAll(Arrays.asList(temp));
            if (i != 7) {
                stats.add(null);
            }
        }

        TeamHelper.setDivisionalChampions(divisionalChampions);

        return stats;
    }

    private List<Standing> calculateConferenceTables(int[][] confTeams, List<Standing> stats) {
        StandingsSQLiteHelper db = StandingsSQLiteHelper.getInstance(getActivity());
        Standing[] tableStandings = db.getAllTeams();

        String[] teamNames = getResources().getStringArray(R.array.team_nicknames);
        for (int i = 0; i < 32; i++) {
            tableStandings[i].setName(teamNames[i]);
        }

        List<String> divisionChamps = TeamHelper.getDivisionalChampions();
        if (divisionChamps == null) {
            int[][] divTeams = TeamHelper.getAllDivisionTeams(getActivity());
            calculateDivisionalTables(divTeams, new ArrayList<Standing>());
            divisionChamps = TeamHelper.getDivisionalChampions();
        }

        stats.add(null);
        for (int i = 0; i < 2; i++) {
            Standing[] temp = new Standing[12];
            Standing[] leaders = new Standing[4];
            int teamIndex = 0;
            int leadersIndex = 0;
            for (int j = 0; j < 16; j++) {
                Standing team = tableStandings[confTeams[i][j]];
                if (!divisionChamps.contains(team.getName())) {
                    temp[teamIndex++] = team;
                } else {
                    leaders[leadersIndex++] = team;
                }
            }

            Arrays.sort(leaders);
            Arrays.sort(temp);

            stats.addAll(Arrays.asList(leaders));
            stats.addAll(Arrays.asList(temp));

            if (i == 0) {
                stats.add(null);
            }
        }

        return stats;
    }
}