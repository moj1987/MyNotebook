package com.mynotebook.englishonthego.tabs;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mynotebook.englishonthego.networking.ConnectionManager;
import com.mynotebook.englishonthego.networking.HappiApi;
import com.mynotebook.englishonthego.networking.LyricSearchModel;
import com.mynotebook.englishonthego.networking.RetrofitManager;
import com.mynotebook.englishonthego.networking.SearchFeed;
import com.mynotebook.englishonthego.ui.LyricSearchAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.widget.Toast.LENGTH_LONG;

public class LyricsSearchFragment extends Fragment implements LyricSearchAdapter.OnItemClickListener {
    public static final String KEY_STATE = "com.mynotebook.englishonthego.MainActivity.KEY_STATE";

    private String searchText;
    private EditText searchTextInput;
    private ImageView searchButton;
    private ProgressBar indeterminateProgressBar;

    private ImageView imageNoInternet;
    private LinearLayout linearLayout;

    private HappiApi happiApi;
    private RecyclerView recyclerView;
    private LyricSearchAdapter mAdapter;
    private List<LyricSearchModel> responseData = new ArrayList<>();
    private RetrofitManager retrofitManager;

    private BroadcastReceiver networkBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean hasInternet = ConnectionManager.isNetworkAvailable(getContext());
            updateUi(hasInternet);
        }
    };

    public LyricsSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        retrofitManager = new RetrofitManager();
        happiApi = retrofitManager.getHappiApi();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lyric_search, container, false);

        searchTextInput = view.findViewById(R.id.search_text);
        searchButton = view.findViewById(R.id.search_button);
        indeterminateProgressBar = view.findViewById(R.id.indeterminate_progress_bar);
        recyclerView = view.findViewById(R.id.lyrics_recycler_view);

        imageNoInternet = view.findViewById(R.id.image_no_internet);
        linearLayout = view.findViewById(R.id.linear_layout_lyric_search);

        configureAdapters();
        configureListeners();

        return view;
    }

    private void configureAdapters() {
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new LyricSearchAdapter(responseData, getActivity(), this);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED);
        getActivity().registerReceiver(networkBroadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(networkBroadcastReceiver);
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
            public void onResponse(@NotNull Call<SearchFeed> call, @NotNull Response<SearchFeed> feed) {
                if (!feed.isSuccessful()) {
                    indeterminateProgressBar.setVisibility(View.INVISIBLE);
                    searchButton.setVisibility(View.VISIBLE);
                    return;
                }

                SearchFeed feeds = feed.body();
                responseData = feeds.getCallResult();

                /*
                 * Remove the items that do not have lyric
                 */
                for (Iterator<LyricSearchModel> iterator = responseData.iterator(); iterator.hasNext(); ) {
                    LyricSearchModel currentData = iterator.next();
                    if (!currentData.getHasLyric()) {
                        iterator.remove();
                    }
                }

                if (responseData.isEmpty()) {
                    Snackbar.make(
                            getActivity().findViewById(R.id.coordinator),
                            "Sorry, nothing found.",
                            Snackbar.LENGTH_LONG)
                            .show();
                }

                indeterminateProgressBar.setVisibility(View.INVISIBLE);
                searchButton.setVisibility(View.VISIBLE);

                mAdapter.setItems(responseData);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(@NotNull Call<SearchFeed> call, @NotNull Throwable t) {
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

    private void updateUi(boolean hasInternet) {
        linearLayout.setVisibility(hasInternet ? View.VISIBLE : View.INVISIBLE);
        imageNoInternet.setVisibility(hasInternet ? View.INVISIBLE : View.VISIBLE);
    }

}
