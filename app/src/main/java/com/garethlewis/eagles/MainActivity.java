package com.garethlewis.eagles;

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
import com.garethlewis.eagles.fragments.NewsFragment;
import com.garethlewis.eagles.fragments.ScheduleFragment;
import com.garethlewis.eagles.fragments.home.HomeFragment;


public class MainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

//    private MatchPagerAdapter mMatchPagerAdapter;

//    private ViewPager mViewPager;
//
//    private ActivityFragment[] activityFragments;

    /**
     * Used to store the last screen title. For use in {link #restoreActionBar()}.
     */
//    private CharSequence mTitle;

//    private Bitmap currentBackground;
//    private Bitmap blurredBackground;
//    private boolean blurred = false;
//    private boolean changeOnFinish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FileHandler.setContext(getApplicationContext());
        MasterDatabase db = new MasterDatabase(this);
        db.setContext(getApplicationContext());
//        db.resetDatabase();

//        MediaSQLiteHelper db = new MediaSQLiteHelper(this);
//        ScheduleSQLiteHelper sdb = new ScheduleSQLiteHelper(this);


//        createFragments();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

////        ActivityFragment fragment = new ActivityFragment();
////        Bundle args = new Bundle();
////        args.putInt(ActivityFragment.ARG_SECTION_NUMBER, 0);
////        fragment.setArguments(args);
////        activityFragments[0] = fragment;
//
////        FragmentManager fragmentManager = getFragmentManager();
////        fragmentManager.beginTransaction()
////                .replace(R.id.container, activityFragments[0])
////                .commit();

//        setMatchBackground();
//
//        mMatchPagerAdapter = new MatchPagerAdapter(getSupportFragmentManager());
//
//        mViewPager = (ViewPager) findViewById(R.id.placeholder_pager);
//        mViewPager.setOffscreenPageLimit(2);
//        mViewPager.setAdapter(mMatchPagerAdapter);
//
//        CirclePageIndicator cIndicator = (CirclePageIndicator) findViewById(R.id.circle_indicator);
//        cIndicator.setViewPager(mViewPager);
//
//        mViewPager.setCurrentItem(1);
//
//        cIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i2) {}
//
//            @Override
//            public void onPageScrollStateChanged(int i) {}
//
//            @Override
//            public void onPageSelected(int position) {
//                if (position == 0 || position == 2) {
//                    if (!blurred) changeOnFinish = true;
//                    else {
//                        ((ImageView) findViewById(R.id.home_match_background)).setImageBitmap(blurredBackground);
//                    }
//                } else {
//                    ((ImageView) findViewById(R.id.home_match_background)).setImageBitmap(currentBackground);
//                }
//            }
//        });
//
//        HomeContentPagerAdapter mContentPagerAdapter = new HomeContentPagerAdapter(getSupportFragmentManager());
//
//        ViewPager contentViewPager = (ViewPager) findViewById(R.id.placeholder_content);
//        contentViewPager.setOffscreenPageLimit(2);
//        contentViewPager.setAdapter(mContentPagerAdapter);
//
//        TabPageIndicator tabPageIndicator = (TabPageIndicator) findViewById(R.id.indicator);
//        tabPageIndicator.setViewPager(contentViewPager);
//        tabPageIndicator.setVisibility(View.VISIBLE);
//
//        contentViewPager.setCurrentItem(0);

////        ProgressBar spinner = (ProgressBar)findViewById(R.id.media_progress);
////        spinner.setVisibility(View.GONE);
//
////        mTitle = getTitle();
//
//
////        showMediaContent(null);

    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//            .replace(R.id.container, activityFragments[position - 1])
