package com.example.englishonthego.viewmodel;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.englishonthego.database.AppDatabase;
import com.example.englishonthego.database.AppRepository;
import com.example.englishonthego.model.NoteModel;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NoteEditorViewModel extends AndroidViewModel {
    private AppRepository mAppRepository;
    private Executor executor = Executors.newSingleThreadExecutor();
    public MutableLiveData<NoteModel> mLiveNote = new MutableLiveData<>();

    public NoteEditorViewModel(@NonNull Application application) {
        super(application);
        mAppRepository = AppRepository.getInstance(getApplication());
    }

    public void loadNote(int noteID) {
        executor.execute(() -> {
            NoteModel noteModel = mAppRepository.getNoteByID(noteID);
            mLiveNote.postValue(noteModel);
        });
    }

    public void saveNote(String titleText, String noteText) {
        NoteModel note = mLiveNote.getValue();
        if (note == null) {
            if (TextUtils.isEmpty(titleText) | TextUtils.isEmpty(noteText)) {
                return;
            }
            note = new NoteModel(new Date(), titleText, noteText);
        } else {
            note.setDate(new Date());
            note.setTitle(titleText);
            note.setText(noteText);
        }
        mAppRepository.insertNote(note);
    }

    public void deleteNote() {
        mAppRepository.deleteNote(mLiveNote.getValue());
    }
}
