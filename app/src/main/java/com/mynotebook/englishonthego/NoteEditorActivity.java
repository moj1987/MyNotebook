package com.mynotebook.englishonthego;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.mynotebook.englishonthego.viewmodel.NoteEditorViewModel;

import static com.mynotebook.englishonthego.utilities.Constants.NOTE_ID_KEY;

public class NoteEditorActivity extends AppCompatActivity implements TextWatcher {
    private static final String TAG = "NoteEditorActivity";

    EditText noteTitle, noteText;
    Button saveNote;
    Boolean isNewNote = false;
    EditText[] editTextList;
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
        editTextList = new EditText[]{noteTitle, noteText};

        saveNote.setEnabled(false);
        noteTitle.addTextChangedListener(this);
        noteText.addTextChangedListener(this);

        initViewModel();
        configureListeners();
    }

    /**
     * Initialize view model
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
         * Check if new note
         * Set title accordingly
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
        /**
         *  Saves note and closes activity, when save clicked
         */
        saveNote.setOnClickListener(v -> {
            saveNote();
            finish();
        });
    }

    private void saveNote() {
        String title = noteTitle.getText().toString().trim();
        String note = noteText.getText().toString().trim();
        mViewModel.saveNote(title, note);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        for (EditText editText : editTextList) {
            if (editText.getText().toString().trim().length() <= 0) {
                saveNote.setEnabled(false);
                break;
            }
            saveNote.setEnabled(true);
        }
    }
}
