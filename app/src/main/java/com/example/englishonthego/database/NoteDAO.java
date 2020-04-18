package com.example.englishonthego.database;

import android.icu.text.Replaceable;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.englishonthego.model.NoteModel;

import java.util.List;

@Dao
public interface NoteDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNotes(List<NoteModel> allSampleNotes);

    @Query("SELECT * FROM notesTable ORDER BY date DESC")
    LiveData<List<NoteModel>> getAllNotes();

    @Query("DELETE FROM notesTable")
    int deleteAll();
}