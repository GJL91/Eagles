package com.garethlewis.eagles.parsers;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.garethlewis.eagles.ParserPackage;
import com.garethlewis.eagles.database.MediaSQLiteHelper;
import com.garethlewis.eagles.database.UpdatedSQLiteHelper;
import com.garethlewis.eagles.database.entities.NewsItem;
import com.garethlewis.eagles.fragments.news.NewsViewHelper;
import com.garethlewis.eagles.util.ContentFetcher;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsParser extends AsyncTask<ParserPackage, Void, List<NewsItem>>  {

    private ParserPackage list;

    @Override
    protected List<NewsItem> doInBackground(ParserPackage... params) {

        list = params[0];
        MediaSQLiteHelper db = MediaSQLiteHelper.getInstance(list.getContext());

        List<NewsItem> newsItems = new ArrayList<NewsItem>();
        try {
            String url = "http://www.philadelphiaeagles.com/news/all-news.html";
            Document doc;
            try {
                doc = Jsoup.connect(url).timeout(10000).get();
            } catch (SocketTimeoutException e) {
                return null;
            }
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
//                    doc = Jsoup.connect(link).get();
//                    try {
//                        e = doc.select(".article-content").first().child(0).child(0);
//
//                        String imgSource = null;
//                        if (e.hasAttr("src")) {
//                            imgSource = e.absUrl("src").trim();
//                        }
//
//                        if (imgSource != null) {
//                            Drawable drawable = getImageFromURL(imgSource);
//                            // Type = 0 since this is an article.
//                            newsItems.add(new NewsItem(link, title, imgSource, date, (short) 0, drawable));
//                        } else {
                            newsItems.add(new NewsItem(link, title, null, date, (short) 0, null));
//                        }
//                    } catch (IndexOutOfBoundsException | IOException ex) {
//                        newsItems.add(new NewsItem(link, title, null, date, (short) 0, null));
//                    }
                } else {
                    newsItems.add(new NewsItem(link, title, "spadaro", date, (short) 0, null));
                }
                i++;
            }
        } catch(IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return newsItems;
    }

    @Override
    protected void onPostExecute(List<NewsItem> newStories) {
        if (newStories == null) {
            NewsViewHelper.displayError(list.getInflater(), list.getLinearLayout());
            ContentFetcher.newsFinished();
            list.getProgress().setVisibility(View.GONE);
            return;
        }

        LayoutInflater inflater = list.getInflater();
        LinearLayout linearLayout = list.getLinearLayout();

        MediaSQLiteHelper db = MediaSQLiteHelper.getInstance(list.getContext());

        db.deleteOldStories(newStories.size());

        for (int i = newStories.size() - 1; i > -1; i--) {
            db.insertStory(newStories.get(i));
        }

        NewsItem[] newsItems = db.getStories();
        NewsViewHelper.displayList(list.getContext(), inflater, linearLayout, newsItems);

        UpdatedSQLiteHelper updatedSQLiteHelper = UpdatedSQLiteHelper.getInstance(list.getContext());
        updatedSQLiteHelper.setUpdated("Media");

        list.getProgress().setVisibility(View.GONE);
        ContentFetcher.newsFinished();
    }

    private Drawable getImageFromURL(String url) throws IOException {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, null);
        } catch (MalformedURLException e) {
            Log.e("URL", ""+url);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new IOException();
    }
}


