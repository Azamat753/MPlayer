/*
 * This is the source code of DMAudioStreaming for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry(dibakar.ece@gmail.com), 2017.
 */
package com.lawlett.musicplayer.network;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;


import com.lawlett.musicplayer.models.SongModel;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import dm.audiostreamer.MediaMetaData;

public class MusicBrowser {

    public static final String music = "https://firebasestorage.googleapis.com/v0/b/dmaudiostreamer.appspot.com/o/music.json?alt=media&token=64ac05a8-2f23-4cef-b25c-b488519b0650";

    public static String RESPONSE_CODE_SUCCESS = "200";
    public static String RESPONSE_CODE_CONNECTION_TIMEOUT = "9001";
    public static String RESPONSE_CODE_SOCKET_TIMEOUT = "903";

    public static ArrayList<String> songsURL = new ArrayList<>();
    public static ArrayList<MediaMetaData> songModels = new ArrayList<>();

    public static final String urlHome = "http://hotcharts.ru/";

    private static final String BASE_URL_FOR_SONG = "http://45.88.104.12//hc/preview/temp_067TG/";
    private static final String MP3 = ".mp3";

    public static void loadMusic(final Context context, final MusicLoaderListener loaderListener) {

        final AsyncTask<Void, Void, Void> loadTask = new AsyncTask<Void, Void, Void>() {
            String[] resp = {"", ""};
            List<MediaMetaData> listMusic = new ArrayList<>();

            @Override
            protected Void doInBackground(Void... voids) {
                //resp = getDataResponse();
                try {
                    Document doc = Jsoup.connect(urlHome)
                            .referrer("http://hotcharts.ru/")
                            .get();
                    final Elements els = doc.select("td[class=song]");
                    for (int i = 0; i < els.size(); i++) {
                        final Element as = els.get(i);
                        String url1 = as.getElementsByTag("a").attr("href");
                        songsURL.add(url1);
                        String artist = as.getElementsByTag("a").attr("data-artist-name");
                        String song = as.getElementsByTag("a").attr("data-song-name");
                        MediaMetaData mediaMetaData = new MediaMetaData();
                        mediaMetaData.setMediaArtist(artist);
                        mediaMetaData.setMediaTitle(song);
                        mediaMetaData.setMediaArt("art");
                        mediaMetaData.setMediaAlbum("album");
                        mediaMetaData.setMediaComposer("composer");
                        mediaMetaData.setMediaDuration("4");
                        mediaMetaData.setMediaId("id" + i);
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
                    final String url = BASE_URL_FOR_SONG + mp3Url + MP3;
                    songModels.get(i).setMediaUrl(url);
                }

            } catch(
            IOException e)

            {
                e.printStackTrace();
            }

            listMusic =songModels;
                return null;
        }

        @Override
        protected void onPostExecute (Void aVoid){
            super.onPostExecute(aVoid);

            if (loaderListener != null && listMusic != null && listMusic.size() >= 1) {
                loaderListener.onLoadSuccess(listMusic);
            } else {
                loaderListener.onLoadFailed();
            }
        }
    }

    ;
        loadTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
}

    public static List<MediaMetaData> getMusicList(String response, String name) {
        List<MediaMetaData> listArticle = new ArrayList<>();
        try {
            JSONArray array = new JSONObject(response).getJSONArray(name);
            for (int i = 0; i < array.length(); i++) {
                MediaMetaData infoData = new MediaMetaData();
                JSONObject musicObj = array.getJSONObject(i);
                infoData.setMediaId(musicObj.optString("id"));
                infoData.setMediaUrl(musicObj.optString("site") + musicObj.optString("source"));
                infoData.setMediaTitle(musicObj.optString("title"));
                infoData.setMediaArtist(musicObj.optString("artist"));
                infoData.setMediaAlbum(musicObj.optString("album"));
                infoData.setMediaComposer(musicObj.optString(""));
                infoData.setMediaDuration(musicObj.optString("duration"));
                infoData.setMediaArt(musicObj.optString("site") + musicObj.optString("image"));
                listArticle.add(infoData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listArticle;
    }

    public static String[] getDataResponse() {
        String[] result = {"", ""};
        try {
            URL url = new URL(music);
            HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setConnectTimeout(20000);
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (Build.VERSION.SDK_INT > 13) {
                urlConnection.setRequestProperty("Connection", "close");
            }

            urlConnection.setUseCaches(false);
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            // always check HTTP response code first
            int responseCode = urlConnection.getResponseCode();
            result[0] = responseCode + "";

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Get Response
                InputStream is = urlConnection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();

                if (!TextUtils.isEmpty(response)) {
                    result[0] = RESPONSE_CODE_SUCCESS;
                    result[1] = response.toString();
                }
            }

        } catch (UnsupportedEncodingException e) {
            result[0] = RESPONSE_CODE_CONNECTION_TIMEOUT;
            e.printStackTrace();
        } catch (ConnectTimeoutException e) {
            result[0] = RESPONSE_CODE_CONNECTION_TIMEOUT;
            e.printStackTrace();
        } catch (IOException e) {
            result[0] = RESPONSE_CODE_CONNECTION_TIMEOUT;
            e.printStackTrace();
        } catch (Exception e) {
            result[0] = RESPONSE_CODE_CONNECTION_TIMEOUT;
            e.printStackTrace();
        }
        return result;
    }
}
