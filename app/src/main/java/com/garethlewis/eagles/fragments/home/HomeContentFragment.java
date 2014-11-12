package com.garethlewis.eagles.fragments.home;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.parsers.NewsParser;
import com.garethlewis.eagles.R;

public class HomeContentFragment extends android.support.v4.app.Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private int position = 0;
    private int layout[] = new int[] {
            R.layout.fragment_home_media,
            R.layout.fragment_home_schedule,
            R.layout.fragment_home_twitter
    };

    public HomeContentFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(HomeContentFragment.ARG_SECTION_NUMBER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(layout[position], container, false);

        if (position == 0) {
            LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_media_list);

            ParserPackage parserPackage = new ParserPackage(getActivity(), inflater, container, linearLayout, null);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                new NewsParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, parserPackage);
            } else {
                new NewsParser().execute(parserPackage);
            }
        }

        return view;
    }
}
