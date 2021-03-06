package com.example.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

public class NewsAdapter extends ArrayAdapter<NewsItem> {

    /**
     * Constructor of the adapter
     */
    public NewsAdapter(Context context, ArrayList<NewsItem> newsItems) {
        super(context, 0, newsItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        //get the news item object located at this position in the list
        NewsItem currentNewsItem = getItem(position);

        //title of the news
        TextView titleTV = convertView.findViewById(R.id.title);
        titleTV.setText(currentNewsItem.getTitle());

        //date
        TextView dateView = (TextView) convertView.findViewById(R.id.date);
        dateView.setText(currentNewsItem.getDateFormatted());

        //section - FILM mostly
        TextView sectionTV = (TextView) convertView.findViewById(R.id.section);
        sectionTV.setText(currentNewsItem.getSection());

        //author of the article
        TextView authorTV = (TextView) convertView.findViewById(R.id.author);
        authorTV.setText(currentNewsItem.getAuthor());

        //return the whole list view
        return convertView;
    }

}
