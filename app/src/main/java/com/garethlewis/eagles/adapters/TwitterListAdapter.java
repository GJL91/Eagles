package com.garethlewis.eagles.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.garethlewis.eagles.R;

import java.text.DateFormat;
import java.util.List;

import twitter4j.Status;

public class TwitterListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private List<Status> tweets;
    private Drawable img;

    public TwitterListAdapter(Context context, List<Status> tweets) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.tweets = tweets;
    }

    private class ViewHolder {
        ImageView tweetImage;
        TextView tweetTime;
        TextView tweetContent;
    }

    public void clearItems() {
        tweets.clear();
        notifyDataSetChanged();
    }

    public void setImage(Drawable img) {
        this.img = img;
    }

    public void addTweet(Status status) {
        tweets.add(status);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return tweets.size();
    }

    @Override
    public Object getItem(int position) {
        return tweets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.tweet_item, parent, false);

            viewHolder.tweetImage = (ImageView) convertView.findViewById(R.id.tweet_image);
            viewHolder.tweetTime = (TextView) convertView.findViewById(R.id.tweet_time);
            viewHolder.tweetContent = (TextView) convertView.findViewById(R.id.tweet_content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Status status = tweets.get(position);

        viewHolder.tweetImage.setImageDrawable(img);

        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        String time = dateFormat.format(status.getCreatedAt());
        viewHolder.tweetTime.setText(time);

        viewHolder.tweetContent.setText(status.getText());

        return convertView;
    }
}
