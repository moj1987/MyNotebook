package com.example.englishonthego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    final String happiBaseUrl = "https://api.happi.dev/";
    final String happi_dev_api_key = "348763zJYkQkKjFckCf6KxwSvAGgcsAgbn6pr0dbEZLFBwv7MXfqclmC";
    private HappiApi happiApi;
    private TextView testView;
    private EditText searchTextInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testView = findViewById(R.id.testView);
        searchTextInput = findViewById(R.id.search_text);

        initHappiRequest(happiBaseUrl);
        configureListener();

//        searchButtonClicked();
//        getSearch();
    }

    private void configureListener() {
//        configure search button
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSearch();
            }
        });
    }

    private void getSearch() {

        String searchText;
        searchText = searchTextInput.getText().toString().trim();

        testView.setText("");

        Call<Feed> call = happiApi.getSearch(searchText, happi_dev_api_key);

        Log.d(TAG, "call url: " + call);
        call.enqueue(new Callback<Feed>() {
            @Override
            public void onResponse(Call<Feed> call, Response<Feed> feed) {
                if (!feed.isSuccessful()) {
                    testView.setText(feed.code() + "");
                    return;
                }
                Feed feeds = feed.body();
                String content = "";
                for (Responses response : feeds.getCallResult()) {
                    content += "" + response.getTrackName()
                            + ", " + response.getArtistName()
                            + ", " + response.getAlbumName()
                            + "\n\n";
                }

//                content += "" + feeds.getCallResult() + "\n";
                testView.append(content);
            }

            @Override
            public void onFailure(Call<Feed> call, Throwable t) {
                testView.setText(t.getMessage());
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
}

