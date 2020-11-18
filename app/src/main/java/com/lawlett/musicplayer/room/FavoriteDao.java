package com.lawlett.musicplayer.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FavoriteDao {
    @Query("SELECT*FROM favoritemodel")
    List<FavoriteModel> getAll();

    @Query("SELECT*FROM favoritemodel")
    LiveData<List<FavoriteModel>> getAllLive();

    @Insert
    void insert(FavoriteModel favoritemodel);

    @Delete
    void delete(FavoriteModel favoritemodel);

    @Delete
    void deleteAll(List<FavoriteModel> favoritemodel);

    @Update
    void update(FavoriteModel favoritemodel);
}
