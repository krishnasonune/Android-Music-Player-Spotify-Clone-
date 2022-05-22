package com.example.navmusic.Models;

public class favourites {
    private String songname;

    public favourites() {
    }

    public favourites(String songname) {
        this.songname = songname;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }
}
