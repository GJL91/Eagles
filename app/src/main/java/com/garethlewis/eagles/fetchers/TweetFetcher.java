package com.garethlewis.eagles.fetchers;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.util.FetcherPackage;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.util.ContentFetcher;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

public class TweetFetcher extends AsyncTask<FetcherPackage, Void, List<Status>> {

    private FetcherPackage fetcherPackage;
    private Drawable img = null;

    @Override
    protected List<twitter4j.Status> doInBackground(FetcherPackage... params) {
        this.fetcherPackage = params[0];

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setApplicationOnlyAuthEnabled(true)
          .setOAuthConsumerKey("vLAf0MR79eqCTj9AGuumUKKov")
          .setOAuthConsumerSecret("W8dKojT4CynjNYDV73YFUdgIxemh3HmaciDYZeqvkVoReXSb7I");

        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        try {
            OAuth2Token token = twitter.getOAuth2Token();
            cb = new ConfigurationBuilder();
            cb.setApplicationOnlyAuthEnabled(true)
              .setOAuthConsumerKey("vLAf0MR79eqCTj9AGuumUKKov")
              .setOAuthConsumerSecret("W8dKojT4CynjNYDV73YFUdgIxemh3HmaciDYZeqvkVoReXSb7I")
              .setOAuth2TokenType(token.getTokenType())
              .setOAuth2AccessToken(token.getAccessToken());

            twitter = new TwitterFactory(cb.build()).getInstance();
        } catch (TwitterException e) {
            Log.e("EAGLES", "Twitter exception thrown, are keys wrong?");
        }

        String username = "Eagles";
        Query query = new Query("from:" + username);
        query.setCount(25);

        try {
            User user = twitter.showUser(username);
            String imageUrl = user.getOriginalProfileImageURL();
            img = getImageFromURL(imageUrl);



            QueryResult result = twitter.search(query);
            return result.getTweets();
        } catch (TwitterException e) {
            // Do nothing.
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<twitter4j.Status> tweets) {
        LayoutInflater inflater = fetcherPackage.getInflater();
        LinearLayout parent = fetcherPackage.getLinearLayout();
        int layout = R.layout.tweet_item;

        if (img == null) {
            img = fetcherPackage.getContext().getResources().getDrawable(R.drawable.philadelphia);
        }

        for (twitter4j.Status tweet : tweets) {
            View view = inflater.inflate(layout, null, false);

            ImageView imageView = (ImageView) view.findViewById(R.id.tweet_image);
            imageView.setImageDrawable(img);

            DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
            String time = dateFormat.format(tweet.getCreatedAt());
            TextView textView = (TextView) view.findViewById(R.id.tweet_time);
            textView.setText(time);

            textView = (TextView) view.findViewById(R.id.tweet_content);
            textView.setText(tweet.getText());

            parent.addView(view);
        }

        UpdatedSQLiteHelper updatedSQLiteHelper = UpdatedSQLiteHelper.getInstance(fetcherPackage.getContext());
        updatedSQLiteHelper.setUpdated("Twitter");

        fetcherPackage.getProgress().setVisibility(View.GONE);
        ContentFetcher.twitterFinished();
    }

    private Drawable getImageFromURL(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, null);
        } catch (MalformedURLException e) {
            Log.e("URL", "" + url);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
