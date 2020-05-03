package com.mynotebook.englishonthego.tabs;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.VocabEditorActivity;
import com.mynotebook.englishonthego.model.VocabModel;
import com.mynotebook.englishonthego.ui.VocabAdapter;
import com.mynotebook.englishonthego.viewmodel.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static com.mynotebook.englishonthego.utilities.Constants.VOCAB_ID_KEY;

public class DictionaryFragment extends Fragment implements VocabAdapter.OnVocabClickListener {

    private List<VocabModel> vocabData = new ArrayList<>();
    private VocabAdapter vocabAdapter;
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;

    private FloatingActionButton fabAddNewVocab;

    /**
     * Empty constructor
     */
    public DictionaryFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);

        recyclerView = view.findViewById(R.id.dictionary_recycler_view);
        fabAddNewVocab = view.findViewById(R.id.add_new_vocab);

        configureAdapters();
        initViewModel();
        configureListeners();

        return view;
    }

    private void configureAdapters() {
        /**
         *configuring recyclerView Adapter for Dictionary Fragment
         */
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * initializing ViewModel
     */
    private void initViewModel() {
//     setting up an observer
        final Observer<List<VocabModel>> vocabObserver =
                vocabModels -> {
                    vocabData.clear();
                    vocabData.addAll(vocabModels);

                    if (vocabAdapter == null) {
                        vocabAdapter = new VocabAdapter(vocabData, getActivity(), this);
                        recyclerView.setAdapter(vocabAdapter);
                    } else {
                        vocabAdapter.notifyDataSetChanged();
                    }
                };

//        viewModel instantiation
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.mLiveVocab.observe(getViewLifecycleOwner(), vocabObserver);
    }

    private void configureListeners() {
        /**
         * fab listener to call VocabEditor activity to add a new vocab
         */
        fabAddNewVocab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), VocabEditorActivity.class);
            startActivity(intent);
        });
    }

    /**
     * When an item is clicked
     */
    @Override
    public void onVocabClicked(int position) {
        /**
         * Show the clicked Vocab in VocabEditor activity
         */
        Intent intent = new Intent(getActivity(), VocabEditorActivity.class);
        VocabModel currentVocab = vocabData.get(position);
        intent.putExtra(VOCAB_ID_KEY, currentVocab.getId());
        startActivity(intent);
    }

    /**
     * It clears the database and adds sample data
     */
    private void testData() {
        mainViewModel.deleteAllVocabs();
        mainViewModel.addSampleVocabs();
    }
}
