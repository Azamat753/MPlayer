package com.lawlett.musicplayer.models;

public class SongModel {
    String song;
    String artist;
    String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public SongModel(String song, String artist) {
        this.song = song;
        this.artist = artist;
    }

    public String getSong() {
        return song;
    }

    public String getArtist() {
        return artist;
    }
}
