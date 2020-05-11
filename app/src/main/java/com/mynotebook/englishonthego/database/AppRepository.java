package com.mynotebook.englishonthego.database;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mynotebook.englishonthego.model.LyricSaveModel;
import com.mynotebook.englishonthego.model.NoteModel;
import com.mynotebook.englishonthego.model.VocabModel;
import com.mynotebook.englishonthego.utilities.SampleLyric;
import com.mynotebook.englishonthego.utilities.SampleNote;
import com.mynotebook.englishonthego.utilities.SampleVocab;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AppRepository {
    private static AppRepository ourInstance;

    public LiveData<List<VocabModel>> mVocabs;
    public LiveData<List<NoteModel>> mNotes;
    public LiveData<List<LyricSaveModel>> mLyrics;

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
        mLyrics = getAllLyrics();
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

    public void deleteAllNotes() {
        executor.execute(() -> mDb.noteDAO().deleteAll());
    }

    public void addSampleVocab() {
        executor.execute(() -> mDb.vocabDAO().insertAllVocab(SampleVocab.INSTANCE.getAllVocab()));
    }

    /**
     * Note
     */
    private LiveData<List<NoteModel>> getAllNotes() {
        return mDb.noteDAO().getAllNotes();
    }

    public NoteModel getNoteByID(int noteID) {
        return mDb.noteDAO().getNotedByID(noteID);
    }

    public void insertNote(NoteModel note) {
        executor.execute(() -> mDb.noteDAO().insertNote(note));
    }

    public void deleteNote(NoteModel note) {
        executor.execute(() -> mDb.noteDAO().deleteNote(note));
    }

    public void deleteAllVocab() {
        executor.execute(() -> mDb.vocabDAO().deleteALL());
    }

    public void addSampleNote() {
        executor.execute(() -> mDb.noteDAO().insertAllNotes(SampleNote.INSTANCE.getAllNotes()));
    }

    /**
     * Lyric
     */
    private LiveData<List<LyricSaveModel>> getAllLyrics() {
        return mDb.lyricDAO().getAllLyrics();
    }

    public LyricSaveModel getLyricByID(int lyricID) {
        return mDb.lyricDAO().getLyricByID(lyricID);
    }

    public void insertLyric(LyricSaveModel lyricSaveModel) {
        executor.execute(() -> mDb.lyricDAO().insertLyric(lyricSaveModel));
    }

    public void addSampleLyrics() {
        executor.execute(() -> mDb.lyricDAO().insertAllLyrics(SampleLyric.INSTANCE.getAllLyric()));
    }
}
