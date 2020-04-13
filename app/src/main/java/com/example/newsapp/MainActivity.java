package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<NewsItem>> {

    //URL to query the Guardian dataset for marvel information
    private static final String GUARDIAN_API_URL = "https://content.guardianapis.com/search";

    //Constant value for the news loader ID
    private static final int NEWS_LOADER_ID = 1;

    //Adapter for the list of news
    private NewsAdapter adapter;

    //TextView that is displayed when the list is empty
    private TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list);
        //list view for displaying data
        ListView listView = (ListView)findViewById(R.id.list);
        //empty text view - setting the text
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyStateTextView);
        //adapter for the news
        adapter = new NewsAdapter(MainActivity.this, new ArrayList<NewsItem>());

        //makes the list view use the adapter for the dataset
        listView.setAdapter(adapter);
        //sets a click listener to open the intent of the web browser
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                //get the news item object at the given position the user clicked on
                NewsItem news = adapter.getItem(position);
                //is there url available?
                if(news.getUrl() != null) {
                    //open web browser with the giver url
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    if (i.resolveActivity(getPackageManager()) != null) {
                        i.setData(Uri.parse(news.getUrl()));
                        startActivity(i);
                    }
                }
            }
        });

        //get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        //get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        //if there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            //init the loader
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            //display no connection message
            View loadingIndicator = findViewById(R.id.progressBar);
            loadingIndicator.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        //parse the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_API_URL);
        //buildUpon prepares the baseUri that we just parsed so we can add query parameters to itclean
        Uri.Builder uriBuilder = baseUri.buildUpon();
        //append query parameter and its value
        uriBuilder.appendQueryParameter("q", "marvel");
        uriBuilder.appendQueryParameter("page", "1");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "b9008914-e1ce-43b2-8dec-43804573e66e");
        //create a new loader for the given URL
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, List<NewsItem> data) {
        View progressBar = (View)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //set empty state text to display "No news found."
        emptyStateTextView.setText(R.string.no_news);

        //clear the adapter of previous data
        adapter.clear();

        //if there are valid data, display them
        if (data != null && !data.isEmpty()) {
            adapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        //loader reset, so we can clear out our existing data.
        adapter.clear();
    }
}
