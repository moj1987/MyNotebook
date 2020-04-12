package com.example.englishonthego;

import android.os.Bundle;

import com.example.englishonthego.database.VocabDatabase;
import com.example.englishonthego.model.VocabModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class VocabEditorActivity extends AppCompatActivity {
//    private static final String TAG = "VocabEditorActivity";
    private static final String TAG = "testTTTTTTTTTTTTTTTTT";

    EditText vocabText, definitionText, exampleText;
    String vocab, definition, example;
    VocabModel newVocab;
    private Executor executor = Executors.newSingleThreadExecutor();
    private VocabDatabase vocabDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        vocabText = findViewById(R.id.edit_vocab);
        definitionText = findViewById(R.id.edit_definition);
        exampleText = findViewById(R.id.edit_example);

        setSupportActionBar(toolbar);

        vocabDatabase=VocabDatabase.getInstance(getApplicationContext());
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveVocab();
            }
        });
        /**
         * for testing. to be deleted
         */
        vocabText.setText("hi");
        definitionText.setText("hello");
        exampleText.setText("I said hello");
        saveVocab();
    }

    private void saveVocab() {
        String vocab = vocabText.getText().toString().trim();
        String definition = definitionText.getText().toString().trim();
        String example = exampleText.getText().toString().trim();
        if (TextUtils.isEmpty(vocab) | TextUtils.isEmpty(definition) | TextUtils.isEmpty(example)) {
            Toast.makeText(getApplicationContext(), "Please fill in all the parameters", Toast.LENGTH_LONG).show();
        } else {
            Log.i(TAG, "Vocab: "+ vocab);
            Log.i(TAG, "Definition: "+ definition);
            Log.i(TAG, "Example: "+ example);
            newVocab = new VocabModel(vocab, definition, example);
            Log.i(TAG, "Object "+ newVocab.toString());
            executor.execute(() -> vocabDatabase.vocabDAO().insertVocab(newVocab));
//            TODO: Go back to Dictionary Fragment
//            TODO: update recyclerview adapter
        }
    }
}