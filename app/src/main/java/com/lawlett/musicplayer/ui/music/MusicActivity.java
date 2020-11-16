/*
 * This is the source code of DMAudioStreaming for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry(dibakar.ece@gmail.com), 2017.
 */
package com.lawlett.musicplayer.ui.music;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.DateUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lawlett.musicplayer.R;
import com.lawlett.musicplayer.adapter.AdapterMusic;
import com.lawlett.musicplayer.data.network.MusicBrowser;
import com.lawlett.musicplayer.data.network.MusicLoaderListener;
import com.lawlett.musicplayer.widgets.LineProgress;
import com.lawlett.musicplayer.widgets.PlayPauseView;
import com.lawlett.musicplayer.widgets.Slider;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import dm.audiostreamer.AudioStreamingManager;
import dm.audiostreamer.CurrentSessionCallback;
import dm.audiostreamer.Logger;
import dm.audiostreamer.MediaMetaData;
//import dm.audiostreamerdemo.R;
//import dm.audiostreamerdemo.adapter.AdapterMusic;
//import dm.audiostreamerdemo.network.MusicBrowser;
//import dm.audiostreamerdemo.network.MusicLoaderListener;
//import dm.audiostreamerdemo.widgets.LineProgress;
//import dm.audiostreamerdemo.widgets.PlayPauseView;
//import dm.audiostreamerdemo.widgets.Slider;

public class MusicActivity extends AppCompatActivity{

    private static final String TAG = MusicActivity.class.getSimpleName();

    NavController navController;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        navController = Navigation.findNavController(this,R.id.nav_host_fragment);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.historyFragment){
                navController.navigate(R.id.historyFragment);
            }else if (item.getItemId() == R.id.listFragment){
                navController.navigate(R.id.listFragment);
            }else if (item.getItemId() == R.id.yearsFragment){
                navController.navigate(R.id.yearsFragment);
            }
            return true;
        });

    }


}