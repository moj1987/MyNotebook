package com.example.englishonthego.viewmodel;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.englishonthego.VocabEditorActivity;
import com.example.englishonthego.database.AppRepository;
import com.example.englishonthego.model.VocabModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VocabEditorViewModel extends AndroidViewModel {
//    private static final String TAG = "VocabEditorViewModel";
    private static final String TAG = "testTTTT-EditorVM";

    private AppRepository mAppRepository;
    private MutableLiveData<VocabModel> mLiveVocab = new MutableLiveData<>();
    private Executor executor = Executors.newSingleThreadExecutor();

    public VocabEditorViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(getApplication());
    }

    //  NOT wrapped in LiveData. Hence executor.
    public void loadVocab(int vocabID) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                VocabModel vocab = mAppRepository.getVocabByID(vocabID);
            }
        });
    }

    public void saveVocab(String vocabText, String definitionText, String exampleText) {
        VocabModel vocab = mLiveVocab.getValue();
        if (vocab == null) {
            if (TextUtils.isEmpty(vocabText) | TextUtils.isEmpty(definitionText) | TextUtils.isEmpty(exampleText)) {
                Log.d(TAG, "it was empty. Returning");
                return;
            }
            vocab = new VocabModel(vocabText, definitionText, exampleText);
            Log.d(TAG, "new Vocab created: " + vocab.toString());
        } else {
//            TODO: 1. why the new word is saved even though it's not set?
//             2. No setter for data class?
//          vocab.
            Log.d(TAG, "vocab replaced");
        }
        mAppRepository.insertVocab(vocab);
        Log.d(TAG, "vocab inserted in database");
    }

    public void deleteVocab() {
        mAppRepository.deleteVocab(mLiveVocab.getValue());
    }
}
