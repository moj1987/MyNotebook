package com.example.englishonthego.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.example.englishonthego.VocabEditorActivity;
import com.example.englishonthego.sampleData.SampleVocab;
import com.example.englishonthego.ui.VocabAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DictionaryFragment extends Fragment {
    private RecyclerView recyclerView;
    private VocabAdapter vocabAdapter;
    private FloatingActionButton fab;

    public DictionaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        recyclerView = view.findViewById(R.id.dictionary_recycler_view);
        fab = view.findViewById(R.id.add_vocab);

        configureAdapters();
        configureListeners();
        return view;
    }

    private void configureAdapters() {
//       configuring recyclerView Adapter for Dictionary Fragment
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        vocabAdapter = new VocabAdapter(SampleVocab.INSTANCE.getAllVocab(), getActivity());
        recyclerView.setAdapter(vocabAdapter);
    }

    private void configureListeners() {
//        fab listener to call VocabEditor activity
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VocabEditorActivity.class);
            startActivity(intent);
        });
    }
}
