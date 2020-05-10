package com.mynotebook.englishonthego;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.snackbar.Snackbar;
import com.mynotebook.englishonthego.networking.HappiApi;
import com.mynotebook.englishonthego.networking.Lyric;
import com.mynotebook.englishonthego.networking.LyricFeed;
import com.mynotebook.englishonthego.networking.RetrofitManager;
import com.mynotebook.englishonthego.viewmodel.LyricViewerViewModel;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyricsViewerActivity extends AppCompatActivity {

    private static final String TAG = LyricsViewerActivity.class.getSimpleName();

    public static final String KEY_ARTIST_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_ARTIST_ID";
    public static final String KEY_ALBUM_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_ALBUM_ID";
    public static final String KEY_TRACK_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_TRACK_ID";
    public static final String KEY_TRACK_NAME = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_TRACK_NAME";
    public static final String KEY_ALBUM_COVER_URL = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_ALBUM_COVER_URL";
    private final String happi_dev_api_key = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";

    private int artistId;
    private int albumId;
    private int trackId;
    private String albumCoverUrl;
    private boolean fromDB;

    private LyricViewerViewModel lyricViewerViewModel;
    private HappiApi happiApi;
    private RetrofitManager retrofitManager;
    private Lyric lyricData;

    private TextView textViewLyric;
    private CollapsingToolbarLayout toolbarLayout;
    private ProgressBar intermittentProgressBar;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textViewLyric = findViewById(R.id.lyric_text_view);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        imageView = findViewById(R.id.toolbar_img_album);
        intermittentProgressBar = findViewById(R.id.indeterminateProgressBar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        artistId = getIntent().getIntExtra(KEY_ARTIST_ID, -1);
        albumId = getIntent().getIntExtra(KEY_ALBUM_ID, -1);
        trackId = getIntent().getIntExtra(KEY_TRACK_ID, -1);
        albumCoverUrl = getIntent().getStringExtra(KEY_ALBUM_COVER_URL);

        toolbarLayout.setTitle(getIntent().getStringExtra(KEY_TRACK_NAME));
        Picasso.get().load(albumCoverUrl).fit().into(imageView);

        retrofitManager = new RetrofitManager();
        happiApi = retrofitManager.getHappiApi();
        initViewModel();
        getLyric();
    }

    private void initViewModel() {
        lyricViewerViewModel = new ViewModelProvider(this).get(LyricViewerViewModel.class);


    }

    private void getLyric() {
        textViewLyric.setVisibility(View.INVISIBLE);
        intermittentProgressBar.setVisibility(View.VISIBLE);

        Call<LyricFeed> call = happiApi.getLyric(artistId, albumId, trackId, happi_dev_api_key);

        call.enqueue(new Callback<LyricFeed>() {
            @Override
            public void onResponse(Call<LyricFeed> call, Response<LyricFeed> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                LyricFeed lyricFeed = response.body();
                lyricData = lyricFeed.getLyricResult();
                intermittentProgressBar.setVisibility(View.INVISIBLE);
                textViewLyric.setVisibility(View.VISIBLE);
                textViewLyric.setText(lyricData.getLyrics());
            }

            @Override
            public void onFailure(Call<LyricFeed> call, Throwable t) {
                textViewLyric.setText("Failed to get the lyrics with message: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_lyrics_viewer, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_save_lyric:
                saveLyric();
                Snackbar.make(
                        this.findViewById(R.id.lyric_viewer_coordinator_layout),
                        "Lyric added to your library.",
                        Snackbar.LENGTH_LONG)
                        .show();
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveLyric() {
        lyricViewerViewModel.saveLyric(lyricData.getId(),
                lyricData.getTrack(),
                lyricData.getArtist(),
                lyricData.getAlbum(),
                albumCoverUrl,
                lyricData.getLyrics());
    }
}
