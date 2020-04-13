package com.example.newsapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Additional util methods
 */
public class Utils {

    /**
     * create url object from the string
     * @param stringUrl url as a string
     * @return URL object from the string
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            return null;
        }
        return url;
    }

    //tag for the log messages
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Fetch the data from the server
     * @param u url string
     * @return fetched data from the server
     */
    public static ArrayList<NewsItem> fetchNewsData(String u) {
        URL url = createUrl(u);
        //perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            HttpHandler httpHandler = new HttpHandler();
            //make request and recieve the json response
            jsonResponse = httpHandler.makeHttpRequest(url);
            Log.e(LOG_TAG, "Response from url: " + jsonResponse);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error making http request", e);
        }

        //get data from the json response
        ArrayList<NewsItem> news = extractFromJson(jsonResponse);
        //return fetched data
        return news;
    }

    /**
     * get data from the json
     * @param jsonString json string
     * @return parsed data
     */
    private static ArrayList<NewsItem> extractFromJson(String jsonString) {
        if (jsonString != null) {
            //init new list for the data
            ArrayList<NewsItem> newsList = new ArrayList<>();
            try {
                //get json object from the string
                JSONObject jsonObj = new JSONObject(jsonString);
                //get the "response" part - key from guardian API
                JSONObject response = jsonObj.getJSONObject("response");
                //check if there are some results
                if(response.getInt("total") == 0) {
                    Log.e(LOG_TAG, "No data to show.");
                    //no results - return null
                    return null;
                }

                //get the results part - actual news
                JSONArray news = response.getJSONArray("results");
                //looping through all news
                for (int i = 0; i < news.length(); i++) {
                    JSONObject c = news.getJSONObject(i);
                    //section
                    String sectionName = c.getString("sectionName");
                    //date
                    String publicationDate = c.getString("webPublicationDate");
                    //title
                    String articleTitle = c.getString("webTitle");
                    //url
                    String webUrl = c.getString("webUrl");
                    //thumbnail url
                    String thumbnailUrl = null;
                    if(c.has("fields") && c.getJSONObject("fields").has("thumbnail")) {
                        thumbnailUrl = c.getJSONObject("fields").getString("thumbnail");
                    }
                    //author
                    String author = "";
                    if(c.has("tags")) {
                        JSONArray tags = c.getJSONArray("tags");
                        if(tags != null && tags.length() > 0) {
                            for (int j = 0; j < tags.length(); j++) {
                                if(j > 0) author += ", ";
                                author += tags.getJSONObject(j).getString("webTitle");
                            }
                        }
                    }
                    //adding a news article to our news list
                    newsList.add(new NewsItem(articleTitle, sectionName, publicationDate, webUrl, thumbnailUrl, author));
                }
                //return the list
                return newsList;
            } catch (final JSONException e) {
                Log.e(LOG_TAG, "Json parsing error: " + e.getMessage());
            }
        } else {
            Log.e(LOG_TAG, "Couldn't get json from server.");
        }
        return null;
    }
}
