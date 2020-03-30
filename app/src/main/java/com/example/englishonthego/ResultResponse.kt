package com.example.englishonthego

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultResponse(
        @SerializedName("track")
        @Expose
        val track: String,
                          val artist: String,
                          val album: String,
                          val id_track: Int,
                          val id_artist: Int,
                          val id_album: Int) {
}