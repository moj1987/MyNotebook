package com.mynotebook.englishonthego.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mynotebook.englishonthego.LyricsViewerActivity;
import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.model.LyricSaveModel;
import com.mynotebook.englishonthego.ui.LyricViewAdapter;
import com.mynotebook.englishonthego.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.List;

public class LyricsViewerFragment extends Fragment implements LyricViewAdapter.OnItemClickListener {

    private List<LyricSaveModel> lyricData = new ArrayList<>();
    private LyricViewAdapter lyricViewAdapter;
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;


    public LyricsViewerFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lyric_viewer, container, false);

        recyclerView = view.findViewById(R.id.lyric_viewer_fragment_recyclerview);

        configureAdapters();

        initViewModel();

        return view;
    }

    private void configureAdapters() {
        recyclerView.hasFixedSize();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(lyricViewAdapter);
    }

    private void initViewModel() {
        final Observer<List<LyricSaveModel>> lyricObserver =
                lyricSaveModel -> {
                    lyricData.clear();
                    lyricData.addAll(lyricSaveModel);

                    if (lyricViewAdapter == null) {
                        lyricViewAdapter = new LyricViewAdapter(lyricData, getActivity(), this);
                        recyclerView.setAdapter(lyricViewAdapter);
                    } else {
                        lyricViewAdapter.notifyDataSetChanged();
                    }
                };

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.mLiveLyrics.observe(getViewLifecycleOwner(), lyricObserver);
    }


    @Override
    public void onItemClicked(int position) {
        int trackId = lyricData.get(position).getId();
        LyricsViewerActivity.startActivity(trackId, true, true, getContext());
    }

    private void addSampleData() {
        mainViewModel.addSampleLyrics();
    }
}
