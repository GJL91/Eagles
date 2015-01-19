package com.garethlewis.eagles.fragments.team;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.baoyz.widget.PullRefreshLayout;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.adapters.AllStatsListAdapter;
import com.garethlewis.eagles.adapters.StatsNameListAdapter;
import com.garethlewis.eagles.fetchers.AllStatsFetcher;
import com.garethlewis.eagles.util.FetcherPackage;

import java.util.ArrayList;
import java.util.List;

public class TeamFragment extends Fragment {

    private PullRefreshLayout pullLayout;

    private StatsNameListAdapter passingNameAdapter, rushingNameAdapter, receivingNameAdapter, defendingNameAdapter,
                                 kickReturningNameAdapter, puntReturningNameAdapter, kickingNameAdapter, puntingNameAdapter;

    private AllStatsListAdapter passingStatsAdapter, rushingStatsAdapter, receivingStatsAdapter, defendingStatsAdapter,
                                kickReturningStatsAdapter, puntReturningStatsAdapter, kickingStatsAdapter, puntingStatsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parent = inflater.inflate(R.layout.team_main, container, false);

        pullLayout = (PullRefreshLayout) parent.findViewById(R.id.stats_swipe_container);
        pullLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doFetch();
            }
        });
        pullLayout.setRefreshing(true);

        // Passing lists.
        ListView passingNamesList = (ListView) parent.findViewById(R.id.team_passing_name_layout);
        passingNameAdapter = new StatsNameListAdapter(getActivity(), passingNamesList, new ArrayList<String>());

        passingNamesList.setAdapter(passingNameAdapter);

        ListView passingStatsList = (ListView) parent.findViewById(R.id.team_passing_stats_layout);
        passingStatsAdapter = new AllStatsListAdapter(getActivity(), passingStatsList, new ArrayList<List<String>>());

        passingStatsList.setAdapter(passingStatsAdapter);

        // Rushing lists.
        ListView rushingNameList = (ListView) parent.findViewById(R.id.team_rushing_name_layout);
        rushingNameAdapter = new StatsNameListAdapter(getActivity(), rushingNameList, new ArrayList<String>());

        rushingNameList.setAdapter(rushingNameAdapter);

        ListView rushingStatsList = (ListView) parent.findViewById(R.id.team_rushing_stats_layout);
        rushingStatsAdapter = new AllStatsListAdapter(getActivity(), rushingStatsList, new ArrayList<List<String>>());

        rushingStatsList.setAdapter(rushingStatsAdapter);

        // Receiving lists.
        ListView receivingNameList = (ListView) parent.findViewById(R.id.team_receiving_name_layout);
        receivingNameAdapter = new StatsNameListAdapter(getActivity(), receivingNameList, new ArrayList<String>());

        receivingNameList.setAdapter(receivingNameAdapter);

        ListView receivingStatsList = (ListView) parent.findViewById(R.id.team_receiving_stats_layout);
        receivingStatsAdapter = new AllStatsListAdapter(getActivity(), receivingStatsList, new ArrayList<List<String>>());

        receivingStatsList.setAdapter(receivingStatsAdapter);

        // Defense lists.
        ListView defendingNameList = (ListView) parent.findViewById(R.id.team_defending_name_layout);
        defendingNameAdapter = new StatsNameListAdapter(getActivity(), defendingNameList, new ArrayList<String>());

        defendingNameList.setAdapter(defendingNameAdapter);

        ListView defendingStatsList = (ListView) parent.findViewById(R.id.team_defending_stats_layout);
        defendingStatsAdapter = new AllStatsListAdapter(getActivity(), defendingStatsList, new ArrayList<List<String>>());

        defendingStatsList.setAdapter(defendingStatsAdapter);

        // Returning lists.
        ListView kickReturningNameList = (ListView) parent.findViewById(R.id.team_kickreturning_name_layout);
        kickReturningNameAdapter = new StatsNameListAdapter(getActivity(), kickReturningNameList, new ArrayList<String>());

        kickReturningNameList.setAdapter(kickReturningNameAdapter);

        ListView kickReturningStatsList = (ListView) parent.findViewById(R.id.team_kickreturning_stats_layout);
        kickReturningStatsAdapter = new AllStatsListAdapter(getActivity(), kickReturningStatsList, new ArrayList<List<String>>());

        kickReturningStatsList.setAdapter(kickReturningStatsAdapter);

        // Punt Return lists?
        ListView puntReturningNameList = (ListView) parent.findViewById(R.id.team_puntreturning_name_layout);
        puntReturningNameAdapter = new StatsNameListAdapter(getActivity(), puntReturningNameList, new ArrayList<String>());

        puntReturningNameList.setAdapter(puntReturningNameAdapter);

        ListView puntReturningStatsList = (ListView) parent.findViewById(R.id.team_puntreturning_stats_layout);
        puntReturningStatsAdapter = new AllStatsListAdapter(getActivity(), puntReturningStatsList, new ArrayList<List<String>>());

        puntReturningStatsList.setAdapter(puntReturningStatsAdapter);

        // Kicking lists.
        ListView kickingNameList = (ListView) parent.findViewById(R.id.team_kicking_name_layout);
        kickingNameAdapter = new StatsNameListAdapter(getActivity(), kickingNameList, new ArrayList<String>());

        kickingNameList.setAdapter(kickingNameAdapter);

        ListView kickingStatsList = (ListView) parent.findViewById(R.id.team_kicking_stats_layout);
        kickingStatsAdapter = new AllStatsListAdapter(getActivity(), kickingStatsList, new ArrayList<List<String>>());

        kickingStatsList.setAdapter(kickingStatsAdapter);

        // Punting lists.
        ListView puntingNameList = (ListView) parent.findViewById(R.id.team_punting_name_layout);
        puntingNameAdapter = new StatsNameListAdapter(getActivity(), puntingNameList, new ArrayList<String>());

        puntingNameList.setAdapter(puntingNameAdapter);

        ListView puntingStatsList = (ListView) parent.findViewById(R.id.team_punting_stats_layout);
        puntingStatsAdapter = new AllStatsListAdapter(getActivity(), puntingStatsList, new ArrayList<List<String>>());

        puntingStatsList.setAdapter(puntingStatsAdapter);

        doFetch();

        return parent;
    }

    public void doFetch() {
        FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), false, null);
        fetcherPackage.setSource(this);
        fetcherPackage.setStatsNameAdapters(new StatsNameListAdapter[] {passingNameAdapter, rushingNameAdapter, receivingNameAdapter, defendingNameAdapter,
                kickReturningNameAdapter, puntReturningNameAdapter, kickingNameAdapter, puntingNameAdapter});
        fetcherPackage.setAllStatsAdapters(new AllStatsListAdapter[] {passingStatsAdapter, rushingStatsAdapter, receivingStatsAdapter, defendingStatsAdapter,
                kickReturningStatsAdapter, puntReturningStatsAdapter, kickingStatsAdapter, puntingStatsAdapter});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new AllStatsFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fetcherPackage);
        } else {
            new AllStatsFetcher().execute(fetcherPackage);
        }
    }

    public void finish() {
        pullLayout.setRefreshing(false);
    }

}