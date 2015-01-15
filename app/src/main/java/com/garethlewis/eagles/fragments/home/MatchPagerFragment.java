package com.garethlewis.eagles.fragments.home;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.entities.Fixture;
import com.garethlewis.eagles.util.TeamHelper;

public class MatchPagerFragment extends android.support.v4.app.Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private int position = 0;

    public MatchPagerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(MatchPagerFragment.ARG_SECTION_NUMBER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;

        int basePosition = 0;
        ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
        Fixture previousGame = scheduleDB.getLastGame("Eagles");
        if (previousGame == null) {
            basePosition--;
        }

        if (position == basePosition + 1) {
            Fixture game = scheduleDB.getInProgress("Eagles");
            if (game != null) {
                view = inflater.inflate(R.layout.fragment_current_match, container, false);
            } else {
                view = inflater.inflate(R.layout.fragment_empty_match, container, false);

                if (previousGame != null) {
                    // Get fixture overview text e.g. PHI @ NYG\n34 - 26 W
                    String away = TeamHelper.getTriCode(previousGame.getAwayTeam());
                    String home = TeamHelper.getTriCode(previousGame.getHomeTeam());

                    int awayScore = previousGame.getAwayScore();
                    int homeScore = previousGame.getHomeScore();

                    String result = "T";
                    if ("PHI".equals(away)) {
                        if (awayScore > homeScore) {
                            result = "W";
                        } else {
                            if (homeScore > awayScore) {
                                result = "L";
                            }
                        }
                    } else {
                        if (awayScore > homeScore) {
                            result = "L";
                        } else {
                            if (homeScore > awayScore) {
                                result = "W";
                            }
                        }
                    }

                    String overviewText = away + " @ " + home + "\n" + awayScore + " - " + homeScore + " " + result;
                    TextView textView = (TextView) view.findViewById(R.id.empty_prev_match_text);
                    textView.setText(overviewText);
                }

                game = scheduleDB.getLastGame("Eagles");
                if (game != null) {
                    // Get fixture preview text e.g. CAR @ PHI\nNov 11 1:30 AM
                    String away = TeamHelper.getTriCode(game.getAwayTeam());
                    String home = TeamHelper.getTriCode(game.getHomeTeam());

                    String date = game.getDate();
                    date = date.substring(5, date.length() - 5);
                    date += " " + game.getTime();

                    String previewText = away + " @ " + home + "\n" + date;
                    TextView textView = (TextView) view.findViewById(R.id.empty_next_match_text);
                    textView.setText(previewText);
                }
            }

        } else {
            if (position == basePosition) {
                view = inflater.inflate(R.layout.fragment_previous_match, container, false);
                Fixture lastGame = scheduleDB.getLastGame("Eagles");
                if (lastGame == null) {
                    return view;
                }

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
                view = inflater.inflate(R.layout.fragment_next_match, container, false);
//                ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(getActivity());
//                Fixture nextGame = scheduleDB.getNextGame("Eagles");
                Fixture nextGame = scheduleDB.getLastGame("Eagles");

                if (nextGame == null) {
                    return view;
                }

                String awayTeam = TeamHelper.getTriCode(nextGame.getAwayTeam());
                String homeTeam = TeamHelper.getTriCode(nextGame.getHomeTeam());
                String matchup = awayTeam + " @ " + homeTeam;
                matchup = matchup.toUpperCase();

                StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(getActivity());

                String record = standingsDB.getRecord(nextGame.getAwayTeam());
                TextView textView = (TextView) view.findViewById(R.id.next_match_away_team_record);
                textView.setText(record);

                record = standingsDB.getRecord(nextGame.getHomeTeam());
                textView = (TextView) view.findViewById(R.id.next_match_home_team_record);
                textView.setText(record);

                textView = (TextView) view.findViewById(R.id.next_match_matchup_text);
                textView.setText(matchup);

                textView = (TextView) view.findViewById(R.id.next_match_week_number);
                textView.setText("WEEK " + nextGame.getWeek());

                textView = (TextView) view.findViewById(R.id.next_match_date_text);
                textView.setText("" + nextGame.getTime());

                String awayName = nextGame.getAwayTeam().toLowerCase();
                Bitmap bitmap = TeamHelper.getTeamLogo(awayName);
                ImageView imageView = (ImageView) view.findViewById(R.id.next_match_away_team_image);
                imageView.setImageBitmap(bitmap);

                String homeName = nextGame.getHomeTeam().toLowerCase();
                bitmap = TeamHelper.getTeamLogo(homeName);
                imageView = (ImageView) view.findViewById(R.id.next_match_home_team_image);
                imageView.setImageBitmap(bitmap);
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
