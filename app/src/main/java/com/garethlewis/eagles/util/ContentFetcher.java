package com.garethlewis.eagles.util;

import android.os.AsyncTask;
import android.os.Build;
import android.view.View;

import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.parsers.NewsParser;
import com.garethlewis.eagles.parsers.ScheduleParser;

/**
 * This is a utility class to handle the fetching of content.
 * By using this class, we can be sure that only one fetch of any kind happens at any one time.
 */
public class ContentFetcher {

    private static boolean scheduleSyncing = false;
    private static boolean newsSyncing = false;
    private static boolean twitterSyncing = false;

    public static boolean isScheduleSyncing() {
        return scheduleSyncing;
    }

    public static boolean isNewsSyncing() {
        return newsSyncing;
    }

    public static boolean isTwitterSyncing() {
        return twitterSyncing;
    }

    public static void scheduleFinished() {
        scheduleSyncing = false;
    }

    public static void newsFinished() {
        newsSyncing = false;
    }

    public static void fetchSchedules(ParserPackage parserPackage) {
        if (scheduleSyncing) {
            return;
        }

        scheduleSyncing = true;
        parserPackage.getProgress().setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new ScheduleParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
        } else {
            new ScheduleParser().execute(parserPackage);
        }
    }

    public static void fetchNews(ParserPackage parserPackage) {
        if (newsSyncing) {
            return;
        }

        newsSyncing = true;
        parserPackage.getProgress().setVisibility(View.VISIBLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new NewsParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
        } else {
            new NewsParser().execute(parserPackage);
        }
    }

}
