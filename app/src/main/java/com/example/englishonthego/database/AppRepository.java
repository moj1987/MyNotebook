package com.example.englishonthego.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.englishonthego.model.VocabModel;
import com.example.englishonthego.networking.Lyric;
import com.example.englishonthego.sampleData.SampleVocab;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private Lyric mLyrics;

    private static AppRepository ourInstance;

    public LiveData<List<VocabModel>> mVocabs;
    private VocabDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();


    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    public AppRepository(Context context) {
        mDb = VocabDatabase.getInstance(context);
        mVocabs= getAllVocabs();
    }

    public void addSampleVocab(){
        executor.execute(() -> mDb.vocabDAO().insertAllVocab(SampleVocab.INSTANCE.getAllVocab()));
    }

    private LiveData<List<VocabModel>> getAllVocabs() {
        return mDb.vocabDAO().getAllVocab();
    }


    public VocabModel getVocabByID(int vocabID) {
        return mDb.vocabDAO().getVocabByID(vocabID);
    }

    public void insertVocab(VocabModel vocab) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                mDb.vocabDAO().insertVocab(vocab);
            }
        });
    }
}
