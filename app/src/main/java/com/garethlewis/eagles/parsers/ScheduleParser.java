package com.garethlewis.eagles.parsers;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.garethlewis.eagles.FileHandler;
import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.ScheduleParams;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.database.entities.Fixture;
import com.garethlewis.eagles.fragments.schedule.ScheduleViewHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ScheduleParser extends AsyncTask<ParserPackage, Void, List<Fixture>> {

//    private static final String baseUrl = "http://espn.go.com/nfl/schedule/_/year/";
    private static final String baseUrl = "http://www1.skysports.com/nfl/fixtures-results/";
    private int year = 2014;
    private static final String typeString = "/seasontype/";
    private String type = "2"; // 1 for preseason, 2 for regular season, 3 for postseason
    private static final String weekString = "/week-";
    private String week;

//    private String date;

    private ParserPackage input;

    @Override
    protected List<Fixture> doInBackground(ParserPackage... parserPackages) {

        Log.e("EAGLES", "GETTING SCHEDULES");

        this.input = parserPackages[0];

//        ScheduleSQLiteHelper db = new ScheduleSQLiteHelper(input.getContext());
//        db.deleteAllFixtures();
//
//        StandingsSQLiteHelper dbs = new StandingsSQLiteHelper(input.getContext());
//        dbs.deleteAllStandings();

        String[] allTeams = new String[32];
        String[] spinnerTeams = input.getContext().getResources().getStringArray(R.array.spinnerTeams);
        for (int i = 0; i < 32; i++) {
            allTeams[i] = spinnerTeams[i + 1];
        }

        List<Fixture> fixtures = new ArrayList<Fixture>();

        int start = ScheduleParams.getFirstFixture();
        if (start == 0) start = 1;

        try {
            for (int w = 1; w < 23; w++) {
                week = "" + w;
                Log.e("EAGLES", week);
                String url = getUrl();
                Document doc = Jsoup.connect(url).get();
                Elements mainContent = doc.select("div[id$=content]").first().child(1).child(0).children();
                if (w > 17) {
                    if (mainContent.get(1).text().startsWith("Week")) {
                        int nMatches = getNumberOfMatchesForPostseasonWeek(w);
                        for (int i = 0; i < nMatches; i++) {
                            String homeTeam = "TBC";
                            String awayTeam = "TBC";

                            String date = "Thu, Jan 1 1970";
                            String time = "12:01 AM";

                            int homeScore = -1;
                            int awayScore = -1;

                            Fixture fixture = new Fixture(homeTeam, awayTeam, homeScore, awayScore, date, time, week);
                            fixtures.add(fixture);
                        }

                        continue;
                    }
                }

                String lastDate = "";
                List<String> teams = new ArrayList<String>(32);

                mainContent = mainContent.get(2).children();
                int size = mainContent.size();
                for (int i = 0; i < size; i++) {
                    Element element = mainContent.get(i);

                    String time = element.child(0).text();
                    String date = element.child(1).text();

                    String awayTeam = element.child(2).text();
                    String scoreString = element.child(3).text();
                    String homeTeam = element.child(4).text();

                    int homeScore = -1;
                    int awayScore = -1;
                    if (!"@".equals(scoreString)) {
                        ScheduleParams.setLastResult(w);
                        String[] scores = scoreString.split(" - ");
                        awayScore = Integer.parseInt(scores[0]);
                        homeScore = Integer.parseInt(scores[1]);
                    } else {
                        if (ScheduleParams.getFirstFixture() == 0 || w < ScheduleParams.getFirstFixture()) {
                            ScheduleParams.setFirstFixture(w);
                        }
                    }

                    String dateNumber = date.substring(4, 5);
                    try {
                        int secondNumber = Integer.parseInt(date.substring(5, 6));
                        dateNumber += secondNumber;
                    } catch (NumberFormatException e) {
                        // There was no second number in the date. Do nothing.
                    }

                    int tmpYear = year;
                    String month = date.substring(date.length() - 3);
                    if ("jan".equals(month.toLowerCase())) tmpYear++;

                    date = date.substring(0, 3) + ", " + month + " " + dateNumber + " " + tmpYear;
                    lastDate = date;

                    time = convertTimeTo12Hour(time);
                    if (!isInFuture(date, time)) {
                        time = "5:32 AM";
                    }

                    teams.add(homeTeam);
                    teams.add(awayTeam);

                    Fixture fixture = new Fixture(homeTeam, awayTeam, homeScore, awayScore, date, time, week);
                    fixtures.add(fixture);
                }

                if (size != 16) {
                    // There are bye weeks. Need to figure out which teams have not been included in fixtures this week.
                    for (int i = 0; i < 32; i++) {
                        if (!teams.contains(allTeams[i])) {
                            Fixture fixture = new Fixture(allTeams[i], "", 0, 0, lastDate, "5:32 AM", week);
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
//                Elements tableBody = doc.select(".tablehead").first().child(0).children();
//
//                int size = tableBody.size();
//
//                // Get initial date.
//                date = tableBody.get(1).child(0).text() + " " + year;
//
//                for (int i = 2; i < size; i++) {
//                    Element element = tableBody.get(i);
//                    if (!element.hasClass("stathead") && !element.hasClass("colhead")) {
//                        String matchup = element.child(0).text();
//                        //                    Log.e("EAGLES", matchup);
//                        if (matchup.contains(",")) { // Bye week or Result
//                            if (matchup.contains(":")) { // Bye week
//                                String[] teams = matchup.split(",");
//                                teams[0] = teams[0].substring(6);
//
//                                for (String t : teams) {
//                                    Fixture fixture = new Fixture(t.trim(), "", 0, 0, date, "5:32 AM", week);
//                                    fixtures.add(fixture);
//                                }
//
//                            } else { // Result
//                                ScheduleParams.setLastResult(w);
//                                String[] parts = matchup.split(",");
//
//                                String pattern = "(\\D*)(\\d+)(.*)";
//                                String homeTeam = parts[0].replaceAll(pattern, "$1").trim();
//                                int homeScore = Integer.parseInt(parts[0].replaceAll(pattern, "$2").trim());
//                                String awayTeam = parts[1].replaceAll(pattern, "$1").trim();
//                                int awayScore = Integer.parseInt(parts[1].replaceAll(pattern, "$2").trim());
//
//                                Fixture fixture = new Fixture(homeTeam, awayTeam, homeScore, awayScore, date, "5:32 AM", week);
//                                fixtures.add(fixture);
//                            }
//                        } else {  // Fixture is in the future
//                            if (ScheduleParams.getFirstFixture() == 0 || w < ScheduleParams.getFirstFixture()) {
//                                ScheduleParams.setFirstFixture(w);
//                            }
//                            String[] parts = matchup.split(" at ");
//                            String homeTeam = parts[1];
//                            String awayTeam = parts[0];
//
//                            String time = element.child(1).text();
//
//                            Fixture fixture = new Fixture(homeTeam, awayTeam, -1, -1, date, time, week);
//                            fixtures.add(fixture);
//                        }
//                    } else { // Likely a date, check before updating
//                        if (element.hasClass("colhead")) {
//                            date = element.child(0).text() + " " + year;
//                        }
//                    }
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return fixtures;
//    }

    @Override
    protected void onPostExecute(List<Fixture> fixtures) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new FileHandler.writeScheduleParams().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void) null);
        } else {
            new FileHandler.writeScheduleParams().execute();
        }

        Context context = input.getContext();
        ScheduleSQLiteHelper db = new ScheduleSQLiteHelper(context);
        db.setContext(context);

        db.insertManyFixtures(fixtures);

        StandingsSQLiteHelper standingsDB = new StandingsSQLiteHelper(context);
        standingsDB.setContext(context);
        standingsDB.updateStandings(fixtures);

        LayoutInflater inflater = input.getInflater();

        if (input.getMode()) {
            fixtures = db.getFixturesForTeam("Philadelphia");
        } else {
            fixtures = db.getFixturesForWeek(1);
        }

        ScheduleViewHelper.displayList(context, inflater, input.getLinearLayout(), fixtures, true);


//        int i = 0;
//        Resources resources = context.getResources();
//        for (Fixture f : fixtures) {
//            if ("".equals(f.getAwayTeam())) {
//                // Bye Week for homeTeam
//                FrameLayout view = (FrameLayout) inflater.inflate(R.layout.bye_week_fixture, null, false);
//
//                TextView textView = (TextView) view.findViewById(R.id.home_team_name);
//                textView.setText(f.getHomeTeam().toUpperCase());
//
//                textView = (TextView) view.findViewById(R.id.week_number);
//                textView.setText("WEEK " + f.getWeek());
//
//                String homeName = f.getHomeTeam().toLowerCase().replace(" ", "_").replace(".", "");
//                int teamIndex = getTeamIndex(homeName);
//                Bitmap bitmap;
//                if (logos[teamIndex] == null) {
//                    int homeImage = context.getResources().getIdentifier(homeName, "drawable", context.getPackageName());
//
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 4;
//                    bitmap = BitmapFactory.decodeResource(context.getResources(), homeImage, options);
//
//                    logos[teamIndex] = bitmap;
//                } else {
//                    bitmap = logos[teamIndex];
//                }
//                ImageView imageView = (ImageView) view.findViewById(R.id.home_team_image);
//                imageView.setImageBitmap(bitmap);
//
//                input.getLinearLayout().addView(view);
//                i++;
//                Log.e("EAGLES", "" + i);
//
//                continue;
//            }
//
//            FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fixture_item, null, false);
//
//            TextView textView = (TextView) view.findViewById(R.id.away_team_name);
//            textView.setText(f.getAwayTeam().toUpperCase());
//
//            textView = (TextView) view.findViewById(R.id.home_team_name);
//            textView.setText(f.getHomeTeam().toUpperCase());
//
//            textView = (TextView) view.findViewById(R.id.away_team_score);
//            textView.setText("" + f.getAwayScore());
//
//            textView = (TextView) view.findViewById(R.id.home_team_score);
//            textView.setText("" + f.getHomeScore());
//
//            textView = (TextView) view.findViewById(R.id.date_text);
//            textView.setText(formatDateString(f.getDate()));
//
//            textView = (TextView) view.findViewById(R.id.week_number);
//            textView.setText("WEEK " + f.getWeek());
//
//            String time = f.getTime();
//            if ("5:32 AM".equals(time)) time = "Final";
//            textView = (TextView) view.findViewById(R.id.game_time);
//            textView.setText(time);
//
//            String awayName = f.getAwayTeam().toLowerCase().replace(" ", "_").replace(".", "");
//            int teamIndex = getTeamIndex(awayName);
//            Bitmap bitmap;
//            if (logos[teamIndex] == null) {
//                int awayImage = context.getResources().getIdentifier(awayName, "drawable", context.getPackageName());
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                bitmap = BitmapFactory.decodeResource(context.getResources(), awayImage, options);
//
//
////                logo = context.getResources().getDrawable(awayImage);
//                logos[teamIndex] = bitmap;
//            } else {
//                bitmap = logos[teamIndex];
//            }
//            ImageView imageView = (ImageView) view.findViewById(R.id.away_team_image);
//            imageView.setImageBitmap(bitmap);
//
//            String homeName = f.getHomeTeam().toLowerCase().replace(" ", "_").replace(".", "");
//            teamIndex = getTeamIndex(homeName);
////            int homeImage = context.getResources().getIdentifier(homeName, "drawable", context.getPackageName());
//            if (logos[teamIndex] == null) {
//                int homeImage = context.getResources().getIdentifier(homeName, "drawable", context.getPackageName());
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                bitmap = BitmapFactory.decodeResource(context.getResources(), homeImage, options);
//
//                logos[teamIndex] = bitmap;
//            } else {
//                bitmap = logos[teamIndex];
//            }
//            imageView = (ImageView) view.findViewById(R.id.home_team_image);
//            imageView.setImageBitmap(bitmap);

//            View view = ScheduleViewHelper.displayList(context, inflater, f, true);
//
//            input.getLinearLayout().addView(view);
//            i++;
//            Log.e("EAGLES", "" + i);
//        }

        input.getProgress().setVisibility(View.GONE);
    }

    private String getUrl() {
        switch (week) {
            case "18":
                return baseUrl + year + "/wild-card-play-offs";
            case "19":
                return baseUrl + year + "/divisional-play-offs";
            case "20":
                return baseUrl + year + "/conference-championships";
            case "21":
                return baseUrl + year + "/other";
            case "22":
                return baseUrl + year + "/super-bowl";
            default:
                break;
        }

//        return baseUrl + year + typeString + type + weekString + week;
        return baseUrl + year + weekString + week;
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

//    private String formatDateString(String date) {
//        String tmp = date.substring(0, date.length() - 5).trim();
//        String[] parts = tmp.split(" ");
//
//        String output = "";
//        for (String s : parts) {
//            output += s.toUpperCase().replace(s.substring(1), s.substring(1).toLowerCase());
////            output += s.toLowerCase().replace("" + s.charAt(0), ("" + s.charAt(0)).toUpperCase());
//            output += " ";
//        }
//
//        return output.trim();
//    }
//
//    private int getTeamIndex(String name) {
//        switch (name) {
//            case "arizona": return 0;
//            case "atlanta": return 1;
//            case "baltimore": return 2;
//            case "buffalo": return 3;
//            case "carolina": return 4;
//            case "chicago": return 5;
//            case "cincinnati": return 6;
//            case "cleveland": return 7;
//            case "dallas": return 8;
//            case "denver": return 9;
//            case "detroit": return 10;
//            case "green_bay": return 11;
//            case "houston": return 12;
//            case "indianapolis": return 13;
//            case "jacksonville": return 14;
//            case "kansas_city": return 15;
//            case "miami": return 16;
//            case "minnesota": return 17;
//            case "new_england": return 18;
//            case "new_orleans": return 19;
//            case "ny_giants": return 20;
//            case "ny_jets": return 21;
//            case "oakland": return 22;
//            case "philadelphia": return 23;
//            case "pittsburgh": return 24;
//            case "st_louis": return 25;
//            case "san_diego": return 26;
//            case "san_francisco": return 27;
//            case "seattle": return 28;
//            case "tampa_bay": return 29;
//            case "tennessee": return 30;
//            case "washington": return 31;
//            default: return -1;
//        }
//    }
}
