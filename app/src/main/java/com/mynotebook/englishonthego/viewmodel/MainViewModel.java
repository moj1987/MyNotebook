package com.mynotebook.englishonthego.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.mynotebook.englishonthego.database.AppRepository;
import com.mynotebook.englishonthego.model.NoteModel;
import com.mynotebook.englishonthego.model.VocabModel;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    /**
     * Live Data instances
     */
    public LiveData<List<VocabModel>> mLiveVocab;
    public LiveData<List<NoteModel>> mLiveNote;

    private AppRepository mAppRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(application.getApplicationContext());
        mLiveVocab = mAppRepository.mVocabs;
        mLiveNote = mAppRepository.mNotes;
    }

    public void addSampleVocabs() { mAppRepository.addSampleVocab(); }

    public void deleteAllVocabs() {
        mAppRepository.deleteAllVocab();
    }


    public void addSampleNotes() {
        mAppRepository.addSampleNote();
    }

    public void deleteAllNotes() {
        mAppRepository.deleteAllNotes();
    }
}
