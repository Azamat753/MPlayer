package com.lawlett.musicplayer;

import android.app.Application;

import androidx.room.Room;

import com.lawlett.musicplayer.room.AppDataBase;

public class App extends Application {
    public static App instance;
    private static AppDataBase dataBase;
    @Override
    public void onCreate() {
        super.onCreate();
        dataBase = Room.databaseBuilder(this, AppDataBase.class, "favorite")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }
    public static AppDataBase getDataBase() {
        return dataBase;
    }
}
