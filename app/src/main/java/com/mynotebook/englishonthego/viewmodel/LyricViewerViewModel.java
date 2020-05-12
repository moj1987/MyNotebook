package com.mynotebook.englishonthego.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.mynotebook.englishonthego.database.AppRepository;
import com.mynotebook.englishonthego.model.LyricSaveModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LyricViewerViewModel extends AndroidViewModel {

    private AppRepository mAppRepository;
    public MutableLiveData<LyricSaveModel> mLiveLyric = new MutableLiveData<>();
    private Executor executor = Executors.newSingleThreadExecutor();

    public LyricViewerViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(getApplication());
    }

    public void loadLyric(int lyricID) {
        executor.execute(() -> {
            LyricSaveModel lyricSaveModel = mAppRepository.getLyricByID(lyricID);
            mLiveLyric.postValue(lyricSaveModel);
        });
    }

    public void saveLyric(int id, String trackName, String artistName, String albumName, String albumCoverUrl, String lyric) {
        LyricSaveModel lyricSaveModel = mLiveLyric.getValue();
        if (lyricSaveModel == null) {
            lyricSaveModel = new LyricSaveModel(id, trackName, artistName, albumName, albumCoverUrl, lyric);
            mAppRepository.insertLyric(lyricSaveModel);
        }
    }

    public void deleteLyric() {
        mAppRepository.deleteLyric(mLiveLyric.getValue());
    }


}
