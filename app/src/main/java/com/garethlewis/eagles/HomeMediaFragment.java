package com.garethlewis.eagles;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garethlewis.eagles.parsers.NewsParser;

public class HomeMediaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_media, container, false);
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_media_list);

//        ProgressBar spinner = (ProgressBar) getActivity().findViewById(R.id.media_progress);
//        spinner.setVisibility(View.VISIBLE);

        ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, linearLayout, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new NewsParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
        } else {
            new NewsParser().execute(parserPackage);
        }

        return view;
    }

}
