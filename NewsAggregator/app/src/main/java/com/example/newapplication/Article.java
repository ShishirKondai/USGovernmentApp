package com.example.newapplication;

public class Article {
    private String headline, date, authorNames;
    private String articleURL, imageURL, description;

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return authorNames;
    }

    public void setAuthor(String authorNames) {
        this.authorNames = authorNames;
    }

    public String getUrl() {
        return articleURL;
    }

    public void setUrl(String URL) {
        this.articleURL = URL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String urlToImage) {
        this.imageURL = urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
