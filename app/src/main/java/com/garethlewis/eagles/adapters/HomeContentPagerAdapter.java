package com.garethlewis.eagles.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.garethlewis.eagles.fragments.home.HomeContentFragment;

public class HomeContentPagerAdapter extends FragmentPagerAdapter {

    public HomeContentFragment[] homeContentFragments = new HomeContentFragment[3];

    public HomeContentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new HomeContentFragment();
        this.homeContentFragments[position] = (HomeContentFragment) fragment;
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
            case 0: return "News";
            case 1: return "Schedule";
            default: return "Twitter";
        }
    }
}
