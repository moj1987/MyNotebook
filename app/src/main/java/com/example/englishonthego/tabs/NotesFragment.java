package com.example.englishonthego.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.example.englishonthego.model.NoteModel;
import com.example.englishonthego.ui.NotesAdapter;
import com.example.englishonthego.ui.VocabAdapter;
import com.example.englishonthego.utilities.SampleNote;
import com.example.englishonthego.utilities.SampleVocab;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class NotesFragment extends Fragment implements NotesAdapter.OnNoteClickListener {
    RecyclerView recyclerView;
    FloatingActionButton fabAddNewNote;
    private NotesAdapter notesAdapter;
    private List<NoteModel> notedData = new ArrayList<>();


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

        /**
         * TODO: Delete!!
         */
        notedData.addAll(SampleNote.INSTANCE.getAllNotes());

        return view;
    }

    private void configureListeners() {

    }

    private void initViewModel() {

    }

    private void configureAdapters() {
        recyclerView.setHasFixedSize(true);

        notesAdapter = new NotesAdapter(notedData, getActivity(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public void onNoteClicked(int position) {
        Toast.makeText(getActivity(), "position: " + position, Toast.LENGTH_SHORT).show();
    }
}
