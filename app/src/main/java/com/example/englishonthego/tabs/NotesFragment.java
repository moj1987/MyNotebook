package com.example.englishonthego.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NotesFragment extends Fragment {
    RecyclerView recyclerView;
    FloatingActionButton fabAddNewNote;

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

    private void configureListeners() {

    }

    private void initViewModel() {

    }

    private void configureAdapters() {

    }
}
