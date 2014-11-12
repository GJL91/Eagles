package com.garethlewis.eagles.adapters;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;

import com.garethlewis.eagles.fragments.home.HomeContentFragment;

public class HomeContentPagerAdapter extends FragmentPagerAdapter {

    public HomeContentPagerAdapter(android.support.v4.app.FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        android.support.v4.app.Fragment fragment = new HomeContentFragment();
        Bundle args = new Bundle();
        args.putInt(HomeContentFragment.ARG_SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return "Media";
            case 1: return "Schedule";
            default: return "Twitter";
        }
    }
}
