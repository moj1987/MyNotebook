package com.mynotebook.englishonthego;

import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.mynotebook.englishonthego.database.AppDatabase;
import com.mynotebook.englishonthego.model.VocabModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static com.mynotebook.englishonthego.database.AppDatabase.MIGRATION_1_2;
import static org.junit.Assert.assertEquals;


@RunWith(AndroidJUnit4.class)
public class MigrationTest {

    private static final String TEST_DATABASE = "migration-test";
    private static final String VOCABS_TABLE = "vocabsTable";
    private static final VocabModel VOCAB_MODEL = new VocabModel(1, "hello", "hi", "I said hello");


    @Rule
    public MigrationTestHelper migrationTestHelper;

    public MigrationTest() {
        migrationTestHelper = new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                AppDatabase.class.getCanonicalName(),
                new FrameworkSQLiteOpenHelperFactory());
    }

    @Test
    public void migrate1to2() throws IOException {
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DATABASE,1);
//        db.execSQL("INSERT INTO vocabsTable");
    }





    private SqliteTestDbOpenHelper sqliteTestDbOpenHelper;
//    @Before
//    public void createDb() {
////        db = helper.createDatabase(TEST_DATABASE, 1);
//        sqliteTestDbOpenHelper = new SqliteTestDbOpenHelper(ApplicationProvider.getApplicationContext(),
//                TEST_DATABASE);
//        SqliteDatabaseTestHelper.createTables(sqliteTestDbOpenHelper);

//    }
//    @After
//    public void closeDb() {
//        SqliteDatabaseTestHelper.clearDatabase(sqliteTestDbOpenHelper);

//    }


    @Test
    public void onMigrationFrom1To2Check() throws IOException {
        // Adding data to version 1
        SqliteDatabaseTestHelper.insertData(1, VOCAB_MODEL.getVocab(),
                VOCAB_MODEL.getDefinition(),
                VOCAB_MODEL.getExample(),
                sqliteTestDbOpenHelper);

        migrationTestHelper.runMigrationsAndValidate(TEST_DATABASE, 2, true, MIGRATION_1_2);

        AppDatabase latestDb = getMigratedDatabase();

        VocabModel vocabModel = latestDb.vocabDAO().getVocabByID(1);
        assertEquals(vocabModel.getId(), VOCAB_MODEL.getId());
        assertEquals(vocabModel.getVocab(), VOCAB_MODEL.getVocab());
        assertEquals(vocabModel.getDefinition(), VOCAB_MODEL.getDefinition());
        assertEquals(vocabModel.getExample(), VOCAB_MODEL.getExample());
    }

    private AppDatabase getMigratedDatabase() {
        AppDatabase database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                AppDatabase.class, TEST_DATABASE)
                .addMigrations(MIGRATION_1_2)
                .build();

        migrationTestHelper.closeWhenFinished(database);

        return database;
    }
}
