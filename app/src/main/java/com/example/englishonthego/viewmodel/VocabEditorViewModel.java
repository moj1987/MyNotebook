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

public class VocabEditorViewModel extends AndroidViewModel {
    private static final String TAG = "VocabEditorViewModel";

    private AppRepository mAppRepository;
    private MutableLiveData<VocabModel> mLiveVocab = new MutableLiveData<>();

    public VocabEditorViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(getApplication());
    }

    public void saveVocab(String vocabText, String definitionText, String exampleText) {
        VocabModel vocab = mLiveVocab.getValue();
        if (vocab == null) {
            if (TextUtils.isEmpty(vocabText)) {
                Log.d(TAG, "it was empty. Returning");
                return;
            }
            vocab = new VocabModel(vocabText, definitionText, exampleText);
            Log.d(TAG, "new Vocab created: " + vocab.toString());

        } else {
//          vocab.setText
            Log.d(TAG, "vocab replaced");
        }
        mAppRepository.insertVocab(vocab);
    }
}
