package com.example.englishonthego;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
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
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.testView);

        initHappiRequest(happiBaseUrl);
//        searchButtonClicked();
        getSearch();
    }

    private void getSearch() {
        Call<ResultResponse> call = happiApi.getSearch("wish you were here, pink floyd", happi_dev_api_key);

        Log.d(TAG, "call url: " + call);
        call.enqueue(new Callback<ResultResponse>() {
            @Override
            public void onResponse(Call<ResultResponse> call, Response<ResultResponse> response) {
                if (!response.isSuccessful()) {
                    textView.setText(response.code() + "");
                    return;
                }
                ResultResponse resultResponse = response.body();
                String content = "";
                content += "" + resultResponse.getTrack() + "\n";

                textView.append(content);
            }

            @Override
            public void onFailure(Call<ResultResponse> call, Throwable t) {
                textView.setText(t.getMessage());
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

