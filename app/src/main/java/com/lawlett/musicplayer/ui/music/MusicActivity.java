/*
 * This is the source code of DMAudioStreaming for Android v. 1.0.0.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Copyright @Dibakar_Mistry(dibakar.ece@gmail.com), 2017.
 */
package com.lawlett.musicplayer.ui.music;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lawlett.musicplayer.R;

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