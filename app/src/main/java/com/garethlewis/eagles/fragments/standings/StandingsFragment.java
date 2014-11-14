package com.garethlewis.eagles.fragments.standings;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.adapters.StandingsPagerAdapter;
import com.garethlewis.eagles.tabs.TabPageIndicator;

public class StandingsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.standings_main, container, false);

        ViewPager pager = (ViewPager) parent.findViewById(R.id.standings_pager);
        StandingsPagerAdapter standingsPagerAdapter = new StandingsPagerAdapter(getChildFragmentManager());
        pager.setAdapter(standingsPagerAdapter);

        TabPageIndicator tabPageIndicator = (TabPageIndicator) parent.findViewById(R.id.tab_indicator);
        tabPageIndicator.setInflater(inflater);
        tabPageIndicator.setViewPager(pager, 0);

        return parent;
    }

}