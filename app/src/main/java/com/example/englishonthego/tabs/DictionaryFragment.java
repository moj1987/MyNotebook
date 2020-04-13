package com.example.englishonthego.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.englishonthego.R;
import com.example.englishonthego.VocabEditorActivity;
import com.example.englishonthego.model.VocabModel;
import com.example.englishonthego.ui.VocabAdapter;
import com.example.englishonthego.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DictionaryFragment extends Fragment {
//    private static final String TAG = "DictionaryFragment";
    private static final String TAG = "tesTTTTTTTTTTTTTT";

    private List<VocabModel> vocabData = new ArrayList<>();
    private VocabAdapter vocabAdapter;
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;

    private FloatingActionButton addNewVocab;

    public DictionaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        recyclerView = view.findViewById(R.id.dictionary_recycler_view);
        addNewVocab = view.findViewById(R.id.add_new_vocab);

        configureAdapters();
        initViewModel();
        configureListeners();
        return view;
    }

    //  initializing ViewModel
    private void initViewModel() {
        final Observer<List<VocabModel>> vocabObserver =
                vocabModels -> {
                    vocabData.clear();
                    vocabData.addAll(vocabModels);
                    Log.d(TAG, "initViewModel+data loaded: "+ vocabData.toString());

                    if (vocabAdapter == null) {
//                        vocabAdapter = new VocabAdapter(SampleVocab.INSTANCE.getAllVocab(), getActivity());
                        vocabAdapter = new VocabAdapter(vocabData, getActivity());
                        recyclerView.setAdapter(vocabAdapter);
                    } else {
                        vocabAdapter.notifyDataSetChanged();
                    }
                };
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.mLiveVocab.observe(getViewLifecycleOwner(), vocabObserver);
    }

    private void configureAdapters() {
//       configuring recyclerView Adapter for Dictionary Fragment
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void configureListeners() {
//        fab listener to call VocabEditor activity
        addNewVocab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VocabEditorActivity.class);
            startActivity(intent);
        });
    }
}
