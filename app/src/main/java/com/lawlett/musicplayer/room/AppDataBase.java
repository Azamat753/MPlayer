package com.lawlett.musicplayer.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = FavoriteModel.class,version = 1,exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {

    public abstract FavoriteDao favoriteDao();

}
