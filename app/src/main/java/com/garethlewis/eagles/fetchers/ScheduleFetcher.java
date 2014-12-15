package com.garethlewis.eagles.fetchers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.garethlewis.eagles.FetcherPackage;
import com.garethlewis.eagles.util.ContentFetcher;
import com.garethlewis.eagles.util.FileHandler;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.util.ScheduleParams;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.database.entities.Fixture;
import com.garethlewis.eagles.fragments.schedule.ScheduleViewHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleFetcher extends AsyncTask<FetcherPackage, Void, List<Fixture>> {

////    private static final String baseUrl = "http://espn.go.com/nfl/schedule/_/year/";
//    private static final String baseUrl = "http://www1.skysports.com/nfl/fixtures-results/";
    private static final String baseUrl = "http://www.nfl.com/schedules/";
    private int year = 2014;
//    private static final String typeString = "/seasontype/";
//    private String type = "2"; // 1 for preseason, 2 for regular season, 3 for postseason
//    private static final String weekString = "/week-";
    private String week;

//    private String date;

    private FetcherPackage input;

    @Override
    protected List<Fixture> doInBackground(FetcherPackage... fetcherPackages) {

        Log.e("EAGLES", "GETTING SCHEDULES");

        this.input = fetcherPackages[0];

//        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(input.getContext());
//        db.deleteAllFixtures();
//
//        StandingsSQLiteHelper dbs = StandingsSQLiteHelper.getInstance(input.getContext());
//        dbs.deleteAllStandings();

        String[] allTeams = input.getContext().getResources().getStringArray(R.array.team_nicknames);

        List<Fixture> fixtures = new ArrayList<Fixture>();

        int start = ScheduleParams.getFirstFixture();
        if (start == 0) start = 1;

        try {
            for (int w = start; w < 18; w++) {
                week = "" + w;
                Log.e("EAGLES", week);
                String url = getUrl();
                Document doc;
                try {
                    doc = Jsoup.connect(url).timeout(10000).get();
                } catch (SocketTimeoutException e) {
                    continue;
                }
                Elements tables = doc.select(".schedules-table");

                Elements fullSchedule;
                if (tables.size() > 1) { // Current/future game week. tables.first() has the next game.
                    fullSchedule = tables.get(1).children();
                } else {
                    fullSchedule = tables.first().children();
                }

                List<String> teams = new ArrayList<String>(32);
                int gameCount = 0;

                String date = "";
                int size = fullSchedule.size();
                for (int i = 0; i < size; i++) {
                    Element row = fullSchedule.get(i);
                    if (row.hasClass("schedules-list-date")) { // Date row.
                        date = row.child(0).child(0).text();
                        String[] dateParts = date.split(", ");
                        dateParts[0] = dateParts[0].substring(0, 3);
                        String day = dateParts[1].split(" ")[1];
                        dateParts[1] = dateParts[1].substring(0, 3);

                        int tempYear = year;
                        if ("jan".equals(dateParts[1].toLowerCase())) {
                            tempYear++;
                        }

                        date = dateParts[0] + ", " + dateParts[1] + " " + day + " " + tempYear;

                    } else { // Game row. More concrete check available with class schedules-list-matchup
                        row = row.child(0).child(1); // Get time before changing the row pointer.
                        String time = row.child(0).child(0).text();
                        if (!"FINAL".equals(time)) time += " PM";
                        else time = "5:32 AM";

                        row = row.child(2).child(0);
                        String awayTeam = row.child(0).text();
                        String homeTeam;
                        int awayScore = -1;
                        int homeScore = -1;
                        int status = 0;

                        if (row.children().size() == 5) { // Future fixture.
                            if (ScheduleParams.getFirstFixture() == 0 || w < ScheduleParams.getFirstFixture()) {
                                ScheduleParams.setFirstFixture(w);
                                long epoch = Fixture.dateStringToEpoch(date + " " + time);
                                if (ScheduleParams.getNextGameTime() < epoch) {
                                    ScheduleParams.setNextGameTime(epoch);
                                }

                            }
                            homeTeam = row.child(4).text();
                        } else { // Either result or in-progress depending on time.
                            homeTeam = row.child(5).text();
                            awayScore = Integer.parseInt(row.child(2).text());
                            homeScore = Integer.parseInt(row.child(3).text());
                            if ("5:32 AM".equals(time)) { // Result
                                status = 2;
                                ScheduleParams.setLastResult(w);
                            } else { // In-progress.
                                if (ScheduleParams.getFirstFixture() == 0 || w < ScheduleParams.getFirstFixture()) {
                                    ScheduleParams.setFirstFixture(w);
                                }
                                status = 1;
                            }
                        }

                        homeTeam = camelCaseString(homeTeam);
                        awayTeam = camelCaseString(awayTeam);

                        teams.add(homeTeam);
                        teams.add(awayTeam);
                        gameCount++;

                        Fixture fixture = new Fixture(homeTeam, awayTeam, homeScore, awayScore, date, time, week, status);
                        fixtures.add(fixture);
                    }
                }

                if (gameCount != 16) {
                    // There are bye weeks. Need to figure out which teams have not been included in fixtures this week.
                    for (int i = 0; i < 32; i++) {
                        if (!teams.contains(allTeams[i])) {
                            Fixture fixture = new Fixture(allTeams[i], "", 0, 0, date, "5:32 AM", week, 2);
                            fixtures.add(fixture);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fixtures;
    }

    @Override
    protected void onPostExecute(List<Fixture> fixtures) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new FileHandler.writeScheduleParams().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        } else {
            new FileHandler.writeScheduleParams().execute();
        }

        Context context = input.getContext();
        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(context);
        db.setContext(context);

        db.insertManyFixtures(fixtures);

        StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(context);
        standingsDB.setContext(context);
        List<Fixture> added = standingsDB.updateStandings(fixtures);

        db.setAdded(added);

        LayoutInflater inflater = input.getInflater();

        if (input.getMode()) {
            fixtures = db.getFixturesForTeam("Eagles");
        } else {
            fixtures = db.getFixturesForWeek(1);
        }

        ScheduleViewHelper.displayList(context, inflater, input.getLinearLayout(), fixtures, true);

        input.getProgress().setVisibility(View.GONE);
        ContentFetcher.scheduleFinished();
    }

    private String getUrl() {
        if (Integer.parseInt(week) > 17) {
            return baseUrl + year + "/POST";
        }
        return baseUrl + year + "/REG" + week;
    }

    private String camelCaseString(String text) {
        String[] parts = text.split(" ");

        String output = "";
        for (String s : parts) {
            String tmp = s.toUpperCase();
            output += tmp.replace(tmp.substring(1), s.substring(1).toLowerCase());
            output += " ";
        }

        return output.trim();
    }

    private String convertTimeTo12Hour(String time) {
        int hour = Integer.parseInt(time.substring(0,2));
        if (hour > 12) {
            hour -= 12;
            return hour + time.substring(2) + " PM";
        }

        return time + " AM";
    }

    private boolean isInFuture(String date, String time) {
        long epoch = Fixture.dateStringToEpoch(date + " " + time);
        long now = new Date().getTime();

        return (Long.compare(epoch, now) > 0);
    }

    private int getNumberOfMatchesForPostseasonWeek(int week) {
        switch (week) {
            case 18:
                return 4;
            case 19:
                return 4;
            case 20:
                return 2;
            case 21:
                return 1;
            case 22:
                return 1;
            default:
                return 0;
        }
    }
}
