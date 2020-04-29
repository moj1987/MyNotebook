package com.mynotebook.englishonthego;

import android.os.Bundle;

import com.mynotebook.englishonthego.networking.HappiApi;
import com.mynotebook.englishonthego.networking.Lyric;
import com.mynotebook.englishonthego.networking.LyricFeed;
import com.mynotebook.englishonthego.networking.RetrofitManager;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyricsViewerActivity extends AppCompatActivity {

    private static final String TAG = LyricsViewerActivity.class.getSimpleName();

    public static final String KEY_ARTIST_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_ARTIST_ID";
    public static final String KEY_ALBUM_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_ALBUM_ID";
    public static final String KEY_TRACK_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_TRACK_ID";
    public static final String KEY_TRACK_NAME = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_TRACK_NAME";
    private final String happi_dev_api_key = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";

    private int artistId;
    private int albumId;
    private int trackId;

    private HappiApi happiApi;
    private RetrofitManager retrofitManager;
    private Lyric lyricData;

    private TextView textViewLyric;
    private CollapsingToolbarLayout toolbarLayout;
    private ProgressBar intermitentProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textViewLyric = findViewById(R.id.lyric_text_view);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        intermitentProgressBar = findViewById(R.id.indeterminateProgressBar);

        setSupportActionBar(toolbar);

        artistId = getIntent().getIntExtra(KEY_ARTIST_ID, -1);
        albumId = getIntent().getIntExtra(KEY_ALBUM_ID, -1);
        trackId = getIntent().getIntExtra(KEY_TRACK_ID, -1);

        toolbarLayout.setTitle(getIntent().getStringExtra(KEY_TRACK_NAME));

        retrofitManager = new RetrofitManager();
        happiApi = retrofitManager.getHappiApi();

        getLyric();
    }

    private void getLyric() {
        textViewLyric.setVisibility(View.INVISIBLE);
        intermitentProgressBar.setVisibility(View.VISIBLE);

        Call<LyricFeed> call = happiApi.getLyric(artistId, albumId, trackId, happi_dev_api_key);

        call.enqueue(new Callback<LyricFeed>() {
            @Override
            public void onResponse(Call<LyricFeed> call, Response<LyricFeed> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                LyricFeed lyricFeed = response.body();
                lyricData = lyricFeed.getLyricResult();
                intermitentProgressBar.setVisibility(View.INVISIBLE);
                textViewLyric.setVisibility(View.VISIBLE);
                textViewLyric.setText(lyricData.getLyrics());
            }

            @Override
            public void onFailure(Call<LyricFeed> call, Throwable t) {
                textViewLyric.setText("Failed to get the lyrics with message: " + t.getMessage());
            }
        });
    }
}
