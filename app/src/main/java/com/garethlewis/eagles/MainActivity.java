package com.garethlewis.eagles;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;

import com.garethlewis.eagles.database.MasterDatabase;
import com.garethlewis.eagles.database.ScheduleParamsSQLiteHelper;
import com.garethlewis.eagles.fragments.NavigationDrawerFragment;
import com.garethlewis.eagles.fragments.home.HomeFragment;
import com.garethlewis.eagles.fragments.news.NewsFragment;
import com.garethlewis.eagles.fragments.schedule.ScheduleFragment;
import com.garethlewis.eagles.fragments.settings.SettingsFragment;
import com.garethlewis.eagles.fragments.standings.StandingsFragment;
import com.garethlewis.eagles.util.FileHandler;
import com.garethlewis.eagles.util.TeamHelper;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    private int fragmentDisplayed = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FileHandler.setContext(getApplicationContext());
        MasterDatabase db = new MasterDatabase(this);
        db.setContext(getApplicationContext());

        TeamHelper.setupTeams(this);

//        db.resetDatabase();

        ScheduleParamsSQLiteHelper paramsDB = ScheduleParamsSQLiteHelper.getInstance(this);
        paramsDB.readParams();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        if (fragmentDisplayed != position - 1) {
            displayFragment(position - 1);
        }
    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        String fragmentTag = "";
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                fragmentTag = "Fragment_Home";
                break;
            case 1:
                fragment = new NewsFragment();
                fragmentTag = "Fragment_News";
                break;
//                case 2:
//                    fragment = new TeamFragment();
//                    break;
            case 3:
                fragment = new ScheduleFragment();
                fragmentTag = "Fragment_Schedule";
                break;
            case 4:
                fragment = new StandingsFragment();
                fragmentTag = "Fragment_Standings";
                break;
            case 5:
                fragment = new SettingsFragment();
                fragmentTag = "Fragment_Settings";
                break;
            default:
                break;
        }

        if (fragment == null) {
            Log.e("Eagles", "Error displaying fragment");
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment, fragmentTag)
                .commit();

        fragmentDisplayed = position;
    }

    public void openMenu(View view) {
        DrawerLayout menu = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu.openDrawer(Gravity.START);
    }

    public void refresh(View view) {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Fragment_Home");
        fragment.homeContentPagerAdapter.homeContentFragments[0].refreshNews();
        fragment.homeContentPagerAdapter.homeContentFragments[1].refreshSchedule();
        fragment.homeContentPagerAdapter.homeContentFragments[2].refreshTwitter();
    }

    public void clearData(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Do you really want to clear all data?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MasterDatabase db = new MasterDatabase(getApplicationContext());
                db.resetDatabase();
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing.
            }
        });

        dialog.setCancelable(false);
        dialog.show();
    }

//    public void refetchData() {
//        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
//        dialog.setMessage("Data cleared, gather now?");
//        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                refresh(null);
//            }
//        });
//
//        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Do nothing.
//            }
//        });
//
//        dialog.setCancelable(false);
//        dialog.show();
//    }

    public void fetchNews(View view) {
//        ListView list = (ListView) findViewById(R.id.home_media_list);
//        View errorView = findViewById(R.id.news_error);
//        if (errorView != null) {
//            list.removeView(errorView);
//        }

        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Fragment_Home");
        fragment.homeContentPagerAdapter.homeContentFragments[0].refreshNews();
    }

    public void fetchSchedules(View view) {
        // TODO: Update for schedule.

        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Fragment_Home");
        fragment.homeContentPagerAdapter.homeContentFragments[1].refreshSchedule();
    }

    public void fetchTwitter(View view) {
        HomeFragment fragment = (HomeFragment) getSupportFragmentManager().findFragmentByTag("Fragment_Home");
        fragment.homeContentPagerAdapter.homeContentFragments[2].refreshTwitter();
    }

    @Override
    public void onBackPressed() {
        if (fragmentDisplayed != 0) {
            mNavigationDrawerFragment.setItemChecked(1);
            displayFragment(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
//            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }
}
