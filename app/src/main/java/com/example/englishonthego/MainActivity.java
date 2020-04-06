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

    //    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG = "TESTTTTTT";
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
            Log.i(TAG, "Loaded instance text: " + savedInstanceState.getString(KEY_STATE));
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
        //When it is clicked, progress bar is shown and button will be invisible
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchButton.setVisibility(View.GONE);
                binding.indeterminateProgressBar.setVisibility(View.VISIBLE);

                closeKeyboard();
                getLyricSearch(binding.searchText.getText().toString().trim());
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

        Log.d(TAG, "call url: " + call);
        call.enqueue(new Callback<SearchFeed>() {
            @Override
            public void onResponse(Call<SearchFeed> call, Response<SearchFeed> feed) {
                if (!feed.isSuccessful()) {

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
        Log.i(TAG, "on Save Instance State: " + searchText);
        outState.putString(KEY_STATE, searchText);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.i(TAG, "on Restore Instance State: " + savedInstanceState.getString(KEY_STATE));
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

        Log.i(TAG, "onItemClicked: artistId: " + artistId + ", albumId: " + albumId + ", trackId: " + trackId);
        startActivity(intent);

        Toast.makeText(this, currentLyricData.getTrackName() + " was clicked", Toast.LENGTH_SHORT).show();
    }

//    public class MyParcelable implements Parcelable {
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        @Override
//        public void writeToParcel(Parcel out, int flags) {
//            out.writeInt(mStateData);
//        }
//
//        public static final Parcelable.Creator<MyParcelable> CREATOR
//                = new Parcelable.Creator<MyParcelable>() {
//            public MyParcelable createFromParcel(Parcel in) {
//                return new MyParcelable(in);
//            }
//
//            @Override
//            public MyParcelable[] newArray(int size) {
//                return new MyParcelable[size];
//            }
//        };
//    }
}

