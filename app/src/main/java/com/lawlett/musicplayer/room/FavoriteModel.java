package com.lawlett.musicplayer.room;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class FavoriteModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String mediaArt;
    private String mediaArtist;
    private String songUrl;
    private String year;
    private String idSong;

    public String getIdSong() {
        return idSong;
    }

    public void setIdSong(String idSong) {
        this.idSong = idSong;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public FavoriteModel(String title, String mediaArt, String mediaArtist, String songUrl, String year,String idSong) {
        this.title = title;
        this.mediaArt = mediaArt;
        this.mediaArtist = mediaArtist;
        this.songUrl=songUrl;
        this.year = year;
        this.idSong = idSong;
    }

    public String getSongUrl() {
        return songUrl;
    }

    public void setSongUrl(String songUrl) {
        this.songUrl = songUrl;
    }

    public String getMediaArtist() {
        return mediaArtist;
    }

    public void setMediaArtist(String mediaArtist) {
        this.mediaArtist = mediaArtist;
    }

    public String getMediaArt() {
        return mediaArt;
    }

    public void setMediaArt(String mediaArt) {
        this.mediaArt = mediaArt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}