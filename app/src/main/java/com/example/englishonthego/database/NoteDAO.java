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
    void insertNote(NoteModel noteModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllNotes(List<NoteModel> allSampleNotes);

    @Delete
    void deleteNote(NoteModel noteModel);

    @Query("SELECT * FROM notesTable ORDER BY date DESC")
    LiveData<List<NoteModel>> getAllNotes();

    @Query("SELECT * FROM notesTable WHERE id=:id")
    NoteModel getNotedByID(int id);

    @Query("DELETE FROM notesTable")
    int deleteAll();

    @Query("SELECT COUNT(*) FROM notesTable")
    int getCount();

}