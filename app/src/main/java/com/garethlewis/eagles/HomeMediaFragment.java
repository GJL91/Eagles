package com.garethlewis.eagles;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garethlewis.eagles.fetchers.NewsFetcher;

public class HomeMediaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_media, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_media_list);

//        ProgressBar spinner = (ProgressBar) getActivity().findViewById(R.id.media_progress);
//        spinner.setVisibility(View.VISIBLE);

        FetcherPackage fetcherPackage = new FetcherPackage(getActivity(), inflater, container, linearLayout, null, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new NewsFetcher().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, fetcherPackage);
        } else {
            new NewsFetcher().execute(fetcherPackage);
        }

        return view;
    }

}
