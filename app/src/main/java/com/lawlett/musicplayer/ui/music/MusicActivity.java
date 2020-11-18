/*
 * This is the source code of DMAudioStreaming for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry(dibakar.ece@gmail.com), 2017.
 */
package com.lawlett.musicplayer.ui.music;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lawlett.musicplayer.R;
//import dm.audiostreamerdemo.R;
//import dm.audiostreamerdemo.adapter.AdapterMusic;
//import dm.audiostreamerdemo.network.MusicBrowser;
//import dm.audiostreamerdemo.network.MusicLoaderListener;
//import dm.audiostreamerdemo.widgets.LineProgress;
//import dm.audiostreamerdemo.widgets.PlayPauseView;
//import dm.audiostreamerdemo.widgets.Slider;

public class MusicActivity extends AppCompatActivity {

    private static final String TAG = MusicActivity.class.getSimpleName();

    NavController navController;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideStatusBar();
        setContentView(R.layout.activity_music);


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.historyFragment) {
                navController.navigate(R.id.historyFragment);
            } else if (item.getItemId() == R.id.listFragment) {
                navController.navigate(R.id.listFragment);
            } else if (item.getItemId() == R.id.yearsFragment) {
                navController.navigate(R.id.yearsFragment);
            }
            return true;
        });

    }

    private void hideStatusBar() {
        int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        getWindow().getDecorView().setSystemUiVisibility(flags);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }


}