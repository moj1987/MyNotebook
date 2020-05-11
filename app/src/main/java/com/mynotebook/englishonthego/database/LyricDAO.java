package com.mynotebook.englishonthego.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mynotebook.englishonthego.model.LyricSaveModel;

import java.util.List;

@Dao
public interface LyricDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertLyric(LyricSaveModel lyricSaveModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllLyrics(List<LyricSaveModel> allSampleLyrics);

    @Delete
    void deleteLyric(LyricSaveModel lyricSaveModel);

    @Query("SELECT * FROM lyricsTable WHERE id=:id")
    LyricSaveModel getLyricByID(int id);

    @Query("SELECT * FROM lyricsTable ORDER BY  trackName ASC")
    LiveData<List<LyricSaveModel>> getAllLyrics();

    @Query("SELECT COUNT(*) FROM lyricsTable")
    int getCount();
}
