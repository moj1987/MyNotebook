package com.mynotebook.englishonthego.networking

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LyricModel(
        @SerializedName("track")
        @Expose
        val trackName: String,

        @SerializedName("artist")
        val artistName: String,

        @SerializedName("album")
        val albumName: String,

        @SerializedName("id_track")
        val trackId: Int,

        @SerializedName("id_artist")
        val artistId: Int,

        @SerializedName("id_album")
        val albumId: Int,

        @SerializedName("haslyrics")

        val hasLyric: Boolean,

        @SerializedName("cover")
        val albumCoverUrl: String

) {
}