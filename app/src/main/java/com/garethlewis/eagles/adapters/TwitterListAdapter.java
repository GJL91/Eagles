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

    private boolean error = false;
    private boolean internet = true;

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
        error = false;
        internet = true;
        notifyDataSetChanged();
    }

    public void setImage(Drawable img) {
        this.img = img;
    }

    public void addError() {
        tweets.clear();
        tweets.add(null);
        error = true;
        internet = true;
        notifyDataSetChanged();
    }

    public void addInternetError() {
        tweets.clear();
        tweets.add(null);
        internet = false;
        error = true;
        notifyDataSetChanged();
    }

    public void addTweet(Status status) {
        if (error) {
            tweets.clear();
            error = false;
            internet = true;
        }
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
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (error) {
            return internet ? 1 : 2;
        }
        return 0;

//        return futureFixturePositions.contains(position) ? TYPE_FUTURE : TYPE_RESULT;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        int type = getItemViewType(position);

        if (convertView == null) {
            viewHolder = new ViewHolder();

            if (type == 0) {
                convertView = inflater.inflate(R.layout.tweet_item, parent, false);

                viewHolder.tweetImage = (ImageView) convertView.findViewById(R.id.tweet_image);
                viewHolder.tweetTime = (TextView) convertView.findViewById(R.id.tweet_time);
                viewHolder.tweetContent = (TextView) convertView.findViewById(R.id.tweet_content);
            } else {
                if (type == 1) {
                    convertView = inflater.inflate(R.layout.tweet_error_item, parent, false);
                } else {
                    convertView = inflater.inflate(R.layout.tweet_internet_error_item, parent, false);
                }
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Status status = tweets.get(position);

        if (status != null) {
            viewHolder.tweetImage.setImageDrawable(img);

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            String time = dateFormat.format(status.getCreatedAt());
            viewHolder.tweetTime.setText(time);

            viewHolder.tweetContent.setText(status.getText());
        }

        return convertView;
    }
}
