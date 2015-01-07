package com.garethlewis.eagles.fragments.news;

import com.garethlewis.eagles.adapters.NewsListAdapter;
import com.garethlewis.eagles.entities.NewsItem;

public class NewsViewHelper {

    public static void displayList(NewsListAdapter adapter, NewsItem[] newsItems) {
        for (NewsItem newsItem : newsItems) {
            adapter.addNewsStory(newsItem);
        }
    }

    public static void displayError(NewsListAdapter adapter) {
        adapter.addError();
    }

    public static void displayInternetError(NewsListAdapter adapter) {
        adapter.addInternetError();
    }
}
