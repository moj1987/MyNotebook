package com.mynotebook.englishonthego;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.mynotebook.englishonthego.viewmodel.NoteEditorViewModel;

import static com.mynotebook.englishonthego.utilities.Constants.KEY_EDITING;
import static com.mynotebook.englishonthego.utilities.Constants.KEY_NOTE_ID;

public class NoteEditorActivity extends AppCompatActivity implements TextWatcher {
    private static final String TAG = "NoteEditorActivity";

    EditText noteTitle, noteText;
    Boolean isNewNote = false, isEditing = false;
    EditText[] editTextList;
    private NoteEditorViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        editTextList = new EditText[]{noteTitle, noteText};

        noteTitle.addTextChangedListener(this);
//        noteText.addTextChangedListener(this);

        if (savedInstanceState != null) {
            isEditing = savedInstanceState.getBoolean(KEY_EDITING);
        }

        initViewModel();
    }

    /**
     * Initialize view model
     */
    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(NoteEditorViewModel.class);
        mViewModel.mLiveNote.observe(this, noteModel -> {
                    if (noteModel != null && !isEditing) {
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
            mViewModel.loadNote(extras.getInt(KEY_NOTE_ID));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_note_editor, menu);

        if (!isNewNote) {
            MenuItem deleteItem = menu.findItem(R.id.action_delete_note);
            deleteItem.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save_note:
                saveNote();
                finish();
                break;
            case R.id.action_delete_note:
                deleteNote();
                finish();
                break;
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteNote() {
        mViewModel.deleteNote();
        finish();
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
        /**
         * Checks if all inputs have text then enables button
         */
        for (EditText editText : editTextList) {
            if (editText.getText().toString().trim().length() <= 0) {
//                saveNote.setEnabled(false);
                break;
            }
//            saveNote.setEnabled(true);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(KEY_EDITING, true);
        super.onSaveInstanceState(outState);
    }
}
