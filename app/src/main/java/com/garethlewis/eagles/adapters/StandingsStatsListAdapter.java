package com.garethlewis.eagles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.entities.Standing;

import java.util.List;

public class StandingsStatsListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Standing> standings;

    private int itemHeight;
    private int paddingHeight;

    public StandingsStatsListAdapter(Context context, List<Standing> standings1) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.standings = standings1;

        float multiplier = context.getResources().getDisplayMetrics().densityDpi / 160f;

        itemHeight = (int) (30 * multiplier);
        paddingHeight = (int) (10 * multiplier);
    }

    private static class ViewHolder {
        TextView winsText, lossesText, tiesText, percentText, homeText, roadText;
        TextView divisionText, conferenceText, pointsForText, pointsAgainstText, diffText, streakText;
    }

    public void addHeader() {
        standings.add(null);
        notifyDataSetChanged();
    }

    public void addStanding(Standing standing) {
        standings.add(standing);
        notifyDataSetChanged();
    }

    public void setHeight(ListView list) {
        ViewGroup.LayoutParams params = list.getLayoutParams();
        params.height = (getCount() * itemHeight) + paddingHeight;
        list.setLayoutParams(params);
    }

    @Override
    public int getCount() {
        return standings.size();
    }

    @Override
    public Object getItem(int position) {
        return standings.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return standings.get(position) == null ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
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

            if (type == 0) {
                convertView = inflater.inflate(R.layout.standings_table_stats_item, parent, false);

                viewHolder.winsText = (TextView) convertView.findViewById(R.id.standings_wins_text);
                viewHolder.lossesText = (TextView) convertView.findViewById(R.id.standings_losses_text);
                viewHolder.tiesText = (TextView) convertView.findViewById(R.id.standings_ties_text);
                viewHolder.percentText = (TextView) convertView.findViewById(R.id.standings_percent_text);
                viewHolder.homeText = (TextView) convertView.findViewById(R.id.standings_home_text);
                viewHolder.roadText = (TextView) convertView.findViewById(R.id.standings_road_text);
                viewHolder.divisionText = (TextView) convertView.findViewById(R.id.standings_division_text);
                viewHolder.conferenceText = (TextView) convertView.findViewById(R.id.standings_conference_text);
                viewHolder.pointsForText = (TextView) convertView.findViewById(R.id.standings_points_for_text);
                viewHolder.pointsAgainstText = (TextView) convertView.findViewById(R.id.standings_points_against_text);
                viewHolder.diffText = (TextView) convertView.findViewById(R.id.standings_diff_text);
                viewHolder.streakText = (TextView) convertView.findViewById(R.id.standings_streak_text);

            } else {
                convertView = inflater.inflate(R.layout.standings_table_stats_header_item, parent, false);

                viewHolder.winsText = (TextView) convertView.findViewById(R.id.standings_header_wins_text);
                viewHolder.lossesText = (TextView) convertView.findViewById(R.id.standings_header_losses_text);
                viewHolder.tiesText = (TextView) convertView.findViewById(R.id.standings_header_ties_text);
                viewHolder.percentText = (TextView) convertView.findViewById(R.id.standings_header_percent_text);
                viewHolder.homeText = (TextView) convertView.findViewById(R.id.standings_header_home_text);
                viewHolder.roadText = (TextView) convertView.findViewById(R.id.standings_header_road_text);
                viewHolder.divisionText = (TextView) convertView.findViewById(R.id.standings_header_division_text);
                viewHolder.conferenceText = (TextView) convertView.findViewById(R.id.standings_header_conference_text);
                viewHolder.pointsForText = (TextView) convertView.findViewById(R.id.standings_header_points_for_text);
                viewHolder.pointsAgainstText = (TextView) convertView.findViewById(R.id.standings_header_points_against_text);
                viewHolder.diffText = (TextView) convertView.findViewById(R.id.standings_header_diff_text);
                viewHolder.streakText = (TextView) convertView.findViewById(R.id.standings_header_streak_text);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Standing standing = standings.get(position);
        if (type == 0) { // Normal standing
            viewHolder.winsText.setText("" + standing.getWins());
            viewHolder.lossesText.setText("" + standing.getLosses());
            viewHolder.tiesText.setText("" + standing.getTies());

            float pct = (float) (standing.getWins() + (standing.getTies() / 2)) /  (float) (standing.getWins() + standing.getLosses() + standing.getTies());
            viewHolder.percentText.setText(String.format("%.3f", pct));

            viewHolder.homeText.setText("" + standing.getHomeRecord());
            viewHolder.roadText.setText("" + standing.getRoadRecord());
            viewHolder.divisionText.setText("" + standing.getDivisionRecord());
            viewHolder.conferenceText.setText("" + standing.getConferenceRecord());

            int pf = standing.getPointsFor(), pa = standing.getPointsAgainst();
            viewHolder.pointsForText.setText("" + pf);
            viewHolder.pointsAgainstText.setText("" + pa);
            viewHolder.diffText.setText("" + (pf - pa));

            viewHolder.streakText.setText("" + standing.getStreak());

        } else { // Header
            viewHolder.winsText.setText("W");
            viewHolder.lossesText.setText("L");
            viewHolder.tiesText.setText("T");
            viewHolder.percentText.setText("PCT");
            viewHolder.homeText.setText("HOME");
            viewHolder.roadText.setText("ROAD");
            viewHolder.divisionText.setText("DIV");
            viewHolder.conferenceText.setText("CONF");
            viewHolder.pointsForText.setText("PF");
            viewHolder.pointsAgainstText.setText("PA");
            viewHolder.diffText.setText("DIFF");
            viewHolder.streakText.setText("STRK");
        }

        return convertView;
    }
}
