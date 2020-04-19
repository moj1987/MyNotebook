package com.example.englishonthego.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.englishonthego.model.NoteModel;
import com.example.englishonthego.model.VocabModel;

@Database(entities = {VocabModel.class, NoteModel.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "AppDatabase.db";
    private static volatile AppDatabase instance;
    private static final Object LOCK = new Object();

    public abstract VocabDAO vocabDAO();
    public abstract NoteDAO noteDAO();
//    TODO: LyricsDAO

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DATABASE_NAME).build();
                }
            }
        }
        return instance;
    }

}
