package com.garethlewis.eagles.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.entities.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class NewsListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<NewsItem> newsItems;

    private boolean error = false;
    private boolean internet = true;

    public NewsListAdapter(Context context, ArrayList<NewsItem> newsItems) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.newsItems = newsItems;
    }

    private class ViewHolder {
        ImageView imageHolder, imageOverlay;
        TextView date, title;
    }

    public void clearItems() {
        newsItems.clear();
        error = false;
        internet = true;
        notifyDataSetChanged();
    }

    public void addError() {
        newsItems.clear();
        newsItems.add(null);
        error = true;
        internet = true;
        notifyDataSetChanged();
    }

    public void addInternetError() {
        newsItems.clear();
        newsItems.add(null);
        error = true;
        internet = false;
        notifyDataSetChanged();
    }

    public void addNewsStory(NewsItem newsItem) {
        newsItems.add(newsItem);
    }

    @Override
    public int getCount() {
        return newsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return newsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (error) {
            return internet ? 1 : 2;
        }
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int type = getItemViewType(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            if (type == 0) {
                convertView = inflater.inflate(R.layout.media_story_item, parent, false);

                viewHolder.imageOverlay = (ImageView) convertView.findViewById(R.id.image_overlay);
                viewHolder.imageHolder = (ImageView) convertView.findViewById(R.id.image_holder);
                viewHolder.title = (TextView) convertView.findViewById(R.id.post_title);
                viewHolder.date = (TextView) convertView.findViewById(R.id.post_date);
            } else {
                if (type == 1) {
                    convertView = inflater.inflate(R.layout.news_error_item, parent, false);
                } else {
                    convertView = inflater.inflate(R.layout.news_internet_error_item, parent, false);
                }
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        NewsItem newsItem = newsItems.get(position);

        if (newsItem != null) {
            if (newsItem.getImg() != null) {
                viewHolder.imageHolder.setImageDrawable(newsItem.getImg());
            }

            // Image Overlay stuff can go here

            viewHolder.date.setText(newsItem.getTime());
            viewHolder.title.setText(newsItem.getTitle());
        }

        return convertView;
    }
}
