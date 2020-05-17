package com.mynotebook.englishonthego.tabs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.mynotebook.englishonthego.LyricsViewerActivity;
import com.mynotebook.englishonthego.R;
import com.mynotebook.englishonthego.networking.HappiApi;
import com.mynotebook.englishonthego.networking.LyricSearchModel;
import com.mynotebook.englishonthego.networking.RetrofitManager;
import com.mynotebook.englishonthego.networking.SearchFeed;
import com.mynotebook.englishonthego.ui.LyricSearchAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class LyricsSearchFragment extends Fragment implements LyricSearchAdapter.OnItemClickListener, TextWatcher {
    public static final String KEY_STATE = "com.mynotebook.englishonthego.MainActivity.KEY_STATE";

    private String searchText;
    private EditText searchTextInput;
    private ImageView searchButton;
    private ProgressBar indeterminateProgressBar;

    private HappiApi happiApi;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private LyricSearchAdapter mAdapter;
    private List<LyricSearchModel> responseData = new ArrayList<>();
    private RetrofitManager retrofitManager;

    public LyricsSearchFragment() {
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

        searchTextInput.addTextChangedListener(this);

        configureAdapters();
        configureListeners();

        return view;
    }

    private void configureAdapters() {
        recyclerView.setHasFixedSize(true);

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
        searchButton.setOnClickListener(v -> performSearch());

        //Configure perform search when Enter clicked
        searchTextInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    /**
     * Checks input if not empty then searches for lyric
     */
    private void performSearch() {
        if (searchTextInput.getText().toString().trim().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Error")
                    .setMessage("Please type a song to search.")
                    .setNegativeButton("Dismiss", (dialog, id) -> dialog.dismiss())
                    .create()
                    .show();
            return;
        }

        closeKeyboard();

        //Hide search button and show progress bar
        searchButton.setVisibility(View.INVISIBLE);
        indeterminateProgressBar.setVisibility(View.VISIBLE);

        searchText = searchTextInput.getText().toString().trim();

        getLyricSearch(searchText);
    }

    private void getLyricSearch(String searchText) {
        String HAPPI_DEV_API_KEY = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";
        Call<SearchFeed> call = happiApi.getSearch(searchText, HAPPI_DEV_API_KEY);

        call.enqueue(new Callback<SearchFeed>() {
            @Override
            public void onResponse(Call<SearchFeed> call, Response<SearchFeed> feed) {
                if (!feed.isSuccessful()) {
                    indeterminateProgressBar.setVisibility(View.INVISIBLE);
                    searchButton.setVisibility(View.VISIBLE);
                    return;
                }
                SearchFeed feeds = feed.body();
                responseData = feeds.getCallResult();

                /**
                 * Check if the response data is empty
                 */
                if (responseData.isEmpty()) {
                    Snackbar.make(
                            getActivity().findViewById(R.id.coordinator),
                            "Sorry, nothing found.",
                            Snackbar.LENGTH_LONG)
                            .show();
                }

                /**
                 * Remove the items that do not have lyric
                 */
                for (Iterator<LyricSearchModel> iterator = responseData.iterator(); iterator.hasNext(); ) {
                    LyricSearchModel currentData = iterator.next();
                    if (!currentData.getHasLyric()) {
                        iterator.remove();
                    }
                }

                indeterminateProgressBar.setVisibility(View.INVISIBLE);
                searchButton.setVisibility(View.VISIBLE);

                mAdapter.setItems(responseData);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchFeed> call, Throwable t) {
                Toast.makeText(getContext(), "Network error, please try again later.", LENGTH_LONG).show();
                indeterminateProgressBar.setVisibility(View.INVISIBLE);
                searchButton.setVisibility(View.VISIBLE);
            }
        });
    }

    private void closeKeyboard() {
        InputMethodManager imm = (InputMethodManager)
                getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public void onItemClicked(int position) {
        LyricSearchModel currentLyricData = responseData.get(position);
        int artistId = currentLyricData.getArtistId();
        int albumId = currentLyricData.getAlbumId();
        int trackId = currentLyricData.getTrackId();
        String trackName = currentLyricData.getTrackName();
        String albumCoverUrl = currentLyricData.getAlbumCoverUrl();

        LyricsViewerActivity.startActivity(artistId, albumId, trackId, trackName, albumCoverUrl, false, getContext());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

        } else {
            ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (!isConnected) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Error")
                        .setMessage("No network detected. Please connect to a network.")
                        .setNegativeButton("Dismiss", ((dialog, which) -> dialog.dismiss()))
                        .create()
                        .show();

            }

        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
