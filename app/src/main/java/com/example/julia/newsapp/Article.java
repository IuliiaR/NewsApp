package com.example.julia.newsapp;

public class Article {
    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mPublishedDate;
    private String mUrl;
    private String mImageUrl;

    public Article(String title, String section, String author, String publishedDate, String url, String thumbnail)
    {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mPublishedDate = publishedDate;
        mUrl = url;
        mImageUrl = thumbnail;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
