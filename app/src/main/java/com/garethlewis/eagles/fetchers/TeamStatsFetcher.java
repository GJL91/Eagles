package com.garethlewis.eagles.fetchers;

import android.os.AsyncTask;
import android.util.Log;

import com.garethlewis.eagles.adapters.AllStatsListAdapter;
import com.garethlewis.eagles.adapters.StatsNameListAdapter;
import com.garethlewis.eagles.fragments.team.AllStatsViewHelper;
import com.garethlewis.eagles.fragments.team.TeamFragment;
import com.garethlewis.eagles.util.FetcherPackage;
import com.garethlewis.eagles.util.TeamHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class TeamStatsFetcher extends AsyncTask<FetcherPackage, Void, List<List<List<String>>>> {

    private FetcherPackage fetcherPackage;

    // URL = http://espn.go.com/nfl/team/stats/_/name/phi/year/2014/seasontype/2/type/player
    private static final String baseUrl = "http://espn.go.com/nfl/team/stats/_/name/";
    private static final String yearText = "/year/";
    private static final String seasonText = "/seasontype/";
    private static final String typeText = "/type/player";

    @Override
    protected List<List<List<String>>> doInBackground(FetcherPackage... params) {

        Log.e("EAGLES", "In stats fetcher");

        fetcherPackage = params[0];

        String team = "phi";
        int teamNumber = fetcherPackage.getExtra();
        if (teamNumber != -1) {
            team = TeamHelper.getTriCode(teamNumber).toLowerCase();
        }

        int year = 2014;
        int season = 2;

        try {
            String url = makeUrl(team, "" + year, "" + season);

            Document doc;
            try {
                doc = Jsoup.connect(url).timeout(10000).get();
            } catch (SocketTimeoutException e) {
                return null;
            }

            Elements tables = doc.select(".tablehead"); // get(0) = passing, (1) = rushing, (2) = receiving, (3) = defense
                                                        // get(4) = scoring, (5) = returning, (6) = kicking, (7) = punting

            List<List<List<String>>> stats = new ArrayList<>();

            int[][] columns = new int[][] {
                    {0,2,1,4,8,10,5,3,6,12,14},
                    {0,1,2,3,6,8,7,10,5},
                    {0,1,3,4,5,9,6,8,12,7},
                    {0,1,2,3,4,12,8,9,10,11,14},
                    {},
                    {0,1,2,3,4,5,6,7,8,9,10,11},
                    {0,1,2,3,4,10,11,12,5,6,7,8,9},
                    {0,1,2,4,3,5,6,7,8,9,10,11,12},
            };

            for (int i = 0; i < 8; i++) {
                if (i == 4) { // We don't care about the scoring charts for now.
                    continue;
                }

                Elements tableRows = tables.get(i).child(0).children(); // child(0) = <tbody>
                List<List<String>> tableStats = new ArrayList<>();

                int count = tableRows.size();
                for (int j = 0; j < count; j++) {
                    Element row = tableRows.get(j);
                    if (!row.hasClass("oddrow") && !row.hasClass("evenrow")) {
                        continue;
                    }

                    List<String> rowStats = new ArrayList<>();
                    for (int k = 0; k < columns[i].length; k++) {
                        rowStats.add(row.child(columns[i][k]).text());
                    }

                    tableStats.add(rowStats);
                }

                stats.add(tableStats);
            }

            return stats;
        } catch (IOException e) {
            // Do nothing.
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<List<List<String>>> stats) {
        if (stats == null) {
            // Error handling here.
            Log.e("EAGLES", "ERROR in fetching");

            return;
        }

        // Split the returning list into two
        List<List<String>> returners = stats.get(4);

        List<List<String>> kickReturners = new ArrayList<>();
        List<List<String>> puntReturners = new ArrayList<>();

        int count = returners.size();

        for (int i = 0; i < count; i++) {
            List<String> kickReturnersStats = new ArrayList<>();
            List<String> puntReturnersStats = new ArrayList<>();

            List<String> row = returners.get(i);
            boolean kickReturner = !"0".equals(row.get(1));
            boolean puntReturner = !"0".equals(row.get(6));

            if (puntReturner) {
                puntReturnersStats.add(row.get(0));
            }

            for (int j = 0; j < 6; j++) {
                if (kickReturner) {
                    kickReturnersStats.add(row.get(j));
                }

                if (puntReturner) {
                    puntReturnersStats.add(row.get(j + 6));
                }
            }

            if (kickReturner) {
                kickReturners.add(kickReturnersStats);
            }

            if (puntReturner) {
                puntReturners.add(puntReturnersStats);
            }
        }

        stats.remove(4);
        stats.add(4, kickReturners);
        stats.add(5, puntReturners);

        // Combine some kicker stats.
        List<List<String>> kickers = stats.get(6);
        count = kickers.size();

        List<List<String>> newKickers = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            List<String> newKicker = new ArrayList<>();
            List<String> kicker = kickers.get(i);

            String att = kicker.get(1) + "/" + kicker.get(2);
            String xp = kicker.get(5) + "/" + kicker.get(6);

            newKicker.add(kicker.get(0));
            newKicker.add(att);
            newKicker.add(kicker.get(3));
            newKicker.add(kicker.get(4));
            newKicker.add(xp);
            newKicker.add(kicker.get(7));
            newKicker.add(kicker.get(8));
            newKicker.add(kicker.get(9));
            newKicker.add(kicker.get(10));
            newKicker.add(kicker.get(11));
            newKicker.add(kicker.get(12));

            newKickers.add(newKicker);
        }

        stats.remove(6);
        stats.add(6, newKickers);

        // TODO: Need to save the stats into a TeamStats database table.


        StatsNameListAdapter[] nameListAdapters = fetcherPackage.getStatsNameAdapters();
        AllStatsListAdapter[] allStatsListAdapters = fetcherPackage.getAllStatsAdapters();
        for (int i = 0; i < nameListAdapters.length; i++) {
            AllStatsViewHelper.displayTable(nameListAdapters[i], allStatsListAdapters[i], stats.get(i), i);
        }

        ((TeamFragment) fetcherPackage.getSource()).finish();
    }

    private String makeUrl(String tricode, String year, String season) {
        return baseUrl + tricode + yearText + year + seasonText + season + typeText;
    }
}
