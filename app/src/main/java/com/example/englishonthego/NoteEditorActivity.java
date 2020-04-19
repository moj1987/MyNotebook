package com.example.englishonthego;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.widget.Button;
import android.widget.EditText;

import com.example.englishonthego.viewmodel.NoteEditorViewModel;

import static com.example.englishonthego.utilities.Constants.NOTE_ID_KEY;

public class NoteEditorActivity extends AppCompatActivity {

    EditText noteTitle, noteText;
    Button saveNote;
    Boolean isNewNote = false;
    private NoteEditorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        saveNote = findViewById(R.id.save_note);

        initViewModel();
        configureListeners();
    }

    /**
     * initialize view model
     */
    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(NoteEditorViewModel.class);
        mViewModel.mLiveNote.observe(this, noteModel -> {
                    if (noteModel != null) {
                        noteTitle.setText(noteModel.getTitle());
                        noteText.setText(noteModel.getText());
                    }
                }
        );

        /**
         * check if new note
         * set title accordingly
         */
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            isNewNote = true;
            setTitle("New note");
        } else {
            isNewNote = false;
            setTitle("Editing");
            mViewModel.loadNote(extras.getInt(NOTE_ID_KEY));
        }

    }

    private void configureListeners() {

        saveNote.setOnClickListener(v -> {
            saveNoteAndReturn();
        });
    }

    private void saveNoteAndReturn() {
        String title = noteTitle.getText().toString().trim();
        String note = noteText.getText().toString().trim();
        mViewModel.saveNote(title,note);
        finish();
    }
}
