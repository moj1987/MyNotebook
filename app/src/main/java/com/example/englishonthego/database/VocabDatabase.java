package com.example.englishonthego.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.englishonthego.model.VocabModel;

@Database(entities = {VocabModel.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class VocabDatabase extends RoomDatabase {
    public static final String DATABASE_NAME = "VocabsDatabase.db";
    private static volatile VocabDatabase instance;
    private static final Object LOCK = new Object();

    public abstract VocabDAO vocabDAO();
//    TODO: NotesDAO
//    TODO: LyricsDAO

    public static VocabDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            VocabDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}
