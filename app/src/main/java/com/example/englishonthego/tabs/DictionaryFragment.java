package com.example.englishonthego.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.example.englishonthego.model.VocabModel;
import com.example.englishonthego.sampleData.SampleVocab;
import com.example.englishonthego.ui.VocabAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class DictionaryFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private VocabAdapter vocabAdapter;

    public DictionaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        recyclerView = view.findViewById(R.id.dictionary_recycler_view);

        initAdapters();
        return view;
    }

    private void initAdapters() {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        vocabAdapter = new VocabAdapter(SampleVocab.INSTANCE.getAllVocab(), getActivity());
        recyclerView.setAdapter(vocabAdapter);

    }

}
