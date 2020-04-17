package com.example.englishonthego.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "vocabsTable")
data class VocabModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var vocab: String?,
        var definition: String?,
        var example: String?) {
    constructor(vocab: String, definition: String, example: String) : this(0, vocab, definition, example)
}

