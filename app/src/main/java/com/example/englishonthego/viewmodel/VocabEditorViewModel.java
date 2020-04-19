package com.example.englishonthego.viewmodel;

import android.app.Application;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.englishonthego.database.AppRepository;
import com.example.englishonthego.model.VocabModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VocabEditorViewModel extends AndroidViewModel {
    private static final String TAG = "VocabEditorViewModel";

    private AppRepository mAppRepository;
    public MutableLiveData<VocabModel> mLiveVocab = new MutableLiveData<>();
    private Executor executor = Executors.newSingleThreadExecutor();

    public VocabEditorViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(getApplication());
    }

    //  NOT wrapped in LiveData. Hence executor.
    public void loadVocab(int vocabID) {
        executor.execute(() -> {
            VocabModel vocab = mAppRepository.getVocabByID(vocabID);
            mLiveVocab.postValue(vocab);
        });
    }

    public void saveVocab(String vocabText, String definitionText, String exampleText) {
        VocabModel vocab = mLiveVocab.getValue();
        if (vocab == null) {
            if (TextUtils.isEmpty(vocabText) | TextUtils.isEmpty(definitionText) | TextUtils.isEmpty(exampleText)) {
                return;
            }
            vocab = new VocabModel(vocabText, definitionText, exampleText);
        } else {
            vocab.setVocab(vocabText);
            vocab.setDefinition(definitionText);
            vocab.setExample(exampleText);
        }
        mAppRepository.insertVocab(vocab);
    }

    public void deleteVocab() {
        mAppRepository.deleteVocab(mLiveVocab.getValue());
    }
}
