package com.mynotebook.englishonthego;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public class SqliteDatabaseTestHelper {

    public static void insertData(int id, String vocab, String definition, String example, SqliteTestDbOpenHelper helper ) {
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id", id);
        values.put("vocab", vocab);
        values.put("definition", definition);
        values.put("example", example);

        db.insertWithOnConflict("vocabsTable", null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }


    public static void createTables(SqliteTestDbOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("CREATE TABLE IF NOT EXISTS 'vocabsTable' ('id' INTEGER PRIMARY KEY NOT NULL, 'vocab' TEXT, " +
                "'definition' TEXT, 'example' TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS 'notesTable' ('id' INTEGER PRIMARY KEY NOT NULL, 'date' INTEGER, " +
                "'title' TEXT, 'text' TEXT)");
        db.close();
    }

    public static void clearDatabase(SqliteTestDbOpenHelper helper) {
        SQLiteDatabase db = helper.getWritableDatabase();

        db.execSQL("DROP TABLE IF EXISTS vocabsTable");

        db.close();
    }
}
