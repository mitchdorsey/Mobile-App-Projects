package com.example.android.booklistingapp;

public class Book {

    private String mTitle;
    private String mAuthor;
    private short mPageCount;

    public Book(String title, String author, short pageCount){
       mTitle = title;
       mAuthor = author;
       mPageCount = pageCount;
    }
    public String getTitle(){return mTitle;}
    public String getAuthor(){return mAuthor;}
    public short getPageCount(){return mPageCount;}
}
