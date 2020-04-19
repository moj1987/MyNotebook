package com.example.englishonthego;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.example.englishonthego.model.NoteModel;
import com.example.englishonthego.viewmodel.NoteEditorViewModel;

import static com.example.englishonthego.utilities.Constants.NOTE_ID_KEY;

public class NoteEditorActivity extends AppCompatActivity {
    private static final String TAG = "NoteEditorActivity";

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
            setTitle("Editing");
            mViewModel.loadNote(extras.getInt(NOTE_ID_KEY));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isNewNote) {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_note_editor, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_delete_note) {
            deleteNote();
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
        mViewModel.deleteNote();
        finish();
    }

    private void configureListeners() {

        saveNote.setOnClickListener(v -> {
            saveNoteAndReturn();
        });
    }

    private void saveNoteAndReturn() {
        String title = noteTitle.getText().toString().trim();
        String note = noteText.getText().toString().trim();
        mViewModel.saveNote(title, note);
        finish();
    }
}
