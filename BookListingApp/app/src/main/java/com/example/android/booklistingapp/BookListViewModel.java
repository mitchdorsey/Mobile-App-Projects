package com.example.android.booklistingapp;

import androidx.lifecycle.ViewModel;

import java.util.List;

public class BookListViewModel extends ViewModel {
    private List<Book> mBooks;

    public List<Book> getBooks() {
        return mBooks;
    }
    public void setBooks(List<Book> books){
        mBooks = books;
    }
}