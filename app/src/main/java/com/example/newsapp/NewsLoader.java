package com.example.newsapp;

import android.content.Context;
import android.content.AsyncTaskLoader;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<NewsItem>> {

    //tag for log messages
    private static final String LOG_TAG = NewsLoader.class.getName();

    //url for the request
    private String url;

    /**
     * Constructs
     *
     * @param context of the activity
     * @param u url to load data from
     */
    public NewsLoader(Context context, String u) {
        super(context);
        url = u;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsItem> loadInBackground() {
        if (url == null) {
            return null;
        }

        //perform the network request, parse the response, and extract a list of news.
        List<NewsItem> news = Utils.fetchNewsData(url);
        return news;
    }
}
