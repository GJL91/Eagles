package com.garethlewis.eagles.fragments.schedule;

import com.garethlewis.eagles.adapters.FixtureListAdapter;
import com.garethlewis.eagles.entities.Fixture;

import java.util.List;

public class ScheduleViewHelper {

    public static void displayList(FixtureListAdapter adapter, List<Fixture> fixtures, boolean viewAll) {
        adapter.clearItems();
        adapter.setMode(viewAll);
        for (Fixture fixture : fixtures) {
            if ("".equals(fixture.getAwayTeam())) {
                adapter.addByeFixture(fixture);
            } else {
                if (fixture.getHomeScore() == -1) {
                    adapter.addFutureFixture(fixture);
                } else {
                    adapter.addResultFixture(fixture);
                }
            }
        }
    }

//    public static void displayError(FixtureListAdapter adapter) {
//
//    }

}
