package com.example.englishonthego.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.englishonthego.model.NoteModel;
import com.example.englishonthego.model.VocabModel;
import com.example.englishonthego.networking.Lyric;
import com.example.englishonthego.utilities.SampleNote;
import com.example.englishonthego.utilities.SampleVocab;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<VocabModel>> mVocabs;
    public LiveData<List<NoteModel>> mNotes;

    private AppDatabase mDb;
    private Executor executor = Executors.newSingleThreadExecutor();

    public static AppRepository getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new AppRepository(context);
        }
        return ourInstance;
    }

    private AppRepository(Context context) {
        mDb = AppDatabase.getInstance(context);
        mVocabs = getAllVocabs();
        mNotes = getAllNotes();
    }

    /**
     * Vocab
     */
    private LiveData<List<VocabModel>> getAllVocabs() {
        return mDb.vocabDAO().getAllVocab();
    }

    public VocabModel getVocabByID(int vocabID) {
        return mDb.vocabDAO().getVocabByID(vocabID);
    }

    public void insertVocab(VocabModel vocab) {
        executor.execute(() -> mDb.vocabDAO().insertVocab(vocab));
    }

    public void deleteVocab(VocabModel vocab) {
        executor.execute(() -> mDb.vocabDAO().deleteVocab(vocab));
    }

    public void deleteAllNotes() { executor.execute(() -> mDb.noteDAO().deleteAll()); }

    public void addSampleVocab() {
        executor.execute(() -> mDb.vocabDAO().insertAllVocab(SampleVocab.INSTANCE.getAllVocab()));
    }

    /**
     * Note
     */
    private LiveData<List<NoteModel>> getAllNotes() { return mDb.noteDAO().getAllNotes(); }

    public NoteModel getNoteByID(int noteID) { return mDb.noteDAO().getNotedByID(noteID); }

    public void insertNote(NoteModel note) { executor.execute(() -> mDb.noteDAO().insertNote(note)); }

    public void deleteAllVocab() { executor.execute(() -> mDb.vocabDAO().deleteALL()); }

    public void addSampleNote() {
        executor.execute(() -> mDb.noteDAO().insertAllNotes(SampleNote.INSTANCE.getAllNotes()));
    }
}
