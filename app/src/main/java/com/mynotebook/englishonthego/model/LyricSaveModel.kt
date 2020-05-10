package com.mynotebook.englishonthego.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lyricsTable")
data class LyricSaveModel(
        @PrimaryKey
        var id: Int,
        var trackName: String,
        var artistName: String,
        var albumName: String,
        var albumCoverUrl: String,
        var lyric: String) {
    constructor(trackName: String, artistName: String, albumName: String, albumCoverUrl: String, lyric: String)
            : this(0, trackName, artistName, albumName, albumCoverUrl, lyric)
}