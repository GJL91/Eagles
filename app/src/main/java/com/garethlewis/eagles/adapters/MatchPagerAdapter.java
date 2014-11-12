package com.garethlewis.eagles.adapters;

import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

import com.garethlewis.eagles.fragments.home.MatchPagerFragment;

public class MatchPagerAdapter extends FragmentPagerAdapter {

    public MatchPagerAdapter(FragmentManager fm) {
        super(fm);
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
        return 3;
    }
}