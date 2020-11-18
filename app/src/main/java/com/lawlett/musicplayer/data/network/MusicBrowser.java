/*
 * This is the source code of DMAudioStreaming for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry(dibakar.ece@gmail.com), 2017.
 */
package com.lawlett.musicplayer.data.network;


import android.content.Context;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dm.audiostreamer.MediaMetaData;

public class MusicBrowser {

    public static ArrayList<String> songsURL = new ArrayList<>();
    public static ArrayList<MediaMetaData> songModels = new ArrayList<>();

    public static final String BASE_URL = "http://hotcharts.ru/europaplus/archive/";
    public static final String HTM = ".htm";

    private static final String BASE_URL_FOR_SONG = "http://45.88.104.12//hc/preview/temp_067TG/";

//    private static final String BASE_URL_FOR_SONG_ID = "http://hotcharts.ru/mp3/?song=";

    private static final String BASE_URL_FOR_SONG2 = "http://45.80.69.117/hc/preview/temp_067TG/";

    private static final String MP3 = ".mp3";

    //    https://img.youtube.com/vi/KCHgxoXv4g4/default.jpg
    public static void loadMusic(String baseUrl, final Context context, final MusicLoaderListener loaderListener) {

        if (songModels!= null || songModels.size()>0 || !songModels.isEmpty()){
            songModels.clear();
        }
        if (songsURL!= null || songsURL.size()>0 || !songsURL.isEmpty()){
            songsURL.clear();
        }

        final AsyncTask<Void, Void, Void> loadTask = new AsyncTask<Void, Void, Void>() {
            String[] resp = {"", ""};
            List<MediaMetaData> listMusic = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... voids) {
                //resp = getDataResponse();
                try {
                    Document doc = Jsoup.connect(BASE_URL + baseUrl + HTM)
//                            .referrer(baseUrl)
                            .get();
                    final Elements els = doc.select("td[class=song]");
                    for (int i = 0; i < els.size(); i++) {
                        final Element as = els.get(i);
                        String url1 = as.getElementsByTag("a").attr("href");
                        songsURL.add(url1);
                        String artist = as.getElementsByTag("a").attr("data-artist-name");
                        String song = as.getElementsByTag("a").attr("data-song-name");
                        String id_song = as.getElementsByTag("a").attr("data-song-id");
                        MediaMetaData mediaMetaData = new MediaMetaData();
                        mediaMetaData.setMediaArtist(artist);
                        mediaMetaData.setMediaTitle(song);
                        mediaMetaData.setMediaArt("https://png.pngtree.com/element_our/png_detail/20181013/music-icon-design-vector-png_133746.jpg");
                        mediaMetaData.setMediaAlbum("album");
                        mediaMetaData.setMediaComposer("composer");
                        mediaMetaData.setMediaId("id" + i);
                        mediaMetaData.setMediaDuration("210");
                        songModels.add(mediaMetaData);
                    }

                    for (int i = 0; i < songsURL.size(); i++) {
                        char[] chars = songsURL.get(i).toString().toCharArray();
                        String mp3Url = "";
                        for (int j = 0; j < chars.length; j++) {
                            if (chars[j] != '#') {
                                if (chars[j] == ' ') {
                                    mp3Url = mp3Url + "%20";
                                } else if (chars[j] == '\'') {
                                    mp3Url = mp3Url + "%27";
                                } else if (chars[j] == '&') {
                                    mp3Url = mp3Url + "%26";
                                } else if (chars[j] == ')') {
                                    mp3Url = mp3Url + "%29";
                                } else if (chars[j] == '(') {
                                    mp3Url = mp3Url + "%28";
                                } else {
                                    mp3Url = mp3Url + chars[j];
                                }
                            }
                        }
                        String url = "";
                        if (baseUrl.equals("2020") || baseUrl.equals("2019")) {
                            url = BASE_URL_FOR_SONG2 + mp3Url + MP3;
                        } else {
                            url = BASE_URL_FOR_SONG + mp3Url + MP3;
                        }
                        songModels.get(i).setMediaUrl(url);
                    }

                } catch (
                        IOException e) {
                    e.printStackTrace();
                }
                if (listMusic != null || listMusic.size() > 0 || !listMusic.isEmpty()){
                    listMusic.clear();
                }
                listMusic = songModels;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                if (loaderListener != null && listMusic != null && listMusic.size() >= 1) {
                    loaderListener.onLoadSuccess(listMusic);
                } else {
                    loaderListener.onLoadFailed();
                }
            }
        };
        loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
}
