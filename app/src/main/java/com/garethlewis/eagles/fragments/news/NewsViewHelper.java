package com.garethlewis.eagles.fragments.news;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.entities.NewsItem;

public class NewsViewHelper {

    private static Context context;
    private static LayoutInflater inflater;

    public static void displayList(Context c, LayoutInflater i, LinearLayout parent, NewsItem[] newsItems) {
        context = c;
        inflater = i;

        for (NewsItem n : newsItems) {
            FrameLayout view = (FrameLayout) inflater.inflate(R.layout.media_story_item, null, false);

            if (n.getImg() != null) {
                ((ImageView) view.findViewById(R.id.image_holder)).setImageDrawable(n.getImg());
            }

            Drawable overlay = null;
            if (n.getType() == 0) {
                overlay = context.getResources().getDrawable(R.drawable.item_news_overlay_large_mob);
            }

            ((ImageView) view.findViewById(R.id.image_overlay)).setImageDrawable(overlay);

            ((TextView) view.findViewById(R.id.post_date)).setText(n.getTime());
            ((TextView) view.findViewById(R.id.post_title)).setText(n.getTitle());

            parent.addView(view);



//            View fixtureView = setupViewForFixture(f, viewAll);
//            parent.addView(fixtureView);
        }
    }

}
