package com.example.englishonthego.networking;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface HappiApi {

    @GET("v1/music")
    Call<SearchFeed> getSearch(@Query("q") String searchText,
                               @Query("apikey") String api_key);

    @GET("v1/music/artists/{id_artist}/albums/{id_album}/tracks/{id_track}/lyrics")
    Call<LyricFeed> getLyric(
                         @Path("id_artist") int idArtist,
                         @Path("id_album") int idAlbum,
                         @Path("id_track") int idTrack,
                         @Query("apikey") String api_key);
}
