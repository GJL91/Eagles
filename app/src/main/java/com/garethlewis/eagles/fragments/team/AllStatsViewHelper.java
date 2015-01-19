package com.garethlewis.eagles.fragments.team;

import com.garethlewis.eagles.adapters.AllStatsListAdapter;
import com.garethlewis.eagles.adapters.StatsNameListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllStatsViewHelper {

    private static final String[][] headers = new String[][] {
            {"CMP", "ATT", "YDS", "TD", "INT", "AVG", "PCT", "YPG", "SACK", "RTG"},
            {"ATT", "YDS", "AVG", "TD", "FUM", "YPG", "1DN", "20+"},
            {"REC", "YDS", "AVG", "TD", "FUM", "LNG", "YPG", "1DN", "20+"},
            {"SOLO", "AST", "TTL", "SACK", "FF", "INT", "YDS", "LNG", "TD"},
            {"RET", "YDS", "AVG", "LNG", "TD"},
            {"RET", "YDS", "AVG", "LNG", "TD", "FC"},
            {"FG", "FG%", "LNG", "XP", "XP%", "1-19", "20-29", "30-39", "40-49", "50+"},
            {"ATT", "YDS", "AVG", "LNG", "NET", "BP", "IN20", "TB", "FC", "RET", "RETY", "AVG"}
    };

    public static void displayTable(StatsNameListAdapter nameAdapter, AllStatsListAdapter statsAdapter, List<List<String>> stats, int tableType) {

        nameAdapter.clear();
        statsAdapter.clear();

        nameAdapter.addHeader();
        statsAdapter.addHeader(new ArrayList<>(Arrays.asList(headers[tableType])));

        for (List<String> stat : stats) {
            nameAdapter.addItem(stat.get(0));
            statsAdapter.addItem(stat);
        }

        nameAdapter.setHeight();
        statsAdapter.setHeight();
    }

}
