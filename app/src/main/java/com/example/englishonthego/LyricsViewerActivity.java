package com.example.englishonthego;

import android.os.Bundle;

import com.example.englishonthego.networking.HappiApi;
import com.example.englishonthego.networking.Lyric;
import com.example.englishonthego.networking.LyricFeed;
import com.example.englishonthego.networking.RetrofitManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyricsViewerActivity extends AppCompatActivity {

    //    private static final String TAG = LyricsViewerActivity.class.getSimpleName();
    private static final String TAG = "TESTTTTTT";

    public static final String KEY_ARTIST_ID = "com.example.englishonthego.LyricsViewerActivity.KEY_ARTIST_ID";
    public static final String KEY_ALBUM_ID = "com.example.englishonthego.LyricsViewerActivity.KEY_ALBUM_ID";
    public static final String KEY_TRACK_ID = "com.example.englishonthego.LyricsViewerActivity.KEY_TRACK_ID";

    private final String happi_dev_api_key = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";

    private int artistId;
    private int albumId;
    private int trackId;

    private HappiApi happiApi;
    private RetrofitManager retrofitManager;
    private Lyric lyricData;

    private TextView textViewLyric;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textViewLyric = findViewById(R.id.lyric_text_view);
        toolbarLayout = findViewById(R.id.toolbar_layout);

        artistId = getIntent().getIntExtra(KEY_ARTIST_ID, -1);
        setSupportActionBar(toolbar);
        albumId = getIntent().getIntExtra(KEY_ALBUM_ID, -1);
        trackId = getIntent().getIntExtra(KEY_TRACK_ID, -1);

        retrofitManager = new RetrofitManager();
        happiApi = retrofitManager.getHappiApi();

        getLyric();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "the lyric will be saved in future versions", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getLyric() {
        Call<LyricFeed> call = happiApi.getLyric(artistId, albumId, trackId, happi_dev_api_key);
        Log.d(TAG, "lyric call url: " + call);
        Log.i(TAG, "Finding lyric for: artistId: " + artistId + ", albumId: " + albumId + ", trackId: " + trackId);

        call.enqueue(new Callback<LyricFeed>() {
            @Override
            public void onResponse(Call<LyricFeed> call, Response<LyricFeed> response) {
                if (!response.isSuccessful()) {
                    toolbarLayout.setTitle(trackId + " not found");
                    textViewLyric.setText("Lyrics not found with response code: " + response.code());
                    return;
                }
                LyricFeed lyricFeed = response.body();
                lyricData = lyricFeed.getLyricResult();
                textViewLyric.setText(lyricData.getLyrics());
                toolbarLayout.setTitle(lyricData.getTrack());
                Log.d(TAG, "lyric: " + lyricData.getLyrics());
            }

            @Override
            public void onFailure(Call<LyricFeed> call, Throwable t) {
                toolbarLayout.setTitle(trackId + " failed");
                textViewLyric.setText("Failed to get the lyrics with message: " + t.getMessage());
            }
        });
    }
}
