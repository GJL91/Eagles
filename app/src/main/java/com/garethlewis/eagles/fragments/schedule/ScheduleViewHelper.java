package com.garethlewis.eagles.fragments.schedule;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.entities.Fixture;
import com.garethlewis.eagles.util.TeamHelper;

import java.util.List;

public class ScheduleViewHelper {

    private static Context context;
    private static LayoutInflater inflater;

    public static void displayList(Context c, LayoutInflater i, LinearLayout parent, List<Fixture> fixtures, boolean viewAll) {
        context = c;
        inflater = i;

        for (Fixture f : fixtures) {
            View fixtureView = setupViewForFixture(f, viewAll);
            parent.addView(fixtureView);
        }
    }

    private static View setupViewForFixture(Fixture fixture, boolean viewAll) {
        if ("".equals(fixture.getAwayTeam())) {
            // fixture indicates a bye week
            return setupByeWeek(fixture, viewAll);
        }

        if (Integer.parseInt(fixture.getWeek()) > 17) {
            // Postseason game
            return setupPlayoffGame(fixture);
        }

        if (fixture.getHomeScore() == -1) {
            // fixture is in the future
            return setupFutureFixture(fixture, viewAll);
        }

        return setupResultFixture(fixture, viewAll);
    }

    private static View setupByeWeek(Fixture fixture, boolean viewAll) {
        FrameLayout view;

        TextView textView;
        if (viewAll) {
            view = (FrameLayout) inflater.inflate(R.layout.fixture_bye_item, null, false);
        } else {
            view = (FrameLayout) inflater.inflate(R.layout.fixture_bye_week_item, null, false);
            textView = (TextView) view.findViewById(R.id.week_number);
            textView.setText("WEEK " + fixture.getWeek());
        }

        textView = (TextView) view.findViewById(R.id.home_team_name);
        textView.setText(fixture.getHomeTeam().toUpperCase());

        String homeName = fixture.getHomeTeam().toLowerCase(); //.replace(" ", "_").replace(".", "");
//        int teamIndex = TeamHelper.getTeamIndex(homeName);
//        Bitmap bitmap;
//        if (logos[teamIndex] == null) {
//            int homeImage = context.getResources().getIdentifier(homeName, "drawable", context.getPackageName());
//
//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 4;
//            bitmap = BitmapFactory.decodeResource(context.getResources(), homeImage, options);
//
//            logos[teamIndex] = bitmap;
//        } else {
//            bitmap = logos[teamIndex];
//        }
        Bitmap bitmap = TeamHelper.getTeamLogo(homeName);
        ImageView imageView = (ImageView) view.findViewById(R.id.home_team_image);
        imageView.setImageBitmap(bitmap);

        return view;
    }

    private static View setupPlayoffGame(Fixture fixture) {
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fixture_result_item, null, false);

        if ("TBC".equals(fixture.getHomeTeam())) {
            TextView textView = (TextView) view.findViewById(R.id.away_team_score);
            textView.setText("TBC");

            textView = (TextView) view.findViewById(R.id.home_team_score);
            textView.setText("TBC");

            textView = (TextView) view.findViewById(R.id.date_text);
            textView.setText("TBC");

            textView = (TextView) view.findViewById(R.id.week_number);
            textView.setText("WEEK " + fixture.getWeek());

            textView = (TextView) view.findViewById(R.id.game_time);
            textView.setText("TBC");

            return view;
        }

        return view;
    }

    private static View setupFutureFixture(Fixture fixture, boolean viewAll) {
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fixture_future_item, null, false);

        setupCommonViews(view, fixture, viewAll);

        String awayName = fixture.getAwayTeam(); //.toLowerCase().replace(" ", "_").replace(".", "");
        String homeName = fixture.getHomeTeam(); //.toLowerCase().replace(" ", "_").replace(".", "");

//        Log.e("EAGLES", awayName + " - " + homeName);

        StandingsSQLiteHelper db = StandingsSQLiteHelper.getInstance(context);
        String record = db.getRecord(homeName);
        TextView textView = (TextView) view.findViewById(R.id.home_team_record);
        textView.setText(record);

        record = db.getRecord(awayName);
        textView = (TextView) view.findViewById(R.id.away_team_record);
        textView.setText(record);

//        db.printTeamNames();

        return view;
    }

    private static View setupResultFixture(Fixture fixture, boolean viewAll) {
        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fixture_result_item, null, false);

        setupCommonViews(view, fixture, viewAll);

        TextView textView = (TextView) view.findViewById(R.id.away_team_score);
        textView.setText("" + fixture.getAwayScore());

        textView = (TextView) view.findViewById(R.id.home_team_score);
        textView.setText("" + fixture.getHomeScore());

        return view;
    }

    private static void setupCommonViews(FrameLayout view, Fixture fixture, boolean viewAll) {
        TextView textView = (TextView) view.findViewById(R.id.away_team_name);
        textView.setText(fixture.getAwayTeam().toUpperCase());

        textView = (TextView) view.findViewById(R.id.home_team_name);
        textView.setText(fixture.getHomeTeam().toUpperCase());

        textView = (TextView) view.findViewById(R.id.date_text);
        textView.setText(formatDateString(fixture.getDate()));

        if (!viewAll) {
            textView = (TextView) view.findViewById(R.id.week_number);
            textView.setText("WEEK " + fixture.getWeek());
        }

        String time = fixture.getTime();
        if ("5:32 AM".equals(time)) time = "Final";
        textView = (TextView) view.findViewById(R.id.game_time);
        textView.setText(time);

        String awayName = fixture.getAwayTeam().toLowerCase(); //.replace(" ", "_").replace(".", "");
        Bitmap bitmap = TeamHelper.getTeamLogo(awayName);
        ImageView imageView = (ImageView) view.findViewById(R.id.away_team_image);
        imageView.setImageBitmap(bitmap);

        String homeName = fixture.getHomeTeam().toLowerCase(); //.replace(" ", "_").replace(".", "");
        bitmap = TeamHelper.getTeamLogo(homeName);
        imageView = (ImageView) view.findViewById(R.id.home_team_image);
        imageView.setImageBitmap(bitmap);
    }

//    private static Bitmap getTeamLogo(String nickname) {
////        int teamIndex = TeamHelper.getTeamIndex(team);
//        int teamIndex = TeamHelper.getTeamIndexFromNick(nickname);
//        if (teamIndex != -1) {
//            Bitmap bitmap;
//            String place = TeamHelper.getTeamPlacename(teamIndex).toLowerCase().replace(" ", "_");
//            if (logos[teamIndex] == null) {
//                int awayImage = context.getResources().getIdentifier(place, "drawable", context.getPackageName());
//
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                bitmap = BitmapFactory.decodeResource(context.getResources(), awayImage, options);
//
//                logos[teamIndex] = bitmap;
//            } else {
//                bitmap = logos[teamIndex];
//            }
//            return bitmap;
//        } else {
//            Log.e("EAGLES", "Index not found for team: " + nickname);
//        }
//        return null;
//    }

    private static String formatDateString(String date) {
        String tmp = date.substring(0, date.length() - 5).trim();
        String[] parts = tmp.split(" ");

        String output = "";
        for (String s : parts) {
            String temp = s.toUpperCase();
            output += temp.replace(temp.substring(1), s.substring(1).toLowerCase());
            output += " ";
        }

        return output.trim();
    }

}
