package com.example.englishonthego;

import android.os.Bundle;

import com.example.englishonthego.viewmodel.VocabEditorViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class VocabEditorActivity extends AppCompatActivity {
    //    private static final String TAG = "VocabEditorActivity";
    private static final String TAG = "testTTTTTTTTTTTTTTTTT";

    EditText vocabText, definitionText, exampleText;
    Button saveVocabToDictionary;
    private VocabEditorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocab_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        vocabText = findViewById(R.id.edit_vocab);
        definitionText = findViewById(R.id.edit_definition);
        exampleText = findViewById(R.id.edit_example);
        saveVocabToDictionary = findViewById(R.id.save_vocab_to_dictionary);
        setSupportActionBar(toolbar);
        initViewModel();
        configureListeners();
    }

    private void configureListeners() {
        saveVocabToDictionary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveVocabAndReturn();
            }
        });
    }

    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(VocabEditorViewModel.class);
    }

    private void saveVocabAndReturn() {
        String vocab = vocabText.getText().toString().trim();
        String definition = definitionText.getText().toString().trim();
        String example = exampleText.getText().toString().trim();
        mViewModel.saveVocab(vocab, definition, example);
        Log.d(TAG, "vocab saved");
        finish();
    }
}