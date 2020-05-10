package com.mynotebook.englishonthego.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mynotebook.englishonthego.model.LyricSaveModel;
import com.mynotebook.englishonthego.model.NoteModel;
import com.mynotebook.englishonthego.model.VocabModel;

@Database(entities = {VocabModel.class, NoteModel.class, LyricSaveModel.class}, version = 2, exportSchema = true)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DATABASE_NAME = "AppDatabase.db";

    private static volatile AppDatabase instance;

    private static final Object LOCK = new Object();

    public abstract VocabDAO vocabDAO();

    public abstract NoteDAO noteDAO();

    public abstract LyricDAO lyricDAO();

    /**
     * Database migration from version 1 to version 2
     * lyrics table added to database
     */
    @VisibleForTesting
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS 'lyricsTable' ('id' INTEGER PRIMARY KEY NOT NULL, " +
                    "'trackName' TEXT NOT NULL, " +
                    "'artistName' TEXT NOT NULL, " +
                    "'albumName' TEXT NOT NULL, " +
                    "'albumCoverUrl' TEXT NOT NULL, " +
                    "'lyric' TEXT NOT NULL)");
        }
    };

    public static AppDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,
                            DATABASE_NAME)
                            .addMigrations(MIGRATION_1_2)
                            .build();
                }
            }
        }
        return instance;
    }
}