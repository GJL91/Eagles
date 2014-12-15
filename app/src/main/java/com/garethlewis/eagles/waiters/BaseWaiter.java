package com.garethlewis.eagles.waiters;

import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.garethlewis.eagles.util.ContentFetcher;

public abstract class BaseWaiter {

    private LayoutInflater inflater;
    private View view;
    private LinearLayout linearLayout;

    public BaseWaiter() { }

    public void waitTask(int task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new Waiter().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, task);
        } else {
            new Waiter().execute(task);
        }
    }

    public abstract void displayResults();

    public abstract void startWaiting();

    private class Waiter extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int task = params[0];
            while (ContentFetcher.isSyncing(task)) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    // Do nothing.
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            displayResults();
        }
    }
}
