package com.mynotebook.englishonthego;

import android.content.Context;
import android.content.Intent;
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

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyricsViewerActivity extends AppCompatActivity {

    private static final String KEY_ARTIST_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_ARTIST_ID";
    private static final String KEY_ALBUM_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_ALBUM_ID";
    private static final String KEY_TRACK_ID = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_TRACK_ID";
    private static final String KEY_TRACK_NAME = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_TRACK_NAME";
    private static final String KEY_ALBUM_COVER_URL = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_ALBUM_COVER_URL";
    private static final String KEY_IS_FROM_DB = "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_IS_FROM_DB";
    // Check if the viewer activity should finish after deleting the lyric
    private static final String KEY_SHOULD_FINISH_AFTER_DELETE =
            "com.mynotebook.englishonthego.LyricsViewerActivity.KEY_SHOULD_FINISH_AFTER_DELETE";
    private final String happi_dev_api_key = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";

    private int artistId;
    private int albumId;
    private int trackId;
    private String albumCoverUrl;
    private boolean isFromDb;
    private boolean shouldFinishAfterDelete;

    private LyricViewerViewModel lyricViewerViewModel;
    private HappiApi happiApi;
    private RetrofitManager retrofitManager;
    private Lyric lyricFeedData;

    private TextView textViewLyric;
    private CollapsingToolbarLayout toolbarLayout;
    private ProgressBar intermittentProgressBar;
    private ImageView imageView;

    /**
     * Start this activity with the given parameters.
     *
     * @param artistId                The ID of the artist.
     * @param albumId                 The ID of the album.
     * @param trackId                 The ID of the track.
     * @param trackName               The name of the track.
     * @param albumCoverUrl           The URL for the album cover.
     * @param shouldFinishAfterDelete If true this activity would finish after deleting.
     * @param context                 {@link Context}.
     */
    public static void startActivity(
            int artistId, int albumId, int trackId, String trackName, String albumCoverUrl,
            boolean shouldFinishAfterDelete, Context context) {
        Intent intent = new Intent(context, LyricsViewerActivity.class);

        intent.putExtra(LyricsViewerActivity.KEY_ARTIST_ID, artistId);
        intent.putExtra(LyricsViewerActivity.KEY_ALBUM_ID, albumId);
        intent.putExtra(LyricsViewerActivity.KEY_TRACK_ID, trackId);
        intent.putExtra(LyricsViewerActivity.KEY_TRACK_NAME, trackName);
        intent.putExtra(LyricsViewerActivity.KEY_ALBUM_COVER_URL, albumCoverUrl);
        intent.putExtra(LyricsViewerActivity.KEY_SHOULD_FINISH_AFTER_DELETE, shouldFinishAfterDelete);

        context.startActivity(intent);
    }

    /**
     * Start this activity with the given parameters.
     *
     * @param trackId                 The ID of the track.
     * @param isFromDb                Check if the lyric is being loaded from database.
     * @param shouldFinishAfterDelete If true this activity would finish after deleting.
     * @param context                 {@link Context}.
     */
    public static void startActivity(int trackId, boolean isFromDb, boolean shouldFinishAfterDelete, Context context) {
        Intent intent = new Intent(context, LyricsViewerActivity.class);

        intent.putExtra(LyricsViewerActivity.KEY_TRACK_ID, trackId);
        intent.putExtra(LyricsViewerActivity.KEY_IS_FROM_DB, isFromDb);
        intent.putExtra(LyricsViewerActivity.KEY_SHOULD_FINISH_AFTER_DELETE, shouldFinishAfterDelete);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lyrics_viewer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        textViewLyric = findViewById(R.id.lyric_text_view);
        toolbarLayout = findViewById(R.id.toolbar_layout);
        imageView = findViewById(R.id.toolbar_img_album);
        intermittentProgressBar = findViewById(R.id.indeterminate_progress_bar);

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        retrofitManager = new RetrofitManager();
        happiApi = retrofitManager.getHappiApi();
        initViewModel();

        loadLyric();
    }

    private void loadLyric() {
        Bundle extras = getIntent().getExtras();
        isFromDb = extras != null && extras.getBoolean(LyricsViewerActivity.KEY_IS_FROM_DB);
        shouldFinishAfterDelete = extras != null && extras.getBoolean(LyricsViewerActivity.KEY_SHOULD_FINISH_AFTER_DELETE);
        int id = extras.getInt(LyricsViewerActivity.KEY_TRACK_ID);
        if (id == 0) {
            id = extras.getInt(LyricsViewerActivity.KEY_TRACK_ID);
        }

        lyricViewerViewModel.loadLyric(id);

        lyricViewerViewModel.mLiveLyric.observe(this, lyricSaveModel -> {
            if (lyricSaveModel != null) {
                isFromDb = true;
                intermittentProgressBar.setVisibility(View.INVISIBLE);
                toolbarLayout.setTitle(lyricSaveModel.getTrackName());
                textViewLyric.setText(lyricSaveModel.getLyric());
                Picasso.get().load(lyricSaveModel.getAlbumCoverUrl()).fit().into(imageView);
            } else {
                artistId = extras.getInt(KEY_ARTIST_ID);
                albumId = extras.getInt(KEY_ALBUM_ID);
                trackId = extras.getInt(KEY_TRACK_ID);
                albumCoverUrl = extras.getString(KEY_ALBUM_COVER_URL);

                toolbarLayout.setTitle(extras.getString(KEY_TRACK_NAME));
                Picasso.get().load(albumCoverUrl).fit().into(imageView);

                getLyric();
            }
        });
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
            public void onResponse(@NotNull Call<LyricFeed> call, @NotNull Response<LyricFeed> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                LyricFeed lyricFeed = response.body();
                lyricFeedData = lyricFeed.getLyricResult();
                intermittentProgressBar.setVisibility(View.INVISIBLE);
                textViewLyric.setVisibility(View.VISIBLE);
                textViewLyric.setText(lyricFeedData.getLyrics());
            }

            @Override
            public void onFailure(@NotNull Call<LyricFeed> call, @NotNull Throwable t) {
                String text = "Failed to get the lyrics with message: " + t.getMessage();
                textViewLyric.setText(text);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_lyrics_viewer, menu);
        MenuItem saveItem = menu.findItem(R.id.action_save_lyric);
        saveItem.setVisible(!isFromDb);
        MenuItem deleteItem = menu.findItem(R.id.action_delete_lyric);
        deleteItem.setVisible(isFromDb);
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
                invalidateOptionsMenu();
                isFromDb = true;
                break;

            case R.id.action_delete_lyric:
                deleteLyric();
                if (shouldFinishAfterDelete) {
                    finish();
                } else {
                    Snackbar.make(
                            this.findViewById(R.id.lyric_viewer_coordinator_layout),
                            "Lyric removed from your library.",
                            Snackbar.LENGTH_LONG)
                            .show();
                    isFromDb = false;
                    invalidateOptionsMenu();
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteLyric() {
        lyricViewerViewModel.deleteLyric();
    }

    private void saveLyric() {
        lyricViewerViewModel.saveLyric(trackId,
                lyricFeedData.getTrack(),
                lyricFeedData.getArtist(),
                lyricFeedData.getAlbum(),
                albumCoverUrl,
                lyricFeedData.getLyrics());
    }
}