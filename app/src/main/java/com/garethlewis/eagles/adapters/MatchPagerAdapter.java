package com.garethlewis.eagles.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.fragments.home.MatchPagerFragment;

public class MatchPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public MatchPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        android.support.v4.app.Fragment fragment = new MatchPagerFragment();
        Bundle args = new Bundle();
        args.putInt(MatchPagerFragment.ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(context);

        String team = "Eagles";
        if (db.gameInProgress(team)) {
            return 1;
        }

        int total = 1;
        if (db.hasPreviousGame(team)) total++;
        if (db.hasNextGame(team)) total++;

        return total;
    }
}