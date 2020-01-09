package com.example.android.booklistingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {

    private TextView mEmptyStateTextView;
    private BookAdapter mAdapter;
    private BookListViewModel mBookListViewModel;
    private ProgressBar mLoadingIndicator;
    private static boolean isNetworkConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_activity);

        registerNetworkCallback(this);

        mBookListViewModel = ViewModelProviders.of(this).get(BookListViewModel.class);

        // Find a reference to the {@link ListView} in the layout
        ListView bookListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        bookListView.setEmptyView(mEmptyStateTextView);

        // Create a new {@link ArrayAdapter} of books
        mAdapter = new BookAdapter(this, new ArrayList<Book>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        bookListView.setAdapter(mAdapter);

        mLoadingIndicator = findViewById(R.id.loading_indicator);
        mLoadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.search_to_begin);

        // When list of books is displayed and activity onCreate is called due to configuration
        // change(ie. screen rotation), retrieve list of books from ViewModel and repopulate adapter
        // Note: when the activity first starts up, there is nothing to retrieve from the ViewModel
        List<Book> books = mBookListViewModel.getBooks();
        if(books != null && !books.isEmpty()){
            mAdapter.addAll(books);
        }
    }

//action when Submit button is clicked
    public void submitBookQuery(View v){
        mEmptyStateTextView.clearComposingText();
        mEmptyStateTextView.setVisibility(View.GONE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
        mAdapter.clear();
        EditText bookEntered = findViewById(R.id.book_title_field);
        String bookTitle = bookEntered.getText().toString();

        if(bookTitle != "") {
            String bookRequestURL = "https://www.googleapis.com/books/v1/volumes?q=" + bookTitle + "&maxResults=10";
            BooksTask task = new BooksTask();
            task.execute(bookRequestURL);
        }

    }
    public void registerNetworkCallback(Context context)
    {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkRequest.Builder builder = new NetworkRequest.Builder();

            connectivityManager.registerNetworkCallback(builder.build(),new ConnectivityManager.NetworkCallback() {
                @Override
                public void onAvailable(Network network) {
                    isNetworkConnected = true;
                }
                @Override
                public void onLost(Network network) {
                    isNetworkConnected = false;
                }
                @Override
                public void onUnavailable(){
                    isNetworkConnected = false;
                }
            });
            isNetworkConnected = false;
        }catch (Exception e){
            isNetworkConnected = false;
        }
    }
    private class BooksTask extends AsyncTask<String, Void, List<Book>> {

        @Override
        protected List<Book> doInBackground(String... urls) {
            try{
                if(isNetworkConnected){
                    return QueryUtils.fetchBookData(urls[0]);
                }
                else{
                    return null;
                }
            }
            catch (Exception e){
                //TODO: handle this better
                Log.e("BooksTask", "Problem in doInBackground ", e);
                return null;
            }
        }
        @Override
        protected void onPostExecute(List<Book> books){
            mLoadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_books);
            mEmptyStateTextView.setVisibility(View.VISIBLE);

            mAdapter.clear();

            if(!isNetworkConnected){
                mEmptyStateTextView.setText(R.string.no_internet);

            }
            else if(books != null && !books.isEmpty()){
                mBookListViewModel.setBooks(books);
                mAdapter.addAll(books);
            }
        }
    }

}
