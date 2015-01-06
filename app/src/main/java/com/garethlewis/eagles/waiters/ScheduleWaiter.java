package com.garethlewis.eagles.waiters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.database.StandingsSQLiteHelper;
import com.garethlewis.eagles.entities.Fixture;
import com.garethlewis.eagles.fragments.schedule.ScheduleViewHelper;
import com.garethlewis.eagles.util.ContentFetcher;
import com.garethlewis.eagles.util.ScheduleParams;

import java.util.List;

public class ScheduleWaiter extends BaseWaiter {

    private Context context;
    private LayoutInflater inflater;
    private View view;
    private LinearLayout linearLayout;
    private LinearLayout progress;
    private Spinner spinner;
    private boolean mode;

    public ScheduleWaiter(Context context, LayoutInflater inflater, View view, LinearLayout linearLayout, LinearLayout progress, Spinner spinner, boolean mode) {
        this.context = context;
        this.inflater = inflater;
        this.view = view;
        this.linearLayout = linearLayout;
        this.progress = progress;
        this.spinner = spinner;
        this.mode = mode;
    }

    @Override
    public void startWaiting() {
        progress.setVisibility(View.VISIBLE);
        waitTask(ContentFetcher.SCHEDULE);
    }

    @Override
    public void displayResults() {
        if (!mode) {
            TextView recordView = (TextView) view.findViewById(R.id.record_text);
            StandingsSQLiteHelper standingsDB = StandingsSQLiteHelper.getInstance(context);
            String record = standingsDB.getRecord("Eagles");
            recordView.setText(record);
        }
        ScheduleSQLiteHelper scheduleDB = ScheduleSQLiteHelper.getInstance(context);
        List<Fixture> fixtures;
        if (mode) {
            int week = ScheduleParams.getFirstFixture();
            if (week == 0) week = 1;
            fixtures = scheduleDB.getFixturesForWeek(week);
            spinner.setSelection(week - 1);
        } else {
            fixtures = scheduleDB.getFixturesForTeam("Eagles");
        }

        ScheduleViewHelper.displayList(context, inflater, linearLayout, fixtures, mode);
        progress.setVisibility(View.GONE);
    }
}
