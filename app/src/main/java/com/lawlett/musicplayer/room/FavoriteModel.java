package com.lawlett.musicplayer.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import dm.audiostreamer.MediaMetaData;

@Entity
public class FavoriteModel implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String mediaArt;
    private String mediaArtist;
    private String songUrl;

    public FavoriteModel(String title,String mediaArt,String mediaArtist,String songUrl) {
        this.title = title;
        this.mediaArt = mediaArt;
        this.mediaArtist = mediaArtist;
        this.songUrl=songUrl;
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