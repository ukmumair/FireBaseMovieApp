package com.umair.moviesapp;

public class MoviesModel {
    private String thumbnail,title,url,movie_type,movie_genre,download_url;

    public MoviesModel() {
    }

    public MoviesModel(String thumbnail, String title, String url, String movie_type, String movie_genre, String download_url) {
        this.thumbnail = thumbnail;
        this.title = title;
        this.url = url;
        this.movie_type = movie_type;
        this.movie_genre = movie_genre;
        this.download_url = download_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMovie_type() {
        return movie_type;
    }

    public void setMovie_type(String movie_type) {
        this.movie_type = movie_type;
    }

    public String getMovie_genre() {
        return movie_genre;
    }

    public void setMovie_genre(String movie_genre) {
        this.movie_genre = movie_genre;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
