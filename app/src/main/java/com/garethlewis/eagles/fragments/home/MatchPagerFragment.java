package com.garethlewis.eagles.fragments.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.R;

public class MatchPagerFragment extends android.support.v4.app.Fragment {
    public static final String ARG_SECTION_NUMBER = "section_number";
    private int position = 0;

    public MatchPagerFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(MatchPagerFragment.ARG_SECTION_NUMBER, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next_match, container, false);

        if (position == 1) {
            LinearLayout parent = (LinearLayout) view.findViewById(R.id.home_match_parent);
            parent.removeViews(0, 2);
        } else {
            final TextView pass = (TextView) view.findViewById(R.id.heading_pass);
            final TextView rush = (TextView) view.findViewById(R.id.heading_rush);
            final TextView rec = (TextView) view.findViewById(R.id.heading_rec);

            final int greyColour = getResources().getColor(R.color.translucent_black_dark);
            final int whiteColour = getResources().getColor(R.color.white);

            pass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    rush.setTextColor(greyColour);
                    rec.setTextColor(greyColour);
                    pass.setTextColor(whiteColour);
                }
            });

            rush.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pass.setTextColor(greyColour);
                    rec.setTextColor(greyColour);
                    rush.setTextColor(whiteColour);
                }
            });

            rec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pass.setTextColor(greyColour);
                    rush.setTextColor(greyColour);
                    rec.setTextColor(whiteColour);
                }
            });
        }

        return view;
    }
}
