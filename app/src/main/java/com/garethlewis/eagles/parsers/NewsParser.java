package com.garethlewis.eagles.parsers;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.NewsItem;
import com.garethlewis.eagles.R;
import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsParser extends AsyncTask<ParserPackage, Void, List<NewsItem>>  {

    private ParserPackage list;

    @Override
    protected List<NewsItem> doInBackground(ParserPackage... params) {

        list = params[0];
        MediaSQLiteHelper db = new MediaSQLiteHelper(list.getContext());
        //db.deleteAllStories();

        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        try {
            String url = "http://www.philadelphiaeagles.com/news/all-news.html";
            Document doc = Jsoup.connect(url).get();
            Elements el = doc.select(".mod-wrp-5").first().child(2).child(0).child(0).children();

            int stories = el.size();

            int i = 0;
            while (i < stories) {
                Elements post = el.get(i).child(0).child(0).children();

                int size = post.size() - 1;

                // Get the link to the article.
                Element e = post.get(size - 2).child(0);
                String link = e.absUrl("href").trim();

                // Get the title of the article.
                e = e.child(0);
                String title = e.text().trim();
                if (db.containsStory(title)) {
                    break;
                }
                Log.e("EAGLES", title);

                // Check if Dave Spadaro was the author
                boolean spadaro = false;
                e = post.get(size - 1);
                if (e.child(0).text().toLowerCase().contains("spadaro")) {
                    spadaro = true;
                }

                // Get the date of the article
                e = e.child(e.children().size() - 1);
                String date = e.text();

                // Get the source of the image.
                if (!spadaro) {
                    doc = Jsoup.connect(link).get();
                    try {
                        e = doc.select(".article-content").first().child(0).child(0);

                        String imgSource = null;
                        Drawable img = null;
                        if (e.hasAttr("src")) {
                            imgSource = e.absUrl("src").trim();
                        }

                        if (imgSource != null) {
                            Drawable drawable = getImageFromURL(imgSource);
                            // Type = 0 since this is an article.
                            newsItems.add(new NewsItem(link, title, imgSource, date, (short) 0, drawable));
                        } else {
                            newsItems.add(new NewsItem(link, title, null, date, (short) 0, null));
                        }
                    } catch (IndexOutOfBoundsException ex) {
                        newsItems.add(new NewsItem(link, title, null, date, (short) 0, null));
                    } catch (IOException ex) {
                        newsItems.add(new NewsItem(link, title, null, date, (short) 0, null));
                    }
                } else {
                    newsItems.add(new NewsItem(link, title, "spadaro", date, (short) 0, null));
                }
                i++;
            }
        } catch(IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return newsItems;
    }

    @Override
    protected void onPostExecute(List<NewsItem> newStories) {
        LayoutInflater inflater = list.getInflater();
        LinearLayout linearLayout = list.getLinearLayout();

        MediaSQLiteHelper db = new MediaSQLiteHelper(list.getContext());

        db.deleteOldStories(newStories.size());

        for (int i = newStories.size() - 1; i > -1; i--) {
            db.insertStory(newStories.get(i));
        }

        NewsItem[] newsItems = db.getStories();

        for (NewsItem n : newsItems) {
            FrameLayout view = (FrameLayout) inflater.inflate(R.layout.media_story_item, null, false);

            if (n.getImg() != null) {
                ((ImageView) view.findViewById(R.id.image_holder)).setImageDrawable(n.getImg());
            }

            Drawable overlay = null;
            if (n.getType() == 0) {
                overlay = list.getContext().getResources().getDrawable(R.drawable.item_news_overlay_large_mob);
            }

            ((ImageView) view.findViewById(R.id.image_overlay)).setImageDrawable(overlay);

            ((TextView) view.findViewById(R.id.post_date)).setText(n.getTime());
            ((TextView) view.findViewById(R.id.post_title)).setText(n.getTitle());

            linearLayout.addView(view);
        }

        UpdatedSQLiteHelper updatedSQLiteHelper = new UpdatedSQLiteHelper(list.getContext());
        updatedSQLiteHelper.setUpdated("Media");

        //db.deleteAllStories();
    }

    private Drawable getImageFromURL(String url) throws IOException {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, null);
            return d;
        } catch (MalformedURLException e) {
            Log.e("URL", ""+url);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new IOException();
    }
}


