package com.garethlewis.eagles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.garethlewis.eagles.R;

import java.util.ArrayList;
import java.util.List;

public class StandingsNamesListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<String> names;
    private List<Integer> headerPositions;

    private int itemHeight;
    private int paddingHeight;

    public StandingsNamesListAdapter(Context context, List<String> names) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.names = names;

        headerPositions = new ArrayList<>();

        itemHeight = (int) (30 * context.getResources().getDisplayMetrics().densityDpi / 160f);
        paddingHeight = (int) (20 * context.getResources().getDisplayMetrics().densityDpi / 160f);
    }

    public void addHeader(String name) {
        headerPositions.add(names.size());
        names.add(name);
        notifyDataSetChanged();
    }

    public void addTeam(String name) {
        names.add(name);
        notifyDataSetChanged();
    }

    public void setHeight(ListView list) {
        ViewGroup.LayoutParams params = list.getLayoutParams();
        params.height = (getCount() * itemHeight) + paddingHeight;
        list.setLayoutParams(params);
    }

    private class ViewHolder {
        TextView name;
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return names.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return headerPositions.contains(position) ? 1 : 0;
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
                convertView = inflater.inflate(R.layout.standings_table_name_item, parent, false);
                viewHolder.name = (TextView) convertView.findViewById(R.id.standings_name_text);
            } else {
                convertView = inflater.inflate(R.layout.standings_table_name_header_item, parent, false);
                viewHolder.name = (TextView) convertView.findViewById(R.id.standings_header_name_text);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String name = names.get(position);
        viewHolder.name.setText(name);

        return convertView;
    }
}
