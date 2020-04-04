package com.example.englishonthego.database;

import android.content.Context;

import com.example.englishonthego.networking.Lyric;

public class LyricsRepository {
    private Lyric mLyrics;

    private static LyricsRepository ourInstance;

    public static LyricsRepository getInstance(Context context){
        if (ourInstance==null){
            ourInstance = new LyricsRepository(context);
        }
        return ourInstance;
    }

    public LyricsRepository(Context context) {
        mLyrics= getLyrics();

    }

    private Lyric getLyrics() {
        return null;
    }


}
