package com.example.englishonthego;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface HappiApi {

    @GET("v1/music")
    Call<ResultResponse> getSearch(@Query("q") String searchText,
                                   @Query("apikey") String api_key);

    @GET("v1/music")
    Call<LyricResponse> getLyric(@Query("q") String searchText,
                                 @Query("x-happi-key") String api_key);
}
