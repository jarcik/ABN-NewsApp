package com.example.newsapp;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class NewsItem {

    /**
     * title of the article
     */
    private String title;
    /**
     * section of the article
     */
    private String section;
    /**
     * published date
     */
    private String date;
    /**
     * url of the article
     */
    private String url;
    /**
     * thumbnail url
     */
    private String thumbnailUrl;
    /**
     * author of the article
     */
    private String author;

    /**
     * constructor of the news item
     * @param t title
     * @param s section
     * @param d date
     * @param u url
     * @param tu thumbnail url
     * @param a author
     */
    public NewsItem(String t, String s, String d, String u, String tu, String a) {
        title = t;
        section = s;
        date = d;
        url = u;
        thumbnailUrl = tu;
        author = a;
    }

    public String getTitle() {
        return title;
    }

    public String getSection() {
        return section;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public String getAuthor() {
        return author;
    }

    /**
     * format the date to display
     * @return formatted date
     */
    public String getDateFormatted() {
        //formatter for the parsing the date from the string
        SimpleDateFormat parseF =new SimpleDateFormat("yyyy-MM-dd");
        //formatter for formatting the string to display in the view
        SimpleDateFormat formatF =new SimpleDateFormat("dd.MM.yyyy");
        try {
            //first substring for the requested format, then parse from string and then format to display
            return formatF.format(parseF.parse(date.substring(0, 10)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //if there is error, return the empty string rather then default date value
        return "";
    }
}
