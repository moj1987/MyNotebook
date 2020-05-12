package com.mynotebook.englishonthego.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mynotebook.englishonthego.NoteEditorActivity;
import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.model.NoteModel;
import com.mynotebook.englishonthego.ui.NotesAdapter;
import com.mynotebook.englishonthego.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.mynotebook.englishonthego.utilities.Constants.KEY_NOTE_ID;

public class NotesFragment extends Fragment implements NotesAdapter.OnNoteClickListener {
    private static final String TAG = "Notes Fragment";


    RecyclerView recyclerView;
    FloatingActionButton fabAddNewNote;
    private NotesAdapter notesAdapter;
    private List<NoteModel> noteData = new ArrayList<>();
    private MainViewModel mainViewModel;


    public NotesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notes, container, false);
        recyclerView = view.findViewById(R.id.note_recycler_view);
        fabAddNewNote = view.findViewById(R.id.add_new_note);

        configureAdapters();
        initViewModel();
        configureListeners();

        return view;
    }

    /**
     * Delete all data and add simple data to database.
     */
    private void testData() {
        mainViewModel.deleteAllNotes();
        mainViewModel.addSampleNotes();
    }

    private void configureAdapters() {
        /**
         *  Visual arrangements for items
         */
        recyclerView.setHasFixedSize(true);

        /**
         * Set up RecyclerView
         */
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(notesAdapter);
    }

    private void initViewModel() {
        /**
         * set up an Observer
         */
        final Observer<List<NoteModel>> noteObserver =
                noteModels -> {
                    noteData.clear();
                    noteData.addAll(noteModels);

                    if (notesAdapter == null) {
                        notesAdapter = new NotesAdapter(noteData, getActivity(), this);
                        recyclerView.setAdapter(notesAdapter);
                    } else {
                        notesAdapter.notifyDataSetChanged();
                    }
                };

        /**
         * initialize ViewModel
         */
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.mLiveNote.observe(getViewLifecycleOwner(), noteObserver);
    }

    private void configureListeners() {
        /**
         * Add button opens note editor activity
         */
        fabAddNewNote.setOnClickListener(v -> startActivity(new Intent(getActivity(), NoteEditorActivity.class)));
    }

    @Override
    public void onNoteClicked(int position) {
        /**
         * opens the clicked note in editor activity
         */
        Intent intent = new Intent(getActivity(), NoteEditorActivity.class);
        NoteModel currentNote = noteData.get(position);
        intent.putExtra(KEY_NOTE_ID, currentNote.getId());
        startActivity(intent);
    }
}
