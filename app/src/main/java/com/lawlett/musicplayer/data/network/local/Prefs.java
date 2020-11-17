package com.lawlett.musicplayer.data.network.local;

import android.content.Context;
import android.content.SharedPreferences;

public class Prefs {
    private SharedPreferences sharedPreferences;

    public Prefs(Context context) {
        sharedPreferences = context.getSharedPreferences("settings",Context.MODE_PRIVATE);
    }

    public void setYear(String year){
        sharedPreferences.edit().putString("isShown",year).apply();
    }

    public String getYear(){
        return sharedPreferences.getString("isShown","2020");
    }
}

