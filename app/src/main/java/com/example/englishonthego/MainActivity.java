package com.example.englishonthego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.englishonthego.databinding.ActivityMainBinding;
import com.example.englishonthego.networking.Lyric;
import com.example.englishonthego.networking.LyricFeed;
import com.example.englishonthego.networking.SearchFeed;
import com.example.englishonthego.networking.HappiApi;
import com.example.englishonthego.networking.Responses;
import com.example.englishonthego.ui.LyricSearchAdapter;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LyricSearchAdapter.OnItemClickListener {
    private static final String TAG = "MainActivity";
    final String happiBaseUrl = "https://api.happi.dev/";
    final String happi_dev_api_key = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";
    private HappiApi happiApi;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ActivityMainBinding binding;
    private LyricSearchAdapter mAdapter;
    private List<Responses> responseData = new ArrayList<>();
    private Lyric lyricData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        recyclerView = findViewById(R.id.recycler_view);

        initHappiRequest(happiBaseUrl);
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
                closeKeyboard();
                getSearch();
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

    private void getSearch() {

        String searchText;
        searchText = binding.searchText.getText().toString().trim();

        Call<SearchFeed> call = happiApi.getSearch(searchText, happi_dev_api_key);

        Log.d(TAG, "call url: " + call);
        call.enqueue(new Callback<SearchFeed>() {
            @Override
            public void onResponse(Call<SearchFeed> call, Response<SearchFeed> feed) {
                if (!feed.isSuccessful()) {

                    return;
                }
                SearchFeed feeds = feed.body();
                responseData = feeds.getCallResult();
                mAdapter.setItems(responseData);
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<SearchFeed> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getLyrics(int idArtist, int idAlbum, int idTrack) {

        Call<LyricFeed> call = happiApi.getLyric( idArtist, idAlbum, idTrack,happi_dev_api_key);
        Log.d(TAG, "lyric call url: " + call);
        call.enqueue(new Callback<LyricFeed>() {
            @Override
            public void onResponse(Call<LyricFeed> call, Response<LyricFeed> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                LyricFeed lyricFeed = response.body();
                lyricData = lyricFeed.getLyricResult();
                Log.d(TAG, "lyric: " + lyricData.getLyrics());
            }

            @Override
            public void onFailure(Call<LyricFeed> call, Throwable t) {

            }
        });
    }

    /**
     * initialize Happi api call with retrofit
     * and HttpLogging set up
     *
     * @param baseUrl
     */
    private void initHappiRequest(String baseUrl) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        happiApi = retrofit.create(HappiApi.class);
    }

    @Override
    public void onItemClicked(int position) {
        Responses currentLyricData = responseData.get(position);
        getLyrics(currentLyricData.getArtistId(), currentLyricData.getAlbumId(),currentLyricData.getTrackId());

//        this.getActionBar().setTitle(songName);
        Intent intent = new Intent(this, LyricsViewerActivity.class);
        startActivity(intent);

        Toast.makeText(this, currentLyricData.getTrackName() + " was clicked", Toast.LENGTH_SHORT).show();
    }
}

