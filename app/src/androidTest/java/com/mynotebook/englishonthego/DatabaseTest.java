package com.mynotebook.englishonthego;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mynotebook.englishonthego.database.AppDatabase;
import com.mynotebook.englishonthego.database.VocabDAO;
import com.mynotebook.englishonthego.utilities.SampleVocab;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class DatabaseTest {
    private static final String TAG = "JUnit";
    private AppDatabase mDB;
    private VocabDAO mDAO;

    @Before
    public void createDB() {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        mDB = Room.inMemoryDatabaseBuilder(context,
                AppDatabase.class).build();
        mDAO = mDB.vocabDAO();
        Log.i(TAG, "DB created!");
    }

    @After
    public void closeDB() {
        mDB.close();
        Log.i(TAG, "DB closed!");
    }

    @Test
    public void createAndRetrieveVocabs() {
        mDAO.insertAllVocab(SampleVocab.INSTANCE.getAllVocab());
        int count = mDAO.getCount();
        Log.i(TAG, "CreateAndRetrieveVocabs: count= " + count);
        assertEquals(SampleVocab.INSTANCE.getAllVocab().size(), count);
    }

    @Test
    public void compareStrings() {
        mDAO.insertAllVocab(SampleVocab.INSTANCE.getAllVocab());
        String original = SampleVocab.INSTANCE.getVocab1();
        String fromDB = mDAO.getVocabByID(1).getVocab().toString();
        assertEquals(original, fromDB);
    }
}
