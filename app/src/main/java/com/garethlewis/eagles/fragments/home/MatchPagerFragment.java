package com.garethlewis.eagles.fragments.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.entities.Fixture;
import com.garethlewis.eagles.util.TeamHelper;

public class MatchPagerFragment extends android.support.v4.app.Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private int position = 0;
    private int[] layouts = new int[] {
        R.layout.fragment_previous_match,
        R.layout.fragment_next_match,
        R.layout.fragment_next_match
    };

    public MatchPagerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(MatchPagerFragment.ARG_SECTION_NUMBER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layouts[position], container, false);

        if (position == 1) {
            LinearLayout parent = (LinearLayout) view.findViewById(R.id.home_match_parent);
            parent.removeViews(0, 2);
        } else {
            if (position == 0) {
                ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
                Fixture lastGame = scheduleDB.getLastGame("Eagles");

                String awayTeam = TeamHelper.getTriCode(lastGame.getAwayTeam());
                String homeTeam = TeamHelper.getTriCode(lastGame.getHomeTeam());
                String matchup = awayTeam + " @ " + homeTeam;
                matchup = matchup.toUpperCase();

                TextView textView = (TextView) view.findViewById(R.id.previous_match_away_team_record);
                textView.setText("" + lastGame.getAwayScore());

                textView = (TextView) view.findViewById(R.id.previous_match_home_team_record);
                textView.setText("" + lastGame.getHomeScore());

                textView = (TextView) view.findViewById(R.id.previous_match_matchup_text);
                textView.setText(matchup);

                textView = (TextView) view.findViewById(R.id.previous_match_week_number);
                textView.setText("WEEK " + lastGame.getWeek());

                textView = (TextView) view.findViewById(R.id.previous_match_date_text);
                textView.setText("FINAL");

                String awayName = lastGame.getAwayTeam().toLowerCase();
                Bitmap bitmap = TeamHelper.getTeamLogo(awayName);
                ImageView imageView = (ImageView) view.findViewById(R.id.previous_match_away_team_image);
                imageView.setImageBitmap(bitmap);

                String homeName = lastGame.getHomeTeam().toLowerCase();
                bitmap = TeamHelper.getTeamLogo(homeName);
                imageView = (ImageView) view.findViewById(R.id.previous_match_home_team_image);
                imageView.setImageBitmap(bitmap);
            } else {



            }

            final TextView pass = (TextView) view.findViewById(R.id.heading_pass);
            final TextView rush = (TextView) view.findViewById(R.id.heading_rush);
            final TextView rec = (TextView) view.findViewById(R.id.heading_rec);

            final int greyColour = getResources().getColor(R.color.translucent_black_dark);
            final int whiteColour = getResources().getColor(R.color.white);

            pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rush.setTextColor(greyColour);
                    rec.setTextColor(greyColour);
                    pass.setTextColor(whiteColour);
                }
            });

            rush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pass.setTextColor(greyColour);
                    rec.setTextColor(greyColour);
                    rush.setTextColor(whiteColour);
                }
            });

            rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pass.setTextColor(greyColour);
                    rush.setTextColor(greyColour);
                    rec.setTextColor(whiteColour);
                }
            });
        }

        return view;
    }
}
