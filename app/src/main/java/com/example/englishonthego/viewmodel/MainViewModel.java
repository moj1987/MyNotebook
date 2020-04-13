package com.example.englishonthego.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.englishonthego.database.AppRepository;
import com.example.englishonthego.model.VocabModel;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModel extends AndroidViewModel {
    /**
     * Live Data instance
     */
    public LiveData<List<VocabModel>> mLiveVocab;
    private AppRepository mAppRepository;
    private Executor executor = Executors.newSingleThreadExecutor();

    public MainViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(application.getApplicationContext());
        mLiveVocab = mAppRepository.mVocabs;
    }

    //  NOT wrapped in LiveData.
    public void loadVocab(int vocabID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                VocabModel vocab = mAppRepository.getVocabByID(vocabID);
            }
        });
    }


}
