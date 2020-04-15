package com.example.englishonthego.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.englishonthego.database.AppRepository;
import com.example.englishonthego.model.VocabModel;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    /**
     * Live Data instance
     */
    public LiveData<List<VocabModel>> mLiveVocab;
    private AppRepository mAppRepository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(application.getApplicationContext());
        mLiveVocab = mAppRepository.mVocabs;
    }




}
