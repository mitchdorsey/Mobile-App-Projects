package com.example.android.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends ArrayAdapter {

    public BookAdapter(Context context, ArrayList<Book> bookArrayList){
        super(context, 0, bookArrayList);
    }

    private static final String BY = "By: ";
    private static final String NOT_AVAILABLE = "n/a";

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Book currentBook = (Book) getItem(position);

        String author = currentBook.getAuthor();
        String formattedAuthor;

        if(author == null || author == ""){
            formattedAuthor = NOT_AVAILABLE;
        }
        else{
            formattedAuthor = BY + author;
        }

        String title = currentBook.getTitle();
        short pageCount = currentBook.getPageCount();
        String formattedPageCount;
        if(pageCount > 0){
         formattedPageCount = pageCount + " pgs.";
        }
        else{
            formattedPageCount = NOT_AVAILABLE;
        }

        // Find the TextView in the list_item.xml layout with the ID author
        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        // Get the author from the current Book object and
        // set this text on the author TextView
        authorView.setText(formattedAuthor);

        // Find the TextView in the list_item.xml layout with the ID title
        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        // Get the author from the current Book object and
        // set this text on the author TextView
        titleView.setText(title);

        // Find the TextView in the list_item.xml layout with the ID page_count
        TextView pageCountView = (TextView) listItemView.findViewById(R.id.page_count);
        // Get the author from the current Book object and
        // set this text on the author TextView
        pageCountView.setText(formattedPageCount);

        return listItemView;
    }
}