package com.example.englishonthego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.englishonthego.databinding.ActivityMainBinding;
import com.example.englishonthego.networking.RetrofitManager;
import com.example.englishonthego.networking.SearchFeed;
import com.example.englishonthego.networking.HappiApi;
import com.example.englishonthego.networking.Responses;
import com.example.englishonthego.ui.LyricSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LyricSearchAdapter.OnItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String KEY_STATE = "com.example.englishonthego.MainActivity.KEY_STATE";
    final String happi_dev_api_key = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";
    String searchText;

    private HappiApi happiApi;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ActivityMainBinding binding;
    private LyricSearchAdapter mAdapter;
    private List<Responses> responseData = new ArrayList<>();
    private RetrofitManager retrofitManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            binding.searchText.setText(savedInstanceState.getString(KEY_STATE));
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = findViewById(R.id.recycler_view);
        retrofitManager = new RetrofitManager();
        happiApi = retrofitManager.getHappiApi();

        configureListener();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new LyricSearchAdapter(responseData, this, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void configureListener() {
        //Configure search button
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // check if the same search is being requested
                if (binding.searchText.getText().toString().trim().equals(searchText)) {
                    Toast.makeText(getApplicationContext(), "You already have the result for this search", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    searchText = binding.searchText.getText().toString().trim();
                }

                //Hide search button and show progress bar
                binding.searchButton.setVisibility(View.GONE);
                binding.indeterminateProgressBar.setVisibility(View.VISIBLE);

                closeKeyboard();
                getLyricSearch(searchText);
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void getLyricSearch(String searchText) {
        Call<SearchFeed> call = happiApi.getSearch(searchText, happi_dev_api_key);

        call.enqueue(new Callback<SearchFeed>() {
            @Override
            public void onResponse(Call<SearchFeed> call, Response<SearchFeed> feed) {
                if (!feed.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Sorry. Your search was not successful.", Toast.LENGTH_LONG).show();

                    binding.indeterminateProgressBar.setVisibility(View.GONE);
                    binding.searchButton.setVisibility(View.VISIBLE);
                    return;
                }
                SearchFeed feeds = feed.body();
                responseData = feeds.getCallResult();

                binding.indeterminateProgressBar.setVisibility(View.GONE);
                binding.searchButton.setVisibility(View.VISIBLE);

                mAdapter.setItems(responseData);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchFeed> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        searchText = binding.searchText.getText().toString().trim();
        outState.putString(KEY_STATE, searchText);

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemClicked(int position) {
        Responses currentLyricData = responseData.get(position);
        int artistId = currentLyricData.getArtistId();
        int albumId = currentLyricData.getAlbumId();
        int trackId = currentLyricData.getTrackId();

        Intent intent = new Intent(this, LyricsViewerActivity.class);
        intent.putExtra(LyricsViewerActivity.KEY_ARTIST_ID, artistId);
        intent.putExtra(LyricsViewerActivity.KEY_ALBUM_ID, albumId);
        intent.putExtra(LyricsViewerActivity.KEY_TRACK_ID, trackId);

        startActivity(intent);
    }
}

