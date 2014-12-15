package com.garethlewis.eagles.util;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import com.garethlewis.eagles.FetcherPackage;
import com.garethlewis.eagles.fetchers.NewsFetcher;
import com.garethlewis.eagles.fetchers.ScheduleFetcher;
import com.garethlewis.eagles.fetchers.TweetFetcher;

/**
 * This is a utility class to handle the fetching of content.
 * By using this class, we can be sure that only one fetch of any kind happens at any one time.
 */
public class ContentFetcher {
    public static int SCHEDULE = 0;
    public static int NEWS = 1;
    public static int TWITTER = 2;

    private static boolean[] syncing = {
            false, // Schedule
            false, // News
            false  // Twitter
    };

    /* Methods for checking whether a fetch is currently in progress. */

    public static boolean isSyncing(int task) { return syncing[task]; }

    public static boolean isScheduleSyncing() {
        return syncing[0];
    }

    public static boolean isNewsSyncing() {
        return syncing[1];
    }

    public static boolean isTwitterSyncing() {
        return syncing[2];
    }

    /* Methods for informing the fetcher that the fetch has finished. */

    public static void scheduleFinished() {
        syncing[0] = false;
    }

    public static void newsFinished() {
        syncing[1] = false;
    }

    public static void twitterFinished() {
        syncing[2] = false;
    }

    /* Methods for fetching content. */

    public static void fetchSchedules(FetcherPackage fetcherPackage) {
        if (syncing[0]) {
            return;
        }

        syncing[0] = true;
        fetcherPackage.getProgress().setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new ScheduleFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fetcherPackage);
        } else {
            new ScheduleFetcher().execute(fetcherPackage);
        }
    }

    public static void fetchNews(FetcherPackage fetcherPackage) {
        if (syncing[1]) {
            return;
        }

        syncing[1] = true;
        fetcherPackage.getProgress().setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new NewsFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fetcherPackage);
        } else {
            new NewsFetcher().execute(fetcherPackage);
        }
    }

    public static void fetchTwitter(FetcherPackage fetcherPackage) {
        if (syncing[TWITTER]) {
            return;
        }

        syncing[TWITTER] = true;
        fetcherPackage.getProgress().setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new TweetFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fetcherPackage);
        } else {
            new TweetFetcher().execute(fetcherPackage);
        }
    }

//    /* Methods for creating tasks to wait for fetching to finish. */
//
//    public static void waitSchedule(LayoutInflater inflater, View view, LinearLayout linearLayout, LinearLayout progress) {
//
//    }
}