//            .commit();
        displayFragment(position - 1);
    }

    private void displayFragment(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new NewsFragment();
                break;
//                case 2:
//                    fragment = new TeamFragment();
//                    break;
            case 3:
                fragment = new ScheduleFragment();
                break;
//                case 4:
//                    fragment = new StandingsFragment();
//                    break;
//                case 5:
//                    fragment = new SettingsFragment();
//                    break;
            default:
                break;
        }

        if (fragment == null) {
            Log.e("Eagles", "Error displaying fragment");
            return;
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

//    public void onSectionAttached(int number) {
//        String[] headings = getResources().getStringArray(R.array.sectionHeadings);
//        Log.e("HTML", headings.length + " - " + number);
//        mTitle = headings[number - 1];
//    }

//    public void restoreActionBar() {
//        ActionBar actionBar = getActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(false);
//        //actionBar.setTitle(mTitle);
//    }

//    private void setMatchBackground() {
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.top_image);
//        int width = bmp.getWidth();
//        int height = bmp.getHeight() - 120;
//
//        Bitmap resizedBmp = Bitmap.createBitmap(bmp, 0, 0, width, height);
//        currentBackground = resizedBmp;
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            new blurImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
//        } else {
//            new blurImageTask().execute((Void[])null);
//        }
//
//        ((ImageView) findViewById(R.id.home_match_background)).setImageBitmap(resizedBmp);
//    }

//    private void createFragments() {
//        int length = getResources().getStringArray(R.array.activityLayouts).length;
//        activityFragments = new ActivityFragment[length];
//
//        for (int i = 0; i < length; i++) {
//            ActivityFragment fragment = new ActivityFragment();
//            Bundle args = new Bundle();
//            args.putInt(ActivityFragment.ARG_SECTION_NUMBER, i);
//            fragment.setArguments(args);
//            activityFragments[i] = fragment;
//        }
//    }

    public void openMenu(View view) {
        DrawerLayout menu = (DrawerLayout) findViewById(R.id.drawer_layout);
        menu.openDrawer(Gravity.LEFT);
    }

    public void refresh(View view) {
//        if (!blurred) changeOnFinish = true;
//        else {
//            ((ImageView) findViewById(R.id.home_match_background)).setImageBitmap(blurredBackground);
//        }
    }

//    public void showMediaContent(View view) {
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//            .replace(R.id.placeholder_content, new HomeMediaFragment())
//            .commit();
//    }
//
//    public void showScheduleContent(View view) {
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//            .replace(R.id.placeholder_content, new HomeScheduleFragment())
//            .commit();
//    }
//
//    public void showTwitterContent(View view) {
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction()
//            .replace(R.id.placeholder_content, new HomeTwitterFragment())
//            .commit();
//    }

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

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        /*if (id == R.id.action_settings) {
//            return true;
//        }*/
//        return super.onOptionsItemSelected(item);
//    }

