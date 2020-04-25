package com.mynotebook.englishonthego.tabs;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mynotebook.englishonthego.LyricsViewerActivity;
import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.databinding.ActivityMainBinding;
import com.mynotebook.englishonthego.networking.HappiApi;
import com.mynotebook.englishonthego.networking.Responses;
import com.mynotebook.englishonthego.networking.RetrofitManager;
import com.mynotebook.englishonthego.networking.SearchFeed;
import com.mynotebook.englishonthego.ui.LyricSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyricsFragment extends Fragment implements LyricSearchAdapter.OnItemClickListener {
    private static final String TAG = "tesTTTTTTTTTTTT";

    private ActivityMainBinding binding;
    public static final String KEY_STATE = "com.mynotebook.englishonthego.MainActivity.KEY_STATE";
    final String HAPPI_DEV_API_KEY = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";

    private String searchText;
    private EditText searchTextInput;
    private Button searchButton;
    private ProgressBar indeterminateProgressBar;

    private HappiApi happiApi;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LyricSearchAdapter mAdapter;
    private List<Responses> responseData = new ArrayList<>();
    private RetrofitManager retrofitManager;

    public LyricsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retrofitManager = new RetrofitManager();
        happiApi = retrofitManager.getHappiApi();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);

        searchTextInput = view.findViewById(R.id.search_text);
        searchButton = view.findViewById(R.id.search_button);
        indeterminateProgressBar = view.findViewById(R.id.indeterminateProgressBar);
        recyclerView = view.findViewById(R.id.lyrics_recycler_view);

        configureAdapters();
        configureListeners();

        return view;
    }

    private void configureAdapters() {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new LyricSearchAdapter(responseData, getActivity(), this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void configureListeners() {
        //Configure search button
        searchButton.setOnClickListener(v -> {
            // check if the same search is being requested
            if (searchTextInput.getText().toString().trim().equals(searchText)) {
                Toast.makeText(getContext(), "You already have the result for this search", Toast.LENGTH_LONG).show();
                return;
            } else {
                searchText = searchTextInput.getText().toString().trim();
            }

            //Hide search button and show progress bar
            searchButton.setVisibility(View.GONE);
            indeterminateProgressBar.setVisibility(View.VISIBLE);

            closeKeyboard();
            getLyricSearch(searchText);
        });
    }

    private void getLyricSearch(String searchText) {
        Call<SearchFeed> call = happiApi.getSearch(searchText, HAPPI_DEV_API_KEY);

        call.enqueue(new Callback<SearchFeed>() {
            @Override
            public void onResponse(Call<SearchFeed> call, Response<SearchFeed> feed) {
                if (!feed.isSuccessful()) {
                    Toast.makeText(getContext(), "Sorry. Your search was not successful.", Toast.LENGTH_LONG).show();

                    indeterminateProgressBar.setVisibility(View.GONE);
                    searchButton.setVisibility(View.VISIBLE);
                    return;
                }
                SearchFeed feeds = feed.body();
                responseData = feeds.getCallResult();

                Log.i(TAG, "Response: " + responseData.toString());

                indeterminateProgressBar.setVisibility(View.GONE);
                searchButton.setVisibility(View.VISIBLE);

                Log.i(TAG, "before setItems");
                mAdapter.setItems(responseData);
                Log.i(TAG, "before notifying adapter");
                mAdapter.notifyDataSetChanged();
                Log.i(TAG, "last line of api call");
            }

            @Override
            public void onFailure(Call<SearchFeed> call, Throwable t) {
                Toast.makeText(getContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void closeKeyboard() {
//        View view = this.getCurrentFocus();
//        if (view != null) {
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
//        }
    }

    @Override
    public void onItemClicked(int position) {
        Responses currentLyricData = responseData.get(position);
        int artistId = currentLyricData.getArtistId();
        int albumId = currentLyricData.getAlbumId();
        int trackId = currentLyricData.getTrackId();

        Intent intent = new Intent(getContext(), LyricsViewerActivity.class);
        intent.putExtra(LyricsViewerActivity.KEY_ARTIST_ID, artistId);
        intent.putExtra(LyricsViewerActivity.KEY_ALBUM_ID, albumId);
        intent.putExtra(LyricsViewerActivity.KEY_TRACK_ID, trackId);

        startActivity(intent);
    }

}
