package com.example.navmusic.Models;

public class Songs {
    private String artist, name, url, cover;

    public Songs() {
    }

    public Songs(String artist, String name, String url, String cover) {
        this.artist = artist;
        this.name = name;
        this.url = url;
        this.cover = cover;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

}
