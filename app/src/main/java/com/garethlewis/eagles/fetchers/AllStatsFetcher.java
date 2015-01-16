package com.garethlewis.eagles.fetchers;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class AllStatsFetcher extends AsyncTask<Void, Void, Void> {
    // URL = http://espn.go.com/nfl/team/stats/_/name/phi/year/2014/seasontype/2/type/player
    private static final String baseUrl = "http://espn.go.com/nfl/team/stats/_/name/";
    private static final String yearText = "/year/";
    private static final String seasonText = "/seasontype/";
    private static final String typeText = "/type/player";

    @Override
    protected Void doInBackground(Void... params) {
        int year = 2014;
        int season = 2;

        try {
            String url = makeUrl("phi", "" + year, "" + season);

            Document doc;
            try {
                doc = Jsoup.connect(url).timeout(10000).get();
            } catch (SocketTimeoutException e) {
                return null;
            }

            Elements tables = doc.select(".tablehead"); // get(0) = passing, (1) = rushing, (2) = receiving, (3) = defense
                                                        // get(4) = scoring, (5) = returning, (6) = kicking, (7) = punting

            String[][] headers = new String[][] {
                    {"CMP", "ATT", "YDS", "TD", "INT", "YPA", "PCT", "YPG", "SACK", "RTG"},
                    {"ATT", "YDS", "AVG", "TD", "FUM", "YPG", "1DN", "20+", "40+"},
                    {"REC", "YDS", "AVG", "TD", "FUM", "LNG", "YPG", "1DN", "20+", "40+"},
                    {"SOLO", "AST", "TTL", "SACK", "FF", "INT", "YDS", "LNG", "TD"},
                    {},
                    {"RET", "YDS", "AVG", "LNG", "TD", "FUM", "20+", "40+", "FC"},
                    {"FG", "FG%", "LNG", "XP", "XP%", "1-19", "20-29", "30-39", "40-49", "50+"},
                    {"ATT", "YDS", "AVG", "LNG", "NET", "BP", "IN20", "TB", "FC", "RET", "RETY", "AVG"}
            };


        } catch (IOException e) {

        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
    }

    private String makeUrl(String tricode, String year, String season) {
        return baseUrl + tricode + yearText + year + seasonText + season + typeText;
    }
}
