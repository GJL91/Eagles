package com.garethlewis.eagles.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.entities.Fixture;
import com.garethlewis.eagles.util.TeamHelper;

import java.util.ArrayList;
import java.util.List;

public class FixtureListAdapter extends BaseAdapter {

    private static final int TYPE_BYE = 0;
    private static final int TYPE_FUTURE = 1;
    private static final int TYPE_RESULT = 2;

    private List<Integer> byeWeekPositions;
    private List<Integer> futureFixturePositions;

    private List<Fixture> fixtures;
    private Context context;
    private LayoutInflater inflater;

    private boolean mode;

    public FixtureListAdapter(Context context, ArrayList<Fixture> fixtures) {
        this.context = context;
        this.fixtures = fixtures;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        byeWeekPositions = new ArrayList<>();
        futureFixturePositions = new ArrayList<>();
    }

    public void clearItems() {
        fixtures.clear();
        byeWeekPositions.clear();
        futureFixturePositions.clear();
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    private static class ViewHolder {
        ImageView awayTeamImage;
        TextView awayTeamName;
        TextView awayTeamRecord;
        TextView date;
        TextView week;
        TextView time;
        TextView homeTeamRecord;
        TextView homeTeamName;
        ImageView homeTeamImage;
    }

    public void addByeFixture(Fixture fixture) {
        fixtures.add(fixture);
        byeWeekPositions.add(fixtures.size() - 1);
        notifyDataSetChanged();
    }

    public void addFutureFixture(Fixture fixture) {
        fixtures.add(fixture);
        futureFixturePositions.add(fixtures.size() - 1);
        notifyDataSetChanged();
    }

    public void addResultFixture(Fixture fixture) {
        fixtures.add(fixture);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return fixtures.size();
    }

    @Override
    public Object getItem(int position) {
        return fixtures.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (byeWeekPositions.contains(position)) {
            return TYPE_BYE;
        } else {
            return futureFixturePositions.contains(position) ? TYPE_FUTURE : TYPE_RESULT;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int type = getItemViewType(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            switch (type) {
                case TYPE_BYE:
                    convertView = inflater.inflate(R.layout.fixture_bye_week_item, parent, false);

                    viewHolder.homeTeamName = (TextView) convertView.findViewById(R.id.home_team_name);
                    viewHolder.homeTeamImage = (ImageView) convertView.findViewById(R.id.home_team_image);

                    viewHolder.week = (TextView) convertView.findViewById(R.id.week_number);
                    break;

                case TYPE_FUTURE:
                    convertView = inflater.inflate(R.layout.fixture_future_item, parent, false);

                    viewHolder.awayTeamImage = (ImageView) convertView.findViewById(R.id.away_team_image);
                    viewHolder.awayTeamName = (TextView) convertView.findViewById(R.id.away_team_name);
                    viewHolder.awayTeamRecord = (TextView) convertView.findViewById(R.id.away_team_record);

                    viewHolder.date = (TextView) convertView.findViewById(R.id.date_text);
                    viewHolder.week = (TextView) convertView.findViewById(R.id.week_number);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.game_time);

                    viewHolder.homeTeamRecord = (TextView) convertView.findViewById(R.id.home_team_record);
                    viewHolder.homeTeamName = (TextView) convertView.findViewById(R.id.home_team_name);
                    viewHolder.homeTeamImage = (ImageView) convertView.findViewById(R.id.home_team_image);
                    break;

                default:
                    convertView = inflater.inflate(R.layout.fixture_result_item, parent, false);

                    viewHolder.awayTeamImage = (ImageView) convertView.findViewById(R.id.away_team_image);
                    viewHolder.awayTeamName = (TextView) convertView.findViewById(R.id.away_team_name);
                    viewHolder.awayTeamRecord = (TextView) convertView.findViewById(R.id.away_team_score);

                    viewHolder.date = (TextView) convertView.findViewById(R.id.date_text);
                    viewHolder.week = (TextView) convertView.findViewById(R.id.week_number);
                    viewHolder.time = (TextView) convertView.findViewById(R.id.game_time);

                    viewHolder.homeTeamRecord = (TextView) convertView.findViewById(R.id.home_team_score);
                    viewHolder.homeTeamName = (TextView) convertView.findViewById(R.id.home_team_name);
                    viewHolder.homeTeamImage = (ImageView) convertView.findViewById(R.id.home_team_image);
                    break;
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Fixture fixture = fixtures.get(position);

        if (type == TYPE_FUTURE || type == TYPE_RESULT) {
            Bitmap bitmap = TeamHelper.getTeamLogo(fixture.getAwayTeam().toLowerCase());
            viewHolder.awayTeamImage.setImageBitmap(bitmap);

            viewHolder.awayTeamName.setText(fixture.getAwayTeam());

            viewHolder.date.setText(formatDateString(fixture.getDate()));
            if (fixture.getStatus() == 2) {
                viewHolder.time.setText("Final");
            } else {
                viewHolder.time.setText(fixture.getTime());
            }

            if (type == TYPE_RESULT) {
                viewHolder.homeTeamRecord.setText("" + fixture.getHomeScore());
                viewHolder.awayTeamRecord.setText("" + fixture.getAwayScore());
            } else {
                // Get record from standings.
                StandingsSQLiteHelper db = StandingsSQLiteHelper.getInstance(context);
                String record = db.getRecord(fixture.getHomeTeam());
                viewHolder.homeTeamRecord.setText(record);

                record = db.getRecord(fixture.getAwayTeam());
                viewHolder.awayTeamRecord.setText(record);
            }
        }

        if (!mode) {
            viewHolder.week.setText("WEEK " + fixture.getWeek());
        } else {
            viewHolder.week.setText("");
        }

        viewHolder.homeTeamName.setText(fixture.getHomeTeam());
        Bitmap bitmap = TeamHelper.getTeamLogo(fixture.getHomeTeam().toLowerCase());
        viewHolder.homeTeamImage.setImageBitmap(bitmap);

        return convertView;
    }

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