//    public class HomeContentPagerAdapter extends FragmentPagerAdapter {
//
//        public HomeContentPagerAdapter(android.support.v4.app.FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public android.support.v4.app.Fragment getItem(int position) {
//            android.support.v4.app.Fragment fragment = new HomeContentFragment();
//            Bundle args = new Bundle();
//            args.putInt(HomeContentFragment.ARG_SECTION_NUMBER, position);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0: return "Media";
//                case 1: return "Schedule";
//                default: return "Twitter";
//            }
//        }
//    }
//
//    public static class HomeContentFragment extends android.support.v4.app.Fragment {
//        public static final String ARG_SECTION_NUMBER = "section_number";
//        private int position = 0;
//        private int layout[] = new int[] {
//                R.layout.fragment_home_media,
//                R.layout.fragment_home_schedule,
//                R.layout.fragment_home_twitter
//        };
//
//        public HomeContentFragment() { }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            position = getArguments().getInt(HomeContentFragment.ARG_SECTION_NUMBER, 0);
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(layout[position], container, false);
//
//            if (position == 0) {
//                LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.home_media_list);
//
//                ListPackage listPackage = new ListPackage(getActivity(), inflater, container, linearLayout, null);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//                    new NewsParser().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, listPackage);
//                } else {
//                    new NewsParser().execute(listPackage);
//                }
//            }
//
//            return view;
//        }
//    }
//
//    public class MatchPagerAdapter extends FragmentPagerAdapter {
//
//        public MatchPagerAdapter(android.support.v4.app.FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public android.support.v4.app.Fragment getItem(int position) {
//            android.support.v4.app.Fragment fragment = new MatchPagerFragment();
//            Bundle args = new Bundle();
//            args.putInt(MatchPagerFragment.ARG_SECTION_NUMBER, position);
//            fragment.setArguments(args);
//            return fragment;
//        }
//
//        @Override
//        public int getCount() {
//            return 3;
//        }
//    }
//
//    public static class MatchPagerFragment extends android.support.v4.app.Fragment {
//        public static final String ARG_SECTION_NUMBER = "section_number";
//        private int position = 0;
//
//        public MatchPagerFragment() { }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            position = getArguments().getInt(MatchPagerFragment.ARG_SECTION_NUMBER, 0);
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(R.layout.fragment_next_match, container, false);
//
//            if (position == 1) {
//                LinearLayout parent = (LinearLayout) view.findViewById(R.id.home_match_parent);
//                parent.removeViews(0, 2);
//            } else {
//                final TextView pass = (TextView) view.findViewById(R.id.heading_pass);
//                final TextView rush = (TextView) view.findViewById(R.id.heading_rush);
//                final TextView rec = (TextView) view.findViewById(R.id.heading_rec);
//
//                final int greyColour = getResources().getColor(R.color.translucent_black_dark);
//                final int whiteColour = getResources().getColor(R.color.white);
//
//                pass.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        rush.setTextColor(greyColour);
//                        rec.setTextColor(greyColour);
//                        pass.setTextColor(whiteColour);
//                    }
//                });
//
//                rush.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        pass.setTextColor(greyColour);
//                        rec.setTextColor(greyColour);
//                        rush.setTextColor(whiteColour);
//                    }
//                });
//
//                rec.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        pass.setTextColor(greyColour);
//                        rush.setTextColor(greyColour);
//                        rec.setTextColor(whiteColour);
//                    }
//                });
//            }
//
//            return view;
//        }
//    }
//
//    public static class ActivityFragment extends Fragment {
//        private static final String ARG_SECTION_NUMBER = "section_number";
//        private int position = 0;
//        private int[] layouts = getResources().getIntArray(R.array.activityLayouts);
//
//        public ActivityFragment() { }
//
//        @Override
//        public void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            position = getArguments().getInt(MatchPagerFragment.ARG_SECTION_NUMBER, 0);
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//            View view = inflater.inflate(layouts[position], container, false);
//
//            return view;
//        }
//    }
//
////    /**
////     * A placeholder fragment containing a simple view.
////     */
////    public static class PlaceholderFragment extends Fragment {
////        /**
////         * The fragment argument representing the section number for this
////         * fragment.
////         */
////        private static final String ARG_SECTION_NUMBER = "section_number";
////
////        /**
////         * Returns a new instance of this fragment for the given section
////         * number.
////         */
////        public static PlaceholderFragment newInstance(int sectionNumber) {
////            PlaceholderFragment fragment = new PlaceholderFragment();
////            Bundle args = new Bundle();
////            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
////            fragment.setArguments(args);
////            return fragment;
////        }
////
////        public PlaceholderFragment() {
////        }
////
////        @Override
////        public View onCreateView(LayoutInflater inflater, ViewGroup container,
////                Bundle savedInstanceState) {
////            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
////            return rootView;
////        }
////
////        @Override
////        public void onAttach(Activity activity) {
////            super.onAttach(activity);
////            /*((MainActivity) activity).onSectionAttached(
////                    getArguments().getInt(ARG_SECTION_NUMBER));*/
////        }
////    }
//
//    private class blurImageTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... args) {
//            blurred = false;
//
//            Bitmap blur = Bitmap.createBitmap(currentBackground);
//
//            RenderScript rs = RenderScript.create(getApplicationContext());
//            Allocation input = Allocation.createFromBitmap(rs, currentBackground, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);
//            Allocation output = Allocation.createTyped(rs, input.getType());
//
//            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//            script.setInput(input);
//
//            script.setRadius(25);
//
//            script.forEach(output);
//
//            output.copyTo(blur);
//
//            blurredBackground = blur;
//
//            blurred = true;
//
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            if (changeOnFinish) {
//                ((ImageView) findViewById(R.id.home_match_background)).setImageBitmap(blurredBackground);
//            }
//            changeOnFinish = false;
//        }
//    }

}
