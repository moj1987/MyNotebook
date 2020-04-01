package com.example.englishonthego;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.englishonthego.databinding.ActivityMainBinding;
import com.example.englishonthego.networking.Feed;
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

public class MainActivity extends AppCompatActivity implements LyricSearchAdapter.onItemClickListener {
    private static final String TAG = "MainActivity";
    final String happiBaseUrl = "https://api.happi.dev/";
    final String happi_dev_api_key = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";
    private HappiApi happiApi;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ActivityMainBinding binding;
    private LyricSearchAdapter mAdapter;
    private List<Responses> responseData = new ArrayList<>();

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

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new LyricSearchAdapter(responseData, this);
        recyclerView.setAdapter(mAdapter);


    }

    private void configureListener() {

        //Configure search button
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearch();
            }
        });
    }

    private void getSearch() {

        String searchText;
        searchText = binding.searchText.getText().toString().trim();

        Call<Feed> call = happiApi.getSearch(searchText, happi_dev_api_key);

        Log.d(TAG, "call url: " + call);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> feed) {
                if (!feed.isSuccessful()) {

                    return;
                }
                Feed feeds = feed.body();
                responseData = feeds.getCallResult();
                mAdapter.setItems(responseData);
                mAdapter.notifyDataSetChanged();
                Log.i(TAG, "onResponse: " + responseData);

            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
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
        responseData.get(position);
        Toast.makeText(this,"position "+responseData.get(position)+" was selected!", Toast.LENGTH_LONG).show();
    }
}

