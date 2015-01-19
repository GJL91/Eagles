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

public class AllStatsListAdapter extends BaseAdapter {

    private ListView list;

    private LayoutInflater inflater;
    private List<List<String>> stats;

    private List<Integer> headerPositions;

    private int itemHeight;

    public AllStatsListAdapter(Context context, ListView list, List<List<String>> stats) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.list = list;
        this.stats = stats;

        headerPositions = new ArrayList<>();

        float multiplier = context.getResources().getDisplayMetrics().densityDpi / 160f;

        itemHeight = (int) (30 * multiplier);
    }

    public void addItem(List<String> item) {
        stats.add(item);
        notifyDataSetChanged();
    }

    public void addHeader(List<String> header) {
        headerPositions.add(stats.size());
        stats.add(header);
        notifyDataSetChanged();
    }

    public void clear() {
        headerPositions.clear();
        stats.clear();
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView stat0, stat1, stat2, stat3, stat4, stat5,
                 stat6, stat7, stat8, stat9, stat10, stat11;
    }

    public void setHeight() {
        ViewGroup.LayoutParams params = list.getLayoutParams();
        params.height = (getCount() * itemHeight);
        list.setLayoutParams(params);
    }

    @Override
    public int getCount() {
        return stats.size();
    }

    @Override
    public Object getItem(int position) {
        return stats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return headerPositions.contains(position) ? 1 : 0;
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
                convertView = inflater.inflate(R.layout.stats_table_row_item, parent, false);

                viewHolder.stat0 = (TextView) convertView.findViewById(R.id.stats_text_0);
                viewHolder.stat1 = (TextView) convertView.findViewById(R.id.stats_text_1);
                viewHolder.stat2 = (TextView) convertView.findViewById(R.id.stats_text_2);
                viewHolder.stat3 = (TextView) convertView.findViewById(R.id.stats_text_3);
                viewHolder.stat4 = (TextView) convertView.findViewById(R.id.stats_text_4);
                viewHolder.stat5 = (TextView) convertView.findViewById(R.id.stats_text_5);
                viewHolder.stat6 = (TextView) convertView.findViewById(R.id.stats_text_6);
                viewHolder.stat7 = (TextView) convertView.findViewById(R.id.stats_text_7);
                viewHolder.stat8 = (TextView) convertView.findViewById(R.id.stats_text_8);
                viewHolder.stat9 = (TextView) convertView.findViewById(R.id.stats_text_9);
                viewHolder.stat10 = (TextView) convertView.findViewById(R.id.stats_text_10);
                viewHolder.stat11 = (TextView) convertView.findViewById(R.id.stats_text_11);
            } else {
                convertView = inflater.inflate(R.layout.stats_table_row_header_item, parent, false);

                viewHolder.stat0 = (TextView) convertView.findViewById(R.id.stats_header_text_0);
                viewHolder.stat1 = (TextView) convertView.findViewById(R.id.stats_header_text_1);
                viewHolder.stat2 = (TextView) convertView.findViewById(R.id.stats_header_text_2);
                viewHolder.stat3 = (TextView) convertView.findViewById(R.id.stats_header_text_3);
                viewHolder.stat4 = (TextView) convertView.findViewById(R.id.stats_header_text_4);
                viewHolder.stat5 = (TextView) convertView.findViewById(R.id.stats_header_text_5);
                viewHolder.stat6 = (TextView) convertView.findViewById(R.id.stats_header_text_6);
                viewHolder.stat7 = (TextView) convertView.findViewById(R.id.stats_header_text_7);
                viewHolder.stat8 = (TextView) convertView.findViewById(R.id.stats_header_text_8);
                viewHolder.stat9 = (TextView) convertView.findViewById(R.id.stats_header_text_9);
                viewHolder.stat10 = (TextView) convertView.findViewById(R.id.stats_header_text_10);
                viewHolder.stat11 = (TextView) convertView.findViewById(R.id.stats_header_text_11);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        List<String> stat = stats.get(position);

        int count = stat.size();
        int offset = type == 0 ? 1 : 0;

        removeExtras(viewHolder, count - offset);

        for (int i = 0; i < count - offset; i++) {
            TextView textView = getViewHolderView(viewHolder, i);
            textView.setText(stat.get(i + offset));
        }

        return convertView;
    }

    private TextView getViewHolderView(ViewHolder viewHolder, int index) {
        switch (index) {
            case 0:
                return viewHolder.stat0;
            case 1:
                return viewHolder.stat1;
            case 2:
                return viewHolder.stat2;
            case 3:
                return viewHolder.stat3;
            case 4:
                return viewHolder.stat4;
            case 5:
                return viewHolder.stat5;
            case 6:
                return viewHolder.stat6;
            case 7:
                return viewHolder.stat7;
            case 8:
                return viewHolder.stat8;
            case 9:
                return viewHolder.stat9;
            case 10:
                return viewHolder.stat10;
            default:
                return viewHolder.stat11;
        }
    }

    private void removeExtras(ViewHolder viewHolder, int size) {
        switch (size) {
            case 0:
                viewHolder.stat0.setVisibility(View.GONE);
            case 1:
                viewHolder.stat1.setVisibility(View.GONE);
            case 2:
                viewHolder.stat2.setVisibility(View.GONE);
            case 3:
                viewHolder.stat3.setVisibility(View.GONE);
            case 4:
                viewHolder.stat4.setVisibility(View.GONE);
            case 5:
                viewHolder.stat5.setVisibility(View.GONE);
            case 6:
                viewHolder.stat6.setVisibility(View.GONE);
            case 7:
                viewHolder.stat7.setVisibility(View.GONE);
            case 8:
                viewHolder.stat8.setVisibility(View.GONE);
            case 9:
                viewHolder.stat9.setVisibility(View.GONE);
            case 10:
                viewHolder.stat10.setVisibility(View.GONE);
            case 11:
                viewHolder.stat11.setVisibility(View.GONE);
                break;
        }
    }
}
