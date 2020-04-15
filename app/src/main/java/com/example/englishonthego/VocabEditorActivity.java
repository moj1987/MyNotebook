package com.example.englishonthego;

import android.os.Bundle;

import com.example.englishonthego.viewmodel.VocabEditorViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.example.englishonthego.utilities.Constants.VOCAB_ID_KEY;

public class VocabEditorActivity extends AppCompatActivity {
    //    private static final String TAG = "VocabEditorActivity";
    private static final String TAG = "testTTTTTTTTTTTTTTTTT";

    private EditText vocabText, definitionText, exampleText;
    private Boolean isNewNote;
    Button saveVocabToDictionary;
    private VocabEditorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vocab_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        vocabText = findViewById(R.id.edit_vocab);
        definitionText = findViewById(R.id.edit_definition);
        exampleText = findViewById(R.id.edit_example);
        saveVocabToDictionary = findViewById(R.id.save_vocab_to_dictionary);

        setSupportActionBar(toolbar);

        initViewModel();
        configureListeners();
    }

    private void configureListeners() {
        /**
         * save button configuration
         */
        saveVocabToDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveVocabAndReturn();
            }
        });
    }

    /**
     * initialize View Model
     */
    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(VocabEditorViewModel.class);

        /**
         * Check if new note.
         * Set title accordingly
         */
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New");
            isNewNote = true;
        } else {
            setTitle("Edit Vocab");
            int vocabID = extras.getInt(VOCAB_ID_KEY);
            mViewModel.loadVocab(vocabID);
        }
    }

    /**
     * saves the new vocab or replaces the new information
     */
    private void saveVocabAndReturn() {
        String vocab = vocabText.getText().toString().trim();
        String definition = definitionText.getText().toString().trim();
        String example = exampleText.getText().toString().trim();
        mViewModel.saveVocab(vocab, definition, example);
        Log.d(TAG, "came back from saveVocab in view model");
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isNewNote) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_vocab_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.action_delete){
            deleteVocab();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteVocab() {
        mViewModel.deleteVocab();
        finish();
    }
}