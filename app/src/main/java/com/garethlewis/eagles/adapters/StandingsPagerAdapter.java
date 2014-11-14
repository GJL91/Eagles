package com.garethlewis.eagles.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

import com.garethlewis.eagles.fragments.standings.StandingsTableFragment;

public class StandingsPagerAdapter extends FragmentPagerAdapter {

    public StandingsPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        android.support.v4.app.Fragment fragment = new StandingsTableFragment();
        Bundle args = new Bundle();
        args.putInt(StandingsTableFragment.ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Division";
            default: return "Conference";
        }
    }
}

