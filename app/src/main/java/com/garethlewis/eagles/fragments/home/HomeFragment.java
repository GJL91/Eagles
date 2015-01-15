package com.garethlewis.eagles.fragments.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.adapters.HomeContentPagerAdapter;
import com.garethlewis.eagles.adapters.MatchPagerAdapter;
import com.garethlewis.eagles.database.ScheduleSQLiteHelper;
import com.garethlewis.eagles.tabs.TabPageIndicator;

public class HomeFragment extends Fragment {

    private View root;

    private Bitmap currentBackground;
    private Bitmap blurredBackground;

    private boolean blurred = false;
    private boolean changeOnFinish = false;

    public HomeContentPagerAdapter homeContentPagerAdapter;

//    private FragmentActivity fragmentActivity;
//
//    @Override
//    public void onAttach(Activity activity) {
//        fragmentActivity = (FragmentActivity) activity;
//        super.onAttach(activity);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.home_main, container, false);

        ViewPager mViewPager = (ViewPager) root.findViewById(R.id.placeholder_pager);

        MatchPagerAdapter mMatchPagerAdapter = new MatchPagerAdapter(getChildFragmentManager());
        mMatchPagerAdapter.setContext(getActivity());
        mViewPager.setAdapter(mMatchPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);

        ScheduleSQLiteHelper db = ScheduleSQLiteHelper.getInstance(getActivity());
        if (db.hasPreviousGame("Eagles")) {
            mViewPager.setCurrentItem(1);
        }

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {}

            @Override
            public void onPageScrollStateChanged(int i) {}

            @Override
            public void onPageSelected(int position) {
                if (position == 0 || position == 2) {
                    if (!blurred) changeOnFinish = true;
                    else {
                        ((ImageView) root.findViewById(R.id.home_match_background)).setImageBitmap(blurredBackground);
                    }
                } else {
                    ((ImageView) root.findViewById(R.id.home_match_background)).setImageBitmap(currentBackground);
                }
            }
        });

        ViewPager contentViewPager = (ViewPager) root.findViewById(R.id.placeholder_content);
        HomeContentPagerAdapter mContentPagerAdapter = new HomeContentPagerAdapter(getChildFragmentManager());
        this.homeContentPagerAdapter = mContentPagerAdapter;
        contentViewPager.setAdapter(mContentPagerAdapter);

        final TabPageIndicator tabPageIndicator = (TabPageIndicator) root.findViewById(R.id.tab_indicator);
        tabPageIndicator.setInflater(inflater);
        tabPageIndicator.setViewPager(contentViewPager, 0);
        contentViewPager.setOffscreenPageLimit(2);

        setMatchBackground();

        return root;
    }

    private void setMatchBackground() {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.top_image);
        int width = bmp.getWidth();
        int height = bmp.getHeight() - 120;

        currentBackground = Bitmap.createBitmap(bmp, 0, 0, width, height);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            new blurImageTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[])null);
        } else {
            new blurImageTask().execute((Void[])null);
        }

        ((ImageView) root.findViewById(R.id.home_match_background)).setImageBitmap(currentBackground);
    }

    private class blurImageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {
            blurred = false;

            Bitmap blur = Bitmap.createBitmap(currentBackground);

            RenderScript rs = RenderScript.create(getActivity().getApplicationContext());
            Allocation input = Allocation.createFromBitmap(rs, currentBackground, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SCRIPT);
            Allocation output = Allocation.createTyped(rs, input.getType());

            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            script.setRadius(25);

            script.forEach(output);

            output.copyTo(blur);

            blurredBackground = blur;

            blurred = true;

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (changeOnFinish) {
                ((ImageView) root.findViewById(R.id.home_match_background)).setImageBitmap(blurredBackground);
            }
            changeOnFinish = false;
        }
    }



}
