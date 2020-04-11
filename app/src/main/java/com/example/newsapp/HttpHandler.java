package com.example.newsapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Handler for http requests
 */
public class HttpHandler {

    /**
     * Empty constructor
     */
    public HttpHandler() {
    }

    /**
     * Construct and make http request to given url
     * @param url given url for the request
     * @return JSON response
     * @throws IOException
     */
    public String makeHttpRequest(URL url) throws IOException {
        //response from the server
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            //construction connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            //connecting
            urlConnection.connect();
            //reading the response
            inputStream = urlConnection.getInputStream();
            //converting response to the json
            jsonResponse = convertStreamToString(inputStream);

        } catch (IOException e) {
            //error
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
            return jsonResponse;
        }
    }

    /**
     * Converting the response from the server to a readable format
     * @param is input stresm - the response from the server
     * @return string from the response
     */
    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }
}