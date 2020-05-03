package com.mynotebook.englishonthego.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.mynotebook.englishonthego.model.VocabModel;

import java.util.List;

@Dao
public interface VocabDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertVocab(VocabModel vocabModel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAllVocab(List<VocabModel> allSampleVocabs);

    @Delete
    void deleteVocab(VocabModel vocabModel);

    @Query("SELECT * FROM vocabsTable WHERE id=:id")
    VocabModel getVocabByID(int id);

    @Query("SELECT * FROM vocabsTable ORDER BY vocab ASC")
    LiveData<List<VocabModel>> getAllVocab();

    @Query("DELETE FROM vocabsTable")
    int deleteALL();

    @Query("SELECT COUNT(*) FROM vocabstable")
    int getCount();

}
