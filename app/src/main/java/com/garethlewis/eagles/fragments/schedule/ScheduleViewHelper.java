package com.garethlewis.eagles.fragments.schedule;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.Fixture;

public class ScheduleViewHelper {

    private static Bitmap[] logos = new Bitmap[32];

    public static View setupViewForFixture(Context context, LayoutInflater inflater, Fixture fixture) {
        if ("".equals(fixture.getAwayTeam())) {
            // Bye Week for homeTeam
            FrameLayout view = (FrameLayout) inflater.inflate(R.layout.bye_week_fixture, null, false);

            TextView textView = (TextView) view.findViewById(R.id.home_team_name);
            textView.setText(fixture.getHomeTeam().toUpperCase());

            textView = (TextView) view.findViewById(R.id.week_number);
            textView.setText("WEEK " + fixture.getWeek());

            String homeName = fixture.getHomeTeam().toLowerCase().replace(" ", "_").replace(".", "");
            int teamIndex = getTeamIndex(homeName);
            Bitmap bitmap;
            if (logos[teamIndex] == null) {
                int homeImage = context.getResources().getIdentifier(homeName, "drawable", context.getPackageName());

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 4;
                bitmap = BitmapFactory.decodeResource(context.getResources(), homeImage, options);

                logos[teamIndex] = bitmap;
            } else {
                bitmap = logos[teamIndex];
            }
            ImageView imageView = (ImageView) view.findViewById(R.id.home_team_image);
            imageView.setImageBitmap(bitmap);

            return view;
        }

        FrameLayout view = (FrameLayout) inflater.inflate(R.layout.fixture_item, null, false);

        TextView textView = (TextView) view.findViewById(R.id.away_team_name);
        textView.setText(fixture.getAwayTeam().toUpperCase());

        textView = (TextView) view.findViewById(R.id.home_team_name);
        textView.setText(fixture.getHomeTeam().toUpperCase());

        textView = (TextView) view.findViewById(R.id.away_team_score);
        textView.setText("" + fixture.getAwayScore());

        textView = (TextView) view.findViewById(R.id.home_team_score);
        textView.setText("" + fixture.getHomeScore());

        textView = (TextView) view.findViewById(R.id.date_text);
        textView.setText(formatDateString(fixture.getDate()));

        textView = (TextView) view.findViewById(R.id.week_number);
        textView.setText("WEEK " + fixture.getWeek());

        String time = fixture.getTime();
        if ("0:00 AM".equals(time)) time = "Final";
        textView = (TextView) view.findViewById(R.id.game_time);
        textView.setText(time);

        String awayName = fixture.getAwayTeam().toLowerCase().replace(" ", "_").replace(".", "");
        int teamIndex = getTeamIndex(awayName);
        Bitmap bitmap;
        if (logos[teamIndex] == null) {
            int awayImage = context.getResources().getIdentifier(awayName, "drawable", context.getPackageName());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            bitmap = BitmapFactory.decodeResource(context.getResources(), awayImage, options);


//                logo = context.getResources().getDrawable(awayImage);
            logos[teamIndex] = bitmap;
        } else {
            bitmap = logos[teamIndex];
        }
        ImageView imageView = (ImageView) view.findViewById(R.id.away_team_image);
        imageView.setImageBitmap(bitmap);

        String homeName = fixture.getHomeTeam().toLowerCase().replace(" ", "_").replace(".", "");
        teamIndex = getTeamIndex(homeName);
//            int homeImage = context.getResources().getIdentifier(homeName, "drawable", context.getPackageName());
        if (logos[teamIndex] == null) {
            int homeImage = context.getResources().getIdentifier(homeName, "drawable", context.getPackageName());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            bitmap = BitmapFactory.decodeResource(context.getResources(), homeImage, options);

            logos[teamIndex] = bitmap;
        } else {
            bitmap = logos[teamIndex];
        }
        imageView = (ImageView) view.findViewById(R.id.home_team_image);
        imageView.setImageBitmap(bitmap);

        return view;
    }

    private static String formatDateString(String date) {
        String tmp = date.substring(0, date.length() - 5).trim();
        String[] parts = tmp.split(" ");

        String output = "";
        for (String s : parts) {
            output += s.toUpperCase().replace(s.substring(1), s.substring(1).toLowerCase());
            output += " ";
        }

        return output.trim();
    }

    private static int getTeamIndex(String name) {
        switch (name) {
            case "arizona": return 0;
            case "atlanta": return 1;
            case "baltimore": return 2;
            case "buffalo": return 3;
            case "carolina": return 4;
            case "chicago": return 5;
            case "cincinnati": return 6;
            case "cleveland": return 7;
            case "dallas": return 8;
            case "denver": return 9;
            case "detroit": return 10;
            case "green_bay": return 11;
            case "houston": return 12;
            case "indianapolis": return 13;
            case "jacksonville": return 14;
            case "kansas_city": return 15;
            case "miami": return 16;
            case "minnesota": return 17;
            case "new_england": return 18;
            case "new_orleans": return 19;
            case "ny_giants": return 20;
            case "ny_jets": return 21;
            case "oakland": return 22;
            case "philadelphia": return 23;
            case "pittsburgh": return 24;
            case "st_louis": return 25;
            case "san_diego": return 26;
            case "san_francisco": return 27;
            case "seattle": return 28;
            case "tampa_bay": return 29;
            case "tennessee": return 30;
            case "washington": return 31;
            default: return -1;
        }
    }


}
