package com.garethlewis.eagles.fetchers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.adapters.TwitterListAdapter;
import com.garethlewis.eagles.util.ContentFetcher;
import com.garethlewis.eagles.util.FetcherPackage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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

    private boolean internet = true;

    @Override
    protected List<twitter4j.Status> doInBackground(FetcherPackage... params) {
        this.fetcherPackage = params[0];

        ConnectivityManager cm = (ConnectivityManager) fetcherPackage.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        internet = activeNetwork != null && activeNetwork.isConnected();

        if (!internet) {
            return null;
        }

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

            String username = "Eagles";
            Query query = new Query("from:" + username);
            query.setCount(25);

            User user = twitter.showUser(username);
            String imageUrl = user.getOriginalProfileImageURL();
            img = getImageFromURL(imageUrl);

            QueryResult result = twitter.search(query);
            return result.getTweets();
        } catch (TwitterException e) {
            Log.e("EAGLES", "Twitter exception thrown, are keys wrong?");
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<twitter4j.Status> tweets) {
        TwitterListAdapter adapter = fetcherPackage.getTwitterAdapter();

        if (tweets == null) {
            if (internet) {
                adapter.addError();
            } else {
                adapter.addInternetError();
            }

            finish();
            return;
        }

        if (img == null) {
            img = fetcherPackage.getContext().getResources().getDrawable(R.drawable.philadelphia);
        }
        adapter.setImage(img);

        for (twitter4j.Status tweet : tweets) {
            adapter.addTweet(tweet);
        }

        finish();
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

    private void finish() {
        fetcherPackage.getProgress().setVisibility(View.GONE);
        ContentFetcher.twitterFinished();
    }

}
