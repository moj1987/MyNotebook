package com.mynotebook.englishonthego;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.mynotebook.englishonthego.viewmodel.VocabEditorViewModel;

import static com.mynotebook.englishonthego.utilities.Constants.EDITING_KEY;
import static com.mynotebook.englishonthego.utilities.Constants.VOCAB_ID_KEY;

public class VocabEditorActivity extends AppCompatActivity implements TextWatcher {

    private EditText vocabText, definitionText, exampleText;
    private Boolean isNewVocab = false;
    private Boolean isEditing = false;
    private VocabEditorViewModel mViewModel;
    private EditText[] editTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vocab_editor);
        Toolbar toolbar = findViewById(R.id.toolbar);

        vocabText = findViewById(R.id.edit_vocab);
        definitionText = findViewById(R.id.edit_definition);
        exampleText = findViewById(R.id.edit_example);
        editTextList = new EditText[]{vocabText, definitionText, exampleText};

        vocabText.addTextChangedListener(this);
        definitionText.addTextChangedListener(this);
        exampleText.addTextChangedListener(this);

        setSupportActionBar(toolbar);

        /**
         * Check if it is editing. Not let live data object publish it.
         */
        if (savedInstanceState != null) {
            isEditing = savedInstanceState.getBoolean(EDITING_KEY);
        }

        initViewModel();
    }

    /**
     * Initialize View Model
     */
    private void initViewModel() {
        mViewModel = new ViewModelProvider(this).get(VocabEditorViewModel.class);
        mViewModel.mLiveVocab.observe(this, vocabModel -> {
            if (vocabModel != null && !isEditing) {
                vocabText.setText(vocabModel.getVocab());
                definitionText.setText(vocabModel.getDefinition());
                exampleText.setText(vocabModel.getExample());
            }
        });

        /**
         * Check if new vocab.
         * Set title accordingly
         */
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setTitle("New");
            isNewVocab = true;
        } else {
            setTitle("Editing");
            mViewModel.loadVocab(extras.getInt(VOCAB_ID_KEY));
        }
    }

    /**
     * Saves the new vocab or replaces the new information
     */
    private void saveVocab() {
        String vocab = vocabText.getText().toString().trim();
        String definition = definitionText.getText().toString().trim();
        String example = exampleText.getText().toString().trim();
        mViewModel.saveVocab(vocab, definition, example);
    }

    /**
     * create menu options in toolbar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_vocab_editor, menu);

        if (!isNewVocab) {
            MenuItem deleteItem = menu.findItem(R.id.action_delete_vocab);
            deleteItem.setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Save or delete vocab when the corresponding icon is clicked
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_vocab:
                deleteVocab();
                finish();
                break;
            case R.id.action_save_vocab:
                saveVocab();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteVocab() {
        mViewModel.deleteVocab();
        finish();
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
//                saveVocabToDictionary.setEnabled(false);
                break;
            }
//                saveVocabToDictionary.setEnabled(true);
        }

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(EDITING_KEY, true);
        super.onSaveInstanceState(outState);
    }
}